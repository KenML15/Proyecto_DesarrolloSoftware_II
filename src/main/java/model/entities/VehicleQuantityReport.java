/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 *
 * @author 50687
 */
public class VehicleQuantityReport extends Reports{
    
    private ArrayList<String> parkingLotsNames;
    private ArrayList<String> vehicleTypes;
    private ArrayList<Integer> quantity;

    public VehicleQuantityReport() {
    }

    public VehicleQuantityReport(ArrayList<String> parkingLotsNames, ArrayList<String> vehicleTypes, ArrayList<Integer> quantity, LocalDateTime entryTime, LocalDateTime exitTime, String administratorName, LocalDateTime currentDate) {
        super(entryTime, exitTime, administratorName, currentDate);
        this.parkingLotsNames = parkingLotsNames;
        this.vehicleTypes = vehicleTypes;
        this.quantity = quantity;
    }

    public ArrayList<String> getParkingLotsNames() {
        return parkingLotsNames;
    }

    public void setParkingLotsNames(ArrayList<String> parkingLotsNames) {
        this.parkingLotsNames = parkingLotsNames;
    }
    
    public ArrayList<String> getVehicleTypes() {
        return vehicleTypes;
    }

    public void setVehicleTypes(ArrayList<String> vehicleTypes) {
        this.vehicleTypes = vehicleTypes;
    }

    public ArrayList<Integer> getQuantity() {
        return quantity;
    }

    public void setQuantity(ArrayList<Integer> quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "VehicleQuantityReport{" + "parkingLotsNames=" + parkingLotsNames + ", vehicleTypes=" + vehicleTypes + ", quantity=" + quantity + '}';
    }
}
