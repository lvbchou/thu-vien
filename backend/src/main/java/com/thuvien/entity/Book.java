package com.thuvien.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @Column(nullable = false, unique = true)
    private String id;

    @Column(nullable = false)
    private String title;

    private String author;
    private String publisher;
    private String genre;

    @Column(precision = 12, scale = 0)
    private BigDecimal price;

    private String shelf;
    private LocalDate importDate;
    private LocalDate purchaseDate;

    @Column(columnDefinition = "TEXT")
    private String externalCondition;

    @Enumerated(EnumType.STRING)
    private BookStatus status = BookStatus.AVAILABLE;

    private String imageUrl;
    private int totalBorrows = 0;
    private LocalDate lastReturnDate;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BorrowRecord> borrowRecords;

    public enum BookStatus { AVAILABLE, BORROWED, MAINTENANCE, LOST }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public String getShelf() { return shelf; }
    public void setShelf(String shelf) { this.shelf = shelf; }
    public LocalDate getImportDate() { return importDate; }
    public void setImportDate(LocalDate importDate) { this.importDate = importDate; }
    public LocalDate getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(LocalDate purchaseDate) { this.purchaseDate = purchaseDate; }
    public String getExternalCondition() { return externalCondition; }
    public void setExternalCondition(String v) { this.externalCondition = v; }
    public BookStatus getStatus() { return status; }
    public void setStatus(BookStatus status) { this.status = status; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public int getTotalBorrows() { return totalBorrows; }
    public void setTotalBorrows(int totalBorrows) { this.totalBorrows = totalBorrows; }
    public LocalDate getLastReturnDate() { return lastReturnDate; }
    public void setLastReturnDate(LocalDate v) { this.lastReturnDate = v; }
    public List<BorrowRecord> getBorrowRecords() { return borrowRecords; }
    public void setBorrowRecords(List<BorrowRecord> v) { this.borrowRecords = v; }
}
