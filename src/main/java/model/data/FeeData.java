/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import model.entities.Fee;
import model.entities.VehicleType;

/**
 *
 * @author 50687
 */
public class FeeData {
    private static final String FEE = "fee.txt";
    private static final String CONFG = "config_fee_prices.txt";

    public boolean configureFeePrices(Fee newFee){
        boolean works = false;
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(CONFG, true))){
            String line = newFee.getVehicleType() + ";"
                    + newFee.getHalfHourRate() + ";"
                    + newFee.getHourlyRate() + ";"
                    + newFee.getDailyRate() + ";"
                    + newFee.getWeeklyRate() + ";"
                    + newFee.getMonthlyRate() + ";"
                    + newFee.getAnnualRate() + ";";
            
            bufferedWriter.write(line);
            bufferedWriter.newLine();
            
            works = true;
        }catch(IOException e){
            works = false;
        }
        return works;
    }
    
    public Fee searchFee(String vehicleType, ArrayList<VehicleType> allVehicleTypes) {
        Fee feeToReturn = null;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(CONFG))) {

            String line;
            
            while ((line = bufferedReader.readLine()) != null) {

                String[] splitLines = line.split(",");
                if (splitLines[0].equalsIgnoreCase(vehicleType)) {

                    String foundType = null;
                    for (VehicleType vehicleTypeToFound : allVehicleTypes) {
                        if (vehicleTypeToFound.getDescription().equalsIgnoreCase(vehicleType)) {
                            foundType = vehicleTypeToFound.getDescription();
                            break;
                        }
                    }
                    
                    feeToReturn = new Fee(foundType, Float.parseFloat(splitLines[1]), Float.parseFloat(splitLines[2]), Float.parseFloat(splitLines[3]),
                            Float.parseFloat(splitLines[4]), Float.parseFloat(splitLines[5]), Float.parseFloat(splitLines[6]));
                }
                
            }
        } catch (IOException e) {
            feeToReturn = null;
        }
        return feeToReturn;
    }   
}
