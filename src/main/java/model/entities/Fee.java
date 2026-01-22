/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.entities;

/**
 *
 * @author 50687
 */
public class Fee {
    private double halfHourRate; //Tarifa de mediahora
    private double hourlyRate; //Tarifa por hora
    private double dailyRate; //Tarifa por día
    private double weeklyRate; //Tarifa por semana
    private double monthlyRate; //Tarifa por mes
    private double annualRate; //Tarifa anual

    public Fee() {
    }

    public Fee(double halfHourRate, double hourlyRate, double dailyRate, double weeklyRate, double monthlyRate, double annualRate) {
        this.halfHourRate = halfHourRate;
        this.hourlyRate = hourlyRate;
        this.dailyRate = dailyRate;
        this.weeklyRate = weeklyRate;
        this.monthlyRate = monthlyRate;
        this.annualRate = annualRate;
    }

    public double getHalfHourRate() {
        return halfHourRate;
    }

    public void setHalfHourRate(double halfHourRate) {
        this.halfHourRate = halfHourRate;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public double getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(double dailyRate) {
        this.dailyRate = dailyRate;
    }

    public double getWeeklyRate() {
        return weeklyRate;
    }

    public void setWeeklyRate(double weeklyRate) {
        this.weeklyRate = weeklyRate;
    }

    public double getMonthlyRate() {
        return monthlyRate;
    }

    public void setMonthlyRate(double monthlyRate) {
        this.monthlyRate = monthlyRate;
    }

    public double getAnnualRate() {
        return annualRate;
    }

    public void setAnnualRate(double annualRate) {
        this.annualRate = annualRate;
    }
    
    
}
