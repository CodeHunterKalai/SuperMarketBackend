# Supermarket Management System - Backend API

## Overview
Spring Boot REST API for Supermarket Management System with barcode-centric inventory and billing.

## Technology Stack
- Java 17
- Spring Boot 3.2.0
- Spring Data JPA
- H2 Database (Development)
- MySQL (Production Ready)
- Lombok
- Maven

## Database Configuration

### Development (H2 - In Memory)
The application uses H2 in-memory database by default. Access H2 console at:
\`\`\`
http://localhost:8080/h2-console
URL: jdbc:h2:mem:supermarketdb
Username: sa
Password: (leave blank)
\`\`\`

### Production (MySQL)
Uncomment MySQL configuration in `application.properties` and create database:
\`\`\`sql
CREATE DATABASE supermarket_db;
\`\`\`

## Running the Application

### Prerequisites
- JDK 17 or higher
- Maven 3.6+

### Build and Run
\`\`\`bash
cd backend
mvn clean install
mvn spring-boot:run
\`\`\`

The API will be available at `http://localhost:8080`

## API Endpoints

### Products
- `GET /api/products` - Get all products
- `GET /api/products/{id}` - Get product by ID
- `GET /api/products/barcode/{barcode}` - Get product by barcode
- `GET /api/products/search?keyword={keyword}` - Search products
- `GET /api/products/categories` - Get all categories
- `GET /api/products/low-stock` - Get low stock products
- `GET /api/products/out-of-stock` - Get out of stock products
- `POST /api/products` - Create new product
- `PUT /api/products/{id}` - Update product
- `DELETE /api/products/{id}` - Delete product
- `PATCH /api/products/{id}/adjust-stock` - Adjust stock quantity
- `GET /api/products/{id}/stock-movements` - Get stock movement history

### Billing
- `POST /api/bills` - Create new bill
- `GET /api/bills` - Get all bills
- `GET /api/bills/{id}` - Get bill by ID
- `GET /api/bills/number/{billNumber}` - Get bill by number
- `GET /api/bills/date-range?start={start}&end={end}` - Get bills by date range

### Reports
- `GET /api/reports/dashboard` - Get dashboard statistics
- `GET /api/reports/sales/daily` - Get daily sales report
- `GET /api/reports/sales/monthly` - Get monthly sales report
- `GET /api/reports/sales/custom?startDate={start}&endDate={end}` - Get custom date range report

## Request Examples

### Create Product
\`\`\`json
POST /api/products
{
  "name": "Coca Cola 500ml",
  "category": "Beverages",
  "barcode": "8901234567890",
  "price": 45.00,
  "quantity": 100,
  "lowStockThreshold": 20
}
\`\`\`

### Create Bill
\`\`\`json
POST /api/bills
{
  "items": [
    {
      "barcode": "8901234567890",
      "quantity": 2
    }
  ],
  "taxRate": 5.0,
  "discount": 10.0
}
\`\`\`

## Features
- Barcode-centric product management
- Duplicate barcode prevention
- Automatic stock deduction on billing
- Stock movement tracking
- Low stock alerts
- Sales reports and analytics
- Transaction history
- Dashboard statistics

## Error Handling
The API returns appropriate HTTP status codes:
- `200 OK` - Success
- `201 Created` - Resource created
- `204 No Content` - Deleted successfully
- `400 Bad Request` - Validation error
- `404 Not Found` - Resource not found
- `409 Conflict` - Duplicate barcode
- `500 Internal Server Error` - Server error

## CORS Configuration
CORS is configured to allow requests from:
- `http://localhost:3000` (React default)
- `http://localhost:5173` (Vite default)
