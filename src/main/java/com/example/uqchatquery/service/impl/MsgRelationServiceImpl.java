package com.example.uqchatquery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.uqchatquery.dao.mapper.MsgRelationMapper;
import com.example.uqchatquery.dao.model.GroupRelation;
import com.example.uqchatquery.dao.model.MsgHistory;
import com.example.uqchatquery.dao.model.MsgRelation;
import com.example.uqchatquery.dto.MessageBody;
import com.example.uqchatquery.dto.QueryGroupHistoryMsgParam;
import com.example.uqchatquery.dto.QueryHistoryMsgParam;
import com.example.uqchatquery.dto.QueryUnReadMsgParam;
import com.example.uqchatquery.enums.MsgReadedEnum;
import com.example.uqchatquery.service.MsgHistoryService;
import com.example.uqchatquery.service.MsgRelationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MsgRelationServiceImpl extends ServiceImpl<MsgRelationMapper, MsgRelation> implements MsgRelationService {

    @Autowired
    MsgHistoryService msgHistoryService;

    @Override
    public Long saveMsgRelation(MessageBody messageBody, MsgHistory msgHistory) {
        MsgRelation msgRelation = new MsgRelation();
        msgRelation.setSendId(messageBody.getSendId());
        msgRelation.setReceiverId(messageBody.getReceiverId());
        msgRelation.setMsgId(msgHistory.getId());
        msgRelation.setReaded(messageBody.getReaded());
        msgRelation.setIsGroupMsg(messageBody.getIsGroupMsg());
        msgRelation.setGroupChatId(messageBody.getGroupChatId());
        msgRelation.setType(msgHistory.getType());
        msgRelation.setSendTime(msgHistory.getSendTime());
        baseMapper.insert(msgRelation);
        return msgRelation.getId();
    }

    @Override
    public Boolean batchSaveMsgRelation(List<MessageBody> messageBodyList, MsgHistory msgHistory) {
        ArrayList<MsgRelation> msgRelations = new ArrayList<>();
        for (MessageBody messageBody : messageBodyList) {
            MsgRelation msgRelation = new MsgRelation();
            msgRelation.setSendId(messageBody.getSendId());
            msgRelation.setReceiverId(messageBody.getReceiverId());
            msgRelation.setMsgId(msgHistory.getId());
            msgRelation.setReaded(messageBody.getReaded());
            msgRelation.setIsGroupMsg(messageBody.getIsGroupMsg());
            msgRelation.setGroupChatId(messageBody.getGroupChatId());
            msgRelation.setType(msgHistory.getType());
            msgRelation.setSendTime(msgHistory.getSendTime());
            msgRelations.add(msgRelation);
        }
        return this.saveBatch(msgRelations);
    }

    @Override
    public List<MessageBody> queryGroupHistoryMsg(QueryGroupHistoryMsgParam queryParam) {
        QueryWrapper<MsgRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("group_chat_id", queryParam.getGroupId());
        queryWrapper.orderByDesc("group_chat_id");
        queryWrapper.last("LIMIT 20");
        List<MsgRelation> msgRelationList = this.list(queryWrapper);
        //生成map key为msgId value为msgRelation
        HashMap<Long, MsgRelation> msgRelationMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(msgRelationList)) {
            for (MsgRelation msgRelation : msgRelationList) {
                msgRelationMap.put(msgRelation.getMsgId(), msgRelation);
            }
        }
        //创建结果集
        ArrayList<MessageBody> result = new ArrayList<>(msgRelationList.size());

        List<Long> msgIds = msgRelationList.stream().map(MsgRelation::getMsgId).collect(Collectors.toList());
        QueryWrapper<MsgHistory> historyQueryWrapper = new QueryWrapper<>();
        historyQueryWrapper.in("id", msgIds);
        List<MsgHistory> msgHistoryList = msgHistoryService.list(historyQueryWrapper);
        if (!CollectionUtils.isEmpty(msgHistoryList)) {
            for (MsgHistory msgHistory : msgHistoryList) {
                MessageBody messageBody = new MessageBody();
                messageBody.setMessageId(msgHistory.getMessageId());
                messageBody.setMessage(msgHistory.getMessage());
                messageBody.setSendTime(msgHistory.getSendTime().getTime());
                //从之前的map中获取消息关系数据
                MsgRelation msgRelation = msgRelationMap.get(msgHistory.getId());
                messageBody.setIsGroupMsg(msgRelation.getIsGroupMsg());
                messageBody.setSendId(msgRelation.getSendId());
                messageBody.setGroupChatId(msgRelation.getGroupChatId());
                result.add(messageBody);
            }
        }
        return result;
    }

    @Override
    public Map<Long, Integer> queryUnreadMsg(QueryUnReadMsgParam queryParam) {
        QueryWrapper<MsgRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("send_id", "receiver_id", "COUNT(*) AS nums");
        queryWrapper.in("send_id", queryParam.getSendId());
        queryWrapper.eq("receiver_id", queryParam.getReceiverId());
        queryWrapper.eq("readed", queryParam.getReaded());
        queryWrapper.eq("type", queryParam.getType());
        //1表示不是群聊消息
        queryWrapper.eq("is_group_msg", 1);
        queryWrapper.groupBy("send_id");
        List<Map<String, Object>> maps = baseMapper.selectMaps(queryWrapper);
        Map<Long, Integer> result = new HashMap<>();
        if (!ObjectUtils.isEmpty(maps)) {
            for (Map<String, Object> item : maps) {
//                HashMap<Long, Integer> map = new HashMap<>();
                result.put(Long.parseLong(item.get("send_id").toString()),
                        Integer.valueOf(item.get("nums").toString()));
            }
        }
        return result;
    }

    @Override
    public Integer queryGroupUnreadMsg(GroupRelation groupRelation) {
        QueryWrapper<MsgRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("group_chat_id", groupRelation.getGroupChatId());
//        queryWrapper.eq("receiver_id", id);
        queryWrapper.ge("send_time", groupRelation.getUserLastAckMsgTime());
//        queryWrapper.eq("readed", MsgReadedEnum.UN_READED.getCode());
        List<MsgRelation> list = this.list(queryWrapper);
        return list.size();
    }

    @Override
    public Boolean updateBatch(Collection<MsgRelation> entityList) {
        return updateBatch(entityList,1000);
    }




    public boolean updateBatch(Collection<MsgRelation> entityList, int batchSize) {
        Assert.notEmpty(entityList, "error: entityList must not be empty");
        String sqlStatement = sqlStatement(SqlMethod.UPDATE);
        try (SqlSession batchSqlSession = sqlSessionBatch()) {
            int i = 0;
            for (MsgRelation anEntityList : entityList) {
                MapperMethod.ParamMap<Object> param = new MapperMethod.ParamMap<>();
                param.put(Constants.ENTITY, anEntityList);
                param.put(Constants.WRAPPER, getQueryWrapper(anEntityList.getSendId(),anEntityList.getReceiverId(), anEntityList.getMsgId()));
                batchSqlSession.update(sqlStatement, param);
                if (i >= 1 && i % batchSize == 0) {
                    batchSqlSession.flushStatements();
                }
                i++;
            }
            batchSqlSession.flushStatements();
        }
        return true;
    }

    private LambdaQueryWrapper<MsgRelation> getQueryWrapper(Long cond1, Long cond2, Long cond3) {
        final LambdaQueryWrapper<MsgRelation> queryWrapper = new QueryWrapper<MsgRelation>().lambda();
        queryWrapper.eq(MsgRelation::getSendId,cond1);
        queryWrapper.eq(MsgRelation::getReceiverId,cond2);
        queryWrapper.eq(MsgRelation::getMsgId, cond3);
        queryWrapper.eq(MsgRelation::getReaded, 2);
        return queryWrapper;
    }


}
