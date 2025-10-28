# catalog-api-0.0.1-SNAPSHOT.jar
___
# 🛍️ Product Catalog API
___
# 📘 About the Project

Product Catalog API is a RESTful service for managing products and categories.
It is built with Spring Boot, PostgreSQL, Redis (Redisson), and Liquibase, following modern backend development
principles and production-grade design.
___

# 🧩 Core Features

# 🔹 Categories

- Full CRUD operations
- Filtering products by category

# 🔹 Products

- Full CRUD operations
- Pagination and filtering (by categoryId, availability)
- Redis caching for frequently accessed endpoints
- Validation and centralized error handling
___

# 🧠 Database Model

**Entities**:

- CategoryEntity
     - id
     - name
     - description
     - createdAt / updatedAt

- ProductEntity
     - id
     - name
     - description
     - price
     - productCount
     - availability (Enum: IN_STOCK / LOW_STOCK / OUT_OF_STOCK)
     - category (ManyToOne relationship with CategoryEntity)
     - createdAt / updatedAt
___

# 🚀 Tech Stack
| Technology                      | Purpose                   |
| ------------------------------- |---------------------------|
| **Java 17+**                    | Programming Language      |
| **Spring Boot**                 | Backend Framework         |
| **Spring Data JPA**             | ORM & Database operations |
| **PostgreSQL**                  | Primary Database          |
| **Liquibase**                   | Database version control  |
| **Redis (Redisson)**            | Caching layer             |
| **Docker**                      | Containerization          |
| **Lombok**                      | Boilerplate reduction     |
| **Spring Validation**           | Input validation          |
| **Swagger (Springdoc OpenAPI)** | API documentation         |