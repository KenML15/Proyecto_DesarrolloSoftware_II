/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.util.ArrayList;
import model.data.VehicleDataFile;
import model.entities.Vehicle;

/**
 *
 * @author 50687
 */
public class VehicleFileController {
    VehicleDataFile vehicleDataFile = new VehicleDataFile();
    
    public int insertVehicle(Vehicle vehicle){
        return vehicleDataFile.insertVehicle(vehicle);
    }
    
    public boolean findVehicleByPlate(String plate) {
        return vehicleDataFile.findVehicleByPlate(plate);
    }
    
    public Vehicle getVehicleFromFile(String plate) {
        return vehicleDataFile.getVehicleFromFile(plate);
    }
    
    public ArrayList<Vehicle> getAllVehicles() {
        return vehicleDataFile.getAllVehicles();
    }
    
    public String[][] createVehicleMatrix(ArrayList<Vehicle> vehicles) {
        return vehicleDataFile.createVehicleMatrix(vehicles);
    }
    
    public void deleteVehicleFromFile(String lineToRemove) {
        vehicleDataFile.deleteVehicleFromFile(lineToRemove);
    }
}
