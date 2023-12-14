package br.edu.ifnmg.poo.credential;

import br.edu.ifnmg.poo.credential.Credential.TypeUser;
import static br.edu.ifnmg.poo.credential.Credential.TypeUser.ADMIN;
import static br.edu.ifnmg.poo.credential.Credential.TypeUser.OPERATOR;
import static br.edu.ifnmg.poo.credential.Credential.TypeUser.SUBSCRIBER;
import br.edu.ifnmg.poo.repository.Dao;
import br.edu.ifnmg.poo.repository.DbConnection;

import br.edu.ifnmg.poo.user.User;
import br.edu.ifnmg.poo.user.UserDAO;
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
public class CredentialDAO extends Dao<Credential> {

    public static final String TABLE = "credential";
    private static String SALT = "0P4L4";

    @Override
    public String getSaveStatment() {
        return "insert into " + TABLE
                + " (id_user, username, password, lastAccess, typeUser)"
                + " values (?, ?, MD5(?), ?, ?)";
    }

    @Override
    public String getUpdateStatment() {
        return "update " + TABLE
                + " set id_user = ?"
                + "username = ?, password = MD5(?), "
                + "lastAccess = ?, typeUser = ?"
                + " where id = ?";
    }

    @Override
    public void composeSaveOrUpdateStatement(PreparedStatement pstmt, Credential e) {
        try {
            pstmt.setLong(1, e.getId_user());
            pstmt.setString(2, e.getUsername());
            String password = e.getPassword() + SALT;
            pstmt.setString(3, password);
            pstmt.setObject(4, e.getLastAcces(), java.sql.Types.DATE);

            switch (e.getType()) {
                case ADMIN -> {
                    pstmt.setString(5, "ADMIN");
                }
                case OPERATOR -> {
                    pstmt.setString(5, "OPERATOR");
                }
                case SUBSCRIBER -> {
                    pstmt.setString(5, "SUBSCRIBER");
                }
            }

            if (e.getId() != null) {
                pstmt.setLong(6, e.getId());
            }
        } catch (SQLException ex) {
            Logger.getLogger(CredentialDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getFindByIdStatment() {
        return "select id, id_user, username, password, "
                + "lastAccess, typeUser"
                + " from " + TABLE + " where id = ?";
    }

    @Override
    public String getFindAllStatment() {
        return "select id, id_user, username, password, "
                + "lastAccess, typeUser"
                + " from " + TABLE;
    }

    @Override
    public String getDeleteStatement() {
        return "delete from " + TABLE + " where id = ?";
    }

    @Override
    public Credential extractObject(ResultSet resultSet) {
        Credential credential = null;
        try {
            credential = new Credential();
            credential.setId(resultSet.getLong("id"));
            credential.setId_user(resultSet.getLong("id_user"));
            credential.setUsername(resultSet.getString("username"));
            String password = resultSet.getString("password");
            credential.setPassword(password);
            credential.setLastAcces(resultSet.getObject("lastAccess", LocalDateTime.class));
            credential.setType(TypeUser.valueOf(resultSet.getString("TypeUser")));

            UserDAO userDao = new UserDAO();
            User user = userDao.findById(credential.getId());
            credential.setUser(user);
            user.setCredential(credential);

        } catch (SQLException ex) {
            Logger.getLogger(CredentialDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CredentialDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return credential;
    }

    public User authenticate(Credential credential) {
        final String SQL = "SELECT * "
                + "FROM credential "
                + "WHERE username = ? "
                + "AND password = MD5(?)";
        try (PreparedStatement preparedStatement
                = DbConnection.getConnection().prepareStatement(SQL)) {

            // Assemble the SQL statement with the id
            preparedStatement.setString(1, credential.getUsername());
            String password = credential.getPassword() + SALT;
            preparedStatement.setString(2, password);

            // Performs the query on the database
            ResultSet resultSet = preparedStatement.executeQuery();

            // Returns the respective object if exists
            if (resultSet.next()) {
                credential = extractObject(resultSet);
                return credential.getUser();
            }

        } catch (Exception ex) {
            System.out.println("Exception: " + ex);
        }
        return null;
    }
}
