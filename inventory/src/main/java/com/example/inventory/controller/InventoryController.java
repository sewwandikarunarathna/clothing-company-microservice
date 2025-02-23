package com.example.inventory.controller;

import com.example.inventory.dto.InventoryDTO;
import com.example.inventory.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/inventory")
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/getinventories")
    public List<InventoryDTO> getInventories() {
        return inventoryService.getAllInventories();
    }

    @GetMapping("/item/{itemId}")
    public InventoryDTO getItemByItemId(@PathVariable Integer itemId) {
        return inventoryService.getInventoryByItemId(itemId);
    }

    @PostMapping("/addinventory")
    public InventoryDTO saveInventory(@RequestBody InventoryDTO inventoryDTO) {
        return inventoryService.saveInventory(inventoryDTO);
    }

    @PutMapping("/updateinventory")
    public InventoryDTO updateInventory(@RequestBody InventoryDTO inventoryDTO) {
        return inventoryService.updateInventory(inventoryDTO);
    }

    @DeleteMapping("/deleteinventory/{inventoryId}") //use a path parameter
    public String deleteInventoryById(@PathVariable Integer inventoryId) {
        return inventoryService.deleteInventoryById(inventoryId);
    }
}
