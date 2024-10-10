use MyGlamping
go

insert into Glamping (Name,Adress,PhoneNumber,Description) values ('Joyping','Str. Crinului, Hidiselul de Jos','0359173245','Bucura-te de o relxare in aer liber si vino alaturi de noi intr-o oaza de liniste!')
select * from Glamping

insert into Admin (idA,Name,Email,Password) values (1,'Purto Alin','purtoalin@gmail.com','arlnj2334')
select * from Admin

insert into Costs (idG, Total, Date, Type) values (1,238.99,'2023-11-20','gaz')
insert into Costs (idG, Total, Date, Type) values (1,400.00,'2023-09-15','curent')
insert into Costs (idG, Total, Date, Type) values (1,250.00,'2023-10-12','necesare baie')
insert into Costs (idG, Total, Date, Type) values (1,500.00,'2023-11-01','curatenie')
select * from Costs

insert into Tents (idG,Name,Capacity,Price) values (1,'Spiridusii',2,50.00)
insert into Tents (idG,Name,Capacity,Price) values (1,'Nimfele',2,50.00)
insert into Tents (idG,Name,Capacity,Price) values (1,'Ursitoarele',4,120.00)
insert into Tents (idG,Name,Capacity,Price) values (1,'Zanele',3,80.00)
insert into Tents (idG,Name,Capacity,Price) values (1,'Vanatorii',6,200.00)
select * from Tents

insert into Cottages (idG,Name,Capacity,Price) values (1,'Cabana 1',4,200.00)
insert into Cottages (idG,Name,Capacity,Price) values (1,'Cabana 2',4,200.00)
insert into Cottages (idG,Name,Capacity,Price) values (1,'Cabana 3',2,150.00)
insert into Cottages (idG,Name,Capacity,Price) values (1,'Cabana 4',3,180.00)
insert into Cottages (idG,Name,Capacity,Price) values (1,'Cabana 5',8,400.00)
select * from Cottages

insert into FreeSpaces (idG, Name, Surface, Price) values (1, 'Langa apa', 2.50, 25.00)
insert into FreeSpaces (idG, Name, Surface, Price) values (1, 'Langa apa', 5.00, 40.00)
insert into FreeSpaces (idG, Name, Surface, Price) values (1, 'Langa padure', 3.00, 35.00)
select * from FreeSpaces

insert into Clients (FirstName, Surname, Email, PhoneNumber,Adress) values ('Maria', 'Popescu', 'mariapopescu@yahoo.com', '0746185924','Str. Octavian Goga 53, Munteni, jud. Galati')
insert into Clients (FirstName, Surname, Email, PhoneNumber,Adress) values ('Estera', 'Indre', 'esteraindre@gmail.com', '0770962873','Str. Merilor 1, Rosia Montana, jud. Alba')
insert into Clients (FirstName, Surname, Email, PhoneNumber,Adress) values ('Marius', 'Negrut', 'mariusnegrut@gmail.com', '0771249658','Str. Camil Petrescu 13, Dej, jud. Cluj')
insert into Clients (FirstName, Surname, Email, PhoneNumber,Adress) values ('Angelo', 'Farcas', 'angelofarcas@icloud.com', '0770834596','Str. Pastorilor 22, Oradea, jud. Bihor')
insert into Clients (FirstName, Surname, Email, PhoneNumber,Adress) values ('Sergiu', 'Salajan', 'sergiusalajan@gmail.com', '0746875347','Str. Tudor Vladimirescu 45, Bucuresti')
insert into Clients (FirstName, Surname, Email, PhoneNumber,Adress) values ('Irina', 'Copos', 'irinacopos@yahoo.com', '0771849234','Str. Cerbului 2, Sibiu, jud. Sibiu')
select * from Clients
--delete from Clients

insert into Bills (idCLNT, Service, Date, Total) values (1, 'cazare cabana', '2023-10-09', 400.00)
insert into Bills (idCLNT, Service, Date, Total) values (3, 'cazare cort', '2023-11-01', 80.00)
insert into Bills (idCLNT, Service, Date, Total) values (4, 'cazare cort', '2023-11-01', 50.00)
insert into Bills (idCLNT, Service, Date, Total) values (6, 'spatiu liber', '2023-08-22', 25.00)
--delete from Bills
select * from Bills

insert into Reviews (idCLNT, Description, Rating) values (1, 'Gazde dragute si peisaj superb. Voi reveni cu drag in fiecare an!', 10.00)
insert into Reviews (idCLNT, Description, Rating) values (3, 'Totul in mijlocul naturii, relaxare totala. Ar fi fost utila o baie in cabana.', 9.00)
insert into Reviews (idCLNT, Description, Rating) values (4, 'Imprejurimi minunate, foarte multe activitati de facut. Gazde faine!', 9.50)
insert into Reviews (idCLNT, Description, Rating) values (6, 'Loc linistit, curat, gazdele ne-au ajutat cu tot ce am avut nevoie. Recomand!', 10.00)
select * from Reviews

insert into Bookings (idCTG, idCLNT, CheckIn, CheckOut) values (2,1,'2023-10-09', '2023-10-11')
insert into Bookings (idT, idCLNT, CheckIn, CheckOut) values (4,3,'2023-11-01', '2023-11-02')
insert into Bookings (idT, idCLNT, CheckIn, CheckOut) values (2,4,'2023-11-01', '2023-11-02')
insert into Bookings (idFS, idCLNT, CheckIn, CheckOut) values (1,6,'2023-08-22', '2023-08-23')
select * from Bookings

insert into Services (Type, Availability) values ('gratar',7)
insert into Services (Type, Availability) values ('undite',20)
insert into Services (Type, Availability) values ('echipament catarat',10)
insert into Services (Type, Availability) values ('izopren',25)
select * from Services

insert into ServicesBookings (idBS,idSRV,Total,Period) values (1,1,2,'08->12')
insert into ServicesBookings (idBS,idSRV,Total,Period) values (1,2,8,'09->10')
insert into ServicesBookings (idBS,idSRV,Total,Period) values (2,1,2,'17->19')
insert into ServicesBookings (idBS,idSRV,Total,Period) values (3,3,1,'13->20')
insert into ServicesBookings (idBS,idSRV,Total,Period) values (4,4,1,'21->09')
select * from ServicesBookings

