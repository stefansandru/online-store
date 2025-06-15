package server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.AuthService;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");
        logger.info("Received username: {}", username);
        String token = authService.login(username, password);
        if (token != null) {
            logger.info("Authentication successful for user: {}", username);
            return ResponseEntity.ok(Map.of("token", token));
        } else {
            logger.warn("Authentication failed for user: {}", username);
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> userDetails) {
        String username = userDetails.get("username");
        String password = userDetails.get("password");
        String role = userDetails.get("role");

        if (authService.register(username, password, role)) {
            logger.info("User registered successfully: {}", username);
            return ResponseEntity.ok("User registered successfully");
        } else {
            logger.warn("Registration failed for user: {}", username);
            return ResponseEntity.status(400).body("Registration failed");
        }
    }

    @GetMapping("/usernames")
    public Iterable<String> getAllUsernames() {
        return authService.getAllUsernames();
    }
}