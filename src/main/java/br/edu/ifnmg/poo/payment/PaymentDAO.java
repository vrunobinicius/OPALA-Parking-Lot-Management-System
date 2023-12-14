package br.edu.ifnmg.poo.payment;

import br.edu.ifnmg.poo.payment.Payment.PaymentType;
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
public class PaymentDAO extends Dao<Payment> {

    public static final String TABLE = "payment";

    @Override
    public String getSaveStatment() {
        return "insert into " + TABLE
                + " (id_driver, id_parking_space, "
                + "paymentFrequency, amount, paymentDate, "
                + "paymentTime, paymentMethod, paymentType)"
                + " values (?, ?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    public String getUpdateStatment() {
        return "update " + TABLE
                + " set id_driver = ?,"
                + " id_parking_space = ?,"
                + " paymentFrequency = ?,"
                + " amount = ?,"
                + " paymentDate = ?,"
                + " paymentTime = ?,"
                + " paymentMethod = ?,"
                + " paymentType = ?"
                + " where id = ?";
    }

    @Override
    public void composeSaveOrUpdateStatement(PreparedStatement pstmt, Payment e) {
        try {
            pstmt.setLong(1, e.getId_driver());
            pstmt.setLong(2, e.getId_parking_space());
            pstmt.setInt(3, e.getPaymentFrequency());
            pstmt.setBigDecimal(4, e.getAmount());
            pstmt.setString(5, e.getPaymentDate());
            pstmt.setString(6, e.getPaymentTime());
            pstmt.setString(7, e.getPaymentMethod());

            switch (e.getPaymentType()) {
                case HORISTA -> {
                    pstmt.setString(8, "CARRO");
                }
                case MENSALISTA -> {
                    pstmt.setString(8, "MOTO");
                }
            }

            if (e.getId() != null) {
                pstmt.setLong(9, e.getId());
            }
        } catch (SQLException ex) {
            Logger.getLogger(PaymentDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getFindByIdStatment() {
        return "select id, id_driver,"
                + " id_parking_space,"
                + " paymentFrequency,"
                + " amount,"
                + " paymentDate,"
                + " paymentTime,"
                + " paymentMethod,"
                + " paymentType"
                + " from " + TABLE
                + " where id = ?";
    }

    @Override
    public String getFindAllStatment() {
        return "select id, id_driver, "
                + " id_parking_space,"
                + " paymentFrequency,"
                + " amount,"
                + " paymentDate,"
                + " paymentTime,"
                + " paymentMethod,"
                + " paymentType"
                + " from " + TABLE;
    }

    @Override
    public String getDeleteStatement() {
        return "delete from " + TABLE
                + " where id = ?";
    }

    @Override
    public Payment extractObject(ResultSet resultSet) {
        Payment payment = null;
        try {
            payment = new Payment();
            payment.setId(resultSet.getLong("id"));
            payment.setId_driver(resultSet.getLong("id_driver"));
            payment.setId_parking_space(resultSet.getLong("id_parking_space"));
            payment.setAmount(resultSet.getBigDecimal("amount"));
            payment.setPaymentDate(resultSet.getString("paymentDate"));
            payment.setPaymentTime(resultSet.getString("paymentTime"));
            payment.setPaymentFrequency(resultSet.getInt("paymentFrequency"));
            payment.setPaymentMethod(resultSet.getString("paymentMethod"));
            payment.setPaymentType(PaymentType.valueOf(resultSet.getString("paymentype")));
        } catch (SQLException ex) {
            Logger.getLogger(PaymentDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(PaymentDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return payment;
    }
}
