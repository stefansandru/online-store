package service;

import model.AccountStatus;
import model.Category;
import model.Seller;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import persistance.CategoryRepository;
import persistance.UserRepository;

import java.util.List;

public class ModeratorService {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ModeratorService (
            UserRepository userRepository,
            CategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }


    public List<Seller> getAllSellers() {
        return userRepository.getAllSellers();
    }

    public User blockSeller(int sellerId) {
        Seller seller = userRepository.findSellerById(sellerId);
        if (seller != null) {
            seller.setAccountStatus(AccountStatus.BLOCKED);
            userRepository.edit(seller);
            return seller;
        }
        return null; // Seller not found
    }

    public User unblockSeller(int sellerId) {
        Seller seller = userRepository.findSellerById(sellerId);
        if (seller != null) {
            seller.setAccountStatus(AccountStatus.ACTIVE);
            userRepository.edit(seller);
            return seller;
        }
        return null; // Seller not found
    }

    public boolean createCategory(String categoryName) {
        if (categoryRepository.existsByName(categoryName)) {
            return false; // Category already exists
        }
        model.Category category = new model.Category();
        category.setName(categoryName);
        categoryRepository.save(category);
        return true; // Category created successfully
    }

    public List<Category> getAllCategories() {
        return categoryRepository.getAll();
    }

    public boolean editCategory(int id, String newName) {
        Category category = categoryRepository.findById(id);
        if (category == null) {
            return false; // Category not found
        }
        if (categoryRepository.existsByName(newName)) {
            return false; // New name already exists
        }
        category.setName(newName);
        categoryRepository.edit(category);
        return true; // Category updated successfully
    }
}
