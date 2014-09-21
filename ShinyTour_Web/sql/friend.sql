/*好友列表friend的创建*/
CREATE TABLE friend
(
	f_id			INT NOT NULL,
	u_noz			INT NOT NULL,
	u_noy 		INT NOT NULL,
	f_date 		TIMESTAMP,
	PRIMARY KEY(f_id),
	KEY FK_friend_1(u_noz),
	KEY FK_friend_2(u_noy),
	constraint FK_friend_1 FOREIGN KEY(u_noz) REFERENCES users(u_no),
	constraint FK_friend_2 FOREIGN KEY(u_noy) REFERENCES users(u_no)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;