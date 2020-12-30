CREATE TABLE assets
(
    VALUE VARCHAR PRIMARY KEY
);

CREATE TABLE quotes
(
    id         SERIAL PRIMARY KEY,
    bid        NUMERIC,
    ask        NUMERIC,
    last_price NUMERIC,
    volume     NUMERIC,
    asset      VARCHAR,
    FOREIGN KEY (asset) REFERENCES assets (value),
    CONSTRAINT last_price_is_positive CHECK (last_price > 0),
    CONSTRAINT volume_is_positive_or_zero CHECK (volume >= 0)
);
