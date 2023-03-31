package com.example.uqchatquery.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class MessageBody {
    /**
     * 数据库msgHistory表中的主键id
     */
    private Long msgId;

    private Integer type;

    private Long sendId;

    private String sendName;

    private Long receiverId;

    private String receiverName;
    /**
     * ws链接传输过程中该消息的序列号 唯一识别ID
     * 生成规则：
     * 当前时间毫秒+接收该消息包的用户的id+6位随机数
     */
    private String messageId;

    private String message;

    private Long sendTime;
    /**
     * 1-已读
     * 2-未读
     */
    private Integer readed;
    /**
     * 判断是否是群聊消息字段
     * 0-是
     * 1-否
     */
    private Integer isGroupMsg;
    /**
     * 群聊id
     */
    private Integer groupChatId;
}
