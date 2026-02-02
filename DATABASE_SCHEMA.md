# Database Schema (MongoDB Collections)

## Users
| Field | Type | Description |
|-------|------|-------------|
| _id | String | ObjectId |
| name | String | Display name |
| username | String | Username |
| email | String | Unique |
| password | String | BCrypt hashed |
| profilePic | String | URL |
| role | String | ADMIN / USER |
| isAdmin | Boolean | Legacy, derived from role |
| createdAt | Date | |

## Categories
| Field | Type | Description |
|-------|------|-------------|
| _id | String | ObjectId |
| categoryName | String | |
| status | String | active / inactive |

## Products
| Field | Type | Description |
|-------|------|-------------|
| _id | String | ObjectId |
| name | String | |
| description | String | |
| price | Number | |
| stock | Integer | |
| category | String | |
| categoryId | String | Reference to Category |
| metalType | String | |
| image | String | URL |
| imageUrl | String | URL |
| weight | Number | |
| featured | Boolean | |
| discountPercentage | Number | 0-100 |
| bestSeller | Boolean | |
| createdAt | Date | |

## Orders
| Field | Type | Description |
|-------|------|-------------|
| _id | String | ObjectId |
| userId | String | User email/id |
| productIds | Array | |
| quantities | Array | |
| totalAmount | Number | |
| orderStatus | String | PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED |
| paymentStatus | String | PENDING, COMPLETED, FAILED, REFUNDED |
| orderItems | Array | [{ productId, productName, quantity, price }] |
| deliveryName | String | |
| deliveryContact | String | |
| deliveryAddress | String | |
| deliveryCity | String | |
| createdAt | Date | |

## Order_Items (embedded in Order)
| Field | Type |
|-------|------|
| productId | String |
| productName | String |
| quantity | Integer |
| price | Number |

## Payments
| Field | Type | Description |
|-------|------|-------------|
| _id | String | ObjectId |
| orderId | String | Reference to Order |
| paymentMethod | String | Razorpay / Card / PayPal / COD |
| paymentStatus | String | pending / completed / failed |
| transactionId | String | Razorpay payment id, etc. |
| createdAt | Date | |

## Cart
| Field | Type | Description |
|-------|------|-------------|
| _id | String | ObjectId |
| userId | String | User email |
| productIds | Array | |
| quantities | Array | |
| grams | Array | |
| finalPrices | Array | |
