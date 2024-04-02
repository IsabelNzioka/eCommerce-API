package com.isabel.inventory.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.isabel.inventory.model.GenericResponse;
import com.isabel.inventory.model.InventoryCreateDto;
import com.isabel.inventory.model.InventoryResponse;
import com.isabel.inventory.service.InventoryService;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RequestMapping("api/inventory")
@RestController
@Slf4j
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    
    @GetMapping
        public GenericResponse<List<InventoryResponse>> findAll() {
        List<InventoryResponse> inventoryResponses = inventoryService.findAll();

        GenericResponse<List<InventoryResponse>> response = GenericResponse.<List<InventoryResponse>>builder()
                .success(true)
                .msg("Data fetched successfully")
                .data(inventoryResponses)
                .build();

        log.info("Returned inventory data: {}", inventoryResponses);
        return response;
    }

    @PostMapping("create")
    @ResponseStatus(code = HttpStatus.CREATED)
    public GenericResponse<InventoryResponse> create(@RequestBody InventoryCreateDto inventoryCreateDto) {
      
        return GenericResponse.<InventoryResponse>builder().data(inventoryService.createInventory(inventoryCreateDto)).success(true).msg("Inventory Saved Successfully").build();
    }




    @PostMapping("create-inventories")
    @ResponseStatus(HttpStatus.CREATED)
    public GenericResponse<List<InventoryResponse>> addInventories(@RequestBody List<InventoryCreateDto> inventoryCreateDtos) {
        List<InventoryResponse> inventoryResponses = inventoryService.createInventories(inventoryCreateDtos);
    return GenericResponse.<List<InventoryResponse>>builder()
            .data(inventoryResponses)
            .success(true)
            .msg("Inventories Saved Successfully")
            .build();
}




    
    @GetMapping("check")
    @ResponseStatus(code = HttpStatus.OK)
    public GenericResponse<Boolean> checkInventory(
            @RequestParam(name = "productCodes") List<String> productCodes,
            @RequestParam(name = "productQuantities") List<Integer> productQuantities) {

        log.info("{}",productCodes);       
        log.info("{}",productQuantities);       
        return GenericResponse.<Boolean>builder()
                .data(inventoryService.checkInventory(productCodes, productQuantities))
                .success(true)
                .msg("Inventory exists/enough")
                .build();
    }
    
    
}
