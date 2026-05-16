package com.thuvien.dto;
import java.math.BigDecimal;
import java.time.LocalDate;
public class BookBorrowHistoryDTO {
    private Long recordId;
    private String customerId;
    private String customerName;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private int overdueDays;
    private int totalDays;
    private BigDecimal deposit;
    private BigDecimal fine;
    
    public Long getRecordId() { return recordId; }
    public void setRecordId(Long v) { this.recordId = v; }
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String v) { this.customerId = v; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String v) { this.customerName = v; }
    public LocalDate getBorrowDate() { return borrowDate; }
    public void setBorrowDate(LocalDate v) { this.borrowDate = v; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate v) { this.dueDate = v; }
    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate v) { this.returnDate = v; }
    public int getOverdueDays() { return overdueDays; }
    public void setOverdueDays(int v) { this.overdueDays = v; }
    public int getTotalDays() { return totalDays; }
    public void setTotalDays(int v) { this.totalDays = v; }
    public BigDecimal getDeposit() { return deposit; }
    public void setDeposit(BigDecimal v) { this.deposit = v; }
    public BigDecimal getFine() { return fine; }
    public void setFine(BigDecimal v) { this.fine = v; }
}
