/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package view;

import controller.CustomerController;
import controller.ParkingLotController;
import controller.VehicleController;
import java.time.LocalDateTime;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import model.entities.Customer;
import model.entities.ParkingLot;
import model.entities.Vehicle;
import model.entities.VehicleType;
import model.entities.Space;

/**
 *
 * @author user
 */
public class Proyecto_DesarrolloSoftware_II {

    static CustomerController customerController = new CustomerController();
    static VehicleController vehicleController = new VehicleController();
    static ParkingLotController parkingLotController = new ParkingLotController();

    public static void main(String[] args) {
        showMenu();
    }

    public static void showMenu() {

        int choice = 1;
        while (choice != 0) {

            choice = Integer.parseInt(JOptionPane.showInputDialog(
                    "Ingrese 0 para cerrar el sistema."
                    + "\n Ingrese 1 para añadir a un cliente."
                    + "\n Ingrese 2 para remover un cliente."
                    + "\n Ingrese 3 para ver la lista de clientes en el sistema. "
                    + "\n Ingrese 4 para registrar el vehículo."
                    + "\n Ingrese 5 para mostrar la lista de vehículos"
                    + "\n Ingrese 6 para administrar el parqueo"));
            
            switch (choice) {
                case 0 -> {
                    JOptionPane.showMessageDialog(null, "Gracias por usar el sistema. \n ¡Hasta luego!");
                }
                case 1 -> {
                    insertCustomer();
                }
                case 2 -> {
                    removeCostumerAndVehicle();
                    //removeCustomer();
                }
                case 3 -> {
                    showAllCustomer();
                }
                case 4 -> {
                    insertVehicle();
                }
                case 5 -> {
                    showAllVehicles();
                }
                case 6 -> {
                    insertParkingLot();
                }
                
            }
        }
    }

    // ====================Todo lo del cliente===========================  
    public static void showAllCustomer() {
        JOptionPane.showMessageDialog(null, customerController.getAllCustomers().toString());
    }

    private static Customer insertCustomer() {
        String id = JOptionPane.showInputDialog("Ingrese el número de cédula del cliente");

        String name = JOptionPane.showInputDialog("Ingrese el nombre del cliente");
        
        int hasDisability = JOptionPane.showConfirmDialog(null, "¿Presenta alguna discapacidad?", "Información", JOptionPane.YES_NO_OPTION);
        boolean disabilityPresented = (hasDisability == JOptionPane.YES_OPTION);
      
        Customer customerToInsert = new Customer(id, name, disabilityPresented);

        JOptionPane.showMessageDialog(null, customerController.insertCustomer(customerToInsert));

        return customerToInsert;
    }
    
    private static void removeCostumerAndVehicle() {
        String id = JOptionPane.showInputDialog("Ingrese la cédula del cliente a eliminar");
        String plate = JOptionPane.showInputDialog("Ingrese la placa del vehiculo del cliente a eliminar");

        Customer customerToRemove = customerController.findCustomerById(id);
        Vehicle vehicleToRemove = vehicleController.findVehicleByPlate(plate);

        for (Customer customerToFind : vehicleToRemove.getCustomer()) {
            if (customerToFind != customerToRemove) {
                JOptionPane.showMessageDialog(null, "No se ha podido encontrar un cliente con ese número de cédula");
                break;
            } else {
                customerController.removeCustomer(customerToRemove);
                vehicleController.removeVehicle(vehicleToRemove);
                JOptionPane.showMessageDialog(null, "El cliente ha sido removido de forma correcta del sistema");
                break;
            }
        }
    }

    //Método de Pablo
    /*private static void removeCustomer() {
        String id = JOptionPane.showInputDialog("Ingrese la cédula del cliente a eliminar");
        String plate = JOptionPane.showInputDialog("Ingrese la placa del vehiculo del cliente a eliminar");
        // Llamamos al controlador pasando solo el ID
        customerController.removeCustomer(id);

        JOptionPane.showMessageDialog(null, "El cliente ha sido removido de forma correcta del sistema");
    }*/
    
