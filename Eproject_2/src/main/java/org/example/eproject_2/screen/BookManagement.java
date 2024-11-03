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
import javafx.stage.Stage;
import org.example.eproject_2.entity.Books;
import org.example.eproject_2.service.BookService;
import org.example.eproject_2.jdbcConnect.dao.JDBCConnect;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

public class BookManagement extends Application {
    private TableView<Books> tableView;
    private BookService bookModel;
    private Pagination pagination;

    private static final int ROWS_PER_PAGE = 10;

    @Override
    public void start(Stage primaryStage) {
        // Left menu (Sidebar)
        VBox leftMenu = new VBox(20); // 20px spacing between elements
        leftMenu.setPadding(new Insets(20));
        leftMenu.setStyle("-fx-background-color: linear-gradient(to bottom, #4A00E0, #8E2DE2);");

        Image homeIcon = new Image(getClass().getResourceAsStream("/images/home.png")); // Ensure this path is correct
        ImageView homeIconView = new ImageView(homeIcon);
        homeIconView.setFitHeight(90);
        homeIconView.setFitWidth(90);

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
        ImageView exitView = createImageView("/images/exit.png", 20,20);


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


        Button logOut = new Button("Log Out", exitView);
        logOut.setOnMouseClicked(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Log Out");
            alert.setHeaderText("Are you sure you want to log out?");
            alert.setContentText("Click OK to confirm or Cancel to go back.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    LoginPage login = new LoginPage();
                    login.start(primaryStage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        for (Button btn : new Button[]{statisticalBtn, readersBtn, bookManagementBtn, borrowReturnBooksBtn, returnBook, history, logOut}) {
            bookManagementBtn.setStyle("-fx-border-color: white; -fx-border-width: 1px; -fx-background-color: #8E2DE2; -fx-pref-width: 180px; -fx-pref-height: 35px; -fx-border-radius: 10px;-fx-font-size: 16px; -fx-alignment: TOP_LEFT;");

            btn.setPrefWidth(190);
            btn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 16px;-fx-alignment: TOP_LEFT;");
        }

        VBox buttonsContainer = new VBox(20,statisticalBtn,  readersBtn, bookManagementBtn, borrowReturnBooksBtn, returnBook, history, logOut);
        menuContainer.getChildren().add(buttonsContainer);

        leftMenu.getChildren().add(menuContainer);

        GridPane contentPane = new GridPane();
        contentPane.setPadding(new Insets(20));
        contentPane.setVgap(10);
        contentPane.setHgap(10);

        Label bookInfoLabel = new Label("Book Information");
        bookInfoLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        contentPane.add(bookInfoLabel, 0, 0, 2, 1);

        Separator separator = new Separator();
        separator.setPrefWidth(200);
        contentPane.add(separator, 0, 1, 7, 1);

        TextField nameField = new TextField();
        nameField.setPromptText("Name book");
        contentPane.add(new Label("Name Book:"), 0, 2);
        contentPane.add(nameField, 1, 2);

        ComboBox<String> authorField = new ComboBox<>();
        authorField.setPromptText("Author");
        Label authors = new Label();
        try (Connection conn = JDBCConnect.getJDBCConnection()) {
            String query = "SELECT author_name FROM authors";
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                authorField.getItems().add(resultSet.getString("author_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        contentPane.add(new Label("Author:"), 0, 5);
        contentPane.add(authorField, 1, 5);
        contentPane.add(authors, 3, 5);

        ComboBox<String> categoryField = new ComboBox<>();
        categoryField.setPromptText("Category");
        Label category = new Label();
        try (Connection conn = JDBCConnect.getJDBCConnection()) {
            String query = "SELECT category_name FROM bookCategories";
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                categoryField.getItems().add(resultSet.getString("category_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        contentPane.add(new Label("Category:"), 0, 4);
        contentPane.add(categoryField, 1, 4);
        contentPane.add(category, 3, 4);


        Spinner<Integer> quantityField = new Spinner<>(0, 99, 1);
        quantityField.setPrefWidth(80);
        contentPane.add(new Label("Quantity:"), 3, 5);
        contentPane.add(quantityField, 4, 5);

        DatePicker yearPublishedField = new DatePicker();
        contentPane.add(new Label("Year published:"), 3, 2);
        contentPane.add(yearPublishedField, 4, 2);


        ComboBox<String> publisherField = new ComboBox<>();
        publisherField.setPromptText("Publisher");
        Label publisher = new Label();
        try (Connection conn = JDBCConnect.getJDBCConnection()) {
            String query = "SELECT publisher_name FROM publishers";
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                publisherField.getItems().add(resultSet.getString("publisher_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        contentPane.add(new Label("Publisher:"), 0, 3);
        contentPane.add(publisherField, 1, 3);
        contentPane.add(publisher, 3, 3);

        TextField isbnField = new TextField();
        isbnField.setPromptText("Enter ISBN");
        contentPane.add(new Label("ISBN:"), 3, 4);
        contentPane.add(isbnField, 4, 4);

        TextField priceField = new TextField();
        priceField.setPromptText("Enter price");
        contentPane.add(new Label("Price:"), 3, 3);
        contentPane.add(priceField, 4, 3);


        Separator genderSeparator = new Separator();
        genderSeparator.setPrefWidth(200);
        contentPane.add(genderSeparator, 0, 6, 7, 1);

        Button addButton = new Button("Add");
        Button updateButton = new Button("Update");
        Button deleteButton = new Button("Delete");

        updateButton.setVisible(false);
        deleteButton.setVisible(false);

        TextField searchField = new TextField();
        searchField.setPromptText("Search by name...");

        addButton.setMinWidth(70);
        updateButton.setMinWidth(70);
        deleteButton.setMinWidth(70);
        searchField.setMinWidth(150);

        HBox actionButtons = new HBox(20, addButton, updateButton, deleteButton, searchField);
        contentPane.add(actionButtons, 1, 7, 2, 1);

        tableView = new TableView<>();
        setupTableView();
        bookModel = new BookService();
        tableView.setItems(bookModel.getAll());
        tableView.setPrefHeight(270);
        contentPane.add(tableView, 0, 8, 5, 1);


        pagination = new Pagination((int) Math.ceil((double) bookModel.getAll().size() / ROWS_PER_PAGE), 0);
        pagination.setPageFactory(this::createPage);
        contentPane.add(pagination, 0, 9, 6, 1);

        Label nameBookLablels = new Label();
        nameBookLablels.setStyle("-fx-text-fill: red;");
        nameBookLablels.setPrefWidth(100);

        Label isbnLablels = new Label();
        isbnLablels.setStyle("-fx-text-fill: red;");
        isbnLablels.setPrefWidth(100);

        Label yearPublishedLablel = new Label();
        yearPublishedLablel.setStyle("-fx-text-fill: red;");
        yearPublishedLablel.setPrefWidth(100);


        Label priceLablel = new Label();
        priceLablel.setStyle("-fx-text-fill: red;");
        priceLablel.setPrefWidth(100);

        Label authorLablel = new Label();
        authorLablel.setStyle("-fx-text-fill: red;");
        authorLablel.setPrefWidth(100);

        contentPane.add(authorLablel, 2, 5);
        contentPane.add(nameBookLablels, 2, 2);
        contentPane.add(yearPublishedLablel, 5, 2);
        contentPane.add(priceLablel, 5, 3);
        contentPane.add(isbnLablels, 5, 4);

        addButton.setOnAction(event -> {
            isbnLablels.setText("");
            priceLablel.setText("");
            yearPublishedLablel.setText("");
            nameBookLablels.setText("");
            authorLablel.setText("");


            String isbn = isbnField.getText().trim();
            String price = priceField.getText().trim();
            String nameBooks = nameField.getText().trim();
            String author = authorField.getValue().trim();
            LocalDate yearPublished = yearPublishedField.getValue();

            boolean isValid = true;

            if (isbn == null) {
                isbnLablels.setText("Cannot be empty!");
                isValid = false;
            } else if (!isbn.matches("\\d{13}")) {
                isbnLablels.setText("Min 13 digits!");
                isValid = false;
            }

            if (author.isEmpty()) {
                authorLablel.setText("Cannot be empty!");
                isValid = false;
            } else if (!author.matches("^[a-zA-Z\\s]+$")) {
                authorLablel.setText("Letters only!");
                isValid = false;
            }

            if (price.isEmpty()) {
                priceLablel.setText("Cannot be empty!");
                isValid = false;
            } else if (!price.matches("\\d+")) {
                priceLablel.setText("Numbers only!");
                isValid = false;
            }else {
                priceLablel.setText(null);
            }


            if (nameBooks.isEmpty()) {
                nameBookLablels.setText("Cannot be empty!");
            } else {
                nameBookLablels.setText(null);
            }


            if (yearPublished == null) {
                yearPublishedLablel.setText("Cannot be empty!");
            } else {
                yearPublishedLablel.setText(null);
            }

            if (isValid) {
                try {
                    Books book = new Books();
                    book.setBookName(nameField.getText());
                    book.setAuthor(authorField.getValue());
                    book.setCategory(categoryField.getValue());
                    book.setQuantity(Integer.parseInt(String.valueOf(quantityField.getValue())));
                    book.setYearPublished(yearPublishedField.getValue());
                    book.setPublisher(publisherField.getValue());
                    book.setIsbn(isbnField.getText());
                    book.setPrice(Double.parseDouble(priceField.getText()));
                    bookModel.add(book);
                    refreshTable();
                    showAlert("Add Book","Add Book successfully!");
                } catch (Exception e) {
                    showAlert("Error", "Please fill in all fields correctly.");
                }
            }

        });

        updateButton.setOnAction(event -> {
            Books selectBook = tableView.getSelectionModel().getSelectedItem();
            if (selectBook != null) {
                selectBook.setBookName(nameField.getText());
                selectBook.setPublisher(publisherField.getValue());
                selectBook.setCategory(categoryField.getValue());
                selectBook.setAuthor(authorField.getValue());
                selectBook.setQuantity(Integer.parseInt(String.valueOf(quantityField.getValue())));
                selectBook.setYearPublished(yearPublishedField.getValue());
                selectBook.setIsbn(isbnField.getText());
                selectBook.setPrice(Double.parseDouble(priceField.getText()));
                bookModel.update(selectBook);
                refreshTable();
                showAlert("Update Book","Update Book successfully!");

            } else {
                showAlert("Error", "Please select a book to update.");
            }
        });

        deleteButton.setOnAction(event -> {
            Books selectedBook = tableView.getSelectionModel().getSelectedItem();
            if (selectedBook != null) {
                if (selectedBook.getQuantity() > 0) {
                    showAlert("Error", "Cannot delete the book because it is still in stock.");
                } else {
                    if (bookModel.hasBorrowedBooks(selectedBook.getBookId())) {
                        showAlert("Error", "Cannot delete the book because it is currently borrowed.");
                    } else {
                        bookModel.delete(selectedBook.getBookId());
                        refreshTable();
                        showAlert("Delete Book","Delete Book successfully!");

                    }
                }
            } else {
                showAlert("Error", "Please select a book to delete.");
            }
        });

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            String searchName = newValue.trim();
            ObservableList<Books> searchResults = bookModel.findByNameContains(searchName);
            tableView.setItems(searchResults);
        });

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->{
            if(newValue != null){
                nameField.setText(newValue.getBookName());
                authorField.setValue(newValue.getAuthor());
                categoryField.setValue(newValue.getCategory());
                quantityField.getValueFactory().setValue(newValue.getQuantity());
                LocalDate yearPublished = newValue.getYearPublished();
                if (yearPublished != null) {
                    yearPublishedField.setValue(yearPublished);
                    yearPublishedField.getEditor().setText(yearPublished.format(dateFormatter));
                }
                priceField.setText(String.valueOf(newValue.getPrice()));
                publisherField.setValue(newValue.getPublisher());
                isbnField.setText(newValue.getIsbn());
                updateButton.setVisible(true);
                deleteButton.setVisible(true);
            } else {
                updateButton.setVisible(false);
                deleteButton.setVisible(false);
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

    private void refreshTable() {
        ObservableList<Books> allBooks = bookModel.getAll();

        int pageCount = (int) Math.ceil((double) allBooks.size() / ROWS_PER_PAGE);
        pagination.setPageCount(pageCount);

        int currentPageIndex = pagination.getCurrentPageIndex();
        if (currentPageIndex >= pageCount) {
            currentPageIndex = pageCount - 1;
        }
        pagination.setCurrentPageIndex(currentPageIndex);

        tableView.setItems(FXCollections.observableArrayList(allBooks.subList(currentPageIndex * ROWS_PER_PAGE,
                Math.min((currentPageIndex + 1) * ROWS_PER_PAGE, allBooks.size()))));
    }
    private VBox createPage(int pageIndex) {
        int fromIndex = pageIndex * ROWS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, bookModel.getAll().size());

        ObservableList<Books> pageData = FXCollections.observableArrayList(bookModel.getAll().subList(fromIndex, toIndex));
        tableView.setItems(pageData);

        return new VBox(tableView);
    }

    public void setupTableView() {
        tableView = new TableView<>();

        TableColumn<Books, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("bookName"));

        TableColumn<Books, String> authorCol = new TableColumn<>("Author");
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));

        TableColumn<Books, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));

        TableColumn<Books, Integer> quantityCol = new TableColumn<>("Quantity");
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<Books, String> publisherCol = new TableColumn<>("Publisher");
        publisherCol.setCellValueFactory(new PropertyValueFactory<>("publisher"));

        TableColumn<Books, String> yearPublishedCol = new TableColumn<>("Year published");
        yearPublishedCol.setCellValueFactory(cellDate ->{
            Books bookYear = cellDate.getValue();
            return new ReadOnlyStringWrapper(bookModel.formatYearPublished(bookYear.getYearPublished()));
        });
        TableColumn<Books, Integer> isbnCol = new TableColumn<>("Isbn");
        isbnCol.setCellValueFactory(new PropertyValueFactory<>("Isbn"));

        TableColumn<Books, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        nameCol.setPrefWidth(120);
        authorCol.setPrefWidth(90);
        categoryCol.setPrefWidth(70);
        quantityCol.setPrefWidth(70);
        publisherCol.setPrefWidth(133);
        yearPublishedCol.setPrefWidth(95);
        isbnCol.setPrefWidth(95);
        priceCol.setPrefWidth(65);


        tableView.getColumns().addAll(nameCol, authorCol, categoryCol, quantityCol, publisherCol, yearPublishedCol, isbnCol, priceCol);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private ImageView createImageView(String path, int width, int height) {
        try {
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(width);
            imageView.setFitHeight(height);
            return imageView;
        } catch (NullPointerException e) {
            System.out.println("Image not found: " + path);
            return new ImageView();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}