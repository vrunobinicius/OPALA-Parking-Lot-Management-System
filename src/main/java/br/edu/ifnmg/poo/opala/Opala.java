package br.edu.ifnmg.poo.opala;

import br.edu.ifnmg.poo.credential.Credential;
import br.edu.ifnmg.poo.credential.CredentialDAO;
import br.edu.ifnmg.poo.driver.DriverDAO;
import br.edu.ifnmg.poo.parkingSpace.ParkingSpaceDAO;
import br.edu.ifnmg.poo.user.User;
import br.edu.ifnmg.poo.user.UserDAO;
import br.edu.ifnmg.poo.vehicle.VehicleDAO;
import com.formdev.flatlaf.FlatLightLaf;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author bvan &lt;Bruno Vinícius at ifnmg&gt;
 */
public class Opala {

    public static void main(String[] args) {
        try {
            CredentialDAO cDao = new CredentialDAO();
            UserDAO uDao = new UserDAO();
            DriverDAO dDao = new DriverDAO();
            ParkingSpaceDAO pDao = new ParkingSpaceDAO();
            VehicleDAO vDao = new VehicleDAO();

            if (cDao.findById(1L) == null) {
                //Gerente user: vrunobinicius password: senha123
                Credential c = new Credential();
                c.setType(Credential.TypeUser.ADMIN);
                c.setLastAcces(LocalDateTime.now());
                try {
                    c.setPassword("senha123");
                    c.setUsername("vrunobinicius");
                } catch (Exception ex) {
                    Logger.getLogger(Opala.class.getName()).log(Level.SEVERE, null, ex);
                }
                User u = new User();
                try {
                    u.setEmail("gerente@gerente.com");
                    u.setName("Bruno Vinícius");
                    u.setTelephone(999999999L);
                } catch (Exception ex) {
                    Logger.getLogger(Opala.class.getName()).log(Level.SEVERE, null, ex);
                }
                u.setCredential(c);
                c.setUser(u);
                cDao.saveOrUpdate(c);
                uDao.saveOrUpdate(u);

                //Funcionário User: paulofilipe password: senha123
                Credential c2 = new Credential();
                c2.setType(Credential.TypeUser.OPERATOR);
                c2.setLastAcces(LocalDateTime.now());
                try {
                    c2.setPassword("senha123");
                    c2.setUsername("paulofilipe");
                } catch (Exception ex) {
                    Logger.getLogger(Opala.class.getName()).log(Level.SEVERE, null, ex);
                }
                User u2 = new User();
                try {
                    u2.setEmail("funcionario@funcionario.com");
                    u2.setName("Paulo Filipe");
                    u2.setTelephone(999999999L);
                } catch (Exception ex) {
                    Logger.getLogger(Opala.class.getName()).log(Level.SEVERE, null, ex);
                }
                u2.setCredential(c2);
                c2.setUser(u2);
                cDao.saveOrUpdate(c2);
                uDao.saveOrUpdate(u2);

                //Usuário user: isaiasvictor password: senha123
                Credential c3 = new Credential();
                c3.setType(Credential.TypeUser.SUBSCRIBER);
                c3.setLastAcces(LocalDateTime.now());
                try {
                    c3.setPassword("senha123");
                    c3.setUsername("isaiasvictor");
                } catch (Exception ex) {
                    Logger.getLogger(Opala.class.getName()).log(Level.SEVERE, null, ex);
                }
                User u3 = new User();
                try {
                    u3.setEmail("usuario@usuario.com");
                    u3.setName("Isaias Victor");
                    u3.setTelephone(999999999L);
                } catch (Exception ex) {
                    Logger.getLogger(Opala.class.getName()).log(Level.SEVERE, null, ex);
                }
                u3.setCredential(c3);
                c3.setUser(u3);
                cDao.saveOrUpdate(c3);
                uDao.saveOrUpdate(u3);
            }

            try {
                UIManager.setLookAndFeel(new FlatLightLaf());
            } catch (UnsupportedLookAndFeelException ex) {
                System.err.println("Failed to initialize LaF");
            }

            LoginScreen.main(null);
        } catch (Exception ex) {
            Logger.getLogger(Opala.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
