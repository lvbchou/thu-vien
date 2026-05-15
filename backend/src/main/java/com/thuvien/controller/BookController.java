package com.thuvien.controller;
import org.springframework.beans.factory.annotation.Autowired;

import com.thuvien.dto.*;
import com.thuvien.service.BookService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired


    private BookService bookService;

    @GetMapping
    public ResponseEntity<List<BookDTO>> getAll(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String sort) {
        return ResponseEntity.ok(bookService.getAllBooks(search, genre, sort));
    }

    @GetMapping("/genres")
    public ResponseEntity<List<String>> getGenres() {
        return ResponseEntity.ok(bookService.getAllGenres());
    }

    @GetMapping("/search-available")
    public ResponseEntity<List<BookDTO>> searchAvailable(@RequestParam String keyword) {
        return ResponseEntity.ok(bookService.searchAvailableBooks(keyword));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @PostMapping
    public ResponseEntity<BookDTO> create(@RequestBody BookRequest req) {
        return ResponseEntity.ok(bookService.createBook(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> update(@PathVariable String id, @RequestBody BookRequest req) {
        return ResponseEntity.ok(bookService.updateBook(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/batch")
    public ResponseEntity<Void> deleteBatch(@RequestBody DeleteBooksRequest req) {
        bookService.deleteBooks(req.getIds());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/borrow-history")
    public ResponseEntity<List<BookBorrowHistoryDTO>> getBorrowHistory(
            @PathVariable String id,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate borrowFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate borrowTo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate returnFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate returnTo) {
        return ResponseEntity.ok(bookService.getBorrowHistory(id, borrowFrom, borrowTo, returnFrom, returnTo));
    }
}
