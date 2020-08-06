CREATE TABLE promotion (
	id INT PRIMARY KEY,
	promotion_id TEXT UNIQUE,
	barcode TEXT,
	description TEXT,
	details TEXT,
	valid_from DATE NOT NULL,
	expires_at DATE,
	valid_for DATE,
	max_redemption_by_coupon INT NOT NULL,
	inserted_by TEXT,
	inserted_at DATE
);