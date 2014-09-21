CREATE TABLE comment(/*评论comment表的创建*/
	c_id 			INT NOT NULL,
	c_content		TEXT NOT NULL,
	u_no 			INT NOT NULL,
	r_id 			INT NOT NULL,
	c_date 		TIMESTAMP,
	PRIMARY 	KEY(c_id),
	KEY FK_comment_1(u_no),
	KEY FK_comment_2(r_id),
	constraint FK_comment_1 FOREIGN 	KEY(u_no) REFERENCES users(u_no),
	constraint FK_comment_2 FOREIGN 	KEY(r_id) REFERENCES diary(r_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;