package org.example.eproject_2.screen;

import javafx.application.Application;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.eproject_2.entity.HistoryEntity;
import org.example.eproject_2.service.HistoryService;

import java.util.Objects;
import java.util.Optional;

public class History extends Application {
    private TableView<HistoryEntity> tableView;
    private HistoryService historyModel;
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
            try{
                BorrowBooksApp borrowBooks = new BorrowBooksApp();
                borrowBooks.start(primaryStage);
            }catch (Exception e) {
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

        for (Button btn : new Button[]{statisticalBtn,readersBtn, bookManagementBtn, borrowReturnBooksBtn, returnBook, history, logOut}) {
            history.setStyle("-fx-border-color: white; -fx-border-width: 1px; -fx-background-color: #8E2DE2; -fx-pref-width: 180px; -fx-pref-height: 35px;-fx-font-size: 16px; -fx-border-radius: 10px; -fx-alignment: TOP_LEFT;");

            btn.setPrefWidth(190);
            btn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 16px;-fx-alignment: TOP_LEFT;");
        }

        VBox buttonsContainer = new VBox(20,statisticalBtn, readersBtn, bookManagementBtn, borrowReturnBooksBtn, returnBook, history, logOut);
        menuContainer.getChildren().add(buttonsContainer);
        leftMenu.getChildren().add(menuContainer);

        GridPane contentPane = new GridPane();
        contentPane.setPadding(new Insets(20));
        contentPane.setVgap(10);
        contentPane.setHgap(10);

        Label titleLabel = new Label("History");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        contentPane.add(titleLabel, 0, 0, 2, 1);

        Separator separator = new Separator();
        separator.setPrefWidth(190);
        contentPane.add(separator, 0, 1, 6, 1);

        TextField searchField = new TextField();
        searchField.setPromptText("Search by name...");

        HBox searchAndActions = new HBox(20, searchField);
        searchAndActions.setAlignment(Pos.CENTER_LEFT);

        contentPane.add(searchAndActions, 1, 2, 2, 1);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            String searchName = newValue.trim();
            ObservableList<HistoryEntity> searchResults = historyModel.findByNameContains(searchName); // Tìm reader theo tên (có thể chứa)
            tableView.setItems(searchResults);
        });

        tableView = new TableView<>();
        setupTableView();
        historyModel = new HistoryService();
        tableView.setItems(historyModel.getAll());

        tableView.setPrefHeight(400);
        tableView.setPrefWidth(800);
        contentPane.add(tableView, 0, 3, 5, 1);

        pagination = new Pagination((int) Math.ceil((double) historyModel.getAll().size() / ROWS_PER_PAGE), 0);
        pagination.setPageFactory(this::createPage);
        contentPane.add(pagination, 0, 4, 6, 1);

        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                HistoryDetalis violateDetails = new HistoryDetalis();
                Stage detailStage = new Stage();
                try {
                    violateDetails.start(detailStage);

                    detailStage.initModality(Modality.APPLICATION_MODAL);
                    detailStage.initOwner(primaryStage);

                    violateDetails.setReaderInfo(newValue.getReaderName(), newValue.getPhoneNumber(),
                            newValue.getEmail(), newValue.getReturnOnTime(),
                            newValue.getReturnLate(), newValue.getFineAmount());

                    detailStage.showAndWait();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        BorderPane root = new BorderPane();
        root.setLeft(leftMenu);
        root.setCenter(contentPane);

        Scene scene = new Scene(root, 1030, 620);
        primaryStage.setTitle("History");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void setupTableView(){
        tableView = new TableView<>();

        TableColumn<HistoryEntity, String> readerNameCol = new TableColumn<>("Name Reader");
        readerNameCol.setCellValueFactory(new PropertyValueFactory<>("readerName"));

        TableColumn<HistoryEntity, String> phoneNumberCol  = new TableColumn<>("Phone Number");
        phoneNumberCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        TableColumn<HistoryEntity, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<HistoryEntity, Integer> returnOnTimeCol = new TableColumn<>("Return On Time ");
        returnOnTimeCol.setCellValueFactory(new PropertyValueFactory<>("returnOnTime"));

        TableColumn<HistoryEntity, Integer> returnLateCol = new TableColumn<>("Return Late");
        returnLateCol.setCellValueFactory(new PropertyValueFactory<>("returnLate"));

        TableColumn<HistoryEntity, Double> fineAmountCol = new TableColumn<>("Fine Amount(vnđ)");
        fineAmountCol.setCellValueFactory(new PropertyValueFactory<>("fineAmount"));


        readerNameCol.setPrefWidth(135);
        phoneNumberCol.setPrefWidth(100);
        emailCol.setPrefWidth(190);
        returnOnTimeCol.setPrefWidth(120);
        returnLateCol.setPrefWidth(90);
        fineAmountCol.setPrefWidth(120);

        tableView.getColumns().addAll(readerNameCol, phoneNumberCol, emailCol, returnOnTimeCol, returnLateCol, fineAmountCol);

    }

    private VBox createPage(int pageIndex) {
        int fromIndex = pageIndex * ROWS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, historyModel.getAll().size());

        ObservableList<HistoryEntity> pageData = FXCollections.observableArrayList(historyModel.getAll().subList(fromIndex, toIndex));
        tableView.setItems(pageData);

        return new VBox(tableView);
    }

    private void refreshTable() {
        ObservableList<HistoryEntity> allReaders = historyModel.getAll();

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