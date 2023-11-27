package br.edu.ifnmg.poo.opala;

import br.edu.ifnmg.poo.credential.Credential;
import br.edu.ifnmg.poo.credential.CredentialDAO;
import br.edu.ifnmg.poo.user.User;
import br.edu.ifnmg.poo.user.UserDAO;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bvan &lt;Bruno Vinícius at ifnmg&gt;
 */
public class Opala {

    public static void main(String[] args) {
        //LoginScreen.main(null);
        Credential c = new Credential();
        c.setEnabled(true);
        c.setLastAcces(LocalDate.of(2003, 1, 20));
        c.setPassword("senha123");
        c.setUsername("VrunoBinicius");

        User u = new User();
        try {
            u.setBirthdate(LocalDate.of(2003, 1, 20));
            u.setEmail("email@email.com");
            u.setName("Brunão");
        } catch (Exception ex) {
            Logger.getLogger(Opala.class.getName()).log(Level.SEVERE, null, ex);
        }
        u.setCredential(c);
        c.setUser(u);

        CredentialDAO cDao = new CredentialDAO();
        cDao.saveOrUpdate(c);
        UserDAO uDao = new UserDAO();
        uDao.saveOrUpdate(u);

        System.out.println("RETORNO DE USUÁRIO >> " + cDao.authenticate(c));

    }
}
