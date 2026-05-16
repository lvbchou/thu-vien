package com.thuvien.service;
import org.springframework.beans.factory.annotation.Autowired;

import com.thuvien.dto.*;
import com.thuvien.entity.*;
import com.thuvien.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    @Autowired


    private CustomerRepository customerRepository;
    @Autowired

    private BorrowRecordRepository borrowRecordRepository;
    @Autowired

    private BookRepository bookRepository;

    private static final int MAX_BORROW = 5;

    @Transactional(readOnly = true)
    public List<CustomerDTO> getAllCustomers(String search, String cardType, String sort) {
        List<Customer> customers;
        if (search != null && !search.isBlank()) {
            customers = customerRepository.findByFullNameContainingIgnoreCaseOrIdContainingIgnoreCaseOrPhoneContainingOrCccdContaining(
                search, search, search, search);
        } else if (cardType != null && !cardType.isBlank() && !cardType.equals("all")) {
            if (cardType.equals("BANNED") || cardType.equals("EXPIRED")) {
                try {
                    customers = customerRepository.findByCardStatus(Customer.CardStatus.valueOf(cardType));
                } catch (Exception e) {
                    customers = customerRepository.findAll();
                }
            } else {
                try {
                    customers = customerRepository.findByCardType(Customer.CardType.valueOf(cardType));
                } catch (Exception e) {
                    customers = customerRepository.findAll();
                }
            }
        } else {
            customers = customerRepository.findAll();
        }

        // cập nhật trạng thái
        customers.forEach(c -> updateCardStatus(c));

        List<CustomerDTO> dtos = customers.stream().map(this::toDTO).collect(Collectors.toList());

        if (sort != null) {
            switch (sort) {
                case "id_asc": dtos.sort(Comparator.comparing(CustomerDTO::getId)); break;
                case "id_desc": dtos.sort(Comparator.comparing(CustomerDTO::getId).reversed()); break;
                case "birthDate_asc": dtos.sort(Comparator.comparing(CustomerDTO::getBirthDate, Comparator.nullsLast(Comparator.naturalOrder()))); break;
                case "birthDate_desc": dtos.sort(Comparator.comparing(CustomerDTO::getBirthDate, Comparator.nullsLast(Comparator.reverseOrder()))); break;
                case "joinDate_asc": dtos.sort(Comparator.comparing(CustomerDTO::getJoinDate, Comparator.nullsLast(Comparator.naturalOrder()))); break;
                case "joinDate_desc": dtos.sort(Comparator.comparing(CustomerDTO::getJoinDate, Comparator.nullsLast(Comparator.reverseOrder()))); break;
                case "borrowed_asc": dtos.sort(Comparator.comparingInt(CustomerDTO::getTotalBorrowed)); break;
                case "borrowed_desc": dtos.sort(Comparator.comparingInt(CustomerDTO::getTotalBorrowed).reversed()); break;
                case "borrowing_asc": dtos.sort(Comparator.comparingInt(CustomerDTO::getCurrentBorrowing)); break;
                case "borrowing_desc": dtos.sort(Comparator.comparingInt(CustomerDTO::getCurrentBorrowing).reversed()); break;
                case "overdue_asc": dtos.sort(Comparator.comparingInt(CustomerDTO::getOverdueBooks)); break;
                case "overdue_desc": dtos.sort(Comparator.comparingInt(CustomerDTO::getOverdueBooks).reversed()); break;
            }
        }
        return dtos;
    }

    @Transactional(readOnly = true)
    public CustomerDTO getCustomerById(String id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
        updateCardStatus(customer);
        return toDTO(customer);
    }

    @Transactional
    public CustomerDTO createCustomer(CustomerRequest req) {
        String newId = generateCustomerId();
        Customer customer = new Customer();
        customer.setId(newId);
        mapToCustomer(customer, req);
        customer.setId(newId); // ensure not overwritten
        customer.setJoinDate(LocalDate.now());
        return toDTO(customerRepository.save(customer));
    }

    private String generateCustomerId() {
        List<Customer> all = customerRepository.findAll();
        int max = all.stream()
            .map(Customer::getId)
            .filter(id -> id != null && id.matches("KH\\d+"))
            .mapToInt(id -> Integer.parseInt(id.substring(2)))
            .max().orElse(0);
        return String.format("KH%03d", max + 1);
    }

    @Transactional
    public CustomerDTO updateCustomer(String id, CustomerRequest req) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
        mapToCustomer(customer, req);
        return toDTO(customerRepository.save(customer));
    }

    @Transactional
    public void deleteCustomer(String id) {
        customerRepository.deleteById(id);
    }

    @Transactional
    public CustomerDTO renewCard(String id, String cardType) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));

        LocalDate currentExpiry = customer.getCardExpireDate();
        LocalDate startDate = (currentExpiry != null && currentExpiry.isAfter(LocalDate.now()))
                ? currentExpiry.plusDays(1) : LocalDate.now();

        Customer.CardType type = Customer.CardType.valueOf(cardType);
        LocalDate newExpiry = type == Customer.CardType.MONTHLY ? startDate.plusMonths(1) : startDate.plusYears(1);

        customer.setCardType(type);
        customer.setCardExpireDate(newExpiry);
        customer.setCardStatus(Customer.CardStatus.ACTIVE);
        return toDTO(customerRepository.save(customer));
    }

    @Transactional
    public CustomerDTO banCustomer(String id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
        customer.setCardStatus(Customer.CardStatus.BANNED);
        return toDTO(customerRepository.save(customer));
    }

    @Transactional
    public CustomerDTO unbanCustomer(String id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
        // Restore to ACTIVE or EXPIRED based on card expiry date
        if (customer.getCardExpireDate() != null && customer.getCardExpireDate().isBefore(java.time.LocalDate.now())) {
            customer.setCardStatus(Customer.CardStatus.EXPIRED);
        } else {
            customer.setCardStatus(Customer.CardStatus.ACTIVE);
        }
        return toDTO(customerRepository.save(customer));
    }

    @Transactional
    public BorrowResultDTO addBorrowRecords(String customerId, List<BorrowItemRequest> items) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new RuntimeException("Customer not found"));

        // Check card status
        updateCardStatus(customer);
        if (customer.getCardStatus() == Customer.CardStatus.BANNED) throw new RuntimeException("Khách hàng bị cấm mượn sách");
        if (customer.getCardStatus() == Customer.CardStatus.EXPIRED) throw new RuntimeException("Thẻ khách hàng đã hết hạn");

        long currentBorrowing = borrowRecordRepository.countActiveBorrowsByCustomer(customerId);
        if (currentBorrowing + items.size() > MAX_BORROW) {
            throw new RuntimeException("Khách hàng không thể mượn quá " + MAX_BORROW + " sách");
        }

        List<BorrowRecord> records = new ArrayList<>();
        for (BorrowItemRequest item : items) {
            Book book = bookRepository.findById(item.getBookId()).orElseThrow(() -> new RuntimeException("Không tìm thấy sách: " + item.getBookId()));
            if (book.getStatus() != Book.BookStatus.AVAILABLE) throw new RuntimeException("Sách '" + book.getTitle() + "' không có sẵn");

            BorrowRecord record = new BorrowRecord();
            record.setBook(book);
            record.setCustomer(customer);
            record.setBorrowDate(LocalDate.now());
            record.setDueDate(LocalDate.now().plusDays(item.getDays()));

            // Calculate deposit: 50% if within 2 years, 30% if older
            LocalDate twoYearsAgo = LocalDate.now().minusYears(2);
            BigDecimal depositRate = (book.getPurchaseDate() != null && book.getPurchaseDate().isAfter(twoYearsAgo))
                    ? new BigDecimal("0.5") : new BigDecimal("0.3");
            record.setDeposit(book.getPrice().multiply(depositRate));

            book.setStatus(Book.BookStatus.BORROWED);
            book.setTotalBorrows(book.getTotalBorrows() + 1);
            bookRepository.save(book);

            records.add(borrowRecordRepository.save(record));
        }

        BorrowResultDTO result = new BorrowResultDTO();
        result.setCustomerId(customerId);
        result.setRecordIds(records.stream().map(BorrowRecord::getId).collect(Collectors.toList()));
        return result;
    }

    @Transactional
    public ReturnResultDTO returnBooks(String customerId, List<Long> recordIds) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new RuntimeException("Customer not found"));
        LocalDate today = LocalDate.now();

        List<ReturnItemDTO> returnItems = new ArrayList<>();
        BigDecimal totalFine = BigDecimal.ZERO;
        BigDecimal totalDeposit = BigDecimal.ZERO;

        for (Long recordId : recordIds) {
            BorrowRecord record = borrowRecordRepository.findById(recordId).orElseThrow(() -> new RuntimeException("Borrow record not found"));
            Book book = record.getBook();

            int overdueDays = (int) Math.max(0, ChronoUnit.DAYS.between(record.getDueDate(), today));
            record.setOverdueDays(overdueDays);
            record.setReturnDate(today);
            record.setStatus(overdueDays > 0 ? BorrowRecord.BorrowStatus.OVERDUE : BorrowRecord.BorrowStatus.RETURNED);

            // Calculate fine
            BigDecimal fine = calculateFine(book, overdueDays);
            record.setFine(fine);

            // Auto ban if overdue > 3 days
            if (overdueDays > 3) {
                customer.setCardStatus(Customer.CardStatus.BANNED);
            }

            book.setStatus(Book.BookStatus.AVAILABLE);
            book.setLastReturnDate(today);
            bookRepository.save(book);
            borrowRecordRepository.save(record);

            ReturnItemDTO item = new ReturnItemDTO();
            item.setRecordId(recordId);
            item.setBookId(book.getId());
            item.setBookTitle(book.getTitle());
            item.setOverdueDays(overdueDays);
            item.setFine(fine);
            item.setDeposit(record.getDeposit());
            returnItems.add(item);

            totalFine = totalFine.add(fine);
            totalDeposit = totalDeposit.add(record.getDeposit());
        }

        customerRepository.save(customer);

        ReturnResultDTO result = new ReturnResultDTO();
        result.setItems(returnItems);
        result.setTotalFine(totalFine);
        result.setTotalDeposit(totalDeposit);
        // Net: positive = customer pays more, negative = we refund
        result.setNetAmount(totalFine.subtract(totalDeposit));
        return result;
    }

    private BigDecimal calculateFine(Book book, int overdueDays) {
        if (overdueDays <= 0) return BigDecimal.ZERO;
        BigDecimal price = book.getPrice();

        if (price.compareTo(new BigDecimal("50000")) < 0) {
            // Sách dưới 50k: 8k/1 ngày, 15k/2 ngày, 30k/3+ ngày
            if (overdueDays == 1) return new BigDecimal("8000");
            if (overdueDays == 2) return new BigDecimal("15000");
            return new BigDecimal("30000");
        } else {
            // Sách 50k+: 10%/1 ngày, 20%/2 ngày, 50%/3 ngày, 100% sau 3 ngày
            if (overdueDays == 1) return price.multiply(new BigDecimal("0.1"));
            if (overdueDays == 2) return price.multiply(new BigDecimal("0.2"));
            if (overdueDays == 3) return price.multiply(new BigDecimal("0.5"));
            return price; // full price after 3 days
        }
    }

    @Transactional
    public void extendBorrow(Long recordId, int additionalDays) {
        BorrowRecord record = borrowRecordRepository.findById(recordId).orElseThrow();
        record.setDueDate(record.getDueDate().plusDays(additionalDays));
        borrowRecordRepository.save(record);
    }

    public List<CustomerBorrowHistoryDTO> getBorrowHistory(String customerId,
                                                            LocalDate borrowFrom, LocalDate borrowTo,
                                                            LocalDate returnFrom, LocalDate returnTo) {
        List<BorrowRecord> records = borrowRecordRepository.findByCustomerId(customerId);
        return records.stream()
                .filter(r -> {
                    if (borrowFrom != null && r.getBorrowDate().isBefore(borrowFrom)) return false;
                    if (borrowTo != null && r.getBorrowDate().isAfter(borrowTo)) return false;
                    if (returnFrom != null && (r.getReturnDate() == null || r.getReturnDate().isBefore(returnFrom))) return false;
                    if (returnTo != null && (r.getReturnDate() == null || r.getReturnDate().isAfter(returnTo))) return false;
                    return true;
                })
                .map(r -> {
                    CustomerBorrowHistoryDTO dto = new CustomerBorrowHistoryDTO();
                    dto.setRecordId(r.getId());
                    dto.setBookId(r.getBook().getId());
                    dto.setBookTitle(r.getBook().getTitle());
                    dto.setAuthor(r.getBook().getAuthor());
                    dto.setBorrowDate(r.getBorrowDate());
                    dto.setDueDate(r.getDueDate());
                    dto.setReturnDate(r.getReturnDate());
                    dto.setOverdueDays(r.getOverdueDays());
                    dto.setStatus(r.getStatus());
                    dto.setDeposit(r.getDeposit());
                    dto.setFine(r.getFine());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    private void updateCardStatus(Customer customer) {
        if (customer.getCardStatus() == Customer.CardStatus.BANNED) return;
        if (customer.getCardExpireDate() != null && customer.getCardExpireDate().isBefore(LocalDate.now())) {
            customer.setCardStatus(Customer.CardStatus.EXPIRED);
        }
    }

    private void mapToCustomer(Customer customer, CustomerRequest req) {
        
        customer.setFullName(req.getFullName());
        customer.setPhone(req.getPhone());
        customer.setCccd(req.getCccd());
        customer.setBirthDate(req.getBirthDate());
        customer.setAddress(req.getAddress());
        customer.setCardType(req.getCardType());

        LocalDate cardStart = req.getCardStartDate() != null ? req.getCardStartDate() : LocalDate.now();
        customer.setCardStartDate(cardStart);

        if (req.getCardExpireDate() != null) {
            customer.setCardExpireDate(req.getCardExpireDate());
        } else if (req.getCardType() != null) {
            customer.setCardExpireDate(req.getCardType() == Customer.CardType.MONTHLY
                    ? cardStart.plusMonths(1) : cardStart.plusYears(1));
        }
    }

    private CustomerDTO toDTO(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(customer.getId());
        dto.setFullName(customer.getFullName());
        dto.setPhone(customer.getPhone());
        dto.setCccd(customer.getCccd());
        dto.setBirthDate(customer.getBirthDate());
        dto.setJoinDate(customer.getJoinDate());
        dto.setCardStartDate(customer.getCardStartDate());
        dto.setCardExpireDate(customer.getCardExpireDate());
        dto.setCardType(customer.getCardType());
        dto.setCardStatus(customer.getCardStatus());
        dto.setAddress(customer.getAddress());

        List<BorrowRecord> records = borrowRecordRepository.findByCustomerId(customer.getId());
        long borrowing = records.stream().filter(r -> r.getStatus() == BorrowRecord.BorrowStatus.BORROWING).count();
        long returned = records.stream().filter(r -> r.getStatus() != BorrowRecord.BorrowStatus.BORROWING).count();
        long overdue = records.stream().filter(r -> r.getStatus() == BorrowRecord.BorrowStatus.BORROWING
                && r.getDueDate().isBefore(LocalDate.now())).count();

        dto.setTotalBorrowed((int) records.size());
        dto.setTotalReturned((int) returned);
        dto.setCurrentBorrowing((int) borrowing);
        dto.setOverdueBooks((int) overdue);
        return dto;
    }
}
