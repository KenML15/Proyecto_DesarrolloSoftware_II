/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.data;

import java.util.ArrayList;
import model.entities.Customer;

/**
 *
 * @author 50687
 */
public class CustomerData {
    
    //LÓGICA PARA CUSTOMER
    
    ArrayList<Customer> customers = new ArrayList<>();
    
    public void insertCustomer(Customer customer) {
        
        customers.add(customer);        
    }
    
    public void removeCustomer(Customer customer) {
        
        customers.remove(customer);        
    }
    
    public ArrayList<Customer> getAllCustomers() {
        
        return customers;        
    }
    
    public Customer findCustomerById(int id) {
        
        Customer customerToReturn = null;
        
        for (Customer customer : customers) {
            if (customer.getId() == id) {
                customerToReturn=customer;
            }
        }
        return customerToReturn;
    }
}
