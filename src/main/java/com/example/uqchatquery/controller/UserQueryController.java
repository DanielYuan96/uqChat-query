package com.example.uqchatquery.controller;

import com.example.uqchatquery.dao.model.User;
import com.example.uqchatquery.dao.model.UserRelation;
import com.example.uqchatquery.dto.*;
import com.example.uqchatquery.service.*;
import com.example.uqchatquery.vo.RespResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserQueryController {

    @Autowired
    UserQueryService userQueryService;

    @Autowired
    UserRelationService userRelationService;

    @Autowired
    OfflineMsgService offlineMsgService;

    @Autowired
    MsgHistoryService msgHistoryService;

    @Autowired
    GroupRelationService groupRelationService;

    @PostMapping("/verifyLogin")
    public RespResult<User> verifyLogin(@RequestBody User user) {
        return userQueryService.queryOneUser(user);
    }

    @PostMapping("/insertUser")
    public RespResult<Integer> insertUser(@RequestBody User user) {
        return userQueryService.insertUser(user);
    }

    @PostMapping("/updateUserAvatar")
    public boolean updateUserAvatar(@RequestBody User user) {
        return userQueryService.updateUserAvatar(user);
    }

    @PostMapping("/updateUserInfo")
    public Integer updateUserInfo(@RequestBody User user) {
        return userQueryService.updateUserInfo(user);
    }

    @PostMapping("/chat/queryRelation")
    public List<UserRelation> queryRelation(@RequestBody User user) {
        return userQueryService.queryRelation(user);
    }
    @PostMapping("/chat/queryUserInfo")
    public User queryUserInfoById(@RequestBody Long userId) {
        return userQueryService.queryUserInfoById(userId);
    }
    @PostMapping("/chat/queryFriendsInfo")
    public List<User> queryFriendsInfo(@RequestBody List<Long> ids) {
        return userQueryService.queryFriendsInfo(ids);
    }
    @PostMapping("/chat/queryGroups")
    public List<GroupChatDto> queryGroups(@RequestBody User user) {
        return groupRelationService.queryGroups(user);
    }

    @PostMapping("/relation/handleRelation")
    public boolean insertRelation(@RequestBody RelationRequest request) {
        log.info("添加好友的请求为：{}", request);
        boolean result = userRelationService.handleRelation(request);
        return result;
    }

    @PostMapping("/relation/changeRelation")
    public boolean changeRelation(@RequestBody RelationRequest request) {
        return userRelationService.changeRelation(request);
    }

//    @PostMapping("/relation/queryRelation")
//    public List<UserRelation> queryRelation(@RequestBody RelationRequest request) {
//        return userRelationService.queryRelation(request);
//    }

    @PostMapping("/chat/saveOffLineMsg")
    public boolean saveOffLineMsg(@RequestBody MessageBody messageBody) {
        return offlineMsgService.saveOffLineMsg(messageBody);
    }

    @PostMapping("/chat/queryOffLineMsg")
    public List<MessageBody> queryOffLineMsg(@RequestBody User user) {
        return offlineMsgService.queryOffLineMsg(user);
    }

    @PostMapping("/chat/saveHistoryMsg")
    public Long saveHistoryMsg(@RequestBody MessageBody messageBody) {
        return msgHistoryService.saveHistoryMsg(messageBody);
    }

    @PostMapping("/chat/batchSaveHistoryMsg")
    public Boolean batchSaveHistoryMsg(@RequestBody List<MessageBody> messageBodyList) {
        return msgHistoryService.batchSaveHistoryMsg(messageBodyList);
    }

    @PostMapping("/chat/queryHistoryMsg")
    public List<MessageBody> queryHistoryMsg(@RequestBody QueryHistoryMsgParam queryParam) {
        return msgHistoryService.queryHistoryMsg(queryParam);
    }

    @PostMapping("/chat/queryUnreadMsg")
    public Map<Long, Integer> queryUnreadMsg(@RequestBody QueryUnReadMsgParam queryParam) {
        return msgHistoryService.queryUnreadMsg(queryParam);
    }
}
