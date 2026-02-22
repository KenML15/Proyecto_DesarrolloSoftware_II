/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import model.data.PDFGenerator;
import model.data.ReportsData;
import model.entities.CommonTypeReport;
import model.entities.IncomeReport;
import model.entities.Invoice;
import model.entities.Vehicle;
import model.entities.VehicleQuantityReport;
import org.jdom2.JDOMException;

/**
 *
 * @author 50687
 */
public class ReportsController {
    ReportsData reportsData;
    private static final String REPORTS_FILE = "reports/";
    
    public ReportsController() throws IOException, JDOMException{
        reportsData = new ReportsData();
        new File(REPORTS_FILE).mkdirs();
    }
    
    public IncomeReport generateIncomeReport (LocalDateTime entryTime, LocalDateTime exitTime, String adminName) throws IOException {
        ArrayList<Invoice> allInvoices = reportsData.getAllInvoices();
        ArrayList<Invoice> filterInvoices = filterInvoicesByDate(allInvoices, entryTime, exitTime);
        
        ArrayList<String> parkingLots = getUniqueParkingLots(filterInvoices);
        ArrayList<Float> incomes = new ArrayList<>();
        
        calculateIncomes(filterInvoices, parkingLots, incomes);
        
        //Calculamos el total de ingresos
        float total = 0;
        for (float income : incomes) {
            total += income;
        }
        
        IncomeReport incomeReport = new IncomeReport();
        incomeReport.setEntryTime(entryTime);
        incomeReport.setExitTime(exitTime);
        incomeReport.setAdministratorName(adminName);
        incomeReport.setParkingLotNames(parkingLots);
        incomeReport.setIncomes(incomes);
        incomeReport.setTotal(total);
        incomeReport.setCurrentDate(LocalDateTime.now());
        
        return incomeReport;
    }
    
    public void saveIncomeReport(LocalDateTime entryTime, LocalDateTime exitTime, String fileName, String adminName) throws IOException {
        IncomeReport incomeReport = generateIncomeReport(entryTime, exitTime, adminName);
        reportsData.saveIncomeReport(incomeReport, fileName);
    }
    
    public void exportIncomeReportToPDF(LocalDateTime entryTime, LocalDateTime exitTime, String fileName, String adminName) throws IOException {

        IncomeReport report = generateIncomeReport(entryTime, exitTime, adminName);

        PDFGenerator pdf = new PDFGenerator();
        pdf.generateIncomePDF(report, REPORTS_FILE + fileName);
    }
    
    public VehicleQuantityReport generateVehicleQuantityReport (LocalDateTime entryTime, LocalDateTime exitTime, String adminName) throws IOException{
        ArrayList<Invoice> allInvoices = reportsData.getAllInvoices();
        ArrayList<Invoice> filterInvoices = filterInvoicesByDate(allInvoices, entryTime, exitTime);
        
        ArrayList<String> parkingLots = getUniqueParkingLots(filterInvoices);
        ArrayList<String> vehicleTypes = new ArrayList<>();
        ArrayList<Integer> quantity = new ArrayList<>();
        
        countVehicleTypes(filterInvoices, vehicleTypes, quantity);
        
        VehicleQuantityReport vehicleQuantityReport = new VehicleQuantityReport();
        vehicleQuantityReport.setEntryTime(entryTime);
        vehicleQuantityReport.setExitTime(exitTime);
        vehicleQuantityReport.setAdministratorName(adminName);
        vehicleQuantityReport.setParkingLotsNames(parkingLots);
        vehicleQuantityReport.setVehicleTypes(vehicleTypes);
        vehicleQuantityReport.setQuantity(quantity);
        vehicleQuantityReport.setCurrentDate(LocalDateTime.now());
        
        return vehicleQuantityReport;
    }
    
    public void saveVehicleQuantityReport(LocalDateTime entryTime, LocalDateTime exitTime, String fileName, String adminName) throws IOException {
        VehicleQuantityReport vehicleQuantityReport = generateVehicleQuantityReport(entryTime, exitTime, adminName);
        reportsData.saveVehicleQuantityReport(vehicleQuantityReport, fileName);
    }
    
    public void exportVehicleQuantityToPDF(LocalDateTime entryTime, LocalDateTime exitTime, String fileName, String adminName) throws IOException {
        
        VehicleQuantityReport vehicleQuantityReport = generateVehicleQuantityReport(entryTime, exitTime, adminName);

        PDFGenerator pdf = new PDFGenerator();
        pdf.generateVehicleQuantityPDF(vehicleQuantityReport, REPORTS_FILE + fileName);
    }
    
    public CommonTypeReport generateCommonTypeReport(LocalDateTime entryTime, LocalDateTime exitTime, String adminName) throws IOException{
        ArrayList<Invoice> allInvoices = reportsData.getAllInvoices();
        ArrayList<Invoice> filterInvoices = filterInvoicesByDate(allInvoices, entryTime, exitTime);
        
        ArrayList<String> parkingLots = getUniqueParkingLots(filterInvoices);
        ArrayList<String> mostCommonVehicleType = findMostCommonVehicleType(filterInvoices, parkingLots);
        
        CommonTypeReport commonTypeReport = new CommonTypeReport();
        commonTypeReport.setEntryTime(entryTime);
        commonTypeReport.setExitTime(exitTime);
        commonTypeReport.setAdministratorName(adminName);
        commonTypeReport.setParkingLots(parkingLots);
        commonTypeReport.setCommonVehicleType(mostCommonVehicleType);
        commonTypeReport.setCurrentDate(LocalDateTime.now());
        
        return commonTypeReport;
    }
    
