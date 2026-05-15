package com.thuvien.dto;
import com.thuvien.entity.Customer;
import java.time.LocalDate;
public class CustomerRequest {
    private String id;
    private String fullName;
    private String phone;
    private String cccd;
    private LocalDate birthDate;
    private String address;
    private Customer.CardType cardType;
    private LocalDate cardStartDate;
    private LocalDate cardExpireDate;
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
    public String getAddress() { return address; }
    public void setAddress(String v) { this.address = v; }
    public Customer.CardType getCardType() { return cardType; }
    public void setCardType(Customer.CardType v) { this.cardType = v; }
    public LocalDate getCardStartDate() { return cardStartDate; }
    public void setCardStartDate(LocalDate v) { this.cardStartDate = v; }
    public LocalDate getCardExpireDate() { return cardExpireDate; }
    public void setCardExpireDate(LocalDate v) { this.cardExpireDate = v; }
}
