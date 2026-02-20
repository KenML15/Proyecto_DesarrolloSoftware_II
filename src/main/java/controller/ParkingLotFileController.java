/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.io.IOException;
import java.util.ArrayList;
import model.data.ParkingLotDataFile;
import model.data.SpaceDataFile;
import model.data.VehicleDataFile;
import model.entities.Customer;
import model.entities.ParkingLot;
import model.entities.Space;
import model.entities.Vehicle;
import org.jdom2.JDOMException;

/**
 *
 * @author 50687
 */
public class ParkingLotFileController {

    private ParkingLotDataFile parkingLotData;
    private SpaceDataFile spaceData;
    private VehicleDataFile vehicleData;

    public ParkingLotFileController() throws IOException, JDOMException {
        this.parkingLotData = new ParkingLotDataFile();
        this.spaceData = new SpaceDataFile();
        this.vehicleData = new VehicleDataFile();
    }

    public void insertParkingLot(ParkingLot parkingLot) throws IOException {
        parkingLotData.insertParkingLot(parkingLot);
    }
    
    public ArrayList<ParkingLot> getAllParkingLots() throws IOException{
        return parkingLotData.getAllParkingLots();
    }
    
    public void updateParkingLot(ParkingLot parkingLot) throws IOException {
        parkingLotData.updateParkingLot(parkingLot);
    }
    
    public void deleteParkingLot(int id) throws IOException {
        //Validamos que no tenga vehículos antes de eliminarlo
        ParkingLot parkingLot = parkingLotData.getParkingLotById(id);
        if (parkingLot != null && !parkingLot.getVehicles().isEmpty()) {
            throw new IllegalStateException("No se puede eliminar un parqueo con vehículos dentro");
        }
        parkingLotData.deleteParkingLot(id);
    }
    
    public ParkingLot getParkingLotById(int id) throws IOException {
        return parkingLotData.getParkingLotById(id);
    }
    //================================================================================

    public Space[] createUniqueSpaces(Space[] spaceUniqueId) throws IOException{
        int nextLotId = parkingLotData.getNextId();
        for (int i = 0; i < spaceUniqueId.length; i++) {
            Space space = spaceUniqueId[i];
            space.setId((nextLotId * 100) + (i + 1)); //Genera 101, 102, 201, etc.
            space.setSpaceTaken(false);

            //Guarda los espacios en el archivo de espacios
            spaceData.insertSpace(space);
        }
        return spaceUniqueId;
    }
    
    public Space[] registerParkingLotSpaces(int quantity) throws IOException{
        Space[] spaces = new Space[quantity];
        for(int i = 0; i < quantity; i++){
            Space space = new Space();
            space.setVehicleType(null);
            space.setDisabilityAdaptation(false);
            space.setSpaceTaken(false);
            spaces[i] = space;
        }
        
        return createUniqueSpaces(spaces);
    }
    
    public void createParkingLot(String name, String address, int quantityOfSpaces) throws IOException {
        //Validar que el nombre no exista
        if (parkingLotData.existsByName(name)) {
            throw new IOException("El nombre del parqueo ya existe.");
        }
        
        Space[] spaces = registerParkingLotSpaces(quantityOfSpaces);

        ParkingLot newLot = new ParkingLot();
        newLot.setId(parkingLotData.getNextId());
        newLot.setName(name);
        newLot.setAddress(address);
        newLot.setNumberOfSpaces(spaces.length);
        newLot.setSpaces(spaces);
        newLot.setVehicles(new ArrayList<>());

        //Guardar el parqueo en el archivo de parqueos
        parkingLotData.insertParkingLot(newLot);
    }
    
