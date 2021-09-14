CREATE TABLE watchlist
(
    account_id VARCHAR(32),
    asset      VARCHAR(32),
    FOREIGN KEY (asset) REFERENCES broker.assets (value),
    PRIMARY KEY (account_id, asset)
);
