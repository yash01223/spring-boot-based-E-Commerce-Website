package com.spring.ecommerce.E_commerce.website.Repository;


import com.spring.ecommerce.E_commerce.website.Entity.Order;
import com.spring.ecommerce.E_commerce.website.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);

}
