CREATE OR REPLACE FUNCTION random_number_account()
  RETURNS SETOF text
RETURN cast(cast(1000000000000000 + floor(random() * 9000000000000000) as decimal(16,0)) as varchar);

CREATE OR REPLACE FUNCTION random_balance_account()
  RETURNS SETOF decimal(12,2)
RETURN cast(100000 + random() * 900000 as decimal(12,2));

CREATE SEQUENCE IF NOT EXISTS account_sq;
CREATE SEQUENCE IF NOT EXISTS transfer_sq;

CREATE TYPE ACCOUNT_TYPE AS ENUM ('SAVINGS_ACCOUNT', 'CHECKING_ACCOUNT');
CREATE TYPE ACCOUNT_STATE AS ENUM ('ACTIVE', 'INACTIVE');

CREATE TABLE IF NOT EXISTS account
(
    id integer NOT NULL DEFAULT nextval('account_sq') PRIMARY KEY,
    "number" varchar(16) NOT NULL,
    balance numeric(12, 2) NOT NULL CHECK (balance > 0),
    "type" ACCOUNT_TYPE NOT NULL,
    state ACCOUNT_STATE NOT NULL,
    creation_date timestamp NOT NULL,
    modification_date timestamp NOT NULL,
    CONSTRAINT number_length_check CHECK(length("number") = 16),
    CONSTRAINT number_unique UNIQUE("number")
);

CREATE INDEX idx_account ON account ("number");

INSERT INTO account("number", balance, "type", state, creation_date, modification_date)
values(random_number_account(), random_balance_account(), 'SAVINGS_ACCOUNT', 'ACTIVE', NOW(), NOW());
INSERT INTO account("number", balance, "type", state, creation_date, modification_date)
values(random_number_account(), random_balance_account(), 'CHECKING_ACCOUNT', 'ACTIVE', NOW(), NOW());
INSERT INTO account("number", balance, "type", state, creation_date, modification_date)
values(random_number_account(), random_balance_account(), 'CHECKING_ACCOUNT', 'ACTIVE', NOW(), NOW());
INSERT INTO account("number", balance, "type", state, creation_date, modification_date)
values(random_number_account(), random_balance_account(), 'SAVINGS_ACCOUNT', 'INACTIVE', NOW(), NOW());

CREATE TABLE IF NOT EXISTS transfer
(
    id integer NOT NULL DEFAULT nextval('transfer_sq') PRIMARY KEY,
    amount numeric(12, 2) NOT NULL CHECK (amount > 0),
    account_from varchar(16) NOT NULL,
    account_to varchar(16) NOT NULL,
    "date" timestamp NOT NULL,
    FOREIGN KEY (account_from) REFERENCES account("number"),
    FOREIGN KEY (account_to) REFERENCES account("number")
 );

CREATE INDEX idx_transfer_from_historic ON transfer (account_from);
CREATE INDEX idx_transfer_to_historic ON transfer (account_to);
CREATE INDEX idx_transfer_historic ON transfer (account_from, account_to, "date" DESC);
