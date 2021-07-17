/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.VehicleOperations;

/**
 *
 * @author Jovana
 */
public class jj170586_VehicleOperations implements VehicleOperations {

    @Override
    public boolean insertVehicle(String licencePlateNumber, int fuelType, BigDecimal fuelConsumption) {
        Connection conn = DB.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement("select * from Vozilo where RegBroj=?")) {
            ps.setString(1, licencePlateNumber);
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next()) {
                    System.out.println("Vozilo sa ovim registarskim brojem vec postoji!");
                    return false;
                } else {
                    try (PreparedStatement ps1 = conn.prepareStatement("insert into Vozilo(RegBroj, TipGoriva, Potrosnja) values(?,?,?)")) {
                        ps1.setString(1, licencePlateNumber);
                        ps1.setInt(2, fuelType);
                        ps1.setBigDecimal(3, fuelConsumption);
                        ps1.executeUpdate();
                        System.out.println("Uspesno ste uneli novo vozilo u bazu!");
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

    //mislim da ova metoda vraca broj izbrisanih redova, proveri to ako nesto ne bude radilo
    @Override
    public int deleteVehicles(String... vehicles) {
        Connection connection = DB.getInstance().getConnection();
        String v = "";
        if (vehicles.length == 0) {
            return -1;
        }
        if (vehicles.length == 1) {
            try (PreparedStatement ps = connection.prepareStatement("delete from Vozilo where RegBroj=?");) {
                ps.setString(1, vehicles[0]);
                return ps.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(jj170586_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            for (int i = 1; i < vehicles.length; i++) {
                v += " or RegBroj=?";
            }
            try (PreparedStatement ps = connection.prepareStatement("delete from Vozilo RegBroj=?" + v);) {
                ps.setString(1, vehicles[0]);
                for (int i = 1; i < vehicles.length; i++) {
                    ps.setString(i + 1, vehicles[i]);
                }
                return ps.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(jj170586_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return -1;
    }

    @Override
    public List<String> getAllVehichles() {
        List<String> list = new ArrayList<>();
        Connection conn = DB.getInstance().getConnection();
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("select * from Vozilo");) {
            while (rs.next()) {
                list.add(rs.getString("RegBroj"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(jj170586_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    @Override
    public boolean changeFuelType(String licencePlateNumber, int fuelType) {
        Connection conn = DB.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement("select * from Vozilo where RegBroj=?")) {
            ps.setString(1, licencePlateNumber);
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next()) {
                    try (PreparedStatement ps1 = conn.prepareStatement("update Vozilo set TipGoriva=? where RegBroj=?")) {
                        ps1.setInt(1, fuelType);
                        ps1.setString(2, licencePlateNumber);
                        ps1.executeUpdate();
                        System.out.println("Uspesno ste promenili tip goriva na vozilu " + licencePlateNumber);
                        return true;
                    } catch (SQLException ex) {
                        Logger.getLogger(jj170586_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    System.out.println("Vozilo " + licencePlateNumber + " ne postoji u bazi podataka, pa ne moze da se promeni tip goriva!");
                    return false;
                }
            } catch (SQLException ex) {
                Logger.getLogger(jj170586_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(jj170586_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public boolean changeConsumption(String licencePlateNumber, BigDecimal fuelConsumption) {
        Connection conn = DB.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement("select * from Vozilo where RegBroj=?")) {
            ps.setString(1, licencePlateNumber);
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next()) {
                    try (PreparedStatement ps1 = conn.prepareStatement("update Vozilo set Potrosnja=? where RegBroj=?")) {
                        ps1.setBigDecimal(1, fuelConsumption);
                        ps1.setString(2, licencePlateNumber);
                        ps1.executeUpdate();
                        System.out.println("Uspesno ste promenili potrosnju na vozilu " + licencePlateNumber);
                        return true;
                    } catch (SQLException ex) {
                        Logger.getLogger(jj170586_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    System.out.println("Vozilo " + licencePlateNumber + " ne postoji u bazi podataka, pa ne moze da se promeni potrosnja vozila!");
                    return false;
                }
            } catch (SQLException ex) {
                Logger.getLogger(jj170586_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(jj170586_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
