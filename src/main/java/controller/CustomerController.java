/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import model.data.CustomerData;
import model.entities.Customer;
import java.util.ArrayList;
import model.entities.Vehicle;

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
            result = "El cliente ha sido agregado al sistema";
        } else {
            result = "El cliente no se ha podido agregar al sistema"
                    + ", porque ya existe un cliente en el sistema";
        }
        return result;
    }
    
    public void removeCustomer(Customer customer) {
        customerData.removeCustomer(customer);
    }
    
    
    //Metodo de Pablo
    /*public String removeCustomer(String id) {

        String result;

        Customer found = customerData.findCustomerById(id);

        if (found != null) {
            customerData.removeCustomer(found);
            result = "El cliente ha sido removido de forma correcta";
        } else {
            result = "El cliente no existe en el sistema";
        }
        return result;
    }*/
    
    public ArrayList<Customer> getAllCustomers() {
        return customerData.getAllCustomers();
    }
    
    public Customer findCustomerById(String id) {
        return customerData.findCustomerById(id);
    }

}
