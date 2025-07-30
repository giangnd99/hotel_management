package com.poly.promotion.domain.application.spi.repository;

import com.poly.promotion.domain.core.entity.VoucherPack;

import java.util.List;

public interface VoucherPackRepository {
    boolean existsById(Long voucherPackId);
    boolean isOfStatus(Long voucherPackId, Integer status);
    long getVoucherPackQuantity(Long voucherPackId);
    /**
     * Retrieves a voucher pack by its ID.
     *
     * @param voucherPackId the ID of the voucher pack to retrieve.
     * @return the voucher pack with the specified ID, or null if not found.
     */
    VoucherPack getVoucherPackById(Long voucherPackId);

    /**
     * Retrieves all voucher packs with a specific status.
     *
     * @param status the status of the voucher packs to retrieve.
     * @return a list of voucher packs with the specified status.
     */
    List<VoucherPack> getAllVoucherPacksWithStatus(Integer status);

    /**
     * Creates a new voucher pack.
     *
     * @param voucherPack the voucher pack to create.
     * @return the created voucher pack.
     */
    VoucherPack createVoucherPack(VoucherPack voucherPack);

    /**
     * Updates a pending voucher pack.
     *
     * @param voucherPackId the ID of the voucher pack to update.
     * @param voucherPack   the updated voucher pack data.
     * @return the updated voucher pack.
     */
    VoucherPack updatePendingVoucherPack(Long voucherPackId, VoucherPack voucherPack);

    /**
     * Closes a voucher pack by setting its status to closed.
     *
     * @param voucherPackId the ID of the voucher pack to close.
     */
    void closeVoucherPack(Long voucherPackId);
    void reduceVoucherPackStockAfterRedeem(Long voucherPackId, Integer quantity);
}
