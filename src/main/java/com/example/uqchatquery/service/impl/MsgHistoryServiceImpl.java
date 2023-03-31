package com.example.uqchatquery.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.uqchatquery.dao.mapper.MsgHistoryMapper;
import com.example.uqchatquery.dao.model.MsgHistory;
import com.example.uqchatquery.dao.model.MsgRelation;
import com.example.uqchatquery.dto.HistoryMsgDto;
import com.example.uqchatquery.dto.MessageBody;
import com.example.uqchatquery.dto.QueryHistoryMsgParam;
import com.example.uqchatquery.dto.QueryUnReadMsgParam;
import com.example.uqchatquery.service.MsgHistoryService;
import com.example.uqchatquery.service.MsgRelationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MsgHistoryServiceImpl extends ServiceImpl<MsgHistoryMapper, MsgHistory> implements MsgHistoryService {

    @Autowired
    MsgRelationService msgRelationService;

    @Override
    @Transactional
    public Long saveHistoryMsg(MessageBody messageBody) {
        MsgHistory msgHistory = new MsgHistory();
//        msgHistory.setSendId(messageBody.getSendId());
//        msgHistory.setReceiverId(messageBody.getReceiverId());
        msgHistory.setSendTime(new Timestamp(messageBody.getSendTime()));
        msgHistory.setMessage(messageBody.getMessage());
        msgHistory.setMessageId(messageBody.getMessageId());
//        msgHistory.setReaded(messageBody.getReaded());
        //TODO 暂且讲type写死后续需要重构
        msgHistory.setType("chatMessage");
        baseMapper.insert(msgHistory);
        //插入消息关系记录
        Long result = msgRelationService.saveMsgRelation(messageBody, msgHistory);
        return result;
    }
    @Override
    public Boolean batchSaveHistoryMsg(List<MessageBody> messageBodyList) {
        MsgHistory msgHistory = new MsgHistory();
        msgHistory.setSendTime(new Timestamp(messageBodyList.get(0).getSendTime()));
        msgHistory.setMessage(messageBodyList.get(0).getMessage());
        msgHistory.setMessageId(messageBodyList.get(0).getMessageId());
        msgHistory.setType("chatMessage");
        baseMapper.insert(msgHistory);
        //批量插入消息关系记录
        return msgRelationService.batchSaveMsgRelation(messageBodyList, msgHistory);
    }

    @Override
    public Map<Long, Integer> queryUnreadMsg(QueryUnReadMsgParam queryParam) {
//        QueryWrapper<MsgHistory> queryWrapper = new QueryWrapper<>();
//        queryWrapper.select("send_id", "receiver_id", "COUNT(*)");
//        queryWrapper.in("send_id", queryParam.getSendId());
//        queryWrapper.eq("receiver_id", queryParam.getReceiverId());
//        queryWrapper.eq("readed", queryParam.getReaded());
//        queryWrapper.eq("type", queryParam.getType());
//        queryWrapper.groupBy("send_id");
        Map<Long, Integer> maps = msgRelationService.queryUnreadMsg(queryParam);
        return maps;
    }



    @Override
    public List<MessageBody> queryHistoryMsg(QueryHistoryMsgParam queryParam) {
        Long startTime = System.currentTimeMillis();
        QueryWrapper<MsgRelation> queryWrapper = new QueryWrapper<>();
        //A->B发消息
        //先查询B->A发的消息
        queryWrapper.eq("send_id", queryParam.getReceiverId());
        queryWrapper.eq("receiver_id", queryParam.getSendId());
        //查询B-A的未读数据2
//        queryWrapper.eq("readed", 2);
        queryWrapper.le("send_time", queryParam.getSendTime());
        queryWrapper.orderByDesc("send_time");
        queryWrapper.eq("type", queryParam.getType());
        queryWrapper.last("limit 20");
        //B->A的最近20条消息
        List<MsgRelation> list = msgRelationService.list(queryWrapper);


        //将未读消息置为已读
        if (!list.isEmpty()) {
            LinkedList<MsgRelation> updateList = new LinkedList<>();
            for (MsgRelation msg : list) {
                //将B->A的未读消息标记为已读 因为A->B发消息 A势必打开了B的对话框
                if (msg.getReaded() == 2) {
                    msg.setReaded(1);
                    updateList.add(msg);
                }
            }
            if (!updateList.isEmpty()) {
                msgRelationService.updateBatch(updateList);
            }
        }
        //查询A->B的消息
        QueryWrapper<MsgRelation> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("send_id", queryParam.getSendId());
        queryWrapper1.eq("receiver_id", queryParam.getReceiverId());
        queryWrapper1.le("send_time", queryParam.getSendTime());
        queryWrapper1.orderByDesc("send_time");
        queryWrapper1.eq("type", queryParam.getType());
        queryWrapper1.last("limit 20");
        //A->B的最近20条消息
        List<MsgRelation> list1 = msgRelationService.list(queryWrapper1);
        //从两个消息合集中选取最近的20条消息返回出去
        list.addAll(list1);
        if (list.isEmpty()) {
            return Collections.emptyList();
        }
        List<MessageBody> result = new LinkedList<>();
        for (MsgRelation msgRelation : list) {
            MessageBody messageBody = new MessageBody();
            messageBody.setMsgId(msgRelation.getMsgId());
            messageBody.setSendId(msgRelation.getSendId());
            messageBody.setReceiverId(msgRelation.getReceiverId());
            messageBody.setSendTime(msgRelation.getSendTime().getTime());
            result.add(messageBody);
        }
        List<Long> msgIds = list.stream().map(MsgRelation::getMsgId).collect(Collectors.toList());
        QueryWrapper<MsgHistory> msgHistoryQueryWrapper = new QueryWrapper<>();
        msgHistoryQueryWrapper.in("id", msgIds);
        List<MsgHistory> msgHistoryList = this.list(msgHistoryQueryWrapper);
        HashMap<Long, MsgHistory> msgHistoryHashMap = new HashMap<>();
        for (MsgHistory msgHistory : msgHistoryList) {
            msgHistoryHashMap.put(msgHistory.getId(), msgHistory);
        }
        for (MessageBody messageBody : result) {
            messageBody.setMessage(msgHistoryHashMap.get(messageBody.getMsgId()).getMessage());
            messageBody.setMessageId(msgHistoryHashMap.get(messageBody.getMsgId()).getMessageId());
        }

        result.sort(new Comparator<MessageBody>() {
            @Override
            public int compare(MessageBody o1, MessageBody o2) {
                return (int) (o2.getSendTime() - o1.getSendTime()) / 100;
            }
        });
        if (result.size() >= 20) {
            result = result.subList(0, 20);
        } else {
            result = result.subList(0, result.size());
        }
        Long timeCost = startTime - System.currentTimeMillis();
        log.info("查询聊天记录耗时：{}ms",timeCost);
        return result;
    }



//    public boolean updateBatch(Collection<MsgHistory> entityList) {
//        return updateBatch(entityList,1000);
//    }
//
//
//    public boolean updateBatch(Collection<MsgHistory> entityList, int batchSize) {
//        Assert.notEmpty(entityList, "error: entityList must not be empty");
//        String sqlStatement = sqlStatement(SqlMethod.UPDATE);
//        try (SqlSession batchSqlSession = sqlSessionBatch()) {
//            int i = 0;
//            for (MsgHistory anEntityList : entityList) {
//                MapperMethod.ParamMap<Object> param = new MapperMethod.ParamMap<>();
//                param.put(Constants.ENTITY, anEntityList);
//                param.put(Constants.WRAPPER, getQueryWrapper(anEntityList.getSendId(),anEntityList.getReceiverId(), anEntityList.getSendTime()));
//                batchSqlSession.update(sqlStatement, param);
//                if (i >= 1 && i % batchSize == 0) {
//                    batchSqlSession.flushStatements();
//                }
//                i++;
//            }
//            batchSqlSession.flushStatements();
//        }
//        return true;
//    }
//
//    private LambdaQueryWrapper<MsgHistory> getQueryWrapper(Long cond1, Long cond2, Timestamp cond3) {
//        final LambdaQueryWrapper<MsgHistory> queryWrapper = new QueryWrapper<MsgHistory>().lambda();
//        queryWrapper.eq(MsgHistory::getSendId,cond1);
//        queryWrapper.eq(MsgHistory::getReceiverId,cond2);
//        queryWrapper.eq(MsgHistory::getSendTime, cond3);
//        queryWrapper.eq(MsgHistory::getReaded, 2);
//        return queryWrapper;
//    }


}
