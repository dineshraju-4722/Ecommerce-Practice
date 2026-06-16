package com.ecommerce.backend.service;

import com.ecommerce.backend.payload.CartDTO;
import org.springframework.stereotype.Service;

public interface CartService {
    public CartDTO addProductToCart(Long productId, int quantity) ;
}
