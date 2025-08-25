package com.spring.ecommerce.E_commerce.website.Controller;

import com.spring.ecommerce.E_commerce.website.Entity.Category;
import com.spring.ecommerce.E_commerce.website.Entity.Product;
import com.spring.ecommerce.E_commerce.website.Service.CategoryService;
import com.spring.ecommerce.E_commerce.website.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/")
    public String viewHomePage(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "index";
    }

    @GetMapping("/product/{id}")
    public String viewProduct(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "product_detail";
    }

    @GetMapping("/products/category/{name}")
    public String viewProductsByCategory(@PathVariable String name, Model model) {
        Category category = categoryService.getCategoryByName(name);
        if (category != null) {
            model.addAttribute("products", productService.getProductsByCategory(category));
            model.addAttribute("category", name);
        }
        return "products_by_category";
    }
}
