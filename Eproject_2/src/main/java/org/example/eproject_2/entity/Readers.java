package org.example.eproject_2.entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Readers {
    private int readerId;
    private String readerName;
    private String gender;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;

    // Định dạng ngày sinh thành DD/MM/YYYY
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public Readers() {
    }

    public Readers(int readerId, String readerName, String email, String phoneNumber,
                   String gender, LocalDate dateOfBirth) {
        this.readerId = readerId;
        this.readerName = readerName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
    }

    // Getter và Setter các thuộc tính
    public int getReaderId() {
        return readerId;
    }

    public void setReaderId(int readerId) {
        this.readerId = readerId;
    }

    public String getReaderName() {
        return readerName;
    }

    public void setReaderName(String readerName) {
        this.readerName = readerName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    // Phương thức trả về ngày sinh đã định dạng
    public String getDateOfBirthFormatted() {
        return dateOfBirth != null ? dateOfBirth.format(DATE_FORMATTER) : null;
    }

    public boolean isMale() {
        return "Male".equalsIgnoreCase(gender);
    }

    public boolean isFemale() {
        return "Female".equalsIgnoreCase(gender);
    }

    @Override
    public String toString() {
        return "Readers{" +
                "readerId=" + readerId +
                ", readerName='" + readerName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", gender='" + gender + '\'' +
                ", dateOfBirth=" + getDateOfBirthFormatted() +
                '}';
    }
}
