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
import java.util.ArrayList;
import model.entities.ParkingLot;
import model.entities.Space;
import model.entities.Vehicle;

/**
 *
 * @author 50687
 */
public class ParkingLotDataFile {

    private static final String PARKINGLOT_FILE = "ParkingLots.txt";
    private static final String FIELD_DELIMITER = ";";
    private static final String ITEM_DELIMITER = ",";

    private SpaceDataFile spaceData = new SpaceDataFile();
    private VehicleDataFile vehicleData;

    public ParkingLotDataFile() throws IOException {
        spaceData = new SpaceDataFile();
        vehicleData = new VehicleDataFile();
        ensureFileExists();
    }

    private void ensureFileExists() {
        File file = new File(PARKINGLOT_FILE);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Error al crear el archivo del parqueo");
            }
        }
    }
    
    //Verifica si el nombre del parqueo ya existe
    public boolean existsByName(String name) {
        for (ParkingLot p : getAllParkingLots()) {
            if (p.getName().equalsIgnoreCase(name.trim())) {
                return true;
            }
        }
        return false;
    }

    public void insertParkingLot(ParkingLot parkingLot) throws IOException {
        if(getParkingLotById(parkingLot.getId()) != null){
            return;
        }
        //Se hace la validación antes de insertar
        if (existsByName(parkingLot.getName())) {
            throw new IOException("El parqueo con el nombre '" + parkingLot.getName() + "' ya existe en el archivo.");
        }
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(PARKINGLOT_FILE, true))) {
            writer.println(formatParkingLot(parkingLot));
        }
    }

    public void updateParkingLot(ParkingLot parkingLot) throws IOException {
        ArrayList<ParkingLot> parkingLots = getAllParkingLots();

        try (PrintWriter writer = new PrintWriter(new FileWriter(PARKINGLOT_FILE))) {
            for (ParkingLot p : parkingLots) {
                if (p.getId() == parkingLot.getId()) {
                    writer.println(formatParkingLot(parkingLot));
                } else {
                    writer.println(formatParkingLot(p));
                }
            }
        }
    }

    public void deleteParkingLot(int id) throws IOException {
        ArrayList<ParkingLot> parkingLots = getAllParkingLots();

        try (PrintWriter writer = new PrintWriter(new FileWriter(PARKINGLOT_FILE))) {
            for (ParkingLot p : parkingLots) {
                if (p.getId() != id) {
                    writer.println(formatParkingLot(p));
                }
            }
        }
    }

    public ParkingLot getParkingLotById(int id) throws IOException {
        for (ParkingLot parkingLot : getAllParkingLots()) {
            if (parkingLot.getId() == id) {
                return parkingLot;
            }
        }
        return null;
    }
    
    public int getNextId() throws IOException {
        int maxId = 0;

        for (ParkingLot parkingLot : getAllParkingLots()) {
            if (parkingLot.getId() > maxId) {
                maxId = parkingLot.getId();
            }
        }

        return maxId + 1;
    }
    
    private String formatParkingLot(ParkingLot parkingLot) {

        String vehicles = formatVehicles(parkingLot.getVehicles());
        String spaces = formatSpaces(parkingLot.getSpaces());

        return parkingLot.getId() + FIELD_DELIMITER
                + parkingLot.getName() + FIELD_DELIMITER
                + parkingLot.getNumberOfSpaces() + FIELD_DELIMITER
                + vehicles + FIELD_DELIMITER
                + spaces;
    }
    
    private String formatVehicles(ArrayList<Vehicle> vehicles) {
        if (vehicles == null || vehicles.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (Vehicle v : vehicles) {
            sb.append(v.getPlate()).append(ITEM_DELIMITER);
        }
        return sb.substring(0, sb.length() - 1);
    }

    private String formatSpaces(Space[] spaces) {
        if (spaces == null || spaces.length == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (Space s : spaces) {
            if (s != null) {
                sb.append(s.getId()).append(ITEM_DELIMITER);
            }
        }
        return sb.substring(0, sb.length() - 1);
    }

    public ArrayList<ParkingLot> getAllParkingLots() {
        ArrayList<ParkingLot> parkingLots = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(PARKINGLOT_FILE))) {
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(FIELD_DELIMITER);

                if (parts.length < 5) {
                    continue;
                }

                int id = Integer.parseInt(parts[0]);
                String name = parts[1];
                int numberOfSpaces = Integer.parseInt(parts[2]);

                ArrayList<Vehicle> vehicles = parseVehicles(parts[3]);
                Space[] spaces = parseSpaces(parts[4], numberOfSpaces);

                ParkingLot parkingLot = new ParkingLot(id, name, numberOfSpaces, vehicles, spaces);

                parkingLots.add(parkingLot);
            }

        } catch (IOException e) {
            System.out.println("Error al leer el archivo del parqueo");
        }

        return parkingLots;
    }

    private Space[] parseSpaces(String spaceIds, int numberOfSpaces) throws IOException {
        Space[] spaces = new Space[numberOfSpaces];

        if (spaceIds == null || spaceIds.isEmpty()) {
            return spaces;
        }

        String[] ids = spaceIds.split(ITEM_DELIMITER);
        int index = 0;

        for (String idStr : ids) {
            if (index >= numberOfSpaces) {
                break;
            }

            int id = Integer.parseInt(idStr.trim());
            Space space = spaceData.getSpaceFromFile(id);
            if (space != null) {
                spaces[index] = space;
                index++;
            } else {
                //Para probar
                Space placeholder = new Space();
                placeholder.setId(id);
                placeholder.setSpaceTaken(false);
                spaces[index] = placeholder;
            }
        }

        return spaces;
    }

    private ArrayList<Vehicle> parseVehicles(String vehiclePlates) throws IOException {
        ArrayList<Vehicle> vehicles = new ArrayList<>();

        if (vehiclePlates == null || vehiclePlates.isEmpty()) {
            return vehicles;
        }

        String[] plates = vehiclePlates.split(ITEM_DELIMITER);

        for (String plate : plates) {
            String plateToFound = plate.trim();
            Vehicle vehicle = vehicleData.getVehicleByPlate(plateToFound);
            if (vehicle != null) {
                vehicles.add(vehicle);
            }
        }

        return vehicles;
    }
}
