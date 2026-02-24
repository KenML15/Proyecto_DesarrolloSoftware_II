/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.CustomerFileController;
import controller.ParkingLotFileController;
import controller.VehicleFileController;
import controller.VehicleTypeController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import model.entities.Customer;
import model.entities.ParkingLot;
import model.entities.Space;
import model.entities.Vehicle;
import model.entities.VehicleType;
import org.jdom2.JDOMException;

/**
 *
 * @author 50687
 * @author user
 */
public class VehicleWindow extends BaseInternalFrame {

    //Controladores
    private VehicleFileController vehicleController;
    private CustomerFileController customerController;
    private ParkingLotFileController parkingLotController;
    private VehicleTypeController vehicleTypeController;

    //Componentes UI
    private JTextField plateField, colorField, brandField, modelField;
    private JComboBox<String> vehicleTypeCombo, parkingLotCombo, customerCombo;
    private JList<String> customerList;
    private DefaultListModel<String> customerListModel;

    //Datos
    private ArrayList<Customer> allCustomers;
    private ArrayList<ParkingLot> allParkingLots;
    private ArrayList<VehicleType> allVehicleTypes;
    private ArrayList<Customer> selectedCustomers;

    private Vehicle vehicleToEdit;

    //Constructor para nuevo vehículo
    public VehicleWindow() {
        // Pasamos el título directamente al constructor de BaseInternalFrame
        super("REGISTRO DE VEHÍCULO");
        this.vehicleToEdit = null;
        this.selectedCustomers = new ArrayList<>(); //Inicializar lista vacía
        initWindow();
        setVisible(true);
    }

    //Constructor para editar vehículo
    public VehicleWindow(Vehicle vehicle) {
        super("EDITAR VEHÍCULO");
        this.vehicleToEdit = vehicle;
        this.selectedCustomers = vehicle.getCustomer();
        initWindow();
        setVisible(true);
    }

