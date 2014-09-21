CREATE TABLE photo(/*照片photo表的创建*/
	p_id 		INT NOT NULL,
	p_name 		VARCHAR(18) NOT NULL,
	p_des		VARCHAR(50) NOT NULL,
	p_data 		MEDIUMBLOB,
	x_id 		INT NOT NULL,
	PRIMARY KEY(p_id),
	key FK_photo_1(x_id),
	constraint FK_photo_1 FOREIGN KEY(x_id) REFERENCES album(x_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;