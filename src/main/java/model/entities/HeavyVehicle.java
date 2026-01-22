/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.entities;

import java.time.LocalDateTime;

/**
 *
 * @author 50687
 */
public class HeavyVehicle extends Vehicle {
    private boolean extraWeight;
    private double maxWeigth;

    public HeavyVehicle() {
    }

   

    

    public boolean isExtraWeight() {
        return extraWeight;
    }

    public void setExtraWeight(boolean extraWeight) {
        this.extraWeight = extraWeight;
    }

    public double getMaxWeigth() {
        return maxWeigth;
    }

    public void setMaxWeigth(double maxWeigth) {
        this.maxWeigth = maxWeigth;
    }

    
    
    
}
