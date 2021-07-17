/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.sql.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.CourierRequestOperation;

/**
 *
 * @author Jovana
 */
public class jj170586_CourierRequestOperation implements CourierRequestOperation {

    //ja ovde nisam blokirala ako vec postoji zahtev drugog kurira za taj auto
    //takodje nisam blokirala ako je on vec podneo zahtev, ne znam da li moze da podnese vise zahteva za razliciti auto
    @Override
    public boolean insertCourierRequest(String username, String licencePlateNumber) {
        Connection conn = DB.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement("select * from Vozilo where RegBroj=?")) {
            ps.setString(1, licencePlateNumber);
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next()) {
                    try (PreparedStatement ps2 = conn.prepareStatement("select * from Korisnik where KorisnickoIme=?")) {
                        ps2.setString(1, username);
                        try (ResultSet rs2 = ps2.executeQuery()) {
                            if (rs2.next()) {
                                try (PreparedStatement ps3 = conn.prepareStatement("select * from ZahtevZaKurira where KorisnickoIme=? and RegBroj=?")) {
                                    ps3.setString(1, username);
                                    ps3.setString(2, licencePlateNumber);
                                    try (ResultSet rs3 = ps3.executeQuery()) {
                                        if (rs3.next()) {
                                            System.out.println("Zahtev za ovog kurira sa ovim kolima vec postoji!");
                                            return false;
                                        } else {
                                            try (PreparedStatement ps1 = conn.prepareStatement("insert into ZahtevZaKurira(KorisnickoIme, RegBroj) values(?,?)")) {
                                                ps1.setString(1, username);
                                                ps1.setString(2, licencePlateNumber);
                                                ps1.executeUpdate();
                                                return true;
                                            } catch (SQLException ex) {
                                                Logger.getLogger(jj170586_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                        }
                                    }
                                } catch (SQLException ex) {
                                    Logger.getLogger(jj170586_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            } else {
                                System.out.println("Korisnik " + username + " ne postoji u bazi, pa ne moze da podnese zahtev da postane kurir");
                                return false;
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(jj170586_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(jj170586_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    System.out.println("U bazi ne postoji vozilo sa reg brojem " + licencePlateNumber + ", pa se ne moze kreirati zahtev za kurira");
                    return false;
                }
            } catch (SQLException ex) {
                Logger.getLogger(jj170586_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(jj170586_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    //pretpostavljam da je parametar username kurira
    @Override
    public boolean deleteCourierRequest(String username) {
        Connection conn = DB.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement("select * from ZahtevZaKurira where KorisnickoIme=?")) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    try (PreparedStatement ps1 = conn.prepareStatement("delete from ZahtevZaKurira where KorisnickoIme=?")) {
                        ps1.setString(1, username);
                        ps1.executeUpdate();
                        return true;
                    } catch (SQLException ex) {
                        Logger.getLogger(jj170586_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    System.out.println("Zahtev za ovog kurira ne postoji, pa ne moze biti obrisan!");
                    return false;
                }
            } catch (SQLException ex) {
                Logger.getLogger(jj170586_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(jj170586_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public boolean changeVehicleInCourierRequest(String username, String licencePlateNumber) {
        Connection conn = DB.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement("select * from ZahtevZaKurira where KorisnickoIme=?")) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    try (PreparedStatement ps1 = conn.prepareStatement("select * from Vozilo where RegBroj=?")) {
                        ps1.setString(1, licencePlateNumber);
                        try (ResultSet rs1 = ps1.executeQuery()) {
                            if (rs1.next()) {
                                try (PreparedStatement ps2 = conn.prepareStatement("update ZahtevZaKurira set RegBroj=? where KorisnickoIme=?")) {
                                    ps2.setString(1, licencePlateNumber);
                                    ps2.setString(2, username);
                                    ps2.executeUpdate();
                                    return true;
                                } catch (SQLException ex) {
                                    Logger.getLogger(jj170586_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            } else {
                                System.out.println("Vozilo sa registracijom " + licencePlateNumber + " ne postji!");
                                return false;
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(jj170586_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(jj170586_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    System.out.println("U zahtevima ne postoji zahtev za ovog kurira!");
                    return false;
                }
            } catch (SQLException ex) {
                Logger.getLogger(jj170586_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(jj170586_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    //pretpostavljam da dohvata kurire, tj njihova korisnickaImena
    @Override
    public List<String> getAllCourierRequests() {
        List<String> list = new ArrayList<>();
        Connection conn = DB.getInstance().getConnection();
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("select * from ZahtevZaKurira");) {
            while (rs.next()) {
                list.add(rs.getString("KorisnickoIme"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(jj170586_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    //treba prvo proveriti da li takav korisnik postoji uopste kao korisnik u bazi
    //zatim treba proveriti da li je on vec postao kurir
    //tek nakon toga mozemo da pozovemo proceduru
    @Override
    public boolean grantRequest(String username) {
        Connection conn = DB.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement("select * from Korisnik where KorisnickoIme=?");) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    try (PreparedStatement ps1 = conn.prepareStatement("select * from Kurir where KorisnickoIme=?")) {
                        ps1.setString(1, username);
                        try (ResultSet rs1 = ps1.executeQuery()) {
                            if (rs1.next()) {
                                System.out.println("Ovaj korisnik je vec kurir!");
                                return false;
                            } else {
                                try (PreparedStatement ps2 = conn.prepareStatement("select * from ZahtevZaKurira where KorisnickoIme=?")) {
                                    ps2.setString(1, username);
                                    try (ResultSet rs2 = ps2.executeQuery()) {
                                        if (rs2.next()) {
                                            ////TEK OVDE SMEM DA POKUSAM DA MU ODOBRIM DA POSTANE KURIR
                                            //ja mogu toj proceduri da prosledim korisnickoIme i registracioniBroj
                                            String query = "{ call grantRequest (?,?) }";
                                            try (CallableStatement cs = conn.prepareCall(query)) {
                                                cs.setString(1, username);
                                                cs.setString(2, rs2.getString("RegBroj"));
                                                cs.execute();
                                                System.out.println("Uspesno je postao kurir " + username);
                                                return true;
                                            } catch (SQLException ex) {
                                                Logger.getLogger(jj170586_CourierRequestOperation.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                        } else {
                                            System.out.println("Ovaj korisnik nije uputio zahtev da postane kurir");
                                            return false;
                                        }
                                    } catch (SQLException ex) {
                                        Logger.getLogger(jj170586_CourierRequestOperation.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                } catch (SQLException ex) {
                                    Logger.getLogger(jj170586_CourierRequestOperation.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(jj170586_CourierRequestOperation.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(jj170586_CourierRequestOperation.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    System.out.println("Ovaj korisnik ne postoji u bazi podataka, pa nije mogao da podnese zahtev za kurira niti da postane kurir!");
                    return false;
                }
            } catch (SQLException ex) {
                Logger.getLogger(jj170586_CourierRequestOperation.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(jj170586_CourierRequestOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

}
