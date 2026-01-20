/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.entity;

/**
 *
 * @author user
 */
public class Space {
    
     private int id;
    private boolean disabilityAdaptation;
    private boolean spaceTaken;
    private VehicleType vehicleType;

    public Space(int id, boolean disabilityAdaptation, boolean spaceTaken, VehicleType vehicleType) {
        this.id = id;
        this.disabilityAdaptation = disabilityAdaptation;
        this.spaceTaken = spaceTaken;
        this.vehicleType = vehicleType;
    }

    public Space() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isDisabilityAdaptation() {
        return disabilityAdaptation;
    }

    public void setDisabilityAdaptation(boolean disabilityAdaptation) {
        this.disabilityAdaptation = disabilityAdaptation;
    }

    public boolean isSpaceTaken() {
        return spaceTaken;
    }

    public void setSpaceTaken(boolean spaceTaken) {
        this.spaceTaken = spaceTaken;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    @Override
    public String toString() {
        return "Space{" + "id=" + id + ", disabilityAdaptation=" + disabilityAdaptation + ", spaceTaken=" + spaceTaken + ", vehicleType=" + vehicleType + '}';
    }
    
    
}
