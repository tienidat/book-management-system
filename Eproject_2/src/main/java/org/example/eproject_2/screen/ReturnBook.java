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

import org.example.eproject_2.entity.ReturnB;
import org.example.eproject_2.service.ReturnBookService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.Optional;

public class ReturnBook extends Application {
    private TableView<ReturnB> tableView;
    private ReturnBookService returnModel;
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
        borrowReturnBooksBtn.setOnAction(event -> {
            try {
                BorrowBooksApp borrowBooks = new BorrowBooksApp();
                borrowBooks.start(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Button returnBook = new Button("Return Book", returnIconView);

        Button history = new Button("History", historyIconView);
        history.setOnAction(event -> {
            try {
                History violation = new History();
                violation.start(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


        ImageView exitView = createImageView("/images/exit.png", 20, 20);
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
            btn.setPrefWidth(190);
            returnBook.setStyle("-fx-border-color: white; -fx-border-width: 1px; -fx-background-color: #8E2DE2; -fx-pref-width: 180px; -fx-pref-height: 35px;-fx-font-size: 16px; -fx-border-radius: 10px; -fx-alignment: TOP_LEFT;");
            btn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 16px;-fx-alignment: TOP_LEFT;");
        }

        VBox buttonsContainer = new VBox(20, statisticalBtn, readersBtn, bookManagementBtn, borrowReturnBooksBtn, returnBook, history, logOut);
        menuContainer.getChildren().add(buttonsContainer);
        leftMenu.getChildren().add(menuContainer);

        GridPane contentPane = new GridPane();
        contentPane.setPadding(new Insets(20));
        contentPane.setVgap(10);
        contentPane.setHgap(10);

        Label readerInfoLabel = new Label("Return Book");
        readerInfoLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        contentPane.add(readerInfoLabel, 0, 0, 2, 1);


        Separator separator = new Separator();
        separator.setPrefWidth(190);
        contentPane.add(separator, 0, 1, 6, 1);

        Text bookNameComboBox = new Text();
        contentPane.add(new Label("Book Name:"), 0, 2);
        contentPane.add(bookNameComboBox, 1, 2);
        bookNameComboBox.setWrappingWidth(150);

        Text authorField = new Text();
        contentPane.add(new Label("Author:"), 0, 3);
        contentPane.add(authorField, 1, 3);

        Text priceField = new Text();
        contentPane.add(new Label("Price:"), 0, 4);
        contentPane.add(priceField, 1, 4);

        Text categoryField = new Text();
        contentPane.add(new Label("Category:"), 0, 5);
        contentPane.add(categoryField, 1, 5);

        ComboBox<String> bookCondition = new ComboBox<>();
        bookCondition.getItems().addAll("Good", "Severely damaged", "Slightly damaged", "Lost");
        contentPane.add(new Label("Book Status:"), 0, 6);
        contentPane.add(bookCondition, 1, 6);
        bookCondition.setPrefWidth(130);

        Text readerNameComboBox = new Text();
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

        Text borrowDate = new Text();
        Text dueDate = new Text();


        contentPane.add(new Label("Borrow Date:"), 4, 2);
        contentPane.add(borrowDate, 5, 2);

        contentPane.add(new Label("Due date:"), 4, 3);
        contentPane.add(dueDate, 5, 3);

        DatePicker returnDate = new DatePicker();
        contentPane.add(new Label("Return date:"), 4, 4);
        contentPane.add(returnDate, 5, 4);

        returnDate.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate borrowDateValue = LocalDate.parse(borrowDate.getText(), formatter);

                if (newValue.isBefore(borrowDateValue)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid Date");
                    alert.setHeaderText(null);
                    alert.setContentText("Return date cannot be less than the current date.");
                    alert.showAndWait();
                    returnDate.setValue(null);
                }
            }
        });

        contentPane.add(new Label("Quantity:"), 4, 5);
        Spinner<Integer> amountSpinner = new Spinner<>(1, 9999, 1);
        amountSpinner.setPrefWidth(80);
        contentPane.add(amountSpinner, 5, 5);

        Separator genderSeparator = new Separator();
        genderSeparator.setPrefWidth(200);
        contentPane.add(genderSeparator, 0, 8, 6, 1);

        Button updateButton = new Button("Return");

        TextField searchField = new TextField();
        searchField.setPromptText("Search by name...");

        HBox actionButtons = new HBox(20, updateButton, searchField);
        contentPane.add(actionButtons, 1, 9, 2, 1);

        updateButton.setMinWidth(70);
        searchField.setMinWidth(150);

        tableView = new TableView<>();
        setupTableView();
        returnModel = new ReturnBookService();
        tableView.setItems(returnModel.getAll());


        tableView.setPrefHeight(280);
        contentPane.add(tableView, 0, 10, 6, 1);

        pagination = new Pagination((int) Math.ceil((double) returnModel.getAll().size() / ROWS_PER_PAGE), 0);
        pagination.setPageFactory(this::createPage);
        contentPane.add(pagination, 0, 11, 6, 1);

        updateButton.setOnAction(event -> {
            ReturnB selectReturn = tableView.getSelectionModel().getSelectedItem();
            if (selectReturn != null) {
                if (returnDate.getValue() != null) {
                    try {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                        selectReturn.setBookName(bookNameComboBox.getText());
                        selectReturn.setReaderName(readerNameComboBox.getText());
                        selectReturn.setQuantity(amountSpinner.getValue());
                        selectReturn.setBorrowDate(LocalDate.parse(borrowDate.getText(), formatter));
                        selectReturn.setDueDate(LocalDate.parse(dueDate.getText(), formatter));
                        selectReturn.setReturnDate(returnDate.getValue());
                        selectReturn.setBookCondition(bookCondition.getValue());

                        returnModel.update(selectReturn);

                        if ((bookCondition.getValue().equals("Severely damaged") ||
                                bookCondition.getValue().equals("Slightly damaged") ||
                                bookCondition.getValue().equals("Lost")) ||
                                returnDate.getValue().isAfter(LocalDate.parse(dueDate.getText(), formatter))) {

                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Violation Detected");
                            alert.setHeaderText("This return has violations.");
                            alert.setContentText("Do you want to go to the Violation page?");

                            alert.showAndWait().ifPresent(response -> {
                                if (response == ButtonType.OK) {
                                    History violationPage = new History();
                                    violationPage.start(primaryStage);
                                }
                            });
                        } else {
                            showAlert("Return Book", "Return successfully");
                        }
                        refreshTable();
                    } catch (DateTimeParseException e) {
                        showAlert("Invalid Date Format", "Please make sure all dates are in the correct format (dd/MM/yyyy).");
                    }
                } else {
                    showAlert("No Reader Selected", "Please select a borrow date.");
                }
            } else {
                showAlert("No Reader Selected", "Please select a borrowed book to update.");
            }
        });

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            String searchName = newValue.trim();
            ObservableList<ReturnB> searchResults = returnModel.findByNameContains(searchName);
            tableView.setItems(searchResults);
        });

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                bookNameComboBox.setText(newValue.getBookName());
                authorField.setText(newValue.getBookAuthor());
                priceField.setText(String.valueOf(newValue.getBookPrice()));
                categoryField.setText(newValue.getBookCategory());

                readerNameComboBox.setText(newValue.getReaderName());
                emailField.setText(newValue.getEmail());
                phoneField.setText(newValue.getPhoneNumber());

                if (newValue.getDob() != null) {
                    dobField.setText(newValue.getDob().format(dateFormatter));
                } else {
                    dobField.setText("");
                }

                bookCondition.setValue(newValue.getBookCondition());
                amountSpinner.getValueFactory().setValue(newValue.getQuantity());
                borrowDate.setText(newValue.getBorrowDate().format(dateFormatter));
                dueDate.setText(newValue.getDueDate().format(dateFormatter));

                if (newValue.getReturnDate() != null) {
                    returnDate.setValue(newValue.getReturnDate());
                } else {
                    returnDate.setValue(LocalDate.now());
                }

                bookCondition.setValue(String.valueOf(newValue.getBookCondition()));
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

        TableColumn<ReturnB, String> bookCol = new TableColumn<>("Name Book");
        bookCol.setCellValueFactory(new PropertyValueFactory<>("bookName"));

        TableColumn<ReturnB, String> readerCol = new TableColumn<>("Name Reader");
        readerCol.setCellValueFactory(new PropertyValueFactory<>("readerName"));

        TableColumn<ReturnB, Integer> quantityCol = new TableColumn<>("Quantity");
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<ReturnB, String> borrowDateCol = new TableColumn<>("Borrow Date");
        borrowDateCol.setCellValueFactory(cellData -> {
            ReturnB returnB = cellData.getValue();
            return new ReadOnlyStringWrapper(returnB.formatDate(returnB.getBorrowDate()));
        });

        TableColumn<ReturnB, String> dueDateCol = new TableColumn<>("Due date");
        dueDateCol.setCellValueFactory(cellData -> {
            ReturnB returnB = cellData.getValue();
            return new ReadOnlyStringWrapper(returnB.formatDate(returnB.getDueDate()));
        });

        TableColumn<ReturnB, String> returnDateCol = new TableColumn<>(" Return date");
        returnDateCol.setCellValueFactory(cellData -> {
            ReturnB returnB = cellData.getValue();
            return new ReadOnlyStringWrapper(returnB.formatDate(returnB.getReturnDate()));
        });

        TableColumn<ReturnB, String> conditionCol = new TableColumn<>("Book Status");
        conditionCol.setCellValueFactory(new PropertyValueFactory<>("bookCondition"));

        bookCol.setPrefWidth(177);
        readerCol.setPrefWidth(120);
        quantityCol.setPrefWidth(60);
        borrowDateCol.setPrefWidth(90);
        dueDateCol.setPrefWidth(90);
        returnDateCol.setPrefWidth(90);
        conditionCol.setPrefWidth(120);

        tableView.getColumns().addAll(bookCol, readerCol, quantityCol, borrowDateCol, dueDateCol, returnDateCol, conditionCol);
    }

    private VBox createPage(int pageIndex) {
        int fromIndex = pageIndex * ROWS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, returnModel.getAll().size());

        ObservableList<ReturnB> pageData = FXCollections.observableArrayList(returnModel.getAll().subList(fromIndex, toIndex));
        tableView.setItems(pageData);

        return new VBox(tableView);
    }

    private void refreshTable() {
        ObservableList<ReturnB> allReaders = returnModel.getAll();

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
            System.out.println("Image not found: " + path);
            return new ImageView();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
