package com.example.uqchatquery.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespResult<T> implements Serializable {

    /**
     * 返回码
     */
    private String respCode;

    /**
     * 返回码说明
     */
    private String respMsg;

    /**
     * 业务对象
     */
    private T data;


}
