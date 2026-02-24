/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.ParkingLotFileController;
import controller.VehicleFileController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import model.entities.Invoice;
import model.entities.ParkingLot;
import model.entities.Vehicle;
import org.jdom2.JDOMException;

/**
 *
 * @author 50687
 */
public class VehicleExitWindow extends BaseInternalFrame {

    private VehicleFileController vehicleController;
    private ParkingLotFileController parkingLotController;

    private JTextField plateField;
    private JButton searchButton, exitButton, cancelButton;
    private JLabel plateLabel, parkingLotLabel, clientsLabel, entryTimeLabel;

    private Vehicle currentVehicle;
    private ParkingLot selectedParkingLot;

    public VehicleExitWindow(ParkingLot parkingLot) {
        super("REGISTRAR SALIDA - " + parkingLot.getName());
        this.selectedParkingLot = parkingLot;
        initControllers();
        initUI();
        parkingLotLabel.setText(parkingLot.getName());
        parkingLotLabel.setForeground(primaryColor); //Resaltar el nombre del parqueo
    }

    public VehicleExitWindow() {
        super("REGISTRAR SALIDA DE VEHÍCULO");
        initControllers();
        initUI();
    }

    private void initControllers() {
        try {
            vehicleController = new VehicleFileController();
            parkingLotController = new ParkingLotFileController();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al acceder a los archivos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
        } catch (JDOMException ex) {
            Logger.getLogger(VehicleExitWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initUI() {
        setSize(550, 450);
        setLayout(new BorderLayout(15, 15));

        //Barra de busqueda
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        searchPanel.setBackground(backgroundColor);

        JLabel lblPlate = new JLabel("Placa del Vehículo:");
        lblPlate.setFont(labelFont);
        searchPanel.add(lblPlate);

        plateField = new JTextField(12);
        styleTextField(plateField);
        searchPanel.add(plateField);

        searchButton = new JButton("BUSCAR");
        styleButton(searchButton);
        searchButton.addActionListener(e -> searchVehicle());
        searchPanel.add(searchButton);

        add(searchPanel, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel(new GridLayout(4, 2, 10, 20));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(null, " DETALLES DE ESTANCIA ", 0, 0, labelFont, primaryColor),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        plateLabel = createDataLabel();
        parkingLotLabel = createDataLabel();
        clientsLabel = createDataLabel();
        entryTimeLabel = createDataLabel();

        addInfoRow(infoPanel, "Placa:", plateLabel);
        addInfoRow(infoPanel, "Parqueo:", parkingLotLabel);
        addInfoRow(infoPanel, "Clientes:", clientsLabel);
        addInfoRow(infoPanel, "Hora de Entrada:", entryTimeLabel);

        add(infoPanel, BorderLayout.CENTER);

        //botones de acción
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttonPanel.setBackground(backgroundColor);

        exitButton = new JButton("REGISTRAR SALIDA Y FACTURAR");
        styleButton(exitButton);
        exitButton.setBackground(new Color(39, 174, 96)); //Verde para éxito
        exitButton.setEnabled(false);
        exitButton.addActionListener(e -> registerExit());

        cancelButton = new JButton("CANCELAR");
        styleButton(cancelButton);
        cancelButton.setBackground(new Color(127, 140, 141));
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(exitButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    //Auxiliares para el diseño
    private JLabel createDataLabel() {
        JLabel label = new JLabel("-");
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return label;
    }

    private void addInfoRow(JPanel panel, String text, JLabel valueLabel) {
        JLabel title = new JLabel(text);
        title.setFont(labelFont);
        panel.add(title);
        panel.add(valueLabel);
    }

    private void searchVehicle() {
        String plate = plateField.getText().trim();
        if (plate.isEmpty()) {
            showWarning("Ingrese una placa para buscar");
            return;
        }

        try {
            Vehicle vehicle = null;
            if (selectedParkingLot != null) {
                vehicle = parkingLotController.findVehicleInParkingLot(selectedParkingLot.getId(), plate);
            } else {
                vehicle = vehicleController.getByPlate(plate);
            }

            if (vehicle == null) {
                showError("No se encontró el vehículo en el parqueo seleccionado");
                resetInfo();
                return;
            }

            //Vehículo encontrado y activo
            currentVehicle = vehicle;

            //Obtener nombre del parqueo
            String parkingLotName = "Desconocido";
            try {
                var lot = parkingLotController.findParkingLotByVehicle(vehicle);
                if (lot != null) {
                    parkingLotName = lot.getName();
                }
            } catch (IOException e) {
            }

            //Formatear clientes
            String clients = "Sin clientes";
            if (vehicle.getCustomer() != null && !vehicle.getCustomer().isEmpty()) {
                clients = vehicle.getCustomer().stream()
                        .map(c -> c.getName())
                        .reduce((a, b) -> a + ", " + b)
                        .orElse("Sin clientes");
            }

            String entryTime = vehicle.getEntryTime()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

            //Actualizar labels
            plateLabel.setText(vehicle.getPlate());
            parkingLotLabel.setText(parkingLotName);
            clientsLabel.setText(clients);
            entryTimeLabel.setText(entryTime);

            exitButton.setEnabled(true);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al buscar el vehículo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetInfo() {
        currentVehicle = null;
        plateLabel.setText("-");
        parkingLotLabel.setText("-");
        clientsLabel.setText("-");
        entryTimeLabel.setText("-");
        exitButton.setEnabled(false);
    }

    private void registerExit() {
        if (currentVehicle == null) {
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "¿Registrar salida del vehículo " + currentVehicle.getPlate() + "?", "Confirmar Salida", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Invoice invoice = vehicleController.registerVehicleExit(currentVehicle.getPlate());

                String message = String.format("""
                    ¡SALIDA REGISTRADA CON ÉXITO!
                    
                    ===== FACTURA =====
                    ID Factura: %d
                    Placa: %s
                    Parqueo: %s
                    
                    Hora Entrada: %s
                    Hora Salida: %s
                    
                    Clientes: %s
                    
                    TOTAL A PAGAR: ₡%.2f
                    """,
                        invoice.getId(),
                        invoice.getVehiclePlate(),
                        invoice.getParkingLotName(),
                        invoice.getEntryTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                        invoice.getExitTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                        clientsLabel.getText(),
                        invoice.getTotalAmount()
                );

                JOptionPane.showMessageDialog(this, message, "Factura", JOptionPane.INFORMATION_MESSAGE);

                //Limpiar formulario
                resetInfo();
                plateField.setText("");

            } catch (IOException e) {
                showError("Error al registrar la salida: " + e.getMessage());
            } catch (IllegalStateException e) {
                showError("Error: " + e.getMessage());
            }
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }
}
