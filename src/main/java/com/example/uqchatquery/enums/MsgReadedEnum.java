package com.example.uqchatquery.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MsgReadedEnum {
    READED(1),
    UN_READED(2),
    ;

    private final Integer code;
}
