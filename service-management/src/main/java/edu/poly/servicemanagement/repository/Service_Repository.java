package edu.poly.servicemanagement.repository;

import edu.poly.servicemanagement.entity.Service_;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Service_Repository extends JpaRepository<Service_, Integer> {
    
    Optional<Service_> findByServiceName(String serviceName);
    
    List<Service_> findByAvailability(String availability);
    
    @Query("SELECT s FROM Service_ s WHERE s.price BETWEEN :minPrice AND :maxPrice")
    List<Service_> findByPriceRange(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);
    
    @Query("SELECT s FROM Service_ s WHERE s.serviceName LIKE %:keyword% OR s.description LIKE %:keyword%")
    List<Service_> searchByKeyword(@Param("keyword") String keyword);
}
