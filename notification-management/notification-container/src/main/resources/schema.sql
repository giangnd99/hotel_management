DROP SCHEMA IF EXISTS notification_db CASCADE;
CREATE SCHEMA notification_db;
SET search_path TO notification_db;

-- Bảng QR Codes
CREATE TABLE IF NOT EXISTS qr_codes (
                                        id BIGSERIAL PRIMARY KEY,
                                        data VARCHAR(255) NOT NULL,
                                        qr_code_image_path TEXT,
                                        width INT NOT NULL DEFAULT 300,
                                        height INT NOT NULL DEFAULT 300,
                                        format VARCHAR(10) NOT NULL DEFAULT 'PNG',
                                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                        is_active BOOLEAN DEFAULT TRUE,
                                        description TEXT,
                                        scanned_at TIMESTAMP,
                                        is_scanned BOOLEAN  DEFAULT FALSE,

    -- Constraints
                                        CONSTRAINT chk_width CHECK (width BETWEEN 100 AND 1000),
                                        CONSTRAINT chk_height CHECK (height BETWEEN 100 AND 1000)
);

-- Bảng Notifications
CREATE TABLE IF NOT EXISTS notifications (
                                             id BIGSERIAL PRIMARY KEY,
                                             user_id VARCHAR(255) NOT NULL,
                                             notification_method VARCHAR(10) NOT NULL,
                                             message TEXT NOT NULL,
                                             priority VARCHAR(10),
                                             status VARCHAR(10) NOT NULL,
                                             created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                             updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);