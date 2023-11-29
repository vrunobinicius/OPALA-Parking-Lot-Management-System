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
        CredentialDAO cDao = new CredentialDAO();
        UserDAO uDao = new UserDAO();

        //Gerente user: vrunobinicius password: senha123
        Credential c = new Credential();
        c.setTypeUser("Gerente");
        c.setLastAcces(LocalDate.of(2023, 11, 25));
        c.setPassword("senha123");
        c.setUsername("vrunobinicius");
        User u = new User();
        try {
            u.setBirthdate(LocalDate.of(2004, 5, 6));
            u.setEmail("gerente@gerente.com");
            u.setName("Bruno Vinícius");
        } catch (Exception ex) {
            Logger.getLogger(Opala.class.getName()).log(Level.SEVERE, null, ex);
        }
        u.setCredential(c);
        c.setUser(u);
        cDao.saveOrUpdate(c);
        uDao.saveOrUpdate(u);

        //Funcionário User: paulofilipe password: senha123
        Credential c2 = new Credential();
        c2.setTypeUser("Funcionário");
        c2.setLastAcces(LocalDate.of(2023, 11, 25));
        c2.setPassword("senha123");
        c2.setUsername("paulofilipe");
        User u2 = new User();
        try {
            u2.setBirthdate(LocalDate.of(2004, 5, 6));
            u2.setEmail("funcionario@funcionario.com");
            u2.setName("Paulo Filipe");
        } catch (Exception ex) {
            Logger.getLogger(Opala.class.getName()).log(Level.SEVERE, null, ex);
        }
        u2.setCredential(c2);
        c2.setUser(u2);
        cDao.saveOrUpdate(c2);
        uDao.saveOrUpdate(u2);

        //Usuário user: isaiasvictor password: senha123
        Credential c3 = new Credential();
        c3.setTypeUser("Usuário");
        c3.setLastAcces(LocalDate.of(2023, 11, 25));
        c3.setPassword("senha123");
        c3.setUsername("isaiasvictor");
        User u3 = new User();
        try {
            u3.setBirthdate(LocalDate.of(2004, 5, 6));
            u3.setEmail("usuario@usuario.com");
            u3.setName("Isaias Victor");
        } catch (Exception ex) {
            Logger.getLogger(Opala.class.getName()).log(Level.SEVERE, null, ex);
        }
        u3.setCredential(c3);
        c3.setUser(u3);
        cDao.saveOrUpdate(c3);
        uDao.saveOrUpdate(u3);

        LoginScreen.main(null);
    }
}
