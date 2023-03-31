package com.example.uqchatquery.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.uqchatquery.dao.mapper.GroupCategoryMapper;
import com.example.uqchatquery.dao.model.GroupCategory;
import com.example.uqchatquery.service.GroupCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GroupCategoryServiceImpl extends ServiceImpl<GroupCategoryMapper, GroupCategory> implements GroupCategoryService {
}
