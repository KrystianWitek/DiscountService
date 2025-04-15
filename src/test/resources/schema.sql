DROP TABLE IF EXISTS PRODUCTS;
DROP TABLE IF EXISTS DISCOUNT_POLICY_CONFIGS;

CREATE TABLE PRODUCTS
(
    id    VARCHAR(36) PRIMARY KEY,
    name  VARCHAR(255),
    price DECIMAL(10, 2)
);

CREATE TABLE DISCOUNT_POLICY_CONFIGS
(
    id                    VARCHAR(36) PRIMARY KEY,
    start_threshold_limit INTEGER,
    end_threshold_limit   INTEGER,
    discount_rate         DECIMAL(10, 2),
    type                  VARCHAR(20)
);

INSERT IGNORE INTO PRODUCTS (id, name, price)
VALUES ('f2f9c22d-49c1-4c3a-800f-8df891be1cf3', 'Test', 140.99);

INSERT IGNORE INTO DISCOUNT_POLICY_CONFIGS (id, start_threshold_limit, end_threshold_limit, discount_rate, type)
VALUES ('14fa9f9e-d301-4959-833c-fbed4000948d', 1, 9, 0, 'QUANTITY'),
       ('8f280403-ba17-4f13-8232-cbced3827f9d', 10, 19, 5, 'QUANTITY'),
       ('1b1ffc00-f926-4b63-9ca9-c1c3cbebb91a', 20, 49, 10, 'QUANTITY'),
       ('af2a7a3f-76e4-4809-8c91-ae89ca4e3b1c', 50, 9999999, 15, 'QUANTITY'),
       ('ca66ea2c-adeb-4eb6-bbff-a4877f5e11af', null, null, 4, 'PERCENTAGE')
;