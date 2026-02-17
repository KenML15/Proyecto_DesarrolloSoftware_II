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
import model.data.InvoiceDataFile;
import model.data.VehicleDataFile;
import model.entities.Customer;
import model.entities.Invoice;
import model.entities.ParkingLot;
import model.entities.Vehicle;
import org.jdom2.JDOMException;

/**
 *
 * @author 50687
 */
public class VehicleFileController {

    private final VehicleDataFile vehicleData;
    private final CustomerDataFile customerData;
    private final InvoiceDataFile invoiceData;
    private final ParkingLotFileController parkingLotController;
    
    public VehicleFileController() throws IOException, JDOMException {
        this.customerData = new CustomerDataFile();
        this.vehicleData = new VehicleDataFile();
        this.invoiceData = new InvoiceDataFile();
        this.parkingLotController = new ParkingLotFileController();
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
    public Invoice registerVehicleExit(String plate) throws IOException {
        Vehicle vehicle = getByPlate(plate);
        
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehículo no encontrado: " + plate);
        }
        
        if (vehicle.getExitTime() != null) {
            throw new IllegalStateException("Ya se registró la salida del vehículo: " + plate);
        }
        
        //Por probar
        if (vehicle.getVehicleType() == null) {
            throw new IllegalStateException(
                    "El vehículo con placa " + plate
                    + " no tiene tipo de vehículo. Por favor, edite el vehículo y asígnele un tipo."
            );
        }
        
        ParkingLot parkingLot = findParkingLotByVehicle(vehicle);
        
        if (parkingLot == null) {
            throw new IllegalStateException("No se pudo encontrar el parqueo del vehículo: " + plate);
        }
        
        LocalDateTime exitTime = LocalDateTime.now();
        vehicle.setExitTime(exitTime);
        
        float feeAmount = calculateParkingFee(vehicle);
        vehicle.setFeeToPay(feeAmount);

        vehicleData.updateVehicle(vehicle);
        
        parkingLotController.freeVehicleSpace(vehicle);
        
        Invoice invoice = createInvoice(vehicle, parkingLot, feeAmount);
        invoiceData.insertInvoice(invoice);

        return invoice;
//        return feeAmount;
    }
    
    private ParkingLot findParkingLotByVehicle(Vehicle vehicle) throws IOException {
        for (ParkingLot lot : parkingLotController.getAllParkingLots()) {
            for (Vehicle v : lot.getVehicles()) {
                if (v.getPlate().equals(vehicle.getPlate())) {
                    return lot;
                }
            }
        }
        return null;
    }
    
    private Invoice createInvoice(Vehicle vehicle, ParkingLot parkingLot, float amount) throws IOException {
        Invoice invoice = new Invoice();
        invoice.setId(invoiceData.getNextId());
        invoice.setVehiclePlate(vehicle.getPlate());
        invoice.setParkingLotName(parkingLot.getName());
        invoice.setEntryTime(vehicle.getEntryTime());
        invoice.setExitTime(vehicle.getExitTime());
        invoice.setTotalAmount(amount);
        return invoice;
    }
    
    private float calculateParkingFee(Vehicle vehicle) {
        if (vehicle.getEntryTime() == null || vehicle.getExitTime() == null) {
            return 0.0f;
        }
        
        if (vehicle.getVehicleType() == null) {
            throw new IllegalStateException(
                    "El vehículo con placa " + vehicle.getPlate()
                    + " no tiene un tipo de vehículo asignado. "
                    + "No se puede calcular la tarifa."
            );
        }
        
        if (vehicle.getVehicleType().getFee() == null) {
            throw new IllegalStateException(
                    "El tipo de vehículo '" + vehicle.getVehicleType().getDescription()
                    + "' no tiene una tarifa configurada."
            );
        }
        
//        if (vehicle.getVehicleType() == null || vehicle.getVehicleType().getFee() == null) {
//            throw new IllegalStateException("No hay tarifa configurada para este tipo de vehículo");
//        }
        
        Duration duration = Duration.between(vehicle.getEntryTime(), vehicle.getExitTime());
        long minutes = duration.toMinutes();
        
        return (float) vehicle.getVehicleType().calculateParkingFee(minutes);
    }
    
    //Consultas
    public ArrayList<Vehicle> getCurrentlyParkedVehicles() throws IOException {
        return vehicleData.getCurrentlyParkedVehicles();
    }
}
