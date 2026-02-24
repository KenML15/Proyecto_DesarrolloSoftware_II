/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.ReportsController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import org.jdom2.JDOMException;

/**
 *
 * @author 50687
 */
public class ReportsWindow extends BaseInternalFrame implements ActionListener{
   
    private JTextField txtStartDate, txtStartTime, txtEndDate, txtEndTime;
    private JTextField txtAdminName;
    private JButton btnGenerateJSON, btnGeneratePDF, btnCancel;
    
    //Reporte 1: Ingresos
    private JRadioButton rbIncome;
    
    //Reporte 2: Cantidad de vehículos según su tipo de vehículo
    private JRadioButton rbVehicleQuantity;
    
    //Reporte 3: Tipo de vehículo más común
    private JRadioButton rbCommonType;
    
    private ButtonGroup reportGroup;

    private ReportsController reportsController;
    private String currentUser;
    
    //Formato de fechas
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    
    public ReportsWindow(String userName) {
        super("GENERAR REPORTES");
        this.currentUser = userName;
        SwingUtilities.invokeLater(() -> centerInDesktop());
        
        try {
            this.reportsController = new ReportsController();
        } catch (IOException | JDOMException e) {
            showError("Error: " + e.getMessage());
        }
        
        initUI();
    }
    
    private void initUI() {
        setSize(700, 550);
        setLocation(150, 50);
        setLayout(new BorderLayout(10, 10));
        
        //Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        //Panel de selección de reporte
        mainPanel.add(createReportSelectionPanel(), BorderLayout.NORTH);
        
        //Panel de fechas y administrador
        mainPanel.add(createDateTimePanel(), BorderLayout.CENTER);
        
        //Panel de botones
        mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);
        
