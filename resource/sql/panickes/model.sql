create table user
(   U_Id       int primary key auto_increment
  , username varchar(45) not null
  , email    varchar(128)
  , joined   timestamp default now()
);

create table polls
(   P_Id      int  auto_increment,
    P_Subject varchar(255) not null,
    U_Id   int not null,
    date_create timestamp,
    status varchar(255) not null,
    is_active BIT not null,
    url varchar(128),
    PRIMARY KEY (P_Id),
    FOREIGN KEY (U_Id) REFERENCES user(U_Id)
);

create table choice
(   P_Id      int  auto_increment,
    PollID int auto_increment,
    OptionText varchar(255),
    is_active BIT not null,
    PRIMARY KEY (PollID),
    FOREIGN KEY (P_Id) REFERENCES polls(P_Id)
);