package com.example.uqchatquery.dao.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

@Data
@TableName(value = "offline_msg")
public class OfflineMsg {
    @TableField(value = "receiver_id")
    private Long receiverId;

    @TableField(value = "message")
    private String message;

    @TableField(value = "send_time")
    private Timestamp sendTime;

}
