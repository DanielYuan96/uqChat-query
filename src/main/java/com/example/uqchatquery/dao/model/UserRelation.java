package com.example.uqchatquery.dao.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("user_relation")
public class UserRelation implements Serializable {

    @TableField(value = "user_id")
    private Long userId;

    @TableField(value = "friend_id")
    private Long friendId;

    @TableField(value = "friendship")
    private String friendship;
}
