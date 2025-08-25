package com.spring.ecommerce.E_commerce.website.Service;

import com.spring.ecommerce.E_commerce.website.Entity.Category;
import com.spring.ecommerce.E_commerce.website.Repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name).orElse(null);
    }

    public void saveCategory(Category category) {
        categoryRepository.save(category);
    }
}