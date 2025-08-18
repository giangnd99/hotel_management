package com.poly.notification.management.repository;

import com.poly.notification.management.entity.QrCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface QrCodeRepository extends JpaRepository<QrCode, Long> {
    
    /**
     * Tìm QR code theo data
     */
    Optional<QrCode> findByData(String data);
    
    /**
     * Tìm tất cả QR code đang hoạt động
     */
    List<QrCode> findByIsActiveTrue();
    
    /**
     * Tìm QR code theo data và trạng thái hoạt động
     */
    Optional<QrCode> findByDataAndIsActiveTrue(String data);
    
    /**
     * Tìm QR code được tạo trong khoảng thời gian
     */
    List<QrCode> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Tìm QR code theo mô tả (tìm kiếm mờ)
     */
    @Query("SELECT q FROM QrCode q WHERE q.description LIKE %:keyword% AND q.isActive = true")
    List<QrCode> findByDescriptionContaining(@Param("keyword") String keyword);
    
    /**
     * Đếm số lượng QR code đang hoạt động
     */
    long countByIsActiveTrue();
    
    /**
     * Tìm QR code theo kích thước
     */
    List<QrCode> findByWidthAndHeight(Integer width, Integer height);
    
    /**
     * Tìm QR code theo format
     */
    List<QrCode> findByFormat(String format);
    
    /**
     * Xóa mềm QR code (cập nhật trạng thái không hoạt động)
     */
    @Query("UPDATE QrCode q SET q.isActive = false, q.updatedAt = :updatedAt WHERE q.id = :id")
    void softDeleteById(@Param("id") Long id, @Param("updatedAt") LocalDateTime updatedAt);
}
