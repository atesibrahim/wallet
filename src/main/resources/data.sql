INSERT INTO customer (id, name, surname, tckn, email, password, authority_type, create_by, create_date, update_by, update_date)
VALUES ('1', 'Alice', 'Smith', '12345678901', 'a@test.com', '$2a$10$ibkpUkiG/pQq5aWt13M5VOUbABiSFt5C6/74TV6d9eSzslmrq3M7y', 'ADMIN', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP),
       ('2', 'Bob', 'Johnson', '23456789012', 'b@test.com', '$2a$10$4HGSug4UK9vCw5Fwbg6kae6mhCmGILcTZ2vfuOJLc9Y1JYWXRLCz2', 'CUSTOMER', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP),
       ('3', 'Bob', 'Johnson', '23456789012',  'c@test.com', '$2a$10$0LDDYZXnES0Xjm0k0RWinORMcAd5FXgzvZpq8Le4A1L9.9nn8CyeW', 'USER', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP);

INSERT INTO wallet (id, wallet_name, currency, active_for_shopping, active_for_withdraw, balance, usable_balance, customer_id, create_by, create_date, update_by, update_date)
VALUES ('TR11234', 'AliceWalletTRY', 'TRY', true, true, 1000.00, 800.00,'1', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP),
       ('ABC2', 'BobWalletUSD', 'USD', false, true, 2000.00, 1500.00,'2', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP);

INSERT INTO transaction (id, wallet_id, amount, type, opposite_party_type, opposite_party, status, create_by, create_date, update_by, update_date)
VALUES ('1', 'TR11234', 300.00, 'DEPOSIT', 'IBAN', 'TR123456', 'APPROVED', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP),
       ('2', 'ABC2', 1200.00, 'WITHDRAW', 'PAYMENT', 'PAY9988', 'PENDING', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP);
