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
	constraint FK_USERS_1 FOREIGN KEY(h_id) REFERENCES head(h_id);
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
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
	constraint FK_friend_1 FOREIGN KEY(u_noz) REFERENCES user(u_no),
	constraint FK_friend_2 FOREIGN KEY(u_noy) REFERENCES user(u_no)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE diary(/*日记diary表的创建*/
	r_id 				INT NOT NULL,
	r_title			VARCHAR(18) NOT NULL,
	r_content 	TEXT NOT NULL,
	r_date 			TIMESTAMP,
	u_no 				INT NOT NULL,
	PRIMARY 		KEY(r_id),
	key FK_diary_1(u_no),
	CONSTRAINT FK_diary_1 FOREIGN KEY(u_no) REFERENCES user(u_no)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE album(/*相册album表的创建*/
	x_id 		INT NOT NULL,
	x_name 	VARCHAR(18) NOT NULL,
	u_no 		INT NOT NULL,
	x_access INT DEFAULT 0,						-- 0:公开，1：好友，2：仅自己可见
	x_date 	TIMESTAMP,
	PRIMARY KEY(x_id),
	key FK_album_1(u_no),
	constraint FK_album_1 FOREIGN KEY(u_no) REFERENCES user(u_no)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

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

CREATE TABLE comment(/*评论comment表的创建*/
	c_id 			INT NOT NULL,
	c_content		TEXT NOT NULL,
	u_no 			INT NOT NULL,
	r_id 			INT NOT NULL,
	c_date 		TIMESTAMP,
	PRIMARY 	KEY(c_id),
	KEY FK_comment_1(u_id),
	KEY FK_comment_2(r_id),
	constraint FK_comment_1 FOREIGN 	KEY(u_no) REFERENCES user(u_no),
	constraint FK_comment_2 FOREIGN 	KEY(r_id) REFERENCES diary(r_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE p_comment(/*照片评论p_comment表*/
	c_id		INT NOT NULL,
	c_content	TEXT NOT NULL,
	u_no		INT NOT NULL,
	p_id		INT NOT NULL,
	c_date		TIMESTAMP,
	PRIMARY KEY(c_id),
	KEY FK_p_comment_1(u_no),
	KEY FK_p_comment_2(p_id),
	constraint FK_p_comment_1 FOREIGN	KEY(u_no) REFERENCES user(u_no),
	constraint FK_p_comment_2 FOREIGN KEY(p_id) REFERENCES photo(p_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*访客足迹visit表的创建*/
CREATE TABLE visit
(
	v_id 			INT NOT NULL,
	u_no 		INT NOT NULL,
	v_no 		INT NOT NULL,
	v_date 			TIMESTAMP,
	PRIMARY KEY(v_id),
	KEY FK_visit_1(u_no),
	KEY FK_visit_2(v_no),
	constraint FK_visit_1 FOREIGN KEY(u_no) REFERENCES user(u_no),
	constraint FK_visit_2 FOREIGN KEY(v_no) REFERENCES user(u_no)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


CREATE TABLE max(/*创建max表*/
	friend_max INT NOT NULL DEFAULT 0,
	diary_max INT NOT NULL DEFAULT 0,
	album_max INT NOT NULL DEFAULT 0,
	photo_max INT NOT NULL DEFAULT 0,
	comment_max INT NOT NULL DEFAULT 0,
	visit_max INT NOT NULL DEFAULT 0,
	user_max INT NOT NULL DEFAULT 0,
	head_max INT NOT NULL DEFAULT 0,
	p_comment_max INT NOT NULL DEFAULT 0
);


CREATE TABLE head(/*头像head表*/
	h_id INT NOT NULL,
	h_des VARCHAR(40) NOT NULL,
	h_data MEDIUMBLOB NOT NULL,
	u_no INT,
	PRIMARY KEY(h_id),
	key FK_head_1(u_no),
	constraint FK_head_1 FOREIGN KEY(u_no) REFERENCES user(u_no)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
-- 建表后先把默认头像传上去







