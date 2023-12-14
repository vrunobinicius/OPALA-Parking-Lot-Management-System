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
typeVehicle ENUM('CARRO','MOTO','BICICLETA',
'CAMINHONETE','CAMINHAO','ONIBUS','TANK','HELICOPTER') NOT NULL,
FOREIGN KEY(id_driver) REFERENCES Driver(id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

create table ParkingSpace (
id bigint unsigned PRIMARY KEY AUTO_INCREMENT,
id_driver bigint unsigned,
number smallint unsigned,
arrivalTime varchar(10),
departureTime varchar(10),
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

CREATE TABLE Payment (
    id bigint unsigned PRIMARY KEY AUTO_INCREMENT,
    id_driver bigint unsigned,
    id_parking_space bigint unsigned,
    paymentType ENUM('MENSALISTA', 'HORISTA') NOT NULL,
    paymentFrequency INT,
    amount DECIMAL(10, 2) NOT NULL,
    paymentDate varchar(10),
    paymentTime varchar(10),
    paymentMethod VARCHAR(50) NOT NULL,
    FOREIGN KEY (id_driver) REFERENCES Driver(id),
    FOREIGN KEY (id_parking_space) REFERENCES ParkingSpace(id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

desc Driver;
desc Vehicle;
desc ParkingSpace;
desc User;
desc Credential;
desc Payment;