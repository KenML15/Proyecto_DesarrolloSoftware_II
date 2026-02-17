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
public class Invoice {
    private int id;
    private String vehiclePlate;
    private String parkingLotName;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private float totalAmount;

    public Invoice() {
    }

    public Invoice(int id, String vehiclePlate, String parkingLotName, LocalDateTime entryTime, LocalDateTime exitTime, float totalAmount) {
        this.id = id;
        this.vehiclePlate = vehiclePlate;
        this.parkingLotName = parkingLotName;
        this.entryTime = entryTime;
        this.exitTime = exitTime;
        this.totalAmount = totalAmount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVehiclePlate() {
        return vehiclePlate;
    }

    public void setVehiclePlate(String vehiclePlate) {
        this.vehiclePlate = vehiclePlate;
    }

    public String getParkingLotName() {
        return parkingLotName;
    }

    public void setParkingLotName(String parkingLotName) {
        this.parkingLotName = parkingLotName;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(LocalDateTime entryTime) {
        this.entryTime = entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public void setExitTime(LocalDateTime exitTime) {
        this.exitTime = exitTime;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }
}
