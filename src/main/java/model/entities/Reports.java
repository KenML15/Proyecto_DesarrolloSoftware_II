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
public class Reports {
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private String administratorName;
    private LocalDateTime currentDate;

    public Reports() {
    }

    public Reports(LocalDateTime entryTime, LocalDateTime exitTime, String administratorName, LocalDateTime currentDate) {
        this.entryTime = entryTime;
        this.exitTime = exitTime;
        this.administratorName = administratorName;
        this.currentDate = currentDate;
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

    public String getAdministratorName() {
        return administratorName;
    }

    public void setAdministratorName(String administratorName) {
        this.administratorName = administratorName;
    }

    public LocalDateTime getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(LocalDateTime currentDate) {
        this.currentDate = currentDate;
    }

    @Override
    public String toString() {
        return "BaseReport{" + "entryTime=" + entryTime + ", exitTime=" + exitTime + ", administratorName=" + administratorName + ", currentDate=" + currentDate + '}';
    }
}
