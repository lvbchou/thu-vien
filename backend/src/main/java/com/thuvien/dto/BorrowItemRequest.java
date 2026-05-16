package com.thuvien.dto;
public class BorrowItemRequest {
    private String bookId;
    private int days;
    public String getBookId() { return bookId; }
    
    public void setBookId(String v) { this.bookId = v; }
    public int getDays() { return days; }
    public void setDays(int v) { this.days = v; }
}
