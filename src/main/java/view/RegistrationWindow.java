/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package view;

import controller.CustomerController;
import controller.ParkingLotController;
import controller.VehicleController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import model.entities.Administrator;
import model.entities.Clerk;
import model.entities.Customer;
import model.entities.ParkingLot;
import model.entities.Vehicle;
import model.entities.VehicleType;
import model.entities.Space;
import model.entities.User;

/**
 *
 * @author user
 */
public class RegistrationWindow extends JFrame implements ActionListener {

    private JButton btnCustomers, btnVehicles, btnParking, btnLogout;
    private JLabel lblTitle;
    private JPanel headerPanel;
    private final User currentUser;

    // Controladores (Instancias para acceder a la lógica)
    static CustomerController customerController = new CustomerController();
    static VehicleController vehicleController = new VehicleController();
    static ParkingLotController parkingLotController = new ParkingLotController();

    public RegistrationWindow(User user) {
        this.currentUser = user; // Recibimos el usuario del Login
        initComponents();
        applyPermissions(); // Ajustar qué se ve y qué no
    }

    private void applyPermissions() {

        if (currentUser instanceof Administrator) {

            headerPanel.setBackground(new Color(192, 57, 43));
            lblTitle.setText("Administrador: " + currentUser.getName());
            btnParking.setVisible(true); // El admin ve la configuración
            btnParking.setText("Configuración del sistema");
        } else if (currentUser instanceof Clerk) {

            headerPanel.setBackground(new Color(39, 174, 96));
            lblTitle.setText("Operador: " + currentUser.getName());

            btnParking.setVisible(false);
        }
    }

