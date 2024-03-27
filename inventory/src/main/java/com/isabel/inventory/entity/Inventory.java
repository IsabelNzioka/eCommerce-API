package com.isabel.inventory.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "inventories")
public class Inventory {

    @Id
    private String id;

    @Indexed(unique = true)
    private String productCode;

    private Integer quantity;
    
}
