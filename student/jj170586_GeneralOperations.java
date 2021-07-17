/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.GeneralOperations;

/**
 *
 * @author Jovana
 */
public class jj170586_GeneralOperations implements GeneralOperations {

    //ovde treba da vidim da li je potrebno mozda obrisati i procedure, funkcije
    @Override
    public void eraseAll() {

        Connection conn = DB.getInstance().getConnection();

        try (Statement stmt = conn.createStatement();) {

            stmt.executeUpdate("delete from Admin");

            stmt.executeUpdate("delete from Ponuda");

            stmt.executeUpdate("delete from Paket");

            stmt.executeUpdate("delete from ZahtevZaPrevoz");

            stmt.executeUpdate("delete from Opstina");

            stmt.executeUpdate("delete from Grad");

            stmt.executeUpdate("delete from ZahtevZaKurira");

            stmt.executeUpdate("delete from Korisnik");

            stmt.executeUpdate("delete from Kurir");

            stmt.executeUpdate("delete from Vozilo");

        } catch (SQLException ex) {
            Logger.getLogger(jj170586_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
