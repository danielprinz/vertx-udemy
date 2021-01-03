CREATE TABLE watchlist
(
    account_id VARCHAR,
    asset      VARCHAR,
    FOREIGN KEY (asset) REFERENCES broker.assets (value),
    PRIMARY KEY (account_id, asset)
);
