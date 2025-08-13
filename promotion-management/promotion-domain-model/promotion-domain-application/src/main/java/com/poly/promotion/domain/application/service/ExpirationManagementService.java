package com.poly.promotion.domain.application.service;

/**
 * <h2>ExpirationManagementService Interface</h2>
 * 
 * <p>Service interface for managing expiration operations on vouchers and voucher packs.
 * This service is designed to be used by scheduled tasks or system operations to
 * automatically expire expired vouchers and voucher packs.</p>
 * 
 * <p><strong>Service Responsibilities:</strong></p>
 * <ul>
 *   <li>Automatic expiration of expired voucher packs</li>
 *   <li>Automatic expiration of expired vouchers</li>
 *   <li>Comprehensive expiration checks across the system</li>
 *   <li>Coordination of expiration operations</li>
 *   <li>Performance monitoring and reporting</li>
 * </ul>
 * 
 * <p><strong>Business Context:</strong></p>
 * <p>This service is crucial for maintaining data integrity and ensuring that
 * expired promotional items are properly marked. It's typically invoked by:</p>
 * <ul>
 *   <li>Scheduled cron jobs (e.g., daily at midnight)</li>
 *   <li>System maintenance processes</li>
 *   <li>Manual administrative operations</li>
 *   <li>System health checks and monitoring</li>
 * </ul>
 * 
 * <p><strong>Expiration Logic:</strong></p>
 * <ul>
 *   <li><strong>Voucher Packs:</strong> PUBLISHED or PENDING packs past their packValidTo date</li>
 *   <li><strong>Vouchers:</strong> REDEEMED vouchers past their validTo date</li>
 *   <li><strong>Priority:</strong> Packs are expired first, then vouchers</li>
 * </ul>
 * 
 * <p><strong>Performance Considerations:</strong></p>
 * <p>The service includes timing information to monitor performance and identify
 * potential bottlenecks in expiration operations.</p>
 * 
 * @author Nguyen Dam Hoang Linh
 * @version 1.0
 * @since 2025
 * @see VoucherPackService
 * @see VoucherService
 */
public interface ExpirationManagementService {
    
    /**
     * Marks voucher packs as expired that are past their validity date.
     * 
     * <p>This method identifies all voucher packs that are currently PUBLISHED or PENDING
     * and have passed their packValidTo date, then updates their status to EXPIRED.
     * This operation is performed first to ensure proper coordination with voucher expiration.</p>
     * 
     * <p><strong>Process:</strong></p>
     * <ol>
     *   <li>Identifies eligible packs (PUBLISHED or PENDING with expired packValidTo)</li>
     *   <li>Updates each pack's status to EXPIRED</li>
     *   <li>Continues processing even if individual packs fail</li>
     *   <li>Returns the count of successfully expired packs</li>
     * </ol>
     * 
     * @return the number of voucher packs that were marked as expired
     */
    int markExpiredVoucherPacks();
    
    /**
     * Marks vouchers as expired that are past their validity date.
     * 
     * <p>This method identifies all vouchers that are currently REDEEMED
     * and have passed their validTo date, then updates their status to EXPIRED.
     * This operation is performed after voucher pack expiration to ensure proper coordination.</p>
     * 
     * <p><strong>Process:</strong></p>
     * <ol>
     *   <li>Identifies eligible vouchers (REDEEMED with expired validTo)</li>
     *   <li>Updates each voucher's status to EXPIRED</li>
     *   <li>Continues processing even if individual vouchers fail</li>
     *   <li>Returns the count of successfully expired vouchers</li>
     * </ol>
     * 
     * @return the number of vouchers that were marked as expired
     */
    int markExpiredVouchers();
    
    /**
     * Performs a comprehensive expiration check on all vouchers and voucher packs.
     * 
     * <p>This method coordinates the expiration of both voucher packs and vouchers
     * in a single operation, providing performance metrics and ensuring proper
     * coordination between the two operations.</p>
     * 
     * <p><strong>Process:</strong></p>
     * <ol>
     *   <li>Records start time for performance monitoring</li>
     *   <li>Expires voucher packs first (may affect voucher availability)</li>
     *   <li>Expires vouchers second (after pack changes are complete)</li>
     *   <li>Calculates total processing time</li>
     *   <li>Returns comprehensive summary of the operation</li>
     * </ol>
     * 
     * <p><strong>Usage:</strong></p>
     * <p>This method is ideal for scheduled tasks and system maintenance operations
     * where you want to perform all expiration operations in a single, coordinated
     * operation with performance monitoring.</p>
     * 
     * @return a summary of the expiration operation containing counts and timing information
     */
    default ExpirationSummary performComprehensiveExpirationCheck() {
        long startTime = System.currentTimeMillis();
        
        // First, expire voucher packs (this might affect vouchers)
        int expiredVoucherPacks = markExpiredVoucherPacks();
        
        // Then, expire vouchers
        int expiredVouchers = markExpiredVouchers();
        
        long totalProcessingTime = System.currentTimeMillis() - startTime;
        
        return new ExpirationSummary(expiredVoucherPacks, expiredVouchers, totalProcessingTime);
    }
    
    /**
     * Summary of expiration operations.
     * 
     * <p>This record provides a comprehensive overview of an expiration operation,
     * including counts of expired items and performance metrics.</p>
     * 
     * <p><strong>Fields:</strong></p>
     * <ul>
     *   <li><strong>expiredVoucherPacks:</strong> Number of voucher packs that were expired</li>
     *   <li><strong>expiredVouchers:</strong> Number of vouchers that were expired</li>
     *   <li><strong>totalProcessingTimeMs:</strong> Total time taken for the operation in milliseconds</li>
     * </ul>
     * 
     * <p><strong>Usage:</strong></p>
     * <p>This record is returned by comprehensive expiration operations to provide
     * detailed feedback about the operation's results and performance.</p>
     * 
     * @param expiredVoucherPacks the number of voucher packs that were expired
     * @param expiredVouchers the number of vouchers that were expired
     * @param totalProcessingTimeMs the total processing time in milliseconds
     */
    record ExpirationSummary(
        int expiredVoucherPacks,
        int expiredVouchers,
        long totalProcessingTimeMs
    ) {}
}
