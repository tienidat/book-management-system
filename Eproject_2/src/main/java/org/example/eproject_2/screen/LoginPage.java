package org.example.eproject_2.screen;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.eproject_2.jdbcConnect.dao.JDBCConnect;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginPage extends Application {

    @Override
    public void start(Stage primaryStage) {

        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/images/login.jpg")));
        imageView.setFitHeight(500);
        imageView.setFitWidth(600);
        imageView.setPreserveRatio(false);
        StackPane imagePane = new StackPane(imageView);
        imagePane.setPrefSize(600, 500);
        imagePane.getStyleClass().add("image-pane");

        Label loginLabel = new Label("Login");
        loginLabel.getStyleClass().add("label-login");
        loginLabel.setStyle("-fx-font-size: 35px;-fx-padding: 10px;");

        VBox.setMargin(loginLabel, new Insets(-10, 0, 0, 0)); // Move the label 10px up

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.getStyleClass().add("text-field");
        usernameField.setStyle("-fx-border-radius: 10px; -fx-border-color: #ccc; -fx-background-color: white; -fx-font-size: 18px;");
        usernameField.setPrefHeight(40);
        usernameField.setMaxWidth(350);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.getStyleClass().add("password-field");
        passwordField.setStyle("-fx-border-radius: 10px; -fx-border-color: #ccc; -fx-background-color: white; -fx-font-size: 18px;");
        passwordField.setPrefHeight(40);
        passwordField.setMaxWidth(350);

        VBox.setMargin(passwordField, new Insets(30, 0, 0, 0)); // Add 30px margin between username and password fields

        Label errorMessage = new Label();
        errorMessage.getStyleClass().add("error-message");
        errorMessage.setStyle("-fx-text-fill: red;"); // Set error message color to red
        errorMessage.setVisible(false);

        // Validate login credentials
        Button loginButton = new Button("Login");
        loginButton.getStyleClass().add("login-button");
        loginButton.setStyle("-fx-pref-width: 180px; -fx-pref-height: 40px; " +
                "-fx-background-color: #929292; -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-background-radius: 20px; -fx-font-size: 17px;");
        loginButton.setPrefHeight(40);
        loginButton.setPrefWidth(100);
        loginButton.setOnAction(event -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            // Validate login with the database
            if (validateLogin(username, password)) {
                Dashboard dashboard = new Dashboard();
                errorMessage.setVisible(false);
                dashboard.start(primaryStage);  // Show Dashboard on successful login
            } else {
                errorMessage.setText("Incorrect credentials. Please try again.");
                errorMessage.setVisible(true);
            }
        });

        // Add listener for immediate validation
        usernameField.textProperty().addListener((obs, oldText, newText) -> {
            errorMessage.setVisible(false); // Hide error message while typing
        });

        passwordField.textProperty().addListener((obs, oldText, newText) -> {
            errorMessage.setVisible(false); // Hide error message while typing
        });

        Label registerLabel = new Label("You don't have an account yet? Register.");
        registerLabel.getStyleClass().add("register-label");
        registerLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: black; -fx-font-weight: bold;");

        registerLabel.setOnMouseClicked(event -> {
            try {
                // Create an instance of Register class
                Register register = new Register();
                // Call the start method of the Register class to set the scene
                register.start(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        VBox formLayout = new VBox(10);
        formLayout.setAlignment(Pos.CENTER);
        formLayout.setPadding(new Insets(30, 10, 10, 10));
        formLayout.getStyleClass().add("login-container");
        formLayout.setPrefSize(400, 500);

        formLayout.getChildren().addAll(loginLabel, usernameField, passwordField, errorMessage, loginButton, registerLabel);

        HBox mainLayout = new HBox(imagePane, formLayout);
        mainLayout.setPrefSize(1000, 500);
        mainLayout.setStyle("-fx-background-color: #f0f0f0;");

        Scene scene = new Scene(mainLayout);

        primaryStage.setResizable(false);
        primaryStage.setTitle("Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Method to validate login credentials with the database
    private boolean validateLogin(String username, String password) {
        boolean isValid = false;
        String query = "SELECT * FROM admin WHERE username = ? AND password = ?";

        try (Connection connection = JDBCConnect.getJDBCConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            // Hash the password before checking with the database
            String hashedPassword = hashPassword(password);

            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            ResultSet rs = stmt.executeQuery();

            isValid = rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isValid;
    }

    // Method to hash the password (reused from the registration process)
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // Build the initial Login Scene
    private Scene buildLoginScene(Stage primaryStage) {
        return primaryStage.getScene();  // Return the initial login scene (for simplicity)
    }

    public static void main(String[] args) {
        launch(args);
    }
}
