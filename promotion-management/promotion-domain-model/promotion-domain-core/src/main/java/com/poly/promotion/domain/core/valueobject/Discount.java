package com.poly.promotion.domain.core.valueobject;

import com.poly.domain.valueobject.Money;

import java.math.BigDecimal;

/**
 * <h2>Discount Interface</h2>
 * 
 * <p>Represents a discount that can be applied to transactions. This interface provides
 * a common contract for different types of discounts, allowing the system to handle
 * both percentage-based and fixed-amount discounts uniformly.</p>
 * 
 * <p><strong>Discount Types:</strong></p>
 * <ul>
 *   <li><strong>Percentage Discount:</strong> Reduces the transaction amount by a percentage (e.g., 10% off)</li>
 *   <li><strong>Fixed Amount Discount:</strong> Reduces the transaction amount by a fixed amount (e.g., $50 off)</li>
 * </ul>
 * 
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Percentage discounts must be between 0% and 100%</li>
 *   <li>Fixed amount discounts must be at least 1000 VND</li>
 *   <li>Discounts cannot exceed the original transaction amount</li>
 * </p>
 * 
 * <p><strong>Usage:</strong></p>
 * <p>This interface is implemented by {@link DiscountPercentage} and {@link DiscountAmount}
 * classes, which provide specific implementations for each discount type.</p>
 * 
 * @author Nguyen Dam Hoang Linh
 * @version 1.0
 * @since 2025
 * @see DiscountPercentage
 * @see DiscountAmount
 * @see Money
 */
public interface Discount {
    
    /**
     * Gets the numeric value of this discount.
     * 
     * <p>For percentage discounts, this returns the percentage value (e.g., 15.0 for 15%).
     * For fixed amount discounts, this returns the amount in the smallest currency unit.</p>
     * 
     * @return the discount value as a BigDecimal for precision
     */
    BigDecimal getValue();
    
    /**
     * Calculates the actual discount amount to be applied to a transaction.
     * 
     * <p>This method takes the original transaction amount and calculates how much
     * should be discounted based on the discount type and value.</p>
     * 
     * <p><strong>Examples:</strong></p>
     * <ul>
     *   <li>Percentage discount: 20% off $100 = $20 discount</li>
     *   <li>Fixed amount: $50 off $100 = $50 discount</li>
     *   <li>Fixed amount: $150 off $100 = $100 discount (capped at transaction amount)</li>
     * </ul>
     * 
     * @param originalPrice the original transaction amount before discount
     * @return the discount amount to be applied, never exceeding the original price
     */
    Money calculateDiscountAmount(Money originalPrice);
    
    /**
     * Gets a human-readable representation of this discount.
     * 
     * <p>This method returns a formatted string that customers can understand,
     * such as "15%" for percentage discounts or "50,000 VND" for fixed amounts.</p>
     * 
     * @return a formatted string representation of the discount
     */
    String getDisplayValue();
}
