package com.thuvien;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ThuVienApplication {
    public static void main(String[] args) {
        SpringApplication.run(ThuVienApplication.class, args);
    }
}
