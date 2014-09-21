CREATE TABLE diary(/*日记diary表的创建*/
	r_id 				INT NOT NULL,
	r_title			VARCHAR(18) NOT NULL,
	r_content 	TEXT NOT NULL,
	r_date 			TIMESTAMP,
	u_no 				INT NOT NULL,
	PRIMARY 		KEY(r_id),
	key FK_diary_1(u_no),
	CONSTRAINT FK_diary_1 FOREIGN KEY(u_no) REFERENCES users(u_no)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;