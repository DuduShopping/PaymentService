CREATE TABLE stripe_customers (
  user_id BIGINT UNIQUE NOT NULL,
  customer_id VARCHAR (50) UNIQUE NOT NULL,
  -- indicate if an error happens, that needs to be taken care of. 0 means unlock.
  locked_reason_code INT NOT NULL DEFAULT 0,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);