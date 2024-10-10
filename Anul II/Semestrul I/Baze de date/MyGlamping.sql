create database MyGlamping
go
use MyGlamping
go

create table Glamping
(idG int primary key identity,
Name varchar(50),
Adress varchar(75) not null,
PhoneNumber varchar(13),
Description varchar(700))

create table Admin
(idA int foreign key references Glamping(idG),
constraint pkA primary key(idA),
Name varchar(50),
Email varchar(50),
Password varchar(50))

create table Costs
(idC int primary key identity,
idG int foreign key references Glamping (idG),
Total numeric,
Date date,
Type varchar(50))

create table Cottages
(idCTG int primary key identity,
idG int foreign key references Glamping (idG),
Name varchar(50),
Capacity int,
Price numeric)

create table FreeSpaces
(idFS int primary key identity,
idG int foreign key references Glamping (idG),
Name varchar(50),
Surface numeric,
Price numeric)

create table Tents
(idT int primary key identity,
idG int foreign key references Glamping (idG),
Name varchar(50),
Capacity int,
Price numeric)

create table Bookings
(idB int primary key identity,
idCTG int foreign key references Cottages(idCTG),
idFS int foreign key references FreeSpaces(idFS),
idT int foreign key references Tents(idT),
idCLNT int foreign key references Clients(idCLNT),
CheckIn date,
CheckOut date)

create table Services
(idSRV int primary key identity,
Type varchar(50),
Availability int)

create table ServicesBookings
(idBS int foreign key references Bookings(idB),
idSRV int foreign key references Services(idSRV),
constraint pkSrvBks primary key (idBS, idSRV),
Total int,
Period varchar(50))

create table Clients
(idCLNT int primary key identity,
FirstName varchar(50),
Surname varchar(50),
Email varchar(50),
PhoneNumber varchar(13),
Adress varchar(50))

create table Bills
(idBLS int primary key identity,
idCLNT int foreign key references Clients(idCLNT),
Service varchar(100),
Date date,
Total numeric)

create table Reviews
(idRWS int primary key identity,
idCLNT int foreign key references Clients(idCLNT),
Description varchar(500),
Rating numeric)