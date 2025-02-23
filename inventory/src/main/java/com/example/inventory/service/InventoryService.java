package com.example.inventory.service;

import com.example.inventory.dto.InventoryDTO;
import com.example.inventory.model.Inventory;
import com.example.inventory.repo.InventoryRepo;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class InventoryService {
    @Autowired
    private InventoryRepo InventoryRepo;

    @Autowired
    private ModelMapper modelMapper;

    public List<InventoryDTO> getAllInventories() {
        List<Inventory> inventoryList = InventoryRepo.findAll();
        return modelMapper.map(inventoryList, new TypeToken<List<InventoryDTO>>(){}.getType());
    }

    public InventoryDTO getInventoryByItemId(Integer itemId) {
        Inventory item = InventoryRepo.getInventoryByItemId(itemId);
        return modelMapper.map(item, InventoryDTO.class);
    }

    public InventoryDTO saveInventory(InventoryDTO inventoryDTO) {
        InventoryRepo.save(modelMapper.map(inventoryDTO, Inventory.class));
        return inventoryDTO;
    }

    public InventoryDTO updateInventory(InventoryDTO inventoryDTO) {
        InventoryRepo.save(modelMapper.map(inventoryDTO, Inventory.class));
        return inventoryDTO;
    }

    public String deleteInventoryById(Integer inventoryId) {
        InventoryRepo.deleteById(inventoryId);
        return "Inventory with id " + inventoryId + " was deleted";
    }
}
