package com.thuvien.dto;
import com.thuvien.entity.BorrowRecord;
import java.math.BigDecimal;
import java.time.LocalDate;
public class CustomerBorrowHistoryDTO {
    private Long recordId;
    private String bookId;
    private String bookTitle;
    private String author;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private int overdueDays;
    private BorrowRecord.BorrowStatus status;
    private BigDecimal deposit;
    private BigDecimal fine;
    
    public Long getRecordId() { return recordId; }
    public void setRecordId(Long v) { this.recordId = v; }
    public String getBookId() { return bookId; }
    public void setBookId(String v) { this.bookId = v; }
    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String v) { this.bookTitle = v; }
    public String getAuthor() { return author; }
    public void setAuthor(String v) { this.author = v; }
    public LocalDate getBorrowDate() { return borrowDate; }
    public void setBorrowDate(LocalDate v) { this.borrowDate = v; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate v) { this.dueDate = v; }
    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate v) { this.returnDate = v; }
    public int getOverdueDays() { return overdueDays; }
    public void setOverdueDays(int v) { this.overdueDays = v; }
    public BorrowRecord.BorrowStatus getStatus() { return status; }
    public void setStatus(BorrowRecord.BorrowStatus v) { this.status = v; }
    public BigDecimal getDeposit() { return deposit; }
    public void setDeposit(BigDecimal v) { this.deposit = v; }
    public BigDecimal getFine() { return fine; }
    public void setFine(BigDecimal v) { this.fine = v; }
}
