package br.edu.ifnmg.poo.vehicle;

import java.math.BigDecimal;

/**
 *
 * @author bvan &lt;Bruno Vinícius at ifnmg&gt;
 */
public class TypeVehicle {

    private String description;
    private BigDecimal cost;

    public TypeVehicle() {
    }

    //<editor-fold defaultstate="collapsed" desc="GETTERS/SETTERS">
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) throws Exception {
        if (description.length() > 500) {
            throw new Exception();
        } else {
            this.description = description;
        }
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }
    //</editor-fold>

    @Override
    public String toString() {
        return "TypeVehicle{" + "description=" + description
                + ", cost=" + cost + '}';
    }

}
