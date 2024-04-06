package com.isabel.apigateway.config;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.CacheRequestBodyGatewayFilterFactory.Config;
import org.springframework.stereotype.Component;

import com.isabel.apigateway.entity.Request;
import com.isabel.apigateway.repository.RequestRepository;

import lombok.extern.slf4j.Slf4j;

@Component()
@Slf4j
public class RequestFilter extends AbstractGatewayFilterFactory<RequestFilter.Config>{

    private RequestRepository requestRepository;

    @Autowired
    public RequestFilter(RequestRepository requestRepository) {
        super(Config.class);
        this.requestRepository = requestRepository;
    }
   
    @Override
    public GatewayFilter apply(Config config) {
        log.info("Detected>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        return (exchange, chain) -> {
         
            // Get request details
            String method = exchange.getRequest().getMethod().name();
            String path = exchange.getRequest().getURI().getPath();
            String queryString = exchange.getRequest().getURI().getQuery();
            String requestBody = exchange.getRequest().getBody().toString();
            String remoteAddress = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
            
            Date timestamp = new Date();
           
            // Save request details to the Database
            Request requestLog = new Request();
            requestLog.setTimestamp(timestamp);
            requestLog.setIpAddress(remoteAddress);
            requestLog.setResourceUrl(path);
            requestLog.setMethod(method);

            requestRepository.save(requestLog);

            log.info("Incoming request: method={}, path={}, queryString={}, remoteAddress={}, timestamp={}",
            method, path, queryString, remoteAddress, timestamp);

            return chain.filter(exchange);
        };
    }


    public static class Config {
     
    }



    
}
