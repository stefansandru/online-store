# Online Store – Full-Stack Reference Implementation

This repository contains a full-stack e-commerce application. It demonstrates the integration of Spring Boot, Hibernate, and Angular in a layered architecture.

The implementation covers core e-commerce functionality such as CRUD operations, JWT-based authentication, and role-based access control.


---

## 1. What lives where?

```
online-store/
 ├─ backend/           # Multi-module Gradle build → REST API
 │   ├─ model/         # Pure JPA entities shared by all modules
 │   ├─ persistance/   # Repositories & custom SQLite dialect
 │   └─ server/        # Spring-Boot web layer, JWT security, services, controllers
 │
 └─ frontend/          # Angular 17 SPA (Material + RxJS)
     └─ src/           # Components, guards, interceptors, dashboard modules
```

### User roles
* **Buyer** – browse catalogue, add products to cart, place orders
* **Seller** – manage own products (CRUD + stock)
* **Moderator** – tidy up categories & keep an eye on users

---

## 2. Tech-stack

* Java 17, Spring Boot 3, Hibernate 6
* SQLite (file `emag.db`) – default development database
* JWT for stateless authentication (custom filter + `SecurityConfig`)
* Angular 17, Angular-Material, JWT interceptor & role guard
* Gradle Wrapper and Angular CLI are included in the repository.

---

## 3. Prerequisites

1. JDK 17 (or newer)
2. Node 18/20 + npm (v9+)
3. **That's it** – the Gradle & Angular CLIs are already bundled.

---

## 4. Running the app

### Backend (Spring Boot)
```bash
# from repo root
cd backend/server
./gradlew bootRun
```
The REST API starts on `http://localhost:8080` and initializes the `emag.db` SQLite file if it is not present.

### Frontend (Angular)
```bash
cd frontend
npm install      # grabs node_modules the first time
npm start        # alias for ng serve –o
```
The Angular application is served at `http://localhost:4200`; CORS is configured in `ServerApplication.java`.

---

## 5. Database quick-start (optional)
`SQLite` works out of the box.  If you want to peek inside:
```bash
sqlite3 backend/emag.db
.tables            # list all tables
SELECT * FROM product LIMIT 5;
```
Feel free to replace the data source in `backend/server/src/main/resources/application.properties` to use PostgreSQL or MySQL.

---

## Test Users

| Username | Password | Role      | Status  |
|----------|----------|-----------|---------|
| ana      | ana      | BUYER     | ACTIVE  |
| adi      | adi      | SELLER    | ACTIVE  |
| dan      | dan      | SELLER    | ACTIVE  |
| mimi     | mimi     | BUYER     | ACTIVE  |
| ion      | ion      | MODERATOR | ACTIVE  |

> The password for each account is the same as the username.

*Use these accounts to log in and test the application.*
