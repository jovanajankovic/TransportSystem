CREATE TABLE [Admin]
( 
	[KorisnickoIme]      varchar(100)  NOT NULL 
)
go

CREATE TABLE [Grad]
( 
	[IdGrad]             integer  IDENTITY  NOT NULL ,
	[Naziv]              varchar(100)  NOT NULL ,
	[PostanskiBroj]      integer  NOT NULL 
)
go

CREATE TABLE [Korisnik]
( 
	[KorisnickoIme]      varchar(100)  NOT NULL ,
	[Ime]                varchar(100)  NOT NULL ,
	[Prezime]            varchar(100)  NOT NULL ,
	[Sifra]              varchar(100)  NOT NULL ,
	[BrojPoslatihPaketa] integer  NOT NULL 
	CONSTRAINT [PodrazumevanoNula_673755515]
		 DEFAULT  0
	CONSTRAINT [MinVrednostNula_171452448]
		CHECK  ( BrojPoslatihPaketa >= 0 )
)
go

CREATE TABLE [Kurir]
( 
	[KorisnickoIme]      varchar(100)  NOT NULL ,
	[BrojIsporucenihPaketa] integer  NOT NULL 
	CONSTRAINT [PodrazumevanoNula_695361418]
		 DEFAULT  0
	CONSTRAINT [MinVrednostNula_1203295895]
		CHECK  ( BrojIsporucenihPaketa >= 0 ),
	[OstvarenProfit]     decimal(10,3)  NOT NULL 
	CONSTRAINT [PodrazumevanoNula_1215235686]
		 DEFAULT  0,
	[Status]             integer  NOT NULL 
	CONSTRAINT [PodrazumevanoNula_521202510]
		 DEFAULT  0
	CONSTRAINT [StatusKurira_109272485]
		CHECK  ( [Status]=0 OR [Status]=1 ),
	[RegBroj]            varchar(100)  NOT NULL 
)
go

CREATE TABLE [Opstina]
( 
	[IdOpstina]          integer  IDENTITY  NOT NULL ,
	[Naziv]              varchar(100)  NOT NULL ,
	[X_koordinata]       integer  NOT NULL 
	CONSTRAINT [Koordinate_14864170]
		CHECK  ( X_koordinata >= 0 ),
	[Y_koordinata]       integer  NOT NULL 
	CONSTRAINT [Koordinate_14864426]
		CHECK  ( Y_koordinata >= 0 ),
	[IdGrad]             integer  NOT NULL 
)
go

CREATE TABLE [Paket]
( 
	[IdPaket]            integer  IDENTITY  NOT NULL ,
	[StatusIsporuke]     integer  NULL 
	CONSTRAINT [StatusPaketa_1217450914]
		CHECK  ( [StatusIsporuke]=0 OR [StatusIsporuke]=1 OR [StatusIsporuke]=2 OR [StatusIsporuke]=3 ),
	[Cena]               decimal(10,3)  NULL 
	CONSTRAINT [MinVrednostNula_961118076]
		CHECK  ( Cena >= 0 ),
	[VremePrihvatanjaZahteva] datetime  NULL ,
	[Kurir]              varchar(100)  NULL ,
	[IdZahtev]           integer  NOT NULL 
)
go

CREATE TABLE [Ponuda]
( 
	[IdPonuda]           integer  IDENTITY  NOT NULL ,
	[Kurir]              varchar(100)  NOT NULL ,
	[IdPaket]            integer  NOT NULL ,
	[ProcenatCeneIsporuke] decimal(10,3)  NOT NULL 
)
go

CREATE TABLE [Vozilo]
( 
	[RegBroj]            varchar(100)  NOT NULL ,
	[TipGoriva]          integer  NOT NULL 
	CONSTRAINT [TipGoriva_2021023082]
		CHECK  ( [TipGoriva]=0 OR [TipGoriva]=1 OR [TipGoriva]=2 ),
	[Potrosnja]          decimal(10,3)  NOT NULL 
)
go

CREATE TABLE [ZahtevZaKurira]
( 
	[RegBroj]            varchar(100)  NOT NULL ,
	[KorisnickoIme]      varchar(100)  NOT NULL 
)
go

CREATE TABLE [ZahtevZaPrevoz]
( 
	[IdZahtev]           integer  IDENTITY  NOT NULL ,
	[OpstinaSlanja]      integer  NOT NULL ,
	[OpstinaPreuzimanja] integer  NOT NULL ,
	[TipPaketa]          integer  NOT NULL 
	CONSTRAINT [TipPaketa_710870072]
		CHECK  ( [TipPaketa]=0 OR [TipPaketa]=1 OR [TipPaketa]=2 ),
	[TezinaPaketa]       decimal(10,3)  NOT NULL 
	CONSTRAINT [MinVrednostNula_66791271]
		CHECK  ( TezinaPaketa >= 0 ),
	[Klijent]            varchar(100)  NOT NULL 
)
go

