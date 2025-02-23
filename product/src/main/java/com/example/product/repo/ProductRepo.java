package com.example.product.repo;

import com.example.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo extends JpaRepository<Product, Integer> {
    @Query(value = "SELECT * FROM product WHERE product_id = ?1", nativeQuery = true) // itemId should be as item_id since Entity annotation converts likewise when storing properties to db
    Product getProductsByProductId(Integer productId);
}
