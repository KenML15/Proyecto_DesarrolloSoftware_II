/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.StringTokenizer;
import model.entities.Customer;
import model.entities.Vehicle;

/**
 *
 * @author 50687
 */
public class VehicleDataFile {
    public int exception = 0;
    String fileName;
    final int ID = 0, PLATE = 1, COLOR = 2, BRAND = 3, MODEL = 4, CUSTOMER = 5, VEHICLETYPE = 6, SPACE = 7, ENTRYTIME = 8;
    
    SpaceDataFile spaceData;
    VehicleTypeDataFile vehicleTypeData;
    CustomerDataFile customerData;

    public VehicleDataFile(String fileName) {

        this.fileName = fileName;
    }

    public VehicleDataFile() {
    }

    public VehicleDataFile(CustomerDataFile customerData, SpaceDataFile spaceData, VehicleTypeDataFile vehicleTypeData) {
        this.customerData = customerData;
        this.spaceData = spaceData;
        this.vehicleTypeData = vehicleTypeData;
    }
    
    public int insertVehicle(Vehicle vehicle){
        int result = -1;
        exception = 0; 
        
        try{
            File vehicleFile = new File(fileName);
 
            FileOutputStream fileOutputStream = new FileOutputStream(vehicleFile, true);

            PrintStream printStream = new PrintStream(fileOutputStream);

            boolean vehicleExist = findVehicleByPlate(vehicle.getPlate());
            
            
            String customerIdsText = "";

            ArrayList<Customer> customers = vehicle.getCustomer();

            for (int i = 0; i < customers.size(); i++) {
                customerIdsText += customers.get(i).getId();
                if (i < customers.size() - 1) {
                    customerIdsText += ",";
                }
            }

            if (!vehicleExist){
                printStream.println(vehicle.getPlate() + ";"
                        + vehicle.getColor() + ";"
                        + vehicle.getBrand() + ";"
                        + vehicle.getModel() + ";"
                        + customerIdsText + ";"
                        + vehicle.getVehicleType() + ";"
                        + vehicle.getSpace() + ";"
                        + vehicle.getEntryTime() + ";");

                result = 0;
            } else {
                exception = 3;
            } 
            
            fileOutputStream.close();
            printStream.close();
        } catch (FileNotFoundException fileException) {

            exception = 1;
   
        }catch(IOException e){
            exception = 2;
        }
        
        return result;
    }
    
    public boolean findVehicleByPlate(String plate) {

        exception = 0;
        boolean vehicleExists = false;
        String vehiclePlate = "",
                vehicleColor = "",
                vehicleBrand = "",
                vehicleModel = "",
                vehicleOwners = "",
                vehicleType = "",
                vehicleSpace = "",
                vehicleEntryTime = "";

        int counter = 0;

        try {

            File vehicleFile = new File(fileName);

            FileInputStream fileInputStream
                    = new FileInputStream(vehicleFile);

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String currentTuple = bufferedReader.readLine();

            while (currentTuple != null && !vehicleExists) {

                StringTokenizer stringTokenizer
                        = new StringTokenizer(currentTuple, ";");

                while (stringTokenizer.hasMoreTokens()) {

                    if (counter == PLATE) {

                        vehiclePlate = stringTokenizer.nextToken();

                    }
                    if (counter == COLOR) {

                        vehicleColor = stringTokenizer.nextToken();

                    }
                    if (counter == BRAND) {

                        vehicleBrand = stringTokenizer.nextToken();

                    }
                    if (counter == MODEL) {

                        vehicleModel = stringTokenizer.nextToken();

                    }
                    if (counter == CUSTOMER) {

                        vehicleOwners = stringTokenizer.nextToken();

                    }
                    if (counter == VEHICLETYPE) {

                        vehicleType = stringTokenizer.nextToken();

                    }
                    if (counter == SPACE) {

                        vehicleSpace = stringTokenizer.nextToken();

                    }
                    if (counter == ENTRYTIME) {

                        vehicleEntryTime = stringTokenizer.nextToken();

                    }
                    counter++;
                }

                if (plate.equalsIgnoreCase(vehiclePlate)) {
                    
                    vehicleExists = true;
                } else {

                    currentTuple = bufferedReader.readLine();
                }

                counter = 0;

            }

            bufferedReader.close();
            fileInputStream.close();
            inputStreamReader.close();

        } catch (FileNotFoundException fileException) {

            exception = 1;

        } catch (IOException ioException) {

            exception = 2;

        }

        return vehicleExists;
    }
    
