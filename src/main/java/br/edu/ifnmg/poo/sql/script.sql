create database opaladb;
use opaladb;

create table Driver (
id bigint unsigned PRIMARY KEY AUTO_INCREMENT,
ticket int
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

create table Vehicle (
id bigint unsigned PRIMARY KEY AUTO_INCREMENT,
id_driver bigint unsigned,
licensePlate varchar(8) NOT NULL,
note varchar(500),
description varchar(500),
cost decimal(10,2),
FOREIGN KEY(id_driver) REFERENCES Driver(id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

create table ParkingSpace (
id bigint unsigned PRIMARY KEY AUTO_INCREMENT,
id_driver bigint unsigned,
number smallint unsigned,
arrivalTime DATETIME,
departureTime DATETIME,
FOREIGN KEY(id_driver) REFERENCES Driver(id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

create table User (
id bigint unsigned PRIMARY KEY AUTO_INCREMENT,
name varchar(150) NOT NULL,
email varchar(255) NOT NULL,
telephone bigint unsigned NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

create table Credential (
id bigint unsigned PRIMARY KEY AUTO_INCREMENT,
id_user bigint unsigned,
username varchar(32) NOT NULL,
password varchar(32) NOT NULL,
lastAccess Date NOT NULL,
typeUser ENUM('ADMIN', 'OPERATOR', 'SUBSCRIBER', 'DISABLED') NOT NULL,
FOREIGN KEY(id_user) REFERENCES User(id) 
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

desc Driver;
desc Vehicle;
desc ParkingSpace;
desc User;
desc Credential;