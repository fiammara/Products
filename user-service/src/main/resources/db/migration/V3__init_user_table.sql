
CREATE TABLE IF NOT EXISTS `user` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    phone VARCHAR(255),
    role VARCHAR(100),
    active BOOLEAN NOT NULL
);

INSERT IGNORE INTO `user` (email, password, first_name, last_name, phone, role, active) VALUES
('ra@ra.com', '$2a$10$36krtAA3Oz4mV.LKvaxc9.1Ddz5XTfYoDu/hfluTdN0Nn3Pdz8ykq', 'Ra', 'Admin', '123-456-7890', 'ROLE_USER', true);
