/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.CourierOperations;

/**
 *
 * @author Jovana
 */
//za ovu klasu ne postoji javni test, moguce da ce to testirati tajnim testom, proveri to
public class jj170586_CourierOperations implements CourierOperations {

    //moja pretpostavka je da prvo kao parametar ide username, a onda regBroj
    //vidi kod kurira ostala polja, da mozda promenis, da budu null ili da postoji neka podrazumevana vrednost
    @Override
    public boolean insertCourier(String username, String licencePlateNumber) {
        Connection conn = DB.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement("select * from Korisnik where KorisnickoIme=?");) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    try (PreparedStatement ps1 = conn.prepareStatement("select * from Vozilo where RegBroj=?")) {
                        ps1.setString(1, licencePlateNumber);
                        try (ResultSet rs1 = ps1.executeQuery()) {
                            if (rs1.next()) {
                                try (PreparedStatement ps2 = conn.prepareStatement("select * from Kurir where KorisnickoIme=? and RegBroj=?")) {
                                    ps2.setString(1, username);
                                    ps2.setString(2, licencePlateNumber);
                                    try (ResultSet rs2 = ps2.executeQuery()) {
                                        if (rs2.next()) {
                                            System.out.println("Kurir " + username + " sa registracionim tablicama " + licencePlateNumber + " vec postoji");
                                            return false;
                                        } else {
                                            try (PreparedStatement ps3 = conn.prepareStatement("select * from Kurir where KorisnickoIme=?")) {
                                                ps3.setString(1, username);
                                                try (ResultSet rs3 = ps3.executeQuery()) {
                                                    if (rs3.next()) {
                                                        System.out.println("Kurir " + username + " vec postoji!");
                                                        return false;
                                                    } else {
                                                        try (PreparedStatement ps4 = conn.prepareStatement("insert into Kurir(KorisnickoIme, RegBroj) values(?,?)")) {
                                                            ps4.setString(1, username);
                                                            ps4.setString(2, licencePlateNumber);
                                                            ps4.executeUpdate();
                                                            return true;
                                                        } catch (SQLException ex) {
                                                            Logger.getLogger(jj170586_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
                                                        }
                                                    }
                                                } catch (SQLException ex) {
                                                    Logger.getLogger(jj170586_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
                                                }
                                            } catch (SQLException ex) {
                                                Logger.getLogger(jj170586_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                        }
                                    } catch (SQLException ex) {
                                        Logger.getLogger(jj170586_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                } catch (SQLException ex) {
                                    Logger.getLogger(jj170586_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            } else {
                                System.out.println("Vozilo " + licencePlateNumber + " ne postoji u bazi podataka, pa se ne moze dodeliti kuriru!");
                                return false;
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(jj170586_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(jj170586_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    System.out.println("Korisnik " + username + " ne postoji u bazi podataka, pa ne moze biti kurir");
                    return false;
                }
            } catch (SQLException ex) {
                Logger.getLogger(jj170586_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(jj170586_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    //pretpostavljam da je parametar username, cim je tipa string
    //vidi da li treba iz korisnika isto da brisem
    @Override
    public boolean deleteCourier(String username) {
        Connection conn = DB.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement("select * from Kurir where KorisnickoIme=?")) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    try (PreparedStatement ps1 = conn.prepareStatement("delete from Kurir where KorisnickoIme=?")) {
                        ps1.setString(1, username);
                        ps1.executeUpdate();
                        return true;
                    } catch (SQLException ex) {
                        Logger.getLogger(jj170586_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    System.out.print("Kurir " + username + " ne postoji u bazi, pa ne moze biti obrisan!");
                    return false;
                }
            } catch (SQLException ex) {
                Logger.getLogger(jj170586_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(jj170586_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public List<String> getCouriersWithStatus(int status) {
        List<String> list = new ArrayList<>();
        Connection conn = DB.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement("select * from Kurir where Status=?");) {
            ps.setInt(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(rs.getString("KorisnickoIme"));
                }
            } catch (SQLException ex) {
                Logger.getLogger(jj170586_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(jj170586_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    @Override
    public List<String> getAllCouriers() {
        List<String> list = new ArrayList<>();
        Connection conn = DB.getInstance().getConnection();
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("select * from Kurir");) {
            while (rs.next()) {
                list.add(rs.getString("KorisnickoIme"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(jj170586_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    //ne znam da li su ovo hteli, pitaj na licu mesta ako nesto bude pucalo ovde
    @Override
    public BigDecimal getAverageCourierProfit(int numOfDeliveredPackages) {
        Connection conn = DB.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement("select avg(OstvarenProfit) from Kurir where BrojIsporucenihPaketa>=?")) {
            ps.setInt(1, numOfDeliveredPackages);
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next()) {
                    return rs.getBigDecimal(1);
                }
            } catch (SQLException ex) {
                Logger.getLogger(jj170586_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(jj170586_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new BigDecimal(-1);
    }

}
