CREATE TABLE StripeSources (
  UserId BIGINT NOT NULL,
  SourceId VARCHAR(100) NOT NULL UNIQUE,
  IsDefault INT NOT NULL DEFAULT 0,
  CreatedAt DATETIME NOT NULL DEFAULT SYSDATETIME(),

  -- meta data meant for users to identify payment source
  LastFour CHAR(4) NULL,
  ExpMonth INT NULL,
  ExpYear INT NULL,
  Funding VARCHAR(10) NULL, -- Card funding type. Can be credit, debit, prepaid, or unknown.
  Brand VARCHAR(20) NULL
)