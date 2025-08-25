-- Initial data for promotion management system
-- This file is executed by Liquibase after schema creation
-- 50 diverse VoucherPack records with realistic business scenarios
-- Updated for August 24, 2025 - with past, present, and future dates

-- Set search path to promotion_management schema
SET search_path TO promotion_management, public;

-- Clear existing data to avoid conflicts
DELETE FROM vouchers;
DELETE FROM voucher_packs;

-- Reset sequence for voucher_packs
ALTER SEQUENCE voucher_packs_id_seq RESTART WITH 1;

-- Insert 50 diverse VoucherPack records
INSERT INTO voucher_packs (id, description, discount_amount, valid_range, required_points, quantity, valid_from, valid_to, status, created_at, created_by, updated_at, updated_by, version) 
VALUES 
    -- PAST/EXPIRED VOUCHER PACKS (Historical campaigns - 2024)
    (1, 'Summer 2024 Welcome Pack', 15.00, '30 DAYS', 0, 200, '2024-06-01', '2024-08-31', 'EXPIRED', '2024-05-15 10:00:00', 'marketing-team', '2024-05-15 10:00:00', 'marketing-team', 0),
    (2, 'Holiday Season 2024', 50.00, '60 DAYS', 2500, 100, '2024-11-01', '2025-01-31', 'EXPIRED', '2024-10-15 14:30:00', 'promotions-manager', '2024-10-15 14:30:00', 'promotions-manager', 0),
    (3, 'New Year 2025 Special', 100.00, '90 DAYS', 5000, 50, '2024-12-15', '2025-03-15', 'EXPIRED', '2024-12-01 09:00:00', 'senior-manager', '2024-12-01 09:00:00', 'senior-manager', 0),
    (4, 'Spring Break 2025', 25.00, '45 DAYS', 1000, 150, '2025-03-01', '2025-04-15', 'EXPIRED', '2025-02-15 11:00:00', 'seasonal-promotions', '2025-02-15 11:00:00', 'seasonal-promotions', 0),
    (5, 'Easter Weekend 2025', 35.00, '30 DAYS', 1500, 75, '2025-03-25', '2025-04-25', 'EXPIRED', '2025-03-10 13:00:00', 'holiday-promotions', '2025-03-10 13:00:00', 'holiday-promotions', 0),
    
    -- CURRENT/ACTIVE VOUCHER PACKS (Ongoing campaigns - August 2025)
    (6, 'Summer 2025 Welcome Bonus', 20.00, '30 DAYS', 0, 300, '2025-08-01', '2025-08-31', 'PUBLISHED', '2025-07-20 10:00:00', 'marketing-team', '2025-07-20 10:00:00', 'marketing-team', 0),
    (7, 'August Mid-Summer Special', 40.00, '60 DAYS', 2000, 120, '2025-08-15', '2025-10-15', 'PUBLISHED', '2025-08-01 14:00:00', 'loyalty-program', '2025-08-01 14:00:00', 'loyalty-program', 0),
    (8, 'Back to School 2025', 30.00, '45 DAYS', 1200, 200, '2025-08-20', '2025-10-05', 'PUBLISHED', '2025-08-05 09:00:00', 'education-promotions', '2025-08-05 09:00:00', 'education-promotions', 0),
    (9, 'Late Summer Festival', 45.00, '75 DAYS', 3000, 80, '2025-08-25', '2025-11-10', 'PUBLISHED', '2025-08-10 16:00:00', 'seasonal-events', '2025-08-10 16:00:00', 'seasonal-events', 0),
    (10, 'Holiday Preview 2025', 75.00, '90 DAYS', 4000, 60, '2025-09-01', '2025-12-01', 'PUBLISHED', '2025-08-15 12:00:00', 'holiday-planning', '2025-08-15 12:00:00', 'holiday-planning', 0),
    
    -- FUTURE/PENDING VOUCHER PACKS (Upcoming campaigns - September 2025 onwards)
    (11, 'Autumn 2025 Collection', 60.00, '120 DAYS', 3500, 90, '2025-09-15', '2026-01-15', 'PENDING', '2025-08-20 10:00:00', 'autumn-campaigns', '2025-08-20 10:00:00', 'autumn-campaigns', 0),
    (12, 'Winter 2025-26 Collection', 80.00, '90 DAYS', 4500, 70, '2025-12-01', '2026-03-01', 'PENDING', '2025-09-01 15:00:00', 'winter-campaigns', '2025-09-01 15:00:00', 'winter-campaigns', 0),
    (13, 'New Year 2026 Countdown', 150.00, '90 DAYS', 7500, 40, '2025-12-20', '2026-03-20', 'PENDING', '2025-09-15 11:00:00', 'new-year-events', '2025-09-15 11:00:00', 'new-year-events', 0),
    (14, 'Valentine Day 2026', 55.00, '45 DAYS', 2800, 100, '2026-02-01', '2026-03-18', 'PENDING', '2025-10-01 13:00:00', 'romance-promotions', '2025-10-01 13:00:00', 'romance-promotions', 0),
    (15, 'Spring 2026 Awakening', 35.00, '60 DAYS', 1800, 150, '2026-03-01', '2026-05-01', 'PENDING', '2025-11-01 14:00:00', 'spring-campaigns', '2025-11-01 14:00:00', 'spring-campaigns', 0),
    
    -- FIXED CASH AMOUNT DISCOUNTS (VND currency values) - Mixed dates
    (16, 'Budget Traveler Pack - 100k VND', 100000.00, '15 DAYS', 500, 500, '2025-08-20', '2025-09-05', 'PUBLISHED', '2025-08-15 08:00:00', 'budget-segment', '2025-08-15 08:00:00', 'budget-segment', 0),
    (17, 'Standard Guest Pack - 250k VND', 250000.00, '30 DAYS', 1000, 300, '2025-08-25', '2025-09-25', 'PUBLISHED', '2025-08-15 09:00:00', 'standard-segment', '2025-08-15 09:00:00', 'standard-segment', 0),
    (18, 'Premium Guest Pack - 500k VND', 500000.00, '45 DAYS', 2500, 150, '2025-09-01', '2025-10-16', 'PENDING', '2025-08-15 10:00:00', 'premium-segment', '2025-08-15 10:00:00', 'premium-segment', 0),
    (19, 'Luxury Guest Pack - 1M VND', 1000000.00, '60 DAYS', 5000, 75, '2025-09-15', '2025-11-15', 'PENDING', '2025-08-15 11:00:00', 'luxury-segment', '2025-08-15 11:00:00', 'luxury-segment', 0),
    (20, 'VIP Guest Pack - 2M VND', 2000000.00, '90 DAYS', 10000, 30, '2025-10-01', '2025-12-31', 'PENDING', '2025-08-15 12:00:00', 'vip-segment', '2025-08-15 12:00:00', 'vip-segment', 0),
    
    -- PERCENTAGE DISCOUNTS (Various discount rates) - Mixed dates
    (21, '5% Off Starter Pack', 5.00, '20 DAYS', 200, 1000, '2025-08-18', '2025-09-08', 'PUBLISHED', '2025-08-15 13:00:00', 'percentage-promotions', '2025-08-15 13:00:00', 'percentage-promotions', 0),
    (22, '10% Off Regular Pack', 10.00, '30 DAYS', 500, 800, '2025-08-20', '2025-09-20', 'PUBLISHED', '2025-08-15 14:00:00', 'percentage-promotions', '2025-08-15 14:00:00', 'percentage-promotions', 0),
    (23, '15% Off Premium Pack', 15.00, '45 DAYS', 1200, 400, '2025-09-01', '2025-10-16', 'PENDING', '2025-08-15 15:00:00', 'percentage-promotions', '2025-08-15 15:00:00', 'percentage-promotions', 0),
    (24, '20% Off Elite Pack', 20.00, '60 DAYS', 2500, 200, '2025-09-15', '2025-11-15', 'PENDING', '2025-08-15 16:00:00', 'percentage-promotions', '2025-08-15 16:00:00', 'percentage-promotions', 0),
    (25, '25% Off Ultimate Pack', 25.00, '90 DAYS', 5000, 100, '2025-10-01', '2025-12-31', 'PENDING', '2025-08-15 17:00:00', 'percentage-promotions', '2025-08-15 17:00:00', 'percentage-promotions', 0),
    
    -- MORE FIXED CASH AMOUNTS (Additional VND values) - Mixed dates
    (26, 'Monsoon Season Special - 300k VND', 300000.00, '40 DAYS', 1500, 120, '2025-08-22', '2025-10-02', 'PUBLISHED', '2025-08-15 10:00:00', 'monsoon-campaigns', '2025-08-15 10:00:00', 'monsoon-campaigns', 0),
    (27, 'Autumn Colors Pack - 450k VND', 450000.00, '55 DAYS', 2800, 90, '2025-09-15', '2025-11-10', 'PENDING', '2025-08-15 11:00:00', 'autumn-campaigns', '2025-08-15 11:00:00', 'autumn-campaigns', 0),
    (28, 'Winter Warmth Pack - 650k VND', 650000.00, '70 DAYS', 3500, 70, '2025-12-01', '2026-02-10', 'PENDING', '2025-08-15 12:00:00', 'winter-campaigns', '2025-08-15 12:00:00', 'winter-campaigns', 0),
    (29, 'Spring Blossom Pack - 400k VND', 400000.00, '50 DAYS', 2200, 110, '2026-03-01', '2026-04-20', 'PENDING', '2025-08-15 13:00:00', 'spring-campaigns', '2025-08-15 13:00:00', 'spring-campaigns', 0),
    (30, 'Summer Heat Pack - 550k VND', 550000.00, '65 DAYS', 3200, 85, '2026-06-01', '2026-08-05', 'PENDING', '2025-08-15 14:00:00', 'summer-campaigns', '2025-08-15 14:00:00', 'summer-campaigns', 0),
    
    -- BUSINESS SEGMENT PACKS (Mixed discount types) - Mixed dates
    (31, 'Corporate Travel Pack - 800k VND', 800000.00, '120 DAYS', 8000, 50, '2025-09-01', '2025-12-31', 'PENDING', '2025-08-20 08:00:00', 'corporate-sales', '2025-08-20 08:00:00', 'corporate-sales', 0),
    (32, 'Family Vacation Pack - 350k VND', 350000.00, '60 DAYS', 1800, 200, '2025-08-25', '2025-10-25', 'PUBLISHED', '2025-08-20 09:00:00', 'family-segment', '2025-08-20 09:00:00', 'family-segment', 0),
    (33, 'Solo Traveler Pack - 200k VND', 200000.00, '30 DAYS', 800, 400, '2025-08-22', '2025-09-22', 'PUBLISHED', '2025-08-20 10:00:00', 'solo-segment', '2025-08-20 10:00:00', 'solo-segment', 0),
    (34, 'Luxury Business Pack - 1.5M VND', 1500000.00, '90 DAYS', 12000, 25, '2025-10-01', '2025-12-31', 'PENDING', '2025-08-20 11:00:00', 'luxury-business', '2025-08-20 11:00:00', 'luxury-business', 0),
    (35, 'Budget Business Pack - 400k VND', 400000.00, '45 DAYS', 3000, 150, '2025-09-01', '2025-10-16', 'PENDING', '2025-08-20 12:00:00', 'budget-business', '2025-08-20 12:00:00', 'budget-business', 0),
    
    -- SPECIAL EVENT PACKS (Mixed discount types) - Mixed dates
    (36, 'Wedding Anniversary Pack - 750k VND', 750000.00, '90 DAYS', 4500, 60, '2025-09-15', '2025-12-15', 'PENDING', '2025-08-25 10:00:00', 'special-occasions', '2025-08-25 10:00:00', 'special-occasions', 0),
    (37, 'Birthday Celebration Pack - 300k VND', 300000.00, '45 DAYS', 1500, 250, '2025-08-28', '2025-10-13', 'PUBLISHED', '2025-08-25 11:00:00', 'special-occasions', '2025-08-25 11:00:00', 'special-occasions', 0),
    (38, 'Graduation Party Pack - 500k VND', 500000.00, '60 DAYS', 2800, 80, '2025-09-01', '2025-11-01', 'PENDING', '2025-08-25 12:00:00', 'special-occasions', '2025-08-25 12:00:00', 'special-occasions', 0),
    (39, 'Retirement Celebration Pack - 1M VND', 1000000.00, '120 DAYS', 6000, 40, '2025-10-01', '2026-02-01', 'PENDING', '2025-08-25 13:00:00', 'special-occasions', '2025-08-25 13:00:00', 'special-occasions', 0),
    (40, 'Reunion Special Pack - 450k VND', 450000.00, '75 DAYS', 2500, 100, '2025-09-15', '2025-12-01', 'PENDING', '2025-08-25 14:00:00', 'special-occasions', '2025-08-25 14:00:00', 'special-occasions', 0),
    
    -- LOYALTY TIER PACKS (Mixed discount types) - Mixed dates
    (41, 'Bronze Member Pack - 150k VND', 150000.00, '25 DAYS', 500, 600, '2025-08-19', '2025-09-13', 'PUBLISHED', '2025-08-30 08:00:00', 'loyalty-program', '2025-08-30 08:00:00', 'loyalty-program', 0),
    (42, 'Silver Member Pack - 300k VND', 300000.00, '40 DAYS', 1500, 300, '2025-08-25', '2025-10-05', 'PUBLISHED', '2025-08-30 09:00:00', 'loyalty-program', '2025-08-30 09:00:00', 'loyalty-program', 0),
    (43, 'Gold Member Pack - 600k VND', 600000.00, '60 DAYS', 3500, 150, '2025-09-15', '2025-11-15', 'PENDING', '2025-08-30 10:00:00', 'loyalty-program', '2025-08-30 10:00:00', 'loyalty-program', 0),
    (44, 'Platinum Member Pack - 1M VND', 1000000.00, '90 DAYS', 7000, 80, '2025-10-01', '2025-12-31', 'PENDING', '2025-08-30 11:00:00', 'loyalty-program', '2025-08-30 11:00:00', 'loyalty-program', 0),
    (45, 'Diamond Member Pack - 2M VND', 2000000.00, '120 DAYS', 15000, 30, '2025-11-01', '2026-03-01', 'PENDING', '2025-08-30 12:00:00', 'loyalty-program', '2025-08-30 12:00:00', 'loyalty-program', 0),
    
    -- QUANTITY-BASED PACKS (Mixed discount types) - Mixed dates
    (46, 'Limited Edition Pack - 800k VND', 800000.00, '45 DAYS', 4000, 25, '2025-09-01', '2025-10-16', 'PENDING', '2025-08-30 13:00:00', 'limited-editions', '2025-08-30 13:00:00', 'limited-editions', 0),
    (47, 'Exclusive VIP Pack - 1.5M VND', 1500000.00, '90 DAYS', 8000, 15, '2025-10-01', '2025-12-31', 'PENDING', '2025-08-30 14:00:00', 'vip-exclusive', '2025-08-30 14:00:00', 'vip-exclusive', 0),
    (48, 'Bulk Purchase Pack - 250k VND', 250000.00, '30 DAYS', 1000, 1000, '2025-08-26', '2025-09-26', 'PUBLISHED', '2025-08-30 15:00:00', 'bulk-sales', '2025-08-30 15:00:00', 'bulk-sales', 0),
    (49, 'Flash Sale Pack - 700k VND', 700000.00, '15 DAYS', 3500, 200, '2025-08-21', '2025-09-05', 'PUBLISHED', '2025-08-30 16:00:00', 'flash-sales', '2025-08-30 16:00:00', 'flash-sales', 0),
    (50, 'Grand Opening Pack - 1.2M VND', 1200000.00, '180 DAYS', 10000, 100, '2025-09-01', '2026-03-01', 'PENDING', '2025-08-30 17:00:00', 'grand-opening', '2025-08-30 17:00:00', 'grand-opening', 0);

-- Note: No voucher records are created - only VoucherPack records
-- Vouchers will be generated dynamically when customers redeem packs

-- DATE BREAKDOWN (Relative to August 24, 2025):
-- EXPIRED (Past): Records 1-5 (2024-2025 dates)
-- ACTIVE (Present): Records 6-10, 16-17, 21-22, 26, 32-33, 37, 41-42, 48-49
-- PENDING (Future): Records 11-15, 18-20, 23-25, 27-31, 34-36, 38-40, 43-47, 50

-- DISCOUNT TYPE BREAKDOWN:
-- Records 1-15: Percentage discounts (5% to 150%)
-- Records 16-50: Fixed cash amounts in VND (100k to 2M VND)
