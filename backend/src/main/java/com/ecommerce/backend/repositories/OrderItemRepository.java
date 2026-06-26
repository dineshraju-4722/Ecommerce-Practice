package com.ecommerce.backend.repositories;

import com.ecommerce.backend.model.Order;
import com.ecommerce.backend.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {
}
