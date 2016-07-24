INSERT INTO user(U_Id,username,email) VALUES
(11201,'Sreenu Panicker','sreenu.panicker@gmail.com'),
(11202,'Madhu Panicker','madhu.panicker@gmail.com'),
(11203,'Bindu Nair','bindu.nair@gmail.com'),
(11204,'Srijith Panicker','jithu.panicker@gmail.com'),
(11205,'Monisha Mathew','monisha.mathew@gmail.com'),
(11206,'Supriya Suresh','ss.santosh@gmail.com');


insert into user (username) values
('alice'),
('bob'),
('devi');

insert into user (username, email) values
('chris', 'league@example.com');

insert into polls (P_Subject,U_Id,status,is_active) VALUES
('Interest',11201,'Finding the interest', 'yes' );

insert into polls (P_Subject,U_Id,status,is_active) VALUES
('Love',11205,'Hard to find', 'no' );