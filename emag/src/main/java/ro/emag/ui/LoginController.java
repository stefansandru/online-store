package ro.emag.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ro.emag.model.AccountStatus;
import ro.emag.model.Buyer;
import ro.emag.model.Seller;
import ro.emag.model.User;
import ro.emag.service.AuthService;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    @FXML
    private TextField emailField;

    @FXML
    private ComboBox<String> roleComboBox;

    private AuthService authService;

    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }

    @FXML
    public void initialize() {
        roleComboBox.getItems().addAll("Buyer", "Seller");
    }

    @FXML
    private void handleLogin() {
        String user = usernameField.getText();
        String pass = passwordField.getText();
        try {
            User authUser = authService.login(user, pass);

            if (authUser instanceof Seller) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/seller_home.fxml"));
                Parent root = loader.load();

                SellerHomeController sellerController = loader.getController();
                sellerController.setSeller((Seller) authUser);

                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setScene(new Scene(root, 600, 400));
                stage.setTitle("Seller Home");
                stage.show();
            } else if (authUser instanceof Buyer) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/buyer_home.fxml"));
                Parent root = loader.load();

                BuyerHomeController buyerController = loader.getController();
                buyerController.setBuyer((Buyer) authUser);

                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setScene(new Scene(root, 600, 400));
                stage.setTitle("Buyer Home");
                stage.show();
            }
        } catch (Exception e) {
            messageLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void handleRegister() throws IOException {
        String user = usernameField.getText();
        String pass = passwordField.getText();
        String email = emailField.getText();
        String role = roleComboBox.getValue();
        if (user.isEmpty() || pass.isEmpty() || email.isEmpty() || role == null) {
            messageLabel.setText("Please fill in all fields");
            return;
        }
        
        if (!email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{1,4}$")) {
            messageLabel.setText("Invalid email format");
            return;
        }

        User newUser;
        if (role.equals("Seller")) {
            newUser = new Seller(user, pass, email, AccountStatus.ACTIVE);
        } else if (role.equals("Buyer")) {
            newUser = new Buyer(user, pass, email, AccountStatus.ACTIVE);
        } else {
            messageLabel.setText("Select a role!");
            return;
        }

        boolean registered = authService.register(newUser);
        if (registered) {
            messageLabel.setText("User registered successfully!");
            // show user home page
            if (newUser instanceof Seller) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/seller_home.fxml"));
                Parent root = loader.load();

                SellerHomeController sellerController = loader.getController();
                sellerController.setSeller((Seller) newUser);

                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setScene(new Scene(root, 600, 400));
                stage.setTitle("Seller Home");
                stage.show();
            } else { // is Buyer
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/buyer_home.fxml"));
                Parent root = loader.load();

                BuyerHomeController buyerController = loader.getController();
                buyerController.setBuyer((Buyer) newUser);

                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setScene(new Scene(root, 600, 400));
                stage.setTitle("Buyer Home");
                stage.show();
            }
        } else {
            messageLabel.setText("Username already exists!");
        }
    }
}