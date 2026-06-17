package com.ecommerce.backend.service;

import com.ecommerce.backend.payload.CartDTO;
import com.ecommerce.backend.security.response.MessageResponse;
import jakarta.transaction.Transactional;

import java.util.List;

public interface CartService {
    public CartDTO addProductToCart(Long productId, int quantity) ;

    List<CartDTO> getCarts();

    CartDTO getCart(String loggedInEmail);

    CartDTO updateProductCart(Long productId, int delete);

    MessageResponse deleteProductFromCart(Long cartId, Long productId);

}
