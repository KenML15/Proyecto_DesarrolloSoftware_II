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
import model.entities.Space;

/**
 *
 * @author 50687
 */
class SpaceDataFile {

    public int exception = 0;
    String fileName;
    final int SPACEID = 0, ADAPT = 1, TAKEN = 2, VEHICLETYPE = 3;

    public SpaceDataFile(String fileName) {

        this.fileName = fileName;

    }

    public int insert(Space space) {

        int result = -1;
        exception = 0;

        try {

            File spaceFile = new File(fileName);
 
            FileOutputStream fileOutputStream
                    = new FileOutputStream(spaceFile, true);

            PrintStream printStream
                    = new PrintStream(fileOutputStream);

            boolean spaceExists = find(space.getId());

            if (!spaceExists) {

                printStream.println(space.getId() + ";"
                        + space.isDisabilityAdaptation() + ";"
                        + space.isSpaceTaken() + ";"
                        + space.getVehicleTypeId() + ";");

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

    public void modifySpacesFromFile(String lineToModify, String newSpace) {

        exception = 0;

        try {

            File file = new File(fileName);

            File tempFile = new File("SpacesTemp");

            BufferedReader bufferReader = new BufferedReader(new FileReader(fileName));
            PrintWriter printWriter = new PrintWriter(new FileWriter(tempFile));

            String line = null;

            while ((line = bufferReader.readLine()) != null) {

                if (!line.trim().equals(lineToModify)) {

                    printWriter.println(line);
                    printWriter.flush();
                } else {

                    printWriter.println(newSpace);
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

    public Space getSpaceFromFile(int spaceId) {

        exception = 0;

        int counter = 0;
        int spaceNumber = 0;
        boolean disabilityAdaptation = false;
        boolean spaceTaken = false;
        int vehicleTypeId = 0;

        Space space = null;
        String currentTuple = "";

        try {

            File spaceFile = new File(fileName);

            FileInputStream fileInputStream
                    = new FileInputStream(spaceFile);

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            currentTuple = bufferedReader.readLine();

            while (currentTuple != null) {

                StringTokenizer stringTokenizer
                        = new StringTokenizer(currentTuple, ";");

                while (stringTokenizer.hasMoreTokens()) {

                    if (counter == SPACEID) {

                        spaceNumber = Integer.parseInt(stringTokenizer.nextToken());
                    }
                    else if (counter == ADAPT) {

                        disabilityAdaptation = Boolean.parseBoolean(stringTokenizer.nextToken());

                    }
                    else if (counter == TAKEN) {

                        spaceTaken = Boolean.parseBoolean(stringTokenizer.nextToken());

                    }
                    else if (counter == VEHICLETYPE) {

                        vehicleTypeId = Integer.parseInt(stringTokenizer.nextToken());

                    } else {

                        stringTokenizer.nextToken();

                    }

                    counter++;
                }

                if (spaceId == spaceNumber) {
                    space = new Space(spaceNumber, disabilityAdaptation, spaceTaken, vehicleTypeId);
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
 
        return space;

    }

    public boolean find(int spaceId) {

        exception = 0;
        boolean spaceExists = false;

        int id = 0;
        int counter = 0;

        try {

            File spaceFile = new File(fileName);

            FileInputStream fileInputStream
                    = new FileInputStream(spaceFile);

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String currentTuple = bufferedReader.readLine();

            while (currentTuple != null && !spaceExists) {

                StringTokenizer stringTokenizer
                        = new StringTokenizer(currentTuple, ";");

                while (stringTokenizer.hasMoreTokens()) {

                    if (counter == SPACEID) {

                        id = Integer.parseInt(stringTokenizer.nextToken());

                    }
                    
                    counter++;
                }

                if (spaceId == id) {

                    spaceExists = true;
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

        return spaceExists;
    }

    public int findLastIdNumberOfSpace() {

        exception = 0;

        int counter = 0;
        int idSpace = 0;

        try {

            File spaceFile = new File(fileName);

            FileInputStream fileInputStream
                    = new FileInputStream(spaceFile);

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String currentTuple = bufferedReader.readLine();

            while (currentTuple != null) {

                StringTokenizer stringTokenizer
                        = new StringTokenizer(currentTuple, ";");

                while (stringTokenizer.hasMoreTokens()) {

                    if (counter == SPACEID) {

                        idSpace = Integer.parseInt(stringTokenizer.nextToken());

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

        return idSpace;
    }

    public ArrayList<Space> getAllSpaces() {

        exception = 0;
        ArrayList<Space> allSpaces = new ArrayList<>();

        int spaceNumber = 0;
        boolean disabilityAdaptation = false;
        boolean spaceTaken = false;
        int vehicleTypeId = 0;
        int counter = 0;

        try {

            File customerFile = new File(fileName);
 
            FileInputStream fileInputStream
                    = new FileInputStream(customerFile);

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String currentTuple = bufferedReader.readLine();

            while (currentTuple != null) {

                StringTokenizer stringTokenizer
                        = new StringTokenizer(currentTuple, ";");

                while (stringTokenizer.hasMoreTokens()) {

                    if (counter == SPACEID) {

                        spaceNumber = Integer.parseInt(stringTokenizer.nextToken());

                    }else if (counter == ADAPT) {

                        disabilityAdaptation = Boolean.parseBoolean(stringTokenizer.nextToken());

                    }else if (counter == TAKEN) {

                        spaceTaken = Boolean.parseBoolean(stringTokenizer.nextToken());

                    }else if (counter == VEHICLETYPE) {

                        vehicleTypeId = Integer.parseInt(stringTokenizer.nextToken());

                    }

                    counter++;
                }

                Space space = new Space(spaceNumber, disabilityAdaptation, spaceTaken, vehicleTypeId);
                allSpaces.add(space);
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

        return allSpaces;

    }

    public String[][] createSpaceMatrix(ArrayList<Space> spaces) {

        String[][] matrixSpacesFromFile
                = new String[spaces.size()][4];

        for (int i = 0; i < spaces.size(); i++) {

            Space space = spaces.get(i);

            matrixSpacesFromFile[i][SPACEID] = "" + space.getId();
            matrixSpacesFromFile[i][ADAPT] = "" + space.isDisabilityAdaptation();
            matrixSpacesFromFile[i][TAKEN] = "" + space.isSpaceTaken();
            matrixSpacesFromFile[i][VEHICLETYPE] = "" + space.getVehicleTypeId();

        }

        return matrixSpacesFromFile;

    }

    public void deleteSpaceFromFile(String lineToRemove) {

        exception = 0;

        try {

            File file = new File(fileName);

            File tempFile = new File("SpacesTemp");

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
