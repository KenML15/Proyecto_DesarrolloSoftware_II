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
import model.entities.VehicleType;

/**
 *
 * @author 50687
 */
public class VehicleTypeDataFile {

    private final String fileName;
    private static final String DELIMETER = ";";
    private static final String VEHICLETYPE_FILE = "VehicleTypes.txt";
    
    public VehicleTypeDataFile() throws IOException {
        this(VEHICLETYPE_FILE);
    }
    
    public VehicleTypeDataFile(String fileName) throws IOException {
        this.fileName = fileName;
        ensureFileExists();
        initializeDefaultTypes();
    }
    
    private void ensureFileExists() throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
    }
    
    private void initializeDefaultTypes() throws IOException {
        
        if (isFileEmpty()) {
            insertDefaultTypes();
        }
    }
    
    private boolean isFileEmpty() throws IOException {
        boolean empty;
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            empty = reader.readLine() == null;
        }
        return empty;
    }
    
    private void insertDefaultTypes() throws IOException {
        createMotorcycleType();
        createCarType();
        createTruckType();
        createBicycleType();
        createOtherType();
    }
    
    private void createMotorcycleType() throws IOException {
        VehicleType type = new VehicleType(1, "Moto", 2);
        insertVehicleType(type);
    }
    
    private void createCarType() throws IOException {
        VehicleType type = new VehicleType(2, "Liviano", 4);
        insertVehicleType(type);
    }
    
    private void createTruckType() throws IOException {
        VehicleType type = new VehicleType(3, "Pesado", 6);
        insertVehicleType(type);
    }
    
    private void createBicycleType() throws IOException {
        VehicleType type = new VehicleType(4, "Bicicleta", 2);
        insertVehicleType(type);
    }
    
    private void createOtherType() throws IOException {
        VehicleType type = new VehicleType(5, "Otro", 4);
        insertVehicleType(type);
    }
    
    public void insertVehicleType(VehicleType type) throws IOException {
        checkDuplicate(type.getId());
        appendToFile(type);
    }
    
    private void checkDuplicate(int id) throws IOException {
        if (getVehicleTypeFromFile(id) != null) {
            throw new IllegalArgumentException("Tipo ya existe");
        }
    }
    
    private void appendToFile(VehicleType type) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName, true))) {
            writer.println(formatVehicleType(type));
        }
    }
    
    private String formatVehicleType(VehicleType vehicleType) {
        
        return String.join(DELIMETER,
            String.valueOf(vehicleType.getId()),
            vehicleType.getDescription(),
            String.valueOf(vehicleType.getNumberOfTires())
        );
    }

    
    public VehicleType getVehicleTypeFromFile(int id) throws IOException {
        try(BufferedReader reader = new BufferedReader(new FileReader(fileName))){
        VehicleType result = findTypeById(id, reader);
        reader.close();
        
        return result;
        }
    }
    
    public VehicleType getVehicleTypeByDescription(String description) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                //Parsear la línea completa
                VehicleType type = parseVehicleType(line);
                if (type != null && type.getDescription().equalsIgnoreCase(description)) {
                    return type;
                }
            }
        }
        return null;
    }
    
    private VehicleType findTypeById(int id, BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            VehicleType type = parseVehicleType(line);
            if (type != null && type.getId() == id) {
                return type;
            }
        }
        return null;
    }
    
    private VehicleType parseVehicleType(String line) throws NumberFormatException{
            if (line == null || line.trim().isEmpty()) {
                return null;
            }

            String[] parts = line.split(DELIMETER, -1);
            if (parts.length != 3) {
                return null;
            }

            return new VehicleType(
                    Integer.parseInt(parts[0]),
                    parts[1],
                    Integer.parseInt(parts[2])
            );
        }
       
    public void updateVehicleType(VehicleType vehicleType) throws IOException {
        ArrayList<String> lines = new ArrayList<>();
        
        
        //Leer todas las líneas
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        
        //Reescribir con el tipo actualizado
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            for (String line : lines) {
                String[] parts = line.split(DELIMETER);
                if (parts.length > 0 && Integer.parseInt(parts[0]) == vehicleType.getId()) {
                    writer.println(formatVehicleType(vehicleType));
                } else {
                    writer.println(line);
                }
            }
        }
    }
    
    public ArrayList<VehicleType> getAllVehicleTypes() throws IOException {
        ArrayList<VehicleType> types = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            addAllTypesFromReader(types, reader);
            reader.close();
        }
        
        return types;
    }
    
    private void addAllTypesFromReader(ArrayList<VehicleType> types, BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            VehicleType vehicleType = parseVehicleType(line);
            if (vehicleType != null) {
                types.add(vehicleType);
            }
        }
    }
}
