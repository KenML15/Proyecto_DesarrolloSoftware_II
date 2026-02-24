/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.CustomerFileController;
import controller.VehicleFileController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.entities.Customer;
import model.entities.Space;
import model.entities.Vehicle;
import org.jdom2.JDOMException;

/**
 *
 * @author 50687
 */
public class VehicleManagement extends BaseInternalFrame {

    private VehicleFileController vehicleController;
    private CustomerFileController customerController;
    private JTable vehicleTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;

    private final String[] COLUMNS = {"Placa", "Color", "Marca", "Modelo", "Tipo", "Clientes", "Espacio", "Estado", "Entrada"};

    public VehicleManagement() {
        super("GESTIÓN DE VEHÍCULOS");
        initControllers();
        createInterface();
        loadAllVehicles();

        styleTable(vehicleTable);
    }

    //Inicializar controladores
    private void initControllers() {
        try {
            vehicleController = new VehicleFileController();
            customerController = new CustomerFileController();
        } catch (IOException e) {
            showError("No se pueden acceder a los archivos de los clientes y vehículos" + e.getMessage());
            dispose();
        } catch (JDOMException ex) {
            Logger.getLogger(VehicleManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Crear interfaz
    private void createInterface() {
        setSize(1000, 500);
        setLocation(50, 50);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        mainPanel.add(createSearchPanel(), BorderLayout.NORTH);
        mainPanel.add(createTablePanel(), BorderLayout.CENTER);
        mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    //Panel de búsqueda
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        panel.setBackground(backgroundColor); //Gris muy claro

        JLabel lblSearch = new JLabel("Buscar vehículo:");
        lblSearch.setFont(labelFont);
        panel.add(lblSearch);

        searchField = new JTextField(20);
        styleTextField(searchField); //Borde azul y padding
        panel.add(searchField);

        JButton searchButton = new JButton("BUSCAR");
        styleButton(searchButton);
        searchButton.addActionListener(e -> searchVehicles());
        panel.add(searchButton);

        JButton clearButton = new JButton("MOSTRAR TODOS");
        styleButton(clearButton);
        clearButton.setBackground(new Color(127, 140, 141)); //Gris para diferenciar
        clearButton.addActionListener(e -> loadAllVehicles());
        panel.add(clearButton);

        return panel;
    }

    //Panel de tabla
    private JScrollPane createTablePanel() {
        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        vehicleTable = new JTable(tableModel);
        vehicleTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        vehicleTable.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setBackground(primaryColor); //Azul oscuro
                label.setForeground(Color.WHITE);   //Texto blanco
                label.setFont(new Font("Segoe UI", Font.BOLD, 12));
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setOpaque(true);
                label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.WHITE));
                return label;
            }
        });

        JScrollPane scroll = new JScrollPane(vehicleTable);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        scroll.getViewport().setBackground(Color.WHITE);
        return scroll;
    }

    //Panel de botones
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        panel.setBackground(backgroundColor);

        JButton btnNew = createActionButton("NUEVO VEHÍCULO", primaryColor, e -> openNewVehicle());
        JButton btnEdit = createActionButton("EDITAR", new Color(39, 174, 96), e -> editSelectedVehicle()); //Verde
        JButton btnDelete = createActionButton("ELIMINAR", new Color(192, 57, 43), e -> deleteSelectedVehicle()); //Rojo
        JButton btnRefresh = createActionButton("ACTUALIZAR", new Color(52, 152, 219), e -> loadAllVehicles()); //Azul claro

        panel.add(btnNew);
        panel.add(btnEdit);
        panel.add(btnDelete);
        panel.add(btnRefresh);

        return panel;
    }

    private JButton createActionButton(String text, Color bg, ActionListener action) {
        JButton btn = new JButton(text);
        styleButton(btn);
        btn.setBackground(bg);
        btn.addActionListener(action);
        return btn;
    }

    //Cargar todos los vehículos
    private void loadAllVehicles() {
        try {
            ArrayList<Vehicle> vehicles = vehicleController.getAll();
            showVehiclesInTable(vehicles);
            searchField.setText("");
        } catch (IOException e) {
            showError("No se pudo cargar la lista de vehículos" + e.getMessage());
        }
    }

    //Mostrar vehículos en tabla
    private void showVehiclesInTable(ArrayList<Vehicle> vehicles) {
        tableModel.setRowCount(0);
        for (Vehicle vehicle : vehicles) {
            addVehicleToTable(vehicle);
        }
    }

    //Agregar vehículo a tabla
    private void addVehicleToTable(Vehicle vehicle) {
        String typeName = "No especificado";
        if (vehicle.getVehicleType() != null) {
            typeName = vehicle.getVehicleType().getDescription();
        }
        
        String status = vehicle.getExitTime() == null ? "PARQUEADO" : "FUERA";

        Object[] row = {
            vehicle.getPlate(),
            vehicle.getColor(),
            vehicle.getBrand(),
            vehicle.getModel(),
            typeName,
            formatCustomerNames(vehicle.getCustomer()),
            formatSpace(vehicle.getSpace()),
            status,
            formatDateTime(vehicle.getEntryTime())
        };

        tableModel.addRow(row);
    }

    //Formatear nombres de clientes
    private String formatCustomerNames(ArrayList<Customer> customers) {
        if (customers == null || customers.isEmpty()) {
            return "Sin clientes";
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < customers.size(); i++) {
            stringBuilder.append(customers.get(i).getName());
            if (i < customers.size() - 1) {
                stringBuilder.append(", ");
            }
        }
        return stringBuilder.toString();
    }
    
    //Formatear espacios
    private String formatSpace(Space space) {
        if (space == null) {
            return "N/A";
        }
        return String.valueOf(space.getId());
    }
    
    //Formatear fecha/hora
    private String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return dateTime.format(formatter);
    }

    //Buscar vehículos
    private void searchVehicles() {
        String term = searchField.getText().trim();
        if (term.isEmpty()) {
            loadAllVehicles();
            return;
        }

        try {
            ArrayList<Vehicle> results = new ArrayList<>();

            for (Vehicle vehicle : vehicleController.getAll()) {
                if (vehicleMatchesSearch(vehicle, term)) {
                    results.add(vehicle);
                }
            }

            showVehiclesInTable(results);
        } catch (IOException e) {
            showError("No se pudo realizar la busqueda de vehículos" + e.getMessage());
        }
    }

    //Verificar si vehículo coincide con búsqueda
    private boolean vehicleMatchesSearch(Vehicle vehicle, String term) {
        String searchTerm = term.toLowerCase();

        if (vehicle.getPlate().toLowerCase().contains(searchTerm)) {
            return true;
        }
        if (vehicle.getBrand().toLowerCase().contains(searchTerm)) {
            return true;
        }
        if (vehicle.getModel().toLowerCase().contains(searchTerm)) {
            return true;
        }

        if (vehicle.getVehicleType() != null) {
            if (vehicle.getVehicleType().getDescription().toLowerCase().contains(searchTerm)) {
                return true;
            }
        }

        return false;
    }

    //Abrir nuevo vehículo
    private void openNewVehicle() {
        openVehicleWindow(null);
    }

    //Editar vehículo seleccionado
    private void editSelectedVehicle() {
        int row = vehicleTable.getSelectedRow();
        if (row < 0) {
            showWarning("Seleccione un vehículo");
            return;
        }

        String plate = (String) tableModel.getValueAt(row, 0);
        try {
            Vehicle vehicle = vehicleController.getByPlate(plate);
            if (vehicle != null) {
                openVehicleWindow(vehicle);
            }
        } catch (IOException e) {
            showError("Error al intentar editar la información del vehículo" + e.getMessage());
        }
    }

    //Eliminar vehículo seleccionado
    private void deleteSelectedVehicle() {
        int row = vehicleTable.getSelectedRow();
        if (row < 0) {
            showWarning("Seleccione un vehículo");
            return;
        }

        String plate = (String) tableModel.getValueAt(row, 0);

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Eliminar vehículo " + plate + "?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                vehicleController.delete(plate);
                showInfo("Vehículo eliminado");
                loadAllVehicles();
            } catch (IOException | IllegalArgumentException e) {
                showError("No se pudo eliminar el vehículo" + e.getMessage());
            }
        }
    }

    //Abrir ventana de vehículo
    private void openVehicleWindow(Vehicle vehicle) {
        try {
            VehicleWindow window = (vehicle == null) ? new VehicleWindow() : new VehicleWindow(vehicle);

            // Obtenemos el desktop de forma segura
            JDesktopPane desktop = getDesktopPane();

            if (desktop != null) {
                desktop.add(window);
                window.setVisible(true); // ¡IMPORTANTE!
                window.toFront();
                window.setSelected(true);

                window.addInternalFrameListener(new InternalFrameAdapter() {
                    @Override
                    public void internalFrameClosed(InternalFrameEvent e) {
                        loadAllVehicles();
                    }
                });
            } else {
                // Si el desktop es null, algo va mal con la jerarquía
                System.err.println("Error: No se encontró el DesktopPane");
            }
        } catch (PropertyVetoException e) {
            showError("No se pudo abrir la pantalla de registro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //Mostrar error
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    //Mostrar advertencia
    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }

    //Mostrar información
    private void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Información", JOptionPane.INFORMATION_MESSAGE);
    }
}
