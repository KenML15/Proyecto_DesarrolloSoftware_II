/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.util.ArrayList;
import model.data.VehicleData;
import model.entity.Customer;
import model.entity.Vehicle;

/**
 *
 * @author 50687
 */
public class VehicleController {
    
    VehicleData vehicleData = new VehicleData();
    
    public void insertVehicle(Vehicle vehicle){
        vehicleData.insertVehicle(vehicle);
    }
    
    public ArrayList<Vehicle> getAllVehicles(){
        return vehicleData.getAllVehicles();
    }
    
    public Vehicle findVehicle(Customer customer){
        return vehicleData.findVehicle(customer);
    }

}
    

