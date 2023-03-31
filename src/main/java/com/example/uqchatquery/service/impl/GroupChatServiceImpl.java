package com.example.uqchatquery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.uqchatquery.dao.mapper.GroupChatMapper;
import com.example.uqchatquery.dao.model.GroupChat;
import com.example.uqchatquery.dao.model.GroupRelation;
import com.example.uqchatquery.dto.GroupChatDto;
import com.example.uqchatquery.dto.GroupChatPageQueryParam;
import com.example.uqchatquery.service.GroupChatService;
import com.example.uqchatquery.service.GroupRelationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GroupChatServiceImpl extends ServiceImpl<GroupChatMapper, GroupChat> implements GroupChatService {

    @Autowired
    GroupRelationService groupRelationService;

    @Override
    public List<GroupChatDto> queryGroupsByCondition(GroupChatPageQueryParam queryParam) {
        Page<GroupChat> groupChatPage = new Page<>(queryParam.getPageNum(), queryParam.getPageSize());
        QueryWrapper<GroupChat> queryWrapper = new QueryWrapper<>();
        if (!ObjectUtils.isEmpty(queryParam.getCategoryId())) {
            queryWrapper.eq("group_category_id", queryParam.getCategoryId());
        }
        if (!ObjectUtils.isEmpty(queryParam.getCourseCode())) {
            queryWrapper.eq("course_code", queryParam.getCourseCode());
        }
        List<GroupChat> records = baseMapper.selectPage(groupChatPage, queryWrapper).getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return Collections.emptyList();
        }
        //查询每个group中的成员数量
        QueryWrapper<GroupRelation> relationQueryWrapper = new QueryWrapper<>();
        relationQueryWrapper.select("group_chat_id", "COUNT(*) AS nums");
        List<Integer> groupIdList = records.stream().map(GroupChat::getId).collect(Collectors.toList());
        relationQueryWrapper.in("group_chat_id", groupIdList);
        relationQueryWrapper.groupBy("group_chat_id");
        List<Map<String, Object>> maps = groupRelationService.listMaps(relationQueryWrapper);
        HashMap<Integer, Integer> membersNumMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(maps)) {
            for (Map<String, Object> map : maps) {
                membersNumMap.put(Integer.parseInt(map.get("group_chat_id").toString()),
                        Integer.parseInt(map.get("nums").toString()));
            }
        }
        ArrayList<GroupChatDto> result = new ArrayList<>(records.size());
        for (GroupChat record : records) {
            GroupChatDto groupChatDto = new GroupChatDto();
            groupChatDto.setGroupName(record.getCourseCode());
            groupChatDto.setGroupIntro(record.getIntro());
            groupChatDto.setGroupId(record.getId());
            groupChatDto.setGroupBackground(record.getBackground());
            groupChatDto.setGroupAvatar(record.getAvatar());
            groupChatDto.setCategory(record.getGroupCategoryId());
            groupChatDto.setMembersNum(membersNumMap.get(record.getId()));
            result.add(groupChatDto);
        }
        return result;
    }
}
