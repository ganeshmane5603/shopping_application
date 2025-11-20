package com.example.shopping_application.service;

import com.example.shopping_application.entity.Category;
import com.example.shopping_application.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category createCategory(Category category){
        if (!StringUtils.hasText(category.getCategoryName())){
            throw new IllegalArgumentException("Enter valid category name");
        }
        return categoryRepository.save(category);
    }

    public Category getCategoryById(Long id){
        return categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Category not found"));
    }

    public Page<Category> getAllCategories(int page,int size,String sortBy,String sortOrder){
        Sort sort=sortOrder.equalsIgnoreCase("desc")
                ?Sort.by(sortBy).descending()
                :Sort.by(sortBy).ascending();

        Pageable pageable= PageRequest.of(page-1,size,sort);
        return categoryRepository.findAll(pageable);
    }

    public Category updateCategory(Long id,Category category){
        Category existingCategory=categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Category not found"));

        existingCategory.setCategoryName(category.getCategoryName());
        return categoryRepository.save(existingCategory);
    }
    public void deleteCategory(Long id){
        if (!categoryRepository.existsById(id)){
            throw new EntityNotFoundException("Category not found");
        }
        categoryRepository.deleteById(id);
    }
}
