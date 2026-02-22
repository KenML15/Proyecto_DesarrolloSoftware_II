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
public class IncomeReport extends Reports{
    private ArrayList<String> parkingLotNames;
    private ArrayList<Float> incomes;
    private float total;

    public IncomeReport() {
    }

    public IncomeReport(ArrayList<String> parkingLotNames, ArrayList<Float> incomes, float total, LocalDateTime entryTime, LocalDateTime exitTime, String administratorName, LocalDateTime currentDate) {
        super(entryTime, exitTime, administratorName, currentDate);
        this.parkingLotNames = parkingLotNames;
        this.incomes = incomes;
        this.total = total;
    }

    public ArrayList<String> getParkingLotNames() {
        return parkingLotNames;
    }

    public void setParkingLotNames(ArrayList<String> parkingLotNames) {
        this.parkingLotNames = parkingLotNames;
    }

    public ArrayList<Float> getIncomes() {
        return incomes;
    }

    public void setIncomes(ArrayList<Float> incomes) {
        this.incomes = incomes;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "IncomeReport{" + "parkingLotNames=" + parkingLotNames + ", incomes=" + incomes + ", total=" + total + '}';
    }
}
