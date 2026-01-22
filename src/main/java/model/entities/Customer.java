/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.entities;

import java.util.Objects;

/**
 *
 * @author pablo
 */
public class Customer {
    
    private String id;
    private String name;
    private boolean disabilityPresented;

    public Customer() {
    }

    public Customer(String id, String name, boolean disabilityPresented) {
        this.id = id;
        this.name = name;
        this.disabilityPresented = disabilityPresented;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDisabilityPresented() {
        return disabilityPresented;
    }

    public void setDisabilityPresented(boolean disabilityPresented) {
        this.disabilityPresented = disabilityPresented;
    }

    @Override
    public String toString() {
        return "Customer{" + "id=" + id + ", name=" + name + ", disabilityPresented=" + disabilityPresented + '}' + "\n";
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(id, customer.id); // Compara solo por ID
}   
}

