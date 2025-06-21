package service;

import model.AccountStatus;
import model.Category;
import model.Seller;
import model.User;
import model.dto.SellerDTO;
import model.mappers.SellerMapper;
import model.dto.CategoryDTO;
import model.mappers.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import persistance.CategoryRepository;
import persistance.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ModeratorService {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final SellerMapper sellerMapper;
    private final CategoryMapper categoryMapper;

    @Autowired
    public ModeratorService (
            UserRepository userRepository,
            CategoryRepository categoryRepository,
            SellerMapper sellerMapper,
            CategoryMapper categoryMapper) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.sellerMapper = sellerMapper;
        this.categoryMapper = categoryMapper;
    }

    public List<SellerDTO> getAllSellers() {
        return userRepository.getAllSellers().stream()
            .map(sellerMapper::toDto)
            .collect(Collectors.toList());
    }

    public SellerDTO blockSeller(int sellerId) {
        Seller seller = userRepository.findSellerById(sellerId);
        if (seller != null) {
            seller.setAccountStatus(AccountStatus.BLOCKED);
            userRepository.edit(seller);
            return sellerMapper.toDto(seller);
        }
        return null;
    }

    public SellerDTO unblockSeller(int sellerId) {
        Seller seller = userRepository.findSellerById(sellerId);
        if (seller != null) {
            seller.setAccountStatus(AccountStatus.ACTIVE);
            userRepository.edit(seller);
            return sellerMapper.toDto(seller);
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

    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.getAll().stream()
            .map(categoryMapper::toDto)
            .collect(Collectors.toList());
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
