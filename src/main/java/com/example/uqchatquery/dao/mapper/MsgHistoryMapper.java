package com.example.uqchatquery.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.uqchatquery.dao.model.MsgHistory;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MsgHistoryMapper extends BaseMapper<MsgHistory> {
}
