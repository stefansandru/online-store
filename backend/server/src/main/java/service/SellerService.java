package service;

import model.Product;
import model.User;
import persistance.ProductRepository;
import persistance.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SellerService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Autowired
    public SellerService(ProductRepository productRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public List<Product> getSellerProducts(User seller) {
        return productRepository.getProductsBySeller(seller);
    }

    public Product addProduct(Product product, User seller) {
        product.setSeller(seller);
        System.out.print("Adding product: " + product.getId() + " for seller: " + seller.getUsername());
        productRepository.save(product);
        return product;
    }

    public boolean deleteProduct(Long id, User seller) {
        List<Product> products = productRepository.getProductsBySeller(seller);
        Product toDelete = products.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
        if (toDelete == null) return false;
        productRepository.delete(toDelete);
        return true;
    }

    public Product editProduct(Long id, Product updatedProduct, User seller) {
        List<Product> products = productRepository.getProductsBySeller(seller);
        Product existing = products.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
        if (existing == null) return null;
        existing.setName(updatedProduct.getName());
        existing.setPrice(updatedProduct.getPrice());
        existing.setDescription(updatedProduct.getDescription());
        existing.setStock(updatedProduct.getStock());
        // ... add other fields as necessary
        productRepository.edit(existing);
        return existing;
    }

    /**
     * Resolve a seller entity by username. Returns null if the user does not exist or is not a Seller.
     */
    public User getSeller(String username) {
        User user = userRepository.findByUsername(username);
        return (user instanceof model.Seller) ? user : null;
    }
}
