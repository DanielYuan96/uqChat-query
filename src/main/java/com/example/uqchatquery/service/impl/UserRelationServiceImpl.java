package com.example.uqchatquery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.uqchatquery.dao.mapper.UserRelationMapper;
import com.example.uqchatquery.dao.model.UserRelation;
import com.example.uqchatquery.dto.RelationRequest;
import com.example.uqchatquery.enums.FriendShipEnum;
import com.example.uqchatquery.service.UserRelationService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class UserRelationServiceImpl extends ServiceImpl<UserRelationMapper, UserRelation> implements UserRelationService {
    @Override
    public boolean handleRelation(RelationRequest request) {
        ArrayList<UserRelation> userRelations = new ArrayList<>();
        UserRelation userRelation = new UserRelation();
        userRelation.setUserId(request.getSendId());
        userRelation.setFriendId(request.getReceiverId());
        //如果接收方拒绝了好友请求即action值为2，则删除数据库中记录的双方关系，
        if (!ObjectUtils.isEmpty(request.getAction()) && request.getAction().equals(2)) {
            QueryWrapper<UserRelation> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.eq("user_id", request.getSendId());
            deleteWrapper.eq("friend_id", request.getReceiverId());
            int affectedRow = baseMapper.delete(deleteWrapper);
            return affectedRow == 1;
        }
        //如果接收方同意了好友请求，则双方互为好友 关系为 friend_created
        if (request.getFriendShip().equals(FriendShipEnum.FRIEND_CREATED.getDes())) {
            //将好友关系表中发送方跟接收方的关系从wait改为friend
            UpdateWrapper<UserRelation> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("friendship", FriendShipEnum.FRIEND_CREATED.getDes());
            updateWrapper.eq("user_id", request.getSendId());
            updateWrapper.eq("friend_id", request.getReceiverId());
            this.update(updateWrapper);
            //新增接收方和发送方的关系为friend
            //判断接收方和发送方的关系是否已经存在
            QueryWrapper<UserRelation> friendQueryWrapper = new QueryWrapper<>();
            friendQueryWrapper.eq("user_id", request.getReceiverId());
            friendQueryWrapper.eq("friend_id", request.getSendId());
            UserRelation relation = this.getOne(friendQueryWrapper);
            //如果不存在新增该关系
            if (ObjectUtils.isEmpty(relation)) {
                UserRelation userRelation1 = new UserRelation();
                userRelation1.setUserId(request.getReceiverId());
                userRelation1.setFriendId(request.getSendId());
                userRelation1.setFriendship(FriendShipEnum.FRIEND_CREATED.getDes());
                userRelations.add(userRelation1);
                return this.saveBatch(userRelations);
            }
            //如果存在判断关系是否为friend 如果不是更新该条记录
            if (!relation.getFriendship().equals(FriendShipEnum.FRIEND_CREATED.getDes())) {
                updateWrapper.set("friendship", FriendShipEnum.FRIEND_CREATED.getDes());
                updateWrapper.eq("user_id", request.getReceiverId());
                updateWrapper.eq("friend_id", request.getSendId());
                return this.update(updateWrapper);
            }

        }
        //否则只保存发送方跟接收方的关系 即接收方无法感知好友状态
        //判断发送方跟接收方的关系是否已经存在
        QueryWrapper<UserRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", request.getSendId());
        queryWrapper.eq("friend_id", request.getReceiverId());
        boolean isExist  = !ObjectUtils.isEmpty(this.getOne(queryWrapper));
        //如果存在直接返回
        if (isExist) {
            return true;
        }
        userRelation.setFriendship(FriendShipEnum.FRIEND_WAIT.getDes());
        //如果是wait状态调换发送方接收方接受关系---eg. 如果发送方为4 接收方为6 存到数据库中为6，4，wait
        Long userId = userRelation.getUserId();
        userRelation.setUserId(userRelation.getFriendId());
        userRelation.setFriendId(userId);
        userRelations.add(userRelation);

        return this.saveBatch(userRelations);
    }

    @Override
    public boolean changeRelation(RelationRequest request) {
        UpdateWrapper<UserRelation> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("friendship", FriendShipEnum.FRIEND_CREATED.getDes());
        updateWrapper.eq("user_id", request.getSendId());
        updateWrapper.eq("friend_id", request.getReceiverId());
        return this.update(updateWrapper);
    }

//    @Override
//    public List<UserRelation> queryRelation(RelationRequest request) {
//        QueryWrapper<UserRelation> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("user_id", request.getSendId());
//        queryWrapper.eq("friend_id", request.getReceiverId());
//        this.
//        return null;
//    }
}
