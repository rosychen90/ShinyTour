CREATE TABLE visit
(
	v_id 			INT NOT NULL,
	u_no 		INT NOT NULL,
	v_no 		INT NOT NULL,
	v_date 			TIMESTAMP,
	PRIMARY KEY(v_id),
	KEY FK_visit_1(u_no),
	KEY FK_visit_2(v_no),
	constraint FK_visit_1 FOREIGN KEY(u_no) REFERENCES users(u_no),
	constraint FK_visit_2 FOREIGN KEY(v_no) REFERENCES users(u_no)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;