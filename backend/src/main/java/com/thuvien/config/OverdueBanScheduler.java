package com.thuvien.config;

import com.thuvien.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

// mỗi ngày lúc 00:05 tự động cấm khách hàng có phiếu mượn quá hạn quá 3 ngày mà chưa trả sách
@Component
public class OverdueBanScheduler {

    @Autowired
    private CustomerService customerService;

    @Scheduled(cron = "0 5 0 * * *")
    public void checkOverdueAndBan() {
        customerService.banOverdueCustomers();
    }
}
