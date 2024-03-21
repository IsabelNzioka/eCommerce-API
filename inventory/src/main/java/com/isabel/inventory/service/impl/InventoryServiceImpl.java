package com.isabel.inventory.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.isabel.inventory.entity.Inventory;
import com.isabel.inventory.model.InventoryCreateDto;
import com.isabel.inventory.model.InventoryResponse;
import com.isabel.inventory.repository.InventoryRepository;
import com.isabel.inventory.service.InventoryService;


@Service
public class InventoryServiceImpl implements InventoryService{

    private final InventoryRepository inventoryRepository;

    public InventoryServiceImpl(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public InventoryResponse createInventory(InventoryCreateDto inventoryCreateDto) {
       var savedObj = inventoryRepository.save(mapToInventory(inventoryCreateDto));
       return mapInventoryResponse(savedObj);
    }

    private Inventory mapToInventory(InventoryCreateDto source) { 
        Inventory target = new Inventory();
        BeanUtils.copyProperties(source, target);
        return target;

    }

    private InventoryResponse mapInventoryResponse(Inventory source) { //receive an object from the db
        InventoryResponse target = new InventoryResponse();
        BeanUtils.copyProperties(source, target);
        return target;

    }
    
}
