package com.example.uqchatquery.dto;

import lombok.Data;

@Data
public class GroupChatPageQueryParam {

    private Integer pageNum;

    private Integer pageSize;

    private Long userId;

    private String courseCode;

    private Integer categoryId;

}