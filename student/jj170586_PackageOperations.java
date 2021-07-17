/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.PackageOperations;

/**
 *
 * @author Jovana
 */
public class jj170586_PackageOperations implements PackageOperations {

    //metode iz klase UTIL, cim je ovde int stavljen za X,Y => znaci da koordinate treba da budu tog tipa
    private double euclidean(final int x1, final int y1, final int x2, final int y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    //ovde sam dodala za tezinski faktor kod case 2 *2 jer to nije bilo uracunato, ako to ne bude radilo, onda mozda to obrisi
    static BigDecimal getPackagePrice(final int type, final BigDecimal weight, final double distance, BigDecimal percentage) {
        percentage = percentage.divide(new BigDecimal(100));
        switch (type) {
            case 0: {
                return new BigDecimal(10.0 * distance).multiply(percentage.add(new BigDecimal(1)));
            }
            case 1: {
                return new BigDecimal((25.0 + weight.doubleValue() * 100.0) * distance).multiply(percentage.add(new BigDecimal(1)));
            }
            case 2: {
                //ovde sam dodala kod weight *2 jer je to tezinski faktor
                return new BigDecimal((75.0 + (weight.doubleValue() * 2) * 300.0) * distance).multiply(percentage.add(new BigDecimal(1)));
            }
            default: {
                return null;
            }
        }
    }

    /* NEKE METODE KOJE SE KORISTE VISE PUTA U PROJEKTU */
    private BigDecimal getCenaPaketa(int idPaket) {
        Connection conn = DB.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement("select Cena from Paket where IdPaket=?");) {
            ps.setInt(1, idPaket);
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next()) {
                    return rs.getBigDecimal("Cena");
                } else {
                    return null;
                }
            } catch (SQLException ex) {
                Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    //vidi ovde da mozda ne vracas null, da ne bi pucao exception
    private String getRegBrojKurira(String usernameCourier) {
        Connection conn = DB.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement("select * from Kurir where KorisnickoIme=?");) {
            ps.setString(1, usernameCourier);
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next()) {
                    return rs.getString("RegBroj");
                } else {
                    return null;
                }
            } catch (SQLException ex) {
                Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private int prviPaketSortiranPoVremenu(String usernameCourier) {
        Connection conn = DB.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement("select * from Paket where Kurir=? and StatusIsporuke=2 order by VremePrihvatanjaZahteva");) {
            ps.setString(1, usernameCourier);
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next()) {
                    return rs.getInt("IdPaket");
                } else {
                    return -1;
                }
            } catch (SQLException ex) {
                Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    private BigDecimal getPotorsnja(String RegBroj) {
        Connection conn = DB.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement("select Potrosnja from Vozilo where RegBroj=?");) {
            ps.setString(1, RegBroj);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("Potrosnja");
                } else {
                    return null;
                }
            } catch (SQLException ex) {
                Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private int getTipGoriva(String RegBroj) {
        Connection conn = DB.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement("select TipGoriva from Vozilo where RegBroj=?");) {
            ps.setString(1, RegBroj);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("TipGoriva");
                } else {
                    return -1;
                }
            } catch (SQLException ex) {
                Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    private int CenaGorivaPoLitru(int TipGoriva) {
        switch (TipGoriva) {
            case 0:
                return 15;
            case 1:
                return 32;
            case 2:
                return 36;
            default:
                return -1;
        }
    }

    //napravicemo dve metode za dovatanje koordivnata opstina
    private jj170586_Pair<Integer, Integer> getKoordinataOpstineSlanje(int idPaket) {
        Connection conn = DB.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement("select OpstinaSlanja\n"
                + "from ZahtevZaPrevoz join Paket on ZahtevZaPrevoz.IdZahtev = Paket.IdZahtev\n"
                + "and IdPaket= ?");) {
            ps.setInt(1, idPaket);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    try (PreparedStatement ps1 = conn.prepareStatement("select X_koordinata, Y_koordinata from Opstina where IdOpstina=?");) {
                        ps1.setInt(1, rs.getInt("OpstinaSlanja"));
                        try (ResultSet rs1 = ps1.executeQuery();) {
                            if (rs1.next()) {
                                return new jj170586_Pair<>(rs1.getInt("X_koordinata"), rs1.getInt("Y_koordinata"));
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    return null;
                }
            } catch (SQLException ex) {
                Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    //napravicemo dve metode za dovatanje koordivnata opstina
    private jj170586_Pair<Integer, Integer> getKoordinataOpstinePreuzimanje(int idPaket) {
        Connection conn = DB.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement("select OpstinaPreuzimanja\n"
                + "from ZahtevZaPrevoz join Paket on ZahtevZaPrevoz.IdZahtev = Paket.IdZahtev\n"
                + "and IdPaket= ?");) {
            ps.setInt(1, idPaket);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    try (PreparedStatement ps1 = conn.prepareStatement("select X_koordinata, Y_koordinata from Opstina where IdOpstina=?");) {
                        ps1.setInt(1, rs.getInt("OpstinaPreuzimanja"));
                        try (ResultSet rs1 = ps1.executeQuery();) {
                            if (rs1.next()) {
                                return new jj170586_Pair<>(rs1.getInt("X_koordinata"), rs1.getInt("Y_koordinata"));
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    return null;
                }
            } catch (SQLException ex) {
                Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    //districtFrom, districtTo, username, packageType, weight
    //treba da vrati -1 ako nije u redu, a vrv IdZahteva ukoliko je sve okej
    //ja ovde radim sa zahtevom za prevoz, ali i paketom
    //probacu da ne radim sve provere, posto ce svakako puci ukoliko nesto ne postoji kao strani kljuc,
    @Override
    public int insertPackage(int districtFrom, int districtTo, String username, int packageType, BigDecimal weight) {
        Connection conn = DB.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement("insert into ZahtevZaPrevoz(OpstinaSlanja, OpstinaPreuzimanja, TipPaketa, TezinaPaketa, Klijent) values(?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);) {
            ps.setInt(1, districtFrom);
            ps.setInt(2, districtTo);
            ps.setInt(3, packageType);
            ps.setBigDecimal(4, weight);
            ps.setString(5, username);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    System.out.println("Kreiran je novi zahteva za prevoz Paketa " + rs.getInt(1));
                    int IdRequest = rs.getInt(1);
                    try (PreparedStatement ps1 = conn.prepareStatement("insert into Paket(IdZahtev) values(?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
                        ps1.setInt(1, IdRequest);
                        ps1.executeUpdate();
                        try (ResultSet rs1 = ps1.getGeneratedKeys()) {
                            if (rs1.next()) {
                                //vracamo IdPaketa koji je kreiran na osnovu zahteva, test zahteva IdPaketa
                                System.out.println("Kreiran je novi paket za prevoz paketa " + rs1.getInt(1));
                                return rs1.getInt(1);
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    @Override
    public int insertTransportOffer(String usernameCourier, int idPackage, BigDecimal pricePercentage) {
        Connection conn = DB.getInstance().getConnection();
		try(PreparedStatement ps = conn.prepareStatement("select Status from Kurir where KorisnickoIme=?")){
			ps.setString(1, usernameCourier);
			try(ResultSet rs = ps.executeQuery()){
				if(rs.next()){
					if(rs.getInt("Status") == 1) return -1;
				}
			} catch (SQLException ex) {
                Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
		} catch (SQLException ex) {
            Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
		
        try (PreparedStatement ps = conn.prepareStatement("insert into Ponuda(Kurir, IdPaket, ProcenatCeneIsporuke) values(?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);) {
            ps.setString(1, usernameCourier);
            ps.setInt(2, idPackage);
            ps.setBigDecimal(3, pricePercentage);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    System.out.println("Kreiran je nova ponuda za prevoz Paketa sa Id " + rs.getInt(1) + " od kurira" + usernameCourier);
                    return rs.getInt(1);
                }
            } catch (SQLException ex) {
                Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    //vidi ovo, nisam sigurna sta sve treba da se odradi ovde
    //ja mislim da treba da se obrisu sve ostale ponude vezane za taj paket =>To se radi u triggeru, sve OK!
    //i da se u paket upise update vrednost za cenu, kurira, status i to je to
    @Override
    public boolean acceptAnOffer(int offerId) {
        Connection conn = DB.getInstance().getConnection();

        int idPackage; //id paketa
        int idRequest; //zahtev za paket
        BigDecimal percentage; //procenat koji trazi kurir
        int packageType; //tip paketa
        BigDecimal weight; //tezina paketa, bez tezinskog faktora
        BigDecimal price; //cena
        String usernameCourier;
        String client; //da bih povecala broj poslatih od strane tog klijenta

        //za racunanje euklidskog rastojanja
        int X_coordinateFrom;
        int Y_coordinateFrom;
        int X_coordinateTo;
        int Y_coordinateTo;
        double euclid;

        try (PreparedStatement ps = conn.prepareStatement("select * from Ponuda where IdPonuda=?");) {
            ps.setInt(1, offerId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usernameCourier = rs.getString("Kurir");
                    idPackage = rs.getInt("IdPaket");
                    percentage = rs.getBigDecimal("ProcenatCeneIsporuke");
                    try (PreparedStatement ps1 = conn.prepareStatement("select IdZahtev from Paket where IdPaket=?")) {
                        ps1.setInt(1, idPackage);
                        try (ResultSet rs1 = ps1.executeQuery()) {
                            if (rs1.next()) {
                                idRequest = rs1.getInt("IdZahtev"); //ovo je zahtev za paket
                                try (PreparedStatement ps2 = conn.prepareStatement("select Klijent, OpstinaSlanja, OpstinaPreuzimanja, TipPaketa, TezinaPaketa from ZahtevZaPrevoz where IdZahtev=?")) {
                                    ps2.setInt(1, idRequest);
                                    try (ResultSet rs2 = ps2.executeQuery()) {
                                        if (rs2.next()) {
                                            packageType = rs2.getInt("TipPaketa");
                                            weight = rs2.getBigDecimal("TezinaPaketa");
                                            client = rs2.getString("Klijent");
                                            try (PreparedStatement ps3 = conn.prepareStatement("select X_koordinata, Y_koordinata from Opstina where IdOpstina=?");
                                                    PreparedStatement ps4 = conn.prepareStatement("select X_koordinata, Y_koordinata from Opstina where IdOpstina=?")) {
                                                ps3.setInt(1, rs2.getInt("OpstinaSlanja"));
                                                ps4.setInt(1, rs2.getInt("OpstinaPreuzimanja"));
                                                try (ResultSet rs3 = ps3.executeQuery()) {
                                                    if (rs3.next()) {
                                                        X_coordinateFrom = rs3.getInt("X_koordinata");
                                                        Y_coordinateFrom = rs3.getInt("Y_koordinata");
                                                        try (ResultSet rs4 = ps4.executeQuery()) {
                                                            if (rs4.next()) {
                                                                X_coordinateTo = rs4.getInt("X_koordinata");
                                                                Y_coordinateTo = rs4.getInt("Y_koordinata");
                                                                euclid = euclidean(X_coordinateFrom, Y_coordinateFrom, X_coordinateTo, Y_coordinateTo);
                                                                switch (packageType) {
                                                                    case 0: //radi se o pismu
                                                                        price = getPackagePrice(0, weight, euclid, percentage);
                                                                        break;
                                                                    case 1: //standardni paket
                                                                        price = getPackagePrice(1, weight, euclid, percentage);
                                                                        break;
                                                                    default:
                                                                        price = getPackagePrice(2, weight, euclid, percentage);
                                                                        break;
                                                                }
                                                                try (PreparedStatement ps5
                                                                        = conn.prepareStatement("update Paket set StatusIsporuke=?, Cena=?, Kurir=?, VremePrihvatanjaZahteva=? where IdPaket=?")) {
                                                                    ps5.setInt(1, 1);
                                                                    ps5.setBigDecimal(2, price);
                                                                    ps5.setString(3, usernameCourier);
                                                                    ps5.setTimestamp(4, java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
                                                                    ps5.setInt(5, idPackage);
                                                                    ps5.executeUpdate();
                                                                    try (PreparedStatement ps6
                                                                            = conn.prepareStatement("update Korisnik set BrojPoslatihPaketa=BrojPoslatihPaketa + 1 where KorisnickoIme=?")) {
                                                                        ps6.setString(1, client);
                                                                        ps6.executeUpdate();
                                                                        return true;
                                                                    } catch (SQLException ex) {
                                                                        Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
                                                                    }
                                                                } catch (SQLException ex) {
                                                                    Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
                                                                }
                                                            }
                                                        } catch (SQLException ex) {
                                                            Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
                                                        }
                                                    }
                                                } catch (SQLException ex) {
                                                    Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
                                                }
                                            } catch (SQLException ex) {
                                                Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                        }
                                    } catch (SQLException ex) {
                                        Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                } catch (SQLException ex) {
                                    Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    System.out.println("Zahtev sa ovim ID ne postoji, pa ne moze biti prihvacen");
                    return false;
                }
            } catch (SQLException ex) {
                Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public List<Integer> getAllOffers() {
        Connection conn = DB.getInstance().getConnection();
        List<Integer> list = new ArrayList<>();
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("select * from Ponuda");) {
            while (rs.next()) {
                list.add(rs.getInt("IdPonuda"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    @Override
    public List<Pair<Integer, BigDecimal>> getAllOffersForPackage(int idPackage) {
        Connection conn = DB.getInstance().getConnection();
        List<Pair<Integer, BigDecimal>> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("select * from Ponuda where IdPaket=?");) {
            ps.setInt(1, idPackage);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new jj170586_Pair(rs.getInt("IdPonuda"), rs.getBigDecimal("ProcenatCeneIsporuke")));
                }
            } catch (SQLException ex) {
                Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }

    //vidi ovde da li kad vec brises paket, da li treba da obrises i ponude za taj paket, kao i zahtev za prevoz tog paketa
    @Override
    public boolean deletePackage(int idPackage) {
        Connection conn = DB.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement("delete from Paket where IdPaket=?");) {
            ps.setInt(1, idPackage);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public boolean changeWeight(int idPackage, BigDecimal newWeight) {
        Connection conn = DB.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement("select * from Paket where IdPaket = ?");) {
            ps.setInt(1, idPackage);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int IdRequest = rs.getInt("IdZahtev");
                    try (PreparedStatement ps1 = conn.prepareStatement("update ZahtevZaPrevoz set TezinaPaketa=? where IdZahtev=?")) {
                        ps1.setBigDecimal(1, newWeight);
                        ps1.setInt(2, IdRequest);
                        ps1.executeUpdate();
                        return true;
                    } catch (SQLException ex) {
                        Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public boolean changeType(int idPackage, int type) {
        Connection conn = DB.getInstance().getConnection();
        if (type == 0 || type == 1 || type == 2) {
            try (PreparedStatement ps = conn.prepareStatement("select * from Paket where IdPaket=?");) {
                ps.setInt(1, idPackage);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        int IdRequest = rs.getInt("IdZahtev");
                        try (PreparedStatement ps1 = conn.prepareStatement("update ZahtevZaPrevoz set TipPaketa=? where IdZahtev=?")) {
                            ps1.setInt(1, type);
                            ps1.setInt(2, IdRequest);
                            ps1.executeUpdate();
                            return true;
                        } catch (SQLException ex) {
                            Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (SQLException ex) {
                Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
            return false;
        } else {
            System.out.println("Pogresan tip paketa!");
            return false;
        }
    }

    @Override
    public Integer getDeliveryStatus(int idPackage) {
        Connection conn = DB.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement("select * from Paket where IdPaket=?");) {
            ps.setInt(1, idPackage);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("StatusIsporuke");
                }
            } catch (SQLException ex) {
                Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    //ovo moram polako da vidim, da li se tu upisuje u stvari cena kada se sve preracuna => to je istina
    //meni je ovde defaultna cena NULL dok se ne izracuna cena paketa, tako da ce vratiti NULL ako je status 0
    //ja koliko kapiram, ovde se trazi da se cena paketa, ali je potrebno da status bude !=0(tj. da nije u statusu kreiran)
    // tj da bude izracunata cena paketa + dostava (ali mozda i moze da vrati cak i null ako jos nije preracunato)
    @Override
    public BigDecimal getPriceOfDelivery(int idPackage) {
        Connection conn = DB.getInstance().getConnection();
        int status = getDeliveryStatus(idPackage);
        if (status != 0) {
            try (PreparedStatement ps = conn.prepareStatement("select Cena from Paket where IdPaket=?");) {
                ps.setInt(1, idPackage);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getBigDecimal("Cena");
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (SQLException ex) {
                Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    @Override
    public Date getAcceptanceTime(int idPackage) {
        Connection conn = DB.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement("select * from Paket where IdPaket=?");) {
            ps.setInt(1, idPackage);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDate("VremePrihvatanjaZahteva");
                }
            } catch (SQLException ex) {
                Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List<Integer> getAllPackagesWithSpecificType(int type) {
        Connection conn = DB.getInstance().getConnection();
        List<Integer> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("select * from ZahtevZaPrevoz where TipPaketa=?");) {
            ps.setInt(1, type);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    try (PreparedStatement ps1 = conn.prepareStatement("select * from Paket where IdZahtev=?")) {
                        ps1.setInt(1, rs.getInt("IdZahtev"));
                        try (ResultSet rs1 = ps1.executeQuery()) {
                            if (rs1.next()) {
                                list.add(rs1.getInt("IdPaket"));
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    @Override
    public List<Integer> getAllPackages() {
        Connection conn = DB.getInstance().getConnection();
        List<Integer> list = new ArrayList<>();
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("select * from Paket");) {
            while (rs.next()) {
                list.add(rs.getInt("IdPaket"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(jj170586_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    /**
     * *********************** CEO ALGORITAM ZADATKA **************************************
     */
    //vraca pakete koje je kurir preuzeo i treba da ih vozi
    @Override
    public List<Integer> getDrive(String usernameCourier) {
        Connection conn = DB.getInstance().getConnection();
        List<Integer> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("select IdPaket from Paket where StatusIsporuke=2 and Kurir=?");) {
            ps.setString(1, usernameCourier);
            try (ResultSet rs = ps.executeQuery();) {
                while (rs.next()) {
                    list.add(rs.getInt("IdPaket"));
                }
            } catch (SQLException ex) {
                Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    //za cuvanje koordinata kurira, uzela sam TreeMap jer kazu da on postuje redosled stavljanja u mapu
    //a da hes mapa nije sigurna
    public static final int START_POSITION = -10; //kada se resetuje putanja za kurira, da mu budu te startne koordinate (-10,-10)
    private Map<String, jj170586_Pair<Integer, Integer>> putanjaKurira = new TreeMap<>();

    private int dohvatanjeJednogPaketa(int statusKurira, String usernameCourier) {
        Connection conn = DB.getInstance().getConnection();
        if (statusKurira == 0) {
            //znaci da je kurir u statusu da ne vozi, sto znaci da moramo da promenimo njegov status, kao i da stavimo
            //da se stavke koje on prevozi prevoze, tj da im promenimo status na 2
            String RegBroj = getRegBrojKurira(usernameCourier);
            try (PreparedStatement ps = conn.prepareStatement("select * from Kurir where RegBroj=? and Status=1")) {
                ps.setString(1, RegBroj);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) { //neko vozi ta kola trenutno, pa ovaj kurir ne moze da zapocne voznju nikako
                        System.out.println("Usao u rs next");
                        return -2;
                    } else { //potrebno da promenimo status kurira, da zapocne voznju, tj da mu stavimo status na 1
                        try (PreparedStatement ps1 = conn.prepareStatement("update Kurir set Status=1 where KorisnickoIme=?")) {
                            putanjaKurira.put(usernameCourier, new jj170586_Pair<>(START_POSITION, START_POSITION));
                            ps1.setString(1, usernameCourier);
                            ps1.executeUpdate();
                            System.out.println("Usao u ovaj deo za update");
                            try (PreparedStatement ps2 = conn.prepareStatement("update Paket set StatusIsporuke=2 where Kurir=? and StatusIsporuke=1")) {
                                ps2.setString(1, usernameCourier);
                                ps2.executeUpdate();
                            } catch (SQLException ex) {
                                Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (SQLException ex) {
                Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        //System.out.println("PROSAO ZA KURIRA");
        //ovo svakako treba da se odradi, jer mora uvek da uzme paket koji je najnoviji
        return prviPaketSortiranPoVremenu(usernameCourier);
    }

    private double PredjenaDistancaPrevozaJednogPaketa(int idPaket, String usernameCourier) {
        double predjenoRastojanje;
        if (putanjaKurira.get(usernameCourier).getFirstParam() == START_POSITION && putanjaKurira.get(usernameCourier).getSecondParam() == START_POSITION) {
            predjenoRastojanje = euclidean(getKoordinataOpstineSlanje(idPaket).getFirstParam(), getKoordinataOpstineSlanje(idPaket).getSecondParam(),
                    getKoordinataOpstinePreuzimanje(idPaket).getFirstParam(), getKoordinataOpstinePreuzimanje(idPaket).getSecondParam());
            putanjaKurira.put(usernameCourier, new jj170586_Pair<>(getKoordinataOpstinePreuzimanje(idPaket).getFirstParam(), getKoordinataOpstinePreuzimanje(idPaket).getSecondParam()));
            return predjenoRastojanje;
        } else {
            predjenoRastojanje = euclidean(putanjaKurira.get(usernameCourier).getFirstParam(), putanjaKurira.get(usernameCourier).getSecondParam(),
                    getKoordinataOpstineSlanje(idPaket).getFirstParam(), getKoordinataOpstineSlanje(idPaket).getSecondParam());
            predjenoRastojanje += euclidean(getKoordinataOpstineSlanje(idPaket).getFirstParam(), getKoordinataOpstineSlanje(idPaket).getSecondParam(),
                    getKoordinataOpstinePreuzimanje(idPaket).getFirstParam(), getKoordinataOpstinePreuzimanje(idPaket).getSecondParam());
            putanjaKurira.put(usernameCourier, new jj170586_Pair<>(getKoordinataOpstinePreuzimanje(idPaket).getFirstParam(), getKoordinataOpstinePreuzimanje(idPaket).getSecondParam()));
            return predjenoRastojanje;
        }
    }

    //ovde nisam sigurna da li se gleda cena paketa ili cena isporuke paketa koja predstavlja vrednost
    //cena paketa - procenat isporuke
    private BigDecimal profitZaJedanPaket(int idPaket, String usernameCourier) {
        if (getCenaPaketa(idPaket) != null) {
            double predjenoRastojanje = PredjenaDistancaPrevozaJednogPaketa(idPaket, usernameCourier);
            String RegBroj = getRegBrojKurira(usernameCourier);
            BigDecimal Potrosnja = getPotorsnja(RegBroj);
            int TipGoriva = getTipGoriva(RegBroj);
            int CenaGorivaLitar = CenaGorivaPoLitru(TipGoriva);
            BigDecimal CenaPaketa = getCenaPaketa(idPaket);
            if (CenaGorivaLitar != -1 && TipGoriva != -1 && Potrosnja != null && RegBroj != null) {
                BigDecimal ukupnoPotroseno = (Potrosnja).multiply(BigDecimal.valueOf(CenaGorivaLitar)).multiply(BigDecimal.valueOf(predjenoRastojanje));
                return CenaPaketa.subtract(ukupnoPotroseno);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public int driveNextPackage(String usernameCourier) {
        Connection conn = DB.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement("select Status from Kurir where KorisnickoIme=?");) {
            ps.setString(1, usernameCourier);
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next()) {
                    int idPaket = dohvatanjeJednogPaketa(rs.getInt("Status"), usernameCourier);
                    if (idPaket != -1 && idPaket != -2) { //ima paketa i niko ne vozi ta kola 
                        //postoji paket za isporuku, pa ce ga kurir prevesti, promeniti status paketu kada ga preveze
                        //i povecati ostvaren profit i broj isporucenih paketa
                        try (PreparedStatement ps2 = conn.prepareStatement("update Paket set StatusIsporuke=3 where IdPaket=?");) {
                            ps2.setInt(1, idPaket);
                            ps2.executeUpdate();
                            BigDecimal OstvarenProfit = profitZaJedanPaket(idPaket, usernameCourier);
                            if (OstvarenProfit != null) {
                                try (PreparedStatement ps1 = conn.prepareStatement("update Kurir\n"
                                        + "set OstvarenProfit=OstvarenProfit+?, BrojIsporucenihPaketa=BrojIsporucenihPaketa+1 where KorisnickoIme=?");) {
                                    ps1.setBigDecimal(1, OstvarenProfit);
                                    ps1.setString(2, usernameCourier);
                                    ps1.executeUpdate();
                                    return idPaket;
                                } catch (SQLException ex) {
                                    Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            } else {
                                return -3; //neka greska postoji
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else if (idPaket == -1) {
                        //kurir vise nema paketa za razvozenje, pa treba da promeni svoj status u status da ne vozi
                        try (PreparedStatement ps3 = conn.prepareStatement("update Kurir set Status=0 where KorisnickoIme=?");) {
                            ps3.setString(1, usernameCourier);
                            ps3.executeUpdate();
                            putanjaKurira.remove(usernameCourier); //ipak da se ne pretrpa to stablo
                            return 0; //nema vise paketa za slanje
                        } catch (SQLException ex) {
                            Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    return -2; //kurirova kola su zauzeta, pa ne moze da vozi ili prosto ima neki problem
                } else {
                    return -3;
                }
            } catch (SQLException ex) {
                Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(jj170586_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -3;
    }

}
