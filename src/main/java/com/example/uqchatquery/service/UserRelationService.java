package com.example.uqchatquery.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.uqchatquery.dao.model.UserRelation;
import com.example.uqchatquery.dto.RelationRequest;

import java.util.List;

public interface UserRelationService extends IService<UserRelation> {
    boolean handleRelation(RelationRequest request);

    boolean changeRelation(RelationRequest request);

//    List<UserRelation> queryRelation(RelationRequest request);
}
