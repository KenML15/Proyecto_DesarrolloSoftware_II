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
public class CommonTypeReport extends Reports{
    
    private ArrayList<String> parkingLots;
    private ArrayList<String> commonVehicleType;

    public CommonTypeReport() {
    }

    public CommonTypeReport(ArrayList<String> parkingLots, ArrayList<String> commonVehicleType, LocalDateTime entryTime, LocalDateTime exitTime, String administratorName, LocalDateTime currentDate) {
        super(entryTime, exitTime, administratorName, currentDate);
        this.parkingLots = parkingLots;
        this.commonVehicleType = commonVehicleType;
    }

    public ArrayList<String> getParkingLots() {
        return parkingLots;
    }

    public void setParkingLots(ArrayList<String> parkingLots) {
        this.parkingLots = parkingLots;
    }

    public ArrayList<String> getCommonVehicleType() {
        return commonVehicleType;
    }

    public void setCommonVehicleType(ArrayList<String> commonVehicleType) {
        this.commonVehicleType = commonVehicleType;
    }

    @Override
    public String toString() {
        return "CommonTypeReport{" + "parkingLots=" + parkingLots + ", commonVehicleType=" + commonVehicleType + '}';
    }
}
