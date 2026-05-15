package com.thuvien.dto;
public class CustomerSummaryDTO {
    private String customerId;
    private String customerName;
    private int borrowing;
    private int returned;
    private int overdue;
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String v) { this.customerId = v; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String v) { this.customerName = v; }
    public int getBorrowing() { return borrowing; }
    public void setBorrowing(int v) { this.borrowing = v; }
    public int getReturned() { return returned; }
    public void setReturned(int v) { this.returned = v; }
    public int getOverdue() { return overdue; }
    public void setOverdue(int v) { this.overdue = v; }
}
