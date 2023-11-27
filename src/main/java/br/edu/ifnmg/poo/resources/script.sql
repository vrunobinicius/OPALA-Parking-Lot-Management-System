create database opaladb;
use opaladb;

create table user (
ID bigint unsigned PRIMARY KEY AUTO_INCREMENT,
name varchar(150) NOT NULL,
email varchar(255) NOT NULL,
birthdate DATE NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

create table credential (
ID bigint unsigned PRIMARY KEY AUTO_INCREMENT, 
username varchar(15) NOT NULL,
password varchar(32) NOT NULL,
lastAccess DATE,
enabled tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

desc user;
desc credential;