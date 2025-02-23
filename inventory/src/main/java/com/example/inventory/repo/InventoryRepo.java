package com.example.inventory.repo;

import com.example.inventory.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepo extends JpaRepository<Inventory, Integer> {
    @Query(value = "SELECT * FROM inventory WHERE item_id = ?1", nativeQuery = true) // itemId should be as item_id since Entity annotation converts likewise when storing properties to db
    Inventory getInventoryByItemId(Integer itemId);
}
