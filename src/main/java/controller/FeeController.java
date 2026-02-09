/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import model.data.FeeData;
import model.entities.Fee;
import org.jdom2.JDOMException;

/**
 *
 * @author 50687
 */
public class FeeController {

    private FeeData feeData;
    
    public FeeController() throws IOException, JDOMException {
        this.feeData = new FeeData();
    }
    
    public void insertFee(Fee fee) throws IOException {
        validateFee(fee);
        feeData.insertFee(fee);
    }
    
    public ArrayList<Fee> getAllFees() throws IOException {
        return feeData.getAllFees();
    }
    
    public Fee getFeeByVehicleType(String vehicleType) throws IOException {
        return feeData.getFeeByVehicleType(vehicleType);
    }
    
    public void deleteFee(String vehicleType) throws IOException {
        feeData.deleteFee(vehicleType);
    }
    
    //InsertFee ungresa y actualiza de forma automática
    public void updateFee(Fee fee) throws IOException {
        validateFee(fee);
        feeData.insertFee(fee);
    }
    
    //Calculos para cobrar las tarifas
    public double calculateEstimatedFee(String vehicleType, LocalDateTime entryTime, LocalDateTime exitTime) throws IOException {
        
        if (entryTime == null || exitTime == null){
            throw new IllegalArgumentException("Fechas inválidas");
        }

        Fee fee = feeData.getFeeByVehicleType(vehicleType);
        if (fee == null) {
            throw new IllegalArgumentException("No hay tarifa configurada para: " + vehicleType);
        }
        
        Duration duration = Duration.between(entryTime, exitTime);
        long minutes = duration.toMinutes();
        
        return fee.calculateFeeForDuration(minutes);
    }
    
    //Validación
    private void validateFee(Fee fee){
        if(fee == null){
            throw new IllegalArgumentException("Tarifa requerida");
        }
        if (fee.getVehicleType() == null || fee.getVehicleType().isBlank()){
            throw new IllegalArgumentException("Tipo de vehículo requerido");
        }
    }
}
