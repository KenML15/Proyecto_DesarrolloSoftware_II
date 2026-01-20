/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.util.ArrayList;
import model.data.ParkingLotData;
import model.entity.ParkingLot;
import model.entity.Space;
import model.entity.Vehicle;

/**
 *
 * @author user
 */
public class ParkingLotController {
    
        private ParkingLotData parkingLotData = new ParkingLotData();

    public ParkingLot registerParkingLot(String name, Space spaces[]) {

        return parkingLotData.registerParkingLot(name, spaces);
    }

    public int registerVehicleInParkingLot(Vehicle vehicle, ParkingLot parkingLot) {

        return parkingLotData.registerVehicleInParkingLot(vehicle, parkingLot);

    }

    public void removeVehicleFromParkingLot(Vehicle vehicle, ParkingLot parkingLot) {

        parkingLotData.removeVehicleFromParkingLot(vehicle, parkingLot);
    }

    public ParkingLot findParkingLotById(int id) {

        return parkingLotData.findParkingLotById(id);
    }

    public ArrayList<ParkingLot> getAllParkingLots() {

        return parkingLotData.getAllParkingLots();
    }
}
