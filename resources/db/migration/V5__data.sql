INSERT INTO user (email, password) VALUES('ALICE@GMAIL.COM','PASSWORD123');

INSERT INTO POLL(QUESTION,OWNERID,CREATED,STARTDATE,ENDDATE,TEXTMSG,URL,STATUS)
VALUES ('1+1?',1,TO_DATE('20160215','YYYYMMDD'),TO_DATE('20160215','YYYYMMDD'),
TO_DATE('20160315','YYYYMMDD'),'YeTe','www.ownpoll','online');

INSERT INTO CHOICE(TEXT,POLLID) VALUES ('2',1);
INSERT INTO CHOICE(TEXT,POLLID) VALUES ('10',1);

INSERT INTO VOTE VALUES('1','1',NOW(),123456789,2345,3);
INSERT INTO VOTE VALUES('1','2',NOW(),987654321,7890,3);

INSERT INTO POLL(QUESTION,OWNERID,CREATED,STARTDATE,ENDDATE,TEXTMSG,URL,STATUS)
VALUES ('Favorite animal?',1,TO_DATE('20160215','YYYYMMDD'),TO_DATE('20160215','YYYYMMDD'),
TO_DATE('20160315','YYYYMMDD'),'arcll','2938','online');

INSERT INTO CHOICE(TEXT,POLLID) VALUES ('Giraffe',2);
INSERT INTO CHOICE(TEXT,POLLID) VALUES ('Cat',2);
