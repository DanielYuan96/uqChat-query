package com.example.uqchatquery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.uqchatquery.dao.mapper.GroupRelationMapper;
import com.example.uqchatquery.dao.model.GroupChat;
import com.example.uqchatquery.dao.model.GroupRelation;
import com.example.uqchatquery.dao.model.User;
import com.example.uqchatquery.dto.EnrollGroupParam;
import com.example.uqchatquery.dto.GroupChatDto;
import com.example.uqchatquery.dto.MessageBody;
import com.example.uqchatquery.dto.QueryGroupHistoryMsgParam;
import com.example.uqchatquery.service.GroupChatService;
import com.example.uqchatquery.service.GroupRelationService;
import com.example.uqchatquery.service.MsgRelationService;
import com.example.uqchatquery.service.UserQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GroupRelationServiceImpl extends ServiceImpl<GroupRelationMapper, GroupRelation> implements GroupRelationService {
    @Autowired
    UserQueryService userQueryService;
    @Autowired
    GroupChatService groupChatService;

    @Autowired
    MsgRelationService msgRelationService;

    @Override
    public List<GroupChatDto> queryGroups(User user) {
        QueryWrapper<GroupRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user.getId());
        List<GroupRelation> list = this.list(queryWrapper);

        List<Integer> groupIds = list.stream().map(GroupRelation::getGroupChatId).collect(Collectors.toList());
        //查询每个group的详情
        List<GroupChat> groupChatList = new LinkedList<>();
        if (!CollectionUtils.isEmpty(groupIds)) {
            QueryWrapper<GroupChat> groupChatQueryWrapper = new QueryWrapper<>();
            groupChatQueryWrapper.in("id", groupIds);
            groupChatList = groupChatService.list(groupChatQueryWrapper);
        }

        //用map保存id和group对象的信息
        HashMap<Integer, GroupChat> groupChatHashMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(groupChatList)) {
            for (GroupChat groupChat : groupChatList) {
                groupChatHashMap.put(groupChat.getId(), groupChat);
            }
        }



        //初始化结果集
        ArrayList<GroupChatDto> result = new ArrayList<>();
        //查询每一个groupID里所有用户的信息
        //创建msg_relation的queryWrapper
        if (!ObjectUtils.isEmpty(groupIds)) {
            Integer groupId;
            for (GroupRelation groupRelation : list) {
                groupId = groupRelation.getGroupChatId();
                queryWrapper.clear();
                queryWrapper.eq("group_chat_id", groupRelation.getGroupChatId());
                List<GroupRelation> groupRelations = this.list(queryWrapper);
                List<Long> groupUsers = groupRelations.stream().map(GroupRelation::getUserId).collect(Collectors.toList());
                List<User> users = userQueryService.listUserByUserIds(groupUsers);
                GroupChatDto groupChatDto = new GroupChatDto();
                groupChatDto.setGroupId(groupId);
                groupChatDto.setMembers(users);
                //TODO 后续设置groupName
                groupChatDto.setGroupAvatar(groupChatHashMap.get(groupId).getAvatar());
                groupChatDto.setGroupName(groupChatHashMap.get(groupId).getCourseCode());
                groupChatDto.setGroupBackground(groupChatHashMap.get(groupId).getBackground());
                groupChatDto.setGroupIntro(groupChatHashMap.get(groupId).getIntro());
                //从msg_relation中获取每个group的notification数量
                Integer msgNums = msgRelationService.queryGroupUnreadMsg(groupRelation);
                groupChatDto.setNotification(msgNums);
                result.add(groupChatDto);
            }
            return result;
        }
        return Collections.emptyList();
    }

    @Override
    public Boolean saveRelation(EnrollGroupParam enrollGroupParam) {
        GroupRelation groupRelation = new GroupRelation();
        groupRelation.setUserId(enrollGroupParam.getUserId());
        groupRelation.setGroupChatId(enrollGroupParam.getGroupId());
        groupRelation.setUserLastAckMsgTime(System.currentTimeMillis());
        //TODO 调用chat服务 利用ws将新用户的详细信息推给该group中当前在线的成员
        return this.save(groupRelation);
    }

    @Override
    public Boolean updateGroupRelation(GroupRelation relation) {
        UpdateWrapper<GroupRelation> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("group_chat_id", relation.getGroupChatId());
        updateWrapper.eq("user_id", relation.getUserId());
        return this.update(relation, updateWrapper);
    }



}
