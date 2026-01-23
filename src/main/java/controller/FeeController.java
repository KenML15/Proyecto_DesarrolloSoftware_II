/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import model.data.FeeData;
import model.entities.Fee;
import model.entities.Vehicle;
import model.entities.VehicleType;

/**
 *
 * @author 50687
 */
public class FeeController {
    
    FeeData feeData = new FeeData();
    
    public boolean configureFeePrices(Fee newFee){
        return feeData.configureFeePrices(newFee);
    }
    
    public Fee searchFee(String vehicleType, ArrayList<VehicleType> allVehicleTypes) {
        return feeData.searchFee(vehicleType, allVehicleTypes);
    }   
}
