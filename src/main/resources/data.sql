-- Items table
CREATE TABLE IF NOT EXISTS item (
                                    name VARCHAR(255) PRIMARY KEY,
    unit_price INTEGER NOT NULL
    );

-- Offers table
CREATE TABLE IF NOT EXISTS offer (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     item_name VARCHAR(255) NOT NULL,
    quantity INTEGER NOT NULL,
    offer_price INTEGER NOT NULL,
    FOREIGN KEY (item_name) REFERENCES item(name)
    );

-- Seed Items
INSERT INTO item (name, unit_price) VALUES ('apple', 30);
INSERT INTO item (name, unit_price) VALUES ('banana', 50);
INSERT INTO item (name, unit_price) VALUES ('Peach', 60);
INSERT INTO item (name, unit_price) VALUES ('Kiwi', 20);
INSERT INTO item (name, unit_price) VALUES ('Orange', 40);
INSERT INTO item (name, unit_price) VALUES ('Pineapple', 100);
INSERT INTO item (name, unit_price) VALUES ('Mango', 80);
INSERT INTO item (name, unit_price) VALUES ('Grapes', 35);

-- Seed Offers
INSERT INTO offer (item_name, quantity, offer_price) VALUES ('apple', 2, 45);         -- 2 for 45 instead of 60
INSERT INTO offer (item_name, quantity, offer_price) VALUES ('banana', 3, 130);       -- 3 for 130 instead of 150
INSERT INTO offer (item_name, quantity, offer_price) VALUES ('Peach', 2, 100);        -- 2 for 100 instead of 120
INSERT INTO offer (item_name, quantity, offer_price) VALUES ('Kiwi', 5, 90);          -- 5 for 90 instead of 100
INSERT INTO offer (item_name, quantity, offer_price) VALUES ('Grapes', 4, 120);       -- 4 for 120 instead of 140


SELECT * FROM item;
SELECT * FROM offer;
