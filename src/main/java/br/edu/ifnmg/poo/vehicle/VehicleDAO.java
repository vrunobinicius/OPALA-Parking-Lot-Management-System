package br.edu.ifnmg.poo.vehicle;

import br.edu.ifnmg.poo.repository.Dao;
import br.edu.ifnmg.poo.vehicle.Vehicle.TypeVehicle;
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
                + " (id_driver, licensePlate, note, typeVehicle)"
                + " values (?, ?, ?, ?)";
    }

    @Override
    public String getUpdateStatment() {
        return "update " + TABLE
                + " set id_driver = ?,"
                + " licensePlate = ?,"
                + " note = ?,"
                + " typeVehicle = ?"
                + " where id = ?";
    }

    @Override
    public void composeSaveOrUpdateStatement(PreparedStatement pstmt, Vehicle e) {
        try {
            pstmt.setLong(1, e.getId_driver());
            pstmt.setString(2, e.getLicensePlate());
            pstmt.setString(3, e.getNote());
            switch (e.getType()) {
                case CARRO -> {
                    pstmt.setString(4, "CARRO");
                }
                case MOTO -> {
                    pstmt.setString(4, "MOTO");
                }
                case BICICLETA -> {
                    pstmt.setString(4, "BICICLETA");
                }
                case CAMINHONETE -> {
                    pstmt.setString(4, "CAMINHONETE");
                }
                case CAMINHAO -> {
                    pstmt.setString(4, "CAMINHAO");
                }
                case ONIBUS -> {
                    pstmt.setString(4, "ONIBUS");
                }
                case TANK -> {
                    pstmt.setString(4, "TANK");
                }
                case HELICOPTER -> {
                    pstmt.setString(4, "HELICOPTER");
                }
            }

            if (e.getId() != null) {
                pstmt.setLong(5, e.getId());
            }
        } catch (SQLException ex) {
            Logger.getLogger(VehicleDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getFindByIdStatment() {
        return "select id, id_driver,"
                + " licensePlate,"
                + " note, typeVehicle"
                + " from " + TABLE
                + " where id = ?";
    }

    @Override
    public String getFindAllStatment() {
        return "select id, id_driver, licensePlate,"
                + " note, typeVehicle"
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
            vehicle.setType(TypeVehicle.valueOf(resultSet.getString("typeVehicle")));
            /*vehicle.getType().setDescription(resultSet.getString("description"));
            vehicle.getType().setCost(resultSet.getBigDecimal("cost"));*/

        } catch (SQLException ex) {
            Logger.getLogger(VehicleDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(VehicleDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return vehicle;
    }
}
