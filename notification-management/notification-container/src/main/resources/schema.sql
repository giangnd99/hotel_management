DROP SCHEMA IF EXISTS notification_db CASCADE;
CREATE SCHEMA notification_db;
SET search_path TO notification_db;

CREATE TABLE IF NOT EXISTS qr_codes (
    id BIGSERIAL PRIMARY KEY,
    data VARCHAR(255) NOT NULL,
    booking_id VARCHAR(255) NOT NULL,
    qr_code_image_path TEXT,
    width INT NOT NULL DEFAULT 300,
    height INT NOT NULL DEFAULT 300,
    format VARCHAR(10) NOT NULL DEFAULT 'PNG',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    description TEXT,
    scanned_at TIMESTAMP NULL,
    is_scanned BOOLEAN NOT NULL DEFAULT FALSE,

    -- Constraints
    CONSTRAINT chk_width CHECK (width BETWEEN 100 AND 1000),
    CONSTRAINT chk_height CHECK (height BETWEEN 100 AND 1000)
    );

CREATE TABLE IF NOT EXISTS notifications (
    id BIGSERIAL PRIMARY KEY,
    booking_id VARCHAR(255) NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    notification_method VARCHAR(10) NOT NULL,
    message TEXT NOT NULL,
    priority VARCHAR(10),
    status VARCHAR(10) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );


CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = now();
RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_qr_codes_updated_at BEFORE UPDATE ON qr_codes FOR EACH ROW EXECUTE PROCEDURE update_updated_at_column();
CREATE TRIGGER update_notifications_updated_at BEFORE UPDATE ON notifications FOR EACH ROW EXECUTE PROCEDURE update_updated_at_column();

CREATE INDEX idx_qr_codes_data ON qr_codes (data);
CREATE INDEX idx_qr_codes_booking_id ON qr_codes (booking_id);
CREATE INDEX idx_qr_codes_is_active ON qr_codes (is_active);
CREATE INDEX idx_qr_codes_is_scanned ON qr_codes (is_scanned);

CREATE INDEX idx_notifications_booking_id ON notifications (booking_id);
CREATE INDEX idx_notifications_user_id ON notifications (user_id);
CREATE INDEX idx_notifications_status ON notifications (status);