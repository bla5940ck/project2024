CREATE TABLE IF NOT EXISTS product
(
    product_id         INT          NOT NULL PRIMARY KEY AUTO_INCREMENT,
    product_name       VARCHAR(128)  NOT NULL,
    category           VARCHAR(32)  NOT NULL,
    image_url          VARCHAR(256) NOT NULL,
    price              INT          NOT NULL,
    stock              INT          NOT NULL,
    description        VARCHAR(1024),
    created_date       TIMESTAMP    NOT NULL,
    last_modified_date TIMESTAMP    NOT NULL
);

CREATE TABLE IF NOT EXISTS member
(
    member_id            INT          NOT NULL PRIMARY KEY AUTO_INCREMENT,
    age                  INT  ,
    email              VARCHAR(256) NOT NULL UNIQUE,
    password           VARCHAR(256) NOT NULL,
    name               VARCHAR(256) NOT NULL,
    created_date       TIMESTAMP    NOT NULL,
    last_modified_date TIMESTAMP    NOT NULL
);


CREATE TABLE IF NOT EXISTS member_has_role
(
    member_has_role_id            INT          NOT NULL PRIMARY KEY AUTO_INCREMENT,
    member_id                  INT  NOT NULL,
    role_id                    INT NOT NULL
);

CREATE TABLE IF NOT EXISTS role
(
    role_id            INT          NOT NULL PRIMARY KEY,
    role_name          VARCHAR(255)  NOT NULL
);
