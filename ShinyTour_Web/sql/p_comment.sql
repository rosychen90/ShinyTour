CREATE TABLE p_comment(/*’’∆¨∆¿¬€p_comment±Ì*/
	c_id		INT NOT NULL,
	c_content	TEXT NOT NULL,
	u_no		INT NOT NULL,
	p_id		INT NOT NULL,
	c_date		TIMESTAMP,
	PRIMARY KEY(c_id),
	KEY FK_p_comment_1(u_no),
	KEY FK_p_comment_2(p_id),
	constraint FK_p_comment_1 FOREIGN	KEY(u_no) REFERENCES users(u_no),
	constraint FK_p_comment_2 FOREIGN KEY(p_id) REFERENCES photo(p_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;