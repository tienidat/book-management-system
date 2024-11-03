package org.example.eproject_2.screen;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.eproject_2.jdbcConnect.dao.JDBCConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

public class Dashboard extends Application {

    @Override
    public void start(Stage primaryStage) {
        VBox leftMenu = new VBox(20);
        leftMenu.setPadding(new Insets(20));
        leftMenu.setStyle("-fx-background-color: linear-gradient(to bottom, #4A00E0, #8E2DE2);");

        ImageView homeIconView = createImageView("/images/home.png", 90, 90);

        Region topSpacer = new Region();
        topSpacer.setPrefHeight(30);

        Region iconToMenuSpacer = new Region();
        iconToMenuSpacer.setPrefHeight(90);

        VBox menuContainer = new VBox();
        menuContainer.setAlignment(Pos.CENTER);

        menuContainer.getChildren().addAll(topSpacer, homeIconView, iconToMenuSpacer);

        ImageView readerIconView = createImageView("/images/readers.png", 20, 20);
        ImageView statisticalIconView = createImageView("/images/statistic.png", 20, 20);
        ImageView bookManagementIconView = createImageView("/images/book_management.png", 20, 20);
        ImageView returnIconView = createImageView("/images/borrow_book.png", 20,20);
        ImageView borrowBooksView = createImageView("/images/borrow_book.png", 20, 20);
        ImageView historyIconView = createImageView("/images/history.png", 20, 20);

        Button readersBtn = new Button("Readers", readerIconView);
        readersBtn.setOnAction(event -> {
            try {
                Reader reader = new Reader();
                reader.start(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


        Button bookManagementBtn = new Button("Book Management", bookManagementIconView);
        bookManagementBtn.setOnAction(event -> {
            try{
                BookManagement bookManagement = new BookManagement();
                bookManagement.start(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Button borrowReturnBooksBtn = new Button("Borrow Book", borrowBooksView);
        borrowReturnBooksBtn.setOnAction(event -> {
            try {
                BorrowBooksApp borrowBooks = new BorrowBooksApp();
                borrowBooks.start(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Button returnBook = new Button("Return Book", returnIconView);
        returnBook.setOnAction(event -> {
            try {
                ReturnBook returnBooks = new ReturnBook();
                returnBooks.start(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Button history = new Button("History", historyIconView);
        history.setOnAction(event ->{
            try {
                History violation = new History();
                violation.start(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Button statisticalBtn = new Button("Dashboard", statisticalIconView);
        ImageView exitView = createImageView("/images/exit.png", 20,20);
        Button logOut = new Button("Log Out", exitView);
        logOut.setOnMouseClicked(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Log Out");
            alert.setHeaderText("Are you sure you want to log out?");
            alert.setContentText("Click OK to confirm or Cancel to go back.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    // Khởi tạo và hiển thị trang đăng nhập
                    LoginPage login = new LoginPage();
                    login.start(primaryStage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        for (Button btn : new Button[]{statisticalBtn,readersBtn, bookManagementBtn, borrowReturnBooksBtn, returnBook, history, logOut}) {
            statisticalBtn.setStyle("-fx-border-color: white; -fx-border-width: 1px; -fx-background-color: #8E2DE2; -fx-pref-width: 180px; -fx-pref-height: 35px;-fx-font-size: 16px; -fx-border-radius: 10px; -fx-alignment: TOP_LEFT;");

            btn.setPrefWidth(190);
            btn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 16px;-fx-alignment: TOP_LEFT;");
        }

        VBox buttonsContainer = new VBox(20, statisticalBtn, readersBtn, bookManagementBtn, borrowReturnBooksBtn, returnBook, history, logOut);
        menuContainer.getChildren().add(buttonsContainer);
        leftMenu.getChildren().add(menuContainer);

        GridPane contentPane = new GridPane();
        contentPane.setPadding(new Insets(20));
        contentPane.setVgap(10);
        contentPane.setHgap(10);

        Label titleLabel = new Label("Dashboard");
        titleLabel.setStyle("-fx-font-size: 25px; -fx-font-weight: bold; -fx-alignment: center;");
        contentPane.add(titleLabel, 0, 0, 2, 1);

        Separator separator = new Separator();
        separator.setPrefWidth(300);
        contentPane.add(separator, 0, 1, 6, 1);

        Text bookNameLabel = new Text();
        Label bookLabel = new Label("Number of books still in stock");
        bookLabel.setStyle("-fx-text-fill: white;");
        bookNameLabel.setFill(Color.WHITE);

        try (Connection conn = JDBCConnect.getJDBCConnection()) {
            String query = "SELECT SUM(quantity_available) AS total_quantity FROM books";
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                bookNameLabel.setText(String.format("%d books", resultSet.getInt("total_quantity")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        StackPane bookStockPane = createBookPane(bookLabel, bookNameLabel, "#4CAF50");
        contentPane.add(bookStockPane, 0, 3);

        Text booksBorrowedLabel = new Text();
        Label borrowedLabel = new Label("Number of books borrowed");
        borrowedLabel.setStyle("-fx-text-fill: white; -fx-font-size: 15px; -fx-font-weight: bold;");
        booksBorrowedLabel.setFill(Color.WHITE);

        try (Connection conn = JDBCConnect.getJDBCConnection()) {
            String query = "SELECT SUM(quantity) AS total_quantity FROM borrow_book";
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                booksBorrowedLabel.setText(String.format("%d books", resultSet.getInt("total_quantity")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        StackPane bookBorrowedPane = createBookPane(borrowedLabel, booksBorrowedLabel, "#FF9800");
        contentPane.add(bookBorrowedPane, 2, 3);

        Text bookReturnLabel = new Text();
        Label returnedLabel = new Label("Number of books returned");
        returnedLabel.setStyle("-fx-text-fill: white;");
        bookReturnLabel.setFill(Color.WHITE);

        try (Connection conn = JDBCConnect.getJDBCConnection()) {
            String query = "SELECT SUM(quantity) AS total_returned_quantity FROM borrow_book " +
                    "WHERE borrow_book_id IN (SELECT borrow_book_id FROM return_book WHERE return_date IS NOT NULL)";
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                bookReturnLabel.setText(String.format("%d books", resultSet.getInt("total_returned_quantity")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        StackPane bookReturnedPane = createBookPane(returnedLabel, bookReturnLabel, "#2196F3");
        contentPane.add(bookReturnedPane, 4, 3);

        Text totalFineLabel = new Text();
        Label fineLabel = new Label("Total Fine Amount");
        fineLabel.setStyle("-fx-text-fill: white;");
        totalFineLabel.setFill(Color.WHITE);

        try (Connection conn = JDBCConnect.getJDBCConnection()) {
            String query = "SELECT SUM(fine_amount) AS total_quantity FROM violate";
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int totalQuantity = resultSet.getInt("total_quantity");
                totalFineLabel.setText(String.format("%d vnđ", totalQuantity));
            } else {
                totalFineLabel.setText("0 vnđ");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        StackPane finePane = createBookPane(fineLabel, totalFineLabel, "#F8E502FF");
        contentPane.add(finePane, 2, 4);
        GridPane.setMargin(finePane, new Insets(15, 0, 0, 0));

        BorderPane root = new BorderPane();
        root.setLeft(leftMenu);
        root.setCenter(contentPane);

        Scene scene = new Scene(root, 1030, 620);
        primaryStage.setTitle("Violations and Penalties");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private ImageView createImageView(String path, int width, int height) {
        try {
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(width);
            imageView.setFitHeight(height);
            return imageView;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private StackPane createBookPane(Label label, Text text, String backgroundColor) {
        StackPane squarePane = new StackPane();
        squarePane.setPrefHeight(100);
        squarePane.setPrefWidth(300);

        squarePane.setStyle("-fx-background-color: " + backgroundColor + "; " +
                "-fx-background-radius: 15; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 5); " +
                "-fx-padding: 10;");

        label.setStyle("-fx-text-fill: white; -fx-font-size: 13px; -fx-font-weight: bold;");
        text.setStyle("-fx-text-fill: white; -fx-font-size: 23px; -fx-font-weight: bold;");

        VBox vbox = new VBox();
        vbox.getChildren().addAll(label, text);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(5);

        squarePane.getChildren().add(vbox);
        return squarePane;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
