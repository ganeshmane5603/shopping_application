package com.example.shopping_application.controller;

import com.example.shopping_application.dto.ProductPatchRequest;
import com.example.shopping_application.entity.Product;
import com.example.shopping_application.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/add")
    public ResponseEntity<Product> addProduct(@RequestBody Product product){
        Product newProduct=productService.addProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id){
        return ResponseEntity.ok().body(productService.getProduct(id));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Product>> getAllProducts(){
        return ResponseEntity.ok().body(productService.getAllProducts());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id,@RequestBody Product product){
        return ResponseEntity.ok().body(productService.updateProduct(id,product));
    }

    @PatchMapping("/patch/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody ProductPatchRequest productPatchRequest){
        return ResponseEntity.ok().body(productService.updateProductSpecificField(id,productPatchRequest));
    }

    @GetMapping("/sort")
    public ResponseEntity<Map<String,Object>> sortedProducts(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "5") int size,
                                                             @RequestParam(defaultValue = "id") String sortBy,
                                                             @RequestParam(defaultValue = "asc") String sortOrder,
                                                             @RequestParam String searchKey){

        return ResponseEntity.ok().body(productService.sortProducts(page,size,sortBy,sortOrder,searchKey));

    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

