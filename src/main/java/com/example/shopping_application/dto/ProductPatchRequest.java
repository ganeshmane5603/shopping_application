package com.example.shopping_application.dto;

import com.example.shopping_application.entity.Category;
import lombok.Data;

@Data
public class ProductPatchRequest {
    private String productName;
    private String description;
    private Double price;
    private Integer stock;
    private Category category;
}
