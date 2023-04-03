package com.example.uqchatquery.controller;

import com.example.uqchatquery.dao.model.GroupCategory;
import com.example.uqchatquery.dao.model.GroupRelation;
import com.example.uqchatquery.dto.*;
import com.example.uqchatquery.service.GroupCategoryService;
import com.example.uqchatquery.service.GroupChatService;
import com.example.uqchatquery.service.GroupRelationService;
import com.example.uqchatquery.service.MsgRelationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/group")
@Slf4j
public class GroupQueryController {

    @Autowired
    GroupChatService groupChatService;
    @Autowired
    GroupRelationService groupRelationService;

    @Autowired
    GroupCategoryService groupCategoryService;

    @Autowired
    MsgRelationService msgRelationService;

    @PostMapping("/queryGroupsByCondition")
    List<GroupChatDto> queryGroupsByCondition(@RequestBody GroupChatPageQueryParam queryParam) {
        return groupChatService.queryGroupsByCondition(queryParam);
    }

    @PostMapping("/enrollGroup")
    Boolean enrollGroup(@RequestBody EnrollGroupParam enrollGroupParam) {
        return groupRelationService.saveRelation(enrollGroupParam);
    }

    @PostMapping("/queryCategory")
    List<GroupCategory> queryGroupCategory() {
        return groupCategoryService.list();
    }

    @PostMapping("/updateGroupRelation")
    Boolean updateGroupRelation(@RequestBody GroupRelation groupRelation) {
        return groupRelationService.updateGroupRelation(groupRelation);
    }

    @PostMapping("/queryGroupHistoryMsg")
    List<MessageBody> queryGroupHistoryMsg(QueryGroupHistoryMsgParam queryParam) {
        return msgRelationService.queryGroupHistoryMsg(queryParam);
    }



}
