CREATE TABLE users (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(40) NOT NULL,
    email VARCHAR(40) NOT NULL,
    phone VARCHAR(10) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    `role` VARCHAR(255) NOT NULL,
    user_status VARCHAR(255) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uk_user_email UNIQUE (email),
    CONSTRAINT uk_user_phone UNIQUE (phone)
) ENGINE=InnoDB;

CREATE TABLE vendors (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    business_name VARCHAR(100) NOT NULL,
    business_address VARCHAR(255) NOT NULL,
    city VARCHAR(30) NOT NULL,
    gst_number VARCHAR(15),
    verified BIT(1) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    CONSTRAINT pk_vendors PRIMARY KEY (id),
    CONSTRAINT uk_vendor_user_id UNIQUE (user_id),
    CONSTRAINT uk_vendor_gst_number UNIQUE (gst_number),
    CONSTRAINT fk_vendor_user FOREIGN KEY (user_id) REFERENCES users (id)
) ENGINE=InnoDB;

CREATE TABLE vehicles (
    id BIGINT NOT NULL AUTO_INCREMENT,
    vendor_id BIGINT NOT NULL,
    vehicle_number VARCHAR(10) NOT NULL,
    brand VARCHAR(20),
    model VARCHAR(20),
    vehicle_type VARCHAR(255) NOT NULL,
    transmission_type VARCHAR(255) NOT NULL,
    fuel_type VARCHAR(255) NOT NULL,
    manufacturing_year INT NOT NULL,
    seating_capacity INT NOT NULL,
    description VARCHAR(500),
    available BIT(1) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    CONSTRAINT pk_vehicles PRIMARY KEY (id),
    CONSTRAINT uk_vehicle_number UNIQUE (vehicle_number),
    CONSTRAINT fk_vehicle_vendor FOREIGN KEY (vendor_id) REFERENCES vendors (id)
) ENGINE=InnoDB;
