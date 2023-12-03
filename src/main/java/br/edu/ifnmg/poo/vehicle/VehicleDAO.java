package br.edu.ifnmg.poo.vehicle;

import br.edu.ifnmg.poo.repository.Dao;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bvan &lt;Bruno Vinícius at ifnmg&gt;
 */
public class VehicleDAO extends Dao<Vehicle> {

    public static final String TABLE = "vehicle";

    @Override
    public String getSaveStatment() {
        return "insert into " + TABLE
                + " (id_driver, licensePlate, note, description, cost)"
                + " values (?, ?, ?, ?, ?)";
    }

    @Override
    public String getUpdateStatment() {
        return "update " + TABLE
                + " set id_driver = ?,"
                + " licensePlate = ?,"
                + " note = ?,"
                + " description = ?,"
                + " cost = ?"
                + " where id = ?";
    }

    @Override
    public void composeSaveOrUpdateStatement(PreparedStatement pstmt, Vehicle e) {
        try {
            pstmt.setLong(1, e.getId_driver());
            pstmt.setString(2, e.getLicensePlate());
            pstmt.setString(3, e.getNote());
            pstmt.setString(4, e.getType().getDescription());
            pstmt.setBigDecimal(5, e.getType().getCost());

            if (e.getId() != null) {
                pstmt.setLong(6, e.getId());
            }
        } catch (SQLException ex) {
            Logger.getLogger(VehicleDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getFindByIdStatment() {
        return "select id, id_driver, licensePlate,"
                + " note, description, cost"
                + " from " + TABLE
                + " where id = ?";
    }

    @Override
    public String getFindAllStatment() {
        return "select id, id_driver, licensePlate,"
                + " note, description, cost"
                + " from " + TABLE;
    }

    @Override
    public String getDeleteStatement() {
        return "delete from " + TABLE
                + " where id = ?";
    }

    @Override
    public Vehicle extractObject(ResultSet resultSet) {
        Vehicle vehicle = null;
        try {
            vehicle = new Vehicle();
            vehicle.setId(resultSet.getLong("id"));
            vehicle.setId_driver(resultSet.getLong("id_driver"));
            vehicle.setLicensePlate(resultSet.getString("licensePlate"));
            vehicle.setNote(resultSet.getString("note"));
            vehicle.getType().setDescription(resultSet.getString("description"));
            vehicle.getType().setCost(resultSet.getBigDecimal("cost"));

        } catch (SQLException ex) {
            Logger.getLogger(VehicleDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(VehicleDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return vehicle;
    }
}
