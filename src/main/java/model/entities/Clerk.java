/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.entities;

/**
 *
 * @author 50687
 */
public class Clerk extends User implements Employee{
    private int employeeCode;
    private String schedule;
    private int age;
    private ParkingLot parkingLot;

    public Clerk() {
    }

    public Clerk(int employeeCode, String schedule, int age, ParkingLot parkingLot) {
        this.employeeCode = employeeCode;
        this.schedule = schedule;
        this.age = age;
        this.parkingLot = parkingLot;
    }

    public Clerk(int employeeCode, String schedule, int age, ParkingLot parkingLot, String identification, String name, String username, String password) {
        super(identification, name, username, password);
        this.employeeCode = employeeCode;
        this.schedule = schedule;
        this.age = age;
        this.parkingLot = parkingLot;
    }

    public int getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(int employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public ParkingLot getParkingLot() {
        return parkingLot;
    }

    public void setParkingLot(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
    }

    @Override
    public String toString() {
        return "Clerk{" + "employeeCode=" + employeeCode + ", schedule=" + schedule + ", age=" + age + ", parkingLot=" + parkingLot + '}';
    }

    @Override
    public boolean verifyUserLogin(String loginDetails) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public float calculateSalary(float dailySalary) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ParkingLot assignWorkplace(int parkingLotId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
}
