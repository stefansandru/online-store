package service;

import model.Category;
import model.Product;
import model.User;
import model.dto.ProductDTO;
import model.dto.ProductMapper;
import persistance.CategoryRepository;
import persistance.ProductRepository;
import persistance.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SellerService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    @Autowired
    private ProductMapper productMapper;

    @Autowired
    public SellerService(ProductRepository productRepository, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<ProductDTO> getSellerProducts(User seller) {
        return productRepository.getProductsBySeller(seller)
            .stream()
            .map(productMapper::toDto)
            .collect(Collectors.toList());
    }

    public ProductDTO addProduct(ProductDTO productDTO, User seller) {
        Product product = productMapper.toEntity(productDTO);
        product.setSeller(seller);
        Category category = categoryRepository.findByName(productDTO.getCategoryName());
        if (category == null) {
            throw new IllegalArgumentException("Category not found: " + productDTO.getCategoryName());
        }
        product.setCategory(category);
        productRepository.save(product);
        return productMapper.toDto(product);
    }

    public boolean deleteProduct(Long id, User seller) {
        List<Product> products = productRepository.getProductsBySeller(seller);
        Product toDelete = products.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
        if (toDelete == null) return false;
        productRepository.delete(toDelete);
        return true;
    }

    public ProductDTO editProduct(Long id, ProductDTO updatedProductDTO, User seller) {
        List<Product> products = productRepository.getProductsBySeller(seller);
        Product existing = products.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
        if (existing == null) return null;
        Product updatedProduct = productMapper.toEntity(updatedProductDTO);
        existing.setName(updatedProduct.getName());
        existing.setPrice(updatedProduct.getPrice());
        existing.setDescription(updatedProduct.getDescription());
        existing.setStock(updatedProduct.getStock());
        productRepository.edit(existing);
        return productMapper.toDto(existing);
    }

    public User getSeller(String username) {
        User user = userRepository.findByUsername(username);
        return (user instanceof model.Seller) ? user : null;
    }
}
