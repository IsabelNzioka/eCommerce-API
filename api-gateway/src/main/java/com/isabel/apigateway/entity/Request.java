package com.isabel.apigateway.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "requests")
public class Request {

     @Id
    private String id;
    private Date timestamp;
    private String ipAddress;
    private String resourceUrl;
    private String method;
    
}
