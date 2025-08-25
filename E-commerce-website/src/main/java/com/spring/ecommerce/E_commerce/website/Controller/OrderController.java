package com.spring.ecommerce.E_commerce.website.Controller;

import com.spring.ecommerce.E_commerce.website.Entity.Order;
import com.spring.ecommerce.E_commerce.website.Entity.OrderItem;
import com.spring.ecommerce.E_commerce.website.Entity.Product;

import com.spring.ecommerce.E_commerce.website.Entity.User;
import com.spring.ecommerce.E_commerce.website.Service.OrderService;
import com.spring.ecommerce.E_commerce.website.Service.ProductService;
import com.spring.ecommerce.E_commerce.website.Service.ShoppingCartService;
import com.spring.ecommerce.E_commerce.website.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ShoppingCartService shoppingCartService;

    @GetMapping("/cart")
    public String viewCart(HttpSession session, Model model) {
        Map<Long, Integer> cart = getCartFromSession(session);
        List<Product> products = shoppingCartService.getCartProducts(cart);
        BigDecimal total = shoppingCartService.getCartTotal(cart);

        model.addAttribute("products", products);
        model.addAttribute("cart", cart);
        model.addAttribute("total", total);
        return "cart";
    }

    @PostMapping("/add-to-cart")
    public String addToCart(@RequestParam Long productId,
                            @RequestParam(defaultValue = "1") int quantity, HttpSession session) {
        Map<Long, Integer> cart = getCartFromSession(session);
        shoppingCartService.addToCart(cart, productId, quantity);
        session.setAttribute("cart", cart);
        return "redirect:/order/cart";
    }

    @GetMapping("/remove-from-cart/{id}")
    public String removeFromCart(@PathVariable Long id, HttpSession session) {
        Map<Long, Integer> cart = getCartFromSession(session);
        shoppingCartService.removeFromCart(cart, id);
        session.setAttribute("cart", cart);
        return "redirect:/order/cart";
    }

    @PostMapping("/update-cart")
    public String updateCart(@RequestParam Map<String, String> params, HttpSession session) {
        Map<Long, Integer> cart = getCartFromSession(session);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (entry.getKey().startsWith("quantity_")) {
                Long productId = Long.parseLong(entry.getKey().substring(9));
                int quantity = Integer.parseInt(entry.getValue());
                shoppingCartService.updateQuantity(cart, productId, quantity);
            }
        }
        session.setAttribute("cart", cart);
        return "redirect:/order/cart";
    }

    @GetMapping("/checkout")
    public String checkout(HttpSession session, Model model) {
        Map<Long, Integer> cart = getCartFromSession(session);
        if (cart.isEmpty()) {
            return "redirect:/order/cart";
        }

        List<Product> products = shoppingCartService.getCartProducts(cart);
        BigDecimal total = shoppingCartService.getCartTotal(cart);

        model.addAttribute("products", products);
        model.addAttribute("cart", cart);
        model.addAttribute("total", total);
        return "checkout";
    }

    @PostMapping("/place-order")
    public String placeOrder(HttpSession session, Principal principal) {
        Map<Long, Integer> cart = getCartFromSession(session);
        if (cart.isEmpty()) {
            return "redirect:/order/cart";
        }

        User user = userService.findByEmail(principal.getName());
        Order order = new Order();
        order.setUser(user);
        order.setStatus("PROCESSING");

        List<OrderItem> orderItems = new ArrayList<>();
        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
            Product product = productService.getProductById(entry.getKey());
            if (product != null) {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setProduct(product);
                orderItem.setQuantity(entry.getValue());
                orderItem.setPrice(product.getPrice());
                orderItems.add(orderItem);
            }
        }

        order.setOrderItems(orderItems);
        orderService.saveOrder(order);

        // Clear cart
        session.removeAttribute("cart");

        return "redirect:/order/confirmation/" + order.getId();
    }

    @GetMapping("/confirmation/{id}")
    public String orderConfirmation(@PathVariable Long id, Model model) {
        Order order = orderService.getOrderById(id);
        model.addAttribute("order", order);
        return "order_confirmation";
    }

    @GetMapping("/history")
    public String orderHistory(Principal principal, Model model) {
        User user = userService.findByEmail(principal.getName());
        List<Order> orders = orderService.getOrdersByUser(user);
        model.addAttribute("orders", orders);
        return "order_history";
    }

    private Map<Long, Integer> getCartFromSession(HttpSession session) {
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
            session.setAttribute("cart", cart);
        }
        return cart;
    }
}