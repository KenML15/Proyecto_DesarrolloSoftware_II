/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.entities;

/**
 *
 * @author 50687
 */
public class Administrator extends User implements Employee{
    private int adminNumber;
    private ParkingLot parkingLot;

    public Administrator() {
    }

    public Administrator(int adminNumber, ParkingLot parkingLot) {
        this.adminNumber = adminNumber;
        this.parkingLot = parkingLot;
    }

    public int getAdminNumber() {
        return adminNumber;
    }

    public void setAdminNumber(int adminNumber) {
        this.adminNumber = adminNumber;
    }

    public ParkingLot getParkingLot() {
        return parkingLot;
    }

    public void setParkingLot(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
    }

    @Override
    public String toString() {
        return "Administrator{" + "adminNumber=" + adminNumber + ", parkingLot=" + parkingLot + '}';
    }

    @Override
    public boolean verifyUserLogin(String loginDetails) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public float calculateSalary(float dailySalary) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ParkingLot assignWorkplace(int parkingLotId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
}
