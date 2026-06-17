package com.ecommerce.backend.repositories;

import com.ecommerce.backend.model.Cart;
import com.ecommerce.backend.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByProduct_ProductIdAndCart_CartId(Long productId, Long cartId);

    @Modifying
    @Query(" delete from CartItem ci where ci.cart.cartId = ?2 and ci.product.productId = ?1")
    void deleteByProduct_ProductIdAndCart_CartId(Long productProductId, Long cartCartId);
}
