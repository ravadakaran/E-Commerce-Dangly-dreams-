package com.ecommerce.jewelleryMart.controller;

import com.ecommerce.jewelleryMart.model.Order;
import com.ecommerce.jewelleryMart.repository.OrderRepository;
import com.ecommerce.jewelleryMart.repository.ProductRepository;
import com.ecommerce.jewelleryMart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/analytics")
    public ResponseEntity<Map<String, Object>> getAnalytics() {
        List<Order> allOrders = orderRepository.findAll();
        long productCount = productRepository.count();
        long userCount = userRepository.count();

        double totalRevenue = allOrders.stream()
                .mapToDouble(Order::getTotalAmount)
                .sum();

        Date now = new Date();
        long oneDayMs = 24 * 60 * 60 * 1000L;
        long thirtyDaysMs = 30 * oneDayMs;
        long oneYearMs = 365 * oneDayMs;

        long dailyOrders = allOrders.stream()
                .filter(o -> o.getCreatedAt() != null && (now.getTime() - o.getCreatedAt().getTime()) < oneDayMs)
                .count();

        long monthlyOrders = allOrders.stream()
                .filter(o -> o.getCreatedAt() != null && (now.getTime() - o.getCreatedAt().getTime()) < thirtyDaysMs)
                .count();

        long yearlyOrders = allOrders.stream()
                .filter(o -> o.getCreatedAt() != null && (now.getTime() - o.getCreatedAt().getTime()) < oneYearMs)
                .count();

        double monthlyRevenue = allOrders.stream()
                .filter(o -> o.getCreatedAt() != null && (now.getTime() - o.getCreatedAt().getTime()) < thirtyDaysMs)
                .mapToDouble(Order::getTotalAmount)
                .sum();

        double yearlyRevenue = allOrders.stream()
                .filter(o -> o.getCreatedAt() != null && (now.getTime() - o.getCreatedAt().getTime()) < oneYearMs)
                .mapToDouble(Order::getTotalAmount)
                .sum();

        List<Order> recentOrders = allOrders.stream()
                .sorted(Comparator.comparing(Order::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(10)
                .collect(Collectors.toList());

        Map<String, Object> analytics = new HashMap<>();
        analytics.put("products", productCount);
        analytics.put("orders", allOrders.size());
        analytics.put("users", userCount);
        analytics.put("totalRevenue", totalRevenue);
        analytics.put("dailyOrders", dailyOrders);
        analytics.put("monthlyOrders", monthlyOrders);
        analytics.put("yearlyOrders", yearlyOrders);
        analytics.put("monthlyRevenue", monthlyRevenue);
        analytics.put("yearlyRevenue", yearlyRevenue);
        analytics.put("recentOrders", recentOrders);

        return ResponseEntity.ok(analytics);
    }
}