    // ====================Todo lo del vehículo===========================
    public static void showAllVehicles() {
        JOptionPane.showMessageDialog(null, vehicleController.getAllVehicles().toString());
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

        if (spaceAssigned != -1) {
            vehicleController.insertVehicle(vehicleToInsert); 
            JOptionPane.showMessageDialog(null, "Vehículo ingresado con éxito.\nEspacio asignado: " + spaceAssigned);
        } else {
            JOptionPane.showMessageDialog(null, "ERROR: No hay espacios disponibles para este tipo de vehículo.");
        }
    }
    
    //Método que retorna la lista con los dueños del vehículo
    private static ArrayList<Customer> vehicleResponsibles(){
        
        ArrayList<Customer> responsibleList = new ArrayList<>();
        int op;
        do {
            responsibleList.add(insertCustomer());
            op = JOptionPane.showConfirmDialog(null, "¿Desea agregar otro dueño?", "Múltiples Dueños", JOptionPane.YES_NO_OPTION);
        } while (op == JOptionPane.YES_OPTION);
        
        return responsibleList;
    }

    private static VehicleType configureVehicleTypeOfSpaces(int position, boolean disabilityPresented) {
        String[] types = {"Tipos de vehículo", "1)moto", "2)liviano", "3)pesado", "4)bicicleta", "5)otro"};
        byte[] tires = {0, 2, 4, 8, 12, -1};

        String allTypes = "";
        for (String type : types) {

            allTypes += type + "\n";
        }

        VehicleType vehicleType = new VehicleType();

        byte typeNumber;
        typeNumber = Byte.parseByte(JOptionPane.showInputDialog(allTypes + "\n" + "Ingrese el número del tipo de vehículo del espacio # " + position + " ¿Discapacidad?=" + (disabilityPresented ? "Sí" : "No")));
        vehicleType.setId(typeNumber);
        vehicleType.setDescription(types[typeNumber]);
        vehicleType.setNumberOfTires(tires[typeNumber]);

        return vehicleType;
    }
    
     private static VehicleType selectVehicleType() {
        String[] types = {"1) Moto", "2) Liviano", "3) Pesado", "4) Bicicleta", "5) Otro"};
        float[] fees = {500.0f, 1000.0f, 2500.0f, 300.0f, 1500.0f};
        byte[] tires = {2, 4, 10, 2, 4};

        String menu = "Seleccione el tipo de vehículo:\n";
        for (String type : types) {
            menu += type + "\n";
        }

        int option = Integer.parseInt(JOptionPane.showInputDialog(menu)) - 1;

        VehicleType vt = new VehicleType();
        vt.setId(option + 1);
        vt.setDescription(types[option]);
        vt.setFee(fees[option]);
        vt.setNumberOfTires(tires[option]);

        return vt;
    }
    
    // ====================Todo lo del parqueo===========================
    private static void insertParkingLot() {

        String name = JOptionPane.showInputDialog("Ingrese el nombre del parqueo");
        int numberOfSpaces = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el número de espacios que tiene el parqueo"));
        int numberOfSpacesWithDisabiltyAdaptation = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el número de espacios designados para personas con discapacidad"));

        Space[] spaces = new Space[numberOfSpaces];
        spaces = configureSpaces(spaces, numberOfSpacesWithDisabiltyAdaptation);

        ParkingLot parkingLot = parkingLotController.registerParkingLot(name, spaces);
        parkingLot.setNumberOfSpaces(numberOfSpaces);
        parkingLot.setVehicles(new ArrayList<Vehicle>());

    }
    
    private static ParkingLot selectParkingLot() {

        String parkingLotsInformation = "Lista de parqueos en el sistema\n\n";
        for (ParkingLot parkingLot : parkingLotController.getAllParkingLots()) {

            parkingLotsInformation += "Número de parqueo: " + parkingLot.getId() + " Nombre del parqueo: " + parkingLot.getName() + "\n";

        }
        int option;
        ParkingLot parkingLot;
        do {

            option = Integer.parseInt(JOptionPane.showInputDialog(parkingLotsInformation + "\n Ingrese el número de parqueo que desea consultar\n o en el que desea ingresar un vehículo"));
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

            JOptionPane.showMessageDialog(null, "El número de espacios seleccionados sobrepasa el máximo configurado para este parqueo");
        }

        return spaces;
    }
    
}
