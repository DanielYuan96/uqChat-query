package com.example.uqchatquery.dao.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "group_category")
public class GroupCategory {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField(value = "category_name")
    private String categoryName;

    @TableField(value = "category_avatar")
    private String categoryAvatar;
}
