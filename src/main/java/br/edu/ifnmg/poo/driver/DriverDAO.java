package br.edu.ifnmg.poo.driver;

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
public class DriverDAO extends Dao<Driver> {

    public static final String TABLE = "driver";

    @Override
    public String getSaveStatment() {
        return "insert into " + TABLE
                + " (ticket)"
                + " values (?)";
    }

    @Override
    public String getUpdateStatment() {
        return "update " + TABLE
                + " set ticket = ?"
                + " where id = ?";
    }

    @Override
    public void composeSaveOrUpdateStatement(PreparedStatement pstmt, Driver e) {
        try {
            pstmt.setLong(1, e.getTicket());

            if (e.getId() != null) {
                pstmt.setLong(2, e.getId());
            }
        } catch (SQLException ex) {
            Logger.getLogger(DriverDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getFindByIdStatment() {
        return "select id, ticket"
                + " from " + TABLE
                + " where id = ?";
    }

    @Override
    public String getFindAllStatment() {
        return "select id, ticket"
                + " from " + TABLE;
    }

    @Override
    public String getDeleteStatement() {
        return "delete from " + TABLE
                + " where id = ?";
    }

    @Override
    public Driver extractObject(ResultSet resultSet) {
        Driver driver = null;
        try {
            driver = new Driver();
            driver.setId(resultSet.getLong("id"));
            driver.setTicket(resultSet.getInt("ticket"));

        } catch (SQLException ex) {
            Logger.getLogger(DriverDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return driver;
    }
}
