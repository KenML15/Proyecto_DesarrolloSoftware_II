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
    private ArrayList<Customer> customer;
    private VehicleType vehicleType;
    private Space space;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private float feeToPay;
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
        this.exitTime = null;
        this.feeToPay = 0.0f;
    }

    public Vehicle(String plate, String color, String brand, String model, ArrayList<Customer> customer, VehicleType vehicleType, Space space, LocalDateTime entryTime) {
        //this.entryTime = LocalDateTime.now();
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

    public ArrayList<Customer> getCustomer() {
        return customer;
    }

    public void setCustomer(ArrayList<Customer> customer) {
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

    //Métodos auxiliares para compatibilidad de ID's
    public int getVehicleTypeId() {
        return vehicleType != null ? vehicleType.getId() : 0;
    }

    public void setVehicleTypeId(int id) {
        if (vehicleType == null) {
            vehicleType = new VehicleType();
        }
        vehicleType.setId(id);
    }

    public int getSpaceId() {
        return space != null ? space.getId() : 0;
    }

    public void setSpaceId(int id) {
        if (space == null) {
            space = new Space();
        }
        space.setId(id);
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public void setExitTime(LocalDateTime exitTime) {
        this.exitTime = exitTime;
    }

    public float getFeeToPay() {
        return feeToPay;
    }

    public void setFeeToPay(float feeToPay) {
        this.feeToPay = feeToPay;
    }

    @Override
    public String toString() {
        return """
           Vehicles
           Plate = """ + plate + ", Color = " + color + ", Brand = " + brand
                + ", Model = " + model + ", Customer = " + customer
                + ", Vehicle Type = " + vehicleType + ", Space = " + space
                + ", Entry Time = " + entryTime + ".\n";
    }
}
