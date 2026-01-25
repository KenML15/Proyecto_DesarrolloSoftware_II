/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 *
 * @author 50687
 */
public class Vehicle {
    private int id;
    private String plate;
    private String color;
    private String brand;
    private String model;
    private ArrayList <Customer> customer;
    private VehicleType vehicleType;
    private Space space;
    private LocalDateTime entryTime;
    //Atributos para los archivos
    private int customerId;
    

    public Vehicle() {
    }

    public Vehicle(int id, String plate, String color, String brand, String model, ArrayList<Customer> customer, VehicleType vehicleType, Space space, LocalDateTime entryTime) {
        this.id = id;
        this.plate = plate;
        this.color = color;
        this.brand = brand;
        this.model = model;
        this.customer = customer;
        this.vehicleType = vehicleType;
        this.space = space;
        this.entryTime = entryTime;
    }

    

    

    public Vehicle(String plate, String color, String brand, String model, ArrayList <Customer> customer, VehicleType vehicleType, Space space, LocalDateTime entryTime) {
        this.entryTime = LocalDateTime.now();
        this.plate = plate;
        this.color = color;
        this.brand = brand;
        this.model = model;
        this.customer = customer;
        this.vehicleType = vehicleType;
        this.space = space;
        this.entryTime = entryTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public ArrayList <Customer> getCustomer() {
        return customer;
    }

    public void setCustomer(ArrayList <Customer> customer) {
        this.customer = customer;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Space getSpace() {
        return space;
    }

    public void setSpace(Space space) {
        this.space = space;
    }
    
    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(LocalDateTime entryTime) {
        this.entryTime = entryTime;
    }

    @Override
    public String toString() {
        return "Vehicle{" + "plate=" + plate + ", color=" + color + ", brand=" + brand + ", model=" + model + ", customer=" + customer + ", vehicleType=" + vehicleType + ", space=" + space + ", entryTime=" + entryTime + '}';
    }   
}
