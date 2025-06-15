package ro.emag;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ro.emag.data.UserRepository;
import ro.emag.service.AuthService;
//import service.AuthServiceImpl;
import ro.emag.ui.LoginController;

public class Main extends Application {

    private AuthService authService;

    @Override
    public void init() {
        // Initialize backend dependencies
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        UserRepository userRepository = new UserRepository(sessionFactory);
        authService = new AuthService(userRepository);
    }

    @Override
    public void start(Stage stage) throws Exception {
        // Load the login view FXML
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 500);

        // Inject the authService into the controller
        LoginController controller = fxmlLoader.getController();
        controller.setAuthService(authService);

        // Setup and show the stage
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}