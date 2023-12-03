package br.edu.ifnmg.poo.user;

import br.edu.ifnmg.poo.credential.Credential;
import br.edu.ifnmg.poo.entity.Entity;

/**
 *
 * @author bvan &lt;Bruno Vinícius at ifnmg&gt;
 */
public class User extends Entity {

    private String name;
    private String email;
    private Long telephone;
    private Credential credential;

    //<editor-fold defaultstate="collapsed" desc="GETTERS/SETTTERS">
    public Credential getCredential() {
        return credential;
    }

    public void setCredential(Credential credential) {
        this.credential = credential;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws Exception {
        if (name.length() > 150) {
            throw new Exception();
        } else {
            this.name = name;
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws Exception {
        if (email.length() > 255) {
            throw new Exception();
        } else {
            this.email = email;
        }
    }

    public Long getTelephone() {
        return telephone;
    }

    public void setTelephone(Long telephone) {
        this.telephone = telephone;
    }

    //</editor-fold>

    @Override
    public String toString() {
        return "User{" + "name=" + name 
                + ", email=" + email 
                + ", telephone=" + telephone + '}';
    }
    
    
}
