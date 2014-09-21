/*用户表user的创建*/
CREATE TABLE users(
	u_no			INT		NOT NULL,		
	u_pwd			VARCHAR(16)	NOT NULL,
	u_name		VARCHAR(8),
	u_email		VARCHAR(18),
	u_state		TINYTEXT,
	h_id		INT,
	PRIMARY		KEY(u_no),
	key FK_USERS_1(h_id),
	constraint FK_USERS_1 FOREIGN KEY(h_id) REFERENCES head(h_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;