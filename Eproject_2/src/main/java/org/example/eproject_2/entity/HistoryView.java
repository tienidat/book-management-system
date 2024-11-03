package org.example.eproject_2.entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class HistoryView {

    private String bookName;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private String returnStatus;
    private double fineAmount;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public HistoryView() {
    }

    public HistoryView(String bookName, LocalDate borrowDate, LocalDate dueDate, LocalDate returnDate,
                       String returnStatus, double fineAmount) {
        this.bookName = bookName;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.returnStatus = returnStatus;
        this.fineAmount = fineAmount;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public String getReturnStatus() {
        return returnStatus;
    }

    public void setReturnStatus(String returnStatus) {
        this.returnStatus = returnStatus;
    }

    public double getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(double fineAmount) {
        this.fineAmount = fineAmount;
    }

    public String formatDate(LocalDate date) {
        if (date != null) {
            return date.format(DATE_FORMATTER);
        }
        return "";
    }

    @Override
    public String toString() {
        return "ViolationView{" +
                "bookReaders='" + bookName + '\'' +
                ", borrowDate=" + formatDate(borrowDate) +
                ", dueDate=" + formatDate(dueDate) +
                ", returnDate=" + formatDate(returnDate) +
                ", returnStatus='" + returnStatus + '\'' +
                ", fineAmount=" + fineAmount +
                '}';
    }
}
