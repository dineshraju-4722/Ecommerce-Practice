package com.ecommerce.backend.repositories;

import com.ecommerce.backend.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository  extends JpaRepository<Cart, Long> {
    Cart findByUserEmail(String email);
}
