package com.example.uqchatquery.dao.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Value;

@Data
@TableName(value = "group_chat")
public class GroupChat {
    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField(value = "avatar")
    private String avatar;

    @TableField(value = "background")
    private String background;

    @TableField(value = "intro")
    private String intro;

    @TableField(value = "course_code")
    private String courseCode;

    @TableField(value = "group_category_id")
    private Integer groupCategoryId;
}
