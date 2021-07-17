/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.DistrictOperations;

/**
 *
 * @author Jovana
 */
public class jj170586_DistrictOperations implements DistrictOperations {

    //pretpostavljam da mora da vrati id tog grada
    @Override
    public int insertDistrict(String districtName, int idCity, int x_coordinate, int y_coodrdinate) {
        Connection conn = DB.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement("select * from Grad where IdGrad=?");) {
            ps.setInt(1, idCity);
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next()) {
                    try (PreparedStatement ps1 = conn.prepareStatement("select * from Opstina where IdGrad=? and Naziv=?");) {
                        ps1.setInt(1, idCity);
                        ps1.setString(2, districtName);
                        try (ResultSet rs1 = ps1.executeQuery();) {
                            if (rs1.next()) {
                                System.out.println("Ova Opstina u ovom gradu vec postoji!");
                                return -1;
                            } else {
                                String query = "insert into Opstina(Naziv, IdGrad, X_koordinata, Y_koordinata) values(?, ?, ?, ?)";
                                try (PreparedStatement ps2 = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);) {
                                    ps2.setString(1, districtName);
                                    ps2.setInt(2, idCity);
                                    ps2.setInt(3, x_coordinate);
                                    ps2.setInt(4, y_coodrdinate);
                                    ps2.executeUpdate();
                                    try (ResultSet rs2 = ps2.getGeneratedKeys();) {
                                        if (rs2.next()) {
                                            System.out.println("Kreiran je novi grad sa Id grada " + rs2.getInt(1));
                                            return rs2.getInt(1);
                                        } else {
                                            return -1;
                                        }
                                    } catch (SQLException ex) {
                                        Logger.getLogger(jj170586_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                } catch (SQLException ex) {
                                    Logger.getLogger(jj170586_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(jj170586_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(jj170586_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    System.out.println("Grad kojem zelite da dodelite opstinu ne postoji!");
                    return -1;
                }
            } catch (SQLException ex) {
                Logger.getLogger(jj170586_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(jj170586_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    //dobija naziv districta
    @Override
    public int deleteDistricts(String... districtNames) {
        Connection connection = DB.getInstance().getConnection();
        String d = "";
        if (districtNames.length == 0) {
            return -1;
        }
        if (districtNames.length == 1) {
            try (PreparedStatement ps = connection.prepareStatement("delete from Opstina where Naziv=?");) {
                ps.setString(1, districtNames[0]);
                return ps.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(jj170586_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            for (int i = 1; i < districtNames.length; i++) {
                d += " or Naziv=?";
            }
            try (PreparedStatement ps = connection.prepareStatement("delete from Opstina where Naziv=?" + d);) {
                ps.setString(1, districtNames[0]);
                for (int i = 1; i < districtNames.length; i++) {
                    ps.setString(i + 1, districtNames[i]);
                }
                return ps.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(jj170586_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return -1;
    }

    @Override
    public boolean deleteDistrict(int idDistrict) {
        Connection conn = DB.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement("select * from Opstina where IdOpstina=?");) {
            ps.setInt(1, idDistrict);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String query = "delete from Opstina where IdOpstina=?";
                try (PreparedStatement ps1 = conn.prepareStatement(query)) {
                    ps1.setInt(1, idDistrict);
                    ps1.executeUpdate();
                    System.out.println("Obrisana opstina sa id" + idDistrict);
                    return true;
                } catch (SQLException ex) {
                    Logger.getLogger(jj170586_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                System.out.println("Opstina sa Id " + idDistrict + " ne postoji, pa ne moze biti obrisan!");
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(jj170586_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    //mislim da ja ovde smem odmah lepo da kazem da brisem sve opstine gde je grad jednak ovom prosledjenom gradu
    //jer prosto ako taj grad ne postoji, on nece nista obrisati
    //mislim da ne moram da proveravam da li taj grad postoji, pa tek ako postoji da orbisem
    @Override
    public int deleteAllDistrictsFromCity(String cityName) {
        Connection conn = DB.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement("select * from Grad where Naziv=?")) {
            ps.setString(1, cityName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int IdGrad = rs.getInt("IdGrad");
                    try (PreparedStatement ps1 = conn.prepareStatement("delete from Opstina where IdGrad=?")) {
                        ps1.setInt(1, IdGrad);
                        return ps1.executeUpdate();
                    } catch (SQLException ex) {
                        Logger.getLogger(jj170586_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    System.out.println("Grad sa ovim nazivom ne postoji!");
                    return -1;
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
    public List<Integer> getAllDistrictsFromCity(int idCity) {
        List<Integer> list = new ArrayList<>();
        Connection conn = DB.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement("select * from Opstina where IdGrad=?")) {
            ps.setInt(1, idCity);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(rs.getInt("IdOpstina"));
                }
            } catch (SQLException ex) {
                Logger.getLogger(jj170586_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(jj170586_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    @Override
    public List<Integer> getAllDistricts() {
        List<Integer> list = new ArrayList<>();
        Connection conn = DB.getInstance().getConnection();
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("select * from Opstina");) {
            while (rs.next()) {
                list.add(rs.getInt("IdOpstina"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(jj170586_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

}
