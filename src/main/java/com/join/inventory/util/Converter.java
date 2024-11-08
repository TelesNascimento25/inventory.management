package com.join.inventory.util;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class Converter {

    public Sort convertToSort(String[] sort) {
        if (sort != null && sort.length > 0) {
            String property = sort[0];
            String direction = (sort.length > 1 && sort[1].equalsIgnoreCase("desc")) ? "desc" : "asc";

            if ("desc".equalsIgnoreCase(direction)) {
                return Sort.by(Sort.Order.desc(property));
            } else {
                return Sort.by(Sort.Order.asc(property));
            }
        }
        return Sort.unsorted();
    }
}
