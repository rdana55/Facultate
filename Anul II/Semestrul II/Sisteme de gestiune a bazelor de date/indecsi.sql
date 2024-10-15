use MyGlamping

create index indexClientEmail on Clients(FirstName,Surname)

select idCLNT, FirstName, Surname, Email, PhoneNumber, Adress from Clients order by FirstName