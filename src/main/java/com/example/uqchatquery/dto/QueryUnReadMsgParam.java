package com.example.uqchatquery.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class QueryUnReadMsgParam {

    private List<Long> sendId;

    private Long receiverId;
    /**
     * 1-已读，2-未读
     */
    private Integer readed;

    private Timestamp sendTime;
    /**
     * 1-chatMessage
     * 2-friendReqAccept
     */
    private String type;
}
