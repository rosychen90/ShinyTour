CREATE TABLE album(/*���album��Ĵ���*/
	x_id 		INT NOT NULL,
	x_name 	VARCHAR(18) NOT NULL,
	u_no 		INT NOT NULL,
	x_access INT DEFAULT 0,						-- 0:������1�����ѣ�2�����Լ��ɼ�
	x_date 	TIMESTAMP,
	PRIMARY KEY(x_id),
	key FK_album_1(u_no),
	constraint FK_album_1 FOREIGN KEY(u_no) REFERENCES users(u_no)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;