/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.data;

import java.util.ArrayList;
import model.entities.Customer;
import model.entities.ParkingLot;
import model.entities.Space;
import model.entities.Vehicle;

/**
 *
 * @author 50687
 */
public class ParkingLotData {

    public ArrayList<ParkingLot> parkingLots;
    static int parkingLotId = 0;

    public ParkingLotData() {
        parkingLots = new ArrayList<>();
    }

    public ParkingLot registerParkingLot(String name, Space spaces[]) {

        ParkingLot parkingLot = new ParkingLot();
        parkingLotId++;
        parkingLot.setId(parkingLotId);
        parkingLot.setName(name);
        parkingLot.setSpaces(spaces);
        parkingLots.add(parkingLot);

        return parkingLot;

    }

    public boolean hasAnyDisabilityResponsible(Vehicle vehicle) {
        for (Customer customer : vehicle.getCustomer()) {
            if (customer.isDisabilityPresented()) {
                return true;
            }
        }
        return false;
    }

    public int registerVehicleInParkingLot(Vehicle vehicle, ParkingLot parkingLot) {
        ArrayList<Vehicle> vehiclesInParkingLot = parkingLot.getVehicles();
        Space spaces[] = parkingLot.getSpaces();
        int spaceId = -1;

        boolean requiresDisabilitySpace = hasAnyDisabilityResponsible(vehicle);

        for (Space space : spaces) {
            if (!space.isSpaceTaken()) {

                if (space.isDisabilityAdaptation() == requiresDisabilitySpace) {

                    if (space.getVehicleType().getId() == vehicle.getVehicleType().getId()) {

                        vehiclesInParkingLot.add(vehicle);

                        vehiclesInParkingLot.add(vehicle);
                        space.setSpaceTaken(true);
                        spaceId = space.getId();
                        break;
                    }
                }
            }
        }

        parkingLot.setVehicles(vehiclesInParkingLot);
        return spaceId;
    }

    public void removeVehicleFromParkingLot(Vehicle vehicle, ParkingLot parkingLot) {

        ArrayList<Vehicle> vehiclesInParkingLot = parkingLot.getVehicles();
        Space spaces[] = parkingLot.getSpaces();

        for (int i = 0; i < vehiclesInParkingLot.size(); i++) {

            if (vehiclesInParkingLot.get(i) == vehicle) {

                vehiclesInParkingLot.remove(vehicle);
                spaces[i].setSpaceTaken(false);
                break;
            }

        }

        parkingLot.setSpaces(spaces);
        parkingLot.setVehicles(vehiclesInParkingLot);

    }

    public ParkingLot findParkingLotById(int id) {

        ParkingLot parkingLotToBeReturned = null;

        for (ParkingLot parkingLot : parkingLots) {

            if (parkingLot.getId() == id) {

                parkingLotToBeReturned = parkingLot;
                break;
            }
        }
        return parkingLotToBeReturned;
    }

    public ArrayList<ParkingLot> getAllParkingLots() {

        return parkingLots;

    }

}
