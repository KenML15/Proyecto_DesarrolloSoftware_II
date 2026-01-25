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
    
    //Los ID's no existen para mostrarse, existen para relacionar

    public VehicleDataFile(String fileName) {

        this.fileName = fileName;
    }

    public VehicleDataFile(CustomerDataFile customerData, SpaceDataFile spaceData, VehicleTypeDataFile vehicleTypeData) {
        this.customerData = customerData;
        this.spaceData = spaceData;
        this.vehicleTypeData = vehicleTypeData;
    }
    
    public int insertVehicle(Vehicle vehicle){
        int result = -1;
        exception = 0; //limpia la excepcion
        
        //para controlar las excepciones de la vista
        try{
            File vehicleFile = new File(fileName);
            
            //lee el archivo 
            FileOutputStream fileOutputStream = new FileOutputStream(vehicleFile, true);
            
            //preparar para escribir en el archivo
            PrintStream printStream = new PrintStream(fileOutputStream);
            
            //se buscan los vehiculos por su placa, espacio asignado y por el customer para saber si existe
            boolean vehicleExist = findVehicleByPlate(vehicle.getPlate());
            
            
            String customerIdsText = "";

            ArrayList<Customer> customers = vehicle.getCustomer();

            for (int i = 0; i < customers.size(); i++) {
                customerIdsText += customers.get(i).getId();
                if (i < customers.size() - 1) {
                    customerIdsText += ",";
                }
            }
            //evaluamos si el cliente existe
            if (!vehicleExist){
                printStream.println(vehicle.getPlate() + ";"
                        + vehicle.getColor() + ";"
                        + vehicle.getBrand() + ";"
                        + vehicle.getModel() + ";"
                        + customerIdsText + ";"
                        + vehicle.getVehicleType() + ";"
                        + vehicle.getSpace() + ";"
                        + vehicle.getEntryTime() + ";");
                
                //indicador de exito
                result = 0;
            } else {
                exception = 3;//El vehículo ya existe
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

            //lee linea a linea el archivo 
            FileInputStream fileInputStream
                    = new FileInputStream(vehicleFile);

            //helper de InputStream
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            //lee cada parte de registro
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            //lee la primera tupla
            String currentTuple = bufferedReader.readLine();

            //mientras que no se haya llegado al final del archivo y no se haya encontrado al cliente
            while (currentTuple != null && !vehicleExists) {

                StringTokenizer stringTokenizer
                        = new StringTokenizer(currentTuple, ";");

                //mientras hayan más tokens (separados por ; en el archivo)
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

                //esto verifica si se encontro el cliente
                if (plate.equalsIgnoreCase(vehiclePlate) /*&& email.equalsIgnoreCase(customerEmail)*/) {
                    
                    vehicleExists = true;
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

        return vehicleExists;
    }
    
    public String getVehicleFromFile(String plate) {

        exception = 0;

        int counter = 0;
        String vehiclePlate = "";

        String currentTuple = "";

        try {

            File vehicleFile = new File(fileName);

            //lee linea a linea el archivo 
            FileInputStream fileInputStream = new FileInputStream(vehicleFile);

            //helper de InputStream
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            //lee cada parte de registro
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            //lee la primera tupla
            currentTuple = bufferedReader.readLine();

            //mientras que no se haya llegado al final del archivo...
            while (currentTuple != null) {

                StringTokenizer stringTokenizer = new StringTokenizer(currentTuple, ";");

                while (stringTokenizer.hasMoreTokens()) {

                    if (counter == PLATE) {

                        vehiclePlate = stringTokenizer.nextToken();

                    } else {

                        stringTokenizer.nextToken();

                    }

                    counter++;
                }

                //esto verifica si se encontro el cliente
                if (plate.equalsIgnoreCase(vehiclePlate) /*&& email.equalsIgnoreCase(customerEmail)*/) {

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
        return currentTuple;

    }
    
    public ArrayList<Vehicle> getAllVehicles() {

        exception = 0;

        ArrayList<Vehicle> allVehicles = new ArrayList<>();

        //ArrayList<Customer> allCustomers = new ArrayList<>();
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

            //lee linea a linea el archivo 
            FileInputStream fileInputStream = new FileInputStream(vehicleFile);

            //helper de InputStream
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            //lee cada parte de registro
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            //lee la primera tupla
            String currentTuple = bufferedReader.readLine();

            //mientras que no se haya llegado al final del archivo y no se haya encontrado al cliente
            while (currentTuple != null) {

                StringTokenizer stringTokenizer = new StringTokenizer(currentTuple, ";");

                //mientras hayan más tokens (separados por ; en el archivo)
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

        }//Fin del for con contador i
        
        return matrixVehiclesFromFile;
        
    }//Fin del método getDatosArchivo
        
    public void deleteVehicleFromFile(String lineToRemove) {

        exception = 0;

        try {

            File file = new File(fileName);

            //Construct the new file that will later be renamed to the original filename. 
            File tempFile = new File("VehiclesTemp");

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
