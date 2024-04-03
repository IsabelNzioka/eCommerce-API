package com.isabel.inventory.service.impl;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.common.base.Optional;
import com.isabel.inventory.entity.Inventory;
import com.isabel.inventory.exception.InventoryServiceException;
import com.isabel.inventory.exception.NotEnoughQuantityException;
import com.isabel.inventory.exception.ProductServiceException;
import com.isabel.inventory.exception.UniqueProductCodeException;
import com.isabel.inventory.model.GenericResponse;
import com.isabel.inventory.model.InventoryCreateDto;
import com.isabel.inventory.model.InventoryResponse;
import com.isabel.inventory.repository.InventoryRepository;
import com.isabel.inventory.service.InventoryService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;


@Service
@Slf4j
public class InventoryServiceImpl implements InventoryService{

    private final InventoryRepository inventoryRepository;
    // private final WebClient.Builder webClientBuilder;
    private final WebClient webClient;

    public InventoryServiceImpl(InventoryRepository inventoryRepository, WebClient webClient) {
        this.inventoryRepository = inventoryRepository;
        this.webClient = webClient;
        // this.webClientBuilder = webClientBuilder;
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

          List<String> productCodes = inventoryCreateDtos.stream()
                                        .map(InventoryCreateDto::getProductCode)
                                        .collect(Collectors.toList());    
    
       String productCodesString = String.join(",", productCodes);
       log.info("Product codes to check: {} >>>>>>>>>>>>>>>>>>>>>>>>>>", productCodesString);
    

       URI uri = UriComponentsBuilder.fromUriString("http://localhost:7000/api/products/check")
        .queryParam("productCodes", productCodesString)
        .build()
        .toUri();

       GenericResponse<?> response = webClient.get()
       .uri(uri)

       .retrieve()
       .onStatus(HttpStatusCode::isError, clientResponse -> handleError(clientResponse))
       .bodyToMono(new ParameterizedTypeReference<GenericResponse<?>>() {})
       .doOnSuccess(successResponse -> {
           // Log the success response
           log.info("Success response: {} >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>", successResponse);
       })
       .block();
   

    log.info("Response from product service: {}  ", response);


        // Product exists? add inventories
        if (response != null && response.isSuccess()) { 
            for (InventoryCreateDto inventoryCreateDto : inventoryCreateDtos) {
                String productCode = inventoryCreateDto.getProductCode();
        
                Inventory existingInventory = inventoryRepository.findByProductCode(productCode).orElse(null);
                
                if (existingInventory != null) {
                   throw new UniqueProductCodeException("Product Codes should be Unique");
                }
                
                Inventory savedInventory = inventoryRepository.save(mapToInventory(inventoryCreateDto));
                inventoryResponses.add(mapInventoryResponse(savedInventory));
            }
        
            return inventoryResponses;
        } else {
        // Some Products does not exist
        throw new InventoryServiceException(response.getMsg());

        }
    }
     private Mono<? extends Throwable> handleError(ClientResponse response) {
        log.error("Client error received: {}", response.statusCode());
        return Mono.error(new ProductServiceException("Error in Product service"));
    }



    @Override
    public List<InventoryResponse> findAll() {
       return inventoryRepository.findAll().stream().map(this::mapToInventoryResponse).toList();
    }

    private InventoryResponse mapToInventoryResponse(Inventory source){
        InventoryResponse target = new InventoryResponse();
        BeanUtils.copyProperties(source, target);
        return target;

    }

    @Override
    public void updateQuantity(String productCode, int quantity) {
        Inventory inventory = inventoryRepository.findByProductCode(productCode).orElse(null);

        if (inventory != null) {
            inventory.setQuantity(quantity);
            inventoryRepository.save( inventory);
        } else {
            throw new UniqueProductCodeException("Inventory with productCode " + productCode + " not found");
        }
    }
   
}
