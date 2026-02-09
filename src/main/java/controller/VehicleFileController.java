/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import model.data.CustomerDataFile;
import model.data.VehicleDataFile;
import model.entities.Customer;
import model.entities.Vehicle;

/**
 *
 * @author 50687
 */
public class VehicleFileController {

    private final VehicleDataFile vehicleData;
    private final CustomerDataFile customerData;
    
    public VehicleFileController() throws IOException {
        this.customerData = new CustomerDataFile();
        this.vehicleData = new VehicleDataFile();
    }
    
    public void create(Vehicle vehicle) throws IOException, IllegalArgumentException {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehículo inválido");
        }
        
        vehicle.setId(vehicleData.getNextId());
        vehicleData.insertVehicle(vehicle);
    }
    
    public void update(Vehicle vehicle) throws IOException, IllegalArgumentException {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehículo inválido");
        }
        
        Vehicle existing = vehicleData.getVehicleByPlate(vehicle.getPlate());
        if (existing == null){
            throw new IllegalArgumentException("Vehículo no existe");
        }
        
        vehicleData.updateVehicle(vehicle);
    }
    
    public void delete(String plate) throws IOException, IllegalArgumentException {
        if (vehicleData.getVehicleByPlate(plate) == null){
            throw new IllegalArgumentException("Vehículo no existe");
        }
        
        vehicleData.deleteVehicle(plate);
    }
    
    public ArrayList<Vehicle> getAll() throws IOException {
        return vehicleData.getAllVehicles();
    }
    
    public Vehicle getByPlate(String plate) throws IOException {
        return vehicleData.getVehicleByPlate(plate);
    }
    
    public ArrayList<Vehicle> getByCustomerId(int customerId) throws IOException {
        return vehicleData.getByCustomerId(customerId);
    }
    
    //Relaciones
    public void addCustomerToVehicle(String plate, int customerId) throws IOException, IllegalArgumentException {
        Vehicle vehicle = getByPlate(plate);
        if (vehicle == null){
            throw new IllegalArgumentException("Vehículo no existe");
        }
        
        Customer customer = customerData.getCustomerById(customerId);
        if (customer == null){
            throw new IllegalArgumentException("Cliente no existe");
        }
        
        ArrayList<Customer> customers = vehicle.getCustomer();
        if (customers.contains(customer)){
            throw new IllegalArgumentException("Cliente ya asociado");
        }
        
        customers.add(customer);
        vehicle.setCustomer(customers);
        vehicleData.updateVehicle(vehicle);
    }
    
    //Salida y Tarifa
    public double registerVehicleExit(String plate) throws IOException {
        Vehicle vehicle = getByPlate(plate);
        
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehículo no encontrado: " + plate);
        }
        
        if (vehicle.getExitTime() != null) {
            throw new IllegalStateException("Ya se registró la salida del vehículo: " + plate);
        }
        
        LocalDateTime exitTime = LocalDateTime.now();
        vehicle.setExitTime(exitTime);
        
        float feeAmount = calculateParkingFee(vehicle);
        vehicle.setFeeToPay(feeAmount);

        vehicleData.updateVehicle(vehicle);
        
        return feeAmount;
    }
    
    private float calculateParkingFee(Vehicle vehicle) {
        if (vehicle.getEntryTime() == null || vehicle.getExitTime() == null) {
            return 0.0f;
        }
        
        if (vehicle.getVehicleType() == null || vehicle.getVehicleType().getFee() == null) {
            throw new IllegalStateException("No hay tarifa configurada para este tipo de vehículo");
        }
        
        Duration duration = Duration.between(vehicle.getEntryTime(), vehicle.getExitTime());
        long minutes = duration.toMinutes();
        
        return (float) vehicle.getVehicleType().calculateParkingFee(minutes);
    }
    
    //Consultas
    public ArrayList<Vehicle> getCurrentlyParkedVehicles() throws IOException {
        return vehicleData.getCurrentlyParkedVehicles();
    }
}
