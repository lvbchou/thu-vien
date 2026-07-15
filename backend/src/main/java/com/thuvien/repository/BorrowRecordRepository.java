package com.thuvien.repository;

import com.thuvien.entity.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {
    List<BorrowRecord> findByCustomerId(String customerId);
    List<BorrowRecord> findByBookId(String bookId);
    // dùng để check xem khách hàng có đang mượn sách nào không, nếu có thì không cho xóa KH
    List<BorrowRecord> findByCustomerIdAndStatus(String customerId, BorrowRecord.BorrowStatus status);
    
    @Query("SELECT COUNT(r) FROM BorrowRecord r WHERE r.customer.id = :customerId AND r.status = 'BORROWING'")
    long countActiveBorrowsByCustomer(@Param("customerId") String customerId);

    @Query("SELECT r FROM BorrowRecord r WHERE r.status = 'BORROWING' AND r.dueDate < :today")
    List<BorrowRecord> findOverdueRecords(@Param("today") LocalDate today);

    // dùng để cấm khách hàng quá hạn: dựa vào returnDate IS NULL (chưa trả) thay vì status,
    // vì status có thể đã bị đánh dấu OVERDUE dù sách chưa được trả
    @Query("SELECT r FROM BorrowRecord r WHERE r.returnDate IS NULL AND r.dueDate < :cutoff")
    List<BorrowRecord> findUnreturnedOverdueBefore(@Param("cutoff") LocalDate cutoff);
    
    // dùng để thống kê tổng số sách đang được mượn
    @Query("SELECT COUNT(r) FROM BorrowRecord r WHERE r.status = 'BORROWING'")
    long countTotalActiveBorrows();

    // dùng để thống kê số sách mượn theo ngày, tháng, năm
    @Query("SELECT r FROM BorrowRecord r WHERE r.book.id = :bookId AND r.borrowDate BETWEEN :from AND :to")
    List<BorrowRecord> findByBookAndDateRange(@Param("bookId") String bookId,
                                              @Param("from") LocalDate from,
                                              @Param("to") LocalDate to);

    @Query("SELECT r FROM BorrowRecord r WHERE r.customer.id = :customerId AND r.borrowDate >= :from AND (r.returnDate <= :to OR r.returnDate IS NULL)")
    List<BorrowRecord> findByCustomerAndDateRange(@Param("customerId") String customerId,
                                                   @Param("from") LocalDate from,
                                                   @Param("to") LocalDate to);
}
