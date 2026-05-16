package com.thuvien.repository;

import com.thuvien.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

// dùng để truy vấn sách theo thể loại, tìm kiếm sách theo tên hoặc id, và lấy danh sách thể loại có trong thư viện
public interface BookRepository extends JpaRepository<Book, String> {
    List<Book> findByGenreIgnoreCase(String genre);
    List<Book> findByTitleContainingIgnoreCaseOrIdContainingIgnoreCase(String title, String id);

    @Query("SELECT DISTINCT b.genre FROM Book b WHERE b.genre IS NOT NULL ORDER BY b.genre")
    List<String> findAllGenres();
}
