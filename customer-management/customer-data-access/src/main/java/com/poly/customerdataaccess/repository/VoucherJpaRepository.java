package com.poly.customerdataaccess.repository;

import com.poly.customerdataaccess.entity.VoucherEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoucherJpaRepository extends JpaRepository<VoucherEntity, Integer> {
}
