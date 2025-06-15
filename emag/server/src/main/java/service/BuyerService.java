package service;

import model.*;
import model.dto.ProductDTO;
import persistance.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BuyerService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    @Autowired
    public BuyerService(
            ProductRepository productRepository,
            OrderRepository orderRepository,
            CartRepository cartRepository,
            UserRepository userRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
    }

    public List<ProductDTO> getAllProducts() {
        List<Product> products =  productRepository.getAll();
        List<ProductDTO> dtos = products.stream().map(product -> {
            ProductDTO dto = new ProductDTO();
            dto.setId(product.getId());
            dto.setName(product.getName());
            dto.setDescription(product.getDescription());
            dto.setPrice(product.getPrice());
            dto.setStock(product.getStock());
            dto.setCategoryName(product.getCategory() != null ? product.getCategory().getName() : null);
            return dto;
        }).toList();
        return dtos;
    }

    public List<Product> filterProducts(String name, String category) {
        return productRepository.getAll().stream()
                .filter(p -> (name == null || p.getName().toLowerCase().contains(name.toLowerCase()))
                        && (category == null || p.getCategory().getName().equalsIgnoreCase(category)))
                .collect(Collectors.toList());
    }

    public List<CartItem> getCartItems(Buyer buyer) {
        return cartRepository.getCartItems(buyer.getId());
    }

    public List<CartItem> addProductToCart(Buyer buyer, int product_id, int quantity) {
        Product productToAdd = productRepository.findById(product_id);
        CartItem cartItem = new CartItem(0, buyer, productToAdd, quantity);
        CartItem isExistentCartItem =cartRepository.getCartItem(buyer, product_id);
        if (isExistentCartItem != null) {
            isExistentCartItem.setQuantity(isExistentCartItem.getQuantity() + quantity);
            if (isExistentCartItem.getQuantity() > productToAdd.getStock()) {
                throw new IllegalArgumentException("Not enough stock available for this product.");
            }
            cartRepository.modifyCartItem(isExistentCartItem);
            return cartRepository.getCartItems(buyer.getId());
        }

        cartRepository.addProductToCart(cartItem);
        return cartRepository.getCartItems(buyer.getId());
    }

    public List<CartItem> removeProductFromCart(Buyer buyer, int productId) {
        cartRepository.removeProductFromCart(buyer, productId);
        return cartRepository.getCartItems(buyer.getId());
    }

    public void placeOrder(Buyer buyer) {
        List<CartItem> cartItems = cartRepository.getCartItems(buyer.getId());
        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("Cart is empty. Cannot place order.");
        }

        Order order = new Order(0, buyer, LocalDateTime.now(), null);
        List<OrderItem> orderItems = cartItems.stream()
                .map(item -> new OrderItem(order, item.getProduct(), item.getQuantity()))
                .collect(Collectors.toList());
        order.setItems(orderItems);
        orderRepository.save(order);

        // Update stock for each product in the order
        for (CartItem item : cartItems) {
            Product product = item.getProduct();
            int newStock = product.getStock() - item.getQuantity();
            if (newStock < 0) {
                throw new IllegalArgumentException("Not enough stock for product: " + product.getName());
            }
            product.setStock(newStock);
            productRepository.edit(product);
        }

        // Clear the cart after placing the order
        cartRepository.clearCart(buyer.getId());

    }
}
