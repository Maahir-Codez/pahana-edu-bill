# Pahana Edu Billing System - Backend API

This project is the backend RESTful web service for the Pahana Edu Online Billing System. It is a distributed application built entirely with Java EE (Servlets) without the use of external frameworks like Spring. It handles all business logic, data persistence, and provides a complete JSON-based API for any frontend client.

---

## Features

- **Secure Authentication:** User registration and login endpoints using a salted SHA-256 password hashing mechanism.
- **Full CRUD for Customers:** Endpoints for Creating, Reading, Updating, and Deactivating (soft delete) customer records.
- **Full CRUD for Items:** Endpoints for managing inventory items, including stock levels.
- **Transactional Order Management:** An endpoint for creating complex orders that transactionally updates inventory.
- **RESTful API Design:** Utilizes standard HTTP methods (GET, POST, PUT, DELETE) and status codes (200, 201, 400, 401, 404).
- **DTO Layer:** Employs Data Transfer Objects (DTOs) to provide a clean, stable API contract and decouple the public API from internal database models.

---

## Technologies Used

- **Language:** Java 11
- **Web:** Jakarta Servlets 5.0
- **Database:** MySQL 8 with JDBC
- **JSON Serialization:** Google Gson
- **Build Tool:** Apache Maven
- **Testing:** JUnit 5
- **Server:** Apache Tomcat 10+

---

## Setup and Installation

To run this backend service locally, follow these steps:

1.  **Clone the Repository:**
    ```bash
    git clone [Your-GitHub-Repo-URL]
    cd pahana-edu-api
    ```

2.  **Database Setup:**
    - Ensure you have a MySQL server running.
    - Create a new database (schema) named `pahana_edu_db`.
      ```sql
      CREATE DATABASE pahana_edu_db;
      ```
    - Connect to the new database and run the SQL script located at `database/schema.sql` to create all necessary tables and insert initial sample data.

3.  **Configure Database Connection:**
    - Open the file `src/main/resources/database.properties`.
    - Update the `db.username` and `db.password` properties to match your local MySQL credentials.

4.  **Build the Project:**
    - Use Maven to compile the code and package it into a `.war` file.
      ```bash
      mvn clean install
      ```
    - This will generate `target/pahana-edu-api.war`.

5.  **Deploy to Tomcat:**
    - Deploy the `pahana-edu-api.war` file to your Apache Tomcat 10+ server.
    - **Crucially, configure this server to run on port `8081`** to avoid conflicts with the frontend application.
    - Ensure the application is deployed with the context path `/pahana-edu-api`.

---

## API Endpoints

The API is served from the base URL: `http://localhost:8081/pahana-edu-api/api`

### Authentication (`/auth`)
- `POST /auth/login`: Authenticates a user.
- `POST /auth/register`: Creates a new user account.

### Customers (`/customers`)
- `GET /customers`: Get a list of all customers.
- `GET /customers/{id}`: Get a single customer by ID.
- `POST /customers`: Create a new customer.
- `PUT /customers/{id}`: Update an existing customer.
- `DELETE /customers/{id}`: Deactivate (soft delete) a customer.

### Items (`/items`)
- `GET /items`: Get a list of all items.
- `GET /items/{id}`: Get a single item by ID.
- `POST /items`: Create a new item.
- `PUT /items/{id}`: Update an existing item.
- `DELETE /items/{id}`: Delete an item.

### Orders (`/orders`)
- `POST /orders`: Create a new order.
- `GET /orders`: Get a list of all past orders.
- `GET /orders/{id}`: Get a detailed view of a single order.

---

# Pahana Edu Billing System - Web Frontend

This project is the user interface for the Pahana Edu Online Billing System. It is a dynamic web application built with Java Servlets and JavaServer Pages (JSP) that provides a complete management dashboard for the system.

This application is a **pure frontend client**. It does not connect to the database directly. Instead, it consumes the RESTful API provided by the `pahana-edu-api` backend project, communicating entirely over HTTP with JSON.

---

## Features

- **User-Friendly Interface:** Clean, menu-driven dashboard for easy navigation.
- **Complete CRUD Operations:** Provides web forms and tables for managing Customers and Items.
- **Dynamic Order Creation:** An interactive Point-of-Sale (POS) style interface for creating new orders, with client-side JavaScript for a smooth user experience.
- **Order History & Billing:** Allows users to view a list of all past orders and see a detailed, printable invoice for any transaction.
- **Distributed Architecture:** Fully decoupled from the backend, demonstrating a modern web service-based application structure.

---

## Technologies Used

- **Language:** Java 11
- **Web:** Jakarta Servlets 5.0, JavaServer Pages (JSP), JSTL
- **Frontend:** HTML5, CSS3, JavaScript (vanilla)
- **API Communication:** Java 11 HttpClient, Google Gson
- **Build Tool:** Apache Maven
- **Server:** Apache Tomcat 10+

---

## Setup and Installation

To run this frontend application locally, follow these steps:

1.  **Prerequisites:**
    - You **must** have the `pahana-edu-api` backend project set up and running on `http://localhost:8081`. This frontend will not function without it.

2.  **Clone the Repository:**
    ```bash
    git clone [Your-GitHub-Repo-URL]
    cd pahana-edu-web
    ```

3.  **Build the Project:**
    - Use Maven to compile the code and package it into a `.war` file.
      ```bash
      mvn clean install
      ```
    - This will generate `target/pahana-edu-web.war`.

4.  **Deploy to Tomcat:**
    - Deploy the `pahana-edu-web.war` file to your Apache Tomcat 10+ server.
    - **Crucially, configure this server to run on port `8080`** to avoid conflicts with the backend API.
    - Ensure the application is deployed with the context path `/pahana-edu-web`.

5.  **Access the Application:**
    - Open your web browser and navigate to the login page:
      **`http://localhost:8080/pahana-edu-web/app/auth/login`**

---