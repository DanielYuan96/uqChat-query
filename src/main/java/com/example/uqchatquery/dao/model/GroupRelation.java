package com.example.uqchatquery.dao.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "group_relation")
public class GroupRelation {

    @TableField(value = "group_chat_id")
    private Integer groupChatId;

    @TableField(value = "user_id")
    private Long userId;
}
