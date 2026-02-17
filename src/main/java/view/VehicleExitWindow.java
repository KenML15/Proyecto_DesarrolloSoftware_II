/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.ParkingLotFileController;
import controller.VehicleFileController;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
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
public class VehicleExitWindow extends JInternalFrame {

    private VehicleFileController vehicleController;
    private ParkingLotFileController parkingLotController;

    private JTextField plateField;
    private JButton searchButton, exitButton, cancelButton;
    private JLabel plateLabel, parkingLotLabel, clientsLabel, entryTimeLabel;

    private Vehicle currentVehicle;
    private ParkingLot selectedParkingLot;

    public VehicleExitWindow(ParkingLot parkingLot){
        super("Registrar salida de vehículo - " + parkingLot.getName(), true, true, true, true);
        this.selectedParkingLot = parkingLot;
        initControllers();
        initUI();
        
        parkingLotLabel.setText(parkingLot.getName());
    }
    public VehicleExitWindow() {
        super("Registrar Salida de Vehículo", true, true, true, true);
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
        setSize(500, 350);
        setLocation(200, 150);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel de búsqueda
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Placa del Vehículo:"));
        plateField = new JTextField(15);
        searchPanel.add(plateField);
        searchButton = new JButton("Buscar");
        searchButton.addActionListener(e -> searchVehicle());
        searchPanel.add(searchButton);
        mainPanel.add(searchPanel, BorderLayout.NORTH);

        // Panel de información
        JPanel infoPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Información del Vehículo"));

        infoPanel.add(new JLabel("Placa:"));
        plateLabel = new JLabel("-");
        infoPanel.add(plateLabel);

        infoPanel.add(new JLabel("Parqueo:"));
        parkingLotLabel = new JLabel("-");
        infoPanel.add(parkingLotLabel);

        infoPanel.add(new JLabel("Clientes a bordo:"));
        clientsLabel = new JLabel("-");
        infoPanel.add(clientsLabel);

        infoPanel.add(new JLabel("Hora de Entrada:"));
        entryTimeLabel = new JLabel("-");
        infoPanel.add(entryTimeLabel);

        mainPanel.add(infoPanel, BorderLayout.CENTER);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout());
        exitButton = new JButton("Registrar Salida y Facturar");
        exitButton.setEnabled(false);
        exitButton.addActionListener(e -> registerExit());

        cancelButton = new JButton("Cancelar");
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(exitButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    private void searchVehicle() {
        String plate = plateField.getText().trim();
        if (plate.isEmpty()) {
            showWarning("Ingrese una placa para buscar");
            return;
        }

        try {
            Vehicle vehicle = null;//vehicleController.getByPlate(plate);
            if (selectedParkingLot != null) {
                vehicle = parkingLotController.findVehicleInParkingLot(selectedParkingLot.getId(), plate);
            }else{
                vehicle = vehicleController.getByPlate(plate);
            }

            if (vehicle == null){
                showError("No se encontró el vehículo en el parqueo seleccionado");
                resetInfo();
                return;
            }
            
            
            // Vehículo encontrado y activo
            currentVehicle = vehicle;

            // Obtener nombre del parqueo
            String parkingLotName = "Desconocido";
            try {
                var lot = parkingLotController.findParkingLotByVehicle(vehicle);
                if (lot != null) {
                    parkingLotName = lot.getName();
                }
            } catch (IOException e) {
                // Ignorar, ya tenemos valor por defecto
            }

            // Formatear clientes
            String clients = "Sin clientes";
            if (vehicle.getCustomer() != null && !vehicle.getCustomer().isEmpty()) {
                clients = vehicle.getCustomer().stream()
                        .map(c -> c.getName())
                        .reduce((a, b) -> a + ", " + b)
                        .orElse("Sin clientes");
            }

            String entryTime = vehicle.getEntryTime()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

            // Actualizar labels
            plateLabel.setText(vehicle.getPlate());
            parkingLotLabel.setText(parkingLotName);
            clientsLabel.setText(clients);
            entryTimeLabel.setText(entryTime);

            exitButton.setEnabled(true);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al buscar el vehículo: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
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
        
        //Por probar
//        if (currentVehicle.getVehicleType() == null) {
//            JOptionPane.showMessageDialog(this,
//                    "El vehículo con placa " + currentVehicle.getPlate()
//                    + " no tiene un tipo de vehículo asignado.\n\n"
//                    + "Por favor, edite el vehículo y asígnele un tipo antes de registrar su salida.",
//                    "Error de Configuración",
//                    JOptionPane.ERROR_MESSAGE);
//            return;
//        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Registrar salida del vehículo " + currentVehicle.getPlate() + "?",
                "Confirmar Salida",
                JOptionPane.YES_NO_OPTION);

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

                // Limpiar formulario
                resetInfo();
                plateField.setText("");

            } catch (IOException e) {
                showError( "Error al registrar la salida: " + e.getMessage());
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
