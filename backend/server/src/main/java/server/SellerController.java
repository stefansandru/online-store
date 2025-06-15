package server;

import model.Product;
import model.User;

// import org.hibernate.engine.jdbc.env.internal.LobCreationLogging_.logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utils.JwtUtil;
import service.SellerService;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/seller")
public class SellerController {

    private final SellerService sellerService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    public SellerController(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    // Helper to extract seller from JWT
    private User getAuthenticatedSeller(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String username = jwtUtil.extractUsername(token);
            return sellerService.getSeller(username);
        }
        return null;
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getSellerProducts(HttpServletRequest request) {
        User seller = getAuthenticatedSeller(request);
        if (seller == null) return ResponseEntity.status(401).build();
        List<Product> products = sellerService.getSellerProducts(seller);
        return ResponseEntity.ok(products);
    }

    @PostMapping("/products")
    public ResponseEntity<?> addProduct(@RequestBody Product product, HttpServletRequest request) {
        User seller = getAuthenticatedSeller(request);
        if (seller == null) return ResponseEntity.status(401).build();
        Product saved = sellerService.addProduct(product, seller);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id, HttpServletRequest request) {
        User seller = getAuthenticatedSeller(request);
        if (seller == null) return ResponseEntity.status(401).build();
        boolean deleted = sellerService.deleteProduct(id, seller);
        if (!deleted) return ResponseEntity.status(404).body("Product not found or not owned by seller");
        return ResponseEntity.ok().build();
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<?> editProduct(@PathVariable Long id, @RequestBody Product updatedProduct, HttpServletRequest request) {
        User seller = getAuthenticatedSeller(request);
        if (seller == null) return ResponseEntity.status(401).build();
        Product updated = sellerService.editProduct(id, updatedProduct, seller);
        if (updated == null) return ResponseEntity.status(404).body("Product not found or not owned by seller");
        return ResponseEntity.ok(updated);
    }
}