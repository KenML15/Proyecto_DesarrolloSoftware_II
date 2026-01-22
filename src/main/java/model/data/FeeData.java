/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.data;

import java.time.Duration;
import java.time.LocalDateTime;
import model.entities.Vehicle;

/**
 *
 * @author 50687
 */
public class FeeData {
    
    public double calculateTotal(Vehicle vehicle){
        //Se calculan automáticamente el tiempo que lleva el vehículo en el parqueo
        long minutes = Duration.between(vehicle.getEntryTime(), LocalDateTime.now()).toMinutes();
        return 0.0;
        
    }
    
}
