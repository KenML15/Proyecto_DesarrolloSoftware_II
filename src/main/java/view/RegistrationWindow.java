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
import java.awt.Cursor;
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
import javax.swing.SwingUtilities;
import model.entities.Customer;
import model.entities.Fee;
import model.entities.ParkingLot;
import model.entities.Vehicle;
import model.entities.VehicleType;
import model.entities.Space;

/**
 *
 * @author user
 */
public class RegistrationWindow extends JFrame implements ActionListener {

    private JButton btnCustomers, btnVehicles, btnParking, btnLogout;

    public RegistrationWindow() {
        setTitle("Parking Management System - Main Menu");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(45, 52, 54));
        JLabel lblTitle = new JLabel("Main Administration Menu");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(lblTitle);

        JPanel menuPanel = new JPanel(new GridLayout(4, 1, 15, 15));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        btnCustomers = new JButton("Manage Customers");
        btnVehicles = new JButton("Manage Vehicles");
        btnParking = new JButton("Parking Lot Administration");
        btnLogout = new JButton("Exit System");

        JButton[] buttons = {btnCustomers, btnVehicles, btnParking, btnLogout};
        for (JButton btn : buttons) {
            styleMenuButton(btn);
            btn.addActionListener(this);
            menuPanel.add(btn);
        }

        add(headerPanel, BorderLayout.NORTH);
        add(menuPanel, BorderLayout.CENTER);
    }

    private void styleMenuButton(JButton button) {
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        button.setBackground(Color.WHITE);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnCustomers) {
            customersMenu(); 
        } else if (e.getSource() == btnVehicles) {
            vehicleMenu();
        } else if (e.getSource() == btnParking) {
            insertParkingLot();
        } else if (e.getSource() == btnLogout) {
            System.exit(0);
        }
    }

    static CustomerController customerController = new CustomerController();
    static VehicleController vehicleController = new VehicleController();
    static ParkingLotController parkingLotController = new ParkingLotController();
    static FeeController feeController = new FeeController();

    public static void main(String[] args) {
    // Esto asegura que la interfaz se cree de forma segura
    SwingUtilities.invokeLater(() -> {
        new RegistrationWindow().setVisible(true);
    });
}

    //=====================MENU============================
    public static void showMenu() {

        int choice = 1;
        while (choice != 0) {

            choice = Integer.parseInt(JOptionPane.showInputDialog(
                    " Type 0 to exit the system."
                    + "\n Type 1 to enter the customer´s menu. "
                    + "\n Type 2 to enter the vehicle´s menu. "
                    + "\n Type 3 to manage the parcking lots. "));

            switch (choice) {
                case 0 -> {
                    JOptionPane.showMessageDialog(null, "Thanks for use our system. \n ¡See you later!");
                }
                case 1 -> {
                    customersMenu();
                }
                case 2 -> {
                    vehicleMenu();
                    
                }
                case 3 -> {
                    insertParkingLot();
                }
            }
        }
    }
   
    public static void customersMenu(){
        int choice = 1;
        while (choice != 0 ) {
        
            choice = Integer.parseInt(JOptionPane.showInputDialog(
                    "Type 0 to exit the system.\n"
                            + "Type 1 to add a custumer. \n"
                            + "Type 2 to remove a customer. \n"
                            + "Type 3 to view the list of customers. "));
            
            switch (choice) {
                case 0 -> {
                    JOptionPane.showMessageDialog(null, "Leaving the customer´s menu. ");
                    break;
                }
                case 1 -> {
                    insertCustomer();
                }
                case 2 -> {
                    removeCostumerAndVehicle();
                }
                case 3 -> {
                    showAllCustomer();
                }
            }
        }
    }
    
    public static void vehicleMenu() {
        int choice = 1;
        while (choice != 0) {

                    choice = Integer.parseInt(JOptionPane.showInputDialog(
                    "Type 0 to exit the system.\n"
                            + "Type 1 to add a vehicle. \n"
                            + "Type 2 to view the list of vehicle´s. "));
            
            switch (choice) {
                case 0 -> {
                    JOptionPane.showMessageDialog(null, "Leaving the vehicle´s menu. ");
                    break;
                }
                case 1 -> {
                    insertVehicle();
                }
                case 2 -> {
                    showAllVehicles();
                }
            }
        } 
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
        int id = Integer.parseInt(JOptionPane.showInputDialog("Type the customer ID number to remove"));
        String plate = JOptionPane.showInputDialog("Type the plate number of the customer to be remove");

        Customer customerToRemove = customerController.findCustomerById(id);
        Vehicle vehicleToRemove = vehicleController.findVehicleByPlate(plate);

        for (Customer customerToFind : vehicleToRemove.getCustomer()) {
            if (customerToFind != customerToRemove) {
                JOptionPane.showMessageDialog(null, "Customer wasn´t found with that ID");
                break;
            } else {
                customerController.removeCustomer(customerToRemove);
                vehicleController.removeVehicle(vehicleToRemove);
                JOptionPane.showMessageDialog(null, "The customer has been removed");
                break;
            }
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
                +showTicketToCostumer(vehicleToInsert, vehicleType));
    }

    private static ArrayList<Customer> vehicleResponsibles(){
        
        ArrayList<Customer> responsibleList = new ArrayList<>();
        int op;
        do {
            responsibleList.add(insertCustomer());
            op = JOptionPane.showConfirmDialog(null, "¿Do you want to add other customer?", "Múltiples Dueños", JOptionPane.YES_NO_OPTION);
        } while (op == JOptionPane.YES_OPTION);
        
        return responsibleList;
    }

    private static String showTicketToCostumer(Vehicle vehicle, VehicleType vehicleType) {
        String ticket = "---------- TICKET ----------\n"
                + "PLATE: " + vehicle.getPlate() + "\n"
                + "CUSTOMERS: " + vehicle.getCustomer() + "\n"
                + "SPACE: " + vehicle.getSpace().getId() + "\n"
                + "TYPE: " + vehicleType.getDescription() + "\n"
                + "ENTRY: " + vehicle.getEntryTime() + "\n" ;
                        
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
        vehicleType.setFee(fees[option]);
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

        String parkingLotsInformation = "Lista de parqueos en el sistema\n\n";
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
    
}
