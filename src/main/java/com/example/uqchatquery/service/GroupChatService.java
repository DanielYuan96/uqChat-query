package com.example.uqchatquery.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.uqchatquery.dao.model.GroupChat;
import com.example.uqchatquery.dto.GroupChatDto;
import com.example.uqchatquery.dto.GroupChatPageQueryParam;

import java.util.List;

public interface GroupChatService extends IService<GroupChat> {
    List<GroupChatDto> queryGroupsByCondition(GroupChatPageQueryParam queryParam);
}