ALTER TABLE [Admin]
	ADD CONSTRAINT [XPKAdmin] PRIMARY KEY  CLUSTERED ([KorisnickoIme] ASC)
go

ALTER TABLE [Grad]
	ADD CONSTRAINT [XPKGrad] PRIMARY KEY  CLUSTERED ([IdGrad] ASC)
go

ALTER TABLE [Korisnik]
	ADD CONSTRAINT [XPKKorisnik] PRIMARY KEY  CLUSTERED ([KorisnickoIme] ASC)
go

ALTER TABLE [Kurir]
	ADD CONSTRAINT [XPKKurir] PRIMARY KEY  CLUSTERED ([KorisnickoIme] ASC)
go

ALTER TABLE [Opstina]
	ADD CONSTRAINT [XPKOpstina] PRIMARY KEY  CLUSTERED ([IdOpstina] ASC)
go

ALTER TABLE [Paket]
	ADD CONSTRAINT [XPKPaket] PRIMARY KEY  CLUSTERED ([IdPaket] ASC)
go

ALTER TABLE [Ponuda]
	ADD CONSTRAINT [XPKPonuda] PRIMARY KEY  CLUSTERED ([IdPonuda] ASC)
go

ALTER TABLE [Vozilo]
	ADD CONSTRAINT [XPKVozilo] PRIMARY KEY  CLUSTERED ([RegBroj] ASC)
go

ALTER TABLE [ZahtevZaKurira]
	ADD CONSTRAINT [XPKZahtevZaKurira] PRIMARY KEY  CLUSTERED ([KorisnickoIme] ASC)
go

ALTER TABLE [ZahtevZaPrevoz]
	ADD CONSTRAINT [XPKZahtevZaPrevoz] PRIMARY KEY  CLUSTERED ([IdZahtev] ASC)
go


ALTER TABLE [Admin]
	ADD CONSTRAINT [R_4] FOREIGN KEY ([KorisnickoIme]) REFERENCES [Korisnik]([KorisnickoIme])
		ON DELETE CASCADE
		ON UPDATE CASCADE
go


ALTER TABLE [Kurir]
	ADD CONSTRAINT [R_3] FOREIGN KEY ([KorisnickoIme]) REFERENCES [Korisnik]([KorisnickoIme])
		ON DELETE CASCADE
		ON UPDATE CASCADE
go

ALTER TABLE [Kurir]
	ADD CONSTRAINT [R_5] FOREIGN KEY ([RegBroj]) REFERENCES [Vozilo]([RegBroj])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Opstina]
	ADD CONSTRAINT [R_1] FOREIGN KEY ([IdGrad]) REFERENCES [Grad]([IdGrad])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Paket]
	ADD CONSTRAINT [R_11] FOREIGN KEY ([Kurir]) REFERENCES [Kurir]([KorisnickoIme])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Paket]
	ADD CONSTRAINT [R_17] FOREIGN KEY ([IdZahtev]) REFERENCES [ZahtevZaPrevoz]([IdZahtev])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Ponuda]
	ADD CONSTRAINT [R_14] FOREIGN KEY ([Kurir]) REFERENCES [Kurir]([KorisnickoIme])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Ponuda]
	ADD CONSTRAINT [R_15] FOREIGN KEY ([IdPaket]) REFERENCES [Paket]([IdPaket])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [ZahtevZaKurira]
	ADD CONSTRAINT [R_7] FOREIGN KEY ([RegBroj]) REFERENCES [Vozilo]([RegBroj])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [ZahtevZaKurira]
	ADD CONSTRAINT [R_18] FOREIGN KEY ([KorisnickoIme]) REFERENCES [Korisnik]([KorisnickoIme])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [ZahtevZaPrevoz]
	ADD CONSTRAINT [R_9] FOREIGN KEY ([OpstinaSlanja]) REFERENCES [Opstina]([IdOpstina])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [ZahtevZaPrevoz]
	ADD CONSTRAINT [R_10] FOREIGN KEY ([OpstinaPreuzimanja]) REFERENCES [Opstina]([IdOpstina])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [ZahtevZaPrevoz]
	ADD CONSTRAINT [R_13] FOREIGN KEY ([Klijent]) REFERENCES [Korisnik]([KorisnickoIme])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

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
