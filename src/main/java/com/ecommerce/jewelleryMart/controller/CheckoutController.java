package com.ecommerce.jewelleryMart.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.jewelleryMart.model.Cart;
import com.ecommerce.jewelleryMart.model.Order;
import com.ecommerce.jewelleryMart.model.OrderItem;
import com.ecommerce.jewelleryMart.model.Payment;
import com.ecommerce.jewelleryMart.model.Product;
import com.ecommerce.jewelleryMart.repository.CartRepository;
import com.ecommerce.jewelleryMart.repository.OrderRepository;
import com.ecommerce.jewelleryMart.repository.PaymentRepository;
import com.ecommerce.jewelleryMart.repository.ProductRepository;

@RestController
@RequestMapping("/api/checkout")
@CrossOrigin(origins = "*")
public class CheckoutController {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ProductRepository productRepository;

    // ---------------------------
    // GET CART SUMMARY
    // ---------------------------
    @GetMapping("/{userId}")
    public ResponseEntity<?> getCartSummary(@PathVariable String userId) {

        Optional<Cart> optionalCart = cartRepository.findByUserId(userId);
        if (optionalCart.isEmpty() || optionalCart.get().getProductIds().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Cart not found or empty");
        }

        Cart cart = optionalCart.get();

        List<String> productIds = cart.getProductIds();
        List<Integer> quantities = cart.getQuantities();
        List<Double> grams = cart.getGrams();
        List<Double> finalPrices = cart.getFinalPrices();

        // Convert Iterable -> List safely
        List<Product> allProducts = new ArrayList<>();
        productRepository.findAll().forEach(allProducts::add);

        Map<String, Product> productMap = allProducts.stream()
                .collect(Collectors.toMap(Product::getId, p -> p));

        List<Map<String, Object>> items = new ArrayList<>();
        double totalAmount = 0;

        for (int i = 0; i < productIds.size(); i++) {
            Product product = productMap.get(productIds.get(i));
            if (product == null)
                continue;

            int qty = quantities.get(i);
            double gram = grams.get(i);
            double finalPrice = finalPrices.get(i);

            double itemTotal = finalPrice * qty;
            totalAmount += itemTotal;

            Map<String, Object> item = new HashMap<>();
            item.put("productId", product.getId());
            item.put("productName", product.getName());
            item.put("quantity", qty);
            item.put("grams", gram);
            item.put("price", product.getPrice());
            item.put("finalPrice", finalPrice);
            item.put("itemTotal", itemTotal);

            items.add(item);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("userId", userId);
        response.put("items", items);
        response.put("totalAmount", totalAmount);

        return ResponseEntity.ok(response);
    }

    // ---------------------------
    // CONFIRM PAYMENT
    // ---------------------------
    @PostMapping("/confirm-payment")
    public ResponseEntity<?> confirmPayment(@RequestBody Map<String, Object> payload) {

        String userId = (String) payload.get("userId");
        Map<String, Object> delivery = (Map<String, Object>) payload.get("delivery");
        double discount = payload.get("discount") == null ? 0.0
                : ((Number) payload.get("discount")).doubleValue();
        String paymentMethod = payload.get("paymentMethod") != null ? (String) payload.get("paymentMethod") : "COD";
        String transactionId = payload.get("transactionId") != null ? (String) payload.get("transactionId") : null;

        if (userId == null || delivery == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Invalid request data"));
        }

        Optional<Cart> optionalCart = cartRepository.findByUserId(userId);
        if (optionalCart.isEmpty() || optionalCart.get().getProductIds().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Cart is empty"));
        }

        Cart cart = optionalCart.get();

        List<String> productIds = cart.getProductIds();
        List<Integer> quantities = cart.getQuantities();
        List<Double> grams = cart.getGrams();
        List<Double> finalPrices = cart.getFinalPrices();

        List<Product> products = new ArrayList<>();
        productRepository.findAllById(productIds).forEach(products::add);

        Map<String, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getId, p -> p));

        List<Map<String, Object>> invoiceItems = new ArrayList<>();
        List<OrderItem> orderItems = new ArrayList<>();
        double totalAmount = 0;

        for (int i = 0; i < productIds.size(); i++) {
            Product product = productMap.get(productIds.get(i));
            if (product == null)
                continue;

            int qty = quantities.get(i);
            double gram = grams.get(i);
            double finalPrice = finalPrices.get(i);

            double itemTotal = finalPrice * qty;
            totalAmount += itemTotal;

            invoiceItems.add(Map.of(
                    "productId", product.getId(),
                    "productName", product.getName(),
                    "quantity", qty,
                    "grams", gram,
                    "price", product.getPrice(),
                    "finalPrice", finalPrice,
                    "itemTotal", itemTotal));

            orderItems.add(new OrderItem(product.getId(), product.getName(), qty, finalPrice * qty));
        }

        totalAmount = Math.max(totalAmount - discount, 0);

        Order order = new Order(userId,
                new ArrayList<>(productIds),
                new ArrayList<>(quantities),
                totalAmount);

        order.setGrams(new ArrayList<>(grams));
        order.setOrderItems(orderItems);
        order.setDeliveryName((String) delivery.get("name"));
        order.setDeliveryContact((String) delivery.get("contact"));
        order.setDeliveryAddress((String) delivery.get("address"));
        order.setDeliveryCity((String) delivery.get("city"));
        order.setOrderStatus("CONFIRMED");
        order.setPaymentStatus("COMPLETED");

        Order savedOrder = orderRepository.save(order);

        Payment payment = new Payment(savedOrder.getId(), paymentMethod, "COMPLETED", transactionId);
        paymentRepository.save(payment);

        // Clear cart
        cart.setProductIds(new ArrayList<>());
        cart.setQuantities(new ArrayList<>());
        cart.setGrams(new ArrayList<>());
        cart.setFinalPrices(new ArrayList<>());
        cartRepository.save(cart);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Payment successful");
        response.put("orderId", savedOrder.getId());
        response.put("totalAmount", totalAmount);
        response.put("items", invoiceItems);
        // Additional fields expected by the frontend Success page
        response.put("discount", discount);
        response.put("delivery", Map.of(
                "name", order.getDeliveryName(),
                "contact", order.getDeliveryContact(),
                "address", order.getDeliveryAddress(),
                "city", order.getDeliveryCity()));
        response.put("orderDate", savedOrder.getCreatedAt());

        return ResponseEntity.ok(response);
    }
}
