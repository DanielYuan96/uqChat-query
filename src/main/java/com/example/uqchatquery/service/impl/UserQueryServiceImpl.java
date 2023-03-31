package com.example.uqchatquery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.uqchatquery.dao.mapper.UserMapper;
import com.example.uqchatquery.dao.mapper.UserRelationMapper;
import com.example.uqchatquery.dao.model.User;
import com.example.uqchatquery.dao.model.UserRelation;
import com.example.uqchatquery.dto.RelationRequest;
import com.example.uqchatquery.service.UserQueryService;
import com.example.uqchatquery.vo.RespResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserQueryServiceImpl implements UserQueryService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRelationMapper userRelationMapper;


    @Override
    public RespResult<User> queryOneUser(User user) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", user.getAccount());
        queryWrapper.eq("password", user.getPassword());
        User data = userMapper.selectOne(queryWrapper);
        RespResult<User> result = new RespResult<>();
        result.setData(data);
        result.setRespCode("51");
        result.setRespMsg("success");
        return result;
    }

    @Override
    public RespResult<Integer> insertUser(User user) {
        Integer affectedRows = userMapper.insert(user);
        RespResult<Integer> result = new RespResult<>();
        result.setData(affectedRows);
        result.setRespCode("51");
        result.setRespMsg("success");
        return result;
    }

    @Override
    public List<UserRelation> queryRelation(User user) {
        QueryWrapper<UserRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user.getId());
        List<UserRelation> userRelations = userRelationMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(userRelations)) {
            return Collections.emptyList();
        }
        return userRelations;
    }

    @Override
    public List<User> queryFriendsInfo(List<Long> ids) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", ids);
        List<User> users = userMapper.selectList(queryWrapper);
        log.info("queryFriendsInfo方法检索出来的结果为：{}", users);
        return users;
    }

    @Override
    public boolean updateUserAvatar(User user) {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", user.getId());
        int rows = userMapper.update(user, updateWrapper);
        return rows == 1;
    }

    @Override
    public Integer updateUserInfo(User user) {
        if (ObjectUtils.isEmpty(user.getPassword())) {
            UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", user.getId());
            return userMapper.update(user, updateWrapper);
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", user.getId());
        User res = userMapper.selectOne(queryWrapper);
        if (!res.getPassword().equals(user.getPassword())) {
            return 2;
        } else {
            UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", user.getId());
            return userMapper.update(user, updateWrapper);
        }

    }

    @Override
    public List<User> listUserByUserIds(List<Long> ids) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.in("id", ids);
        List<User> users = userMapper.selectList(userQueryWrapper);
        return users;
    }

    @Override
    public User queryUserInfoById(Long userId) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("id", userId);
        return userMapper.selectOne(userQueryWrapper);
    }


}
