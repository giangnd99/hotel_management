package com.poly.promotion.domain.application.spi.repository;

import com.poly.promotion.domain.core.entity.Voucher;
import com.poly.promotion.domain.core.valueobject.VoucherStatus;

import java.util.List;

public interface VoucherRepository {
    /**
     * Checks if a voucher exists by its ID.
     *
     * @param voucherId the ID of the voucher to check.
     * @return true if the voucher exists, false otherwise.
     */
    boolean existsById(String voucherId);

    /**
     * Retrieves a voucher by its ID.
     *
     * @param voucherId the ID of the voucher to retrieve.
     * @return the voucher with the specified ID, or null if not found.
     */
    Voucher getVoucherById(String voucherId);

    /**
     * Retrieves a voucher by its voucher code.
     *
     * @param voucherCode the voucher code to search for.
     * @return the voucher with the specified code, or null if not found.
     */
    Voucher getVoucherByCode(String voucherCode);

    /**
     * Retrieves all vouchers for a specific customer with optional status filtering.
     *
     * @param customerId the customer ID to filter by.
     * @param statuses optional status values to filter by.
     * @return a list of vouchers matching the criteria.
     */
    List<Voucher> getAllVouchersWithStatus(String customerId, VoucherStatus... statuses);

    /**
     * Retrieves all vouchers for a specific voucher pack.
     *
     * @param voucherPackId the voucher pack ID to filter by.
     * @return a list of vouchers from the specified pack.
     */
    List<Voucher> getVouchersByPackId(Long voucherPackId);

    /**
     * Creates a new voucher.
     *
     * @param voucher the voucher to create.
     * @return the created voucher.
     */
    Voucher createVoucher(Voucher voucher);

    /**
     * Updates an existing voucher.
     *
     * @param voucher the voucher to update.
     * @return the updated voucher.
     */
    Voucher updateVoucher(Voucher voucher);

    /**
     * Updates the status of a voucher.
     *
     * @param voucherId the ID of the voucher to update.
     * @param newStatus the new status to set.
     */
    void updateVoucherStatus(String voucherId, VoucherStatus newStatus);

    /**
     * Expires vouchers that are past their validity date.
     *
     * @return the number of vouchers that were expired.
     */
    int expireExpiredVouchers();

    /**
     * Checks if a voucher code is already in use.
     *
     * @param voucherCode the voucher code to check.
     * @return true if the code is already in use, false otherwise.
     */
    boolean isVoucherCodeExists(String voucherCode);
}
