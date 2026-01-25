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
        exception = 0; //limpia la excepcion

        //control de excepciones
        try {

            File spaceFile = new File(fileName);

            //lee el archivo 
            FileOutputStream fileOutputStream
                    = new FileOutputStream(spaceFile, true);

            //preparar para escribir en el archivo
            PrintStream printStream
                    = new PrintStream(fileOutputStream);

            //buscamos al cliente por su nombre y por su email por si ya existe en el archivo
            boolean spaceExists = find(space.getId() /*space.getEmail()*/);

            //evaluamos si el cliente existe
            if (!spaceExists) {

                printStream.println(space.getId() + ";"
                        + space.isDisabilityAdaptation() + ";"
                        + space.isSpaceTaken() + ";"
                        + space.getVehicleTypeId() + ";");

                //indicador de exito
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

            //Construct the new file that will later be renamed to the original filename. 
            File tempFile = new File("SpacesTemp");

            BufferedReader bufferReader = new BufferedReader(new FileReader(fileName));
            PrintWriter printWriter = new PrintWriter(new FileWriter(tempFile));

            String line = null;

            //Read from the original file and write to the new 
            //unless content matches data to be removed.
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

            //Delete the original file
            if (!file.delete()) {

                //no se pudo eliminar el archivo
                exception = 4;
            }

            //Rename the new file to the filename the original file had.
            if (!tempFile.renameTo(file)) {

                //no se pudo renombrar el archivo
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

            //lee linea a linea el archivo 
            FileInputStream fileInputStream
                    = new FileInputStream(spaceFile);

            //helper de InputStream
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            //lee cada parte de registro
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            //lee la primera tupla
            currentTuple = bufferedReader.readLine();

            //mientras que no se haya llegado al final del archivo...
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

                //esto verifica si se encontro el cliente
                if (spaceId == spaceNumber) {
                    space = new Space(spaceNumber, disabilityAdaptation, spaceTaken, vehicleTypeId);
                    break; //terminamos el ciclo para que NO lea el resto de los tokens como nombre, correo, etc. Eso no nos interesa.

                }

                //leemos la siguiente tupla (fila) del archivo.
                currentTuple = bufferedReader.readLine();

                //limpiamos la variable counter
                counter = 0;

            }

            bufferedReader.close();
            fileInputStream.close();
            inputStreamReader.close();

            //no encontró el archivo
        } catch (FileNotFoundException fileException) {

            exception = 1;

            //no pudo leer el archivo
        } catch (IOException ioException) {

            exception = 2;
        }

        //se retorna el id del último cliente 
        return space;

    }
