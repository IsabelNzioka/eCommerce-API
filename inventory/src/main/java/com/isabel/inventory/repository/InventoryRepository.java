package com.isabel.inventory.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.isabel.inventory.entity.Inventory;

public interface InventoryRepository extends MongoRepository<Inventory, String> {
    
}
