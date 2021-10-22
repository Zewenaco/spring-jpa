DROP TABLE IF EXISTS PRICE;
DROP TABLE IF EXISTS BRAND;

CREATE TABLE BRAND (
    ID BIGINT AUTO_INCREMENT  PRIMARY KEY,
    NAME VARCHAR(255) NOT NULL
);
CREATE TABLE PRICE (
      ID BIGINT AUTO_INCREMENT  PRIMARY KEY,
      BRAND_ID BIGINT NOT NULL,
      START_DATE TIMESTAMP NOT NULL,
      END_DATE TIMESTAMP NOT NULL,
      PRICE_LIST INT NOT NULL,
      PRODUCT_ID INT NOT NULL,
      PRIORITY INT NOT NULL,
      PRICE DECIMAL NOT NULL,
      CURR VARCHAR NOT NULL,
      foreign key (BRAND_ID) references BRAND(ID)
);

INSERT INTO BRAND (NAME) VALUES ('Zara'),
                                ('Pull&Bear');

INSERT INTO PRICE (BRAND_ID,
                    START_DATE,
                    END_DATE,
                    PRICE_LIST,
                    PRODUCT_ID,
                    PRIORITY,
                    PRICE,
                    CURR) VALUES (1, '2020-06-14 00:00:00', '2020-12-31 23:59:59', 1, 35455, 0, 35.50, 'EUR'),
                                 (1, '2020-06-14 15:00:00', '2020-06-14 18:30:00', 2, 35455, 1, 25.45, 'EUR'),
                                 (1, '2020-06-15 00:00:00', '2020-06-15 11:00:00', 3, 35455, 1, 30.50, 'EUR'),
                                 (1, '2020-06-15 16:00:00', '2020-12-31 23:59:59', 4, 35455, 1, 38.95, 'EUR');


-- BRAND_ID         START_DATE                                    END_DATE                        PRICE_LIST                   PRODUCT_ID  PRIORITY                 PRICE           CURR
-- ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
-- 1         2020-06-14-00.00.00                        2020-12-31-23.59.59                        1                        35455                0                        35.50            EUR
-- 1         2020-06-14-15.00.00                        2020-06-14-18.30.00                        2                        35455                1                        25.45            EUR
-- 1         2020-06-15-00.00.00                        2020-06-15-11.00.00                        3                        35455                1                        30.50            EUR
-- 1         2020-06-15-16.00.00                        2020-12-31-23.59.59                        4                        35455                1                        38.95            EUR

