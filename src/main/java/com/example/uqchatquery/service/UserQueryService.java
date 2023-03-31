package com.example.uqchatquery.service;

import com.example.uqchatquery.dao.model.User;
import com.example.uqchatquery.dao.model.UserRelation;
import com.example.uqchatquery.dto.RelationRequest;
import com.example.uqchatquery.vo.RespResult;

import java.util.List;

public interface UserQueryService {

    RespResult<User> queryOneUser(User user);

    RespResult<Integer> insertUser(User user);

    List<UserRelation> queryRelation(User user);

    List<User> queryFriendsInfo(List<Long> ids);

    boolean updateUserAvatar(User user);

    Integer updateUserInfo(User user);

    List<User> listUserByUserIds(List<Long> ids);

    User queryUserInfoById(Long userId);
}
