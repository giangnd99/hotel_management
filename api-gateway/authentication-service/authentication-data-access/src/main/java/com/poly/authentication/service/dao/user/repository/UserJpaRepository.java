package com.springboot.asm.fpoly_asm_springboot.repository.primary;

import com.springboot.asm.fpoly_asm_springboot.entity.User;
import com.springboot.asm.fpoly_asm_springboot.repository.custom.CustomUserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository

public interface UserRepository extends JpaRepository<User, Integer>, CustomUserRepository {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Page<User> findAll(Pageable pageable);

    @Query("SELECT new map(u.fullName as name, SUM(o.total) as total_spent) " +
            "FROM ProductOrder o " +
            "JOIN o.user u " +
            "GROUP BY u.fullName " +
            "ORDER BY SUM(o.total) DESC")
    List<Map<String, Object>> getTopVipCustomers();
}
