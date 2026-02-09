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

/**
 *
 * @author 50687
 */
public class ParkingLotFileController {

    private ParkingLotDataFile parkingLotData;
    private SpaceDataFile spaceData;
    private VehicleDataFile vehicleData;

    public ParkingLotFileController() throws IOException {
        this.parkingLotData = new ParkingLotDataFile();
        this.spaceData = new SpaceDataFile();
        this.vehicleData = new VehicleDataFile();
    }

    //Parqueos
    public void registerParkingLot(String name, Space[] spaces) throws IOException {
        validateParkingLotName(name);
        validateSpaces(spaces);
        
        if (parkingLotData.existsByName(name)) {
            throw new IllegalArgumentException("Ya existe un parqueo con el nombre: " + name);
        }

        ParkingLot parkingLot = createParkingLot(name, spaces);
        
        for (Space space : spaces) {
            if (spaceData.getSpaceFromFile(space.getId()) != null) {
                throw new IOException("Error crítico: El ID de espacio " + space.getId() + " ya existe.");
            }
        }

        parkingLotData.insertParkingLot(parkingLot);

        for (Space space : spaces) {
            spaceData.insertSpace(space);
        }
    }
    
    //Construye el objeto, pero no guarda en el archivoo
    private ParkingLot createParkingLot(String name, Space[] spaces) throws IOException {
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setId(getNextId());
        parkingLot.setName(name);
        parkingLot.setNumberOfSpaces(spaces.length);
        
        for(int i = 0; i<spaces.length; i++){
            Space space = new Space();
            space.setId((parkingLot.getId() * 100) + (i+1));
            space.setSpaceTaken(false);
            spaces[i] = space;
        }
        parkingLot.setSpaces(spaces);
        parkingLot.setVehicles(new ArrayList<>());
        
        for (Space space : spaces) {
            spaceData.insertSpace(space);
        }
        return parkingLot;
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
    
    //Asignaciones
    private void saveSpaces(Space[] spaces) throws IOException {
        for (Space space : spaces) {
            spaceData.insertSpace(space);
        }
    }

    public int assignVehicleToParkingLot(Vehicle vehicle, ParkingLot parkingLot) throws IOException {

        validateVehicle(vehicle);
        validateParkingLot(parkingLot);
        
        Space availableSpace = findAvailableSpace(vehicle, parkingLot);
        availableSpace.setSpaceTaken(true);
        vehicle.setSpace(availableSpace);
        
        parkingLot.getVehicles().add(vehicle);
        
        spaceData.updateSpace(availableSpace);
        parkingLotData.updateParkingLot(parkingLot);
        //occupySpaceWithVehicle(vehicle, availableSpace, parkingLot);
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
            if (!space.isSpaceTaken()
                && hasSameType(space, vehicle)
                && space.isDisabilityAdaptation() == needsDisabled) {
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
        if (space.getVehicleType() == null){
            return true;
        }
        if (vehicle.getVehicleType() == null) {
            return false;
        }
        return space.getVehicleType().getId() == vehicle.getVehicleType().getId();
    }

    private boolean hasDisabledCustomer(Vehicle vehicle) {
        if (vehicle.getCustomer() == null){
            return false;
        }
        
        for (Customer customer : vehicle.getCustomer()) {
            if (customer.isDisabilityPresented()) {
                return true;
            }
        }
        return false;
    }
    
    public void updateParkingLot(ParkingLot parkingLot) {
        try {
            parkingLotData.updateParkingLot(parkingLot);
        } catch (IOException e) {
            throw new RuntimeException("Error actualizando: " + e.getMessage());
        }
    }
    
        public ParkingLot findParkingLotById(int id) throws IOException{
        return parkingLotData.getParkingLotById(id);
    }

    public ArrayList<ParkingLot> getAllParkingLots() {
        return parkingLotData.getAllParkingLots();
    }

    public String removeParkingLot(int id) {
        try {
            ParkingLot parkingLot = parkingLotData.getParkingLotById(id);
            validateParkingLotForRemoval(parkingLot);
            removeSpaces(parkingLot.getSpaces());
            parkingLotData.deleteParkingLot(id);
            return "Parqueo eliminado exitosamente";
        } catch (IOException e) {
            throw new RuntimeException("Error eliminando: " + e.getMessage());
        }
    }
    
        private void validateParkingLotForRemoval(ParkingLot parkingLot) {
        if (parkingLot == null) {
            throw new IllegalArgumentException("Parqueo no encontrado");
        }
        if (!parkingLot.getVehicles().isEmpty()) {
            throw new IllegalStateException("No se puede eliminar con vehículos");
        }
    }

    private void removeSpaces(Space[] spaces) throws IOException {
        for (Space space : spaces) {
            if (space != null) {
                spaceData.deleteSpace(space.getId());
            }
        }
    }

    public String updateParkingLotSpaces(int parkingLotId, Space[] newSpaces) throws IOException {
        validateSpaces(newSpaces);
        ParkingLot parkingLot = findParkingLotById(parkingLotId);
        validateParkingLotForUpdate(parkingLot);
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

    private void updateParkingLotWithSpaces(ParkingLot parkingLot, Space[] newSpaces) {
        parkingLot.setSpaces(newSpaces);
        parkingLot.setNumberOfSpaces(newSpaces.length);
        updateParkingLot(parkingLot);
    }
    
        private int getNextId() {
        try {
            return parkingLotData.getNextId();
        } catch (IOException e) {
            throw new RuntimeException("Error obteniendo ID: " + e.getMessage());
        }
    }

    //Método para liberar espacio cuando un vehículo sale
    public void freeVehicleSpace(Vehicle vehicle) throws IOException {
        if (vehicle.getSpace() != null) {
            Space space = vehicle.getSpace();
            space.setSpaceTaken(false);
            spaceData.updateSpace(space);
        }
    }
    
    //Método para buscar vehículo por placa en el parqueo
    public Vehicle findVehicleInParkingLot(int parkingLotId, String plate) throws IOException {
        ParkingLot parkingLot = findParkingLotById(parkingLotId);
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
        ParkingLot parkingLot = findParkingLotById(parkingLotId);
        if (parkingLot == null) {
            return 0;
        }
        
        int count = 0;
        for (Space space : parkingLot.getSpaces()) {
            if (!space.isSpaceTaken()) {
                //Si el espacio no tiene tipo asignado o coincide con el tipo buscado
                if (space.getVehicleType() == null || 
                    space.getVehicleType().getDescription().equalsIgnoreCase(vehicleTypeDescription)) {
                    count++;
                }
            }
        }
        
        return count;
    }
    
    //Estado
    public String getParkingLotStatusById(int parkingLotId) throws IOException {
        ParkingLot parkingLot = parkingLotData.getParkingLotById(parkingLotId);

        if (parkingLot == null) {
            throw new IllegalArgumentException("Parqueo no encontrado");
        }

        return buildStatus(parkingLot);
    }
    
    private String buildStatus(ParkingLot parkingLot) {
        Space[] spaces = parkingLot.getSpaces();

        int occupied = 0;
        int disability = 0;

        for (Space space : spaces) {
            if (space.isSpaceTaken()) occupied++;
            if (space.isDisabilityAdaptation()) disability++;
        }

        return """
               ESTADO DEL PARQUEO
               ------------------------
               Nombre: %s
               Espacios totales: %d
               Ocupados: %d
               Libres: %d
               Espacios discapacidad: %d
               """.formatted(
                parkingLot.getName(),
                spaces.length,
                occupied,
                spaces.length - occupied,
                disability
        );
    }
}
