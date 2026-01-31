/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.entities;

/**
 *
 * @author 50687
 */
public class VehicleType {
    private int id;
    private String description;
    private int numberOfTires;
    private float fee;

    public VehicleType() {
    }

    public VehicleType(int id, String description, int numberOfTires, float fee) {
        this.id = id;
        this.description = description;
        this.numberOfTires = numberOfTires;
        this.fee = fee;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNumberOfTires() {
        return numberOfTires;
    }

    public void setNumberOfTires(int numberOfTires) {
        this.numberOfTires = numberOfTires;
    }

    public float getFee() {
        return fee;
    }

    public void setFee(float fee) {
        this.fee = fee;
    }

    @Override
    public String toString() {
        return """
           Vehicle Types
           Id = """ + id + ", Description = " + description + 
           ", Number of Tires = " + numberOfTires + ", Fee = " + fee + ".\n";
    }
    
    
}
