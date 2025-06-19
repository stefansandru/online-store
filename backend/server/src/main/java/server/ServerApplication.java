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
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
@ComponentScan(basePackages = {"config", "filter", "utils", "server"})
public class ServerApplication {
    private static final Logger logger = LoggerFactory.getLogger(ServerApplication.class);

    @Autowired
    private Environment env;

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    @Bean
    public CommandLineRunner logDbPath(Environment env) {
        return args -> {
            String dbUrl = env.getProperty("spring.datasource.url");
            logger.info("spring.datasource.url: {}", dbUrl);
            if (dbUrl != null && dbUrl.startsWith("jdbc:sqlite:")) {
                String dbPath = dbUrl.substring("jdbc:sqlite:".length());
                logger.info("Resolved SQLite DB path: {}", Paths.get(dbPath).toAbsolutePath());
            }
        };
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
