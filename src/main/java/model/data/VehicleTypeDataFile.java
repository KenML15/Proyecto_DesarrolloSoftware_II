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
import java.util.ArrayList;
import java.util.StringTokenizer;
import model.entities.VehicleType;

/**
 *
 * @author 50687
 */
class VehicleTypeDataFile {

    public int exception = 0;
    String fileName;
    final int ID = 0, DESCRIPTION = 1, TIRES = 2, FEE = 3;

    public VehicleTypeDataFile(String fileName) {

        this.fileName = fileName;

    }

    public int insert(VehicleType vehicleType) {

        int result = -1;
        exception = 0; 
        try {

            File vehicleTypeFile = new File(fileName);

            FileOutputStream fileOutputStream
                    = new FileOutputStream(vehicleTypeFile, true);

            PrintStream printStream
                    = new PrintStream(fileOutputStream);

            boolean vehicleTypeExists
                    = find(vehicleType.getId());

            if (!vehicleTypeExists) {

                printStream.println(vehicleType.getId() + ";"
                        + vehicleType.getDescription() + ";"
                        + vehicleType.getNumberOfTires() + ";"
                        + vehicleType.getFee() + ";");

                result = 0;

            } else {

                exception = 3;
            }

            fileOutputStream.close();
            printStream.close();

        } catch (FileNotFoundException fileException) {

            exception = 1;

        } catch (IOException ex) {

            exception = 2;
        }

        return result;
    }

