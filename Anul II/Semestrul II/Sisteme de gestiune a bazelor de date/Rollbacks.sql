use MyGlamping
go

--FUNCTII DE VALIDARE 


--TABELUL TENTS

go
create or alter function validareT (@idG int, @nume varchar(50), @capacitate int, @pret numeric)
returns bit
as
begin
	if @idG is null or @nume is null or @capacitate is null or @pret is null
		return 0
	return 1
end
go

create or alter function validareTents (@idG int, @nume varchar(50), @capacitate int, @pret numeric)
returns varchar(200)
as
begin
	declare @error varchar(200)
	set @error=''
	if (dbo.validareT(@idG, @nume, @capacitate, @pret)=0)
		set @error = @error + 'Informatiile despre cort nu sunt valide.'
	return @error
end
go


--TABELUL BOOKINGS

go
create or alter function validareCheckIn (@checkin date)
returns bit
as
begin
	if @checkin >= '2024-06-01' and @checkin < '2024-09-01'
		return 1
	return 0
end
go

go
create or alter function validareCheckOut (@checkout date)
returns bit
as
begin
	if @checkout > '2024-06-01' and @checkout <= '2024-09-01'
		return 1
	return 0
end
go

go
create or alter function validareIduri (@idCTG int, @idFS int, @idT int, @idC int)
returns bit
as
begin
	if @idT is null and @idC is null
		return 0
	if @idCTG > 0 or @idFS >0
		return 0
	return 1
end
go

create or alter function validareBookings (@idCTG int, @idFS int, @idT int, @idC int, @checkout date, @checkin date)
returns varchar(200)
as
begin
	declare @error varchar(200)
	set @error=''
	if (dbo.validareCheckIn(@checkin)=0 or dbo.validareCheckOut(@checkout)=0)
		set @error = @error + 'Data nu corespunde perioadei de cazare.'
	if (dbo.validareIduri(@idCTG, @idFS, @idT, @idC)=0)
		set @error = @error + 'Id-uri nepotrivite.'
	return @error
end
go

--TABELUL CLIENTS

go
create or alter function validareNume (@nume varchar(50))
returns bit
as
begin
	declare @lungime int
	declare @contor int
	set @lungime=len(@nume)
	set @contor=1
	while @contor<=@lungime
	begin
		if not substring(@nume,@contor,1) like '%[A-Za-z]'
			return 0
		set @contor=@contor+1
	end
	return 1
end
go

go
create or alter function validareEmail (@email varchar(50))
returns bit
as
begin
	if right(@email,10)='@gmail.com' or right(@email,10)='@yahoo.com'
		return 1
	return 0
end
go

go
create or alter function validareTelefon (@telefon varchar(13))
returns bit
as
begin
	if len(@telefon)<>10
		return 0
	declare @lungime int
	declare @contor int
	set @lungime=len(@telefon)
	set @contor=1
	while @contor<=@lungime
	begin
		if not substring(@telefon,@contor,1) like '%[0-9]'
			return 0
		set @contor=@contor+1
	end
	return 1
end
go

create or alter function validareAdresa (@adresa varchar(50))
returns bit
as
begin
	if @adresa is null
		return 0
	return 1
end
go

create or alter function validareClients (@prenume varchar(50),@nume varchar(50),@email varchar(50),@numar varchar(13),@adresa varchar(50))
returns varchar(200)
as
begin
	declare @error varchar(200)
	set @error=''
	if (dbo.validareNume(@nume)=0 or dbo.validareNume(@prenume)=0)
		set @error = @error + 'Numele este invalid.'
	if (dbo.validareEmail(@email)=0)
		set @error = @error + 'E-mail-ul este invalid.'
	if (dbo.validareTelefon(@numar)=0)
		set @error = @error + 'Numarul de telefon este invalid.'
	if (dbo.validareAdresa(@adresa)=0)
		set @error = @error + 'Adresa este invalida.'
	return @error
end
go


--TABEL DE URMARIRE

create table LogTable
(
	nume varchar(20),
	actiune varchar(20),
	timp time
);



--ROLL-BACK INTREG



go
create or alter procedure insertIntreg
(
@idG int,
@numeT varchar(50),
@capacitate int, 
@pret numeric,
@idCTG int,
@idFS int,
@idT int,
@idCLNT int,
@checkin date,
@checkout date, 
@prenume varchar(50),
@nume varchar(50),
@email varchar(50),
@numar varchar(13),
@adresa varchar(50)
)
as
begin
	begin tran
	begin try
		declare @error varchar(200)
		--Tents
		set @error=dbo.validareTents(@idG, @numeT, @capacitate, @pret)
		if @error!=''
			begin
				print @error
				raiserror(@error,14,1)
			end
		insert into Tents(idG, Name, Capacity, Price) values (@idG, @numeT, @capacitate, @pret)
		insert into LogTable(nume,actiune,timp) values ('Tents', 'Insert', CURRENT_TIMESTAMP)
		--Clients
		set @error=dbo.validareClients(@prenume,@nume,@email,@numar,@adresa)
		if @error!=''
			begin
				print @error
				raiserror(@error,14,1)
			end
		insert into Clients(FirstName, Surname, Email, PhoneNumber, Adress) values (@prenume,@nume,@email,@numar,@adresa)
		insert into LogTable(nume,actiune,timp) values ('Clients', 'Insert', CURRENT_TIMESTAMP)
		--Bookings
		declare @idTent int
		declare @idClient int
		set @idTent=(select max(idT) from Tents)
		set @idClient=(select max(idCLNT) from Clients)
		set @error=dbo.validareBookings(@idCTG, @idFS, @idTent, @idClient, @checkout, @checkin)
		if @error!=''
			begin
				print @error
				raiserror(@error,14,1)
			end
		insert into Bookings(idCTG, idFS, idT, idCLNT, CheckIn, CheckOut) values (@idCTG, @idFS, @idTent, @idClient, @checkin, @checkout)
		insert into LogTable(nume, actiune, timp) values ('Bookings', 'Insert', CURRENT_TIMESTAMP)

		commit tran
		select 'Commited!'
	end try
	begin catch
		rollback tran
		select 'Rollback!'
	end catch
