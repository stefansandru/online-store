package ro.emag.service;

import ro.emag.data.UserRepository;
import ro.emag.model.User;

public class AuthService {
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user == null)
            throw new RuntimeException("Invalid username");
        if (PasswordUtils.verifyPassword(password, user.getPasswordHash())) {
            return user;
        } else {
            throw new RuntimeException("Invalid password");
        }
    }

    public boolean register(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return false;
        }
        user.setPasswordHash(PasswordUtils.hashPassword(user.getPasswordHash()));
        userRepository.save(user);
        return true;
    }
}