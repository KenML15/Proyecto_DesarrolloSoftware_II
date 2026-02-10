/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package view;

import controller.CustomerController;
import controller.FeeController;
import controller.ParkingLotController;
import controller.VehicleController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
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
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import model.entities.Administrator;
import model.entities.Clerk;
import model.entities.Customer;
import model.entities.Fee;
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
    private User currentUser;

    // Controladores (Instancias para acceder a la lógica)
    static CustomerController customerController = new CustomerController();
    static VehicleController vehicleController = new VehicleController();
    static ParkingLotController parkingLotController = new ParkingLotController();

    public RegistrationWindow(User user) {
        this.currentUser = user; // Recibimos el usuario del Login
        initComponents();
        applyPermissions(); // Ajustar qué se ve y qué no
    }

   private void initComponents() {
    // 1. Configuración básica de la ventana
    setTitle("Parking System Dashboard");
    setSize(900, 500);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setLayout(new BorderLayout());

    // 2. INICIALIZACIÓN DE COMPONENTES (Evita el NullPointerException)
    headerPanel = new JPanel(new BorderLayout());
    headerPanel.setPreferredSize(new Dimension(900, 70));
    headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));

    lblTitle = new JLabel("PARKING SYSTEM");
    lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
    lblTitle.setForeground(Color.WHITE);

    btnLogout = new JButton("LOGOUT");
    styleMenuButton(btnLogout);
    btnLogout.addActionListener(this);

    // 3. Montar el Header
    headerPanel.add(lblTitle, BorderLayout.WEST);
    headerPanel.add(btnLogout, BorderLayout.EAST);

    // 4. Panel Central de Botones (Cards)
    JPanel contentPanel = new JPanel(new GridLayout(1, 3, 20, 0));
    contentPanel.setBorder(BorderFactory.createEmptyBorder(50, 40, 50, 40));

    btnCustomers = new JButton("CUSTOMER MANAGEMENT");
    btnVehicles = new JButton("VEHICLE MANAGEMENT");
    btnParking = new JButton("SYSTEM SETTINGS");

    // Aplicar estilos
    styleActionCard(btnCustomers);
    styleActionCard(btnVehicles);
    styleActionCard(btnParking);

    // Agregar Listeners
    btnCustomers.addActionListener(this);
    btnVehicles.addActionListener(this);
    btnParking.addActionListener(this);

    contentPanel.add(btnCustomers);
    contentPanel.add(btnVehicles);
    contentPanel.add(btnParking);

    // 5. Agregar paneles a la ventana
    add(headerPanel, BorderLayout.NORTH);
    add(contentPanel, BorderLayout.CENTER);
}

