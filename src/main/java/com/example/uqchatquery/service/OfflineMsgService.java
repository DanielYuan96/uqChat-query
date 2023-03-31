package com.example.uqchatquery.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.uqchatquery.dao.model.OfflineMsg;
import com.example.uqchatquery.dao.model.User;
import com.example.uqchatquery.dto.MessageBody;

import java.util.List;

public interface OfflineMsgService extends IService<OfflineMsg> {
    boolean saveOffLineMsg(MessageBody messageBody);

    List<MessageBody> queryOffLineMsg(User user);
}
