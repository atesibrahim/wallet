INSERT INTO customer (id, name, surname, tckn, create_by, create_date, update_by, update_date)
VALUES ('1', 'Alice', 'Smith', '12345678901', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP),
       ('2', 'Bob', 'Johnson', '23456789012', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP),
       ('2', 'Bob', 'Johnson', '23456789012',  'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP);

INSERT INTO wallet (id, wallet_name, currency, active_for_shopping, active_for_withdraw, balance, usable_balance, customer_id, create_by, create_date, update_by, update_date)
VALUES ('1', 'AliceWalletTRY', 'TRY', true, true, 1000.00, 800.00,'1', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP),
       ('2', 'BobWalletUSD', 'USD', false, true, 2000.00, 1500.00,'2', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP);

INSERT INTO transaction (id, wallet_id, amount, type, opposite_party_type, opposite_party, status, create_by, create_date, update_by, update_date)
VALUES ('1', '1', 300.00, 'DEPOSIT', 'IBAN', 'TR123456', 'APPROVED', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP),
       ('2', '2', 1200.00, 'WITHDRAW', 'PAYMENT', 'PAY9988', 'PENDING', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP);
