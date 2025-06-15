package server;

import model.Category;
import model.Seller;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.ModeratorService;
import utils.JwtUtil;

import java.util.List;

@RestController
@RequestMapping("/api/moderator")
public class ModeratorController {

    private final ModeratorService moderatorService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    public ModeratorController(ModeratorService moderatorService) {
        this.moderatorService = moderatorService;
    }


    // Example method to get all users (moderator functionality)
     @GetMapping("/users")
     public ResponseEntity<List<Seller>> getAllSellers() {
         List<Seller> sellers = moderatorService.getAllSellers();
         return ResponseEntity.ok(sellers);
     }

    // block a seller
    @PostMapping("/block-seller")
    public ResponseEntity<String> blockSeller(@RequestParam int sellerId) {
        User user = moderatorService.blockSeller(sellerId);
        if (user != null) {
            return ResponseEntity.ok("Seller blocked successfully");
        } else {
            return ResponseEntity.status(404).body("Seller not found");
        }
    }

    // unblock a seller
    @PostMapping("/unblock-seller")
    public ResponseEntity<String> unblockSeller(@RequestParam int sellerId) {
        User user = moderatorService.unblockSeller(sellerId);
        if (user != null) {
            return ResponseEntity.ok("Seller unblocked successfully");
        } else {
            return ResponseEntity.status(404).body("Seller not found");
        }
    }

    // get all categories
    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = moderatorService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    // crete a new category
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
