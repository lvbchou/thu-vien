package com.thuvien.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.thuvien.dto.*;
import com.thuvien.entity.Book;
import com.thuvien.entity.BorrowRecord;
import com.thuvien.repository.BookRepository;
import com.thuvien.repository.BorrowRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookService {
    @Autowired

    private BookRepository bookRepository;
    @Autowired

    private BorrowRecordRepository borrowRecordRepository;

    public List<BookDTO> getAllBooks(String search, String genre, String sort) {
        List<Book> books;
        if (search != null && !search.isBlank()) {
            books = bookRepository.findByTitleContainingIgnoreCaseOrIdContainingIgnoreCase(search, search);
        } else if (genre != null && !genre.isBlank() && !genre.equals("all")) {
            books = bookRepository.findByGenreIgnoreCase(genre);
        } else {
            books = bookRepository.findAll();
        }

        List<BookDTO> dtos = books.stream().map(this::toDTO).collect(Collectors.toList());

        if ("borrows_asc".equals(sort)) {
            dtos.sort(Comparator.comparing(BookDTO::getId));
        } else if ("borrows_desc".equals(sort)) {
            dtos.sort(Comparator.comparingInt(BookDTO::getTotalBorrows).reversed());
        } else if ("importDate_asc".equals(sort)) {
            dtos.sort(Comparator.comparing(BookDTO::getImportDate, Comparator.nullsLast(Comparator.naturalOrder())));
        } else if ("importDate_desc".equals(sort)) {
            dtos.sort(Comparator.comparing(BookDTO::getImportDate, Comparator.nullsLast(Comparator.reverseOrder())));
        } else if ("id_asc".equals(sort)) {
            dtos.sort(Comparator.comparing(BookDTO::getId));
        } else if ("id_desc".equals(sort)) {
            dtos.sort(Comparator.comparing(BookDTO::getId).reversed());
        } else {
            dtos.sort(Comparator.comparing(BookDTO::getId).reversed());
        }
        return dtos;
    }

    public BookDTO getBookById(String id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
        return toDTO(book);
    }

    public List<String> getAllGenres() {
        return bookRepository.findAllGenres();
    }

    // thêm mới
    @Transactional
    public BookDTO createBook(BookRequest req) {
        String newId = generateBookId();
        Book book = new Book();
        book.setId(newId);
        mapToBook(book, req);
        book.setId(newId);
        return toDTO(bookRepository.save(book));
    }

    private String generateBookId() {
        List<Book> all = bookRepository.findAll();
        int max = all.stream()
                .map(Book::getId)
                .filter(id -> id != null && id.matches("S\\d+"))
                .mapToInt(id -> Integer.parseInt(id.substring(1)))
                .max().orElse(0);
        return String.format("S%03d", max + 1);
    }

    // sửa
    @Transactional
    public BookDTO updateBook(String id, BookRequest req) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
        mapToBook(book, req);
        return toDTO(bookRepository.save(book));
    }

    // xoá
    @Transactional
    public void deleteBook(String id) {
        bookRepository.deleteById(id);
    }

    // xoá nhiều
    @Transactional
    public void deleteBooks(List<String> ids) {
        bookRepository.deleteAllById(ids);
    }

    public List<BookBorrowHistoryDTO> getBorrowHistory(String bookId, LocalDate borrowFrom,
            LocalDate borrowTo, LocalDate returnFrom, LocalDate returnTo) {
        List<BorrowRecord> records = borrowRecordRepository.findByBookId(bookId);
        return records.stream()
                .filter(r -> {
                    if (borrowFrom != null && r.getBorrowDate().isBefore(borrowFrom))
                        return false;
                    if (borrowTo != null && r.getBorrowDate().isAfter(borrowTo))
                        return false;
                    if (returnFrom != null && (r.getReturnDate() == null || r.getReturnDate().isBefore(returnFrom)))
                        return false;
                    if (returnTo != null && (r.getReturnDate() == null || r.getReturnDate().isAfter(returnTo)))
                        return false;
                    return true;
                })
                .map(r -> {
                    BookBorrowHistoryDTO dto = new BookBorrowHistoryDTO();
                    dto.setRecordId(r.getId());
                    dto.setCustomerId(r.getCustomer().getId());
                    dto.setCustomerName(r.getCustomer().getFullName());
                    dto.setBorrowDate(r.getBorrowDate());
                    dto.setDueDate(r.getDueDate());
                    dto.setReturnDate(r.getReturnDate());
                    dto.setOverdueDays(r.getOverdueDays());
                    int totalDays = (int) (r.getDueDate().toEpochDay() - r.getBorrowDate().toEpochDay());
                    dto.setTotalDays(totalDays);
                    dto.setDeposit(r.getDeposit());
                    dto.setFine(r.getFine());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<BookDTO> searchAvailableBooks(String keyword) {
        List<Book> books = bookRepository.findByTitleContainingIgnoreCaseOrIdContainingIgnoreCase(keyword, keyword);
        return books.stream()
                .filter(b -> b.getStatus() == Book.BookStatus.AVAILABLE)
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private void mapToBook(Book book, BookRequest req) {
        book.setTitle(req.getTitle());
        book.setAuthor(req.getAuthor());
        book.setPublisher(req.getPublisher());
        book.setGenre(req.getGenre());
        book.setPrice(req.getPrice());
        book.setShelf(req.getShelf());
        book.setImportDate(req.getImportDate());
        book.setPurchaseDate(req.getPurchaseDate() != null ? req.getPurchaseDate() : req.getImportDate());
        book.setExternalCondition(req.getExternalCondition());
        book.setStatus(req.getStatus() != null ? req.getStatus() : Book.BookStatus.AVAILABLE);
        book.setImageUrl(req.getImageUrl());
    }

    private BookDTO toDTO(Book book) {
        BookDTO dto = new BookDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setPublisher(book.getPublisher());
        dto.setGenre(book.getGenre());
        dto.setPrice(book.getPrice());
        dto.setShelf(book.getShelf());
        dto.setImportDate(book.getImportDate());
        dto.setPurchaseDate(book.getPurchaseDate());
        dto.setExternalCondition(book.getExternalCondition());
        dto.setStatus(book.getStatus());
        dto.setImageUrl(book.getImageUrl());
        dto.setTotalBorrows(book.getTotalBorrows());
        dto.setLastReturnDate(book.getLastReturnDate());
        return dto;
    }
}
