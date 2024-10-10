use MyGlamping
go

--modificare tip coloana
create procedure do1
	as
	begin

	alter table Bills
	alter column Total decimal
	print 'Modificare realizata cu succes!'

	end
go

create procedure undo1
	as
	begin

	alter table Bills
	alter column Total numeric
	print 'Undo pentru modificarea tipului de coloana realizat cu succes!'

	end
go

--adaugare constrangere de "val. implicita" pentru un camp
create procedure do2
	as
	begin

	alter table Glamping
	add constraint df_nr
	default '0770134207' for PhoneNumber
	print 'Constrangere adaugata cu succes!'

	end
go

create procedure undo2
	as
	begin

	alter table Glamping
	drop constraint df_nr
	print 'Constrangere stearsa cu succes!'

	end
go

--creare/stergere tabela
create procedure do3
	as
	begin

	create table Angajati(
	idAng int primary key identity,
	FirstName varchar(50) not null,
	LastName varchar(50) not null,
	CNP varchar(13) not null,
	Address varchar(50),
	PhoneNumber varchar(13),
	Email varchar(50));
	print 'Tabel creat cu succes!'

	end
go

create procedure undo3
	as
	begin

	drop table if exists Angajati
	print 'Tabel a fost sters!'

	end
go

--adaugare camp nou
create procedure do4
	as
	begin

	alter table Admin
	add CNP varchar(13)
	print 'Camp adaugat cu succes!'

	end
go

create procedure undo4
	as
	begin

	alter table Admin
	drop column CNP
	print 'Campul a fost sters cu succes!'

	end
go

--creare/stergere constrangere cheie straina
create procedure do5
	as
	begin

	alter table Admin
	add constraint fk_costs foreign key (idCosts) references Costs(idC)
	print 'Constrangere FK adaugata cu succes!'

	end
go

create procedure undo5
	as
	begin

	alter table Admin
	drop constraint fk_costs
	print 'Constrangere FK stearsa cu succes!'

	end
go

-- @ sa fie intre [0 si 5]
-- @ > versiunea curenta -> do pana sunt egale 1,2..@
-- @ < versiunea curenta -> undo invers versiune curenta,.., @
-- actualizare versiune curenta la @

create procedure main
@vrs int
	as
	begin

	--declarare variabile
	declare @crtVrs int
	declare @cmd varchar(50)

	--verificari versiuni
	if (@vrs<0)
		raiserror ('Nu poate sa fie un nr negativ',18,1)
	if @@ERROR!=0
		return
	if (@vrs>5)
		raiserror('Avem doar 5 versiuni disponibile',18,1)
	if @@ERROR!=0
		return

	else
		select top 1 @crtVrs=crtVrs from Version

		--v.c. < vrs
		while @crtVrs<@vrs
		begin
			set @crtVrs=@crtVrs+1
			set @cmd=concat('do',cast(@crtVrs as varchar(1)))
			exec @cmd
		end

		--v.c>vrs
		while @crtVrs>@vrs
		begin
			set @cmd=concat('undo',cast(@crtVrs as varchar(1)))
			exec @cmd
			set @crtVrs=@crtVrs-1
		end

		print ' Versiune modificata cu succes!'

		update Version set crtVrs = @vrs
		print 'Tabelul Version a fost actualizat'

		select * from Version
	end
go

exec main 0;

--tabelul pt versiuni
create table Version(
crtVrs int primary key)

insert into Version(crtVrs) values (0)

go