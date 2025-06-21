package server;

import model.*;
import model.dto.CartItemDTO;
import model.dto.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.BuyerService;
import utils.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/buyer")
public class BuyerController {
    private final BuyerService buyerService;
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    public BuyerController(BuyerService buyerService) {
        this.buyerService = buyerService;
    }

    private Buyer getAuthenticatedBuyer(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String username = jwtUtil.extractUsername(token);
            Buyer buyer = buyerService.getBuyer(username);
            if (buyer != null) return buyer;
        }
        return null;
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        System.out.println("Getting all products");
        List<ProductDTO> productDTOs = buyerService.getAllProducts();
        return ResponseEntity.ok(productDTOs);
    }

    @GetMapping("/products/filter")
    public ResponseEntity<List<ProductDTO>> filterProducts(@RequestParam(required = false) String name, @RequestParam(required = false) String category) {
        return ResponseEntity.ok(buyerService.filterProducts(name, category));
    }

    @GetMapping("/cart")
    public ResponseEntity<List<CartItemDTO>> getCartItems(HttpServletRequest request) {
        Buyer buyer = getAuthenticatedBuyer(request);
        if (buyer == null) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(buyerService.getCartItems(buyer));
    }

    @PostMapping("/cart/add")
    public ResponseEntity<?> addProductToCart(@RequestParam int productId, @RequestParam int quantity, HttpServletRequest request) {
        System.out.println();
        System.out.println("Adding product to cart: " + productId + " x " + quantity);
        Buyer buyer = getAuthenticatedBuyer(request);
        if (buyer == null) return ResponseEntity.status(401).build();
        buyerService.addProductToCart(buyer, productId, quantity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/cart/remove")
    public ResponseEntity<?> removeProductFromCart(@RequestParam int productId, HttpServletRequest request) {
        Buyer buyer = getAuthenticatedBuyer(request);
        if (buyer == null) return ResponseEntity.status(401).build();
        buyerService.removeProductFromCart(buyer, productId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/order")
    public ResponseEntity<?> placeOrder(HttpServletRequest request) {
        Buyer buyer = getAuthenticatedBuyer(request);
        if (buyer == null) return ResponseEntity.status(401).build();
        buyerService.placeOrder(buyer);
        return ResponseEntity.ok().build();
    }
}
