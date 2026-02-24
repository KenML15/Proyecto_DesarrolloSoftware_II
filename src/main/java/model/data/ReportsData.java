/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
public class ReportsData {

    private final ObjectMapper mapper;
    private final String REPORTS_FILE = "reports/";
    private final InvoiceDataFile invoiceData;
    private final VehicleDataFile vehicleData;

    public ReportsData() throws IOException, JDOMException {
        this.mapper = new ObjectMapper();
        //Registra un módulo para las fechas
        mapper.registerModule(new JavaTimeModule());
//        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        //Creamos un directorio para los reportes
        new File(REPORTS_FILE).mkdirs();

        invoiceData = new InvoiceDataFile();
        vehicleData = new VehicleDataFile();
    }

    //Aquí se crea el archivo del reporte de ingresos por parqueo, más el total
    public void saveIncomeReport(IncomeReport incomeReport, String fileName) throws IOException {
        mapper.writeValue(new File(REPORTS_FILE + fileName), incomeReport);
    }

    //Aquí se crea el archivo del reporte de la cantidad de vehículos en cada parqueo según su tipo
    public void saveVehicleQuantityReport(VehicleQuantityReport vehicleReport, String fileName) throws IOException {
        mapper.writeValue(new File(REPORTS_FILE + fileName), vehicleReport);
    }

    //Aquí se crea el reporte del tipo de vehículo más común (o más frecuente) de cada parqueo.
    public void saveCommonTypeReport(CommonTypeReport commonTypeReport, String fileName) throws IOException {
        mapper.writeValue(new File(REPORTS_FILE + fileName), commonTypeReport);
    }

    public ArrayList<Invoice> getAllInvoices() throws IOException {
        return invoiceData.getAllInvoices();
    }

    public ArrayList<Vehicle> getAllVehicles() throws IOException {
        return vehicleData.getAllVehicles();
    }

}
