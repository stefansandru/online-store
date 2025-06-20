package service;

import model.*;
import model.dto.ProductDTO;
import model.dto.ProductMapper;
import model.dto.CartItemDTO;
import model.dto.CartItemMapper;
import model.dto.OrderMapper;
import model.dto.OrderItemMapper;
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
    private final ProductMapper productMapper;
    private final CartItemMapper cartItemMapper;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Autowired
    public BuyerService(
            ProductRepository productRepository,
            OrderRepository orderRepository,
            CartRepository cartRepository,
            UserRepository userRepository,
            ProductMapper productMapper,
            CartItemMapper cartItemMapper,
            OrderMapper orderMapper,
            OrderItemMapper orderItemMapper) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productMapper = productMapper;
        this.cartItemMapper = cartItemMapper;
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
    }

    public List<ProductDTO> getAllProducts() {
        return productRepository.getAll().stream()
            .map(productMapper::toDto)
            .collect(Collectors.toList());
    }

    public List<ProductDTO> filterProducts(String name, String category) {
        return productRepository.getAll().stream()
                .filter(p -> (name == null || p.getName().toLowerCase().contains(name.toLowerCase()))
                        && (category == null || p.getCategory().getName().equalsIgnoreCase(category)))
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<CartItemDTO> getCartItems(Buyer buyer) {
        return cartRepository.getCartItems(buyer.getId()).stream()
            .map(cartItemMapper::toDto)
            .collect(Collectors.toList());
    }

    public void addProductToCart(Buyer buyer, int product_id, int quantity) {
        Product productToAdd = productRepository.findById(product_id);
        CartItem cartItem = new CartItem(0, buyer, productToAdd, quantity);
        CartItem isExistentCartItem =cartRepository.getCartItem(buyer, product_id);
        if (isExistentCartItem != null) {
            isExistentCartItem.setQuantity(isExistentCartItem.getQuantity() + quantity);
            if (isExistentCartItem.getQuantity() > productToAdd.getStock()) {
                throw new IllegalArgumentException("Not enough stock available for this product.");
            }
            cartRepository.modifyCartItem(isExistentCartItem);
            cartRepository.getCartItems(buyer.getId()).stream().map(cartItemMapper::toDto).collect(Collectors.toList());
            return;
        }
        cartRepository.addProductToCart(cartItem);
        cartRepository.getCartItems(buyer.getId()).stream().map(cartItemMapper::toDto).collect(Collectors.toList());
    }

    public List<CartItemDTO> removeProductFromCart(Buyer buyer, int productId) {
        cartRepository.removeProductFromCart(buyer, productId);
        return cartRepository.getCartItems(buyer.getId()).stream().map(cartItemMapper::toDto).collect(Collectors.toList());
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
        for (CartItem item : cartItems) {
            Product product = item.getProduct();
            int newStock = product.getStock() - item.getQuantity();
            if (newStock < 0) {
                throw new IllegalArgumentException("Not enough stock for product: " + product.getName());
            }
            product.setStock(newStock);
            productRepository.edit(product);
        }
        cartRepository.clearCart(buyer.getId());
    }

    public Buyer getBuyer(String username) {
        User user = userRepository.findByUsername(username);
        return (user instanceof Buyer) ? (Buyer) user : null;
    }
}
