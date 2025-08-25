package com.spring.ecommerce.E_commerce.website.Service;

import com.spring.ecommerce.E_commerce.website.Entity.Product;
import com.spring.ecommerce.E_commerce.website.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ShoppingCartService {
    @Autowired
    private ProductService productService;

    public void addToCart(Map<Long, Integer> cart, Long productId, int quantity) {
        cart.put(productId, cart.getOrDefault(productId, 0) + quantity);
    }

    public void removeFromCart(Map<Long, Integer> cart, Long productId) {
        cart.remove(productId);
    }

    public void updateQuantity(Map<Long, Integer> cart, Long productId, int quantity) {
        if (quantity <= 0) {
            cart.remove(productId);
        } else {
            cart.put(productId, quantity);
        }
    }

    public BigDecimal getCartTotal(Map<Long, Integer> cart) {
        BigDecimal total = BigDecimal.ZERO;
        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
            Product product = productService.getProductById(entry.getKey());
            if (product != null) {
                total = total.add(product.getPrice().multiply(BigDecimal.valueOf(entry.getValue())));
            }
        }
        return total;
    }

    public List<Product> getCartProducts(Map<Long, Integer> cart) {
        List<Product> products = new ArrayList<>();
        for (Long productId : cart.keySet()) {
            Product product = productService.getProductById(productId);
            if (product != null) {
                products.add(product);
            }
        }
        return products;
    }
}