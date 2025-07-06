package com.poly.customerdataaccess.jpa;

import com.poly.customerdataaccess.entity.VoucherEntity;
import com.poly.customerdomain.model.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VoucherJpaRepository extends JpaRepository<VoucherEntity, Integer> {
}