    private void initComponents() {
        setTitle("Sistema de parqueos");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        headerPanel = new JPanel();
        lblTitle = new JLabel();
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        headerPanel.add(lblTitle);

        initComponents();        // 2. CREAMOS los botones, etiquetas y paneles (Esto DEBE ir aquí)

        btnCustomers = new JButton("Gestión de clientes");
        btnVehicles = new JButton("Gestión de vehículos");
        btnParking = new JButton("Configuraciones del parqueo");
        btnLogout = new JButton("Cerrar sesión");
    }
    
//    private void applyPermissions() {
//        // Verificamos que los componentes existan antes de tocarlos
//        if (lblTitle == null || headerPanel == null || btnParking == null) {
//            System.err.println("Error: Los componentes no se han inicializado aún.");
//            return;
//        }
//
//        if (currentUser instanceof Administrator) {
//            headerPanel.setBackground(new Color(192, 57, 43));
//            lblTitle.setText("ADMINISTRATOR MODE: " + currentUser.getName());
//            btnParking.setVisible(true);
//        } else {
//            headerPanel.setBackground(new Color(39, 174, 96));
//            lblTitle.setText("OPERATOR TERMINAL: " + currentUser.getName());
//            btnParking.setVisible(false);
//        }
//    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnCustomers) {
            showCustomerSubMenu();
        } else if (e.getSource() == btnVehicles) {
            showVehicleSubMenu();
        } else if (e.getSource() == btnParking) {
            insertParkingLot();
        } else if (e.getSource() == btnLogout) {
            this.dispose();
            new LoginWindow().setVisible(true); // Regresar al login
        }
    }

    // Usamos OptionDialog en lugar de bucles While para menús secundarios
    private void showCustomerSubMenu() {
        String[] options = {"Agregar cliente", "Remover cliente", "Lista de clientes", "Cancelar"};
        int selection = JOptionPane.showOptionDialog(this, "Gestión de clientes", "Seleccione una acción",
                0, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (selection == 0) {
            insertCustomer();
        }
        if (selection == 1) {
            removeCostumerAndVehicle();
        }
        if (selection == 2) {
            showAllCustomer();
        }
    }

    private void showVehicleSubMenu() {
        String[] options = {"Registrar entrada", "Ver vehículos", "Regresar"};
        int selection = JOptionPane.showOptionDialog(this, "Seleccione una acción", "Gestión de vehículos",
                0, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (selection == 0) {
            insertVehicle();
        }
        if (selection == 1) {
            showAllVehicles();
        }
    }

    // --- Estilos y métodos de soporte ---
    private void styleMenuButton(JButton button) {
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(Color.WHITE);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
    }

    //====================CLIENTE===========================  
    public static void showAllCustomer() {
        JOptionPane.showMessageDialog(null, customerController.getAllCustomers().toString());
    }

    private static Customer insertCustomer() {
        int id = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el ID del cliente"));

        String name = JOptionPane.showInputDialog("Ingrese el nombre del cliente");

        int hasDisability = JOptionPane.showConfirmDialog(null, "¿El cliente se encuentra en situación de discapacidad?", "Información", JOptionPane.YES_NO_OPTION);
        boolean disabilityPresented = (hasDisability == JOptionPane.YES_OPTION);

        Customer customerToInsert = new Customer(id, name, disabilityPresented);

        JOptionPane.showMessageDialog(null, customerController.insertCustomer(customerToInsert));

        return customerToInsert;
    }

    private static void removeCostumerAndVehicle() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el ID del cliente a eliminar"));
            String plate = JOptionPane.showInputDialog("Ingrese el número de placa del vehículo");

            Customer customerToRemove = customerController.findCustomerById(id);
            Vehicle vehicleToRemove = vehicleController.findVehicleByPlate(plate);

            // VALIDACIÓN ANTI-CRASH (Punto clave para evitar Exit Value: 1)
            if (vehicleToRemove == null || customerToRemove == null) {
                JOptionPane.showMessageDialog(null, "Error: Vehículo o cliente no encontrado");
                return;
            }

            // Si el cliente es el dueño, procedemos
            if (vehicleToRemove.getCustomer().contains(customerToRemove)) {
                customerController.removeCustomer(customerToRemove);
                vehicleController.removeVehicle(vehicleToRemove);
                JOptionPane.showMessageDialog(null, "Eliminado exitosamente");
            } else {
                JOptionPane.showMessageDialog(null, "Este cliente no está registrado para este vehículo");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Datos inválidos");
        }
    }

    // ====================VEHÍCULO===========================
    public static void showAllVehicles() {
        StringBuilder report = new StringBuilder("--- VEHÍCULOS Y CLIENTES ---\n\n");
        ArrayList<Vehicle> list = vehicleController.getAllVehicles();

        if (list.isEmpty()) {
            report.append("No hay vehículos en el parqueo");
        } else {
            for (Vehicle vehicle : list) {
                report.append("PLACA: ").append(vehicle.getPlate())
                        .append(" | ESPACIO: ").append(vehicle.getSpace() != null ? vehicle.getSpace().getId() : "N/A").append("\n")
                        .append("CLIENTES: ");

                for (int i = 0; i < vehicle.getCustomer().size(); i++) {
                    report.append(vehicle.getCustomer().get(i).getName());
                    if (i < vehicle.getCustomer().size() - 1) {
                        report.append(", ");
                    }
                }
                report.append("\n----------------------------------------------\n");
            }
        }
        JOptionPane.showMessageDialog(null, report.toString());
    }

    private static void insertVehicle() {
        String plate = JOptionPane.showInputDialog("Ingrese la placa del vehículo");
        String color = JOptionPane.showInputDialog("Ingrese el color del vehículo");
        String brand = JOptionPane.showInputDialog("Ingrese la marca del vehículo");
        String model = JOptionPane.showInputDialog("Ingrese el modelo del vehículo");

        VehicleType vehicleType = selectVehicleType();

        ArrayList<Customer> responsibleList = vehicleResponsibles();

        ParkingLot parkingLot = selectParkingLot();

        Vehicle vehicleToInsert = new Vehicle(plate, color, brand, model, responsibleList, vehicleType, null, LocalDateTime.now());

        int spaceAssigned = parkingLotController.registerVehicleInParkingLot(vehicleToInsert, parkingLot);
        Space space = new Space(spaceAssigned);
        vehicleToInsert.setSpace(space);

        if (spaceAssigned != -1) {
            vehicleController.insertVehicle(vehicleToInsert);
            JOptionPane.showMessageDialog(null, "Vehículo registrado con éxito.\n Espacio asignado: " + vehicleToInsert.getSpace());
        } else {
            JOptionPane.showMessageDialog(null, "ERROR: No hay espacios disponibles para este tipo de vehículo");
        }

        JOptionPane.showMessageDialog(null, "Bienvenido al parqueo: " + parkingLot.getName()
                + ".\n Comprobante de entrada: \n"
                + showTicketToCostumer(vehicleToInsert, vehicleType));
    }

    private static ArrayList<Customer> vehicleResponsibles() {

        ArrayList<Customer> responsibleList = new ArrayList<>();
        int op;
        do {
            responsibleList.add(insertCustomer());
            op = JOptionPane.showConfirmDialog(null, "¿Desea agregar otro cliente?", "Varios clientes", JOptionPane.YES_NO_OPTION);
        } while (op == JOptionPane.YES_OPTION);

        return responsibleList;
    }

    private static String showTicketToCostumer(Vehicle vehicle, VehicleType vehicleType) {
        String ticket = "---------- COMPROBANTE ----------\n"
                + "Placa: " + vehicle.getPlate() + "\n"
                + "Clientes: " + vehicle.getCustomer() + "\n"
                + "Espacio: " + vehicle.getSpace().getId() + "\n"
                + "Tipo: " + vehicleType.getDescription() + "\n"
                + "Hora de entrada: " + vehicle.getEntryTime() + "\n";

        return ticket;
    }

    private static VehicleType configureVehicleTypeOfSpaces(int position, boolean disabilityPresented) {
        String[] types = {"Tipo de vehículo", "1)Motocicleta", "2)Liviano", "3)Pesado", "4)Bicicleta", "5)Otro"};
        byte[] tires = {0, 2, 4, 8, 12, -1};

        String allTypes = "";
        for (String type : types) {

            allTypes += type + "\n";
        }

        VehicleType vehicleType = new VehicleType();

        byte typeNumber;
        typeNumber = Byte.parseByte(JOptionPane.showInputDialog(allTypes + "\n" + "Ingrese el número del tipo de vehículo en el espacio" + position + " ¿Discapacidad?=" + (disabilityPresented ? "Sí" : "No")));
        vehicleType.setId(typeNumber);
        vehicleType.setDescription(types[typeNumber]);
        vehicleType.setNumberOfTires(tires[typeNumber]);

        return vehicleType;
    }

    private static VehicleType selectVehicleType() {
        String[] types = {"1) Motocicleta", "2) Liviano", "3) Pesado", "4) Bicicleta", "5) Otro"};
        float[] fees = {500.0f, 1000.0f, 2500.0f, 300.0f, 1500.0f};
        byte[] tires = {2, 4, 10, 2, 4};

        String menu = "Seleccione el tipo de vehículo:\n";
        for (String type : types) {
            menu += type + "\n";
        }

        int option = Integer.parseInt(JOptionPane.showInputDialog(menu)) - 1;

        VehicleType vehicleType = new VehicleType();
        vehicleType.setId(option + 1);
        vehicleType.setDescription(types[option]);
        //vehicleType.setFee(fees[option]);
        vehicleType.setNumberOfTires(tires[option]);

        return vehicleType;
    }

    // ====================PARQUEO===========================
    private static void insertParkingLot() {

        String name = JOptionPane.showInputDialog("Ingrese el nombre del parqueo");
        int numberOfSpaces = Integer.parseInt(JOptionPane.showInputDialog("Escriba el número de espacios que tendrá el parqueo"));
        int numberOfSpacesWithDisabiltyAdaptation = Integer.parseInt(JOptionPane.showInputDialog("Ingrese la cantidad de espacios para discapacitados"));

        Space[] spaces = new Space[numberOfSpaces];
        spaces = configureSpaces(spaces, numberOfSpacesWithDisabiltyAdaptation);

        ParkingLot parkingLot = parkingLotController.registerParkingLot(name, spaces);
        parkingLot.setNumberOfSpaces(numberOfSpaces);
        parkingLot.setVehicles(new ArrayList<Vehicle>());

    }

    private static ParkingLot selectParkingLot() {

        String parkingLotsInformation = "Lista de los parqueos registrados en el sistema\n\n";
        for (ParkingLot parkingLot : parkingLotController.getAllParkingLots()) {

            parkingLotsInformation += "Número de parqueo: " + parkingLot.getId() + " Nombre del parqueo: " + parkingLot.getName() + "\n";
        }

        int option;
        ParkingLot parkingLot;
        do {

            option = Integer.parseInt(JOptionPane.showInputDialog(parkingLotsInformation + "\nIngrese el número de estacionamiento que desea consultar.\n o, donde desea ingresar un vehículo"));
            parkingLot = parkingLotController.findParkingLotById(option);

        } while (parkingLot == null);

        return parkingLot;
    }

    private static Space[] configureSpaces(Space[] spaces, int numberOfSpacesWithDisabilityAdaptation) {

        if (numberOfSpacesWithDisabilityAdaptation <= spaces.length) {
            for (int i = 0; i < numberOfSpacesWithDisabilityAdaptation; i++) {
                Space space = new Space();

                space.setId(i);
                space.setDisabilityAdaptation(true);
                space.setVehicleType(configureVehicleTypeOfSpaces(i, true));

                spaces[i] = space;
            }

            for (int i = numberOfSpacesWithDisabilityAdaptation; i < spaces.length; i++) {
                Space space = new Space();

                space.setId(i);
                space.setDisabilityAdaptation(false);
                space.setVehicleType(configureVehicleTypeOfSpaces(i, false));

                spaces[i] = space;
            }

        } else {

            JOptionPane.showMessageDialog(null, "El número de espacios seleccionado excede el máximo configurado para este parqueo");
        }

        return spaces;
    }

    private void styleActionCard(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(Color.WHITE);
        button.setForeground(new Color(52, 73, 94));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 214, 222), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // ¡Asegúrate de haber borrado la línea que decía "throw new UnsupportedOperationException"!
    }

}
