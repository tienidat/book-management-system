package org.example.eproject_2.screen;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.eproject_2.entity.Borrow;
import org.example.eproject_2.service.BorrowService;
import org.example.eproject_2.jdbcConnect.dao.JDBCConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.Optional;

public class BorrowBooksApp extends Application {
    private TableView<Borrow> tableView;
    private BorrowService borrowModel;
    private Pagination pagination;

    private static final int ROWS_PER_PAGE = 10;

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
        ImageView returnIconView = createImageView("/images/borrow_book.png", 20, 20);
        ImageView borrowBooksView = createImageView("/images/borrow_book.png", 20, 20);
        ImageView historyIconView = createImageView("/images/history.png", 20, 20);
        ImageView exitView = createImageView("/images/exit.png", 20, 20);


        Button statisticalBtn = new Button("Dashboard", statisticalIconView);
        statisticalBtn.setOnAction(event -> {
            try {
                Dashboard dashboard = new Dashboard();
                dashboard.start(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

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
            try {
                BookManagement bookManagement = new BookManagement();
                bookManagement.start(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Button borrowReturnBooksBtn = new Button("Borrow Book", borrowBooksView);


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
                History violationsPenalties = new History();
                violationsPenalties.start(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

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

        for (Button btn : new Button[]{statisticalBtn, readersBtn, bookManagementBtn, borrowReturnBooksBtn, returnBook, history, logOut}) {
            borrowReturnBooksBtn.setStyle("-fx-border-color: white; -fx-border-width: 1px; -fx-background-color: #8E2DE2; -fx-pref-width: 180px; -fx-pref-height: 35px; -fx-border-radius: 10px;-fx-font-size: 16px; -fx-border-radius: 10px; -fx-alignment: TOP_LEFT;");

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

        Label readerInfoLabel = new Label("Borrow books");
        readerInfoLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        contentPane.add(readerInfoLabel, 0, 0, 2, 1);

        Separator separator = new Separator();
        separator.setPrefWidth(190);
        contentPane.add(separator, 0, 1, 6, 1);

        ComboBox<String> bookNameComboBox = new ComboBox<>();
        bookNameComboBox.setPromptText("Name Book");
        Label bookInfoLabel = new Label();

        try (Connection conn = JDBCConnect.getJDBCConnection()) {
            String query = "SELECT book_name FROM books";
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                bookNameComboBox.getItems().add(resultSet.getString("book_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        contentPane.add(new Label("Book Name:"), 0, 2);
        contentPane.add(bookNameComboBox, 1, 2);
        contentPane.add(bookInfoLabel, 1, 3);
        bookNameComboBox.setStyle("-fx-pref-width: 150px;");


        Text authorField = new Text();
        contentPane.add(new Label("Author:"), 0, 3);
        contentPane.add(authorField, 1, 3);


        Text priceField = new Text();
        contentPane.add(new Label("Price:"), 0, 4);
        contentPane.add(priceField, 1, 4);

        Text categoryField = new Text();
        contentPane.add(new Label("Category:"), 0, 5);
        contentPane.add(categoryField, 1, 5);

        bookNameComboBox.setOnAction(event -> {
            String selectedBookName = bookNameComboBox.getValue();
            if (selectedBookName != null) {
                try (Connection conn = JDBCConnect.getJDBCConnection()) {
                    String query = "SELECT authors.author_name, books.price, bookCategories.category_name FROM books " +
                            "JOIN authors ON books.author_id = authors.author_id " +
                            "JOIN bookCategories ON books.category_id = bookCategories.category_id " +
                            "WHERE book_name = ?;";
                    PreparedStatement statement = conn.prepareStatement(query);
                    statement.setString(1, selectedBookName);
                    ResultSet resultSet = statement.executeQuery();

                    if (resultSet.next()) {
                        String author = resultSet.getString("author_name");
                        String price = resultSet.getString("price");
                        String category = resultSet.getString("category_name");

                        authorField.setText(author);
                        priceField.setText(price);
                        categoryField.setText(category);
                    } else {
                        authorField.setText("");
                        priceField.setText("");
                        categoryField.setText("");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                authorField.setText("");
                priceField.setText("");
                categoryField.setText("");
            }
        });


        ComboBox<String> readerNameComboBox = new ComboBox<>();
        readerNameComboBox.setPromptText("Reader's name");
        try (Connection conn = JDBCConnect.getJDBCConnection()) {
            String query = "SELECT reader_name FROM readers";
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                readerNameComboBox.getItems().add(resultSet.getString("reader_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        readerNameComboBox.setPromptText("Reader's name:");
        contentPane.add(new Label("Reader Name:"), 2, 2);
        contentPane.add(readerNameComboBox, 3, 2);

        Text emailField = new Text();
        contentPane.add(new Label("Email:"), 2, 3);
        contentPane.add(emailField, 3, 3);
        emailField.setWrappingWidth(160);

        Text phoneField = new Text();
        contentPane.add(new Label("Phone:"), 2, 4);
        contentPane.add(phoneField, 3, 4);

        Text dobField = new Text();
        contentPane.add(new Label("DOB:"), 2, 5);
        contentPane.add(dobField, 3, 5);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        readerNameComboBox.setOnAction(event -> {
            String selectedBookName = readerNameComboBox.getValue();
            if (selectedBookName != null) {
                try (Connection conn = JDBCConnect.getJDBCConnection()) {
                    String query = "SELECT readers.email, readers.phone_number, readers.date_of_birth FROM readers " +
                            "WHERE reader_name = ?;";
                    PreparedStatement statement = conn.prepareStatement(query);
                    statement.setString(1, selectedBookName);
                    ResultSet resultSet = statement.executeQuery();

                    if (resultSet.next()) {
                        String email = resultSet.getString("email");
                        String phone = resultSet.getString("phone_number");
                        String dob = resultSet.getString("date_of_birth");

                        String formattedDob;
                        try {
                            LocalDate dateOfBirth = LocalDate.parse(dob);
                            formattedDob = dateOfBirth.format(dateFormatter);
                        } catch (DateTimeParseException e) {
                            formattedDob = dob;
                        }
                        emailField.setText(email);
                        phoneField.setText(phone);
                        dobField.setText(formattedDob);
                    } else {
                        emailField.setText("");
                        phoneField.setText("");
                        dobField.setText("");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                emailField.setText("");
                phoneField.setText("");
                dobField.setText("");
            }
        });

        DatePicker borrowDate = new DatePicker(LocalDate.now());
        borrowDate.setStyle("-fx-opacity: 2; ");
        contentPane.add(new Label("Borrow Date:"), 4, 2);
        contentPane.add(borrowDate, 5, 2);

        DatePicker dueDate = new DatePicker();
        contentPane.add(new Label("Due date:"), 4, 3);
        contentPane.add(dueDate, 5, 3);

        dueDate.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.isBefore(borrowDate.getValue())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Date");
                alert.setHeaderText(null);
                alert.setContentText("Due date must be later than the borrow date.");
                alert.showAndWait();
                dueDate.setValue(null);
            }
        });

        contentPane.add(new Label("Quantity:"), 4, 4);
        Spinner<Integer> amountSpinner = new Spinner<>(1, 9999, 1);
        amountSpinner.setPrefWidth(80);
        contentPane.add(amountSpinner, 5, 4);

        Separator genderSeparator = new Separator();
        genderSeparator.setPrefWidth(200);
        contentPane.add(genderSeparator, 0, 8, 6, 1);

        Button addButton = new Button("Borrow");

        TextField searchField = new TextField();
        searchField.setPromptText("Search by name...");

        HBox actionButtons = new HBox(20, addButton,searchField);
        contentPane.add(actionButtons, 1, 9, 2, 1);

        addButton.setMinWidth(70);
        searchField.setMinWidth(150);

        tableView = new TableView<>();
        setupTableView();
        borrowModel = new BorrowService();
        tableView.setItems(borrowModel.getAll());

        tableView.setPrefHeight(270);
        contentPane.add(tableView, 0, 9, 6, 1);

        pagination = new Pagination((int) Math.ceil((double) borrowModel.getAll().size() / ROWS_PER_PAGE), 0);
        pagination.setPageFactory(this::createPage);
        contentPane.add(pagination, 0, 10, 6, 1);

        addButton.setOnAction(event -> {
            try {
                if (bookNameComboBox.getValue() == null ||
                        readerNameComboBox.getValue() == null ||
                        borrowDate.getValue() == null ||
                        dueDate.getValue() == null) {

                    showAlert("No information was entered!!", "Please enter complete information!!!");
                    return;
                }
                int requested = amountSpinner.getValue();
                if (requested <= 0) {
                    showAlert("Invalid quantity", "Quantity must be greater than 0");
                    return;
                }

                // Check stock before borrowing
                try (Connection conn = JDBCConnect.getJDBCConnection()) {
                    String stockSql = "SELECT quantity_available FROM books WHERE book_name = ?";
                    PreparedStatement stockStmt = conn.prepareStatement(stockSql);
                    stockStmt.setString(1, bookNameComboBox.getValue());
                    ResultSet stockRs = stockStmt.executeQuery();
                    if (stockRs.next()) {
                        int available = stockRs.getInt("quantity_available");
                        if (requested > available) {
                            showAlert("Not enough stock", "Only " + available + " book(s) available.");
                            return;
                        }
                    } else {
                        showAlert("Book not found", "Selected book is not available.");
                        return;
                    }
                }

                Borrow borrow = new Borrow();
                borrow.setBookName(bookNameComboBox.getValue());
                borrow.setReaderName(readerNameComboBox.getValue());
                borrow.setBorrowDate(borrowDate.getValue());
                borrow.setQuantity(requested);
                borrow.setDueDate(dueDate.getValue());

                boolean ok = borrowModel.add(borrow);
                if (ok) {
                    refreshTable();
                    showAlert("Borrow","Borrow successfully");
                } else {
                    showAlert("Borrow failed","Cannot borrow. Possibly not enough stock or invalid data.");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            String searchName = newValue.trim();
            ObservableList<Borrow> searchResults = borrowModel.findByNameContains(searchName);
            tableView.setItems(searchResults);
        });


        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                bookNameComboBox.setValue(newValue.getBookName());
                readerNameComboBox.setValue(newValue.getReaderName());
                amountSpinner.getValueFactory().setValue(newValue.getQuantity());

                if (newValue.getBorrowDate() != null) {
                    borrowDate.setPromptText(newValue.getBorrowDate().format(dateFormatter));
                } else {
                    borrowDate.setPromptText("");
                }

                if (newValue.getDueDate() != null) {
                    dueDate.setPromptText(newValue.getDueDate().format(dateFormatter));
                } else {
                    dueDate.setPromptText("");
                }
            }
        });

        BorderPane root = new BorderPane();
        root.setLeft(leftMenu);
        root.setCenter(contentPane);

        Scene scene = new Scene(root, 1030, 620);
        primaryStage.setTitle("Library Management System");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void setupTableView() {
        tableView = new TableView<>();

        TableColumn<Borrow, String> bookCol = new TableColumn<>("Name Book");
        bookCol.setCellValueFactory(new PropertyValueFactory<>("bookName"));

        TableColumn<Borrow, String> readerCol = new TableColumn<>("Name Reader");
        readerCol.setCellValueFactory(new PropertyValueFactory<>("readerName"));

        TableColumn<Borrow, Integer> quantityCol = new TableColumn<>("Quantity");
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<Borrow, String> bookDateCol = new TableColumn<>("Book return date");
        bookDateCol.setCellValueFactory(cellData -> {
            Borrow borrow = cellData.getValue();
            return new ReadOnlyStringWrapper(borrow.formatDate(borrow.getBorrowDate())); // Định dạng ngày
        });

        TableColumn<Borrow, String> readerDateCol = new TableColumn<>("Due date");
        readerDateCol.setCellValueFactory(cellData -> {
            Borrow borrow = cellData.getValue();
            return new ReadOnlyStringWrapper(borrow.formatDate(borrow.getDueDate())); // Định dạng ngày
        });

        bookCol.setPrefWidth(230);
        readerCol.setPrefWidth(180);
        quantityCol.setPrefWidth(95);
        bookDateCol.setPrefWidth(125);
        readerDateCol.setPrefWidth(115);

        tableView.getColumns().addAll(bookCol, readerCol, quantityCol, bookDateCol, readerDateCol);
    }


    private VBox createPage(int pageIndex) {
        int fromIndex = pageIndex * ROWS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, borrowModel.getAll().size());

        ObservableList<Borrow> pageData = FXCollections.observableArrayList(borrowModel.getAll().subList(fromIndex, toIndex));
        tableView.setItems(pageData);

        return new VBox(tableView);
    }

    private void refreshTable() {
        ObservableList<Borrow> allReaders = borrowModel.getAll();

        int pageCount = (int) Math.ceil((double) allReaders.size() / ROWS_PER_PAGE);
        pagination.setPageCount(pageCount);

        int currentPageIndex = pagination.getCurrentPageIndex();
        if (currentPageIndex >= pageCount) {
            currentPageIndex = pageCount - 1;
        }
        pagination.setCurrentPageIndex(currentPageIndex);

        tableView.setItems(FXCollections.observableArrayList(allReaders.subList(currentPageIndex * ROWS_PER_PAGE,
                Math.min((currentPageIndex + 1) * ROWS_PER_PAGE, allReaders.size()))));
    }

    private ImageView createImageView(String path, int width, int height) {
        try {
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(width);
            imageView.setFitHeight(height);
            return imageView;
        } catch (NullPointerException e) {
            System.out.println("Không tìm thấy hình ảnh:" + path);
            return new ImageView();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}



