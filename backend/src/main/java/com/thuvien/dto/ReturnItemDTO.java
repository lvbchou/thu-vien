package com.thuvien.dto;
import java.math.BigDecimal;
public class ReturnItemDTO {
    private Long recordId;
    private String bookId;
    private String bookTitle;
    private int overdueDays;
    private BigDecimal fine;
    private BigDecimal deposit;
    public Long getRecordId() { return recordId; }
    public void setRecordId(Long v) { this.recordId = v; }
    public String getBookId() { return bookId; }
    public void setBookId(String v) { this.bookId = v; }
    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String v) { this.bookTitle = v; }
    public int getOverdueDays() { return overdueDays; }
    public void setOverdueDays(int v) { this.overdueDays = v; }
    public BigDecimal getFine() { return fine; }
    public void setFine(BigDecimal v) { this.fine = v; }
    public BigDecimal getDeposit() { return deposit; }
    public void setDeposit(BigDecimal v) { this.deposit = v; }
}
