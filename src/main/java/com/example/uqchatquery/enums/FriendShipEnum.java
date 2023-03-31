package com.example.uqchatquery.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FriendShipEnum {
    FRIEND_WAIT("wait"),
    FRIEND_BLOCK("block"),
    FRIEND_CREATED("friend"),
    ;

    private final String des;
}
