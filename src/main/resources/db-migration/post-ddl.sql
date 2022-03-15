ALTER TABLE menu_item
    ADD
    (
        CONSTRAINT fk_menu_item_menu_id FOREIGN KEY (menu_id) REFERENCES menu (id),
        CONSTRAINT fk_menu_item_item_id FOREIGN KEY (item_id) REFERENCES item (id)
    );