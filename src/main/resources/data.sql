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
INSERT INTO offer (item_name, quantity, offer_price) VALUES ('apple', 2, 45);
INSERT INTO offer (item_name, quantity, offer_price) VALUES ('banana', 3, 130);
INSERT INTO offer (item_name, quantity, offer_price) VALUES ('Peach', 2, 100);
INSERT INTO offer (item_name, quantity, offer_price) VALUES ('Kiwi', 5, 90);
INSERT INTO offer (item_name, quantity, offer_price) VALUES ('Grapes', 4, 120);