private void applyPermissions() {
    // Verificamos que los objetos existan
    if (currentUser == null) return;

    if (currentUser instanceof Administrator) {
        headerPanel.setBackground(new Color(192, 57, 43)); // Rojo
        lblTitle.setText("ADMINISTRATOR: " + currentUser.getName().toUpperCase());
        btnParking.setVisible(true);
    } else {
        headerPanel.setBackground(new Color(39, 174, 96)); // Verde
        lblTitle.setText("OPERATOR: " + currentUser.getName().toUpperCase());
        btnParking.setVisible(false); // El Clerk no ve configuración
    }
}

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
        String[] options = {"Add Customer", "Remove Customer", "View List", "Cancel"};
        int selection = JOptionPane.showOptionDialog(this, "Customer Management", "Select Action",
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
        String[] options = {"Register Entry", "View Vehicles", "Back"};
        int selection = JOptionPane.showOptionDialog(this, "Select an action", "Vehicle Management",
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
        int id = Integer.parseInt(JOptionPane.showInputDialog("Type the customer ID number"));

        String name = JOptionPane.showInputDialog("Type yhe customer´s name");

        int hasDisability = JOptionPane.showConfirmDialog(null, "¿The customer has a disability?", "Information", JOptionPane.YES_NO_OPTION);
        boolean disabilityPresented = (hasDisability == JOptionPane.YES_OPTION);

        Customer customerToInsert = new Customer(id, name, disabilityPresented);

        JOptionPane.showMessageDialog(null, customerController.insertCustomer(customerToInsert));

        return customerToInsert;
    }

    private static void removeCostumerAndVehicle() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Type the customer ID number to remove"));
            String plate = JOptionPane.showInputDialog("Type the plate number");

            Customer customerToRemove = customerController.findCustomerById(id);
            Vehicle vehicleToRemove = vehicleController.findVehicleByPlate(plate);

            // VALIDACIÓN ANTI-CRASH (Punto clave para evitar Exit Value: 1)
            if (vehicleToRemove == null || customerToRemove == null) {
                JOptionPane.showMessageDialog(null, "Error: Vehicle or Customer not found.");
                return;
            }

            // Si el cliente es el dueño, procedemos
            if (vehicleToRemove.getCustomer().contains(customerToRemove)) {
                customerController.removeCustomer(customerToRemove);
                vehicleController.removeVehicle(vehicleToRemove);
                JOptionPane.showMessageDialog(null, "Successfully removed.");
            } else {
                JOptionPane.showMessageDialog(null, "This customer is not registered to this vehicle.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Invalid data entered.");
        }
    }

    // ====================VEHÍCULO===========================
    public static void showAllVehicles() {
        StringBuilder report = new StringBuilder("--- VEHICLES AND CUSTOMERS ---\n\n");
        ArrayList<Vehicle> list = vehicleController.getAllVehicles();

        if (list.isEmpty()) {
            report.append("There aren´t vehicles in the parking lot. .");
        } else {
            for (Vehicle vehicle : list) {
                report.append("PLATE: ").append(vehicle.getPlate())
                        .append(" | SPACE: ").append(vehicle.getSpace() != null ? vehicle.getSpace().getId() : "N/A").append("\n")
                        .append("CUSTOMERS: ");

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
        String plate = JOptionPane.showInputDialog("Type the vehicle plate.");
        String color = JOptionPane.showInputDialog("Type the vehicle color.");
        String brand = JOptionPane.showInputDialog("Type the vehicle brand.");
        String model = JOptionPane.showInputDialog("Type the vehicle model.");

        VehicleType vehicleType = selectVehicleType();

        ArrayList<Customer> responsibleList = vehicleResponsibles();

        ParkingLot parkingLot = selectParkingLot();

        Vehicle vehicleToInsert = new Vehicle(plate, color, brand, model, responsibleList, vehicleType, null, LocalDateTime.now());

        int spaceAssigned = parkingLotController.registerVehicleInParkingLot(vehicleToInsert, parkingLot);
        Space space = new Space(spaceAssigned);
        vehicleToInsert.setSpace(space);

        if (spaceAssigned != -1) {
            vehicleController.insertVehicle(vehicleToInsert);
            JOptionPane.showMessageDialog(null, "Vehicle successfully entered.\n Allocated space: " + vehicleToInsert.getSpace());
        } else {
            JOptionPane.showMessageDialog(null, "ERROR: There aren´t spaces for this type of vehicle.");
        }

        JOptionPane.showMessageDialog(null, "Welcome to the parking lot: " + parkingLot.getName()
                + ".\n Ticket: \n"
                + showTicketToCostumer(vehicleToInsert, vehicleType));
    }

    private static ArrayList<Customer> vehicleResponsibles() {

        ArrayList<Customer> responsibleList = new ArrayList<>();
        int op;
        do {
            responsibleList.add(insertCustomer());
            op = JOptionPane.showConfirmDialog(null, "¿Do you want to add other customer?", "Many owners", JOptionPane.YES_NO_OPTION);
        } while (op == JOptionPane.YES_OPTION);

        return responsibleList;
    }

    private static String showTicketToCostumer(Vehicle vehicle, VehicleType vehicleType) {
        String ticket = "---------- TICKET ----------\n"
                + "PLATE: " + vehicle.getPlate() + "\n"
                + "CUSTOMERS: " + vehicle.getCustomer() + "\n"
                + "SPACE: " + vehicle.getSpace().getId() + "\n"
                + "TYPE: " + vehicleType.getDescription() + "\n"
                + "ENTRY: " + vehicle.getEntryTime() + "\n";

        return ticket;
    }

    private static VehicleType configureVehicleTypeOfSpaces(int position, boolean disabilityPresented) {
        String[] types = {"Types of vehicle", "1)Motorcycle", "2)Car", "3)Truck", "4)Bicycle", "5)Other"};
        byte[] tires = {0, 2, 4, 8, 12, -1};

        String allTypes = "";
        for (String type : types) {

            allTypes += type + "\n";
        }

        VehicleType vehicleType = new VehicleType();

        byte typeNumber;
        typeNumber = Byte.parseByte(JOptionPane.showInputDialog(allTypes + "\n" + "Enter yhe vehicle type numer in the space#" + position + " ¿disability?=" + (disabilityPresented ? "Yes" : "No")));
        vehicleType.setId(typeNumber);
        vehicleType.setDescription(types[typeNumber]);
        vehicleType.setNumberOfTires(tires[typeNumber]);

        return vehicleType;
    }

    private static VehicleType selectVehicleType() {
        String[] types = {"1) Motorcycle", "2) Car", "3) Truck", "4) Bicycle", "5) other"};
        float[] fees = {500.0f, 1000.0f, 2500.0f, 300.0f, 1500.0f};
        byte[] tires = {2, 4, 10, 2, 4};

        String menu = "Select the vehicle type:\n";
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

        String name = JOptionPane.showInputDialog("Type the name of the Parking lot");
        int numberOfSpaces = Integer.parseInt(JOptionPane.showInputDialog("Type the number of spaces to the parking lot"));
        int numberOfSpacesWithDisabiltyAdaptation = Integer.parseInt(JOptionPane.showInputDialog("Type the number of spaces for disability customers"));

        Space[] spaces = new Space[numberOfSpaces];
        spaces = configureSpaces(spaces, numberOfSpacesWithDisabiltyAdaptation);

        ParkingLot parkingLot = parkingLotController.registerParkingLot(name, spaces);
        parkingLot.setNumberOfSpaces(numberOfSpaces);
        parkingLot.setVehicles(new ArrayList<Vehicle>());

    }

    private static ParkingLot selectParkingLot() {

        String parkingLotsInformation = "List to the parking lots in the system\n\n";
        for (ParkingLot parkingLot : parkingLotController.getAllParkingLots()) {

            parkingLotsInformation += "Number to parking lot: " + parkingLot.getId() + " Name to the parking lot: " + parkingLot.getName() + "\n";
        }

        int option;
        ParkingLot parkingLot;
        do {

            option = Integer.parseInt(JOptionPane.showInputDialog(parkingLotsInformation + "\nEnter the parking number you wish to inquire about.\n or where you wish to enter a vehicle"));
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

            JOptionPane.showMessageDialog(null, "The number of spaces selected exceeds the maximum configured for this parking lot.");
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
