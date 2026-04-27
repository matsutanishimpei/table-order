CREATE TABLE users (
    id VARCHAR(50) PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    salt VARCHAR(100),
    role INT NOT NULL
);
