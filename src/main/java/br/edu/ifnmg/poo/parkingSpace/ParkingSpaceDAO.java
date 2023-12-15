package br.edu.ifnmg.poo.parkingSpace;

import br.edu.ifnmg.poo.repository.Dao;
import br.edu.ifnmg.poo.repository.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

    public ParkingSpace findByLicensePlate(String licensePlate) {
        try (PreparedStatement preparedStatement
                     = DbConnection.getConnection().prepareStatement(
                "select P.id, P.id_driver, P.number, "
                        + "P.arrivalTime, P.departureTime"
                        + " from ParkingSpace P"
                        + " inner join Vehicle V on P.id_driver = V.id_driver"
                        + " where V.licensePlate = ?")) {

            // Assemble the SQL statement with the licensePlate
            preparedStatement.setString(1, licensePlate);

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

    public static int getNextFreeParkingSpot() throws SQLException {
        int firstAvailableSpot = 1;

        try {
            Connection connection = DbConnection.getConnection();

            // Recupera todas as vagas utilizadas
            String usedSpotsQuery = "SELECT number FROM ParkingSpace ORDER BY number ASC";

            try (PreparedStatement usedSpotsStatement = connection.prepareStatement(usedSpotsQuery)) {
//                try (ResultSet usedSpotsResult = usedSpotsStatement.executeQuery()) {
                ResultSet usedSpotsResult = usedSpotsStatement.executeQuery();

                // Armazena as vagas utilizadas em uma lista
                List<Integer> usedSpots = new ArrayList<>();
                while (usedSpotsResult.next()) {
                    usedSpots.add(usedSpotsResult.getInt("number"));
                }

                // Encontra a primeira vaga livre
                int maxSpot = usedSpots.isEmpty() ? 1 : Collections.max(usedSpots) + 1;
                for (int i = 1; i <= maxSpot; i++) {
                    if (!usedSpots.contains(i)) {
                        firstAvailableSpot = i;
                        break;
                    }
                }

                return firstAvailableSpot;
//                }Ï
            }
        } catch (SQLException e) {
            e.printStackTrace(); // TODO: Melhorar isso aqui
            return -1;
        }
    }
}
