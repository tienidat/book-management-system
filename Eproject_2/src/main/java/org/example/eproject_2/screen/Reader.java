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
import org.example.eproject_2.entity.Readers;
import org.example.eproject_2.service.ReaderService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

public class Reader extends Application {
    private TableView<Readers> tableView;
    private ReaderService readerModel;
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
        returnBook.setOnAction(event -> {
            try {
                ReturnBook returnBooks = new ReturnBook();
                returnBooks.start(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

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
            readersBtn.setStyle("-fx-border-color: white; -fx-border-width: 1px; -fx-background-color: #8E2DE2; -fx-pref-width: 180px; -fx-pref-height: 35px; -fx-border-radius: 10px;-fx-font-size: 16px; -fx-border-radius: 10px; -fx-alignment: TOP_LEFT;");

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

        Label readerInfoLabel = new Label("Reader Information");
        readerInfoLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        contentPane.add(readerInfoLabel, 0, 0, 2, 1);


        Separator separator = new Separator();
        separator.setPrefWidth(200);
        contentPane.add(separator, 0, 1, 6, 1);

        TextField nameField = new TextField();
        nameField.setPromptText("Name Readers");
        TextField addressField = new TextField();
        addressField.setPromptText("Address");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        TextField phoneNumberField = new TextField();
        phoneNumberField.setPromptText("Phone");

        DatePicker dob = new DatePicker();

        contentPane.add(new Label("Name:"), 0, 2);
        contentPane.add(nameField, 1, 2);
        contentPane.add(new Label("Email:"), 0, 3);
        contentPane.add(emailField, 1, 3);
        contentPane.add(new Label("Phone:"), 0, 4);
        contentPane.add(phoneNumberField, 1, 4);

        contentPane.add(new Label("Date of birth:"), 3, 2);
        contentPane.add(dob, 4, 2);

        ToggleGroup genderGroup = new ToggleGroup();
        RadioButton maleRadio = new RadioButton("Male");
        maleRadio.setToggleGroup(genderGroup);
        RadioButton femaleRadio = new RadioButton("Female");
        femaleRadio.setToggleGroup(genderGroup);

        contentPane.add(new Label("Gender:"), 3, 3);
        HBox genderBox = new HBox(10, maleRadio, femaleRadio);
        contentPane.add(genderBox, 4, 3);

        Separator genderSeparator = new Separator();
        genderSeparator.setPrefWidth(200);
        contentPane.add(genderSeparator, 0, 6, 6, 1);

        Button addButton = new Button("Add");
        Button updateButton = new Button("Update");
        Button deleteButton = new Button("Delete");

        updateButton.setVisible(false);
        deleteButton.setVisible(false);

        TextField searchField = new TextField();
        searchField.setPromptText("Search by name...");

        HBox actionButtons = new HBox(20, addButton, updateButton, deleteButton, searchField);
        contentPane.add(actionButtons, 1, 7, 2, 1);

        addButton.setMinWidth(70);
        updateButton.setMinWidth(70);
        deleteButton.setMinWidth(70);
        searchField.setMinWidth(150);

        tableView = new TableView<>();
        setupTableView();
        readerModel = new ReaderService();
        tableView.setItems(readerModel.getAll());

        tableView.setPrefHeight(270);
        contentPane.add(tableView, 0, 9, 7, 1);

        pagination = new Pagination((int) Math.ceil((double) readerModel.getAll().size() / ROWS_PER_PAGE), 0);
        pagination.setPageFactory(this::createPage);
        contentPane.add(pagination, 0, 10, 6, 1);

        Label nameErrorLabel = new Label();
        nameErrorLabel.setStyle("-fx-text-fill: red;");
        nameErrorLabel.setPrefWidth(120);

        Label emailErrorLabel = new Label();
        emailErrorLabel.setStyle("-fx-text-fill: red;");
        emailErrorLabel.setPrefWidth(120);

        Label phoneErrorLabel = new Label();
        phoneErrorLabel.setStyle("-fx-text-fill: red;");
        phoneErrorLabel.setPrefWidth(120);

        Label dobErrorLabel = new Label();
        dobErrorLabel.setStyle("-fx-text-fill: red;");
        dobErrorLabel.setPrefWidth(100);

        Label genderErrorLabel = new Label();
        genderErrorLabel.setStyle("-fx-text-fill: red;");
        genderErrorLabel.setPrefWidth(100);

        contentPane.add(nameErrorLabel, 2, 2);
        contentPane.add(emailErrorLabel, 2, 3);
        contentPane.add(phoneErrorLabel, 2, 4);
        contentPane.add(dobErrorLabel, 5, 2);
        contentPane.add(genderErrorLabel, 5, 3);

        addButton.setOnAction(event -> {
            nameErrorLabel.setText("");
            emailErrorLabel.setText("");
            phoneErrorLabel.setText("");
            dobErrorLabel.setText("");
            genderErrorLabel.setText("");

            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneNumberField.getText().trim();
            LocalDate dobDate = dob.getValue();

            boolean isValid = true;
            if (name.isEmpty()) {
                nameErrorLabel.setText("Name empty!");
                isValid = false;
            } else if (!name.matches("^[a-zA-Z\\s]+$")) {
                nameErrorLabel.setText("Letters only!");
                isValid = false;
            }

            if (email.isEmpty()) {
                emailErrorLabel.setText("Email empty.");
                isValid = false;
            } else if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
                emailErrorLabel.setText("Invalid email!");
                isValid = false;
            }

            if (phone.isEmpty()) {
                phoneErrorLabel.setText("Phone empty!");
                isValid = false;
            } else if (!phone.matches("^\\d{10,}$")) {
                phoneErrorLabel.setText("Least 10 digits!");
                isValid = false;
            }

            if (dobDate == null) {
                dobErrorLabel.setText("DOB empty.");
                isValid = false;
            }

            if (!maleRadio.isSelected() && !femaleRadio.isSelected()) {
                genderErrorLabel.setText("Gender empty.");
                isValid = false;
            }

            if (isValid) {
                try {
                    Readers reader = new Readers();
                    reader.setReaderName(name);
                    reader.setEmail(email);
                    reader.setPhoneNumber(phone);
                    reader.setGender(maleRadio.isSelected() ? "Male" : "Female");
                    reader.setDateOfBirth(dobDate);

                    readerModel.add(reader);
                    refreshTable();
                    showAlert("Add Reader", "Add successfully!");

                } catch (Exception e) {
                    nameErrorLabel.setText("Please fill in all fields correctly.");
                    nameErrorLabel.setStyle("-fx-text-fill: red;");
                }
            }
        });


        updateButton.setOnAction(event -> {
            nameErrorLabel.setText("");
            emailErrorLabel.setText("");
            phoneErrorLabel.setText("");

            Readers selectedReader = tableView.getSelectionModel().getSelectedItem(); // Lấy reader được chọn
            if (selectedReader != null) {
                String name = nameField.getText().trim();
                String email = emailField.getText().trim();
                String phone = phoneNumberField.getText().trim();

                boolean isValid = true;

                if (name.isEmpty()) {
                    nameErrorLabel.setText("Name cannot be empty.");
                    isValid = false;
                } else if (!name.matches("^[a-zA-Z\\s]+$")) {
                    nameErrorLabel.setText("Name can only contain letters.");
                    isValid = false;
                }

                if (email.isEmpty()) {
                    emailErrorLabel.setText("Email cannot be empty.");
                    isValid = false;
                } else if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
                    emailErrorLabel.setText("Please enter a valid email address.");
                    isValid = false;
                }

                if (phone.isEmpty()) {
                    phoneErrorLabel.setText("Phone number cannot be empty.");
                    isValid = false;
                } else if (!phone.matches("^\\d{10,}$")) {
                    phoneErrorLabel.setText("Phone number must be at least 10 digits.");
                    isValid = false;
                }

                if (isValid) {
                    try {
                        selectedReader.setReaderName(name);
                        selectedReader.setEmail(email);
                        selectedReader.setPhoneNumber(phone);
                        selectedReader.setGender(maleRadio.isSelected() ? "Male" : "Female");
                        selectedReader.setDateOfBirth(dob.getValue());

                        readerModel.update(selectedReader);
                        refreshTable();

                        // Hiển thị thông báo thành công
                        nameErrorLabel.setText("Update successfully!");
                        nameErrorLabel.setStyle("-fx-text-fill: green;");
                    } catch (Exception e) {
                        nameErrorLabel.setText("Please fill in all fields correctly.");
                        nameErrorLabel.setStyle("-fx-text-fill: red;");
                    }
                }
            } else {
                nameErrorLabel.setText("Please select a reader to update.");
            }
        });

        deleteButton.setOnAction(event -> {
            Readers selectedReader = tableView.getSelectionModel().getSelectedItem();
            if (selectedReader != null) {
                readerModel.delete(selectedReader.getReaderId());
                refreshTable();
                showAlert("Delete Reader", "Delete successfully!");
            } else {
                showAlert("No Reader Selected", "Please select a reader to delete.");
            }
        });

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            String searchName = newValue.trim();
            ObservableList<Readers> searchResults = readerModel.findByNameContains(searchName);
            tableView.setItems(searchResults);
        });

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                nameField.setText(newValue.getReaderName());
                emailField.setText(newValue.getEmail());
                phoneNumberField.setText(newValue.getPhoneNumber());
                if (newValue.isMale()) {
                    maleRadio.setSelected(true);
                } else {
                    femaleRadio.setSelected(true);
                }
                if (newValue.getDateOfBirth() != null) {
                    String formattedDate = newValue.getDateOfBirth().format(dateFormatter);
                    dob.getEditor().setText(formattedDate);
                } else {
                    dob.getEditor().clear();
                }
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

