package org.example.eproject_2.screen;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.eproject_2.entity.Admin;
import org.example.eproject_2.service.RegisterService;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Register extends Application {
    private RegisterService registerModel = new RegisterService();

    @Override
    public void start(Stage primaryStage) {
        Image imageView = new Image(getClass().getResourceAsStream("/images/register.png"));
        ImageView registerImageView = new ImageView(imageView);
        registerImageView.setFitHeight(500);
        registerImageView.setFitWidth(600);
        registerImageView.setPreserveRatio(false);
        StackPane imagePane = new StackPane(registerImageView);
        imagePane.setPrefSize(600, 500);

        // Register form label
        Label registerLabel = new Label("Register");
        registerLabel.getStyleClass().add("label-register");
        registerLabel.setStyle("-fx-font-size: 35px;-fx-padding: 10px;");

        // Fields for username, email, phone, and password
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.getStyleClass().add("text-field");
        usernameField.setPrefHeight(40);
        usernameField.setMaxWidth(350);
        usernameField.setStyle("-fx-border-radius: 10px; -fx-border-color: #ccc; -fx-background-color: white; -fx-font-size: 18px;");


        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.getStyleClass().add("text-field");
        emailField.setPrefHeight(40);
        emailField.setMaxWidth(350);
        emailField.setStyle("-fx-border-radius: 10px; -fx-border-color: #ccc; -fx-background-color: white; -fx-font-size: 18px;");


        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone");
        phoneField.getStyleClass().add("text-field");
        phoneField.setPrefHeight(40);
        phoneField.setMaxWidth(350);
        phoneField.setStyle("-fx-border-radius: 10px; -fx-border-color: #ccc; -fx-background-color: white; -fx-font-size: 18px;");


        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.getStyleClass().add("password-field");
        passwordField.setPrefHeight(40);
        passwordField.setMaxWidth(350);
        passwordField.setStyle("-fx-border-radius: 10px; -fx-border-color: #ccc; -fx-background-color: white; -fx-font-size: 18px;");


        // Error labels
        Label usernameErrorLabel = new Label();
        Label emailErrorLabel = new Label();
        Label phoneErrorLabel = new Label();
        Label passwordErrorLabel = new Label();
        Label successLabel = new Label();

        // Set error label styles (red color)
        usernameErrorLabel.setStyle("-fx-text-fill: red;");
        emailErrorLabel.setStyle("-fx-text-fill: red;");
        phoneErrorLabel.setStyle("-fx-text-fill: red;");
        passwordErrorLabel.setStyle("-fx-text-fill: red;");
        successLabel.setStyle("-fx-text-fill: green;");

        // Register button
        Button registerButton = new Button("Register");
        registerButton.getStyleClass().add("register-button");
        registerButton.setStyle("-fx-border-radius: 10px;");
        registerButton.setStyle("-fx-pref-width: 180px; -fx-pref-height: 40px; " +
                "-fx-background-color: #929292; -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-background-radius: 20px; -fx-font-size: 17px;");
        registerButton.setPrefHeight(40);
        registerButton.setPrefWidth(100);

        // Validation and registration logic
        registerButton.setOnAction(event -> {
            boolean hasError = false;

            // Kiểm tra username
            if (usernameField.getText().isEmpty()) {
                usernameErrorLabel.setText("Username is required");
                hasError = true;
            } else if (!usernameField.getText().matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=])\\S{8,}$")) {
                usernameErrorLabel.setText("Username must have 1 uppercase, 1 digit, 1 special character, and >= 8 characters.");
                hasError = true;
            } else {
                usernameErrorLabel.setText(""); // Clear the error message if no error
            }

            // Kiểm tra email
            if (emailField.getText().isEmpty()) {
                emailErrorLabel.setText("Email is required");
                hasError = true;
            } else if (!emailField.getText().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                emailErrorLabel.setText("Invalid email format");
                hasError = true;
            } else {
                emailErrorLabel.setText("");
            }

            // Kiểm tra phone
            if (phoneField.getText().isEmpty()) {
                phoneErrorLabel.setText("Phone number is required");
                hasError = true;
            } else if (!phoneField.getText().matches("\\d{10}")) {
                phoneErrorLabel.setText("Phone number must be 10 digits!");
                hasError = true;
            } else {
                phoneErrorLabel.setText("");
            }

            // Kiểm tra password
            if (passwordField.getText().isEmpty()) {
                passwordErrorLabel.setText("Password is required");
                hasError = true;
            } else if (passwordField.getText().length() < 8) {
                passwordErrorLabel.setText("Password must be at least 8 characters long!");
                hasError = true;
            } else {
                passwordErrorLabel.setText("");
            }

            // Nếu có bất kỳ trường nào thiếu thông tin, dừng việc thực hiện đăng ký
            if (hasError) {
                return; // Không hiển thị hộp thoại, chỉ cần dừng lại ở đây
            }

            // Nếu tất cả các trường hợp lệ, thực hiện quy trình đăng ký
            String username = usernameField.getText();
            String email = emailField.getText();
            int phone = Integer.parseInt(phoneField.getText());
            String password = hashPassword(passwordField.getText()); // Hash the password before saving

            // Thực hiện đăng ký
            Admin admin = new Admin(username, email, phone, password);
            if (registerModel.add(admin)) {
                successLabel.setText("Registration Successful!");
                try {
                    LoginPage login = new LoginPage();
                    login.start(primaryStage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                successLabel.setText("Registration Failed!");
            }
        });

        // Label for switching to login
        Label loginLabel = new Label("You already have an account? Login");
        loginLabel.getStyleClass().add("login-label");
        loginLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: black; -fx-font-weight: bold;");

        // Event handler for clicking "Login"
        loginLabel.setOnMouseClicked(event -> {
            try {
                LoginPage login = new LoginPage();
                login.start(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Form layout (VBox) with increased spacing
        VBox formLayout = new VBox(10, registerLabel, usernameField, usernameErrorLabel, emailField, emailErrorLabel,
                phoneField, phoneErrorLabel, passwordField, passwordErrorLabel, registerButton, loginLabel, successLabel);
        formLayout.setAlignment(Pos.CENTER);
        formLayout.setPadding(new Insets(30, 10, 10, 10));
        formLayout.setPrefSize(500, 500);

        // Main layout (HBox)
        HBox mainLayout = new HBox(imagePane, formLayout);
        mainLayout.setPrefSize(1000, 500);
        mainLayout.setStyle("-fx-background-color: #f0f0f0;");

        // Scene setup
        Scene scene = new Scene(mainLayout);

        primaryStage.setResizable(false);
        primaryStage.setTitle("Register");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

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

    public static void main(String[] args) {
        launch(args);
    }
}
