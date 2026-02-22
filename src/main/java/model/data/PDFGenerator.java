/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.data;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import model.entities.IncomeReport;
import model.entities.VehicleQuantityReport;
import model.entities.CommonTypeReport;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

/**
 *
 * @author 50687
 */
public class PDFGenerator {

    private static final float START_X = 50;
    private static final float START_Y = 750;
    private static final float LINE_SPACING = 18;
    private static final float MARGIN_LEFT = 50;
    
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    // ===============================
    // MÉTODO BASE PARA CREAR PDF
    // ===============================
    private PDPageContentStream createDocument(PDDocument document, String title) throws IOException {
        PDPage page = new PDPage();
        document.addPage(page);

        PDPageContentStream content = new PDPageContentStream(document, page);

        content.beginText();
        content.setFont(PDType1Font.HELVETICA_BOLD, 16);
        content.newLineAtOffset(START_X, START_Y);
        content.showText(title);
        content.newLineAtOffset(0, -LINE_SPACING * 2);

        content.setFont(PDType1Font.HELVETICA, 11);
        
        return content;
    }

    /**
     * Agrega el encabezado común a todos los reportes
     */
    private void addHeader(PDPageContentStream content, String admin, 
                           String startDate, String endDate) throws IOException {
        content.showText("Administrador: " + admin);
        content.newLineAtOffset(0, -LINE_SPACING);
        
        content.showText("Período: " + startDate + " - " + endDate);
        content.newLineAtOffset(0, -LINE_SPACING);
        
        content.showText("Fecha de generación: " + 
            java.time.LocalDateTime.now().format(dateFormatter));
        content.newLineAtOffset(0, -LINE_SPACING * 2);
    }

    // ===============================
    // INCOME REPORT
    // ===============================
    public void generateIncomePDF(IncomeReport report, String fileName) throws IOException {
        try (PDDocument document = new PDDocument()) {

            PDPageContentStream content = createDocument(document, "REPORTE DE INGRESOS POR PARQUEO");

            // Encabezado
            addHeader(content, 
                report.getAdministratorName(),
                report.getEntryTime().format(dateFormatter),
                report.getExitTime().format(dateFormatter)
            );

            // Detalle de ingresos
            content.setFont(PDType1Font.HELVETICA_BOLD, 11);
            content.showText("PARQUEO                     INGRESOS");
            content.newLineAtOffset(0, -LINE_SPACING);
            content.setFont(PDType1Font.HELVETICA, 11);

            for (int i = 0; i < report.getParkingLotNames().size(); i++) {
                String line = String.format("%-30s CRC%,.2f", 
                    report.getParkingLotNames().get(i),
                    report.getIncomes().get(i));
                content.showText(line);
                content.newLineAtOffset(0, -LINE_SPACING);
            }

            content.newLineAtOffset(0, -LINE_SPACING);
            content.setFont(PDType1Font.HELVETICA_BOLD, 11);
            String totalLine = String.format("%-30s CRC%,.2f", "TOTAL:", report.getTotal());
            content.showText(totalLine);

            content.endText();
            content.close();
            document.save(fileName);
        }
    }

    // ===============================
    // VEHICLE QUANTITY REPORT
    // ===============================
    public void generateVehicleQuantityPDF(VehicleQuantityReport report, String fileName) throws IOException {
        try (PDDocument document = new PDDocument()) {

            PDPageContentStream content = createDocument(document, "REPORTE DE CANTIDAD DE VEHÍCULOS POR TIPO");

            // Encabezado
            addHeader(content,
                report.getAdministratorName(),
                report.getEntryTime().format(dateFormatter),
                report.getExitTime().format(dateFormatter)
            );

            // Detalle
            content.setFont(PDType1Font.HELVETICA_BOLD, 11);
            content.showText("TIPO DE VEHÍCULO           CANTIDAD");
            content.newLineAtOffset(0, -LINE_SPACING);
            content.setFont(PDType1Font.HELVETICA, 11);

            for (int i = 0; i < report.getVehicleTypes().size(); i++) {
                String line = String.format("%-25s %d", 
                    report.getVehicleTypes().get(i),
                    report.getQuantity().get(i));
                content.showText(line);
                content.newLineAtOffset(0, -LINE_SPACING);
            }

            content.endText();
            content.close();
            document.save(fileName);
        }
    }

    // ===============================
    // COMMON TYPE REPORT
    // ===============================
    public void generateCommonTypePDF(CommonTypeReport report, String fileName) throws IOException {
        try (PDDocument document = new PDDocument()) {

            PDPageContentStream content = createDocument(document,
                    "REPORTE DE TIPO DE VEHÍCULO MÁS COMÚN POR PARQUEO");

            // Encabezado
            addHeader(content,
                report.getAdministratorName(),
                report.getEntryTime().format(dateFormatter),
                report.getExitTime().format(dateFormatter)
            );

            // Detalle
            content.setFont(PDType1Font.HELVETICA_BOLD, 11);
            content.showText("PARQUEO                    TIPO MÁS COMÚN");
            content.newLineAtOffset(0, -LINE_SPACING);
            content.setFont(PDType1Font.HELVETICA, 11);

            for (int i = 0; i < report.getParkingLots().size(); i++) {
                String line = String.format("%-30s %s",
                    report.getParkingLots().get(i),
                    report.getCommonVehicleType().get(i));
                content.showText(line);
                content.newLineAtOffset(0, -LINE_SPACING);
            }

            content.endText();
            content.close();
            document.save(fileName);
        }
    }
}
