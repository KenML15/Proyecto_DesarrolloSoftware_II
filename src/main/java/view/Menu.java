/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.FeeController;
import controller.ParkingLotFileController;
import controller.VehicleFileController;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import model.entities.Customer;
import model.entities.ParkingLot;

/**
 *
 * @author 50687
 */
public class Menu extends JFrame {

    public Menu() {
        super("Sistema de Gestión de Parqueos");
        setupMainWindow();
    }
    
    //Configurar ventana principal
    private void setupMainWindow() {
        HomeDesktop desktop = new HomeDesktop();
        this.add(desktop);
        
        this.setSize(1000, 700);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);//Centra la ventana principal
        
        createMenuBar(desktop);
        setResizable(false);
    
        this.setVisible(true);
    }
    
    //Crear barra de menú
    private void createMenuBar(HomeDesktop desktop) {
        JMenuBar menuBar = new JMenuBar();
        
        menuBar.add(createCustomerMenu(desktop));
        menuBar.add(createVehicleMenu(desktop));
        menuBar.add(createParkingMenu(desktop));
        menuBar.add(createFeeMenu(desktop));
//        menuBar.add(createReportsMenu());
        
        this.setJMenuBar(menuBar);
    }
    
    //Menú de clientes
    private JMenu createCustomerMenu(HomeDesktop desktop) {
        JMenu menu = new JMenu("Clientes");
        
        JMenuItem insertItem = createMenuItem("Insertar Cliente", e -> openCustomerWindow(desktop, null));
        JMenuItem manageItem = createMenuItem("Gestionar Clientes", e -> openCustomerManagement(desktop));
        
        menu.add(insertItem);
        menu.add(manageItem);
        
        return menu;
    }
    
    //Menú de vehículos
    private JMenu createVehicleMenu(HomeDesktop desktop) {
        JMenu menu = new JMenu("Vehículos");
        
        JMenuItem insertItem = createMenuItem("Insertar Vehículo", e -> openVehicleWindow(desktop, null));
        JMenuItem manageItem = createMenuItem("Gestionar Vehículos", e -> openVehicleManagement(desktop));
        JMenuItem exitItem = createMenuItem("Registrar Salida", e -> openVehicleExit(desktop));
        
        menu.add(insertItem);
        menu.add(manageItem);
        menu.add(exitItem);
        
        return menu;
    }
    
    //Menú de parqueos
    private JMenu createParkingMenu(HomeDesktop desktop) {
        JMenu menu = new JMenu("Parqueos");
        
        JMenuItem createItem = createMenuItem("Crear Parqueo", e -> openParkingLotWindow(desktop));
        JMenuItem manageItem = createMenuItem("Gestionar Parqueos", e -> openParkingLotManagement(desktop));
        JMenuItem configItem = createMenuItem("Configurar Espacios", e -> openSpaceConfiguration(desktop));
        
        menu.add(createItem);
        menu.add(manageItem);
        menu.add(configItem);
        
        return menu;
    }
    
    //Menú de tarifas
    private JMenu createFeeMenu(HomeDesktop desktop) {
        JMenu menu = new JMenu("Tarifas");
        
        JMenuItem manageItem = createMenuItem("Gestionar Tarifas", e -> openFeeManagement(desktop));
        
        menu.add(manageItem);
        
        return menu;
    }
    
    //Menú de reportes
