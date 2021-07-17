CREATE PROCEDURE grantRequest 
	@username varchar(100),
	@licencePlateNumber varchar(100)
AS
BEGIN
	insert into dbo.Kurir(KorisnickoIme, RegBroj) values(@username, @licencePlateNumber)
	delete from ZahtevZaKurira 
	where KorisnickoIme = @username
END
GO