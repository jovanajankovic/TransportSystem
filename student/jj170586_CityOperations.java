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
import rs.etf.sab.operations.CityOperations;

/**
 *
 * @author Jovana
 */
public class jj170586_CityOperations implements CityOperations {

    //koliko kapiram, ovo treba da vraca Id te nove kolone
    //prvi parametar je ime, drugi parametar je postalCode
    @Override
    public int insertCity(String CityName, String PostalCode) {
        Connection conn = DB.getInstance().getConnection();
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("select * from Grad");) {
            while (rs.next()) {
                if ((rs.getString("Naziv").equals(CityName)) || (rs.getString("PostanskiBroj").equals(PostalCode))) {
                    System.out.println("Postoji takav grad sa tim imenom ili postanskim brojem");
                    return -1;
                }
            }
            try (PreparedStatement ps = conn.prepareStatement("insert into Grad(Naziv,PostanskiBroj) values(?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, CityName);
                ps.setString(2, PostalCode);
                ps.executeUpdate();
                try (ResultSet rs1 = ps.getGeneratedKeys();) {
                    if (rs1.next()) {
                        System.out.println("Kreiran je novi grad sa Id grada " + rs1.getInt(1));
                        return rs1.getInt(1);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(jj170586_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
                }

            } catch (SQLException ex) {
                Logger.getLogger(jj170586_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(jj170586_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    @Override
    public int deleteCity(String... cityNames) {
        Connection connection = DB.getInstance().getConnection();
        String cities = "";
        if (cityNames.length == 0) {
            return -1;
        }
        if (cityNames.length == 1) {
            try (PreparedStatement ps = connection.prepareStatement("delete from Grad where naziv=?");) {
                ps.setString(1, cityNames[0]);
                return ps.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(jj170586_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            for (int i = 1; i < cityNames.length; i++) {
                cities += " or naziv=?";
            }
            try (PreparedStatement ps = connection.prepareStatement("delete from Grad where naziv=?" + cities);) {
                ps.setString(1, cityNames[0]);
                for (int i = 1; i < cityNames.length; i++) {
                    ps.setString(i + 1, cityNames[i]);
                }
                return ps.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(jj170586_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return -1;
    }

    //mislim da je ovaj deo vezan za idGrada, tj. da se prosledjuje idGrada kao parametar
    @Override
    public boolean deleteCity(int idCity) {
        Connection conn = DB.getInstance().getConnection();
        boolean exists = false;
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("select * from Grad");) {
            while (rs.next()) {
                if (rs.getInt("IdGrad") == idCity) {
                    exists = true;
                    break;
                }
            }
            if (exists == false) {
                System.out.println("Grad sa ovim ID ne postoji");
                return false;
            } else {
                String query = "delete from Grad where IdGrad=?";
                try (PreparedStatement ps = conn.prepareStatement(query)) {
                    ps.setInt(1, idCity);
                    ps.executeUpdate();
                    System.out.println("Obrisan grad sa id" + idCity);
                    return true;
                } catch (SQLException ex) {
                    Logger.getLogger(jj170586_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(jj170586_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public List<Integer> getAllCities() {
        List<Integer> list = new ArrayList<>();
        Connection conn = DB.getInstance().getConnection();
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("select * from Grad");) {
            while (rs.next()) {
                list.add(rs.getInt("IdGrad"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(jj170586_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
}
