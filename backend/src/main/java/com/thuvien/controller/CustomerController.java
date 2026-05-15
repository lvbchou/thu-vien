package com.thuvien.controller;

import org.springframework.beans.factory.annotation.Autowired;

import com.thuvien.dto.*;
import com.thuvien.service.CustomerService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

//quản lý thông tin cá nhân, các nghiệp vụ thư viện: mượn, trả, gia hạn sách.

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired

    private CustomerService customerService;

    // lấy danh sách khách hàng, hỗ trợ tìm kiếm, lọc theo loại thẻ và sắp xếp
    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAll(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String cardType,
            @RequestParam(required = false) String sort) {
        return ResponseEntity.ok(customerService.getAllCustomers(search, cardType, sort));
    }

    // xem chi tiết, thêm mới, sửa, xóa
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> create(@RequestBody CustomerRequest req) {
        return ResponseEntity.ok(customerService.createCustomer(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> update(@PathVariable String id, @RequestBody CustomerRequest req) {
        return ResponseEntity.ok(customerService.updateCustomer(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    // quản lý thẻ và trạng thái tài khoản: gia hạn thẻ, khóa/mở khóa tài khoản
    @PostMapping("/{id}/renew-card")
    public ResponseEntity<CustomerDTO> renewCard(@PathVariable String id, @RequestBody RenewCardRequest req) {
        return ResponseEntity.ok(customerService.renewCard(id, req.getCardType()));
    }

    @PostMapping("/{id}/ban")
    public ResponseEntity<CustomerDTO> ban(@PathVariable String id) {
        return ResponseEntity.ok(customerService.banCustomer(id));
    }

    @PostMapping("/{id}/unban")
    public ResponseEntity<CustomerDTO> unban(@PathVariable String id) {
        return ResponseEntity.ok(customerService.unbanCustomer(id));
    }

    // quản lý mượn trả sách: tạo mới phiếu mượn, trả sách, gia hạn mượn, xem lịch
    // sử mượn trả
    @PostMapping("/{id}/borrow")
    public ResponseEntity<BorrowResultDTO> borrow(@PathVariable String id, @RequestBody BorrowRequest req) {
        return ResponseEntity.ok(customerService.addBorrowRecords(id, req.getItems()));
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<ReturnResultDTO> returnBooks(@PathVariable String id, @RequestBody ReturnRequest req) {
        return ResponseEntity.ok(customerService.returnBooks(id, req.getRecordIds()));
    }

    // gia hạn mượn sách cụ thể
    @PostMapping("/extend")
    public ResponseEntity<Void> extend(@RequestBody ExtendRequest req) {
        customerService.extendBorrow(req.getRecordId(), req.getAdditionalDays());
        return ResponseEntity.noContent().build();
    }

    // lịch sử mượn trả của khách hàng cụ thể
    @GetMapping("/{id}/borrow-history")
    public ResponseEntity<List<CustomerBorrowHistoryDTO>> getBorrowHistory(
            @PathVariable String id,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate borrowFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate borrowTo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate returnFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate returnTo) {
        return ResponseEntity.ok(customerService.getBorrowHistory(id, borrowFrom, borrowTo, returnFrom, returnTo));
    }
}
