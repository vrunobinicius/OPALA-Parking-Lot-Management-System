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
        LoginScreen.main(null);
        /*Credential c = new Credential();
        c.setTypeUser("Funcionário");
        c.setLastAcces(LocalDate.of(2001, 10, 22));
        c.setPassword("123456");
        c.setUsername("Binicius");

        User u = new User();
        try {
            u.setBirthdate(LocalDate.of(2004, 5, 6));
            u.setEmail("email@email.com");
            u.setName("bruno");
        } catch (Exception ex) {
            Logger.getLogger(Opala.class.getName()).log(Level.SEVERE, null, ex);
        }
        u.setCredential(c);
        c.setUser(u);

        CredentialDAO cDao = new CredentialDAO();
        cDao.saveOrUpdate(c);
        UserDAO uDao = new UserDAO();
        uDao.saveOrUpdate(u);

        System.out.println("RETORNO DE USUÁRIO >> " + cDao.authenticate(c));*/

    }
}
