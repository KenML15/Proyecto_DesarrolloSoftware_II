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
import model.entities.Customer;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 *
 * @author pablo
 */
public class CustomerDataFile {
    
    
    
    public int exception = 0;
   String fileName;
    final int ID = 0, NAME = 1, EMAIL = 2, ADDRESS = 3, PHONE = 4;

    public CustomerDataFile(String fileName) {

        this.fileName = fileName;

    }

    public int insert(Customer customer) {

        int result = -1;
        exception = 0; //limpia la excepcion

        //control de excepciones
        try {

            File customerFile = new File(fileName);

            //lee el archivo 
            FileOutputStream fileOutputStream
                    = new FileOutputStream(customerFile, true);

            //preparar para escribir en el archivo
            PrintStream printStream
                    = new PrintStream(fileOutputStream);

            //buscamos al cliente por su nombre y por su email por si ya existe en el archivo
            boolean customerExists = find(customer.getName(), customer.getEmail());

            //evaluamos si el cliente existe
            if (!customerExists) {

                printStream.println(customer.getId() + ";"
                        + customer.getName() + ";"
                        + customer.getEmail() + ";"
                        + customer.getAddress() + ";"
                        + customer.getPhoneNumber());

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
    
    public void modifyCustomerFromFile(String lineToModify, String newList) {

       exception = 0;

        try {

            File file = new File(fileName);

            //Construct the new file that will later be renamed to the original filename. 
            File tempFile = new File("CustomersTemp");

            BufferedReader bufferReader = new BufferedReader(new FileReader(fileName));
            PrintWriter printWriter = new PrintWriter(new FileWriter(tempFile));

            String line = null;

            //Read from the original file and write to the new 
            //unless content matches data to be removed.
            while ((line = bufferReader.readLine()) != null) {

                if (!line.trim().equals(lineToModify)) {

                    printWriter.println(line);
                    printWriter.flush();
                }else{

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


    public Customer getCustomerFromFile(int customerId) {

        exception = 0;
        
        String customerName = "",
                customerEmail = "",
                phone = "",
                address = "";

        int id = 0;
        int counter = 0;
        
        Customer customer = null;
        String currentTuple = "";

        try {

            File customerFile = new File(fileName);

            //lee linea a linea el archivo 
            FileInputStream fileInputStream = new FileInputStream(customerFile);

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

                    if (counter == ID) {

                        id = Integer.parseInt(stringTokenizer.nextToken());

                    }else if (counter == NAME) {

                        customerName = stringTokenizer.nextToken();

                    }else if (counter == EMAIL) {

                        customerEmail = stringTokenizer.nextToken();

                    }else if (counter == ADDRESS) {

                        address = stringTokenizer.nextToken();

                    }else if (counter == PHONE) {

                        phone = stringTokenizer.nextToken();

                    }else {

                        stringTokenizer.nextToken();

                    }

                    counter++;
                }

                //esto verifica si se encontro el cliente
                if (customerId == id) {
                    customer = new Customer(id, customerName, customerEmail, address, phone);
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
        return customer;
    }
    
// find = buscar 
    public boolean find(String name, String email) {

        exception = 0;
        boolean customerExists = false;
        String customerName = "",
                customerEmail = "",
                phone = "",
                address = "";

        int id = 0;
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
            while (currentTuple != null && !customerExists) {

                StringTokenizer stringTokenizer
                        = new StringTokenizer(currentTuple, ";");

                //mientras hayan más tokens (separados por ; en el archivo)
                while (stringTokenizer.hasMoreTokens()) {

                    if (counter == ID) {

                        id = Integer.parseInt(stringTokenizer.nextToken());

                    }
                    if (counter == NAME) {

                        customerName = stringTokenizer.nextToken();

                    }
                    if (counter == EMAIL) {

                        customerEmail = stringTokenizer.nextToken();

                    }
                    if (counter == ADDRESS) {

                        address = stringTokenizer.nextToken();

                    }
                    if (counter == PHONE) {

                        phone = stringTokenizer.nextToken();

                    }
                    counter++;
                }

                //esto verifica si se encontro el cliente
                if (name.equalsIgnoreCase(customerName)
                        && email.equalsIgnoreCase(customerEmail)) {

                    customerExists = true;
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

        return customerExists;

    }

    /*Este método encuentra el último id del cliente ingresado
     para que el próximo cliente tenga un id un número mayor que el encontrado.*/
    public int findLastIdNumberOfCustomer() {

        exception = 0;

        int counter = 0;
        int idCustomer = 0;

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

            //mientras que no se haya llegado al final del archivo...
            while (currentTuple != null) {

                StringTokenizer stringTokenizer
                        = new StringTokenizer(currentTuple, ";");

                while (stringTokenizer.hasMoreTokens()) {

                    if (counter == ID) {

                        idCustomer = Integer.parseInt(stringTokenizer.nextToken());

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
        return idCustomer;

    }

    public ArrayList<Customer> getAllCustomers() {

        exception = 0;
        ArrayList<Customer> allCustomers = new ArrayList<>();
        String name = "",
                email = "",
                phone = "",
                address = "";

        int id = 0;
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

                    if (counter == ID) {

                        id = Integer.parseInt(stringTokenizer.nextToken());

                    }
                    if (counter == NAME) {

                        name = stringTokenizer.nextToken();

                    }
                    if (counter == EMAIL) {

                        email = stringTokenizer.nextToken();

                    }
                    if (counter == ADDRESS) {

                        address = stringTokenizer.nextToken();

                    }
                    if (counter == PHONE) {

                        phone = stringTokenizer.nextToken();

                    }
                    counter++;
                }

                Customer customer = new Customer(id, name, phone, address, email);
                allCustomers.add(customer);
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



        return allCustomers;

    }//Fin del método getAllCustomers

    public String[][] createVehicleTypeMatrix(ArrayList<Customer> customers) {

        String[][] matrixClientsFromFile
                = new String[customers.size()][5];

        for (int i = 0; i < customers.size(); i++) {

            Customer customer = customers.get(i);

            matrixClientsFromFile[i][ID] = "" + customer.getId();
            matrixClientsFromFile[i][NAME] = customer.getName();
            matrixClientsFromFile[i][EMAIL] = customer.getEmail();
            matrixClientsFromFile[i][ADDRESS] = customer.getAddress();
            matrixClientsFromFile[i][PHONE] = customer.getPhoneNumber();

        }//Fin del for con contador i
        
        return matrixClientsFromFile;
        
    }//Fin del método getDatosArchivo

    public void deleteVehicleTypeFromFile(String lineToRemove) {

        exception = 0;

        try {

            File file = new File(fileName);

            //Construct the new file that will later be renamed to the original filename. 
            File tempFile = new File("CustomersTemp");

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
