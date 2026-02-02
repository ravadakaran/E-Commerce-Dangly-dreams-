package com.ecommerce.jewelleryMart.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "products")
public class Product {

    @Id
    private String id;
    private String name;
    private String description;
    private double price;
    private int stock = 0;
    private String category;
    private String categoryId;
    private String metalType;
    private String image;
    private String imageUrl;
    private double weight;
    private boolean featured = false;
    private double discountPercentage = 0;
    private boolean bestSeller = false;
    private Date createdAt;

    public Product() {
        this.createdAt = new Date();
    }

    public Product(String name, double price, String category, String metalType, String image, String description) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.metalType = metalType;
        this.image = image;
        this.imageUrl = image;
        this.description = description;
        this.createdAt = new Date();
    }

    // --- Getters and setters ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getMetalType() { return metalType; }
    public void setMetalType(String metalType) { this.metalType = metalType; }

    public String getImage() { return image; }
    public void setImage(String image) {
        this.image = image;
        if (this.imageUrl == null) this.imageUrl = image;
    }

    public String getImageUrl() { return imageUrl != null ? imageUrl : image; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }

    public boolean isFeatured() { return featured; }
    public void setFeatured(boolean featured) { this.featured = featured; }

    public double getDiscountPercentage() { return discountPercentage; }
    public void setDiscountPercentage(double discountPercentage) { this.discountPercentage = discountPercentage; }

    public boolean isBestSeller() { return bestSeller; }
    public void setBestSeller(boolean bestSeller) { this.bestSeller = bestSeller; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
