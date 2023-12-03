package br.edu.ifnmg.poo.parkingSpace;

import br.edu.ifnmg.poo.driver.Driver;
import br.edu.ifnmg.poo.entity.Entity;
import java.time.LocalDateTime;

/**
 *
 * @author bvan &lt;Bruno Vinícius at ifnmg&gt;
 */
public class ParkingSpace extends Entity {

    private short number;
    private LocalDateTime arrivalTime;
    private LocalDateTime departureTime;
    private Driver driver;
    private Long id_driver;

    public ParkingSpace() {
    }

    //<editor-fold defaultstate="collapsed" desc="GETTERS/SETTERS">
    public short getNumber() {
        return number;
    }

    public void setNumber(short number) {
        this.number = number;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
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
        return "ParkingSpace{" + "number=" + number 
                + ", arrivalTime=" + arrivalTime 
                + ", departureTime=" + departureTime 
                + ", driver=" + driver
                + ", id_driver=" + id_driver + '}';
    }
    
    
}
