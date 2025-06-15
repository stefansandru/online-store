package ro.emag.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ro.emag.data.CategoryRepository;
import ro.emag.data.ProductRepository;
import ro.emag.model.Category;
import ro.emag.model.Product;
import ro.emag.model.Seller;

import java.util.List;

public class SellerHomeController {
    @FXML
    private Label welcomeLabel;

    @FXML
    private TableView<Product> productsTable;

    @FXML
    private TableColumn<Product, Integer> idColumn;

    @FXML
    private TableColumn<Product, String> nameColumn;

    @FXML
    private TableColumn<Product, String> descriptionColumn;

    @FXML
    private TableColumn<Product, Double> priceColumn;

    @FXML
    private TableColumn<Product, Integer> stockColumn;

    @FXML
    private TextField nameField;

    @FXML
    private TextArea descriptionField;

    @FXML
    private TextField priceField;

    @FXML
    private TextField stockField;

    @FXML
    private ComboBox<Category> categoryComboBox;

    private Seller seller;
    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private ObservableList<Product> productsList = FXCollections.observableArrayList();
    private ObservableList<Category> categoriesList = FXCollections.observableArrayList();

    public SellerHomeController() {
        // Initialize the repositories
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        productRepository = new ProductRepository(sessionFactory);
        categoryRepository = new CategoryRepository(sessionFactory);
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
        if (welcomeLabel != null && seller != null) {
            welcomeLabel.setText("Welcome, " + seller.getUsername() + "!");
            loadProducts();
        }
    }

    @FXML
    public void initialize() {
        // Set up the table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));

        // Bind the table to the products list
        productsTable.setItems(productsList);

        // Load categories into the dropdown
        loadCategories();

        // Set a default message in case setSeller is called after initialize
        if (seller != null) {
            welcomeLabel.setText("Welcome, " + seller.getUsername() + "!");
            loadProducts();
        }
    }

    private void loadCategories() {
        List<Category> categories = categoryRepository.getAll();
        categoriesList.clear();
        categoriesList.addAll(categories);
        categoryComboBox.setItems(categoriesList);

        // Set a custom cell factory to display the category name
        categoryComboBox.setCellFactory(param -> new ListCell<Category>() {
            @Override
            protected void updateItem(Category item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });

        // Set a custom string converter to display the category name
        categoryComboBox.setConverter(new StringConverter<Category>() {
            @Override
            public String toString(Category category) {
                if (category == null) {
                    return null;
                }
                return category.getName();
            }

            @Override
            public Category fromString(String string) {
                // Not needed for this use case
                return null;
            }
        });
    }

    private void loadProducts() {
        if (seller != null) {
            List<Product> products = productRepository.getProductsBySeller(seller);
            productsList.clear();
            productsList.addAll(products);
        }
    }

    @FXML
    public void handleAddProduct() {
        try {
            String name = nameField.getText();
            String description = descriptionField.getText();
            double price = Double.parseDouble(priceField.getText());
            int stock = Integer.parseInt(stockField.getText());
            Category selectedCategory = categoryComboBox.getValue();

            if (name.isEmpty() || description.isEmpty()) {
                showAlert("Error", "Please fill in all fields");
                return;
            }

            if (selectedCategory == null) {
                showAlert("Error", "Please select a category");
                return;
            }

            // Create a new product
            Product product = new Product();
            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            product.setStock(stock);
            product.setCategory(selectedCategory);
            product.setSeller(seller);

            // Save the product
            productRepository.save(product);

            // Clear the form
            nameField.clear();
            descriptionField.clear();
            priceField.clear();
            stockField.clear();
            categoryComboBox.setValue(null);

            // Reload the products
            loadProducts();

            showAlert("Success", "Product added successfully");
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter valid numbers for price and stock");
        } catch (Exception e) {
            showAlert("Error", "An error occurred: " + e.getMessage());
        }
    }

    @FXML
    public void handleDeleteProduct() {
        Product selectedProduct = productsTable.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            showAlert("Error", "Please select a product to delete");
            return;
        }

        try {
            productRepository.delete(selectedProduct);
            loadProducts();
            showAlert("Success", "Product deleted successfully");
        } catch (Exception e) {
            showAlert("Error", "An error occurred: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
