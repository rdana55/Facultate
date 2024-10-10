create database SpectacolPapusi
go
use SpectacolPapusi
go

---creare tabele

create table Papusi
(
idP int primary key identity,
Nume varchar(50),
Personaj varchar(50),
Gen varchar(10)
)

create table Teatre
(
idT int primary key identity,
Nume varchar(50),
Oras varchar(50),
Stele int,
)

create table Spectacole
(
idS int primary key identity,
idT int foreign key references Teatre(idT),
Denumire varchar(50),
Durata varchar(50),
Regizor varchar(50)
)

create table Mascote
(
idM int foreign key references Spectacole(idS),
constraint pkM primary key (idM),
Nume varchar(50),
Culoare varchar(50),
Personaj varchar(50),
)

create table SpectacolPapusi
(
idP int foreign key references Papusi(idP),
idS int foreign key references Spectacole(idS),
constraint pkSP primary key (idP, idS),
Data date,
Pret numeric
)

---inserari

insert into Papusi values ('Nume1','Personaj1','Gen1'),('Nume2','Personaj2','Gen2'),('Nume3','Personaj3','Gen3'),('Nume4','Personaj4','Gen4')
select * from Papusi
insert into Teatre values ('Nume1','Oras1',4),('Nume2','Oras2',5),('Nume3','Oras3',4),('Nume4','Oras4',1)
select * from Teatre
insert into Spectacole values (1,'Denumire1','Durata1','Regizor1'),(1,'Denumire2','Durata2','Regizor2'),(3,'Denumire3','Durata3','Regizor3'),(4,'Denumire4','Durata4','Regizor4')
select * from Spectacole
insert into Mascote values (1, 'Nume1','Culoare1', 'Persoanj1'),(2, 'Nume2','Culoare2', 'Persoanj2'),(3, 'Nume3','Culoare3', 'Persoanj3'),(4, 'Nume4','Culoare4', 'Persoanj4')
select * from Mascote
insert into SpectacolPapusi values (1,1,'2024-01-18',24.5),(1,4,'2024-02-05',30),(2,3,'2025-07-30',27.5),(3,2,'2024-12-23',20),(4,4,'2025-02-14',45.5)
select * from SpectacolPapusi


--- procedura

create or alter procedure adaugaSpectacol
	@idP int,
	@idS int,
	@data date,
	@pret numeric

as

	if exists (select PS.idP from SpectacolPapusi as PS where PS.idP=@idP and PS.idS=@idS)
	begin
		update SpectacolPapusi
		set Data=@data,Pret=@pret where idP=@idP and idS=@idS
	end
	else
	begin
		insert into SpectacolPapusi values (@idP, @idS, @data, @pret)
	end

go

exec adaugaSpectacol 1,1,'2025-12-12',50.5
exec adaugaSpectacol 4,1,'2024-11-23',12.5

select * from SpectacolPapusi


--- functie 

go
create or alter function Afisare
(
	@n int
)
returns table
as return

	select p.Nume as Papusi
	from Papusi P inner join SpectacolPapusi sp on sp.idP=p.idP
	group by p.Nume
	having count(sp.idP) >= @n
go

select * from Afisare(1)