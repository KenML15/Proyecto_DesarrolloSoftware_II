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

    public CustomerDataFile() {
    }

    public int insert(Customer customer) {

        int result = -1;
        exception = 0;

        try {

            File customerFile = new File(fileName);

            FileOutputStream fileOutputStream = new FileOutputStream(customerFile, true);

            PrintStream printStream = new PrintStream(fileOutputStream);

            boolean customerExists = find(customer.getName(), customer.getEmail());

            if (!customerExists) {

                printStream.println(customer.getId() + ";"
                        + customer.getName() + ";"
                        + customer.getEmail() + ";"
                        + customer.getAddress() + ";"
                        + customer.getPhoneNumber());

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

            File tempFile = new File("CustomersTemp");

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

            FileInputStream fileInputStream = new FileInputStream(customerFile);

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            currentTuple = bufferedReader.readLine();

            while (currentTuple != null) {

                StringTokenizer stringTokenizer
                        = new StringTokenizer(currentTuple, ";");

                while (stringTokenizer.hasMoreTokens()) {

                    if (counter == ID) {

                        id = Integer.parseInt(stringTokenizer.nextToken());

                    } else if (counter == NAME) {

                        customerName = stringTokenizer.nextToken();

                    } else if (counter == EMAIL) {

                        customerEmail = stringTokenizer.nextToken();

                    } else if (counter == ADDRESS) {

                        address = stringTokenizer.nextToken();

                    } else if (counter == PHONE) {

                        phone = stringTokenizer.nextToken();

                    } else {

                        stringTokenizer.nextToken();
                    }
                    
                    counter++;
                }

                if (customerId == id) {
                    customer = new Customer(id, customerName, customerEmail, address, phone);
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

        return customer;
    }
    
    /*public Customer buildCustomerFromLine(String customerFromFile) {
        if (customerFromFile == null || customerFromFile.trim().isEmpty()) {
            return null;
        }

        StringTokenizer stringTokenizer = new StringTokenizer(customerFromFile, ";");

        // Orden: ID=0, NAME=1, EMAIL=2, ADDRESS=3, PHONE=4
        int id = Integer.parseInt(stringTokenizer.nextToken());
        String name = stringTokenizer.nextToken();
        boolean disability = Boolean.parseBoolean(stringTokenizer.nextToken());
        String email = stringTokenizer.nextToken();
        String address = stringTokenizer.nextToken();
        String phone = stringTokenizer.nextToken();

        Customer customer = new Customer();
        customer.setId(id);
        customer.setName(name);
        customer.setDisabilityPresented(disability);
        customer.setEmail(email);
        customer.setAddress(address);
        customer.setPhoneNumber(phone);

        return customer;
    }*/

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
 
            FileInputStream fileInputStream
                    = new FileInputStream(customerFile);

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String currentTuple = bufferedReader.readLine();

            while (currentTuple != null && !customerExists) {

                StringTokenizer stringTokenizer
                        = new StringTokenizer(currentTuple, ";");

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

                if (name.equalsIgnoreCase(customerName)
                        && email.equalsIgnoreCase(customerEmail)) {

                    customerExists = true;
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

        return customerExists;
    }

    public int findLastIdNumberOfCustomer() {

        exception = 0;

        int counter = 0;
        int idCustomer = 0;

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

                    if (counter == ID) {

                        idCustomer = Integer.parseInt(stringTokenizer.nextToken());

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
 
            FileInputStream fileInputStream
                    = new FileInputStream(customerFile);

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

                counter = 0;
            }

            bufferedReader.close();
            fileInputStream.close();
            inputStreamReader.close();

        }
        catch (IOException ioE) {
            exception = 2;
        }

        return allCustomers;
    }

    public String[][] createCustomerMatrix(ArrayList<Customer> customers) {

        String[][] matrixClientsFromFile
                = new String[customers.size()][5];

        for (int i = 0; i < customers.size(); i++) {

            Customer customer = customers.get(i);

            matrixClientsFromFile[i][ID] = "" + customer.getId();
            matrixClientsFromFile[i][NAME] = customer.getName();
            matrixClientsFromFile[i][EMAIL] = customer.getEmail();
            matrixClientsFromFile[i][ADDRESS] = customer.getAddress();
            matrixClientsFromFile[i][PHONE] = customer.getPhoneNumber();

        }

        return matrixClientsFromFile;
    }

    public void deleteCustomerFromFile(String lineToRemove) {

        exception = 0;

        try {

            File file = new File(fileName);

            File tempFile = new File("CustomersTemp");

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
