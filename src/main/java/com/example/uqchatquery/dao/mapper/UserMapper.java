package com.example.uqchatquery.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.uqchatquery.dao.model.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
