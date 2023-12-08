package br.edu.ifnmg.poo.vehicle;

import br.edu.ifnmg.poo.driver.Driver;
import br.edu.ifnmg.poo.entity.Entity;
import static br.edu.ifnmg.poo.vehicle.Vehicle.TypeVehicle.CARRO;

/**
 *
 * @author bvan &lt;Bruno Vinícius at ifnmg&gt;
 */
public class Vehicle extends Entity {

    public enum TypeVehicle {
        CARRO,
        MOTO,
        BICICLETA,
        CAMINHONETE,
        CAMINHAO,
        ONIBUS,
        TANK,
        HELICOPTER;
    }

    private String licensePlate;
    private String note;
    private TypeVehicle type;
    private Driver driver;
    private Long id_driver;

    public Vehicle() {
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

    public void setType(TypeVehicle type) {
        this.type = type;
    }

    public String getStringType() {
        if (this.getType() == null) {
            return "<not especified>";
        }

        switch (this.getType().ordinal()) {
            case 0:
                return "CARRO";
            case 1:
                return "MOTO";
            case 2:
                return "BICICLETA";
            case 3:
                return "CAMINHONETE";
            case 4:
                return "CAMINHAO";
            case 5:
                return "ONIBUS";
            case 6:
                return "TANK";
            case 7:
                return "HELICOPTER";
        }

        return null;
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
