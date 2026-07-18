package com.ecommerce.backend.service;

import com.ecommerce.backend.payload.ProductDTO;
import com.ecommerce.backend.payload.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    ProductDTO addProduct(long categoryId, ProductDTO productDTO);

    ProductResponse getProducts(int pageNumber, int pageSize, String sortBy, String sortOrder, String keyword, String category);

    ProductResponse getProductsByCategory(Long categoryId, int pageNumber, int pageSize, String sortBy, String sortOrder);

    ProductResponse getProductsByKeyword(String keyword, int pageNumber, int pageSize, String sortBy, String sortOrder);

    ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException;
}
