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
import model.entities.ParkingLot;
import model.entities.Space;
import model.entities.Vehicle;

/**
 *
 * @author user
 */
public class ParkingLotDataFile {

    public int exception = 0;
    String fileName;
    final int ID = 0, NAME = 1, NUMBERSPACES = 2, VEHICLES = 3, SPACES = 4;

    SpaceDataFile spaceDataFile;
    VehicleDataFile vehicleDataFile;

    public ParkingLotDataFile(String fileName) {

        this.fileName = fileName;
    }

    public ParkingLotDataFile() {
    }

    public ParkingLotDataFile(SpaceDataFile spaceDataFile, VehicleDataFile vehicleDataFile) {
        this.spaceDataFile = spaceDataFile;
        this.vehicleDataFile = vehicleDataFile;
    }

    public int insertParkingLot(ParkingLot parkingLot) {
        int result = -1;
        exception = 0;

        try {
            File parkingLotFile = new File(fileName);

            FileOutputStream fileOutputStream = new FileOutputStream(parkingLotFile, true);

            PrintStream printStream = new PrintStream(fileOutputStream);

            boolean parkingLotExists = findParkingLotById(parkingLot.getId());

            String vehicleIdsText = "";

            ArrayList<Vehicle> vehicles = parkingLot.getVehicles();

            if (vehicles != null) {
                for (int i = 0; i < vehicles.size(); i++) {
                    vehicleIdsText += vehicles.get(i).getId();
                    if (i < vehicles.size() - 1) {
                        vehicleIdsText += ",";
                    }
                }
            }

            //Convertir los ids del espacio
            String spaceIdsText = "";
            Space[] spaces = parkingLot.getSpaces();
            if (spaces != null) {
                for (int i = 0; i < spaces.length; i++) {
                    spaceIdsText += spaces[i].getId();
                    if (i < spaces.length - 1) {
                        spaceIdsText += ",";
                    }
                }
            }

            if (!parkingLotExists) {
                printStream.println(parkingLot.getId() + ";"
                        + parkingLot.getName() + ";"
                        + parkingLot.getNumberOfSpaces() + ";"
                        + vehicleIdsText + ";"
                        + spaceIdsText + ";");

                result = 0;
            } else {
                exception = 3;
            }

            fileOutputStream.close();
            printStream.close();
        } catch (FileNotFoundException fileException) {
            exception = 1;
        } catch (IOException e) {
            exception = 2;
        }

        return result;
    }

