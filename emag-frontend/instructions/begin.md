# 🧠 AI Prompt: Create a Seller Dashboard in Angular

I’m building an Angular web app with JWT-based authentication (Java backend using Spring and returning JWT tokens and `User` objects).

## 🔐 Auth Context

* The app supports role-based dashboards: `seller`, `buyer`, and `moderator`.
* JWTs are stored in `localStorage`, and requests must include an `Authorization: Bearer <token>` header.
* Angular should use:
  * `AuthService` to manage login, logout, and token/role access.
  * `AuthGuard` for role-based route protection.
  * A JWT `HttpInterceptor` (like `provideHttpClient(withInterceptors([jwtInterceptor]))`).

## 🧱 File/Structure Expectations

Please generate:
* A `SellerDashboardComponent` that:
  * Lists the seller's products (fetched from backend).
  * Shows each product with:
    * Name, price, and description.
    * **Delete** button for removing the product.
    * **Edit** button that opens a form with pre-filled product data.
* A product **edit form** that appears inline or as a modal, allowing:
  * Updating the name, price, and description.
  * Submitting changes (PATCH or PUT request).
* An **Add Product** form (inline or modal).
* `ProductService` with methods:
  * `getSellerProducts()`
  * `addProduct(product)`
  * `deleteProduct(productId)`
  * `updateProduct(product)`
* Use a `models/` folder with at least:
  * `Product` interface (`id`, `name`, `description`, `price`, etc.)

## 📦 Optional but Recommended

* Use `ReactiveFormsModule` for forms.
* Include basic error handling.
* Show loading states (e.g., spinners or disabled buttons).
* Use Angular Material or Bootstrap styles (optional, but helpful for UI).

## 🧩 Extra Notes

* Assume the seller is authenticated and their JWT is stored in `localStorage`.
* You can use mock data or placeholder HTTP URLs (like `/api/seller/products`).
* The backend already distinguishes sellers from other roles by the JWT payload.

here is the backedn SellerController
``` 
package server;

import model.Product;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import persistance.ProductRepository;
import persistance.UserRepository;
import utils.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/seller")
public class SellerController {

    private ProductRepository productRepository;
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    public SellerController(ProductRepository productRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    // Helper to extract seller from JWT
    private User getAuthenticatedSeller(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String username = jwtUtil.extractUsername(token);
            return userRepository.findByUsername(username);
        }
        return null;
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getSellerProducts(HttpServletRequest request) {
        User seller = getAuthenticatedSeller(request);
        if (seller == null) return ResponseEntity.status(401).build();
        List<Product> products = productRepository.getProductsBySeller(seller);
        return ResponseEntity.ok(products);
    }

    @PostMapping("/products")
    public ResponseEntity<?> addProduct(@RequestBody Product product, HttpServletRequest request) {
        User seller = getAuthenticatedSeller(request);
        if (seller == null) return ResponseEntity.status(401).build();
        product.setSeller(seller);
        productRepository.save(product);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id, HttpServletRequest request) {
        User seller = getAuthenticatedSeller(request);
        if (seller == null) return ResponseEntity.status(401).build();
        List<Product> products = productRepository.getProductsBySeller(seller);
        Product toDelete = products.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
        if (toDelete == null) return ResponseEntity.status(404).body("Product not found or not owned by seller");
        productRepository.delete(toDelete);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<?> editProduct(@PathVariable Long id, @RequestBody Product updatedProduct, HttpServletRequest request) {
        User seller = getAuthenticatedSeller(request);
        if (seller == null) return ResponseEntity.status(401).build();
        List<Product> products = productRepository.getProductsBySeller(seller);
        Product existing = products.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
        if (existing == null) return ResponseEntity.status(404).body("Product not found or not owned by seller");
        // Update fields as needed
        existing.setName(updatedProduct.getName());
        existing.setPrice(updatedProduct.getPrice());
        existing.setDescription(updatedProduct.getDescription());
        // ... add other fields as necessary
        productRepository.edit(existing);
        return ResponseEntity.ok(existing);
    }
}
```

here is the product class
```
package model;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String description;
    private double price;
    private int stock;

    // Marking the category as transient to avoid mapping issues
    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User seller;

    public Product() {
        // Required by Hibernate
    }

    public Product(int id, String name, String description, double price, int stock, Category category, User seller) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.seller = seller;
    }

//    get and set
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }
}
```