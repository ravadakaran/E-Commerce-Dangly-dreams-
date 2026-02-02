# JewelleryMart (Dangly Dreams)

## Your Online Destination for Exquisite Jewelry

**JewelleryMart** is a comprehensive e-commerce application designed to showcase and sell beautiful jewelry. It's built with **Spring Boot** (backend), **React + Vite + Tailwind CSS** (frontend), and **MongoDB**.

## Tech Stack

- **Frontend:** React, Vite, Tailwind CSS
- **Backend:** Spring Boot, Spring Security
- **Database:** MongoDB
- **Auth:** JWT (access + refresh tokens)
- **Payments:** Razorpay, Cash on Delivery

## How to Run

### Prerequisites
- Java 11+
- Node.js 18+
- MongoDB (local or Atlas)

### Backend
```bash
cd E-Commerce-Spring
./mvnw spring-boot:run
```
Backend runs on `http://localhost:8081`

### Frontend
```bash
cd E-Commerce-Spring/client
npm install
npm run dev
```
Frontend runs on `http://localhost:5173` and proxies `/api` to the backend.

### Environment Variables
See `.env.example` for backend variables. Set `RAZORPAY_KEY_ID` and `RAZORPAY_KEY_SECRET` for Razorpay payments.

## Features

- **Users:** Register, login, forgot password, browse products, cart, checkout
- **Admin:** Dashboard analytics, products, categories, orders, users, coupons
- **Products:** Featured, Best Seller, Discount filters; stock, category management
- **Payments:** Razorpay, Cash on Delivery

## API Documentation
See [API_DOCUMENTATION.md](API_DOCUMENTATION.md)

## Know about this

https://excalidraw.com/#json=lq59ZBAmcw1TmIV13AqOS,rwsqV-A8kB9kPPBTrtihyg



## Project Structure

````

jewelleryMart
├─ .mvn
│  └─ wrapper
│     └─ maven-wrapper.properties
├─ client
│  ├─ eslint.config.js
│  ├─ index.html
│  ├─ package-lock.json
│  ├─ package.json
│  ├─ public
│  │  ├─ frame.png
│  │  └─ vite.svg
│  ├─ README.md
│  ├─ src
│  │  ├─ App.jsx
│  │  ├─ assets
│  │  │  └─ react.svg
│  │  ├─ components
│  │  │  └─ AdminLayout.jsx
│  │  ├─ hooks
│  │  │  └─ useCheckoutGuard.js
│  │  ├─ index.css
│  │  ├─ main.jsx
│  │  ├─ pages
│  │  │  ├─ Account.jsx
│  │  │  ├─ admin
│  │  │  │  ├─ AdminDashboard.jsx
│  │  │  │  ├─ CartsAdmin.jsx
│  │  │  │  ├─ CouponsAdmin.jsx
│  │  │  │  ├─ Dashboard.jsx
│  │  │  │  ├─ OrdersAdmin.jsx
│  │  │  │  ├─ ProductsAdmin.jsx
│  │  │  │  └─ UsersAdmin.jsx
│  │  │  ├─ Cart.jsx
│  │  │  ├─ Category.jsx
│  │  │  ├─ Checkout.jsx
│  │  │  ├─ Collections.jsx
│  │  │  ├─ Contact.jsx
│  │  │  ├─ Delivery.jsx
│  │  │  ├─ ForgetPassword.jsx
│  │  │  ├─ Home.jsx
│  │  │  ├─ Login.jsx
│  │  │  ├─ Payment.jsx
│  │  │  ├─ Product.jsx
│  │  │  ├─ Signup.jsx
│  │  │  └─ Success.jsx
│  │  └─ utils
│  │     └─ storage.js
│  ├─ tailwind.config.js
│  └─ vite.config.js
├─ mvnw
├─ mvnw.cmd
├─ pom.xml
├─ README.md
└─ src
   └─ main
      ├─ java
      │  └─ com
      │     └─ ecommerce
      │        └─ jewelleryMart
      │           ├─ config
      │           │  └─ SecurityConfig.java
      │           ├─ controller
      │           │  ├─ AuthController.java
      │           │  ├─ CartController.java
      │           │  ├─ CheckoutController.java
      │           │  ├─ CouponController.java
      │           │  ├─ OrderController.java
      │           │  ├─ ProductController.java
      │           │  └─ UserController.java
      │           ├─ JewelleryMartApplication.java
      │           ├─ model
      │           │  ├─ Cart.java
      │           │  ├─ Coupon.java
      │           │  ├─ Order.java
      │           │  ├─ Product.java
      │           │  └─ User.java
      │           └─ repository
      │              ├─ CartRepository.java
      │              ├─ CouponRepository.java
      │              ├─ OrderRepository.java
      │              ├─ ProductRepository.java
      │              └─ UserRepository.java
      └─ resources
         ├─ application.properties
         └─ templates
    

