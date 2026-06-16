package com.ecommerce.backend.service;

import com.ecommerce.backend.payload.CartDTO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CartService {
    public CartDTO addProductToCart(Long productId, int quantity) ;

    List<CartDTO> getCarts();
}
