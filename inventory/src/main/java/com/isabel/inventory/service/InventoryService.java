package com.isabel.inventory.service;

import java.util.List;

import com.isabel.inventory.model.InventoryCreateDto;
import com.isabel.inventory.model.InventoryResponse;

public interface InventoryService {

    InventoryResponse createInventory(InventoryCreateDto inventoryCreateDto);

    Boolean checkInventory(List<String> productCodes, List<Integer> productQuantities);
    
    List<InventoryResponse> createInventories(List<InventoryCreateDto> inventoryCreateDtos);



    public List<InventoryResponse> findAll();
    public void updateQuantity(String productName, int quantity);

    public void deleteInventories(List<String> inventoryIds);
    
} 