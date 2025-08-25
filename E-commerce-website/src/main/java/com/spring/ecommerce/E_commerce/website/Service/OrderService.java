package com.spring.ecommerce.E_commerce.website.Service;

import com.spring.ecommerce.E_commerce.website.Entity.Order;
import com.spring.ecommerce.E_commerce.website.Entity.User;
import com.spring.ecommerce.E_commerce.website.Repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public void saveOrder(Order order) {
        orderRepository.save(order);
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUser(user);
    }
}