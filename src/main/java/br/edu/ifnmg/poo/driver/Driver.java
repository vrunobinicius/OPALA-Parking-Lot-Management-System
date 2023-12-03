package br.edu.ifnmg.poo.driver;

import br.edu.ifnmg.poo.parkingSpace.ParkingSpace;
import br.edu.ifnmg.poo.user.User;
import br.edu.ifnmg.poo.vehicle.Vehicle;
import java.util.ArrayList;

/**
 *
 * @author bvan &lt;Bruno Vinícius at ifnmg&gt;
 */
public class Driver extends User {

    int ticket;
    ArrayList<Vehicle> vehicles;
    ArrayList<ParkingSpace> parkingSpace;

    public Driver() {
        vehicles = new ArrayList<>();
        parkingSpace = new ArrayList<>();
    }

    //<editor-fold defaultstate="collapsed" desc="GETTERS/SETTERS">
    public int getTicket() {
        return ticket;
    }

    public void setTicket(int ticket) {
        this.ticket = ticket;
    }

    public ArrayList<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(ArrayList<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public ArrayList<ParkingSpace> getParkingSpace() {
        return parkingSpace;
    }

    public void setParkingSpace(ArrayList<ParkingSpace> parkingSpace) {
        this.parkingSpace = parkingSpace;
    }
    
    public void addParkingSpace(ParkingSpace ps) {
        this.getParkingSpace().add(ps);
    }
    //</editor-fold>

    @Override
    public String toString() {
        return "Driver{" + "ticket=" + ticket
                + ", vehicles=" + vehicles
                + ", parkingSpace=" + parkingSpace + '}';
    }
}
