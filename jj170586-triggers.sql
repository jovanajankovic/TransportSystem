CREATE TRIGGER TR_TransportOffer_jj170586
   ON  Paket
   AFTER update
AS 
BEGIN
	
	declare @IdPaket int
	
	select @IdPaket = IdPaket
	from inserted

	delete from Ponuda
	where IdPaket = @IdPaket

END
GO