    private void validateParkingLotName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre requerido");
        }
    }

    private void validateSpaces(Space[] spaces) {
        if (spaces == null || spaces.length == 0) {
            throw new IllegalArgumentException("Espacios requeridos");
        }
    }

    public int assignVehicleToParkingLot(Vehicle vehicle, int parkingLotId) throws IOException {

        validateVehicle(vehicle);
        
        ParkingLot parkingLot = parkingLotData.getParkingLotById(parkingLotId);
        validateParkingLot(parkingLot);

        Space availableSpace = findAvailableSpace(vehicle, parkingLot);

        availableSpace.setSpaceTaken(true);
        
        vehicle.setSpace(availableSpace);
        parkingLot.getVehicles().add(vehicle);

        spaceData.updateSpace(availableSpace);
        parkingLotData.updateParkingLot(parkingLot);

        return availableSpace.getId();
    }

    private void validateVehicle(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehículo requerido");
        }
        if (vehicle.getVehicleType() == null) {
            throw new IllegalArgumentException("Tipo de vehículo requerido");
        }
    }

    private void validateParkingLot(ParkingLot parkingLot) {
        if (parkingLot == null) {
            throw new IllegalArgumentException("Parqueo requerido");
        }
        if (parkingLot.getSpaces() == null) {
            throw new IllegalStateException("Parqueo sin espacios configurados");
        }
    }

    public Space findAvailableSpace(Vehicle vehicle, ParkingLot parkingLot) {
        boolean needsDisabled = hasDisabledCustomer(vehicle);

        for (Space space : parkingLot.getSpaces()) {
            if (!space.isSpaceTaken() && hasSameType(space, vehicle) && space.isDisabilityAdaptation() == needsDisabled) {
                return space;
            }
        }

        for (Space space : parkingLot.getSpaces()) {
            if (!space.isSpaceTaken() && hasSameType(space, vehicle)) {
                return space;
            }
        }

        for (Space space : parkingLot.getSpaces()) {
            if (!space.isSpaceTaken()) {
                return space;
            }
        }

        throw new IllegalStateException("No hay espacios disponibles");
    }

    private boolean hasSameType(Space space, Vehicle vehicle) {
        if (space.getVehicleType() == null) {
            return true;
        }
        if (vehicle.getVehicleType() == null) {
            return false;
        }
        
        return space.getVehicleType().getId() == vehicle.getVehicleType().getId();
    }

    private boolean hasDisabledCustomer(Vehicle vehicle) {
        if (vehicle.getCustomer() == null) {
            return false;
        }

        for (Customer customer : vehicle.getCustomer()) {
            if (customer.isDisabilityPresented()) {
                return true;
            }
        }
        return false;
    }
    
    public void freeSpaces(Vehicle vehicle) throws IOException{
        if(vehicle.getSpace() != null){
            Space space = vehicle.getSpace();

            space.setSpaceTaken(false);
            
            spaceData.updateSpace(space);

            //Removemos el vehículo del parqueo
            ParkingLot parkingLot = findParkingLotByVehicle(vehicle);
            if (parkingLot != null){
                parkingLot.getVehicles().removeIf( v -> v.getPlate().equals(vehicle.getPlate()));
                parkingLotData.updateParkingLot(parkingLot);
            }
        }
    }

    public String updateParkingLotSpaces(int parkingLotId, Space[] newSpaces) throws IOException {
        validateSpaces(newSpaces);
        ParkingLot parkingLot = getParkingLotById(parkingLotId);
        validateParkingLotForUpdate(parkingLot);
        
        //Elimina los espacios viejos
        for (Space oldSpace : parkingLot.getSpaces()) {
            spaceData.deleteSpace(oldSpace.getId());
        }
        
        //Actualizamos el parqueo
        updateParkingLotWithSpaces(parkingLot, newSpaces);
        
        return "Espacios actualizados exitosamente";
    }

    private void validateParkingLotForUpdate(ParkingLot parkingLot) {
        if (parkingLot == null) {
            throw new IllegalArgumentException("Parqueo no encontrado");
        }
        if (!parkingLot.getVehicles().isEmpty()) {
            throw new IllegalStateException("No se puede actualizar con vehículos");
        }
    }

    private void updateParkingLotWithSpaces(ParkingLot parkingLot, Space[] newSpaces) throws IOException {
        Space[] savedSpaces = createUniqueSpaces(newSpaces);
        parkingLot.setSpaces(savedSpaces);
        parkingLot.setNumberOfSpaces(savedSpaces.length);
        updateParkingLot(parkingLot);
    }

    private int getNextId() {
        try {
            return parkingLotData.getNextId();
        } catch (IOException e) {
            throw new RuntimeException("Error obteniendo ID: " + e.getMessage());
        }
    }

    //Método para buscar el parqueo según la placa del vehículo
    public ParkingLot findParkingLotByVehicle(Vehicle vehicle) throws IOException {
        for (ParkingLot parkingLot : getAllParkingLots()) {
            for (Vehicle vehicleToFound : parkingLot.getVehicles()) {
                if (vehicleToFound.getPlate().equals(vehicle.getPlate())) {
                    return parkingLot;
                }
            }
        }
        return null;
    }

    //Método para buscar vehículo por placa en el parqueo
    public Vehicle findVehicleInParkingLot(int parkingLotId, String plate) throws IOException {
        ParkingLot parkingLot = getParkingLotById(parkingLotId);
        if (parkingLot == null) {
            return null;
        }

        for (Vehicle vehicle : parkingLot.getVehicles()) {
            if (vehicle.getPlate().equalsIgnoreCase(plate)) {
                return vehicle;
            }
        }

        return null;
    }

    //Método para verificar disponibilidad de espacios por tipo de vehículo
    public int countAvailableSpacesByType(int parkingLotId, String vehicleTypeDescription) throws IOException {
        ParkingLot parkingLot = getParkingLotById(parkingLotId);
        if (parkingLot == null) {
            return 0;
        }

        int count = 0;
        for (Space space : parkingLot.getSpaces()) {
            if (!space.isSpaceTaken()) {
                //Si el espacio no tiene tipo asignado o coincide con el tipo buscado
                if (space.getVehicleType() == null || space.getVehicleType().getDescription().equalsIgnoreCase(vehicleTypeDescription)) {
                    count++;
                }
            }
        }

        return count;
    } 
}
