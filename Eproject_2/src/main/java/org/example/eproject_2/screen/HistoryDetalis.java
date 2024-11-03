package org.example.eproject_2.screen;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.eproject_2.entity.HistoryView;
import org.example.eproject_2.service.HistoryViewService;


public class HistoryDetalis extends Application {
    private TableView<HistoryView> tableView;
    private HistoryViewService historyViewModel;
    private Text nameReader;
    private Text phoneReader;
    private Text emailReader;
    private Text numberOnTimeReader;
    private Text numberOfLateReader;
    private Text totalFineReader;

    @Override
    public void start(Stage primaryStage) throws Exception {
        GridPane contentPane = new GridPane();
        contentPane.setPadding(new Insets(20));
        contentPane.setVgap(10);
        contentPane.setHgap(10);
        Label violationDetails = new Label("Borrow Information");
        violationDetails.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        contentPane.add(violationDetails, 0, 0, 2, 1);

        nameReader = new Text();
        contentPane.add(new Label("Name Reader:"), 0, 1);
        contentPane.add(nameReader, 1, 1);

        phoneReader = new Text();
        contentPane.add(new Label("Phone number:"), 0, 2);
        contentPane.add(phoneReader, 1, 2);
        phoneReader.setWrappingWidth(130);

        emailReader = new Text();
        contentPane.add(new Label("Email:"), 0, 3);
        contentPane.add(emailReader, 1, 3);

        numberOnTimeReader = new Text();
        contentPane.add(new Label("Number of On Time:"), 2, 2);
        contentPane.add(numberOnTimeReader, 3, 2);
        numberOnTimeReader.setWrappingWidth(100);

        numberOfLateReader = new Text();
        contentPane.add(new Label("Number of Late:"), 2, 3);
        contentPane.add(numberOfLateReader, 3, 3);

        totalFineReader = new Text();
        contentPane.add(new Label("Fine amount(vnđ):"), 4, 2);
        contentPane.add(totalFineReader, 5, 2);


        TextField searchField = new TextField();


        tableView = new TableView<>();
        setupTableView();

        historyViewModel = new HistoryViewService();
        String readerName = nameReader.getText();
        System.out.println("Reader Name from Text: " + readerName);
        tableView.setItems(historyViewModel.getAllView(readerName));

        searchField.setOnAction(event -> {
            System.out.println("Reader Name from Search Field: " + readerName);
            tableView.setItems(historyViewModel.getAllView(readerName));
        });

        tableView.setPrefHeight(300);
        contentPane.add(tableView, 0, 7, 6, 1);

        BorderPane root = new BorderPane();
        root.setCenter(contentPane);

        Scene scene = new Scene(root, 700, 500);
        primaryStage.setTitle("History details");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
    }

    private void setupTableView() {
        TableColumn<HistoryView, String> nameBookCol = new TableColumn<>("Book Name");
        nameBookCol.setCellValueFactory(new PropertyValueFactory<>("bookName"));

        TableColumn<HistoryView, String> borrowDateCol  = new TableColumn<>("Borrow date");
        borrowDateCol.setCellValueFactory(cellData -> {
            HistoryView view = cellData.getValue();
            return new ReadOnlyStringWrapper(view.formatDate(view.getBorrowDate()));
        });

        TableColumn<HistoryView, String> dueDateCol = new TableColumn<>("Due date");
        dueDateCol.setCellValueFactory(cellData -> {
            HistoryView view = cellData.getValue();
            return new ReadOnlyStringWrapper(view.formatDate(view.getDueDate()));
        });

        TableColumn<HistoryView, String> returnDateCol = new TableColumn<>("Return Date");
        returnDateCol.setCellValueFactory(cellData -> {
            HistoryView view = cellData.getValue();
            return new ReadOnlyStringWrapper(view.formatDate(view.getReturnDate()));
        });

        TableColumn<HistoryView, String> returnStatus = new TableColumn<>("Return Status");
        returnStatus.setCellValueFactory(new PropertyValueFactory<>("returnStatus"));

        nameBookCol.setPrefWidth(250);
        borrowDateCol.setPrefWidth(90);
        dueDateCol.setPrefWidth(90);
        returnDateCol.setPrefWidth(90);
        returnStatus.setPrefWidth(110);

        tableView.getColumns().addAll(nameBookCol, borrowDateCol, dueDateCol, returnDateCol, returnStatus);
    }

    public void setReaderInfo(String name, String phone, String email, int onTime, int late, double fine) {
        nameReader.setText(name);
        phoneReader.setText(phone);
        emailReader.setText(email);
        numberOnTimeReader.setText(String.valueOf(onTime));
        numberOfLateReader.setText(String.valueOf(late));
        totalFineReader.setText(String.valueOf(fine));

        tableView.setItems(historyViewModel.getAllView(name));

    }
}
