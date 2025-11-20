package com.example.shopping_application.service;

import com.example.shopping_application.dto.ProductPatchRequest;
import com.example.shopping_application.entity.Product;
import com.example.shopping_application.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product addProduct(Product product) {
        if (!StringUtils.hasText(product.getProductName()) ||  product.getProductName().isEmpty()) {
            throw new IllegalArgumentException("Enter valid product");
        }
        return productRepository.save(product);
    }

    public Product getProduct(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product Not Found "));
    }

    public List<Product> getAllProducts() {
        if (productRepository.findAll().isEmpty()) {
            throw new EntityNotFoundException("Product Not Found");
        }
        return productRepository.findAll();
    }

    public Product updateProduct(Long id, Product product) {

        Product existingProduct = productRepository.findById(product.getId()).orElseThrow(() -> new EntityNotFoundException("Product Not Found"));
        existingProduct.setId(product.getId());
        existingProduct.setProductName(product.getProductName());
        existingProduct.setStock(product.getStock());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setDescription(product.getDescription());

        return productRepository.save(existingProduct);
    }
    public Product updateProductSpecificField(Long id, ProductPatchRequest productPatchRequest){

        Product existingProduct=productRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Product Not Found"));

        if (productPatchRequest.getProductName() !=null) existingProduct.setProductName(productPatchRequest.getProductName());
        if (productPatchRequest.getDescription() !=null) existingProduct.setDescription(productPatchRequest.getDescription());
        if (productPatchRequest.getPrice() !=null) existingProduct.setPrice(productPatchRequest.getPrice());
        if (productPatchRequest.getStock() !=null) existingProduct.setStock(productPatchRequest.getStock());
        if (productPatchRequest.getCategory() !=null) existingProduct.setCategory(productPatchRequest.getCategory());

        return productRepository.save(existingProduct);
    }

    public void deleteProduct(Long id) {
        if (productRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException("Product Not Found");
        }
        productRepository.deleteById(id);
    }

    public Map<String, Object> sortProducts(int page, int size, String sortBy, String direction,String searchKey) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Product> productPage;

        if (!StringUtils.hasText(searchKey)){
            productPage=productRepository.findAll(pageable);
        }
        else productPage=productRepository.findByProductName(searchKey,pageable);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("products", productPage.getContent());
        response.put("current Page", productPage.getNumber());
        response.put("page Size", productPage.getSize());
        response.put("total Items", productPage.getTotalElements());
        response.put("total Pages", productPage.getTotalPages());

        return response;
    }


   /* public List<Product> searchProducts(String prodName, Double minPrice, Double maxPrice) {
        Specification<Product> spec = Specification.where(null);

        if (prodName != null && !prodName.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("prodName")), "%" + prodName.toLowerCase() + "%"));
        }

        if (minPrice != null && maxPrice != null) {
            spec = spec.and((root, query, cb) ->
                    cb.between(root.get("price"), minPrice, maxPrice));
        }
        return productRepository.findAll((Sort) spec);
    }*/

    public Product updateStock(Long id,int stock) {
        Product existing=productRepository.findById(id).orElseThrow(()->new RuntimeException("product not found"));
        existing.setStock(stock);
        return productRepository.save(existing);
    }

    public void validateStock(Product product, int quantity) {
        if (product.getStock() < quantity) {
            throw new IllegalArgumentException("Not enough stock");
        }
    }

    public void reduceStock(Product product, int quantity) {
        product.setStock(product.getStock() - quantity);
        productRepository.save(product);
    }
}

