package com.thuvien.dto;
import com.thuvien.entity.Customer;
import java.time.LocalDate;
public class CustomerDTO {
    private String id;
    private String fullName;
    private String phone;
    private String cccd;
    private LocalDate birthDate;
    private LocalDate joinDate;
    private LocalDate cardStartDate;
    private LocalDate cardExpireDate;
    private Customer.CardType cardType;
    private Customer.CardStatus cardStatus;
    private String address;
    private int totalBorrowed;
    private int totalReturned;
    private int currentBorrowing;
    private int overdueBooks;
    public String getId() { return id; }
    public void setId(String v) { this.id = v; }
    public String getFullName() { return fullName; }
    public void setFullName(String v) { this.fullName = v; }
    public String getPhone() { return phone; }
    public void setPhone(String v) { this.phone = v; }
    public String getCccd() { return cccd; }
    public void setCccd(String v) { this.cccd = v; }
    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate v) { this.birthDate = v; }
    public LocalDate getJoinDate() { return joinDate; }
    public void setJoinDate(LocalDate v) { this.joinDate = v; }
    public LocalDate getCardStartDate() { return cardStartDate; }
    public void setCardStartDate(LocalDate v) { this.cardStartDate = v; }
    public LocalDate getCardExpireDate() { return cardExpireDate; }
    public void setCardExpireDate(LocalDate v) { this.cardExpireDate = v; }
    public Customer.CardType getCardType() { return cardType; }
    public void setCardType(Customer.CardType v) { this.cardType = v; }
    public Customer.CardStatus getCardStatus() { return cardStatus; }
    public void setCardStatus(Customer.CardStatus v) { this.cardStatus = v; }
    public String getAddress() { return address; }
    public void setAddress(String v) { this.address = v; }
    public int getTotalBorrowed() { return totalBorrowed; }
    public void setTotalBorrowed(int v) { this.totalBorrowed = v; }
    public int getTotalReturned() { return totalReturned; }
    public void setTotalReturned(int v) { this.totalReturned = v; }
    public int getCurrentBorrowing() { return currentBorrowing; }
    public void setCurrentBorrowing(int v) { this.currentBorrowing = v; }
    public int getOverdueBooks() { return overdueBooks; }
    public void setOverdueBooks(int v) { this.overdueBooks = v; }
}
