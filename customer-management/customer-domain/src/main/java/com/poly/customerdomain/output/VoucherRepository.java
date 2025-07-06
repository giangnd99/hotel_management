package com.poly.customerdomain.output;

import com.poly.customerdomain.model.entity.Voucher;

public interface VoucherRepository {
    Voucher save(Voucher voucher);
}
