package com.join.inventory.repository;

import com.join.inventory.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ItemRepository extends JpaRepository<Item, Long> {
    Page<Item> findByNameContainingAndDescriptionContaining(String name, String description, Pageable pageable);
}
