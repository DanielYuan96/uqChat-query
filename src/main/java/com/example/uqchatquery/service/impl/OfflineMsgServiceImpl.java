package com.example.uqchatquery.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.uqchatquery.dao.mapper.OfflineMsgMapper;
import com.example.uqchatquery.dao.model.OfflineMsg;
import com.example.uqchatquery.dao.model.User;
import com.example.uqchatquery.dto.MessageBody;
import com.example.uqchatquery.service.OfflineMsgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OfflineMsgServiceImpl extends ServiceImpl<OfflineMsgMapper, OfflineMsg> implements OfflineMsgService {
    @Override
    public boolean saveOffLineMsg(MessageBody messageBody) {
        OfflineMsg offlineMsg = new OfflineMsg();
        offlineMsg.setReceiverId(messageBody.getReceiverId());
        offlineMsg.setSendTime(new Timestamp(messageBody.getSendTime()));
        offlineMsg.setMessage(JSON.toJSONString(messageBody));
        return this.save(offlineMsg);
    }

    @Override
    public List<MessageBody> queryOffLineMsg(User user) {
        QueryWrapper<OfflineMsg> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("receiver_id", user.getId());
        List<OfflineMsg> data = this.list(queryWrapper);
        if (CollectionUtils.isEmpty(data)) {
            return Collections.emptyList();
        }
        List<String> collect = data.stream().map(OfflineMsg::getMessage).collect(Collectors.toList());
        return collect.stream().map(o -> JSON.parseObject(o, MessageBody.class)).collect(Collectors.toList());
    }
}
