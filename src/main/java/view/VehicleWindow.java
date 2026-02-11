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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import model.entities.Customer;
import model.entities.ParkingLot;
import model.entities.Space;
import model.entities.Vehicle;
import model.entities.VehicleType;

/**
 *
 * @author 50687
 * @author user
 */
public class VehicleWindow extends JInternalFrame {

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
        super("Registrar Vehículo", true, true, true, true);
        this.vehicleToEdit = null;
        initWindow();
    }

    //Constructor para editar vehículo
    public VehicleWindow(Vehicle vehicle) {
        super("Editar Vehículo", true, true, true, true);
        this.vehicleToEdit = vehicle;
        this.selectedCustomers = vehicle.getCustomer();
        initWindow();
    }

    //Inicializar ventana
    private void initWindow() {
        initControllers();
        initData();
        createUI();
        loadData();
        if (isEditMode()) {
            loadVehicleData();
        }
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
        setSize(550, 550);
        setLocation(100, 50);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        mainPanel.add(createVehiclePanel(), BorderLayout.NORTH);
        mainPanel.add(createCustomerPanel(), BorderLayout.CENTER);
        mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    //Panel de datos del vehículo
    private JPanel createVehiclePanel() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Datos del Vehículo"));

        panel.add(new JLabel("Placa*:"));
        plateField = new JTextField();
        panel.add(plateField);

        panel.add(new JLabel("Color:"));
        colorField = new JTextField();
        panel.add(colorField);

        panel.add(new JLabel("Marca:"));
        brandField = new JTextField();
        panel.add(brandField);

        panel.add(new JLabel("Modelo:"));
        modelField = new JTextField();
        panel.add(modelField);

        panel.add(new JLabel("Tipo*:"));
        vehicleTypeCombo = new JComboBox<>();
        panel.add(vehicleTypeCombo);

        panel.add(new JLabel("Parqueo*:"));
        parkingLotCombo = new JComboBox<>();
        panel.add(parkingLotCombo);

        return panel;
    }

    //Panel de clientes
    private JPanel createCustomerPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Clientes a Bordo*"));

        //Panel superior para agregar clientes
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Cliente:"));
        customerCombo = new JComboBox<>();
        customerCombo.setPreferredSize(new Dimension(250, 25));
        topPanel.add(customerCombo);

        JButton addButton = new JButton("Agregar");
        addButton.addActionListener(e -> addCustomer());
        topPanel.add(addButton);

        //Lista de clientes seleccionados
        customerListModel = new DefaultListModel<>();
        customerList = new JList<>(customerListModel);
        JScrollPane scrollPane = new JScrollPane(customerList);
        scrollPane.setPreferredSize(new Dimension(400, 100));

        //Panel inferior para remover
        JPanel bottomPanel = new JPanel(new FlowLayout());
        JButton removeButton = new JButton("Remover");
        removeButton.addActionListener(e -> removeCustomer());
        bottomPanel.add(removeButton);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    //Panel de botones
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        JButton saveButton = new JButton("Guardar");
        saveButton.addActionListener(e -> saveVehicle());

        JButton cancelButton = new JButton("Cancelar");
        cancelButton.addActionListener(e -> dispose());

        panel.add(saveButton);
        panel.add(cancelButton);

        return panel;
    }

    //Cargar datos
    private void loadData() {
        loadCustomers();
        loadVehicleTypes();
        loadParkingLots();
        updateCustomerList();
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
            parkVehicleInParkingLot(vehicle, parkingLot);
            showAssignmentDetails(vehicle, parkingLot);

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

    private void parkVehicleInParkingLot(Vehicle vehicle, ParkingLot parkingLot) {
        if (vehicle == null) {
            showWarning("Vehículo no puede ser nulo");
        }
        if (parkingLot == null) {
            showWarning("Seleccione un parqueo");
        }

        try {
            // Verificar si hay espacio disponible
            int occupiedSpaces = parkingLot.getVehicles().size();
            if (occupiedSpaces >= parkingLot.getNumberOfSpaces()) {
                showError("No hay espacios disponibles en el parqueo");
            }

            // Agregar vehículo al parqueo
            parkingLot.getVehicles().add(vehicle);

            // Guardar cambios del parqueo (actualizar lista de vehículos)
            parkingLotController.updateParkingLot(parkingLot);

            // Mensaje de confirmación
            JOptionPane.showMessageDialog(this,
                    "Vehículo parqueado en " + parkingLot.getName(),
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (HeadlessException e) {
            showError("Error: " + e.getMessage());
        }
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
    private void showAssignmentDetails(Vehicle vehicle, ParkingLot parkingLot) {
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
                + "Clientes: " + clients.toString() + "\n"
                + "Espacio:" + vehicle.getSpace() + "\n"
                + "Entrada: " + formatDateTime(vehicle.getEntryTime());

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
