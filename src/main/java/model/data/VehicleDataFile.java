/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import model.entities.Customer;
import model.entities.Space;
import model.entities.Vehicle;
import model.entities.VehicleType;

/**
 *
 * @author 50687
 */
public class VehicleDataFile {

    private final String fileName;
    private final CustomerDataFile customerData;
    private final VehicleTypeDataFile vehicleTypeDataFile;
    private final SpaceDataFile spaceDataFile;
    private static final String VEHICLE_FILE = "Vehicles.txt";
    private static final String TEMPORAL = "temporal _vehicles_file.txt";
    private static final String DELIMITER = ";";
    private static final String CUSTOMER_DELIMITER = ",";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public VehicleDataFile(String fileName, CustomerDataFile customerData, VehicleTypeDataFile vehicleTypeDataFile, SpaceDataFile spaceDataFile) throws IOException {
        this.fileName = fileName;
        this.customerData = customerData;
        this.vehicleTypeDataFile = vehicleTypeDataFile;
        this.spaceDataFile = spaceDataFile;
        ensureFileExists();
    }

    public VehicleDataFile() throws IOException {
        this(VEHICLE_FILE, new CustomerDataFile(), new VehicleTypeDataFile(), new SpaceDataFile());
    }

    private void ensureFileExists() throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
    }

    public void insertVehicle(Vehicle vehicle) throws IOException {
        validateVehicle(vehicle);
        checkDuplicate(vehicle);
        appendToFile(vehicle);
    }

    private void validateVehicle(Vehicle vehicle) throws IOException{
        if (vehicle.getPlate() == null || vehicle.getPlate().trim().isEmpty()) {
            throw new IllegalArgumentException("Placa requerida");
        }
        if (vehicle.getCustomer() == null || vehicle.getCustomer().isEmpty()) {
            throw new IllegalArgumentException("Se requiere al menos un cliente");
        }
    }

    private void checkDuplicate(Vehicle vehicle) throws IOException {
        if (getVehicleByPlate(vehicle.getPlate()) != null) {
            throw new IllegalArgumentException("La placa ingresada ya existe. Intentelo de nuevo");
        }
    }

    private void appendToFile(Vehicle vehicle) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName, true))) {
            writer.println(formatVehicle(vehicle));
        }
    }

    private String formatVehicle(Vehicle vehicle) throws IOException{
        String exitTime = (vehicle.getExitTime() != null) ? 
            vehicle.getExitTime().format(DATE_FORMATTER) : "null";
        
        return String.join(DELIMITER,
                String.valueOf(vehicle.getId()),
                vehicle.getPlate(),
                vehicle.getColor(),
                vehicle.getBrand(),
                vehicle.getModel(),
                getCustomerIds(vehicle.getCustomer()),
                String.valueOf(vehicle.getVehicleTypeId()),
                String.valueOf(vehicle.getSpaceId()),
                vehicle.getEntryTime().format(DATE_FORMATTER),
                exitTime,
                String.valueOf(vehicle.getFeeToPay())
        );
    }

    private String getCustomerIds(ArrayList<Customer> customers) throws IOException{
        if (customers == null || customers.isEmpty()) {
            return "";
        }

        StringBuilder ids = new StringBuilder();
        for (int i = 0; i < customers.size(); i++) {
            ids.append(customers.get(i).getId());
            if (i < customers.size() - 1) {
                ids.append(CUSTOMER_DELIMITER);
            }
        }
        return ids.toString();
    }

    public void updateVehicle(Vehicle vehicle) throws IOException {
        validateVehicle(vehicle);
        File file = new File(fileName);
        File temp = new File(TEMPORAL );
        replaceInFile(vehicle, file, temp);
        replaceFile(file, temp);
    }

    private void replaceInFile(Vehicle vehicle, File source, File target) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(source)); PrintWriter writer = new PrintWriter(new FileWriter(target))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (isSameVehicle(line, vehicle)) {
                    writer.println(formatVehicle(vehicle));
                } else {
                    writer.println(line);
                }
            }
        }
    }

    private boolean isSameVehicle(String line, Vehicle vehicle) throws IOException, NullPointerException{
        String[] parts = line.split(DELIMITER);
        return parts.length > 1 && parts[1].equals(vehicle.getPlate());
    }

    private void replaceFile(File original, File temp) throws IOException {
        if (!original.delete()) {
            throw new IOException("Error borrando archivo");
        }
        if (!temp.renameTo(original)) {
            throw new IOException("Error renombrando archivo");
        }
    }

    public void deleteVehicle(String plate) throws IOException {
        File file = new File(fileName);
        File temp = new File(TEMPORAL );
        deleteVehicleFromFile(plate, file, temp);
        replaceFile(file, temp);
    }

    private void deleteVehicleFromFile(String plate, File source, File target) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(source)); PrintWriter writer = new PrintWriter(new FileWriter(target))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Vehicle vehicle = parseVehicle(line);
                if (vehicle != null && !vehicle.getPlate().equals(plate)) {
                    writer.println(line);
                }
            }
        }
    }

    public ArrayList<Vehicle> getAllVehicles() throws IOException {
        ArrayList<Vehicle> vehicles = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Vehicle vehicle = parseVehicle(line);
                if (vehicle != null) {
                    vehicles.add(vehicle);
                }
            }
        }
        return vehicles;
    }

    private Vehicle parseVehicle(String line) throws IOException, NullPointerException {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }
        
        String[] parts = line.split(DELIMITER, -1);

        if (parts.length != 11){
            return null;
        }

        return createVehicleFromParts(parts);
    }
    
    private Vehicle createVehicleFromParts(String[] parts) throws IOException {
        int id = Integer.parseInt(parts[0]);
        String plate = parts [1];
        String color = parts [2];
        String brand = parts [3];
        String model = parts [4];
        
        ArrayList<Customer> customers = parseCustomers(parts[5]);
        
        int vehicleTypeId = Integer.parseInt(parts[6]);
        int spaceId = Integer.parseInt(parts[7]);
        
        VehicleType vehicleType = vehicleTypeDataFile.getVehicleTypeFromFile(vehicleTypeId);
        Space space = spaceDataFile.getSpaceFromFile(spaceId);
        
        LocalDateTime entryTime = LocalDateTime.parse(parts[8], DATE_FORMATTER);
        LocalDateTime exitTime = parts[9].equals("null")? null : LocalDateTime.parse(parts[9], DATE_FORMATTER);
        
        float feeToPay = Float.parseFloat(parts[10]);
        
        return buildVehicle(id, plate, color, brand, model, customers, vehicleType, space, entryTime, exitTime, feeToPay);
    }
    
    private Vehicle buildVehicle(int id, String plate, String color, String brand, String model, ArrayList<Customer> customers, VehicleType vehicleType, Space space, LocalDateTime entryTime, LocalDateTime exitTime, float feeToPay) throws IOException, NullPointerException{
        Vehicle vehicle = new Vehicle();
        
        vehicle.setId(id);
        vehicle.setPlate(plate);
        vehicle.setColor(color);
        vehicle.setBrand(brand);
        vehicle.setModel(model);
        vehicle.setCustomer(customers);
        vehicle.setVehicleType(vehicleType);
        vehicle.setSpace(space);
        vehicle.setEntryTime(entryTime);
        vehicle.setExitTime(exitTime);
        vehicle.setFeeToPay(feeToPay);
        
        return vehicle;
    }

    private ArrayList<Customer> parseCustomers(String customerIds) throws IOException {
        
        ArrayList<Customer> customers = new ArrayList<>();
        if (customerIds == null || customerIds.isEmpty()) {
            return customers;
        }

        String[] ids = customerIds.split(CUSTOMER_DELIMITER);
        for (String idStr : ids) {
            int id = Integer.parseInt(idStr.trim());
            Customer customer = customerData.getCustomerById(id);
            if (customer != null) {
                customers.add(customer);
            }
        }
        return customers;
    }

    public Vehicle getVehicleByPlate(String plate) throws IOException {
        Vehicle vehicleToReturn = null;
        for (Vehicle vehicle : getAllVehicles()) {
            if (vehicle.getPlate().equalsIgnoreCase(plate)) {
                vehicleToReturn = vehicle;
            }
        }
        return vehicleToReturn;
    }

    public int getNextId() throws IOException {
        int maxId = 0;
        for (Vehicle vehicle : getAllVehicles()) {
            if (vehicle.getId() > maxId) {
                maxId = vehicle.getId();
            }
        }
        return maxId + 1;
    }

    public ArrayList<Vehicle> getByCustomerId(int customerId) throws IOException {
        ArrayList<Vehicle> result = new ArrayList<>();
        for (Vehicle vehicle : getAllVehicles()) {
            for (Customer customer : vehicle.getCustomer()) {
                if (customer.getId() == customerId) {
                    result.add(vehicle);
                    break;
                }
            }
        }
        return result;
    }
    
    public boolean isCurrentlyParked(Vehicle vehicle) throws IOException{
        return vehicle.getEntryTime() != null && vehicle.getExitTime() == null;
    }
    
    public ArrayList<Vehicle> getCurrentlyParkedVehicles() throws IOException {
        ArrayList<Vehicle> allVehicles = getAllVehicles();
        ArrayList<Vehicle> parkedVehicles = new ArrayList<>();
        
        for (Vehicle vehicle : allVehicles) {
            if (isCurrentlyParked(vehicle)) {
                parkedVehicles.add(vehicle);
            }
        }
        return parkedVehicles;
    }
}
