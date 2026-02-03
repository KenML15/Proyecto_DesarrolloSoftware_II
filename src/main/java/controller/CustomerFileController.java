/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.util.ArrayList;
import model.data.CustomerDataFile;
import model.entities.Customer;

/**
 *
 * @author 50687
 */
public class CustomerFileController {
    CustomerDataFile customerDataFile = new CustomerDataFile();
    
    public int insert(Customer customer) {
        return customerDataFile.insert(customer);
    }
    
    public void modifyCustomerFromFile(String lineToModify, String newList) {
        customerDataFile.modifyCustomerFromFile(lineToModify, newList);
    }
    
    public Customer getCustomerFromFile(int customerId) {
        return customerDataFile.getCustomerFromFile(customerId);
    }
    
    public boolean find(String name, String email) {
        return customerDataFile.find(name, email);
    }
    
    public int findLastIdNumberOfCustomer() {
        return customerDataFile.findLastIdNumberOfCustomer();
    }
    
    public ArrayList<Customer> getAllCustomers() {
        return customerDataFile.getAllCustomers();
    }

    public String[][] createCustomerMatrix(ArrayList<Customer> customers) {
        return customerDataFile.createCustomerMatrix(customers);
    }
    
    public void deleteCustomerFromFile(String lineToRemove) {
        customerDataFile.deleteCustomerFromFile(lineToRemove);
    }
}
    