        add(mainPanel);
    }

    //Panel para seleccionar el tipo de reporte
    private JPanel createReportSelectionPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(primaryColor),
            " SELECCIONE EL TIPO DE REPORTE ",
            TitledBorder.DEFAULT_JUSTIFICATION,
            TitledBorder.DEFAULT_POSITION,
            new Font("Segoe UI", Font.BOLD, 14),
            primaryColor
        ));
        
        //Crear radio buttons para que solamente tenga que seleccionar
        rbIncome = new JRadioButton("Reporte de ingresos por parqueo");
        rbVehicleQuantity = new JRadioButton("Reporte de cantidad de vehículos por tipo");
        rbCommonType = new JRadioButton("Reporte de tipo de vehículo más común por parqueo");
        
        //Estilo
        Font radioFont = new Font("Segoe UI", Font.PLAIN, 13);
        rbIncome.setFont(radioFont);
        rbVehicleQuantity.setFont(radioFont);
        rbCommonType.setFont(radioFont);
        
        rbIncome.setBackground(Color.WHITE);
        rbVehicleQuantity.setBackground(Color.WHITE);
        rbCommonType.setBackground(Color.WHITE);
        
        //Grupo de botones
        reportGroup = new ButtonGroup();
        reportGroup.add(rbIncome);
        reportGroup.add(rbVehicleQuantity);
        reportGroup.add(rbCommonType);
        
        //Seleccionar primero por defecto
        rbIncome.setSelected(true);
        
        panel.add(rbIncome);
        panel.add(rbVehicleQuantity);
        panel.add(rbCommonType);
        
        return panel;
    }
    
    //Panel para ingresar fechas y administrador
    private JPanel createDateTimePanel() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 15, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(primaryColor),
                " PERÍODO DEL REPORTE ",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
        new Font("Segoe UI", Font.BOLD, 14),
            primaryColor
        ));
        
        //Fecha inicio, osea, desde qué día se tiene que generar el reporte
        panel.add(createStyledLabel("Fecha inicio (dd/mm/aaaa):"));
        txtStartDate = new JTextField(LocalDate.now().minusMonths(1).format(dateFormatter));
        styleTextField(txtStartDate);
        panel.add(txtStartDate);
        
        //Hora inicio, desde qué hora se va a genrar el reporte
        panel.add(createStyledLabel("Hora inicio (hh:mm):"));
        txtStartTime = new JTextField("00:00");
        styleTextField(txtStartTime);
        panel.add(txtStartTime);
        
        //Fecha fin, osea, el fin del periodo de tiempo
        panel.add(createStyledLabel("Fecha fin (dd/mm/aaaa):"));
        txtEndDate = new JTextField(LocalDate.now().format(dateFormatter));
        styleTextField(txtEndDate);
        panel.add(txtEndDate);
        
        //Hora final
        panel.add(createStyledLabel("Hora fin (hh:mm):"));
        txtEndTime = new JTextField("23:59");
        styleTextField(txtEndTime);
        panel.add(txtEndTime);
        
        //Asignamos el admin de forma automática
        panel.add(createStyledLabel("Generado por:"));
        txtAdminName = new JTextField(currentUser);
        txtAdminName.setEditable(false);
        txtAdminName.setBackground(new Color(240, 240, 240));
        txtAdminName.setFont(new Font("Segoe UI", Font.BOLD, 12));
        styleTextField(txtAdminName);
        panel.add(txtAdminName);
        
        panel.add(new JLabel(""));
        panel.add(new JLabel(""));
        
        return panel;
    }
    
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        return label;
    }
    
    //Panel de botones
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setBackground(Color.WHITE);
        
        btnGenerateJSON = new JButton("GENERAR JSON");
        btnGeneratePDF = new JButton("GENERAR PDF");
        btnCancel = new JButton("CANCELAR");
        
        styleButton(btnGenerateJSON);
        styleButton(btnGeneratePDF);
        styleButton(btnCancel);
        
        btnGenerateJSON.setBackground(new Color(39, 174, 96)); //Verde
        btnGeneratePDF.setBackground(new Color(41, 128, 185)); //Azul
        btnCancel.setBackground(new Color(192, 57, 43)); //Rojo
        
        btnGenerateJSON.addActionListener(this);
        btnGeneratePDF.addActionListener(this);
        btnCancel.addActionListener(e -> dispose());
        
        panel.add(btnGenerateJSON);
        panel.add(btnGeneratePDF);
        panel.add(btnCancel);
        
        return panel;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnGenerateJSON) {
            generateReport("json");
        } else if (e.getSource() == btnGeneratePDF) {
            generateReport("pdf");
        }
    }

    //Genera el reporte en el formato especificado
    private void generateReport(String format) {
        try {
            //Validar que haya seleccionado un reporte
            if (!rbIncome.isSelected() && !rbVehicleQuantity.isSelected() && !rbCommonType.isSelected()) {
                showError("Debe seleccionar un tipo de reporte");
                return;
            }
            
            //Validar fechas
            LocalDateTime start = validateAndParseDateTime(
                txtStartDate.getText().trim(),
                txtStartTime.getText().trim(),
                "fecha inicio"
            );
            
            LocalDateTime end = validateAndParseDateTime(
                txtEndDate.getText().trim(),
                txtEndTime.getText().trim(),
                "fecha fin"
            );
            
            if (start.isAfter(end)) {
                showError("La fecha de inicio no puede ser después de la fecha fin");
                return;
            }
            
            //Generar nombre de archivo
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName;
            
            //Determinar tipo de reporte y generar
            if (rbIncome.isSelected()) {
                fileName = "ingresos_" + timestamp + "." + format;
                if (format.equals("json")) {
                    reportsController.saveIncomeReport(start, end, fileName, currentUser);
                } else {
                    reportsController.exportIncomeReportToPDF(start, end, fileName, currentUser);
                }
                showSuccess("Reporte de ingresos", fileName, format);
                
            } else if (rbVehicleQuantity.isSelected()) {
                fileName = "vehiculos_" + timestamp + "." + format;
                if (format.equals("json")) {
                    reportsController.saveVehicleQuantityReport(start, end, fileName, currentUser);
                } else {
                    reportsController.exportVehicleQuantityToPDF(start, end, fileName, currentUser);
                }
                showSuccess("Reporte de cantidad de vehículos", fileName, format);
                
            } else if (rbCommonType.isSelected()) {
                fileName = "tipo_comun_" + timestamp + "." + format;
                if (format.equals("json")) {
                    reportsController.saveCommonTypeReport(start, end, fileName, currentUser);
                } else {
                    reportsController.exportCommonTypeToPDF(start, end, fileName, currentUser);
                }
                showSuccess("Reporte de tipo más común", fileName, format);
            }
            
        } catch (DateTimeParseException ex) {
            showError("Formato de fecha u hora inválido. Use dd/mm/aaaa y hh:mm");
        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage());
        } catch (IOException ex) {
            showError("Error al generar el reporte: " + ex.getMessage());
        }
    }
    
    //Valida y convierte fecha y hora a LocalDateTime
    private LocalDateTime validateAndParseDateTime(String date, String time, String fieldName) 
            throws DateTimeParseException {
        
        LocalDate parsedDate = LocalDate.parse(date, dateFormatter);
        LocalTime parsedTime = LocalTime.parse(time, timeFormatter);
        
        return LocalDateTime.of(parsedDate, parsedTime);
    }

    //Muestra mensaje de éxito y pregunta si desea abrir la carpeta del directorio de reportes
    private void showSuccess(String reportType, String fileName, String format) {
        String message = reportType + " generado exitosamente.\n\n" +
                        "Archivo: reports/" + fileName + "\n\n" +
                        "¿Desea abrir la carpeta de reports?";
        
        int option = JOptionPane.showConfirmDialog(this, message, "Reporte Generado", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
        
        if (option == JOptionPane.YES_OPTION) {
            try {
                Desktop.getDesktop().open(new File("reports/"));
            } catch (IOException e) {
                showError("No se pudo abrir la carpeta");
            }
        }
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
