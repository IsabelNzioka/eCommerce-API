package com.isabel.inventory.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.google.common.base.Optional;
import com.isabel.inventory.entity.Inventory;
import com.isabel.inventory.exception.NotEnoughQuantityException;
import com.isabel.inventory.exception.UniqueProductCodeException;
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

   @Override
    public Boolean checkInventory(List<String> productCodes, List<Integer> productQuantities) {
         
        Map<String, Integer> unavailableItems = new HashMap<>();

        for (int i = 0; i < productCodes.size(); i++) {
            String productCode = productCodes.get(i);
            Integer productQuantity = productQuantities.get(i);
            Inventory inventory = inventoryRepository.findByProductCode(productCode).orElse(null); // !
            if (inventory != null) {
                // check if enough
                var dbInventory = inventory.getQuantity();
                if (productQuantity > dbInventory) {
                    unavailableItems.put(productCode, productQuantity - dbInventory);
                }
            } else {
                unavailableItems.put(productCode, productQuantity);
            }
        }
        if(unavailableItems.isEmpty()){
            return true;
        }else{
            throw new NotEnoughQuantityException("Not Enough Quantity in Stock", unavailableItems);
        }

    }

    @Override
    public List<InventoryResponse> createInventories(List<InventoryCreateDto> inventoryCreateDtos) {
        List<InventoryResponse> inventoryResponses = new ArrayList<>();
        for (InventoryCreateDto inventoryCreateDto : inventoryCreateDtos) {
            Inventory savedInventory = inventoryRepository.save(mapToInventory(inventoryCreateDto));
            inventoryResponses.add(mapInventoryResponse(savedInventory));
        }
        return inventoryResponses;
    }

    
}
