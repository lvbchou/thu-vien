package com.thuvien.service;
import org.springframework.beans.factory.annotation.Autowired;

import com.thuvien.dto.DashboardDTO;
import com.thuvien.entity.BorrowRecord;
import com.thuvien.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class DashboardService {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private BorrowRecordRepository borrowRecordRepository;

    @Transactional(readOnly = true)
    public DashboardDTO getDashboard() {
        DashboardDTO dto = new DashboardDTO();
        dto.setTotalBooks(bookRepository.count());
        dto.setActiveBorrows(borrowRecordRepository.countTotalActiveBorrows());
        dto.setTotalCustomers(customerRepository.count());

        List<BorrowRecord> overdue = borrowRecordRepository.findOverdueRecords(LocalDate.now());
        dto.setOverdueCount(overdue.size());

        return dto;
    }
}