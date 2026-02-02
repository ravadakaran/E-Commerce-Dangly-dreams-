package com.ecommerce.jewelleryMart.controller;

import com.ecommerce.jewelleryMart.model.Product;
import com.ecommerce.jewelleryMart.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String metalType,
            @RequestParam(required = false) Boolean featured,
            @RequestParam(required = false) Boolean bestSeller,
            @RequestParam(required = false) Boolean discount,
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false) Integer limit
    ) {
        List<Product> products;

        if (Boolean.TRUE.equals(featured)) {
            products = productRepository.findByFeaturedTrue();
        } else if (Boolean.TRUE.equals(bestSeller)) {
            products = productRepository.findByBestSellerTrue();
        } else if (Boolean.TRUE.equals(discount)) {
            products = productRepository.findByDiscountPercentageGreaterThan(0);
        } else if (categoryId != null && !categoryId.isEmpty()) {
            products = productRepository.findByCategoryId(categoryId);
        } else if (search != null && !search.isEmpty()) {
            products = productRepository.findByNameContainingIgnoreCase(search);
        } else {
            products = new java.util.ArrayList<>();
            productRepository.findAll().forEach(products::add);
        }

        if (category != null && !category.isEmpty()) {
            products = products.stream()
                    .filter(p -> p.getCategory() != null && p.getCategory().equalsIgnoreCase(category))
                    .collect(Collectors.toList());
        }

        if (metalType != null && !metalType.isEmpty()) {
            products = products.stream()
                    .filter(p -> p.getMetalType() != null && p.getMetalType().equalsIgnoreCase(metalType))
                    .collect(Collectors.toList());
        }

        if (sort != null && !sort.isEmpty()) {
            switch (sort) {
                case "priceLowToHigh":
                case "price-asc":
                    products.sort(Comparator.comparingDouble(Product::getPrice));
                    break;
                case "priceHighToLow":
                case "price-desc":
                    products.sort((a, b) -> Double.compare(b.getPrice(), a.getPrice()));
                    break;
                case "nameAsc":
                    products.sort(Comparator.comparing(Product::getName, String.CASE_INSENSITIVE_ORDER));
                    break;
                case "nameDesc":
                    products.sort(Comparator.comparing(Product::getName, String.CASE_INSENSITIVE_ORDER).reversed());
                    break;
                default:
                    break;
            }
        }

        if (limit != null && limit > 0 && products.size() > limit) {
            products = products.subList(0, limit);
        }

        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable String id) {
        Optional<Product> product = productRepository.findById(id);
        return product.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product savedProduct = productRepository.save(product);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable String id, @RequestBody Product productDetails) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();

            // Update ALL fields explicitly
            product.setName(productDetails.getName());
            product.setPrice(productDetails.getPrice());
            product.setCategory(productDetails.getCategory());
            product.setMetalType(productDetails.getMetalType());
            product.setImage(productDetails.getImage());
            product.setImageUrl(productDetails.getImageUrl() != null ? productDetails.getImageUrl() : productDetails.getImage());
            product.setDescription(productDetails.getDescription());
            product.setWeight(productDetails.getWeight());
            product.setStock(productDetails.getStock());
            product.setFeatured(productDetails.isFeatured());
            product.setDiscountPercentage(productDetails.getDiscountPercentage());
            product.setBestSeller(productDetails.isBestSeller());
            if (productDetails.getCategoryId() != null) product.setCategoryId(productDetails.getCategoryId());

            Product savedProduct = productRepository.save(product);


            return ResponseEntity.ok(savedProduct);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteProduct(@PathVariable String id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
