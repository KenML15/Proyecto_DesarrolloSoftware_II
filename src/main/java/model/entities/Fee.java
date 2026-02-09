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
    private String vehicleType;
    
    private float halfHourRate;
    private float hourlyRate;
    private float dailyRate;
    private float weeklyRate;
    private float monthlyRate;
    private float annualRate;

    public Fee() {
    }

    public Fee(String vehicleType, float halfHourRate, float hourlyRate, float dailyRate, float weeklyRate, float monthlyRate, float annualRate) {
        this.vehicleType = vehicleType;
        this.halfHourRate = halfHourRate;
        this.hourlyRate = hourlyRate;
        this.dailyRate = dailyRate;
        this.weeklyRate = weeklyRate;
        this.monthlyRate = monthlyRate;
        this.annualRate = annualRate;
    }   

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public double getHalfHourRate() {
        return halfHourRate;
    }

    public void setHalfHourRate(float halfHourRate) {
        this.halfHourRate = halfHourRate;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(float hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public double getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(float dailyRate) {
        this.dailyRate = dailyRate;
    }

    public double getWeeklyRate() {
        return weeklyRate;
    }

    public void setWeeklyRate(float weeklyRate) {
        this.weeklyRate = weeklyRate;
    }

    public double getMonthlyRate() {
        return monthlyRate;
    }

    public void setMonthlyRate(float monthlyRate) {
        this.monthlyRate = monthlyRate;
    }

    public double getAnnualRate() {
        return annualRate;
    }

    public void setAnnualRate(float annualRate) {
        this.annualRate = annualRate;
    }
    
    public float calculateFeeForDuration(long minutes) {
        if (minutes <= 30) {
            return halfHourRate;
        } else if (minutes <= 60) {
            return hourlyRate;
        } else if (minutes <= 24 * 60) {
            return dailyRate;
        } else if (minutes <= 7 * 24 * 60) {
            return weeklyRate;
        } else if (minutes <= 30 * 24 * 60) {
            return monthlyRate;
        } else {
            return annualRate;
        }
    }

    @Override
    public String toString() {
        return "Fee{" + "vehicleType=" + vehicleType + ", halfHourRate=" + halfHourRate + ", hourlyRate=" + hourlyRate + ", dailyRate=" + dailyRate + ", weeklyRate=" + weeklyRate + ", monthlyRate=" + monthlyRate + ", annualRate=" + annualRate + '}';
    }        
}
