CREATE TABLE IF NOT EXISTS restaurant(

    restaurant_id BIGINT AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    web_url VARCHAR(255) NOT NULL,
    phone_number VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL ,
    img_url VARCHAR(255) ,
    wish_count INTEGER DEFAULT 0,
    created_date DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_date DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    PRIMARY KEY (restaurant_id)

    );

CREATE TABLE IF NOT EXISTS menu(

    menu_id BIGINT AUTO_INCREMENT,
    restaurant_id BIGINT,
    menu_name VARCHAR(255) NOT NULL ,
    menu_price VARCHAR(255) NOT NULL ,
    created_date DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_date DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    PRIMARY KEY (menu_id),
    FOREIGN KEY (restaurant_id) REFERENCES restaurant(restaurant_id)
    );

CREATE TABLE IF NOT EXISTS category(

    category_id BIGINT AUTO_INCREMENT,
    category_name VARCHAR(255) NOT NULL ,
    category_img_url VARCHAR(255) NOT NULL,
    created_date DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_date DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    PRIMARY KEY (category_id)
    );

CREATE TABLE IF NOT EXISTS restaurant_category(
    restaurant_category_id BIGINT AUTO_INCREMENT,
    restaurant_id BIGINT,
    category_id BIGINT,
    PRIMARY KEY (restaurant_category_id),
    FOREIGN KEY (restaurant_id) REFERENCES restaurant(restaurant_id),
    FOREIGN KEY (category_id) REFERENCES category(category_id)

    );

CREATE TABLE IF NOT EXISTS member(

    member_id BIGINT AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL,
    nickname VARCHAR(255) NOT NULL,
    created_date DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_date DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    PRIMARY KEY (member_id)

    );

CREATE TABLE IF NOT EXISTS member_token(

    member_token_id BIGINT AUTO_INCREMENT,
    member_id BIGINT,
    access_token VARCHAR(255) NOT NULL ,
    access_expiration DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    refresh_token VARCHAR(255) NOT NULL ,
    refresh_expiration DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    PRIMARY KEY (member_token_id),
    FOREIGN KEY (member_id) REFERENCES member(member_id)
    );

CREATE TABLE IF NOT EXISTS wishlist(

    wishlist_id BIGINT AUTO_INCREMENT,
    restaurant_category_id BIGINT,
    member_id BIGINT,
    created_date DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    PRIMARY KEY (wishlist_id),
    FOREIGN KEY (restaurant_category_id) REFERENCES restaurant_category(restaurant_category_id),
    FOREIGN KEY (member_id) REFERENCES member(member_id)
);

ALTER TABLE wishlist DROP COLUMN restaurant_id;
ALTER TABLE wishlist DROP COLUMN category_id;

ALTER TABLE wishlist
ADD COLUMN restaurant_category_id BIGINT;
ALTER TABLE wishlist
ADD FOREIGN KEY (restaurant_category_id) REFERENCES restaurant_category(restaurant_category_id);