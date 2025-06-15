package ro.emag.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import ro.emag.model.Buyer;

public class BuyerHomeController {
    @FXML
    private Label welcomeLabel;

    private Buyer buyer;

    public void setBuyer(Buyer buyer) {
        this.buyer = buyer;
        if (welcomeLabel != null && buyer != null) {
            welcomeLabel.setText("Welcome, " + buyer.getUsername() + "!");
        }
    }

    @FXML
    public void initialize() {
        if (buyer != null) {
            welcomeLabel.setText("Welcome, " + buyer.getUsername() + "!");
        }
    }
}