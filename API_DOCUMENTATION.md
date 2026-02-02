# JewelleryMart API Documentation

## Base URL
- Development: `http://localhost:8081`
- Frontend proxies `/api` to backend

---

## Authentication

### JWT Flow
- **Login** returns `accessToken` and `refreshToken`
- Send `Authorization: Bearer <accessToken>` for protected endpoints
- Refresh token via `POST /api/auth/refresh`

### Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/signup` | Register user |
| POST | `/api/auth/login` | Login, returns JWT + user |
| POST | `/api/auth/refresh` | Refresh access token |
| GET | `/api/auth/user?email=` | Get user by email |
| PUT | `/api/auth/user?email=` | Update user |
| PUT | `/api/auth/reset-password` | Forgot password |

---

## Products

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/products` | List products. Query: `search`, `sort`, `category`, `metalType`, `featured`, `bestSeller`, `discount`, `categoryId`, `limit` |
| GET | `/api/products/{id}` | Get product by ID |
| POST | `/api/products` | Create product (admin) |
| PUT | `/api/products/{id}` | Update product (admin) |
| DELETE | `/api/products/{id}` | Delete product (admin) |

### Product Fields
- `name`, `description`, `price`, `stock`, `category`, `categoryId`, `metalType`
- `image`, `imageUrl`, `weight`
- `featured` (boolean), `bestSeller` (boolean), `discountPercentage` (number)

---

## Categories

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/categories` | List categories. Query: `status` |
| GET | `/api/categories/{id}` | Get category |
| POST | `/api/categories` | Create category |
| PUT | `/api/categories/{id}` | Update category |
| DELETE | `/api/categories/{id}` | Delete category |

---

## Cart

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/cart/{userId}` | Get user cart |
| POST | `/api/cart/add?userId=&productId=&quantity=&grams=&finalPrice=` | Add item |
| DELETE | `/api/cart/remove?userId=&productId=` | Remove item |
| PUT | `/api/cart/update?userId=&productId=&quantity=` | Update quantity |

---

## Checkout

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/checkout/{userId}` | Get cart summary for checkout |
| POST | `/api/checkout/confirm-payment` | Confirm order. Body: `{ userId, delivery, discount?, paymentMethod?, transactionId? }` |

---

## Payments

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/payments/razorpay/config` | Get Razorpay key (if configured) |
| POST | `/api/payments/razorpay/create-order` | Create Razorpay order. Body: `{ amount, currency? }` |

---

## Orders

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/orders` | List all orders |
| GET | `/api/orders/{orderId}` | Get order |
| POST | `/api/orders` | Create order |
| DELETE | `/api/orders/{orderId}` | Delete order |

---

## Admin

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/admin/analytics` | Dashboard analytics: products, orders, users, revenue, recent orders |

---

## Users

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/users` | List users |
| GET | `/api/users/{userId}` | Get user |
| PUT | `/api/users/{userId}` | Update user |
| DELETE | `/api/users/{userId}` | Delete user |

---

## Environment Variables

### Backend (`application.properties` / env)
- `MONGODB_URI` – MongoDB connection string
- `PORT` – Server port (default 8081)
- `JWT_SECRET` – Secret for JWT signing
- `RAZORPAY_KEY_ID` – Razorpay key (for payments)
- `RAZORPAY_KEY_SECRET` – Razorpay secret

### Frontend (`.env`)
- `VITE_API_URL` – Backend URL (optional; dev uses proxy)
