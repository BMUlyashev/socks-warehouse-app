CREATE TABLE IF NOT EXISTS socks(
  id SERIAL PRIMARY KEY,
  color VARCHAR(255) NOT NULL,
  cotton_part INTEGER NOT NULL CHECK (cotton_part >= 0 AND cotton_part <=100),
  quantity INTEGER NOT NULL CHECK(quantity >= 0)
);