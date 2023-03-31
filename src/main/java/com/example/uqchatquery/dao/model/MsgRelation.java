package com.example.uqchatquery.dao.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.management.remote.rmi._RMIConnection_Stub;
import java.sql.Timestamp;

@Data
@TableName(value = "msg_relation")
public class MsgRelation {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField(value = "send_id")
    private Long sendId;

    @TableField(value = "receiver_id")
    private Long receiverId;
    /**
     * 关联MsgHistory id
     */
    @TableField(value = "msg_id")
    private Long msgId;
    /**
     * 1-已读
     * 2-未读
     */
    @TableField(value = "readed")
    private Integer readed;
    /**
     * 0-是
     * 1-否
     */
    @TableField(value = "is_group_msg")
    private Integer isGroupMsg;
    /**
     * 聊天室id
     */
    @TableField(value = "group_chat_id")
    private Integer groupChatId;

    @TableField(value = "send_time")
    private Timestamp sendTime;

    @TableField(value = "type")
    private String type;

}
