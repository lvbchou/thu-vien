package com.thuvien.config;
import org.springframework.beans.factory.annotation.Autowired;

import com.thuvien.entity.*;
import com.thuvien.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired


    private BookRepository bookRepository;
    @Autowired

    private CustomerRepository customerRepository;
    @Autowired

    private BorrowRecordRepository borrowRecordRepository;

    @Override
    public void run(String... args) {
        if (bookRepository.count() > 0) return;

        // Books
        Book b1 = new Book();
        b1.setId("S001"); b1.setTitle("Đắc Nhân Tâm"); b1.setAuthor("Dale Carnegie");
        b1.setPublisher("NXB Trẻ"); b1.setGenre("Kỹ năng sống");
        b1.setPrice(new BigDecimal("89000")); b1.setShelf("A1");
        b1.setImportDate(LocalDate.of(2026,1,12)); b1.setPurchaseDate(LocalDate.of(2026,1,12));
        b1.setExternalCondition("Còn mới"); b1.setStatus(Book.BookStatus.LOST);
        b1.setTotalBorrows(1); bookRepository.save(b1);

        Book b2 = new Book();
        b2.setId("S002"); b2.setTitle("Nhà Giả Kim"); b2.setAuthor("Paulo Coelho");
        b2.setPublisher("Nhà Nam"); b2.setGenre("Tiểu thuyết");
        b2.setPrice(new BigDecimal("79000")); b2.setShelf("B2");
        b2.setImportDate(LocalDate.of(2023,11,24)); b2.setPurchaseDate(LocalDate.of(2023,11,24));
        b2.setExternalCondition("Rách nhẹ trang 12"); b2.setStatus(Book.BookStatus.MAINTENANCE);
        b2.setTotalBorrows(1); b2.setLastReturnDate(LocalDate.of(2026,4,2));
        bookRepository.save(b2);

        Book b3 = new Book();
        b3.setId("S003"); b3.setTitle("Sapiens"); b3.setAuthor("Yuval Noah Harari");
        b3.setPublisher("NXB Thế Giới"); b3.setGenre("Lịch sử");
        b3.setPrice(new BigDecimal("199000")); b3.setShelf("C3");
        b3.setImportDate(LocalDate.of(2026,2,21)); b3.setPurchaseDate(LocalDate.of(2026,2,21));
        b3.setExternalCondition("Tốt"); b3.setStatus(Book.BookStatus.AVAILABLE);
        b3.setTotalBorrows(0); bookRepository.save(b3);

        Book b4 = new Book();
        b4.setId("S004"); b4.setTitle("Tuổi Trẻ Đáng Giá Bao Nhiêu"); b4.setAuthor("Rosie Nguyễn");
        b4.setPublisher("NXB Trẻ"); b4.setGenre("Kỹ năng sống");
        b4.setPrice(new BigDecimal("45000")); b4.setShelf("A2");
        b4.setImportDate(LocalDate.of(2026,3,23)); b4.setPurchaseDate(LocalDate.of(2026,3,23));
        b4.setExternalCondition("Bia hơi nứt"); b4.setStatus(Book.BookStatus.BORROWED);
        b4.setTotalBorrows(1); bookRepository.save(b4);

        // Customers
        Customer c1 = new Customer();
        c1.setId("KH001"); c1.setFullName("Nguyễn Văn An"); c1.setPhone("0901234567");
        c1.setCccd("012345678901"); c1.setBirthDate(LocalDate.of(1995,4,12));
        c1.setJoinDate(LocalDate.of(2026,4,22)); c1.setAddress("12 Lê Lợi, Hà Nội");
        c1.setCardType(Customer.CardType.YEARLY); c1.setCardStartDate(LocalDate.of(2026,4,22));
        c1.setCardExpireDate(LocalDate.of(2027,4,22)); c1.setCardStatus(Customer.CardStatus.ACTIVE);
        customerRepository.save(c1);

        Customer c2 = new Customer();
        c2.setId("KH002"); c2.setFullName("Trần Thị Bình"); c2.setPhone("0912345678");
        c2.setCccd("012345678902"); c2.setBirthDate(LocalDate.of(2001,9,30));
        c2.setJoinDate(LocalDate.of(2026,3,13)); c2.setAddress("45 Nguyễn Huệ, TP.HCM");
        c2.setCardType(Customer.CardType.MONTHLY); c2.setCardStartDate(LocalDate.of(2026,3,13));
        c2.setCardExpireDate(LocalDate.of(2026,6,1)); c2.setCardStatus(Customer.CardStatus.ACTIVE);
        customerRepository.save(c2);

        Customer c3 = new Customer();
        c3.setId("KH003"); c3.setFullName("Lê Hoàng Châu"); c3.setPhone("0987654321");
        c3.setCccd("012345678903"); c3.setBirthDate(LocalDate.of(1988,1,5));
        c3.setJoinDate(LocalDate.of(2026,5,7)); c3.setAddress("78 Trần Phú, Đà Nẵng");
        c3.setCardType(Customer.CardType.MONTHLY); c3.setCardStartDate(LocalDate.of(2026,5,7));
        c3.setCardExpireDate(LocalDate.of(2026,6,6)); c3.setCardStatus(Customer.CardStatus.ACTIVE);
        customerRepository.save(c3);

        // Borrow records
        BorrowRecord r1 = new BorrowRecord();
        r1.setBook(b1); r1.setCustomer(c1);
        r1.setBorrowDate(LocalDate.of(2026,4,22)); r1.setDueDate(LocalDate.of(2026,5,13));
        r1.setDeposit(new BigDecimal("44500")); r1.setStatus(BorrowRecord.BorrowStatus.BORROWING);
        borrowRecordRepository.save(r1);

        BorrowRecord r2 = new BorrowRecord();
        r2.setBook(b2); r2.setCustomer(c2);
        r2.setBorrowDate(LocalDate.of(2026,3,15)); r2.setDueDate(LocalDate.of(2026,4,2));
        r2.setReturnDate(LocalDate.of(2026,4,2)); r2.setOverdueDays(0);
        r2.setDeposit(new BigDecimal("23700")); r2.setFine(BigDecimal.ZERO);
        r2.setStatus(BorrowRecord.BorrowStatus.RETURNED);
        borrowRecordRepository.save(r2);

        BorrowRecord r3 = new BorrowRecord();
        r3.setBook(b4); r3.setCustomer(c3);
        r3.setBorrowDate(LocalDate.of(2026,5,7)); r3.setDueDate(LocalDate.of(2026,5,21));
        r3.setDeposit(new BigDecimal("22500")); r3.setStatus(BorrowRecord.BorrowStatus.BORROWING);
        borrowRecordRepository.save(r3);

        System.out.println("=== Dữ liệu mẫu đã được tạo ===");
    }
}
