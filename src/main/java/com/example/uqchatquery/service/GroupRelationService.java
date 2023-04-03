package com.example.uqchatquery.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.uqchatquery.dao.model.GroupRelation;
import com.example.uqchatquery.dao.model.User;
import com.example.uqchatquery.dto.EnrollGroupParam;
import com.example.uqchatquery.dto.GroupChatDto;
import com.example.uqchatquery.dto.MessageBody;
import com.example.uqchatquery.dto.QueryGroupHistoryMsgParam;

import java.util.List;

public interface GroupRelationService extends IService<GroupRelation> {
    List<GroupChatDto> queryGroups(User user);

    Boolean saveRelation(EnrollGroupParam enrollGroupParam);

    Boolean updateGroupRelation(GroupRelation relation);

}
