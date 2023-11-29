package br.edu.ifnmg.poo.credential;

import br.edu.ifnmg.poo.entity.Entity;
import br.edu.ifnmg.poo.user.User;
import java.time.LocalDate;

/**
 *
 * @author bvan &lt;Bruno Vinícius at ifnmg&gt;
 */
public class Credential extends Entity {

    private String username;
    private String password;
    private LocalDate lastAcces;
    private String typeUser;
    private User user;

    public Credential() {
    }

    //<editor-fold defaultstate="collapsed" desc="GETTERS/SETTERS">
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getLastAcces() {
        return lastAcces;
    }

    public void setLastAcces(LocalDate lastAcces) {
        this.lastAcces = lastAcces;
    }

    public String getTypeUser() {
        return typeUser;
    }

    public void setTypeUser(String typeUser) {
        this.typeUser = typeUser;
    }
    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        //this.user_ID = user.getId();
    }
    //</editor-fold>

    @Override
    public String toString() {
        return "Credential{" + "username=" + username 
                + ", password=" + password
                + ", lastAcces=" + lastAcces 
                + ", typeUser=" + typeUser
                + ", user=" + user + '}';
    }

}
