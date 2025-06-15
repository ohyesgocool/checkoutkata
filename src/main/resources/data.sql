-- Items table
CREATE TABLE IF NOT EXISTS item (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    item_name VARCHAR(255) NOT NULL,
    unit_price INTEGER NOT NULL
    );

-- Offers table
CREATE TABLE IF NOT EXISTS offer (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     item_id BIGINT NOT NULL,
                                     quantity INTEGER NOT NULL,
                                     offer_price INTEGER NOT NULL,
                                     FOREIGN KEY (item_id) REFERENCES item(id)
    );

-- Seed Items
INSERT INTO item (item_name, unit_price) VALUES ('apple', 30);
INSERT INTO item (item_name, unit_price) VALUES ('banana', 50);
INSERT INTO item (item_name, unit_price) VALUES ('peach', 60);
INSERT INTO item (item_name, unit_price) VALUES ('kiwi', 20);
INSERT INTO item (item_name, unit_price) VALUES ('orange', 40);
INSERT INTO item (item_name, unit_price) VALUES ('pineapple', 100);
INSERT INTO item (item_name, unit_price) VALUES ('mango', 80);
INSERT INTO item (item_name, unit_price) VALUES ('grapes', 10);

-- Seed Offers
INSERT INTO offer (item_name, quantity, offer_price) VALUES ('apple', 2, 45);
INSERT INTO offer (item_name, quantity, offer_price) VALUES ('banana', 3, 130);
INSERT INTO offer (item_name, quantity, offer_price) VALUES ('peach', 2, 100);
INSERT INTO offer (item_name, quantity, offer_price) VALUES ('kiwi', 5, 90);
INSERT INTO offer (item_name, quantity, offer_price) VALUES ('grapes', 2, 15);