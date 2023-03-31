package com.example.uqchatquery.dao.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

@Data
@TableName(value = "msg_history")
public class MsgHistory {

    @TableId(type = IdType.AUTO)
    private Long id;

//    @TableField(value = "send_id")
//    private Long sendId;

//    @TableField(value = "receiver_id")
//    private Long receiverId;
    /**
     * 1-已读
     * 2-未读
     */
//    @TableField(value = "readed")
//    private Integer readed;

    @TableField(value = "send_time")
    private Timestamp sendTime;

    @TableField(value = "message")
    private String message;

    @TableField(value = "type")
    private String type;

    @TableField(value = "message_id")
    private String messageId;
}
