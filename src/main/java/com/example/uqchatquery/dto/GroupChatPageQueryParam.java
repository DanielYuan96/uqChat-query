package com.example.uqchatquery.dto;

import lombok.Data;

@Data
public class GroupChatPageQueryParam {

    private Integer pageNum = 1;

    private Integer pageSize = 10;

    private Long userId;

    private String courseCode;

    private Integer categoryId;

}