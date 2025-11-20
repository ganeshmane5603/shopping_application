package com.example.shopping_application.controller;

import com.example.shopping_application.entity.Category;
import com.example.shopping_application.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/add")
    public ResponseEntity<Category> addCategory(@RequestBody Category category){
        Category newCategory=categoryService.createCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCategory);
    }

    @GetMapping("/get")
    public ResponseEntity<Category> getCategoryById(@RequestParam Long id){
        return ResponseEntity.ok().body(categoryService.getCategoryById(id));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Category>> getAllUsers(@RequestParam(defaultValue ="1" ) int page,
                                                  @RequestParam(defaultValue = "10") int size,
                                                  @RequestParam(defaultValue = "id") String sortBy,
                                                  @RequestParam(defaultValue = "asc") String sortOrder){
        return ResponseEntity.ok(categoryService.getAllCategories(page,size,sortBy,sortOrder).getContent());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Category deleted successfully");
    }
}
