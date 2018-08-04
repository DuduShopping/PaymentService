CREATE TABLE orders (
  order_id BIGINT NOT NULL UNIQUE,
  payment_due BIGINT NOT NULL
)