end
go



--ROLLBACK PARTIAL 



go
create or alter procedure insertPartial
(
@idG int,
@numeT varchar(50),
@capacitate int, 
@pret numeric,
@idCTG int,
@idFS int,
@idT int,
@idCLNT int,
@checkin date,
@checkout date, 
@prenume varchar(50),
@nume varchar(50),
@email varchar(50),
@numar varchar(13),
@adresa varchar(50)
)
as
begin
	declare @error varchar(200)
	declare @rollback int
	set @rollback=0
	--Tents
	begin tran
	begin try
		set @error=dbo.validareTents(@idG, @numeT, @capacitate, @pret)
		if @error!=''
			begin
				print @error
				raiserror(@error,14,1)
			end
		insert into Tents(idG, Name, Capacity, Price) values (@idG, @numeT, @capacitate, @pret)
		insert into LogTable(nume,actiune,timp) values ('Tents', 'Insert', CURRENT_TIMESTAMP)
		commit tran
		select 'Commited pentru Tents!'
	end try
	begin catch
		rollback tran
		set @rollback=1
		insert into LogTable (nume,actiune, timp) values ('Tents', 'Rollback', CURRENT_TIMESTAMP)
		select 'Rollback pentru Tents!'
	end catch
	--Clients
	begin tran
	begin try
		set @error=dbo.validareClients(@prenume,@nume,@email,@numar,@adresa)
		if @error!=''
			begin
				print @error
				raiserror(@error,14,1)
			end
		insert into Clients(FirstName, Surname, Email, PhoneNumber, Adress) values (@prenume,@nume,@email,@numar,@adresa)
		insert into LogTable(nume,actiune,timp) values ('Clients', 'Insert', CURRENT_TIMESTAMP)
		commit tran
		select 'Commited pentru Clients!'
	end try
	begin catch
		rollback tran
		set @rollback=1
		insert into LogTable (nume,actiune, timp) values ('Clients', 'Rollback', CURRENT_TIMESTAMP)
		select 'Rollback pentru Clients!'
	end catch
	--Bookings
	begin tran
	begin try
		declare @idTent int
		declare @idClient int
		set @idTent=(select max(idT) from Tents)
		set @idClient=(select max(idCLNT) from Clients)
		set @error=dbo.validareBookings(@idCTG, @idFS, @idTent, @idClient, @checkout, @checkin)
		if @error!=''
			begin
				print @error
				raiserror(@error,14,1)
			end
		insert into Bookings(idCTG, idFS, idT, idCLNT, CheckIn, CheckOut) values (@idCTG, @idFS, @idTent, @idClient, @checkin, @checkout)
		insert into LogTable(nume, actiune, timp) values ('Bookings', 'Insert', CURRENT_TIMESTAMP)
		commit tran
		select 'Commited pentru Bookings!'
	end try
	begin catch
		rollback tran
		set @rollback=1
		insert into LogTable (nume,actiune, timp) values ('Bookings', 'Rollback', CURRENT_TIMESTAMP)
		select 'Rollback pentru Bookings!'
	end catch
end
go



--TESTE

select * from Tents
select * from Clients
select * from Bookings
select * from LogTable

execute dbo.insertIntreg 1, 'Vrajitoarele', 4, 160.00, null,null,null,null,'2024-07-11','2024-07-15',
'Dana', 'Rusu', 'rusudana00@gmail.com', '0770134207','Petru Maior 64'

execute dbo.insertIntreg 1, 'Vrajitoarele', 4, 160.00, null,null,null,null,'2024-09-11','2024-09-15',
'Dana', 'Rusu', 'rusudana00@gmail.com', '0770134207','Petru Maior 64'

execute dbo.insertIntreg 1, 'Vrajitoarele', 4, 160.00, null,null,null,null,'2024-07-11','2024-07-15',
'Dana', 'Rusu', 'rusudana00@icloud.com', '0770134207','Petru Maior 64'

execute dbo.insertPartial 1, 'ZaneleUrsitoare',7, 230.00, null,null,null,null,'2024-07-11','2024-07-15',
'Ariana', 'Stan', 'ariistan75@gmail.com', '0745368997','Detunata 6'

execute dbo.insertPartial 1, 'ZaneleFericite',6, 180.00, null,null,null,null,'2024-09-11','2024-07-15',
'Antonia', 'Moga', 'mogaanto@gmail.com', '0770226813','Liszt Ferenc 26'

execute dbo.insertPartial 1, null,6, null, null,null,null,null,'2024-09-11','2024-07-15',
'Rares', 'Gherescu', 'raresstefan@gmail.com', '0770145504','Olarilor 14'