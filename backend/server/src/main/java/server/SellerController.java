package server;

import model.dto.ProductDTO;
import model.User;
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
    public ResponseEntity<List<ProductDTO>> getSellerProducts(HttpServletRequest request) {
        User seller = getAuthenticatedSeller(request);
        if (seller == null) return ResponseEntity.status(401).build();
        List<ProductDTO> products = sellerService.getSellerProducts(seller);
        return ResponseEntity.ok(products);
    }

    @PostMapping("/products")
    public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductDTO productDTO, HttpServletRequest request) {
        User seller = getAuthenticatedSeller(request);
        if (seller == null) return ResponseEntity.status(401).build();
        ProductDTO saved = sellerService.addProduct(productDTO, seller);
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
    public ResponseEntity<ProductDTO> editProduct(@PathVariable Long id, @RequestBody ProductDTO updatedProductDTO, HttpServletRequest request) {
        User seller = getAuthenticatedSeller(request);
        if (seller == null) return ResponseEntity.status(401).build();
        ProductDTO updated = sellerService.editProduct(id, updatedProductDTO, seller);
        if (updated == null) return ResponseEntity.status(404).body(null);
        return ResponseEntity.ok(updated);
    }
}