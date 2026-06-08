package com.ecommerce.backend.service;

import com.ecommerce.backend.payload.ProductDTO;
import com.ecommerce.backend.payload.ProductResponse;

public interface ProductService {
    ProductDTO addProduct(long categoryId, ProductDTO productDTO);

    ProductResponse getProducts(int pageNumber, int pageSize, String sortBy, String sortOrder);

    ProductResponse getProductsByCategory(Long categoryId, int pageNumber, int pageSize, String sortBy, String sortOrder);

    ProductResponse getProductsByKeyword(String keyword, int pageNumber, int pageSize, String sortBy, String sortOrder);
}
