CREATE TABLE stripe_sources (
  user_id BIGINT NOT NULL,
  source_id VARCHAR(100) NOT NULL UNIQUE,
  is_default INT NOT NULL DEFAULT 0,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

  -- meta data meant for users to identify payment source
  last_four CHAR(4) NULL,
  exp_month INT NULL,
  exp_year INT NULL,
  funding VARCHAR(10) NULL, -- Card funding type. Can be credit, debit, prepaid, or unknown.
  brand VARCHAR(20) NULL
)