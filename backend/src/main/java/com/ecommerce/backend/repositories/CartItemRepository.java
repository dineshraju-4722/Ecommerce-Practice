package com.ecommerce.backend.repositories;

import com.ecommerce.backend.model.Cart;
import com.ecommerce.backend.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByProduct_ProductIdAndCart_CartId(Long productId, Long cartId);
}
