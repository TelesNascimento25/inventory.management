CREATE TABLE categories
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE products
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR        NOT NULL,
    description TEXT,
    price       DECIMAL(10, 2) NOT NULL,
    category_id INT            NOT NULL REFERENCES categories (id),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP
);
