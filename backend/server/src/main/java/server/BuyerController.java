package server;

import model.*;
import model.dto.CartItemDTO;
import model.dto.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.BuyerService;
import persistance.UserRepository;
import utils.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/buyer")
public class BuyerController {
    private final BuyerService buyerService;
    private final UserRepository userRepository;
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    public BuyerController(BuyerService buyerService, UserRepository userRepository) {
        this.buyerService = buyerService;
        this.userRepository = userRepository;
    }

    private Buyer getAuthenticatedBuyer(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String username = jwtUtil.extractUsername(token);
            User user = userRepository.findByUsername(username);
            if (user instanceof Buyer) return (Buyer) user;
        }
        return null;
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        System.out.println("Getting all products");
        List<ProductDTO> productDTOS = buyerService.getAllProducts();
        for (ProductDTO product : productDTOS) {
            System.out.println("Product: " + product.getName() + " - " + product.getPrice());
        }
        return ResponseEntity.ok(productDTOS);
    }

    @GetMapping("/products/filter")
    public ResponseEntity<List<Product>> filterProducts(@RequestParam(required = false) String name, @RequestParam(required = false) String category) {
        return ResponseEntity.ok(buyerService.filterProducts(name, category));
    }

    @GetMapping("/cart")
    public ResponseEntity<List<CartItemDTO>> getCartItems(HttpServletRequest request) {
        Buyer buyer = getAuthenticatedBuyer(request);
        if (buyer == null) return ResponseEntity.status(401).build();
        List<CartItem> cartItems = buyerService.getCartItems(buyer);

        List<CartItemDTO> cartItemDTOs = cartItems.stream()
            .map(item -> {
                CartItemDTO dto = new CartItemDTO();
                dto.setId(item.getId());
                dto.setProductId(item.getProduct().getId());
                dto.setProductName(item.getProduct().getName());
                dto.setQuantity(item.getQuantity());
                dto.setPrice(item.getProduct().getPrice());
                return dto;
            })
            .toList();

        return ResponseEntity.ok(cartItemDTOs);
    }

    @PostMapping("/cart/add")
    public ResponseEntity<?> addProductToCart(@RequestParam int productId, @RequestParam int quantity, HttpServletRequest request) {
        System.out.println("Adding product to cart: " + productId + " x " + quantity);
        Buyer buyer = getAuthenticatedBuyer(request);
        if (buyer == null) return ResponseEntity.status(401).build();
        List<CartItem> cart = buyerService.addProductToCart(buyer, productId, quantity);
        for (CartItem item : cart) {
            System.out.println("Cart item: " + item.getProduct().getName() + " x " + item.getQuantity());
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/cart/remove")
    public ResponseEntity<?> removeProductFromCart(@RequestParam int productId, HttpServletRequest request) {
        Buyer buyer = getAuthenticatedBuyer(request);
        if (buyer == null) return ResponseEntity.status(401).build();
        List<CartItem> cart = buyerService.removeProductFromCart(buyer, productId);
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
