package br.edu.ifnmg.poo.user;

import br.edu.ifnmg.poo.credential.Credential;
import br.edu.ifnmg.poo.credential.CredentialDAO;
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
public class UserDAO extends Dao<User> {

    public static final String TABLE = "user";

    @Override
    public String getSaveStatment() {
        return "insert into " + TABLE
                + " (name, email, telephone)"
                + " values (?, ?, ?)";
    }

    @Override
    public String getUpdateStatment() {
        return "update " + TABLE
                + " set name = ?, email = ?, telephone = ?"
                + " where id = ?";
    }

    @Override
    public void composeSaveOrUpdateStatement(PreparedStatement pstmt, User e) {
        try {
            pstmt.setString(1, e.getName());
            pstmt.setString(2, e.getEmail());
            pstmt.setLong(3, e.getTelephone());

            if (e.getId() != null) {
                pstmt.setLong(4, e.getId());
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getFindByIdStatment() {
        return "select id, name, email, telephone"
                + " from " + TABLE + " where id = ?";
    }

    @Override
    public String getFindAllStatment() {
        return "select id, name, email, telephone"
                + " from " + TABLE;
    }

    @Override
    public String getDeleteStatement() {
        return "delete from " + TABLE + " where id = ?";
    }

    @Override
    public User extractObject(ResultSet resultSet) {
        User user = null;
        try {
            user = new User();
            user.setId(resultSet.getLong("id"));
            user.setName(resultSet.getString("name"));
            user.setEmail(resultSet.getString("email"));
            user.setTelephone(resultSet.getLong("telephone"));

        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return user;
    }
}
