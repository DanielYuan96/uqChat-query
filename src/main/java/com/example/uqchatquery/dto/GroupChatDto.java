package com.example.uqchatquery.dto;

import com.example.uqchatquery.dao.model.User;
import lombok.Data;

import java.util.List;

@Data
public class GroupChatDto {

    private Integer groupId;

    private List<User> members;

    private Integer membersNum;

    private String groupName;

    private String groupAvatar;

    private String groupBackground;

    private String groupIntro;

    private Integer category;

    private Integer notification;

    private Boolean isEnrolled;


}