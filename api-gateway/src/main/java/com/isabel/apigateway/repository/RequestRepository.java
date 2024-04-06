package com.isabel.apigateway.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.isabel.apigateway.entity.Request;

public interface RequestRepository extends MongoRepository<Request, String> {
    
}
