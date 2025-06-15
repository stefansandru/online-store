package server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import persistance.*;
import service.*;
import utils.JwtUtil;

@SpringBootApplication
@ComponentScan(basePackages = {"config", "filter", "utils", "server"})
public class ServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }


    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil();
    }


    // Repositories

    @Bean
    public UserRepository userRepository() {
        return new UserRepository();
    }

    @Bean
    public ProductRepository productRepository() {
         return new ProductRepository();
     }

    @Bean
    public CategoryRepository categoryRepository() {
        return new CategoryRepository(persistance.utils.HibernateUtil.getSessionFactory());
    }

    @Bean
    public OrderRepository orderRepository() {
        return new OrderRepository(persistance.utils.HibernateUtil.getSessionFactory());
    }

    @Bean
    public CartRepository cartRepository() {
        return new CartRepository(persistance.utils.HibernateUtil.getSessionFactory());
    }


    // Services

    @Bean
    public AuthService authService(UserRepository userRepository, JwtUtil jwtUtil) {
        return new AuthService(userRepository, jwtUtil);
    }

    @Bean
    public CategoryService categoryService(CategoryRepository categoryRepository) {
        return new CategoryService(categoryRepository);
    }

    @Bean
    public SellerService sellerService(ProductRepository productRepository, UserRepository userRepository) {
        return new SellerService(productRepository, userRepository);
    }

    @Bean
    public BuyerService buyerService(
            ProductRepository productRepository,
            OrderRepository orderRepository,
            CartRepository cartRepository,
            UserRepository userRepository) {
        return new BuyerService(productRepository, orderRepository, cartRepository, userRepository);
    }

    @Bean
    public ModeratorService moderatorService(
            UserRepository userRepository,
            CategoryRepository categoryRepository) {
        return new ModeratorService(userRepository, categoryRepository);
    }


    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("http://localhost:4200")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}