    private VBox createPage(int pageIndex) {
        int fromIndex = pageIndex * ROWS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, readerModel.getAll().size());

        ObservableList<Readers> pageData = FXCollections.observableArrayList(readerModel.getAll().subList(fromIndex, toIndex));
        tableView.setItems(pageData);

        return new VBox(tableView);
    }


    private void setupTableView() {
        tableView = new TableView<>();

        TableColumn<Readers, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("readerName"));

        TableColumn<Readers, String> genderCol = new TableColumn<>("Gender");
        genderCol.setCellValueFactory(cellData -> {
            Readers reader = cellData.getValue();
            return new ReadOnlyStringWrapper(reader.isMale() ? "Male" : "Female");
        });

        TableColumn<Readers, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Readers, String> phoneCol = new TableColumn<>("Phone Number");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        TableColumn<Readers, String> dobCol = new TableColumn<>("Date of Birth");
        dobCol.setCellValueFactory(cellData -> {
            Readers reader = cellData.getValue();
            return new ReadOnlyStringWrapper(readerModel.formatDateOfBirth(reader.getDateOfBirth()));
        });

        nameCol.setPrefWidth(200);
        dobCol.setPrefWidth(135);
        genderCol.setPrefWidth(100);
        emailCol.setPrefWidth(200);
        phoneCol.setPrefWidth(120);

        tableView.getColumns().addAll(nameCol, dobCol, genderCol, emailCol, phoneCol);
    }


    private void refreshTable() {
        ObservableList<Readers> allReaders = readerModel.getAll();

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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}