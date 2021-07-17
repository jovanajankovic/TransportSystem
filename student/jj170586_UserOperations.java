/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.UserOperations;

/**
 *
 * @author Jovana
 */
public class jj170586_UserOperations implements UserOperations {

    @Override
    public boolean insertUser(String username, String firstName, String lastName, String password) {
        Connection conn = DB.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement("select * from Korisnik where KorisnickoIme=?")) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next()) {
                    System.out.println("Korisnik sa ovim korisnickim imenom vec postoji");
                    return false;
                } else {
                    try (PreparedStatement ps1 = conn.prepareStatement("insert into Korisnik(KorisnickoIme, Ime, Prezime, Sifra) values(?,?,?,?)")) {
                        ps1.setString(1, username);
                        ps1.setString(2, firstName);
                        ps1.setString(3, lastName);
                        ps1.setString(4, password);
                        ps1.executeUpdate();
                        System.out.println("Uspesno ste uneli novog korisnika u bazu!");
                        return true;
                    } catch (SQLException ex) {
                        Logger.getLogger(jj170586_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(jj170586_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(jj170586_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    //iz testa mi deluje da prosto samo kao parametar primam korisnickoIme i to dodajem u tabelu Admin
    @Override
    public int declareAdmin(String username) {
        Connection conn = DB.getInstance().getConnection();
        int returnValue = -1;
        try (PreparedStatement ps = conn.prepareStatement("select * from Korisnik where KorisnickoIme=?")) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next()) {
                    try (PreparedStatement p2 = conn.prepareStatement("select * from Admin where KorisnickoIme=?")) {
                        p2.setString(1, username);
                        try (ResultSet rs1 = p2.executeQuery()) {
                            if (rs1.next()) {
                                System.out.println("Admin sa ovim korisnickim imenom vec postoji!");
                                return 1;
                            } else {
                                //treba da ga dodam u tabelu admin i da ga proglasim za admina na taj nacin
                                try (PreparedStatement ps1 = conn.prepareStatement("insert into Admin(KorisnickoIme) values(?)")) {
                                    ps1.setString(1, username);
                                    returnValue = ps1.executeUpdate();
                                    System.out.println("Uspesno ste postavili novog admina!");
                                    return 0;
                                } catch (SQLException ex) {
                                    Logger.getLogger(jj170586_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(jj170586_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(jj170586_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    //ne postoji, dakle ne mogu da ga proglasim za admina, pa vracam gresku
                    System.out.println("Zadati korisnik ne postoji u bazi, pa ne moze biti proglasen Adminom");
                    return 2;
                }
            } catch (SQLException ex) {
                Logger.getLogger(jj170586_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(jj170586_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return returnValue;
    }

    //nisam sigurna da li ova metoda vraca UKUPAN BROJ poslatih paketa koji su poslali ovi ljudi
    //ovo je okej sto se tice testa, zaista treba da vrati null ukoliko nema korisnika
    @Override
    public Integer getSentPackages(String... usernames) {
        Connection conn = DB.getInstance().getConnection();
        Integer packages = null;
        for (int i = 0; i < usernames.length; ++i) {
            try (PreparedStatement ps = conn.prepareStatement("select * from Korisnik where KorisnickoIme = ?");) {
                ps.setString(1, usernames[i]);
                try (ResultSet rs = ps.executeQuery();) {
                    if (rs.next()) {
                        if (packages == null) {
                            packages = 0;
                        }
                        packages += rs.getInt("BrojPoslatihPaketa"); //ja sam stavila u bazi default value da je 0, tako da ovo nikad nece biti null
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(jj170586_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (SQLException ex) {
                Logger.getLogger(jj170586_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return packages;
    }

    //vidi sta se desava sa userima kad se brisu, da li admin treba da se obrise
    @Override
    public int deleteUsers(String... usernames) {
        Connection connection = DB.getInstance().getConnection();
        String names = "";
        if (usernames.length == 0) {
            return -1;
        }
        if (usernames.length == 1) {
            try (PreparedStatement ps = connection.prepareStatement("delete from Korisnik where KorisnickoIme=?");) {
                ps.setString(1, usernames[0]);
                return ps.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(jj170586_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            for (int i = 1; i < usernames.length; i++) {
                names += " or KorisnickoIme=?";
            }
            try (PreparedStatement ps = connection.prepareStatement("delete from Korisnik where KorisnickoIme=?" + names);) {
                ps.setString(1, usernames[0]);
                for (int i = 1; i < usernames.length; i++) {
                    ps.setString(i + 1, usernames[i]);
                }
                return ps.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(jj170586_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return -1;
    }

    @Override
    public List<String> getAllUsers() {
        List<String> list = new ArrayList<>();
        Connection conn = DB.getInstance().getConnection();
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("select * from Korisnik");) {
            while (rs.next()) {
                list.add(rs.getString("KorisnickoIme"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(jj170586_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

}
