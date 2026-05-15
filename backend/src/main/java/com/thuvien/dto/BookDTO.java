package com.thuvien.dto;
import com.thuvien.entity.Book;
import java.math.BigDecimal;
import java.time.LocalDate;
public class BookDTO {
    private String id;
    private String title;
    private String author;
    private String publisher;
    private String genre;
    private BigDecimal price;
    private String shelf;
    private LocalDate importDate;
    private LocalDate purchaseDate;
    private String externalCondition;
    private Book.BookStatus status;
    private String imageUrl;
    private int totalBorrows;
    private LocalDate lastReturnDate;
    public String getId() { return id; }
    public void setId(String v) { this.id = v; }
    public String getTitle() { return title; }
    public void setTitle(String v) { this.title = v; }
    public String getAuthor() { return author; }
    public void setAuthor(String v) { this.author = v; }
    public String getPublisher() { return publisher; }
    public void setPublisher(String v) { this.publisher = v; }
    public String getGenre() { return genre; }
    public void setGenre(String v) { this.genre = v; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal v) { this.price = v; }
    public String getShelf() { return shelf; }
    public void setShelf(String v) { this.shelf = v; }
    public LocalDate getImportDate() { return importDate; }
    public void setImportDate(LocalDate v) { this.importDate = v; }
    public LocalDate getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(LocalDate v) { this.purchaseDate = v; }
    public String getExternalCondition() { return externalCondition; }
    public void setExternalCondition(String v) { this.externalCondition = v; }
    public Book.BookStatus getStatus() { return status; }
    public void setStatus(Book.BookStatus v) { this.status = v; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String v) { this.imageUrl = v; }
    public int getTotalBorrows() { return totalBorrows; }
    public void setTotalBorrows(int v) { this.totalBorrows = v; }
    public LocalDate getLastReturnDate() { return lastReturnDate; }
    public void setLastReturnDate(LocalDate v) { this.lastReturnDate = v; }
}
