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
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import model.entities.Fee;
import model.entities.Vehicle;
import model.entities.VehicleType;

/**
 *
 * @author 50687
 */
public class FeeData {
    private static final String FEE = "fee.txt";
    private static final String CONFG = "config_fee_prices.txt";

    //Método para que el usuario configure los 
    //precios de la tarifa según el tipo de vehículo
    public boolean configureFeePrices(Fee newFee){
        boolean works = false;
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(CONFG, true))){//Para que guarde todos los tipos de vehículo
            String line = newFee.getVehicleType() + ","
                    + newFee.getHalfHourRate() + ","
                    + newFee.getHourlyRate() + ","
                    + newFee.getDailyRate() + ","
                    + newFee.getWeeklyRate() + ","
                    + newFee.getMonthlyRate() + ","
                    + newFee.getAnnualRate() + ",";
            
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

            String line; //Almacena cada reglón del texto conforme se va leyendo
            
            while ((line = bufferedReader.readLine()) != null) {//Lee el archivo líneaporr línea y deja de leer hasta que sea null

                String[] splitLines = line.split(",");//Divide la línea en pedazos cada vez que encuentra una coma

                if (splitLines[0].equalsIgnoreCase(vehicleType)) {//Compara el primer elemento del arreglo con el tipo de vehículo

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
            feeToReturn = null;// Si no existe el tipo de vehículo 
        }
        return feeToReturn;
    }
    
    
    //Esto es para cuando el vehículo salga del parqueo
    /*public Fee getFeePrices(Fee newFee){
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(CONFG))){
            String line = newFee.getVehicleType() + ","
                    + newFee.getHalfHourRate() + ","
                    + newFee.getDailyRate() + ","
                    + newFee.getWeeklyRate() + ","
                    + newFee.getMonthlyRate() + ","
                    + newFee.getAnnualRate();
            
            bufferedWriter.write(line);
            bufferedWriter.newLine();
            
        }catch(IOException e){
            JOptionPane.showMessageDialog(null, e);
        }
    }*/
    
}
