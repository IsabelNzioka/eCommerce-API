package com.isabel.productservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.isabel.productservice.entity.Product;
import com.isabel.productservice.model.GenericResponse;
import com.isabel.productservice.model.ProductCreateRequest;
import com.isabel.productservice.model.ProductCreateResponse;
import com.isabel.productservice.service.ProductService;

import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


@RequestMapping("api/v1/products")
@RestController
// @Slf4j
public class ProductController {

    Logger logger = LogManager.getLogger(ProductController.class);

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

   
   
    @GetMapping
    public GenericResponse<List<ProductCreateResponse>> list(@RequestParam(required = false) String sortBy) {
       List<ProductCreateResponse> pr = productService.findAll(sortBy);
       GenericResponse<List<ProductCreateResponse>> resp = GenericResponse.<List<ProductCreateResponse>>builder()
                .success(true)
                .msg("Data fetched Successfully")
                .data(pr)
                .build();
                logger.info("We returned : {}",pr);
                return resp;
    }

    @GetMapping("/{productId}")
    public GenericResponse<ProductCreateResponse> findById(@PathVariable Integer productId) {
       ProductCreateResponse pr = productService.findById(productId);
       GenericResponse<ProductCreateResponse> resp = GenericResponse.<ProductCreateResponse>builder()
                .success(true)
                .msg("Data fetched Successfully")
                .data(pr)
                .build();
                logger.info("We returned : {}",pr);
                return resp;
    }

    @PostMapping
    public GenericResponse<ProductCreateResponse> createProduct(@RequestBody ProductCreateRequest productCreateRequest) {
                logger.info("We received : {}",productCreateRequest);
        ProductCreateResponse pr = productService.createProduct(productCreateRequest);
        GenericResponse<ProductCreateResponse> resp = GenericResponse.<ProductCreateResponse>builder()
                .success(true)
                .msg("Data saved Successfully")
                .data(pr)
                .build();
                logger.info("We returned : {}",pr);
        return resp;
    }
    
    @PostMapping("/create-products")
    public GenericResponse<List<ProductCreateResponse>> createProducts(@RequestBody List<ProductCreateRequest> productCreateRequests) {
    logger.info("We received>>>>>>>>>>>>>>>>>>>> : {}", productCreateRequests);
    
    List<ProductCreateResponse> responses = productService.createProducts(productCreateRequests);
    GenericResponse<List<ProductCreateResponse>> resp = GenericResponse.<List<ProductCreateResponse>>builder()
            .success(true)
            .msg("Data saved Successfully")
            .data(responses)
            .build();
    return resp;
}

    @GetMapping("/check")
    @ResponseStatus(code = HttpStatus.OK)
    public GenericResponse<Boolean> checkProducts(
            @RequestParam(name = "productCodes") List<String> productCodes) {

        boolean allProductsExist = productService.checkProducts(productCodes);

        return GenericResponse.<Boolean>builder()
                .data(allProductsExist)
                .success(true)
                .msg("Product Exists")
                .build();
    }


    
}
