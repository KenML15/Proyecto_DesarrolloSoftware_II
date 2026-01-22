/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.data;

import java.util.ArrayList;
import model.entities.Customer;
import model.entities.Vehicle;

/**
 *
 * @author 50687
 */
public class VehicleData {
    ArrayList <Vehicle> vehicles = new ArrayList<>();
    
    public void insertVehicle(Vehicle vehicle){
        vehicles.add(vehicle);
    }
    
    public ArrayList<Vehicle> getAllVehicles(){
        return vehicles;
    } 
    
    public Vehicle findVehicle(Customer customer) {
        Vehicle vehicleToFind = null;
        for (Vehicle vehicle : vehicles) {
            for (Customer customerToFind : vehicle.getCustomer()) {
                if (customerToFind.getId().equals(customer.getId())) {
                    vehicleToFind = vehicle;
                }
            }
        }

        return vehicleToFind;
    }
    
    public void removeVehicle(Vehicle vehicle){
        vehicles.remove(vehicle);
    }
    
    
   //Este método encuentra el vehículo por medio de la placa
    public Vehicle findVehicleByPlate(String plate) {
        
        Vehicle vehicleToReturn = null;
        
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getPlate().equals(plate)) {
                vehicleToReturn = vehicle;
            }
        }
        return vehicleToReturn;
    }
}