    //Inicializar controladores
    private void initControllers() {
        try {
            vehicleController = new VehicleFileController();
            customerController = new CustomerFileController();
            parkingLotController = new ParkingLotFileController();
            vehicleTypeController = new VehicleTypeController();
        } catch (IOException e) {
            showError("No se puede acceder al archivo de vehículos" + e.getMessage());
            dispose();
        } catch (JDOMException ex) {
            Logger.getLogger(VehicleWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Inicializar datos
    private void initData() {
        allCustomers = new ArrayList<>();
        allParkingLots = new ArrayList<>();
        allVehicleTypes = new ArrayList<>();
        if (selectedCustomers == null) {
            selectedCustomers = new ArrayList<>();
        }
    }

    //Crear interfaz
    private void createUI() {
        setSize(600, 700);
        setLayout(new BorderLayout());

        //título
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(primaryColor);
        headerPanel.setPreferredSize(new Dimension(600, 50));
        JLabel lblTitle = new JLabel(isEditMode() ? "EDITAR VEHÍCULO" : "REGISTRO DE VEHÍCULO");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(titleFont);
        headerPanel.add(lblTitle);

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(backgroundColor);
        container.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        container.add(createVehiclePanel());
        container.add(Box.createRigidArea(new Dimension(0, 20))); // Espacio entre paneles
        container.add(createCustomerPanel());

        JScrollPane scroll = new JScrollPane(container);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        add(headerPanel, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

   private void initWindow() {
    initControllers();
    initData();
    createUI();
    loadData();

    if (isEditMode()) {
        loadVehicleData();
    }

    // ESTO TRAE LA VENTANA AL FRENTE
    SwingUtilities.invokeLater(() -> {
        try {
            this.toFront();          // La mueve arriba en la lista de capas
            this.setSelected(true);   // La marca como activa para el DesktopPane
            this.requestFocus();      // Pide el foco del teclado
        } catch (java.beans.PropertyVetoException e) {
            e.printStackTrace();
        }
    });
}
    
    private void loadData() {
        //Llenar el combo de clientes disponibles
        loadCustomers();
        
        //Llenar el combo de tipos de vehículo
        loadVehicleTypes();
        
        //Llenar el combo de parqueos
        loadParkingLots();
        
        //Refrescar la JList visual de clientes que ya están "a bordo"
        updateCustomerList();
    }

    private JPanel createVehiclePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(fieldBorderColor), " DATOS DEL VEHÍCULO ", 0, 0, labelFont, primaryColor),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        //Inicializar y Estilizar campos
        plateField = new JTextField();
        styleTextField(plateField);
        colorField = new JTextField();
        styleTextField(colorField);
        brandField = new JTextField();
        styleTextField(brandField);
        modelField = new JTextField();
        styleTextField(modelField);

        //Añadir al GridBag
        addFormRow(panel, "Placa*:", plateField, gbc, 0);
        addFormRow(panel, "Color:", colorField, gbc, 1);
        addFormRow(panel, "Marca:", brandField, gbc, 2);
        addFormRow(panel, "Modelo:", modelField, gbc, 3);

        vehicleTypeCombo = new JComboBox<>();
        parkingLotCombo = new JComboBox<>();
        addFormRow(panel, "Tipo*:", vehicleTypeCombo, gbc, 4);
        addFormRow(panel, "Parqueo*:", parkingLotCombo, gbc, 5);

        return panel;
    }

    private JPanel createCustomerPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(fieldBorderColor), " CLIENTES A BORDO ", 0, 0, labelFont, primaryColor),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        //Selector superior
        JPanel top = new JPanel(new BorderLayout(10, 0));
        top.setOpaque(false);
        customerCombo = new JComboBox<>();
        JButton addButton = new JButton("AGREGAR");
        styleButton(addButton);
        addButton.addActionListener(e -> addCustomer());

        top.add(new JLabel("Seleccionar Cliente:"), BorderLayout.NORTH);
        top.add(customerCombo, BorderLayout.CENTER);
        top.add(addButton, BorderLayout.EAST);

        //Lista central
        customerListModel = new DefaultListModel<>();
        customerList = new JList<>(customerListModel);
        customerList.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JScrollPane listScroll = new JScrollPane(customerList);
        listScroll.setPreferredSize(new Dimension(0, 120));

        //Botón remover
        JButton removeButton = new JButton("REMOVER SELECCIONADO");
        styleButton(removeButton);
        removeButton.setBackground(new Color(192, 57, 43)); // Rojo
        removeButton.addActionListener(e -> removeCustomer());

        panel.add(top, BorderLayout.NORTH);
        panel.add(listScroll, BorderLayout.CENTER);
        panel.add(removeButton, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        panel.setBackground(backgroundColor);

        JButton saveButton = new JButton("GUARDAR VEHÍCULO");
        JButton cancelButton = new JButton("CANCELAR");

        styleButton(saveButton);
        styleButton(cancelButton);
        cancelButton.setBackground(new Color(127, 140, 141)); // Gris

        saveButton.addActionListener(e -> saveVehicle());
        cancelButton.addActionListener(e -> dispose());

        panel.add(cancelButton);
        panel.add(saveButton);

        return panel;
    }

    //Método auxiliar para el GridBagLayout
    private void addFormRow(JPanel p, String text, JComponent comp, GridBagConstraints gbc, int row) {
        gbc.gridy = row;
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        p.add(new JLabel(text), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        p.add(comp, gbc);
    }

    //Cargar clientes
    private void loadCustomers() {
        try {
            allCustomers = customerController.getAllCustomers();
            customerCombo.removeAllItems();
            customerCombo.addItem("-- Seleccione --");

            for (Customer c : allCustomers) {
                customerCombo.addItem(c.getName() + " (" + c.getEmail() + ")");
            }
        } catch (IOException e) {
            showError("Error al cargar la lista de clientes del vehículo" + e.getMessage());
        }
    }

    //Cargar tipos de vehículo
    private void loadVehicleTypes() {
        try {
            allVehicleTypes = vehicleTypeController.getAllVehicleTypes();
            vehicleTypeCombo.removeAllItems();
            vehicleTypeCombo.addItem("-- Seleccione --");

            for (VehicleType type : allVehicleTypes) {
                vehicleTypeCombo.addItem(type.getDescription());
            }
        } catch (IOException e) {
            showError("Error al cargar los tipos de vehículo" + e.getMessage());
        }
    }

    //Cargar parqueos
    private void loadParkingLots() {
        try {
            allParkingLots = parkingLotController.getAllParkingLots();
            parkingLotCombo.removeAllItems();
            parkingLotCombo.addItem("-- Seleccione --");

            for (ParkingLot parkingLot : allParkingLots) {
                parkingLotCombo.addItem(parkingLot.getName());
            }
        } catch (IOException e) {
            showError("Error al cargar los parqueos" + e.getMessage());
        }
    }

    //Cargar datos para edición
    private void loadVehicleData() {

        plateField.setText(vehicleToEdit.getPlate());
        colorField.setText(vehicleToEdit.getColor());
        brandField.setText(vehicleToEdit.getBrand());
        modelField.setText(vehicleToEdit.getModel());

        for (int i = 0; i < allVehicleTypes.size(); i++) {
            if (allVehicleTypes.get(i).getId() == vehicleToEdit.getVehicleType().getId()) {
                vehicleTypeCombo.setSelectedIndex(i + 1);
                break;
            }
        }
    }

    //Agregar cliente
    private void addCustomer() {
        int index = customerCombo.getSelectedIndex();
        if (index <= 0) {
            showMessage("Seleccione un cliente", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Customer customer = allCustomers.get(index - 1);

        if (isCustomerAdded(customer)) {
            showMessage("Cliente ya agregado", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        selectedCustomers.add(customer);
        updateCustomerList();

        askForMoreCustomers();
    }

    //Verificar si cliente ya está agregado
    private boolean isCustomerAdded(Customer customer) {
        for (Customer c : selectedCustomers) {
            if (c.getId() == customer.getId()) {
                return true;
            }
        }
        return false;
    }

    //Preguntar por más clientes
    private void askForMoreCustomers() {
        int answer = JOptionPane.showConfirmDialog(this,
                "¿Agregar otro cliente?",
                "Más clientes",
                JOptionPane.YES_NO_OPTION);

        if (answer == JOptionPane.YES_OPTION) {
            customerCombo.setSelectedIndex(0);
        }
    }

    //Remover cliente
    private void removeCustomer() {
        int index = customerList.getSelectedIndex();
        if (index >= 0) {
            selectedCustomers.remove(index);
            updateCustomerList();
        }
    }

    //Actualizar lista de clientes
    private void updateCustomerList() {
        customerListModel.clear();
        for (Customer customer : selectedCustomers) {
            String text = customer.getName() + " (" + customer.getEmail() + ")";
            customerListModel.addElement(text);
        }
    }

    //Guardar vehículo
    private void saveVehicle() {
        try {
            validateFields();
            Vehicle vehicle = createVehicle();
            ParkingLot parkingLot = getSelectedParkingLot();

            saveToFile(vehicle);
            int assignedSpace = parkingLotController.assignVehicleToParkingLot(vehicle, parkingLot.getId());
            
            vehicle.setSpace(parkingLotController.findAvailableSpace(vehicle, parkingLot));
            vehicleController.update(vehicle);
            showAssignmentDetails(vehicle, parkingLot, assignedSpace);

            showMessage("Vehículo guardado", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (IllegalArgumentException e) {
            showError("No se pudo completar el registro del vehículo" + e.getMessage());
        } catch (Exception e) {
            showMessage("Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Crear objeto Vehicle
    private Vehicle createVehicle() throws Exception {
        Vehicle vehicle = new Vehicle();

        if (isEditMode()) {
            vehicle.setId(vehicleToEdit.getId());
            vehicle.setEntryTime(vehicleToEdit.getEntryTime());
        } else {
            vehicle.setEntryTime(LocalDateTime.now());
        }

        vehicle.setPlate(plateField.getText().trim());
        vehicle.setColor(colorField.getText().trim());
        vehicle.setBrand(brandField.getText().trim());
        vehicle.setModel(modelField.getText().trim());
        vehicle.setCustomer(selectedCustomers);

        ParkingLot parkingLot = getSelectedParkingLot();
        VehicleType type = getSelectedVehicleType();

        Space availableSpace = parkingLotController.findAvailableSpace(vehicle, parkingLot);
        if (availableSpace != null) {
            vehicle.setVehicleType(type);
        } else {
            showError("No hay espacios disponibles para este tipo de vehículo");
        }

        return vehicle;
    }

    private VehicleType getSelectedVehicleType() {
        int index = vehicleTypeCombo.getSelectedIndex() - 1;
        return index >= 0 ? allVehicleTypes.get(index) : null;
    }

    //Obtener parqueo seleccionado
    private ParkingLot getSelectedParkingLot() {
        int index = parkingLotCombo.getSelectedIndex() - 1;
        if (index >= 0 && index < allParkingLots.size()) {
            return allParkingLots.get(index);
        }
        return null;
    }

    //Validar campos
    private void validateFields() {
        if (plateField.getText().trim().isEmpty()) {
            showWarning("Placa requerida");
        }
        if (vehicleTypeCombo.getSelectedIndex() <= 0) {
            showWarning("Seleccione tipo de vehículo");
        }
        if (parkingLotCombo.getSelectedIndex() <= 0) {
            showWarning("Seleccione parqueo");
        }
        if (selectedCustomers.isEmpty()) {
            showWarning("Agregue al menos un cliente");
        }
    }

    //Mostrar detalles de asignación
    private void showAssignmentDetails(Vehicle vehicle, ParkingLot parkingLot, int spaceId) {
        if (parkingLot == null) {
            return;
        }

        StringBuilder clients = new StringBuilder();
        for (int i = 0; i < selectedCustomers.size(); i++) {
            clients.append(selectedCustomers.get(i).getName());
            if (i < selectedCustomers.size() - 1) {
                clients.append(", ");
            }
        }

        String message = "¡Vehículo registrado!\n\n"
                + "Placa: " + vehicle.getPlate() + "\n"
                + "Parqueo: " + parkingLot.getName() + "\n"
                + "Espacio asignado:" + spaceId + "\n"
                + "Clientes: " + clients.toString() + "\n"  
                + "Hora de entrada: " + formatDateTime(vehicle.getEntryTime());

        JOptionPane.showMessageDialog(this, message, "Registro Exitoso",
                JOptionPane.INFORMATION_MESSAGE);
    }

    //Formatear fecha/hora
    private String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return dateTime.format(formatter);
    }

    //Guardar en archivo
    private void saveToFile(Vehicle vehicle) {
        try {
            if (isEditMode()) {
                vehicleController.update(vehicle);
            } else {
                vehicleController.create(vehicle);
            }
        } catch (IOException | IllegalArgumentException e) {
            showError("No se pudo de guardar la información del vehículo en el archivo" + e.getMessage());
        }
    }

    //Verificar modo edición
    private boolean isEditMode() {
        return vehicleToEdit != null;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }

    //Mostrar mensaje
    private void showMessage(String text, String title, int type) {
        JOptionPane.showMessageDialog(this, text, title, type);
    }
}