    public boolean findParkingLotById(int parkingLotId) {

        exception = 0;
        boolean parkingLotExists = false;
        String parkingLotName = "",
                parkingLotVehicles = "",
                parkingLotSpaces = "";

        int id = 0;
        int numberOfSpaces = 0;

        int counter = 0;

        try {

            File parkingLotFile = new File(fileName);

            FileInputStream fileInputStream
                    = new FileInputStream(parkingLotFile);

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String currentTuple = bufferedReader.readLine();

            while (currentTuple != null && !parkingLotExists) {

                StringTokenizer stringTokenizer
                        = new StringTokenizer(currentTuple, ";");

                while (stringTokenizer.hasMoreTokens()) {
                    if (counter == ID) {
                        id = Integer.parseInt(stringTokenizer.nextToken());
                    }
                    if (counter == NAME) {
                        parkingLotName = stringTokenizer.nextToken();
                    }
                    if (counter == NUMBERSPACES) {
                        numberOfSpaces = Integer.parseInt(stringTokenizer.nextToken());
                    }
                    if (counter == VEHICLES) {
                        parkingLotVehicles = stringTokenizer.nextToken();
                    }
                    if (counter == SPACES) {
                        parkingLotSpaces = stringTokenizer.nextToken();
                    }

                    counter++;
                }

                if (parkingLotId == id) {

                    parkingLotExists = true;
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

        return parkingLotExists;
    }

    public ParkingLot getParkingLotFromFile(int parkingLotId) {

        exception = 0;

        String parkingLotName = "",
                parkingLotVehiclesPlate = "";

        int id = 0;
        int numberOfSpaces = 0;
        int parkingLotSpacesId = 0;

        int counter = 0;

        ParkingLot parkingLotToReturn = null;
        String currentTuple = "";

        try {

            File parkingLotFile = new File(fileName);

            FileInputStream fileInputStream = new FileInputStream(parkingLotFile);

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            currentTuple = bufferedReader.readLine();

            while (currentTuple != null) {

                StringTokenizer stringTokenizer = new StringTokenizer(currentTuple, ";");

                while (stringTokenizer.hasMoreTokens()) {

                    if (counter == ID) {
                        id = Integer.parseInt(stringTokenizer.nextToken());
                    } else if (counter == NAME) {
                        parkingLotName = stringTokenizer.nextToken();
                    } else if (counter == NUMBERSPACES) {
                        numberOfSpaces = Integer.parseInt(stringTokenizer.nextToken());
                    } else if (counter == VEHICLES) {
                        parkingLotVehiclesPlate = stringTokenizer.nextToken();
                    } else if (counter == SPACES) {
                        parkingLotSpacesId = Integer.parseInt(stringTokenizer.nextToken());
                    } else {
                        stringTokenizer.nextToken();
                    }

                    counter++;
                }

                if (parkingLotId == id) {
                    ArrayList<Vehicle> vehicleList = new ArrayList<>();
                    vehicleList.add(vehicleDataFile.getVehicleFromFile(parkingLotVehiclesPlate));

                    Space[] spaceArray = new Space[1];
                    spaceArray[0] = spaceDataFile.getSpaceFromFile(parkingLotSpacesId);

                    parkingLotToReturn = new ParkingLot(id, parkingLotName, numberOfSpaces, vehicleList,
                            spaceArray);
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

        return parkingLotToReturn;
    }

    public ArrayList<ParkingLot> getAllParkingLots() {

        exception = 0;

        ArrayList<ParkingLot> allParkingLots = new ArrayList<>();

        String name = "",
                vehicles = "";

        int id = 0;
        int numberOfSpaces = 0;
        int spaces = 0;

        int counter = 0;

        try {

            File parkingLotFile = new File(fileName);

            FileInputStream fileInputStream = new FileInputStream(parkingLotFile);

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String currentTuple = bufferedReader.readLine();

            while (currentTuple != null) {

                StringTokenizer stringTokenizer = new StringTokenizer(currentTuple, ";");

                while (stringTokenizer.hasMoreTokens()) {

                    if (counter == ID) {
                        id = Integer.parseInt(stringTokenizer.nextToken());
                    }
                    if (counter == NAME) {
                        name = stringTokenizer.nextToken();
                    }
                    if (counter == NUMBERSPACES) {
                        numberOfSpaces = Integer.parseInt(stringTokenizer.nextToken());
                    }
                    if (counter == VEHICLES) {
                        vehicles = stringTokenizer.nextToken();
                    }
                    if (counter == SPACES) {
                        spaces = Integer.parseInt(stringTokenizer.nextToken());
                    }

                    counter++;
                }

                ArrayList<Vehicle> vehiclesInParkingLot = new ArrayList<>();
                if (!vehicles.isEmpty()) {
                    String[] vehiclesSplit = vehicles.split(",");
                    for (String vehiclePlate : vehiclesSplit) {
                        Vehicle vehicle = vehicleDataFile.getVehicleFromFile(vehiclePlate);

                        if (vehicle != null) {
                            vehiclesInParkingLot.add(vehicle);
                        }
                    }
                }

                Space[] spaceArray = new Space[1];
                spaceArray[0] = spaceDataFile.getSpaceFromFile(spaces);

                ParkingLot parkingLot = new ParkingLot(id, name, numberOfSpaces, vehiclesInParkingLot, spaceArray);
                allParkingLots.add(parkingLot);
                currentTuple = bufferedReader.readLine();

                counter = 0;

            }

            bufferedReader.close();
            fileInputStream.close();
            inputStreamReader.close();

        } catch (IOException ioE) {
            exception = 2;

        }
        return allParkingLots;
    }

    public String[][] createParkingLotMatrix(ArrayList<ParkingLot> parkingLots) {

        String[][] matrixParkingLotFromFile
                = new String[parkingLots.size()][9];

        for (int i = 0; i < parkingLots.size(); i++) {

            ParkingLot parkingLot = parkingLots.get(i);

            matrixParkingLotFromFile[i][ID] = "" + parkingLot.getId();
            matrixParkingLotFromFile[i][NAME] = parkingLot.getName();
            matrixParkingLotFromFile[i][NUMBERSPACES] = "" + parkingLot.getNumberOfSpaces();
            matrixParkingLotFromFile[i][VEHICLES] = "" + parkingLot.getVehicles();
            matrixParkingLotFromFile[i][SPACES] = "" + parkingLot.getSpaces();
        }

        return matrixParkingLotFromFile;
    }

    public void deleteParkingLotFromFile(String lineToRemove) {

        exception = 0;

        try {

            File file = new File(fileName);

            File tempFile = new File("ParkingLotTemp");

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

}
