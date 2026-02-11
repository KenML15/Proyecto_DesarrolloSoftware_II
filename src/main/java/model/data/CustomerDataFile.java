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
import model.entities.Customer;
import java.util.ArrayList;

/**
 *
 * @author pablo
 */
public class CustomerDataFile {

    private final String fileName;
    private static final String DELIMITER = ";";

    public CustomerDataFile(String fileName) throws IOException {
        this.fileName = fileName;
        ensureFileExists();
    }

    public CustomerDataFile() throws IOException {
        this("Customers.txt");
    }

    private void ensureFileExists() throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
    }

    public void insertCustomer(Customer customer) throws IOException {
        validateCustomer(customer);
        checkDuplicate(customer);
        appendToFile(customer);
    }

    //TODO: Revisar esto ===================================
    private void validateCustomer(Customer customer) throws IOException, NullPointerException{
        if (isNullOrEmpty(customer.getName())) {
            throw new IllegalArgumentException("Nombre requerido");
        }
        if (!isValidEmail(customer.getEmail())) {
            throw new IllegalArgumentException("Email inválido");
        }
    }

    private boolean isNullOrEmpty(String str) throws IOException{
        return str == null || str.trim().isEmpty();
    }

    private boolean isValidEmail(String email) throws IOException{
        return email != null && email.contains("@");
    }
    //======================================================

    private void checkDuplicate(Customer customer) throws IOException {
        if (existsByEmail(customer.getEmail())) {
            throw new IllegalArgumentException("Email ya existe");
        }
    }

    private boolean existsByEmail(String email) throws IOException {
        return getCustomerByEmail(email) != null;
    }

    private void appendToFile(Customer customer) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName, true))) {
            writer.println(formatCustomer(customer));
        }
    }

    private String formatCustomer(Customer customer) throws IOException{
        return String.join(DELIMITER,
                String.valueOf(customer.getId()),
                customer.getName(),
                String.valueOf(customer.isDisabilityPresented()),
                customer.getEmail(),
                customer.getAddress(),
                customer.getPhoneNumber()
        );
    }

    public void updateCustomer(Customer customer) throws IOException {
        validateCustomer(customer);
        File file = new File(fileName);
        File temp = new File("temp.txt");
        replaceCustomerInFile(customer, file, temp);
        replaceCustomerFile(file, temp);
    }

    private void replaceCustomerInFile(Customer customer, File source, File target) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(source)); PrintWriter writer = new PrintWriter(new FileWriter(target))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (isSameCustomer(line, customer)) {
                    writer.println(formatCustomer(customer));
                } else {
                    writer.println(line);
                }
            }
        }
    }

    private boolean isSameCustomer(String line, Customer customer) throws IOException{
        return line.startsWith(customer.getId() + DELIMITER);
    }

    private void replaceCustomerFile(File original, File temp) throws IOException {
        if (!original.delete()) {
            throw new IOException("No se pudo borrar archivo original");
        }
        if (!temp.renameTo(original)) {
            throw new IOException("No se pudo renombrar archivo temporal");
        }
    }

    public void deleteCustomer(int id) throws IOException {
        File file = new File(fileName);
        File temp = new File("temp.txt");
        deleteCustomerFromFile(id, file, temp);
        replaceCustomerFile(file, temp);
    }

    private void deleteCustomerFromFile(int id, File source, File target) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(source)); PrintWriter writer = new PrintWriter(new FileWriter(target))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith(id + DELIMITER)) {
                    writer.println(line);
                }
            }
        }
    }

    public ArrayList<Customer> getAllCustomers() throws IOException {
        ArrayList<Customer> customers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Customer customer = parseCustomer(line);
                if (customer != null) {
                    customers.add(customer);
                }
            }
        }
        return customers;
    }

    private Customer parseCustomer(String line) throws IOException, NullPointerException{
        String[] parts = line.split(DELIMITER, -1);
        if (parts.length != 6) {
            return null;
        }

        return new Customer(
                Integer.parseInt(parts[0]),
                parts[1],
                Boolean.parseBoolean(parts[2]),
                parts[3],
                parts[4],
                parts[5]
        );
    }

    public Customer getCustomerById(int id) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Customer customer = parseCustomer(line);
                if (customer != null && customer.getId() == id) {
                    return customer;
                }
            }
        }
        return null;
    }

    public Customer getCustomerByEmail(String email) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Customer customer = parseCustomer(line);
                if (customer != null && customer.getEmail().equalsIgnoreCase(email)) {
                    return customer;
                }
            }
        }
        return null;
    }

    public int getCustomerNextId() throws IOException {
        int maxId = 0;
        for (Customer customer : getAllCustomers()) {
            if (customer.getId() > maxId) {
                maxId = customer.getId();
            }
        }
        return maxId + 1;
    }
}
