/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.io.IOException;
import java.util.ArrayList;
import model.data.CustomerDataFile;
import model.entities.Customer;

/**
 *
 * @author 50687
 */
public class CustomerFileController {

    private final CustomerDataFile dataFile;

    //Constructor por defecto
    public CustomerFileController() throws IOException {
        this.dataFile = new CustomerDataFile();
    }
    
    //Constructor sobrecargado
    public CustomerFileController(String fileName) throws IOException {
        this.dataFile = new CustomerDataFile(fileName);
    }

    public void createCustomer(Customer customer) throws IOException, IllegalArgumentException {
        if (customer == null){
            throw new IllegalArgumentException("Cliente requerido");
        }
        customer.setId(dataFile.getCustomerNextId());
        dataFile.insertCustomer(customer);
    }

    public void updateCustomer(Customer customer) throws IOException, IllegalArgumentException {
        
        if (customer == null) {
            throw new IllegalArgumentException("Cliente requerido");
        }
        
        Customer existing = dataFile.getCustomerById(customer.getId());
        if (existing == null) {
            throw new IllegalArgumentException("Cliente no existe");
        }
        dataFile.updateCustomer(customer);
    }

    public void deleteCustomer(int id) throws IOException, IllegalArgumentException {
        if (dataFile.getCustomerById(id) == null) {
            throw new IllegalArgumentException("Cliente no existe");
        }
        dataFile.deleteCustomer(id);
    }

    public ArrayList<Customer> getAllCustomers() throws IOException {
        return dataFile.getAllCustomers();
    }

    public Customer getCustomerById(int id) throws IOException {
        return dataFile.getCustomerById(id);
    }

    public Customer getCustomerByEmail(String email) throws IOException {
        return dataFile.getCustomerByEmail(email);
    }

    public ArrayList<Customer> searchCustomers(String term) throws IOException {
        
        if (term == null || term.trim().isEmpty()) {
            return getAllCustomers();
        }
        
        ArrayList<Customer> results = new ArrayList<>();
        for (Customer customer : getAllCustomers()) {
            if (matchesSearch(customer, term)) {
                results.add(customer);
            }
        }
        return results;
    }

    private boolean matchesSearch(Customer customer, String term) {
        String lowerTerm = term.toLowerCase();
        return (customer.getName() != null && customer.getName().toLowerCase().contains(lowerTerm))
                || (customer.getEmail() != null && customer.getEmail().toLowerCase().contains(lowerTerm))
                || (customer.getPhoneNumber() != null && customer.getPhoneNumber().contains(term));
    }
}
