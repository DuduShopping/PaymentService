CREATE TABLE StripeCharges (
  UserId BIGINT NOT NULL,
  OrderId BIGINT UNIQUE NOT NULL,
  -- multiple of lowest face value. for example, usd, it will be number of cents.
  Amount BIGINT NOT NULL DEFAULT 0,
  Currency VARCHAR(5) NOT NULL DEFAULT 'USD',
  StripeChargeToken VARCHAR(50) NOT NULL UNIQUE,
  Status INT NOT NULL DEFAULT 0, -- TODO Do i need this
  ChargedAt DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
)