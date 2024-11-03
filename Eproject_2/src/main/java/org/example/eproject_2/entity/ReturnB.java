package org.example.eproject_2.entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ReturnB {
    private int returnBookId;
    private String readerName;
    private String bookName;
    private int quantity;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private String bookCondition;
    private String bookAuthor;
    private double bookPrice;
    private String bookCategory;
    private String phoneNumber;
    private String email;
    private LocalDate dob;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ReturnB() {
    }

    public ReturnB(int returnBookId, String readerName, String bookName, int quantity, LocalDate borrowDate,
                   LocalDate dueDate, LocalDate returnDate, String bookCondition, String bookAuthor,
                   double bookPrice, String bookCategory, String phoneNumber, String email, LocalDate dob) {
        this.returnBookId = returnBookId;
        this.readerName = readerName;
        this.bookName = bookName;
        this.quantity = quantity;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.bookCondition = bookCondition;
        this.bookAuthor = bookAuthor;
        this.bookPrice = bookPrice;
        this.bookCategory = bookCategory;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.dob = dob;
    }

    public int getReturnBookId() {
        return returnBookId;
    }

    public void setReturnBookId(int returnBookId) {
        this.returnBookId = returnBookId;
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

    public String getBookCondition() {
        return bookCondition;
    }

    public void setBookCondition(String bookCondition) {
        this.bookCondition = bookCondition;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public double getBookPrice() {
        return bookPrice;
    }

    public void setBookPrice(double bookPrice) {
        this.bookPrice = bookPrice;
    }

    public String getBookCategory() {
        return bookCategory;
    }

    public void setBookCategory(String bookCategory) {
        this.bookCategory = bookCategory;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String formatDate(LocalDate date) {
        if (date != null) {
            return date.format(DATE_FORMATTER);
        }
        return "";
    }

    @Override
    public String toString() {
        return "ReturnB{" +
                "returnBookId=" + returnBookId +
                ", readerName='" + readerName + '\'' +
                ", bookName='" + bookName + '\'' +
                ", quantity=" + quantity +
                ", borrowDate=" + formatDate(borrowDate) +
                ", dueDate=" + formatDate(dueDate) +
                ", returnDate=" + formatDate(returnDate) +
                ", bookCondition='" + bookCondition + '\'' +
                ", bookAuthor='" + bookAuthor + '\'' +
                ", bookPrice=" + bookPrice +
                ", bookCategory='" + bookCategory + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", dob=" + formatDate(dob) +
                '}';
    }
}
