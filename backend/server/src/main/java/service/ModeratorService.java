package service;

import model.AccountStatus;
import model.Category;
import model.Seller;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import persistance.CategoryRepository;
import persistance.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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
        return null;
    }

    public User unblockSeller(int sellerId) {
        Seller seller = userRepository.findSellerById(sellerId);
        if (seller != null) {
            seller.setAccountStatus(AccountStatus.ACTIVE);
            userRepository.edit(seller);
            return seller;
        }
        return null;
    }

    public boolean createCategory(String categoryName) {
        if (categoryRepository.existsByName(categoryName)) {
            return false;
        }
        model.Category category = new model.Category();
        category.setName(categoryName);
        categoryRepository.save(category);
        return true;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.getAll();
    }

    public boolean editCategory(int id, String newName) {
        Category category = categoryRepository.findById(id);
        if (category == null) {
            return false; 
        }
        if (categoryRepository.existsByName(newName)) {
            return false;
        }
        category.setName(newName);
        categoryRepository.edit(category);
        return true;
    }
}
