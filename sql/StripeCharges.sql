CREATE TABLE stripe_charges(
  user_id BIGINT UNIQUE NOT NULL,
  order_id BIGINT UNIQUE NOT NULL,
  -- multiple of lowest face value. for example, usd, it will be number of cents.
  amount BIGINT NOT NULL DEFAULT 0,
  currency VARCHAR(5) NOT NULL DEFAULT 'USD',
  stripe_charge_token VARCHAR(50) NOT NULL,
  status INT NOT NULL DEFAULT 0, -- TODO Do i need this
  charged_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
)