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
import model.entities.Space;
import model.entities.VehicleType;

/**
 *
 * @author 50687
 */
public class SpaceDataFile {

    private final String fileName;
    private final VehicleTypeDataFile vehicleTypeData;
    private static final String DELIMITER = ";";
    private static final String DEFAULT_FILE = "Spaces.txt";
    
    public SpaceDataFile() throws IOException {
        this(DEFAULT_FILE, new VehicleTypeDataFile());
    }
    
    public SpaceDataFile(String fileName, VehicleTypeDataFile vehicleTypeData) throws IOException {
        this.fileName = fileName;
        this.vehicleTypeData = vehicleTypeData;
        ensureFileExists();
    }
    
    private void ensureFileExists() throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
    }
    
    public void insertSpace(Space space) throws IOException {
        validateSpace(space);
        checkDuplicate(space.getId());
        appendToFile(space);
    }
    
    private void validateSpace(Space space) throws IOException{
        if (space.getId() < 0) {
            throw new IllegalArgumentException("ID inválido");
        }
    }
    
    private void checkDuplicate(int id) throws IOException {
        if (getSpaceFromFile(id) != null) {
            throw new IllegalArgumentException("Espacio ya existe");
        }
    }
    
    private void appendToFile(Space space) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName, true))) {
            writer.println(formatSpace(space));
        }
    }

    
    private String formatSpace(Space space) {
        int typeId = getVehicleTypeId(space);
        return String.join(DELIMITER,
            String.valueOf(space.getId()),
            String.valueOf(space.isDisabilityAdaptation()),
            String.valueOf(space.isSpaceTaken()),
            String.valueOf(typeId)
        );
    }
    
    private int getVehicleTypeId(Space space) {
        if (space.getVehicleType() == null) {
            return 0;
        }
        return space.getVehicleType().getId();
    }
    
    public Space getSpaceFromFile(int id) throws IOException {
        Space result;
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            result = findSpaceById(id, reader);
        }
        return result;
    }
    
    private Space findSpaceById(int id, BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            Space space = parseSpace(line);
            if (space != null && space.getId() == id) {
                return space;
            }
        }
        return null;
    }
    
    private Space parseSpace(String line) throws IOException {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }
        
        String[] parts = line.split(DELIMITER, -1);
        if (parts.length != 4) {
            return null;
        }
        
        return createSpaceFromParts(parts);
    }
    
    private Space createSpaceFromParts(String[] parts) throws IOException {
        int id = Integer.parseInt(parts[0]);
        boolean adapt = Boolean.parseBoolean(parts[1]);
        boolean taken = Boolean.parseBoolean(parts[2]);
        int typeId = Integer.parseInt(parts[3]);
        
        Space space = new Space();
        space.setId(id);
        space.setDisabilityAdaptation(adapt);
        space.setSpaceTaken(taken);
        
        if (typeId > 0) {
            VehicleType type = vehicleTypeData.getVehicleTypeFromFile(typeId);
            space.setVehicleType(type);
        }
        
        return space;
    }
    
    public Space[] getAllSpaces() throws IOException {
        int totalSpaces = countLines(fileName);
        Space[] spaces = new Space[totalSpaces];
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            addAllSpacesFromReader(spaces, reader);
        }
        return spaces;
    }
    
    private int countLines(String fileName) throws IOException {

        int count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            while (reader.readLine() != null) {
                count++;
            }
        }
        return count;
    }

    
    private void addAllSpacesFromReader(Space[] spaces, BufferedReader reader) throws IOException {
        String line;
        int index = 0;
        while ((line = reader.readLine()) != null && index < spaces.length) {
            
            Space space = parseSpace(line);
            if (space != null) {
                spaces[index] = space;
                index++;
            }
        }
    }
    
    public void updateSpace(Space space) throws IOException {
        validateSpace(space);
        File file = new File(fileName);
        File temp = new File("temp_spaces.txt");
        replaceInFile(space, file, temp);
        replaceFile(file, temp);
    }
    
    private void replaceInFile(Space space, File source, File target) throws IOException {
        PrintWriter writer;
        try (BufferedReader reader = new BufferedReader(new FileReader(source))) {
            writer = new PrintWriter(new FileWriter(target));
            writeUpdatedFile(space, reader, writer);
        }
        writer.close();
    }
    
    private void writeUpdatedFile(Space space, BufferedReader reader, PrintWriter writer) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            if (isSameSpace(line, space)) {
                writer.println(formatSpace(space));
            } else {
                writer.println(line);
            }
        }
    }
    
    private boolean isSameSpace(String line, Space space) throws IOException{
        String[] parts = line.split(DELIMITER);
        return parts.length > 0 && Integer.parseInt(parts[0]) == space.getId();
    }
    
    private void replaceFile(File original, File temp) throws IOException {
        if (!original.delete()) {
            throw new IOException("Error borrando archivo");
        }
        if (!temp.renameTo(original)) {
            throw new IOException("Error renombrando archivo");
        }
    }
    
    public void deleteSpace(int id) throws IOException {
        File file = new File(fileName);
        File temp = new File("temp_spaces.txt");
        deleteFromFile(id, file, temp);
        replaceFile(file, temp);
    }
    
    private void deleteFromFile(int id, File source, File target) throws IOException {
        PrintWriter writer;
        try (BufferedReader reader = new BufferedReader(new FileReader(source))) {
            writer = new PrintWriter(new FileWriter(target));
            writeFileWithoutId(id, reader, writer);
        }
        writer.close();
    }
    
    private void writeFileWithoutId(int id, BufferedReader reader, PrintWriter writer) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            Space space = parseSpace(line);
            if (space == null || space.getId() != id) {
                writer.println(line);
            }
        }
    }
    
    public int getNextId() throws IOException, NullPointerException {
        int maxId = 0;
        for (Space space : getAllSpaces()) {
            if (space != null && space.getId() > maxId) {
                maxId = space.getId();
            }
        }
        return maxId + 1;
    }
    
    public Space[] getAvailableSpaces() throws IOException {
        Space[] allSpaces = getAllSpaces();
        
        int countAvailableSpaces = 0;
        for (Space space : allSpaces) {
            if (space != null && !space.isSpaceTaken()) {
                countAvailableSpaces++;
            }
        }
        
        Space[] availableSpaces = new Space[countAvailableSpaces];
        
        int index = 0;
        for (Space space : allSpaces) {
            if(space != null && !space.isSpaceTaken()){
                availableSpaces[index] = space;
                index++;
            }
        }
        
        return availableSpaces;
    }
}
