package com.poly.promotion.domain.core.valueobject;

import com.poly.domain.valueobject.Money;

import java.math.BigDecimal;

/**
 * <h2>DiscountAmount Class</h2>
 * 
 * <p>Represents a fixed-amount discount that reduces the transaction amount by a specified monetary value.
 * This class implements the {@link Discount} interface and provides functionality for calculating
 * fixed-amount discounts.</p>
 * 
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Fixed amount must be at least 1000 VND</li>
 *   <li>The discount amount cannot exceed the original transaction amount</li>
 *   <li>Values are stored with precision for accurate calculations</li>
 * </ul>
 * 
 * <p><strong>Calculation Example:</strong></p>
 * <p>For a $50 fixed discount on a $100 transaction:</p>
 * <ul>
 *   <li>Discount amount = $50 (fixed)</li>
 *   <li>Final amount = $100 - $50 = $50</li>
 * </ul>
 * 
 * <p><strong>Safety Features:</strong></p>
 * <p>This class automatically caps the discount amount at the original transaction amount
 * to prevent negative final amounts. For example, a $150 discount on a $100 transaction
 * will only apply $100.</p>
 * 
 * @author Nguyen Dam Hoang Linh
 * @version 1.0
 * @since 2025
 * @see Discount
 * @see Money
 */
public class DiscountAmount implements Discount {
    
    /**
     * The fixed discount amount.
     * Stored as a Money object for proper currency handling and precision.
     * 
     * <p>This field is final to ensure immutability of the discount amount.</p>
     */
    private final Money value;

    /**
     * Creates a new fixed-amount discount with the specified monetary value.
     * 
     * <p>This constructor validates that the discount amount meets the minimum requirement
     * (1000 VND) before creating the discount instance.</p>
     * 
     * @param value the fixed discount amount
     * @throws IllegalArgumentException if the discount amount is less than 1000 VND
     */
    public DiscountAmount(Money value) {
        if (!value.isGreaterThan(new Money(BigDecimal.valueOf(999.99)))) {
            throw new IllegalArgumentException("Discount amount must be greater than or equal 1000 VND");
        }
        this.value = value;
    }

    /**
     * Gets the fixed discount amount.
     * 
     * @return the discount amount as a BigDecimal
     */
    @Override
    public BigDecimal getValue() {
        return value.getAmount();
    }

    /**
     * Calculates the discount amount to be applied to the transaction.
     * 
     * <p>For fixed-amount discounts, this method returns the discount amount directly,
     * but ensures it doesn't exceed the original transaction amount. This prevents
     * negative final amounts.</p>
     * 
     * <p><strong>Safety Logic:</strong></p>
     * <ul>
     *   <li>If discount amount ≤ original price: returns the full discount amount</li>
     *   <li>If discount amount > original price: returns the original price (capped)</li>
     * </ul>
     * 
     * @param originalPrice the original transaction amount before discount
     * @return the discount amount to be applied, never exceeding the original price
     * @throws IllegalArgumentException if originalPrice is null
     */
    @Override
    public Money calculateDiscountAmount(Money originalPrice) {
        if (originalPrice == null) {
            throw new IllegalArgumentException("Original price cannot be null");
        }
        
        // For fixed amount discount, return the discount amount directly
        // but ensure it doesn't exceed the original price
        if (value.isGreaterThan(originalPrice)) {
            return originalPrice;
        }
        return value;
    }

    /**
     * Gets a human-readable representation of this fixed-amount discount.
     * 
     * <p>This method returns a formatted string that customers can easily understand,
     * such as "50,000 VND" for a 50,000 VND discount.</p>
     * 
     * @return a formatted string representation with currency (e.g., "50,000 VND")
     */
    @Override
    public String getDisplayValue() {
        return value.getAmount().toString() + " VND";
    }

    /**
     * Returns a string representation of this discount.
     * 
     * <p>This method delegates to {@link #getDisplayValue()} to provide
     * a consistent string representation.</p>
     * 
     * @return a formatted string representation of the discount
     */
    @Override
    public String toString() {
        return getDisplayValue();
    }
}
