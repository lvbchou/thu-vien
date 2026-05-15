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

    // lấy danh sách Sách, tìm kiếm, lọc theo thể loại và sắp xếp
    @GetMapping
    public ResponseEntity<List<BookDTO>> getAll(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String sort) {
        return ResponseEntity.ok(bookService.getAllBooks(search, genre, sort));
    }

    // lấy danh sách các thể loại sách để Frontend render vào thẻ <select> hoặc
    // dropdown list.
    @GetMapping("/genres")
    public ResponseEntity<List<String>> getGenres() {
        return ResponseEntity.ok(bookService.getAllGenres());
    }

    // tìm kiếm riêng cho những sách còn trong kho để cho mượn
    @GetMapping("/search-available")
    public ResponseEntity<List<BookDTO>> searchAvailable(@RequestParam String keyword) {
        return ResponseEntity.ok(bookService.searchAvailableBooks(keyword));
    }

    // crud: lấy chi tiết, thêm mới, Cập nhật 1 cuốn sách.
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

    // xóa hàng loạt sách
    @DeleteMapping("/batch")
    public ResponseEntity<Void> deleteBatch(@RequestBody DeleteBooksRequest req) {
        bookService.deleteBooks(req.getIds());
        return ResponseEntity.noContent().build();
    }

    // xem lịch sử mượn trả của 1 cuốn sách, lọc theo thời gian.
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
