package br.edu.ifnmg.poo.vehicle;

import br.edu.ifnmg.poo.driver.Driver;
import br.edu.ifnmg.poo.entity.Entity;

/**
 *
 * @author bvan &lt;Bruno Vinícius at ifnmg&gt;
 */
public class Vehicle extends Entity {

    private String licensePlate;
    private String note;
    private TypeVehicle type;
    private Driver driver;
    private Long id_driver;

    public Vehicle() {
        type = new TypeVehicle();
    }

    //<editor-fold defaultstate="collapsed" desc="GETTERS/SETTERS">
    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) throws Exception {
        if (licensePlate.length() > 8 || licensePlate == null) {
            throw new Exception();
        } else {
            this.licensePlate = licensePlate;
        }
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) throws Exception {
        if (note.length() > 500) {
            throw new Exception();
        } else {
            this.note = note;
        }
    }

    public TypeVehicle getType() {
        return type;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Long getId_driver() {
        return id_driver;
    }

    public void setId_driver(Long id_driver) {
        this.id_driver = id_driver;
    }

    //</editor-fold>
    
    @Override
    public String toString() {
        return "Vehicle{" + "licensePlate=" + licensePlate
                + ", note=" + note
                + ", type=" + type
                + ", driver=" + driver
                + ", id_driver=" + id_driver + '}';
    }
}
