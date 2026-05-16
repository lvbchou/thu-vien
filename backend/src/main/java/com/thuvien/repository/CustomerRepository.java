package com.thuvien.repository;

import com.thuvien.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

// dùng để tìm kiếm khách hàng theo tên, id, số điện thoại, cccd, và lọc khách hàng theo loại thẻ và trạng thái thẻ
public interface CustomerRepository extends JpaRepository<Customer, String> {
    List<Customer> findByFullNameContainingIgnoreCaseOrIdContainingIgnoreCaseOrPhoneContainingOrCccdContaining(
        String name, String id, String phone, String cccd);

    List<Customer> findByCardType(Customer.CardType cardType);
    List<Customer> findByCardStatus(Customer.CardStatus cardStatus);
}
