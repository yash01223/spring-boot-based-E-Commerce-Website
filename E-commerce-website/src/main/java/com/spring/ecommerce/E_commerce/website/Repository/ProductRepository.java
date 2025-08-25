package com.spring.ecommerce.E_commerce.website.Repository;

import com.spring.ecommerce.E_commerce.website.Entity.Category;
import com.spring.ecommerce.E_commerce.website.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(Category category);

}
