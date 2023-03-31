package com.example.uqchatquery.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.uqchatquery.dao.model.MsgHistory;
import com.example.uqchatquery.dao.model.User;
import com.example.uqchatquery.dto.MessageBody;
import com.example.uqchatquery.dto.QueryHistoryMsgParam;
import com.example.uqchatquery.dto.QueryUnReadMsgParam;

import java.util.List;
import java.util.Map;

public interface MsgHistoryService extends IService<MsgHistory> {

    List<MessageBody> queryHistoryMsg(QueryHistoryMsgParam queryParam);

    Long saveHistoryMsg(MessageBody messageBody);

    Map<Long, Integer> queryUnreadMsg(QueryUnReadMsgParam queryParam);

    Boolean batchSaveHistoryMsg(List<MessageBody> messageBodyList);
}
