-- =====================================================
-- V1: Initial schema for DVH
-- Order matters: users -> vendors -> vehicles
-- (vendors, users ko refer karta hai; vehicles, vendors ko)
-- =====================================================

-- 1) USERS: sabse pehle, kyunki yeh kisi ko refer nahi karta
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,              -- primary key, DB khud badhata hai
  `email` varchar(40) NOT NULL,
  `name` varchar(40) NOT NULL,
  `password_hash` varchar(255) NOT NULL,            -- BCrypt hash yahan store hota hai
  `phone` varchar(10) NOT NULL,
  `role` enum('ADMIN','CUSTOMER','VENDOR') NOT NULL,
  `user_status` enum('ACTIVE','BLOCKED','INACTIVE') NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `updated_at` datetime(6) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_email` (`email`),             -- ek email se ek hi account
  UNIQUE KEY `uk_user_phone` (`phone`)              -- ek phone se ek hi account
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 2) VENDORS: users ke baad, kyunki user_id se users ko refer karta hai
CREATE TABLE `vendors` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `business_address` varchar(255) NOT NULL,
  `business_name` varchar(100) NOT NULL,
  `city` varchar(30) NOT NULL,
  `gst_number` varchar(15) DEFAULT NULL,            -- optional hai, isliye NULL allowed
  `verified` bit(1) NOT NULL,                       -- boolean: admin ne verify kiya ya nahi
  `user_id` bigint NOT NULL,                        -- yeh vendor kis user ka hai
  `created_at` datetime(6) NOT NULL,
  `updated_at` datetime(6) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_vendor_user_id` (`user_id`),       -- ek user ka sirf ek vendor profile (OneToOne)
  UNIQUE KEY `uk_vendor_gst_number` (`gst_number`), -- GST number duplicate nahi ho sakta
  CONSTRAINT `fk_vendor_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 3) VEHICLES: sabse last, kyunki vendor_id se vendors ko refer karta hai
CREATE TABLE `vehicles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `available` bit(1) NOT NULL,                      -- boolean: abhi rent ke liye available hai ya nahi
  `brand` varchar(20) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  `fuel_type` enum('CNG','DIESEL','ELECTRIC','PETROL') NOT NULL,
  `manufacturing_year` int NOT NULL,
  `model` varchar(20) DEFAULT NULL,
  `seating_capacity` int NOT NULL,
  `transmission_type` enum('AUTOMATIC','MANUAL') NOT NULL,
  `vehicle_number` varchar(10) NOT NULL,
  `vehicle_type` enum('BIKE','CAR','SCOOTER') NOT NULL,
  `vendor_id` bigint NOT NULL,                      -- yeh gaadi kis vendor ki hai
  `created_at` datetime(6) NOT NULL,
  `updated_at` datetime(6) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_vehicle_number` (`vehicle_number`),-- same number plate do baar nahi
  KEY `idx_vehicle_vendor_id` (`vendor_id`),        -- vendor ki gaadiyan jaldi dhundhne ke liye index
  CONSTRAINT `fk_vehicle_vendor` FOREIGN KEY (`vendor_id`) REFERENCES `vendors` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;