use MyGlamping
go


--2 m-n, 3 group by, 2 having


--cate servicii mai sunt disponibile intr un interval orar
select s.Type, sum(s.Availability-sb.Total) as AvailableLeft, sb.Period
from Services s inner join ServicesBookings sb on s.idSRV=sb.idSRV
inner join Bookings b on b.idB =sb.idBS
group by s.Type,sb.Period

--id urile clientilor care au rezervat cel putin un serviciu si nr serviciilor/client
select b.idCLNT, sum(sb.Total) as ServiciiRezervate
from Services s inner join ServicesBookings sb on s.idSRV=sb.idSRV inner join Bookings b on b.idB =sb.idBS
group by b.idCLNT
having sum(sb.Total)>0

--ratingul total >5 al clientilor cazati in lunile octombrie si noiembrie
select avg(r.rating) as OverallRating, c.FirstName, b.CheckIn
from Reviews r inner join Clients c on r.idCLNT=c.idCLNT inner join Bookings b on b.idCLNT=c.idCLNT
group by c.FirstName, b.CheckIn
having avg(r.Rating)>5 and DATEPART(MONTH,b.CheckIn) in (10,11)


--7 de minim 3 dintre care 5 cu where, 1 cu distinct


--id-ul si numele cabanutei la care este cazat un client care are adresa de yahoo
select b.idCTG, ctg.Name, c.Email
from Cottages ctg inner join Bookings b on ctg.idCTG=b.idCTG inner join Clients c on b.idCLNT=c.idCLNT
where c.Email like '%yahoo%'

--numele si prenumele clientilor care platesc cel putin 30 de lei pentru cazarea intr-un cort
select c.Surname, c.FirstName, t.Name, t.Price
from Tents t inner join Bookings b on t.idT=b.idT inner join Clients c on b.idCLNT=c.idCLNT
where t.Price>=30

--denumirea si pretul suprafetelor cuprinse intre 2 si 3.75 metri patrati, care nu se afla langa padure, care sunt rezervate de clienti
select fs.Name, fs.Price
from FreeSpaces fs inner join Bookings b on fs.idFS=b.idFS inner join Clients c on b.idCLNT=c.idCLNT
where fs.Surface>2.00 and fs.Surface<3.75
except
select fs.Name, fs.Price
from FreeSpaces fs inner join Bookings b on fs.idFS=b.idFS inner join Clients c on b.idCLNT=c.idCLNT
where fs.Name='Langa padure'

--id-ul rezervarii si adresa de facturare pentru clientii care au platit sub 300 de lei 
select b.idB, c.Adress, bls.Total
from Bookings b inner join Clients c on b.idCLNT=c.idCLNT inner join Bills bls on bls.idCLNT=c.idCLNT
where bls.Total <300.00

--id-ul rezervarii, al clientului, numele, prenumele si nr. de telefon al acestuia, care au inchiriat corturi
select b.idB, c.idCLNT, c.Surname, c.FirstName, c.PhoneNumber, bls.Service
from Bookings b inner join Clients c on b.idCLNT=c.idCLNT inner join Bills bls on bls.idCLNT=c.idCLNT
where bls.Service like '%cort%'

--doar notele diferite primite de la clienti
select distinct r.Rating
from Reviews r inner join Clients c on r.idCLNT=c.idCLNT inner join Bookings b on b.idCLNT=c.idCLNT


--1 cu distinct, indiferent


--preturile distincte pentru costurile glampingului
select distinct Total
from Costs