//    private JMenu createReportsMenu() {
//        JMenu menu = new JMenu("Reportes");
//        
//        JMenuItem vehicleReport = createMenuItem("Reporte de Vehículos", 
//            e -> showMessage("Generando reporte de vehículos...", "Reporte"));
//        
//        JMenuItem customerReport = createMenuItem("Reporte de Clientes", 
//            e -> showMessage("Generando reporte de clientes...", "Reporte"));
//        
//        menu.add(vehicleReport);
//        menu.add(customerReport);
//        
//        return menu;
//    }
    
    //Crear item de menú genérico
    private JMenuItem createMenuItem(String text, java.awt.event.ActionListener action) {
        JMenuItem item = new JMenuItem(text);
        item.addActionListener(action);
        return item;
    }
    
    //Abrir ventana de cliente (insertar o editar)
    private void openCustomerWindow(HomeDesktop desktop, Customer customer) {
        try {
            CustomerWindow customerWindow = (customer == null) ? new CustomerWindow() : new CustomerWindow(customer);
            addWindowToDesktop(desktop, customerWindow);
        } catch (Exception e) {
            showError("Error: " + e.getMessage());
        }
    }
    
    //Abrir gestión de clientes
    private void openCustomerManagement(HomeDesktop desktop) {
        try {
            CustomerManagement customerManagement = new CustomerManagement();
            addWindowToDesktop(desktop, customerManagement);
        } catch (Exception e) {
            showError("Error: " + e.getMessage());
        }
    }
    
    //Abrir ventana de vehículo (insertar o editar)
    private void openVehicleWindow(HomeDesktop desktop, model.entities.Vehicle vehicle) {
        try {
            VehicleWindow vehicleWindow = (vehicle == null) ? new VehicleWindow() : new VehicleWindow(vehicle);
            addWindowToDesktop(desktop, vehicleWindow);
        } catch (Exception e) {
            showError("Error: " + e.getMessage());
        }
    }
    
    //Abrir gestión de vehículos
    private void openVehicleManagement(HomeDesktop desktop) {
        try {
            VehicleManagement vehicleManagement = new VehicleManagement();
            addWindowToDesktop(desktop, vehicleManagement);
        } catch (Exception e) {
            showError("Error: " + e.getMessage());
        }
    }
    
    //Abre ventana para la salida de vehículos
    private void openVehicleExit(HomeDesktop desktop) {
        try {
            VehicleWindow window = new VehicleWindow();
            addWindowToDesktop(desktop, window);
        } catch (Exception e) {
            showError("Error: " + e.getMessage());
        }
    }
    
    //Abrir ventana de parqueo
    private void openParkingLotWindow(HomeDesktop desktop) {
        try {
            ParkingLotFileController parkingLotController = new ParkingLotFileController();
            ParkingLotWindow window = new ParkingLotWindow(parkingLotController);
            addWindowToDesktop(desktop, window);
        } catch (Exception e) {
            showError("Error: " + e.getMessage());
        }
    }
    
    //Abrir gestión de parqueos
    private void openParkingLotManagement(HomeDesktop desktop) {
        try {
            ParkingLotManagement parkingLotWindow = new ParkingLotManagement();
            addWindowToDesktop(desktop, parkingLotWindow);
        } catch (Exception e) {
            showError("Error: " + e.getMessage());
        }
    }
    
    //Abre la ventana para manejar las tarifas
    private void openFeeManagement(HomeDesktop desktop) {
        try {
            FeeManagement feeWindow = new FeeManagement();
            addWindowToDesktop(desktop, feeWindow);
        } catch (Exception e) {
            showError("Error: " + e.getMessage());
        }
    }
    
