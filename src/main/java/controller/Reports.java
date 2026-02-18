/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import model.data.InvoiceDataFile;
import model.entities.Invoice;
import org.jdom2.JDOMException;

/**
 *
 * @author 50687
 */
public class Reports {
    
    private final InvoiceDataFile invoiceData;
    private final ParkingLotFileController parkingLotController;
    private final VehicleFileController vehicleController;
    private final ObjectMapper mapper;
    
    public Reports() throws IOException, JDOMException {
        this.invoiceData = new InvoiceDataFile();
        this.parkingLotController = new ParkingLotFileController();
        this.vehicleController = new VehicleFileController();
        
        //Inicialización para los archivos JSON
        this.mapper = new ObjectMapper();
    }
    
    //Este método va a filtrar las facturas por fecha
    public ArrayList<Invoice> invoiceDateFilter(ArrayList<Invoice> invoices, String entryTime, String exitTime){
        ArrayList<Invoice> invoicesToReturn = new ArrayList<>();
        
        //Convierte los Strings a localDates
        DateTimeFormatter localDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate entryTimeParsed = LocalDate.parse(entryTime, localDateFormatter);
        LocalDate exitTimeParsed = LocalDate.parse(exitTime, localDateFormatter);
        
        for(Invoice invoice : invoices){
            LocalDate invoiceDate = invoice.getExitTime().toLocalDate();
            
            if ((invoiceDate.isEqual(exitTimeParsed) || invoiceDate.isAfter(entryTimeParsed)) && (invoiceDate.isEqual(exitTimeParsed) || invoiceDate.isBefore(exitTimeParsed))){
                invoicesToReturn.add(invoice);
            }
        }
        
        return invoicesToReturn;
    }
    
    //Método para obtener los nombres de los parqueos sin repetirloss
    public ArrayList<String> getUniqueParkingLotsNames (ArrayList<Invoice> invoices){
        ArrayList<String> parkingLotsNamesToReturn = new ArrayList<>();
        
        for(Invoice invoice : invoices){
            String parkingLotName = invoice.getParkingLotName();
            
            //Verifica si ya esxiste en la lista
            boolean exists = false;
            for(String parkingLot : parkingLotsNamesToReturn){
                if(parkingLot.equalsIgnoreCase(parkingLotName)){
                    exists = true;
                    break;
                }
            }
            
            if(!exists){
                parkingLotsNamesToReturn.add(parkingLotName);
            }
        }
        return parkingLotsNamesToReturn;
    }
    
    //Este método calcula los ingresos obtenidos por parqueo
    public float[] calculateEarnings (ArrayList<Invoice> invoices, ArrayList<String> parkingLotsNames){
        float[] earnings = new float[parkingLotsNames.size()];
        
        //Primero se inicializan los ingresos en cero
        for(int i = 0; i <earnings.length; i++){
            earnings[i] = 0;
        }
        
        //Aquí se van a sumar los ingresos totales
        for (Invoice invoice : invoices) {
            String parkingLotName = invoice.getParkingLotName();
            float amount = invoice.getTotalAmount();

            //Buscar la posición del parqueo
            for (int j = 0; j < parkingLotsNames.size(); j++) {
                if (parkingLotsNames.get(j).equals(parkingLotName)) {
                    earnings[j] += amount;
                    break;
                }
            }
        }
        return earnings;   
    }
    
    //Método para calcular los ingresos totales
    public float calculateTotalEarnings (float[] earnings) {
        float total = 0;
        for (int i = 0; i < earnings.length; i++){
            total += earnings[i];
        }
        return total;
    }
    
    //Método para contar los vehículos por parqueo
    public int[] countVehicles (ArrayList<Invoice> invoices, ArrayList<String> parkingLotsNames){
        int[] count = new int[parkingLotsNames.size()];
        
        //Aquí también inicializamos el conteo en cero
        for(int i = 0; i <count.length; i++){
            count[i] = 0;
        }
        
        //Aquí contamos los vehículos
        for(Invoice invoice : invoices){
            String parkingLotName = invoice.getParkingLotName();
            
            //Busca la posición del parqueo
            for (int j =0; j < parkingLotsNames.size(); j++){
                if(parkingLotsNames.get(j).equalsIgnoreCase(parkingLotName)){
                    count[j] += 1;
                    break;
                }
            }
        }
        
        return count;
    }
    
    //========================================================================
    