// find = buscar 

    public boolean find(int spaceId) {

        exception = 0;
        boolean spaceExists = false;

        int id = 0;
        int counter = 0;

        try {

            File spaceFile = new File(fileName);

            //lee linea a linea el archivo 
            FileInputStream fileInputStream
                    = new FileInputStream(spaceFile);

            //helper de InputStream
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            //lee cada parte de registro
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            //lee la primera tupla
            String currentTuple = bufferedReader.readLine();

            //mientras que no se haya llegado al final del archivo y no se haya encontrado al cliente
            while (currentTuple != null && !spaceExists) {

                StringTokenizer stringTokenizer
                        = new StringTokenizer(currentTuple, ";");

                //mientras hayan más tokens (separados por ; en el archivo)
                while (stringTokenizer.hasMoreTokens()) {

                    if (counter == SPACEID) {

                        id = Integer.parseInt(stringTokenizer.nextToken());

                    }
                    
                    counter++;
                }

                //esto verifica si se encontro el cliente
                if (spaceId == id) {

                    spaceExists = true;
                } else {

                    //leemos la siguiente tupla (fila) del archivo.
                    currentTuple = bufferedReader.readLine();
                }
                //limpiamos la variable counter
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

    /*Este método encuentra el último id del cliente ingresado
     para que el próximo cliente tenga un id un número mayor que el encontrado.*/
    public int findLastIdNumberOfSpace() {

        exception = 0;

        int counter = 0;
        int idSpace = 0;

        try {

            File spaceFile = new File(fileName);

            //lee linea a linea el archivo 
            FileInputStream fileInputStream
                    = new FileInputStream(spaceFile);

            //helper de InputStream
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            //lee cada parte de registro
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            //lee la primera tupla
            String currentTuple = bufferedReader.readLine();

            //mientras que no se haya llegado al final del archivo...
            while (currentTuple != null) {

                StringTokenizer stringTokenizer
                        = new StringTokenizer(currentTuple, ";");

                while (stringTokenizer.hasMoreTokens()) {

                    if (counter == SPACEID) {

                        idSpace = Integer.parseInt(stringTokenizer.nextToken());

                        break; //terminamos el ciclo para que NO lea el resto de los tokens como nombre, correo, etc. Eso no nos interesa.
                    }

                    counter++;
                }

                //leemos la siguiente tupla (fila) del archivo.
                currentTuple = bufferedReader.readLine();

                //limpiamos la variable counter
                counter = 0;

            }

            bufferedReader.close();
            fileInputStream.close();
            inputStreamReader.close();

            //no encontró el archivo
        } catch (FileNotFoundException fileException) {

            exception = 1;

            //no pudo leer el archivo
        } catch (IOException ioException) {

            exception = 2;
        }

        //se retorna el id del último cliente 
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

            //lee linea a linea el archivo 
            FileInputStream fileInputStream
                    = new FileInputStream(customerFile);

            //helper de InputStream
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            //lee cada parte de registro
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            //lee la primera tupla
            String currentTuple = bufferedReader.readLine();

            //mientras que no se haya llegado al final del archivo y no se haya encontrado al cliente
            while (currentTuple != null) {

                StringTokenizer stringTokenizer
                        = new StringTokenizer(currentTuple, ";");

                //mientras hayan más tokens (separados por ; en el archivo)
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

                //limpiamos la variable counter
                counter = 0;

            }

            bufferedReader.close();
            fileInputStream.close();
            inputStreamReader.close();

        }//Fin del try//Fin del try
        catch (IOException ioE) {
            exception = 2;

        }//Fin del catch

        return allSpaces;

    }//Fin del método getAllCustomers

    public String[][] createSpaceMatrix(ArrayList<Space> spaces) {

        String[][] matrixSpacesFromFile
                = new String[spaces.size()][4];

        for (int i = 0; i < spaces.size(); i++) {

            Space space = spaces.get(i);

            matrixSpacesFromFile[i][SPACEID] = "" + space.getId();
            matrixSpacesFromFile[i][ADAPT] = "" + space.isDisabilityAdaptation();
            matrixSpacesFromFile[i][TAKEN] = "" + space.isSpaceTaken();
            matrixSpacesFromFile[i][VEHICLETYPE] = "" + space.getVehicleTypeId();

        }//Fin del for con contador i

        return matrixSpacesFromFile;

    }//Fin del método getDatosArchivo

    public void deleteSpaceFromFile(String lineToRemove) {

        exception = 0;

        try {

            File file = new File(fileName);

            //Construct the new file that will later be renamed to the original filename. 
            File tempFile = new File("SpacesTemp");

            BufferedReader bufferReader = new BufferedReader(new FileReader(fileName));
            PrintWriter printWriter = new PrintWriter(new FileWriter(tempFile));

            String line = null;

            //Read from the original file and write to the new 
            //unless content matches data to be removed.
            while ((line = bufferReader.readLine()) != null) {

                if (!line.trim().equals(lineToRemove)) {

                    printWriter.println(line);
                    printWriter.flush();
                }
            }

            bufferReader.close();
            printWriter.close();

            //Delete the original file
            if (!file.delete()) {

                //no se pudo eliminar el archivo
                exception = 4;
            }

            //Rename the new file to the filename the original file had.
            if (!tempFile.renameTo(file)) {

                //no se pudo renombrar el archivo
                exception = 5;

            }

        } catch (FileNotFoundException ex) {

            exception = 1;

        } catch (IOException ex) {

            exception = 2;
        }
    }
}
