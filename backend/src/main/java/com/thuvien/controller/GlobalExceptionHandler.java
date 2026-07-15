package com.thuvien.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

// bắt các lỗi nghiệp vụ (RuntimeException) trước khi chúng thoát ra khỏi DispatcherServlet;
// nếu không, exception sẽ rơi vào trang lỗi mặc định của Tomcat, bị Spring Security
// chặn lại (yêu cầu xác thực lại) và trả về 403 rỗng thay vì thông báo lỗi thực sự
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", ex.getMessage()));
    }
}
