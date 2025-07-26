
-- Data for roles
INSERT INTO authentication_service.roles (id, name) VALUES
                                 ('ROLE01', 'ROLE_ADMIN'),
                                 ('ROLE02', 'ROLE_CUSTOMER'),
                                 ('ROLE03', 'ROLE_MANAGER'),
                                 ('ROLE04', 'ROLE_RECEPTIONIST');

-- Data for users (example data, adjust as needed)
INSERT INTO authentication_service.users (user_id, email, password, phone, role_id) VALUES
                                                                 ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'admin@example.com', 'hashed_password_admin', '0901234567', 'ROLE01'),
                                                                 ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'user@example.com', 'hashed_password_user', '0912345678', 'ROLE02');

-- Data for token_entity (example data, adjust as needed)
-- INSERT INTO token_entity (id, expiry_time) VALUES
-- ('token_id_1', '2025-12-31 23:59:59'),
-- ('token_id_2', '2025-11-30 12:00:00');