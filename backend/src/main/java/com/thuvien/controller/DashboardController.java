package com.thuvien.controller;
import org.springframework.beans.factory.annotation.Autowired;

import com.thuvien.dto.DashboardDTO;
import com.thuvien.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//cung cấp dữ liệu tổng hợp cho trang chủ (Dashboard)
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired


    private DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<DashboardDTO> getDashboard() {
        return ResponseEntity.ok(dashboardService.getDashboard());
    }
}
