/*��user���������*/
INSERT INTO users(u_no,u_pwd,u_name,u_email,u_state) VALUES('2008','123','��ǿ','www@126.com','I am ����.');
INSERT INTO users(u_no,u_pwd,u_name,u_email,u_state) VALUES('2009','123','Tom','www@126.com','I am sad.');
INSERT INTO users(u_no,u_pwd,u_name,u_email,u_state) VALUES('2010','123','Jerry','www@126.com','I am busy.');
/*��friend���в�������*/
insert into friend(f_id,u_noz,u_noy) VALUES('1','2008','2009');
insert into friend(f_id,u_noz,u_noy) VALUES('2','2008','2010');
insert into friend(f_id,u_noz,u_noy) VALUES('3','2009','2010');

/*��diary���в�������*/
INSERT INTO diary(r_id,r_title,r_content,u_no) VALUES('1','������־1','�����������ã��������ġ�','2008');
INSERT INTO diary(r_id,r_title,r_content,u_no) VALUES('2','������־2','������䡣','2008');
INSERT INTO diary(r_id,r_title,r_content,u_no) VALUES('3','������־','����ȥ�����ˡ�','2009');
/*��album���в�������*/
INSERT INTO album(x_id,x_name,u_no) VALUES('1','������','2008');
INSERT INTO album(x_id,x_name,u_no) VALUES('2','�ҵļ���','2009');

/*��comment�����������*/
/*--INSERT INTO comment(c_content,u_no,r_id) VALUES('��д����־̫���ˣ��Һ�ϡ��Ŷ','2009',SELECT r_id FROM diary WHERE u_no='2008');*/

/*��visit���������*/
/*# INSERT INTO visit(u_no,r_id) VALUES('2008'*/

/*��max���в�������*/
INSERT INTO maxno VALUES(3,3,2,0,0,0,2010,0,0);		#������ű��в���Ωһ�ļ�¼