//    private void showParkedVehiclesReport(HomeDesktop desktop) {
//        try {
//            ParkingLotFileController parkingLotController = new ParkingLotFileController();
//            String report = parkingLotController.getParkedVehiclesReport();
//            
//            JTextArea textArea = new JTextArea(20, 60);
//            textArea.setText(report);
//            textArea.setEditable(false);
//            
//            JScrollPane scrollPane = new JScrollPane(textArea);
//            
//            JDialog dialog = new JDialog(this, "Vehículos Estacionados", true);
//            dialog.add(scrollPane);
//            dialog.pack();
//            dialog.setLocationRelativeTo(this);
//            dialog.setVisible(true);
//            
//        } catch (Exception e) {
//            showError("Error: " + e.getMessage());
//        }
//    }
    
    private void showFeeReport(HomeDesktop desktop) {
        try {
            FeeController controller = new FeeController();
            StringBuilder report = new StringBuilder();
            report.append("=== REPORTE DE TARIFAS ===\n\n");
            
            ArrayList<model.entities.Fee> fees = controller.getAllFees();
            for (model.entities.Fee fee : fees) {
                report.append("Tipo: ").append(fee.getVehicleType()).append("\n");
                report.append("  Media Hora: ₡").append(String.format("%.2f", fee.getHalfHourRate())).append("\n");
                report.append("  Hora: ₡").append(String.format("%.2f", fee.getHourlyRate())).append("\n");
                report.append("  Día: ₡").append(String.format("%.2f", fee.getDailyRate())).append("\n");
                report.append("  Semana: ₡").append(String.format("%.2f", fee.getWeeklyRate())).append("\n");
                report.append("  Mes: ₡").append(String.format("%.2f", fee.getMonthlyRate())).append("\n");
                report.append("  Año: ₡").append(String.format("%.2f", fee.getAnnualRate())).append("\n");
                report.append("---\n");
            }
            
            JTextArea textArea = new JTextArea(20, 60);
            textArea.setText(report.toString());
            textArea.setEditable(false);
            
            JScrollPane scrollPane = new JScrollPane(textArea);
            
            JDialog dialog = new JDialog(this, "Reporte de Tarifas", true);
            dialog.add(scrollPane);
            dialog.pack();
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
            
        } catch (Exception e) {
            showError("Error: " + e.getMessage());
        }
    }
    
    //Abrir configuración de espacios
    private void openSpaceConfiguration(HomeDesktop desktop) {
        try {
            ParkingLotFileController controller = new ParkingLotFileController();
            selectParkingLotForConfig(desktop, controller);
        } catch (Exception e) {
            showError("Error: " + e.getMessage());
        }
    }
    
    //Seleccionar parqueo para configuración
    private void selectParkingLotForConfig(HomeDesktop desktop, ParkingLotFileController controller) {
        ArrayList<ParkingLot> parkingLots = controller.getAllParkingLots();
        
        if (parkingLots.isEmpty()) {
            showMessage("Cree un parqueo primero", "Información");
            return;
        }
        
        String[] options = createParkingLotOptions(parkingLots);
        String selected = showParkingLotSelectionDialog(options);
        
        if (selected != null) {
            ParkingLot selectedLot = findParkingLot(parkingLots, selected);
            if (selectedLot != null) {
                openSpaceConfigWindow(desktop, controller, selectedLot);
            }
        }
    }
    
    //Crear opciones de parqueos para diálogo
    private String[] createParkingLotOptions(ArrayList<ParkingLot> parkingLots) {
        String[] options = new String[parkingLots.size()];
        for (int i = 0; i < parkingLots.size(); i++) {
            ParkingLot lot = parkingLots.get(i);
            options[i] = lot.getName() + " (ID: " + lot.getId() + ")";
        }
        return options;
    }
    
    //Mostrar diálogo de selección de parqueo
    private String showParkingLotSelectionDialog(String[] options) {
        return (String) JOptionPane.showInputDialog(this,
            "Seleccione un parqueo para configurar:",
            "Configurar Espacios",
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]);
    }
    
    //Encontrar parqueo seleccionado
    private ParkingLot findParkingLot(ArrayList<ParkingLot> parkingLots, String selected) {
        for (ParkingLot lot : parkingLots) {
            if (selected.contains(String.valueOf(lot.getId()))) {
                return lot;
            }
        }
        return null;
    }
    
    //Abrir ventana de configuración de espacios
    private void openSpaceConfigWindow(HomeDesktop desktop, ParkingLotFileController controller, ParkingLot parkingLot) {
        try {
            SpaceConfigurationWindow window = new SpaceConfigurationWindow(parkingLot, controller);
            addWindowToDesktop(desktop, window);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error al configurar espacios");
        }
    }
    
    //Agregar ventana al escritorio (método reutilizable)
    private void addWindowToDesktop(HomeDesktop desktop, JInternalFrame window) {
        desktop.add(window);
        centerWindow(window, desktop);
        window.setVisible(true);
        window.toFront();
    }
    
    //Centrar ventana en el escritorio
    private void centerWindow(JInternalFrame window, HomeDesktop desktop) {
        int x = (desktop.getWidth() - window.getWidth()) / 2;
        int y = (desktop.getHeight() - window.getHeight()) / 2;
        window.setLocation(Math.max(0, x), Math.max(0, y));
    }
    
    //Mostrar mensaje de error
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    //Mostrar mensaje informativo
    private void showMessage(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
}