    //Método para guardar en el archivo
    public void saveFile(String content, String fileName) throws IOException{
        FileWriter file = new FileWriter(fileName);
        PrintWriter writer = new PrintWriter(file);
        
        writer.println(content);
        writer.close();
    }
    
    //Este método genera el reporte de los ingresos obtenidos
    public void generateEarningsReport(String fileName, String entryTime, String exitTime) throws IOException{
        //Obtiene las facturas
        ArrayList<Invoice> allInvoices = invoiceData.getAllInvoices();
        
        //Luego las filtramos por fecha
        ArrayList<Invoice> filterInvoices = invoiceDateFilter(allInvoices, entryTime, exitTime);
        
        //OObtenemos la lista de parqueos únicos
        ArrayList<String> parkingLots = getUniqueParkingLotsNames(filterInvoices);
        
        //Calculamos los ingresos por parqueo
        float[] earnings = calculateEarnings(filterInvoices, parkingLots);
        
        //Calculamos el total
        float total = calculateTotalEarnings(earnings);
        
        String earningsJsonFile = createEarningsJsonFile(parkingLots, earnings, total, filterInvoices.size(), entryTime, exitTime);
        
        saveFile(earningsJsonFile, fileName);
        
    }
    
    private String createEarningsJsonFile(ArrayList<String> parkingLots, float[] earnings, float total, int invoicesQuantity, String entryTime, String exitTime){
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        
        String json = "{\n";
        json = json + "  \"reporte\": \"INGRESOS POR PARQUEO\",\n";
        //json = json + "  \"administrador\": \"" + adminName + "\",\n";
        json = json + "  \"fecha_generacion\": \"" + date + "\",\n";
        json = json + "  \"periodo_inicio\": \"" + entryTime + "\",\n";
        json = json + "  \"periodo_fin\": \"" + exitTime + "\",\n";
        json = json + "  \"total_facturas\": " + invoicesQuantity + ",\n";
        json = json + "  \"ingresos_totales\": " + total + ",\n";
        json = json + "  \"detalle_por_parqueo\": [\n";
        
        for (int i = 0; i < parkingLots.size(); i++) {
            json = json + "    {\n";
            json = json + "      \"parqueo\": \"" + parkingLots.get(i) + "\",\n";
            json = json + "      \"ingresos\": " + earnings[i] + "\n";
            json = json + "    }";

            if (i < parkingLots.size() - 1) {
                json = json + ",";
            }
            json = json + "\n";
        }
        
        json = json + "  ]\n";
        json = json + "}";
        
        return json;
    }
    
    public void createVehiclesReport(String fileName, String entryTime, String exitTime) throws IOException{
        //Obtenemos todas las facturas
        ArrayList<Invoice> totalInvoices = invoiceData.getAllInvoices();
        
        //Filtramos por fecha
        ArrayList<Invoice> filterInvoices = invoiceDateFilter(totalInvoices, entryTime, exitTime);
        
        //Obtenemos la lista de parqueos
        ArrayList<String> parkingLots = getUniqueParkingLotsNames(filterInvoices);
        
        //Contamos los vehículos por parqueo
        int[] count = countVehicles(filterInvoices, parkingLots);
        
        String vehicleJsonFile = createVehiclesJsonFile(parkingLots, count, filterInvoices.size(), entryTime, exitTime);
        
        saveFile(vehicleJsonFile, fileName);
    }
    
    public String createVehiclesJsonFile(ArrayList<String> parkingLots, int[] count, int totalVehicles, String entryTime, String exitTime) {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

        String json = "{\n";
        json = json + "  \"reporte\": \"VEHÍCULOS POR PARQUEO\",\n";
        //json = json + "  \"administrador\": \"" + adminName + "\",\n";
        json = json + "  \"fecha_generacion\": \"" + date + "\",\n";
        json = json + "  \"periodo_inicio\": \"" + entryTime + "\",\n";
        json = json + "  \"periodo_fin\": \"" + exitTime + "\",\n";
        json = json + "  \"total_vehiculos\": " + totalVehicles + ",\n";
        json = json + "  \"detalle_por_parqueo\": [\n";

        for (int i = 0; i < parkingLots.size(); i++) {
            json = json + "    {\n";
            json = json + "      \"parqueo\": \"" + parkingLots.get(i) + "\",\n";
            json = json + "      \"vehiculos\": " + count[i] + "\n";
            json = json + "    }";

            if (i < parkingLots.size() - 1) {
                json = json + ",";
            }
            json = json + "\n";
        }

        json = json + "  ]\n";
        json = json + "}";

        return json;
    }
}
