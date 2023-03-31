package com.example.uqchatquery.dto;

import com.example.uqchatquery.enums.FriendShipEnum;
import lombok.Data;

import java.io.Serializable;

@Data
public class RelationRequest implements Serializable {

    private Long sendId;

    private Long receiverId;
    /**
     * 当前关系类型 详情见
     * @see FriendShipEnum
     */
    private String friendShip;

    /**
     * 1-接受好友请求，2-拒绝好友请求
     */
    private Integer action;
}
