/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package model.entities;

/**
 *
 * @author 50687
 */
public interface Employee {
    
    public float calculateSalary(float dailySalary);
    
    public ParkingLot assignWorkplace(int parkingLotId);
    
}
