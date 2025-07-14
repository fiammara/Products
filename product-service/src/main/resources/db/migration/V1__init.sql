use products;

DROP TABLE IF EXISTS product;
CREATE TABLE product (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    description VARCHAR(255),
    quantity INT NOT NULL,
    initial_quantity INT,
    price DOUBLE NOT NULL,
    category VARCHAR(255),
    version BIGINT NOT NULL DEFAULT 0
);
