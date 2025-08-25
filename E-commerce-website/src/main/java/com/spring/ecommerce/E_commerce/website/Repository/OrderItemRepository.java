package com.spring.ecommerce.E_commerce.website.Repository;

import com.spring.ecommerce.E_commerce.website.Entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
