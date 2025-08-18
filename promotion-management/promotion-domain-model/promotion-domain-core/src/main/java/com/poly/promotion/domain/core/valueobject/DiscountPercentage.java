package com.poly.promotion.domain.core.valueobject;

import com.poly.domain.valueobject.Money;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * <h2>DiscountPercentage Class</h2>
 * 
 * <p>Represents a percentage-based discount that reduces the transaction amount by a specified percentage.
 * This class implements the {@link Discount} interface and provides functionality for calculating
 * percentage-based discounts.</p>
 * 
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Percentage must be between 0% and 100% (inclusive)</li>
 *   <li>0% means no discount</li>
 *   <li>100% means the entire transaction amount is discounted</li>
 *   <li>Values are stored with precision for accurate calculations</li>
 * </ul>
 * 
 * <p><strong>Calculation Example:</strong></p>
 * <p>For a 15% discount on a $100 transaction:</p>
 * <ul>
 *   <li>Discount amount = $100 × 15% = $15</li>
 *   <li>Final amount = $100 - $15 = $85</li>
 * </ul>
 * 
 * <p><strong>Precision:</strong></p>
 * <p>This class uses BigDecimal for precise percentage calculations, avoiding floating-point
 * arithmetic errors that could occur with double or float types.</p>
 * 
 * @author Nguyen Dam Hoang Linh
 * @version 1.0
 * @since 2025
 * @see Discount
 * @see Money
 */
public class DiscountPercentage implements Discount {
    
    /**
     * The percentage value of this discount.
     * Stored as a BigDecimal for precision, representing the percentage (e.g., 15.0 for 15%).
     * 
     * <p>This field is final to ensure immutability of the discount value.</p>
     */
    private final BigDecimal percentage;

    /**
     * Creates a new percentage discount with the specified percentage value.
     * 
     * <p>This constructor validates that the percentage is within the valid range
     * (0% to 100%) before creating the discount instance.</p>
     * 
     * @param percentage the percentage value (0.0 to 100.0)
     * @throws IllegalArgumentException if the percentage is outside the valid range
     */
    public DiscountPercentage(double percentage) {
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("Percentage must be between 0 and 100");
        }
        this.percentage = BigDecimal.valueOf(percentage);
    }

    /**
     * Gets the percentage value of this discount.
     * 
     * @return the percentage value as a BigDecimal (e.g., 15.0 for 15%)
     */
    @Override
    public BigDecimal getValue() {
        return percentage;
    }

    /**
     * Gets a human-readable representation of this percentage discount.
     * 
     * <p>This method returns a formatted string that customers can easily understand,
     * such as "15%" for a 15% discount.</p>
     * 
     * @return a formatted string representation (e.g., "15%")
     */
    @Override
    public String getDisplayValue() {
        return percentage + "%";
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
