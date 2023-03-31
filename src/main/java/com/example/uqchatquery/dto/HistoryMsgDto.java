package com.example.uqchatquery.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class HistoryMsgDto {

    private Long id;

    private Long sendId;

    private Long receiverId;
    /**
     * 1-已读
     * 2-未读
     */
    private Integer readed;

    private Timestamp sendTime;

    private String message;

    private String type;

    private String messageId;
}
