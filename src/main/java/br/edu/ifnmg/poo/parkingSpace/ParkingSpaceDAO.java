package br.edu.ifnmg.poo.parkingSpace;

import br.edu.ifnmg.poo.repository.Dao;
import br.edu.ifnmg.poo.repository.DbConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bvan &lt;Bruno Vinícius at ifnmg&gt;
 */
public class ParkingSpaceDAO extends Dao<ParkingSpace> {

    public static final String TABLE = "parkingspace";

    @Override
    public String getSaveStatment() {
        return "insert into " + TABLE
                + " (id_driver, number, "
                + "arrivalTime, departureTime)"
                + " values (?, ?, ?, ?)";
    }

    @Override
    public String getUpdateStatment() {
        return "update " + TABLE
                + " set id_driver = ?, "
                + "number = ?, "
                + "arrivalTime = ?, "
                + "departureTime = ?"
                + " where id = ?";
    }

    @Override
    public void composeSaveOrUpdateStatement(PreparedStatement pstmt, ParkingSpace e) {
        try {
            pstmt.setLong(1, e.getId_driver());
            pstmt.setLong(2, e.getNumber());
            pstmt.setString(3, e.getArrivalTime());
            pstmt.setString(4, e.getDepartureTime());

            if (e.getId() != null) {
                pstmt.setLong(5, e.getId());
            }
        } catch (SQLException ex) {
            Logger.getLogger(ParkingSpaceDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getFindByIdStatment() {
        return "select id, id_driver, number, "
                + "arrivalTime, departureTime"
                + " from " + TABLE + " where id = ?";
    }

    @Override
    public String getFindAllStatment() {
        return "select id, id_driver, number, "
                + "arrivalTime, departureTime"
                + " from " + TABLE;
    }

    @Override
    public String getDeleteStatement() {
        return "delete from " + TABLE + " where id = ?";
    }

    public ParkingSpace findByLicensePlate(Long number) {

        try (PreparedStatement preparedStatement
                = DbConnection.getConnection().prepareStatement( 
                        "select id, id_driver, number, "
                + "arrivalTime, departureTime"
                + " from " + TABLE + " where number = ?")) {

            // Assemble the SQL statement with the id
            preparedStatement.setLong(1, number);

            // Performs the query on the database
            ResultSet resultSet = preparedStatement.executeQuery();

            // Returns the respective object if exists
            if (resultSet.next()) {
                return extractObject(resultSet);
            }

        } catch (Exception ex) {
            System.out.println("Exception: " + ex);
        }

        return null;
    }

    @Override
    public ParkingSpace extractObject(ResultSet resultSet) {
        ParkingSpace ps = null;
        try {
            ps = new ParkingSpace();
            ps.setId(resultSet.getLong("id"));
            ps.setId_driver(resultSet.getLong("id_driver"));
            ps.setNumber(resultSet.getShort("number"));
            ps.setArrivalTime(resultSet.getString("arrivalTime"));
            ps.setDepartureTime(resultSet.getString("departureTime"));

        } catch (SQLException ex) {
            Logger.getLogger(ParkingSpaceDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ps;
    }
}