    public Vehicle getVehicleFromFile(String plate) {

        exception = 0;
        
        String vehiclePlate = "",
                color = "",
                brand = "",
                model = "";

        int customerIds = 0;
        int vehicleTypeId = 0;
        int spaceId = 0;
        LocalDateTime entryTime = null;
        int counter = 0;
        
        Vehicle vehicleToReturn = null;

        String currentTuple = "";

        try {

            File vehicleFile = new File(fileName);

            FileInputStream fileInputStream = new FileInputStream(vehicleFile);

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            currentTuple = bufferedReader.readLine();

            while (currentTuple != null) {

                StringTokenizer stringTokenizer = new StringTokenizer(currentTuple, ";");

                while (stringTokenizer.hasMoreTokens()) {

                    if (counter == PLATE) {
                        vehiclePlate = stringTokenizer.nextToken();
                    }   else if (counter == COLOR) {
                        color = stringTokenizer.nextToken();
                    }else if (counter == BRAND) {
                        brand = stringTokenizer.nextToken();
                    }else if (counter == MODEL) {
                        model = stringTokenizer.nextToken();
                    }else if (counter == CUSTOMER) {
                        customerIds = Integer.parseInt(stringTokenizer.nextToken());
                    }else if (counter == VEHICLETYPE) {
                        vehicleTypeId = Integer.parseInt(stringTokenizer.nextToken());
                    }else if (counter == SPACE) {
                        spaceId = Integer.parseInt(stringTokenizer.nextToken());
                    }else if (counter == ENTRYTIME) {
                        entryTime = LocalDateTime.parse(stringTokenizer.nextToken());
                    } else {
                        stringTokenizer.nextToken();
                    }

                    counter++;
                }

                if (plate.equalsIgnoreCase(vehiclePlate)) {
                    ArrayList <Customer> customerList = new ArrayList<>();
                    customerList.add(customerData.getCustomerFromFile(customerIds));
                    
                    vehicleToReturn = new Vehicle(vehiclePlate, color, brand, model, customerList, 
                            vehicleTypeData.getVehicleTypeFromFile(vehicleTypeId),
                            spaceData.getSpaceFromFile(spaceId), entryTime);
                    break;
                }

                currentTuple = bufferedReader.readLine();

                counter = 0;

            }

            bufferedReader.close();
            fileInputStream.close();
            inputStreamReader.close();

        } catch (FileNotFoundException fileException) {

            exception = 1;

        } catch (IOException ioException) {

            exception = 2;
        }

        return vehicleToReturn;

    }
    
    // Método nuevo en VehicleDataFile.java
    /*public Vehicle buildVehicleFromLine(String vehicleFromFile) {
        if (vehicleFromFile == null || vehicleFromFile.isEmpty()) {
            return null;
        }

        StringTokenizer stringTokenizer = new StringTokenizer(vehicleFromFile, ";");

        // Extraemos los datos en el orden exacto del archivo
        int id = Integer.parseInt(stringTokenizer.nextToken());
        String plate = stringTokenizer.nextToken();
        String color = stringTokenizer.nextToken();
        String brand = stringTokenizer.nextToken();
        String model = stringTokenizer.nextToken();
        int customerId = Integer.parseInt(stringTokenizer.nextToken());
        int vehicleTypeId = Integer.parseInt(stringTokenizer.nextToken());
        int spaceId = Integer.parseInt(stringTokenizer.nextToken());

        
        Vehicle vehicle = new Vehicle();
        vehicle.setId(id);
        vehicle.setPlate(plate);
        vehicle.setColor(color);
        vehicle.setBrand(brand);
        vehicle.setModel(model);
        
        ArrayList <Customer> customerList = new ArrayList<>();
        customerList.add(customerData.getCustomerFromFile(customerId));
        
        vehicle.setCustomer(customerList);
        vehicle.setVehicleType(vehicleTypeData.getVehicleTypeFromFile(vehicleTypeId));
        vehicle.setSpace(spaceData.getSpaceFromFile(spaceId));

        return vehicle;
    }*/
    
    public ArrayList<Vehicle> getAllVehicles() {

        exception = 0;

        ArrayList<Vehicle> allVehicles = new ArrayList<>();

        String plate = "",
                color = "",
                brand = "",
                model = "",
                customerIds = "";

        int vehicleTypeId = 0;
        int spaceId = 0;
        LocalDateTime entryTime = null;

        int id = 0;

        int counter = 0;

        try {

            File vehicleFile = new File(fileName);
 
            FileInputStream fileInputStream = new FileInputStream(vehicleFile);

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String currentTuple = bufferedReader.readLine();

            while (currentTuple != null) {

                StringTokenizer stringTokenizer = new StringTokenizer(currentTuple, ";");

                while (stringTokenizer.hasMoreTokens()) {

                    if (counter == ID) {

                        id = Integer.parseInt(stringTokenizer.nextToken());

                    }
                    if (counter == PLATE) {

                        plate = stringTokenizer.nextToken();

                    }
                    if (counter == COLOR) {

                        color = stringTokenizer.nextToken();

                    }
                    if (counter == BRAND) {

                        brand = stringTokenizer.nextToken();

                    }
                    if (counter == MODEL) {

                        model = stringTokenizer.nextToken();

                    }
                    if (counter == CUSTOMER) {

                        customerIds = stringTokenizer.nextToken();

                    }
                    if (counter == VEHICLETYPE) {

                        vehicleTypeId = Integer.parseInt(stringTokenizer.nextToken());

                    }
                    if (counter == SPACE) {

                        spaceId = Integer.parseInt(stringTokenizer.nextToken());

                    }
                    if (counter == ENTRYTIME) {

                        entryTime = LocalDateTime.parse(stringTokenizer.nextToken());

                    }
                    counter++;
                }

                ArrayList<Customer> customers = new ArrayList<>();
                if (!customerIds.isEmpty()) {
                    String[] customerIdsSplit = customerIds.split(",");
                    for (String idsText : customerIdsSplit) {
                        int customerId = Integer.parseInt(idsText);
                        Customer customerLine = customerData.getCustomerFromFile(customerId);

                        if (customerLine != null) {
                            customers.add(customerLine);
                        }
                    }
                }

                Vehicle vehicle = new Vehicle(id, plate, color, brand, model, customers, vehicleTypeData.getVehicleTypeFromFile(vehicleTypeId), spaceData.getSpaceFromFile(spaceId), entryTime);
                allVehicles.add(vehicle);
                currentTuple = bufferedReader.readLine();

                counter = 0;

            }

            bufferedReader.close();
            fileInputStream.close();
            inputStreamReader.close();

        }
        catch (IOException ioE) {
            exception = 2;

        }
        return allVehicles;
    }
    
