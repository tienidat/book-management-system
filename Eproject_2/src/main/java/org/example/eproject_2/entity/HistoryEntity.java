package org.example.eproject_2.entity;

public class HistoryEntity {
    private int violationId;
    private String readerName;
    private String phoneNumber;
    private String email;
    private int returnOnTime;
    private int returnLate;
    private Double fineAmount;
    private String paymentStatus;

    public HistoryEntity() {
    }

    public HistoryEntity(int violationId, String readerName, String phoneNumber, String email, int returnOnTime,
                         int returnLate, Double fineAmount, String paymentStatus) {
        this.violationId = violationId;
        this.readerName = readerName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.returnOnTime = returnOnTime;
        this.returnLate = returnLate;
        this.fineAmount = fineAmount;
        this.paymentStatus = paymentStatus;
    }
    private int violationCount;

    public int getViolationCount() {
        return violationCount;
    }

    public void setViolationCount(int violationCount) {
        this.violationCount = violationCount;
    }

    public int getViolationId() {
        return violationId;
    }

    public void setViolationId(int violationId) {
        this.violationId = violationId;
    }

    public String getReaderName() {
        return readerName;
    }

    public void setReaderName(String readerName) {
        this.readerName = readerName;
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

    public int getReturnOnTime() {
        return returnOnTime;
    }

    public void setReturnOnTime(int returnOnTime) {
        this.returnOnTime = returnOnTime;
    }

    public int getReturnLate() {
        return returnLate;
    }

    public void setReturnLate(int returnLate) {
        this.returnLate = returnLate;
    }

    public Double getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(Double fineAmount) {
        this.fineAmount = fineAmount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    @Override
    public String toString() {
        return "ViolationEntity{" +
                "violationId=" + violationId +
                ", readerName='" + readerName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", returnOnTime=" + returnOnTime +
                ", returnLate=" + returnLate +
                ", fineAmount=" + fineAmount +
                ", paymentStatus='" + paymentStatus + '\'' +
                '}';
    }
}
