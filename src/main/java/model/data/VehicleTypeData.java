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
class VehicleTypeData {

    public int exception = 0;
    String fileName;
    final int ID = 0, DESCRIPTION = 1, TIRES = 2, FEE = 3;

    public VehicleTypeData(String fileName) {

        this.fileName = fileName;

    }

    public int insert(VehicleType vehicleType) {

        int result = -1;
        exception = 0; //limpia la excepcion

        //control de excepciones
        try {

            File vehicleTypeFile = new File(fileName);

            //lee el archivo 
            FileOutputStream fileOutputStream
                    = new FileOutputStream(vehicleTypeFile, true);

            //preparar para escribir en el archivo
            PrintStream printStream
                    = new PrintStream(fileOutputStream);

            //buscamos al cliente por su nombre y por su email por si ya existe en el archivo
            boolean vehicleTypeExists
                    = find(vehicleType.getId());

            //evaluamos si el cliente existe
            if (!vehicleTypeExists) {

                printStream.println(vehicleType.getId() + ";"
                        + vehicleType.getDescription() + ";"
                        + vehicleType.getNumberOfTires() + ";"
                        + vehicleType.getFee() + ";");

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

    public void modifyVehicleTypeFromFile(String lineToModify, String newList) {

        exception = 0;

        try {

            File file = new File(fileName);

            //Construct the new file that will later be renamed to the original filename. 
            File tempFile = new File("VehicleTypeTemp");

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

                    printWriter.println(newList);
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

    //TODO
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

            //lee linea a linea el archivo 
            FileInputStream fileInputStream
                    = new FileInputStream(vehicleTypeFile);

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

                //esto verifica si se encontro el cliente
                if (id == vehicleTypeId) {
                    vehicleType = new VehicleType(vehicleTypeId, description, numberOfTires, fee);
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
        return vehicleType;

    }
// find = buscar 

    public boolean find(int vehicleTypeId) {

        exception = 0;
        boolean vehicleTypeExists = false;
        
        int id = 0;
        
        int counter = 0;

        try {

            File vehicleTypeFile = new File(fileName);

            //lee linea a linea el archivo 
            FileInputStream fileInputStream
                    = new FileInputStream(vehicleTypeFile);

            //helper de InputStream
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            //lee cada parte de registro
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            //lee la primera tupla
            String currentTuple = bufferedReader.readLine();

            //mientras que no se haya llegado al final del archivo y no se haya encontrado al cliente
            while (currentTuple != null && !vehicleTypeExists) {

                StringTokenizer stringTokenizer
                        = new StringTokenizer(currentTuple, ";");

                //mientras hayan más tokens (separados por ; en el archivo)
                while (stringTokenizer.hasMoreTokens()) {

                    if (counter == ID) {

                        id = Integer.parseInt(stringTokenizer.nextToken());

                    }

                    counter++;
                }

                //esto verifica si se encontro el cliente
                if (vehicleTypeId == id) {

                    vehicleTypeExists = true;
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

        return vehicleTypeExists;

    }

    /*Este método encuentra el último id del cliente ingresado
     para que el próximo cliente tenga un id un número mayor que el encontrado.*/
    public int findLastIdNumberOfVehicleType() {

        exception = 0;

        int counter = 0;
        int idVehicleType = 0;

        try {

            File vehicleTypeFile = new File(fileName);

            //lee linea a linea el archivo 
            FileInputStream fileInputStream
                    = new FileInputStream(vehicleTypeFile);

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

                    if (counter == ID) {

                        idVehicleType = Integer.parseInt(stringTokenizer.nextToken());

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

            //lee linea a linea el archivo 
            FileInputStream fileInputStream
                    = new FileInputStream(vehicleTypeFile);

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

                //limpiamos la variable counter
                counter = 0;

            }

            bufferedReader.close();
            fileInputStream.close();
            inputStreamReader.close();

        }//Fin del try//Fin del try//Fin del try//Fin del try//Fin del try//Fin del try//Fin del try//Fin del try
        catch (IOException ioE) {
            exception = 2;

        }//Fin del catch

        return allVehicleTypes;

    }//Fin del método getAllCustomers

    public String[][] createVehicleTypeMatrix(ArrayList<VehicleType> vehicleTypes) {

        String[][] matrixVehicleTypesFromFile = new String[vehicleTypes.size()][4];

        for (int i = 0; i < vehicleTypes.size(); i++) {

            VehicleType vehicleType = vehicleTypes.get(i);

            matrixVehicleTypesFromFile[i][ID] = "" + vehicleType.getId();
            matrixVehicleTypesFromFile[i][DESCRIPTION] = vehicleType.getDescription();
            matrixVehicleTypesFromFile[i][TIRES] = "" + vehicleType.getNumberOfTires();
            matrixVehicleTypesFromFile[i][FEE] = "" + vehicleType.getFee();

        }//Fin del for con contador i

        return matrixVehicleTypesFromFile;

    }//Fin del método getDatosArchivo

    public void deleteVehicleTypeFromFile(String lineToRemove) {

        exception = 0;

        try {

            File file = new File(fileName);

            //Construct the new file that will later be renamed to the original filename. 
            File tempFile = new File("VehicleTypeTemp");

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
