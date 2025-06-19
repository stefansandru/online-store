package service;

import model.User;
import persistance.UserRepository;
import utils.JwtUtil;
import utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public String login(String username, String password) {
        System.out.println(username);
        User user = userRepository.findByUsername(username);
        System.out.println("User found: " + (user != null ? user.getUsername() : "null"));
        if (user != null && PasswordUtils.verifyPassword(password, user.getPasswordHash())) {
            String role = user.getClass().getSimpleName().toUpperCase();
            return jwtUtil.generateToken(user, role);
        }
        return null;
    }

    public Iterable<String> getAllUsernames() {
        return userRepository.getAllUsernames();
    }

    public boolean register(String username, String password, String role) {
        if (userRepository.findByUsername(username) != null) {
            return false;
        }

        String passwordHash = PasswordUtils.hashPassword(password);
        User user;
        String email = username + "@example.com";

        switch (role.toUpperCase()) {
            case "BUYER":
                user = new model.Buyer(username, email, passwordHash);
                break;
            case "SELLER":
                user = new model.Seller(username, email, passwordHash);
                break;
            case "MODERATOR":
                user = new model.Moderator(username, email, passwordHash);
                break;
            default:
                return false;
        }

        System.out.println("Registering user: " + username + " with role: " + role + " id: " + user.getId());
        System.out.println("Password hash: " + passwordHash);
        userRepository.save(user);
        return true;
    }
}
