CREATE TABLE customer
(
    id          BIGINT PRIMARY KEY,
    name        VARCHAR(255),
    surname     VARCHAR(255),
    tckn        VARCHAR(11),
    email       VARCHAR(255),
    password    VARCHAR(255),
    authority_type   VARCHAR(255),
    create_by   VARCHAR(255),
    create_date TIMESTAMP,
    update_by   VARCHAR(255),
    update_date TIMESTAMP
);

CREATE TABLE wallet
(
    id                     VARCHAR(255) PRIMARY KEY,
    wallet_name            VARCHAR(255),
    currency               VARCHAR(3),
    active_for_shopping    BOOLEAN,
    active_for_withdraw    BOOLEAN,
    balance                DECIMAL(19, 4),
    usable_balance         DECIMAL(19, 4),
    customer_id            BIGINT,
    create_by              VARCHAR(255),
    create_date            TIMESTAMP,
    update_by              VARCHAR(255),
    update_date            TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customer(id)
);

CREATE TABLE transaction
(
    id                     BIGINT PRIMARY KEY,
    wallet_id              VARCHAR(255),
    amount                 DECIMAL(19, 4),
    type                   VARCHAR(255),
    opposite_party_type    VARCHAR(255),
    opposite_party         VARCHAR(255),
    status                 VARCHAR(255),
    create_by              VARCHAR(255),
    create_date            TIMESTAMP,
    update_by              VARCHAR(255),
    update_date            TIMESTAMP,
    FOREIGN KEY (wallet_id) REFERENCES wallet(id)
);