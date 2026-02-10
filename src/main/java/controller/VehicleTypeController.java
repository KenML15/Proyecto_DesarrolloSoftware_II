/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.io.IOException;
import java.util.ArrayList;
import model.data.VehicleTypeDataFile;
import model.entities.VehicleType;

/**
 *
 * @author 50687
 */
public class VehicleTypeController {
    VehicleTypeDataFile vehicleTypeData;
    
    public VehicleTypeController() throws IOException {
        this.vehicleTypeData = new VehicleTypeDataFile();
    }
    
    public ArrayList<VehicleType> getAllVehicleTypes() throws IOException {
        return vehicleTypeData.getAllVehicleTypes();
        
    }
}
