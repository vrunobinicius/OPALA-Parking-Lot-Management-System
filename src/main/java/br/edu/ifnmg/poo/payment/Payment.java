package br.edu.ifnmg.poo.payment;

import br.edu.ifnmg.poo.driver.Driver;
import br.edu.ifnmg.poo.entity.Entity;
import java.math.BigDecimal;

/**
 *
 * @author bvan &lt;Bruno Vinícius at ifnmg&gt;
 */
public class Payment extends Entity {

    public enum PaymentType {
        MENSALISTA,
        HORISTA
    }

    private int paymentFrequency;
    private BigDecimal amount;
    private String paymentDate;
    private String paymentTime;
    private String paymentMethod;
    private Driver driver;
    private PaymentType paymentType;

    private Long id_driver;
    private Long id_parking_space;

    public Payment() {
    }

    //<editor-fold defaultstate="collapsed" desc="GETTERS/SETTERS">
    public int getPaymentFrequency() {
        return paymentFrequency;
    }

    public void setPaymentFrequency(int paymentFrequency) {
        this.paymentFrequency = paymentFrequency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(String paymentTime) {
        this.paymentTime = paymentTime;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public Long getId_driver() {
        return id_driver;
    }

    public void setId_driver(Long id_driver) {
        this.id_driver = id_driver;
    }

    public Long getId_parking_space() {
        return id_parking_space;
    }

    public void setId_parking_space(Long id_parking_space) {
        this.id_parking_space = id_parking_space;
    }
    //</editor-fold>

}
