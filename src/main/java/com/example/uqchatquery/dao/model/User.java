package com.example.uqchatquery.dao.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "test_table")
public class User implements Serializable {

    @TableField(value = "id")
    private long id;

    @TableField(value = "name")
    private String name;

    @TableField(value = "age")
    private int age;

    @TableField(value = "password")
    private String password;

    @TableField(value = "account")
    private String account;

    @TableField(value = "gender")
    private int gender;

    @TableField(value = "email")
    private String email;

    @TableField(value = "avatar")
    private String avatar;

    @TableField(value = "phone")
    private String phone;

    @TableField(value = "banner_color")
    private String bannerColor;

    @TableField(value = "self_intro")
    private String selfIntro;
}
