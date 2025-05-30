package com.poly.ai.management.dao.ai.repository;

import com.poly.ai.management.dao.ai.entity.ResponseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResponseJPARepository extends JpaRepository<ResponseEntity, String> {
}
