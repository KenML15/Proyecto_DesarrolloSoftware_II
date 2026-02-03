/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.util.ArrayList;
import model.data.ParkingLotDataFile;
import model.entities.ParkingLot;

/**
 *
 * @author 50687
 */
public class ParkingLotFileController {
    ParkingLotDataFile parkingLotDataFile = new ParkingLotDataFile();
    
    public int insertParkingLot(ParkingLot parkingLot){
        return parkingLotDataFile.insertParkingLot(parkingLot);
    }
    
    public boolean findParkingLotById(int parkingLotId) {
        return parkingLotDataFile.findParkingLotById(parkingLotId);
    }
    
    public ParkingLot getParkingLotFromFile(int parkingLotId) {
        return parkingLotDataFile.getParkingLotFromFile(parkingLotId);
    }
    
    public ArrayList<ParkingLot> getAllParkingLots() {
        return parkingLotDataFile.getAllParkingLots();
    }
    
    public String[][] createParkingLotMatrix(ArrayList<ParkingLot> parkingLots) {
        return parkingLotDataFile.createParkingLotMatrix(parkingLots);
    }
    
    public void deleteParkingLotFromFile(String lineToRemove) {
        parkingLotDataFile.deleteParkingLotFromFile(lineToRemove);
    }
}
