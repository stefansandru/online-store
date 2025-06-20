package server;

import model.dto.CategoryDTO;
import model.dto.SellerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.ModeratorService;

import java.util.List;

@RestController
@RequestMapping("/api/moderator")
public class ModeratorController {
    private final ModeratorService moderatorService;

    @Autowired
    public ModeratorController(ModeratorService moderatorService) {
        this.moderatorService = moderatorService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<SellerDTO>> getAllSellers() {
        List<SellerDTO> sellers = moderatorService.getAllSellers();
        return ResponseEntity.ok(sellers);
    }

    @PostMapping("/block-seller")
    public ResponseEntity<SellerDTO> blockSeller(@RequestParam int sellerId) {
        SellerDTO seller = moderatorService.blockSeller(sellerId);
        if (seller != null) {
            return ResponseEntity.ok(seller);
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }

    @PostMapping("/unblock-seller")
    public ResponseEntity<SellerDTO> unblockSeller(@RequestParam int sellerId) {
        SellerDTO seller = moderatorService.unblockSeller(sellerId);
        if (seller != null) {
            return ResponseEntity.ok(seller);
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> categories = moderatorService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @PostMapping("/create-category")
    public ResponseEntity<String> createCategory(@RequestParam String categoryName) {
        boolean created = moderatorService.createCategory(categoryName);
        if (created) {
            return ResponseEntity.ok("Category created successfully");
        } else {
            return ResponseEntity.status(400).body("Category creation failed");
        }
    }

    @PutMapping("/edit-category/{id}")
    public ResponseEntity<String> editCategory(@PathVariable int id, @RequestParam String newName) {
        boolean updated = moderatorService.editCategory(id, newName);
        if (updated) {
            return ResponseEntity.ok("Category updated successfully");
        } else {
            return ResponseEntity.status(404).body("Category not found");
        }
    }
}
