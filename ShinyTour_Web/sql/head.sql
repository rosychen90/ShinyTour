CREATE TABLE head(/*ͷ��head��*/
	h_id INT NOT NULL,
	h_des VARCHAR(40) NOT NULL,
	h_data MEDIUMBLOB NOT NULL,
	u_no INT,
	PRIMARY KEY(h_id),
	key FK_head_1(u_no),
	constraint FK_head_1 FOREIGN KEY(u_no) REFERENCES users(u_no)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
-- ������Ȱ�Ĭ��ͷ����ȥ