    public void modifyVehicleTypeFromFile(String lineToModify, String newList) {

        exception = 0;

        try {

            File file = new File(fileName);

            File tempFile = new File("VehicleTypeTemp");

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

    public VehicleType getVehicleTypeFromFile(int id) {

        exception = 0;

        int counter = 0;
        int vehicleTypeId = 0;
        String description = "";
        int numberOfTires = 0;
        float fee = 0;

        VehicleType vehicleType = null;
        String currentTuple = "";

        try {

            File vehicleTypeFile = new File(fileName);

            FileInputStream fileInputStream
                    = new FileInputStream(vehicleTypeFile);

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            currentTuple = bufferedReader.readLine();

            while (currentTuple != null) {

                StringTokenizer stringTokenizer
                        = new StringTokenizer(currentTuple, ";");

                while (stringTokenizer.hasMoreTokens()) {

                    if (counter == ID){
                        
                        vehicleTypeId = Integer.parseInt(stringTokenizer.nextToken());
                    }
                    else if (counter == DESCRIPTION) {

                        description = stringTokenizer.nextToken();

                    } else if (counter == TIRES) {

                        numberOfTires = Integer.parseInt(stringTokenizer.nextToken());

                    } else if (counter == FEE) {

                        fee = Float.parseFloat(stringTokenizer.nextToken());

                    } else {

                        stringTokenizer.nextToken();

                    }

                    counter++;
                }

                if (id == vehicleTypeId) {
                    vehicleType = new VehicleType(vehicleTypeId, description, numberOfTires, fee);
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

        return vehicleType;

    }
    
    public VehicleType buildVehicleTypeFromLine(String vehicleTypeFromFile) {
        if (vehicleTypeFromFile == null || vehicleTypeFromFile.trim().isEmpty()) {
            return null;
        }

        StringTokenizer stringTokenizer = new StringTokenizer(vehicleTypeFromFile, ";");

        // El orden según tus constantes: ID=0, DESCRIPTION=1, TIRES=2, FEE=3
        int id = Integer.parseInt(stringTokenizer.nextToken());
        String description = stringTokenizer.nextToken();
        int tires = Integer.parseInt(stringTokenizer.nextToken());
        float fee = Float.parseFloat(stringTokenizer.nextToken());

        VehicleType vehicleType = new VehicleType();
        vehicleType.setId(id);
        vehicleType.setDescription(description);
        vehicleType.setNumberOfTires(tires);
        vehicleType.setFee(fee);

        return vehicleType;
    }

    public boolean find(int vehicleTypeId) {

        exception = 0;
        boolean vehicleTypeExists = false;
        
        int id = 0;
        
        int counter = 0;

        try {

            File vehicleTypeFile = new File(fileName);

            FileInputStream fileInputStream
                    = new FileInputStream(vehicleTypeFile);

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String currentTuple = bufferedReader.readLine();

            while (currentTuple != null && !vehicleTypeExists) {

                StringTokenizer stringTokenizer
                        = new StringTokenizer(currentTuple, ";");

                while (stringTokenizer.hasMoreTokens()) {

                    if (counter == ID) {

                        id = Integer.parseInt(stringTokenizer.nextToken());

                    }

                    counter++;
                }

                if (vehicleTypeId == id) {

                    vehicleTypeExists = true;
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

        return vehicleTypeExists;

    }

    public int findLastIdNumberOfVehicleType() {

        exception = 0;

        int counter = 0;
        int idVehicleType = 0;

        try {

            File vehicleTypeFile = new File(fileName);

            FileInputStream fileInputStream
                    = new FileInputStream(vehicleTypeFile);

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String currentTuple = bufferedReader.readLine();

            while (currentTuple != null) {

                StringTokenizer stringTokenizer
                        = new StringTokenizer(currentTuple, ";");

                while (stringTokenizer.hasMoreTokens()) {

                    if (counter == ID) {

                        idVehicleType = Integer.parseInt(stringTokenizer.nextToken());

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

        return idVehicleType;

    }

    public ArrayList<VehicleType> getAllVehicleTypes() {

        exception = 0;
        ArrayList<VehicleType> allVehicleTypes = new ArrayList<>();
        int id = 0;
        String description = "";
        int numberOfTires = 0;
        float fee = 0;

        
        int counter = 0;

        try {

            File vehicleTypeFile = new File(fileName);

            FileInputStream fileInputStream
                    = new FileInputStream(vehicleTypeFile);

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String currentTuple = bufferedReader.readLine();

            while (currentTuple != null) {

                StringTokenizer stringTokenizer
                        = new StringTokenizer(currentTuple, ";");

                while (stringTokenizer.hasMoreTokens()) {

                    if (counter == ID) {

                        id = Integer.parseInt(stringTokenizer.nextToken());

                    }
                    else if (counter == DESCRIPTION) {

                        description = stringTokenizer.nextToken();

                    }
                    else if (counter == TIRES) {

                        numberOfTires = Integer.parseInt(stringTokenizer.nextToken());

                    }
                    else if (counter == FEE) {

                        fee = Float.parseFloat(stringTokenizer.nextToken());

                    }

                    counter++;
                }

                VehicleType vehicleType = new VehicleType(id, description, numberOfTires, fee);
                allVehicleTypes.add(vehicleType);
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

        return allVehicleTypes;
    }

    public String[][] createVehicleTypeMatrix(ArrayList<VehicleType> vehicleTypes) {

        String[][] matrixVehicleTypesFromFile = new String[vehicleTypes.size()][4];

        for (int i = 0; i < vehicleTypes.size(); i++) {

            VehicleType vehicleType = vehicleTypes.get(i);

            matrixVehicleTypesFromFile[i][ID] = "" + vehicleType.getId();
            matrixVehicleTypesFromFile[i][DESCRIPTION] = vehicleType.getDescription();
            matrixVehicleTypesFromFile[i][TIRES] = "" + vehicleType.getNumberOfTires();
            matrixVehicleTypesFromFile[i][FEE] = "" + vehicleType.getFee();

        }

        return matrixVehicleTypesFromFile;

    }

    public void deleteVehicleTypeFromFile(String lineToRemove) {

        exception = 0;

        try {

            File file = new File(fileName);
 
            File tempFile = new File("VehicleTypeTemp");

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
