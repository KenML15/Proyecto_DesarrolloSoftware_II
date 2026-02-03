/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import model.data.CustomerData;
import model.entities.Customer;
import java.util.ArrayList;

/**
 *
 * @author pablo
 */
public class CustomerController {
    
    CustomerData customerData = new CustomerData();
    
    public String insertCustomer(Customer customer) {
        String result;
    
        if (customerData.findCustomerById(customer.getId()) == null ) {
            customerData.insertCustomer(customer);
            result = "The client has been added to the system";
        } else {
            result = "The client could not be added to the system because a client already exists in the system.";
        }
        return result;
    }
    
    public void removeCustomer(Customer customer) {
        
        customerData.removeCustomer(customer);
    }
    
    public ArrayList<Customer> getAllCustomers() {
        
        return customerData.getAllCustomers();
    }
    
    public Customer findCustomerById(int id) {
        
        return customerData.findCustomerById(id);
    }

}