    public void saveCommonTypeReport(LocalDateTime entryTime, LocalDateTime exitTime, String fileName, String adminName) throws IOException {
        CommonTypeReport commonTypeReport = generateCommonTypeReport(entryTime, exitTime, adminName);
        reportsData.saveCommonTypeReport(commonTypeReport, fileName);
    }
    
    public void exportCommonTypeToPDF(LocalDateTime entryTime, LocalDateTime exitTime, String fileName, String adminName) throws IOException {
        
        CommonTypeReport commonTypeReport = generateCommonTypeReport(entryTime, exitTime, adminName);

        PDFGenerator pdf = new PDFGenerator();
        pdf.generateCommonTypePDF(commonTypeReport, REPORTS_FILE + fileName);
    }
   
    //Métodos auxiliares
    
    //Este método filtra las facturas por fecha
    private ArrayList<Invoice> filterInvoicesByDate (ArrayList<Invoice> invoices, LocalDateTime entryTime, LocalDateTime exitTime) throws IOException {
        ArrayList<Invoice> filterInvoices = new ArrayList<>();
        
        for (Invoice invoice : invoices){
            if (!invoice.getExitTime().isBefore(entryTime) && !invoice.getExitTime().isAfter(exitTime)){
                filterInvoices.add(invoice);
            }
        }
        
        return filterInvoices;
    }
    
    //Este método obtiene nombres únicos de la lista de facturas
    private ArrayList<String> getUniqueParkingLots(ArrayList<Invoice> invoices) throws IOException {
        ArrayList<String> parkingLots = new ArrayList<>();
        
        for (Invoice invoice : invoices){
            String parkingLot = invoice.getParkingLotName();
            
            if(!parkingLots.contains(parkingLot)){
                parkingLots.add(parkingLot);
            }
        }
        
        return parkingLots;
    }
    
    //Obtiene el tipo de vehículo de una factura
    private String getVehicleType(Invoice invoice) throws IOException {
        ArrayList<Vehicle> vehicles = reportsData.getAllVehicles();
        
        for (Vehicle vehicle : vehicles){
            if(vehicle.getPlate().equals(invoice.getVehiclePlate())){
                return vehicle.getVehicleType().getDescription();
            }
        }
        
        return "Tipo de vehículo desconocido";
    }
    
    //Cuenta los tipos de vehículos de la lista de facturas
    private void countVehicleTypes (ArrayList<Invoice> invoices, ArrayList<String> vehicleTypes, ArrayList<Integer> counts) throws IOException{
        
        for (Invoice invoice : invoices){
            String vehicleType = getVehicleType(invoice);
            int index = vehicleTypes.indexOf(vehicleType);
            
            if (index == -1){
                vehicleTypes.add(vehicleType);
                counts.add(1);
            }else{
                counts.set(index, counts.get(index) + 1);
            }
        }
    }

    //Calculamos los ingresos por parqueo
    private void calculateIncomes(ArrayList<Invoice> invoices, ArrayList<String> parkingLots, ArrayList<Float> incomes) throws IOException {

        //Inicializamos los ingresos en cero
        for (String parkingLot : parkingLots) {
            incomes.add(0f);
        }

        //Se suman los ingresos
        for (Invoice invoice : invoices) {
            int index = parkingLots.indexOf(invoice.getParkingLotName());

            if (index != -1) {
                incomes.set(index, incomes.get(index) + invoice.getTotalAmount());
            }
        }
    }

    //Encuentra el tipo de vehículo más común de la lista de facturas
    private ArrayList<String> findMostCommonVehicleType(ArrayList<Invoice> invoices, ArrayList<String> parkingLots) throws IOException {
        ArrayList<String> mostCommonTypes = new ArrayList<>();

        for (String parkingLot : parkingLots) {

            //Facturas solo de ese parqueo
            ArrayList<Invoice> parkingInvoices = new ArrayList<>();

            for (Invoice invoice : invoices) {
                if (invoice.getParkingLotName().equals(parkingLot)) {
                    parkingInvoices.add(invoice);
                }
            }

            //Contar tipos
            ArrayList<String> vehicleTypes = new ArrayList<>();
            ArrayList<Integer> counts = new ArrayList<>();
            countVehicleTypes(parkingInvoices, vehicleTypes, counts);

            //Buscar el mayor
            String mostCommon = "Sin datos";
            int max = 0;

            for (int i = 0; i < counts.size(); i++) {
                if (counts.get(i) > max) {
                    max = counts.get(i);
                    mostCommon = vehicleTypes.get(i);
                }
            }

            mostCommonTypes.add(mostCommon);
        }

        return mostCommonTypes;
    }
}
