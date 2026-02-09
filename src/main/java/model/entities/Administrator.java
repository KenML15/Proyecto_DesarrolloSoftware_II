/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.entities;

/**
 *
 * @author 50687
 */
public class Administrator extends Clerk {

    public Administrator(int employeeCode, String schedule, int age, ParkingLot parkingLot) {
        super(employeeCode, schedule, age, parkingLot);
    }

    public Administrator(int employeeCode, String schedule, int age, ParkingLot parkingLot, String identification, String name, String username, String password) {
        super(employeeCode, schedule, age, parkingLot, identification, name, username, password);
    }

}
