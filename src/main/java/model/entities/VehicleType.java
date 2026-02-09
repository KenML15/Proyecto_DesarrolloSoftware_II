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
    private Fee fee;

    public VehicleType() {
    }

    public VehicleType(int id, String description, int numberOfTires) {
        this.id = id;
        this.description = description;
        this.numberOfTires = numberOfTires;
        this.fee = null;//Es nula porque el administrador es el que debe configurar los precios después
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

    public Fee getFee() {
        return fee;
    }

    public void setFee(Fee fee) {
        this.fee = fee;
    }
    
    public double calculateParkingFee(long minutes) {
        if (fee == null) {
            throw new IllegalStateException("No hay tarifa configurada para este tipo de vehículo");
        }
        return fee.calculateFeeForDuration(minutes);
    }

    @Override
    public String toString() {
        return "VehicleType{" + "id=" + id + ", description=" + description + 
               ", numberOfTires=" + numberOfTires + ", fee=" + (fee != null ? "Configurada" : "No configurada") + '}';
    }
    
    
}
