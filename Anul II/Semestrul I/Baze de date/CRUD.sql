--- VALIDARI

---validare not null

use MyGlamping
go

create or alter function isNotNull (@string date)
	returns int
as
begin
	if @string is not null
	begin
		return 1
	end

	return 0

end
go

---validare type 

create or alter function isOKString (@string varchar(50))
	returns int
as
begin
	if (len(@string) > 2) 
	begin
		return 1
	end

	return 0

end
go

---validare >=0

create or alter function isOkNumber (@nr int)
	returns int
as
begin
	if @nr > -1
	begin
		return 1
	end

	return 0
end
go

---validare id bookings

create or alter function validIdBookings (@idB int)
	returns int
as
begin
	if exists (select * from Bookings where @idB = Bookings.idB)
	begin
		return 1
	end

	return 0

end
go

---validare id services

create or alter function validIdServices (@idS int)
	returns int
as
begin
	if exists (select * from Services where @idS = Services.idSRV)
	begin
		return 1
	end

	return 0

end
go



--- CRUD

---bookings

go
create or alter procedure crudBookings
	@idCTG int,
	@checkin date,
	@checkout date,
	@randuri int = 1

as
begin
	set nocount on
	if (dbo.isNotNull(@checkin)=1 and
	dbo.isNotNull(@checkout)=1)
	begin
		-- insert
		declare @n int=0
		while(@n < @randuri)
		begin
			insert into Bookings (idCTG,CheckIn,CheckOut)values (@idCTG, @checkin, @checkout)
			set @n=@n+1
		end

		-- select
		select * from Bookings order by Bookings.CheckIn

		-- update
		update Bookings
		set Bookings.CheckIn=@checkin
		where Bookings.CheckIn=@checkin
		select * from Bookings order by Bookings.CheckIn

		-- delete
		delete from Bookings
		where Bookings.CheckIn like @checkin
		select * from Bookings order by CheckIn

		print 'Au fost executate operatiile CRUD pentru Bookings.'
	end
	else
	begin
		raiserror('Datele de intrare sunt invalide!',16,1)
	end
end
go

select * from Bookings
exec crudBookings 2, '2023-10-09', '2023-10-11'

---services

go
create or alter procedure crudServices
	@type varchar(50),
	@availability int,
	@randuri int=1

as
begin
	set nocount on
	if (dbo.isOKString(@type)=1 and
	dbo.isOkNumber(@availability)=1)
	begin
		-- insert
		declare @n int=0
		while(@n < @randuri)
		begin
			insert into Services values (@type, @availability)
			set @n=@n+1
		end

		-- select
		select * from Services order by Services.Availability

		-- update
		update Services
		set Services.Type=@type+'_crud'
		where Services.Type=@type
		select * from Services order by Services.Availability

		-- delete
		delete from Services
		where Services.Type like @type+'_crud'
		select * from Services order by Availability

		print 'Au fost executate operatiile CRUD pentru Services.'
	end
	else
	begin
		raiserror('Datele de intrare sunt invalide!',16,1)
	end
end
go

select * from Services
exec crudServices 'ciubar', 2

---services bookings

go
create or alter procedure crudServicesBookings
	@idB int,
	@idS int,
	@total int,
	@perioada varchar(50),
	@randuri int=1

as
begin
	set nocount on
	if (dbo.validIdBookings(@idB)=1 and
	dbo.validIdServices(@idS)=1)
	begin
		-- insert
		insert into ServicesBookings values (@idB, @idS, @total, @perioada)

		-- select
		select * from ServicesBookings order by ServicesBookings.Total

		-- update
		update ServicesBookings
		set ServicesBookings.Total=@total
		where ServicesBookings.Total=@total
		select * from ServicesBookings order by ServicesBookings.Total

		-- delete
		delete from ServicesBookings
		where ServicesBookings.Total like @total
		select * from ServicesBookings order by Total

		print 'Au fost executate operatiile CRUD pentru ServicesBookings.'
	end
	else
	begin
		raiserror('Datele de intrare sunt invalide!',16,1)
	end
end
go

select * from ServicesBookings
exec crudServicesBookings 11,12, 3, '08->12'



--- VIEWS

---bookings

go
create or alter view viewBookings
as
	select CheckIn,CheckOut 
	from Bookings as B
	inner join ServicesBookings as SB
	on B.idB=SB.idBS
go

---services

go
create or alter view viewBookingsServices
as
	select B.CheckIn, B.CheckOut, Services.Type, Services.Availability
	from Bookings as B
	inner join ServicesBookings as SB
	on B.idB=SB.idBS
	inner join Services
	on Services.idSRV=SB.idSRV
go



--- INDEXI 

---Bookings

create nonclustered index nIdxCI on Bookings(CheckIn)
create nonclustered index nIdxCO on Bookings(CheckOut)
create nonclustered index nIdxIdB on Bookings(idB)

---Services

create nonclustered index nIdxT on Services(Type)
create nonclustered index nIdxA on Services(Availability)
create nonclustered index nIdxIdS on Services(idSRV)

---ServicesBookings

create nonclustered index nIdxIdBookings on ServicesBookings(idBS)
create nonclustered index nIdxIdServices on ServicesBookings(idSRV)


select * from viewBookings
select * from viewBookingsServices
