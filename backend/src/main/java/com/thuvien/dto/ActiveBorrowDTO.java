package com.thuvien.dto;
import java.time.LocalDate;
public class ActiveBorrowDTO {
    private String bookTitle;
    private String customerName;
    private LocalDate dueDate;
    private boolean overdue;
    
    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String v) { this.bookTitle = v; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String v) { this.customerName = v; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate v) { this.dueDate = v; }
    public boolean isOverdue() { return overdue; }
    public void setOverdue(boolean v) { this.overdue = v; }
}
