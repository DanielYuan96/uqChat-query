package com.example.uqchatquery.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.uqchatquery.dao.model.GroupRelation;
import com.example.uqchatquery.dao.model.MsgHistory;
import com.example.uqchatquery.dao.model.MsgRelation;
import com.example.uqchatquery.dto.MessageBody;
import com.example.uqchatquery.dto.QueryGroupHistoryMsgParam;
import com.example.uqchatquery.dto.QueryHistoryMsgParam;
import com.example.uqchatquery.dto.QueryUnReadMsgParam;


import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface MsgRelationService extends IService<MsgRelation> {
    Long saveMsgRelation(MessageBody messageBody, MsgHistory msgHistory);


    Map<Long, Integer> queryUnreadMsg(QueryUnReadMsgParam queryParam);

    Integer queryGroupUnreadMsg(GroupRelation groupRelation);

    Boolean updateBatch(Collection<MsgRelation> entityList);

    Boolean batchSaveMsgRelation(List<MessageBody> messageBodyList, MsgHistory msgHistory);

    List<MessageBody> queryGroupHistoryMsg(QueryGroupHistoryMsgParam queryParam);
}
