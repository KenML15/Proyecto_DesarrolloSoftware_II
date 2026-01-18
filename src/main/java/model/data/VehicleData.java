/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.data;

import java.util.ArrayList;
import model.entity.Customer;
import model.entity.Vehicle;

/**
 *
 * @author 50687
 */
public class VehicleData {
    ArrayList <Vehicle> vehicles = new ArrayList<>();
    ArrayList <Customer> customers = new ArrayList<>();
    
    public void insertVehicle(Vehicle vehicle){
        vehicles.add(vehicle);
    }
    
    public ArrayList<Vehicle> getAllVehicles(){
        return vehicles;
    } 
    
    public Vehicle findVehicle(Customer customer){
        Vehicle vehicleToFind = null;
        for (Vehicle vehicle : vehicles) {
            if(vehicle.getCustomer() == customer){
                vehicleToFind = vehicle;
            }
        }
        
        return vehicleToFind;
    } 
}
