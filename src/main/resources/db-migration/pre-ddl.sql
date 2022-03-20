CREATE TABLE item
(
    id          INTEGER     NOT NULL AUTO_INCREMENT,
    name        VARCHAR(45) NOT NULL,
    description VARCHAR(1024),
    created     DATE,
    updated     DATE,
    version     INTEGER,
    CONSTRAINT pk_item PRIMARY KEY (id)
);

CREATE TABLE user
(
    id                      INTEGER      NOT NULL AUTO_INCREMENT,
    username                VARCHAR(45)  NOT NULL,
    first_name              VARCHAR(45)  NOT NULL,
    last_name               VARCHAR(45)  NOT NULL,
    email                   VARCHAR(255) NOT NULL,
    password                VARCHAR(255) NOT NULL,
    created                 DATETIME,
    updated                 DATETIME,
    version                 INTEGER,
    account_status          VARCHAR(45)  NOT NULL,
    account_non_expired     BOOLEAN,
    account_non_locked      BOOLEAN,
    credentials_non_expired BOOLEAN,
    enabled                 BOOLEAN,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

CREATE TABLE menu
(
    id            INTEGER     NOT NULL AUTO_INCREMENT,
    day           VARCHAR(45) NOT NULL,
    meal_time     VARCHAR(45) NOT NULL,
    meal_date     DATE,
    head_count    INTEGER,
    created_by_id INTEGER,
    updated_by_id INTEGER,
    created       DATE,
    updated       DATE,
    version       INTEGER,
    CONSTRAINT pk_menu PRIMARY KEY (id),
    CONSTRAINT fk_menu_created_by_id FOREIGN KEY (created_by_id) REFERENCES user (id),
    CONSTRAINT fk_menu_updated_by_id FOREIGN KEY (updated_by_id) REFERENCES user (id)
);

CREATE TABLE user_notif_list
(
    user_id INTEGER      NOT NULL,
    notif   VARCHAR(255) NOT NULL,
    idx     INTEGER      NOT NULL,
    CONSTRAINT pk_cmp_user_notif_id PRIMARY KEY (user_id, idx),
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES user (id)
);

CREATE TABLE user_roles
(
    user_id INTEGER      NOT NULL,
    role    VARCHAR(255) NOT NULL,
    idx     INTEGER      NOT NULL,
    CONSTRAINT pk_cmp_user_role_id PRIMARY KEY (user_id, idx),
    CONSTRAINT fk_role_user_id FOREIGN KEY (user_id) REFERENCES user (id)
);

CREATE TABLE menu_item
(
    menu_id INTEGER NOT NULL,
    item_id INTEGER NOT NULL,
    CONSTRAINT pk_menu_item PRIMARY KEY (menu_id, item_id)
);

CREATE TABLE account_confirmation_token
(
    id         INTEGER      NOT NULL AUTO_INCREMENT,
    token      VARCHAR(255) NOT NULL,
    created    DATETIME,
    updated    DATETIME,
    version    INTEGER,
    expiration DATETIME,
    user_id    INTEGER,
    CONSTRAINT pk_account_confirmation PRIMARY KEY (id),
    CONSTRAINT fk_account_confirmation_user_id FOREIGN KEY (user_id) REFERENCES user (id)
);

CREATE TABLE password_reset_token
(
    id                 INTEGER      NOT NULL AUTO_INCREMENT,
    token              VARCHAR(255) NOT NULL,
    created            DATETIME,
    updated            DATETIME,
    version            INTEGER,
    expiration         DATETIME,
    user_id            INTEGER,
    CONSTRAINT pk_password_reset PRIMARY KEY (id),
    CONSTRAINT fk_password_reset_user_id FOREIGN KEY (user_id) REFERENCES user (id)
);