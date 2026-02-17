/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import model.entities.Invoice;

/**
 *
 * @author 50687
 */
public class InvoiceDataFile {
    private final String fileName;
    private static final String INVOICE_FILE = "Invoices.txt";
    private static final String DELIMETER = ";";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    
    public InvoiceDataFile() throws IOException {
        this.fileName = INVOICE_FILE;
        ensureFileExists();
    }
    
    private void ensureFileExists() throws IOException{
        File file = new File(fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
    }
    
    public void insertInvoice (Invoice invoice) throws IOException {
        validateInvoice(invoice);
        appendToFile(invoice);
    }
    
    private void validateInvoice(Invoice invoice) throws IOException {
        if (invoice.getVehiclePlate() == null || invoice.getVehiclePlate().trim().isEmpty()) {
            throw new IllegalArgumentException("Placa del vehículo requerida para la factura");
        }
    }
    
    private void appendToFile(Invoice invoice) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName, true))) {
            writer.println(formatInvoice(invoice));
        }
    }
    
    private String formatInvoice(Invoice invoice){
        return String.join(DELIMETER,
                String.valueOf(invoice.getId()),
                invoice.getVehiclePlate(),
                invoice.getParkingLotName(),
                invoice.getEntryTime().format(DATE_FORMATTER),
                invoice.getExitTime().format(DATE_FORMATTER),
                String.valueOf(invoice.getTotalAmount())
        );
    }
    
    public ArrayList<Invoice> getAllInvoices() throws IOException {
        ArrayList<Invoice> invoices = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(fileName))){
            String line;
            while((line = reader.readLine()) != null){
                Invoice invoice = parseInvoice(line);
                if(invoice != null){
                    invoices.add(invoice);
                }
            }
        }
        return invoices;
    }
    
    private Invoice parseInvoice(String line) throws IOException {
        if (line == null || line.trim().isEmpty()){
            return null;
        }
        
        String[] parts = line.split(DELIMETER, -1);
        if (parts.length != 6) {
            return null;
        }
        
        int id = Integer.parseInt(parts[0]);
        String plate = parts[1];
        String parkingLotName = parts[2];
        LocalDateTime entryTime = LocalDateTime.parse(parts[3], DATE_FORMATTER);
        LocalDateTime exitTime = LocalDateTime.parse(parts[4], DATE_FORMATTER);
        float amount = Float.parseFloat(parts[5]);
        
        return new Invoice(id, plate, parkingLotName, entryTime, exitTime, amount);
    }
    
    public int getNextId()throws IOException {
        int maxId = 0;
        for (Invoice invoice : getAllInvoices()){
            if(invoice.getId() > maxId){
                maxId = invoice.getId();
            }
        }
        return maxId + 1;
    }
}
