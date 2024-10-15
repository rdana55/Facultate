use S11
go 

create index indexMuzeeExponate on Muzee(NrExponate)

select * from Muzee order by NrExponate