package br.edu.ifnmg.poo.credential;

import br.edu.ifnmg.poo.entity.Entity;
import br.edu.ifnmg.poo.user.User;
import java.time.LocalDateTime;

/**
 *
 * @author bvan &lt;Bruno Vinícius at ifnmg&gt;
 */
public class Credential extends Entity {

    public enum TypeUser {
        ADMIN,
        OPERATOR,
        SUBSCRIBER,
        DISABLED;
    }

    private String username;
    private String password;
    private LocalDateTime lastAcces;
    private TypeUser type;
    private User user;
    private Long id_user;

    public Credential() {
    }

    //<editor-fold defaultstate="collapsed" desc="GETTERS/SETTERS">
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) throws Exception {
        if (username.length() > 32 || username == null) {
            throw new Exception();
        } else {
            this.username = username;
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) throws Exception {
        if (password.length() > 32 || password == null) {
            throw new Exception();
        } else {
            this.password = password;
        }
    }

    public LocalDateTime getLastAcces() {
        return lastAcces;
    }

    public void setLastAcces(LocalDateTime lastAcces) {
        this.lastAcces = lastAcces;
    }

    public TypeUser getType() {
        return type;
    }

    public void setType(TypeUser type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId_user() {
        return id_user;
    }

    public void setId_user(Long id_user) {
        this.id_user = id_user;
    }
    //</editor-fold>

    @Override
    public String toString() {
        return "Credential{" + "username=" + username
                + ", password=" + password
                + ", lastAcces=" + lastAcces
                + ", type=" + type
                + ", user=" + user
                + ", id_user=" + id_user + '}';
    }

}
