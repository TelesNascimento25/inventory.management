package com.join.inventory.controller;

import com.join.inventory.model.Item;
import com.join.inventory.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getItems(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String orderBy,
            @RequestParam(defaultValue = "asc") String direction) {
        try {
            Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(orderBy).descending() : Sort.by(orderBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);

            Page<Item> pageItems;
            if ((name == null || name.isEmpty()) && (description == null || description.isEmpty())) {
                pageItems = itemRepository.findAll(pageable);
            } else {
                pageItems = itemRepository.findByNameContainingAndDescriptionContaining(
                        name != null ? name : "",
                        description != null ? description : "",
                        pageable);
            }

            List<Item> items = pageItems.getContent();

            Map<String, Object> response = new HashMap<>();
            response.put("items", items);
            response.put("currentPage", pageItems.getNumber());
            response.put("totalItems", pageItems.getTotalElements());
            response.put("totalPages", pageItems.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