    public String[][] createVehicleMatrix(ArrayList<Vehicle> vehicles) {

        String[][] matrixVehiclesFromFile
                = new String[vehicles.size()][9];

        for (int i = 0; i < vehicles.size(); i++) {

            Vehicle vehicle = vehicles.get(i);

            matrixVehiclesFromFile[i][ID] = "" + vehicle.getId();
            matrixVehiclesFromFile[i][PLATE] = vehicle.getPlate();
            matrixVehiclesFromFile[i][COLOR] = vehicle.getColor();
            matrixVehiclesFromFile[i][BRAND] = vehicle.getBrand();
            matrixVehiclesFromFile[i][MODEL] = vehicle.getModel();
            matrixVehiclesFromFile[i][CUSTOMER] = "" + vehicle.getCustomer();
            matrixVehiclesFromFile[i][VEHICLETYPE] = "" + vehicle.getVehicleType();
            matrixVehiclesFromFile[i][SPACE] = "" + vehicle.getSpace();
            matrixVehiclesFromFile[i][ENTRYTIME] = "" + vehicle.getEntryTime();

        }
        
        return matrixVehiclesFromFile;
        
    }
        
    public void deleteVehicleFromFile(String lineToRemove) {

        exception = 0;

        try {

            File file = new File(fileName);

            File tempFile = new File("VehiclesTemp");

            BufferedReader bufferReader = new BufferedReader(new FileReader(fileName));
            PrintWriter printWriter = new PrintWriter(new FileWriter(tempFile));

            String line = null;

            while ((line = bufferReader.readLine()) != null) {

                if (!line.trim().equals(lineToRemove)) {

                    printWriter.println(line);
                    printWriter.flush();
                }
            }

            bufferReader.close();
            printWriter.close();

            if (!file.delete()) {

                exception = 4;
            }

            if (!tempFile.renameTo(file)) {

                exception = 5;

            }

        } catch (FileNotFoundException ex) {

            exception = 1;

        } catch (IOException ex) {

            exception = 2;
        }
    }
    
    public void modifyVehicleFromFile(String lineToModify, String newList) {

        exception = 0;

        try {

            File file = new File(fileName);

            File tempFile = new File("VehiclesTemp");

            BufferedReader bufferReader = new BufferedReader(new FileReader(fileName));
            PrintWriter printWriter = new PrintWriter(new FileWriter(tempFile));

            String line = null;

            while ((line = bufferReader.readLine()) != null) {

                if (!line.trim().equals(lineToModify)) {

                    printWriter.println(line);
                    printWriter.flush();
                } else {

                    printWriter.println(newList);
                }
            }

            bufferReader.close();
            printWriter.close();

            if (!file.delete()) {

                exception = 4;
            }

            if (!tempFile.renameTo(file)) {

                exception = 5;
            }

        } catch (FileNotFoundException ex) {

            exception = 1;

        } catch (IOException ex) {

            exception = 2;
        }
    }
    
    public int findLastIdNumberOfVehicle() {

        exception = 0;

        int counter = 0;
        int idVehicle = 0;

        try {

            File vehicleFile = new File(fileName);
 
            FileInputStream fileInputStream
                    = new FileInputStream(vehicleFile);

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String currentTuple = bufferedReader.readLine();

            while (currentTuple != null) {

                StringTokenizer stringTokenizer
                        = new StringTokenizer(currentTuple, ";");

                while (stringTokenizer.hasMoreTokens()) {

                    if (counter == ID) {

                        idVehicle = Integer.parseInt(stringTokenizer.nextToken());

                        break;
                    }

                    counter++;
                }

                currentTuple = bufferedReader.readLine();

                counter = 0;
            }

            bufferedReader.close();
            fileInputStream.close();
            inputStreamReader.close();

        } catch (FileNotFoundException fileException) {

            exception = 1;

        } catch (IOException ioException) {

            exception = 2;
        }

        return idVehicle;
    }
    
}
