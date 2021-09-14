CREATE TABLE assets
(
    VALUE VARCHAR(32),
    PRIMARY KEY (VALUE)
);

CREATE TABLE quotes
(
    id         INT NOT NULL AUTO_INCREMENT,
    bid        NUMERIC,
    ask        NUMERIC,
    last_price NUMERIC,
    volume     NUMERIC,
    asset      VARCHAR(32),
    PRIMARY KEY (id),
    FOREIGN KEY (asset) REFERENCES assets (value),
    CONSTRAINT last_price_is_positive CHECK (last_price > 0),
    CONSTRAINT volume_is_positive_or_zero CHECK (volume >= 0)
);
