package org.example.eproject_2.entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Borrow {
    private int borrowId;
    private String readerName;
    private String bookName;
    private int quantity;
    private LocalDate borrowDate;
    private String author;
    private LocalDate dueDate;
    private String category;
    private String condition;
    private LocalDate returnDate;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");


    public Borrow() {
    }

    public Borrow(int borrowId, String readerName, String bookName, int quantity, LocalDate borrowDate, String author,
                  LocalDate dueDate, String category, String condition, LocalDate returnDate) {
        this.borrowId = borrowId;
        this.readerName = readerName;
        this.bookName = bookName;
        this.quantity = quantity;
        this.borrowDate = borrowDate;
        this.author = author;
        this.dueDate = dueDate;
        this.category = category;
        this.condition = condition;
        this.returnDate = returnDate;
    }

    public int getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(int borrowId) {
        this.borrowId = borrowId;
    }

    public String getReaderName() {
        return readerName;
    }

    public void setReaderName(String readerName) {
        this.readerName = readerName;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCondition(String notReturned) {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public String formatDate(LocalDate date) {
        if (date != null) {
            return date.format(DATE_FORMATTER);
        }
        return "";
    }

    @Override
    public String toString() {
        return "Borrow{" +
                "borrowId=" + borrowId +
                ", readerName='" + readerName + '\'' +
                ", bookName='" + bookName + '\'' +
                ", quantity=" + quantity +
                ", borrowDate=" + formatDate(borrowDate) + // Định dạng ngày
                ", author='" + author + '\'' +
                ", dueDate=" + formatDate(dueDate) + // Định dạng ngày
                ", category='" + category + '\'' +
                ", condition='" + condition + '\'' +
                ", returnDate=" + formatDate(returnDate) + // Định dạng ngày
                '}';
    }
}
