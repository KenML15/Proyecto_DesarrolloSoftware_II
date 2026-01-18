/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package view;

import controller.CustomerController;
import controller.VehicleController;
import javax.swing.JOptionPane;
import model.entity.Customer;
import model.entity.Vehicle;
import model.entity.VehicleType;

/**
 *
 * @author user
 */
public class Proyecto_DesarrolloSoftware_II {
    static CustomerController customerController = new CustomerController();
    static VehicleController vehicleController = new VehicleController();
    

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
                    + "\n Ingrese 5 para mostrar la lista de vehículos"));
            
            switch (choice) {
                case 0 -> {
                    JOptionPane.showMessageDialog(null, "Gracias por usar el sistema. \n ¡Hasta luego!");
                }
                case 1 -> {
                    insertCustomer();
                }
                case 2 -> {
                    removeCustomer();
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
        boolean disabilityPresented = false;

        Customer customerToInsert = new Customer(id, name, disabilityPresented);

        JOptionPane.showMessageDialog(null, customerController.
                insertCustomer(customerToInsert));

        return customerToInsert;
    }

    private static void removeCustomer() {
        String id = JOptionPane.showInputDialog("Ingrese la cédula del cliente a eliminar");
        String plate = JOptionPane.showInputDialog("Ingrese la placa del vehiculo del cliente a eliminar");
        // Llamamos al controlador pasando solo el ID
        String mensaje = customerController.removeCustomer(id);

        JOptionPane.showMessageDialog(null, "El cliente ha sido removido de forma correcta del sistema");
    } 
    
    // ====================Todo lo del vehículo===========================
    
    public static void showAllVehicles(){
        JOptionPane.showMessageDialog(null, vehicleController.getAllVehicles().toString());
    }
    
    private static void insertVehicle(){
        String plate = JOptionPane.showInputDialog("Ingrese la placa del vehículo");
        String color = JOptionPane.showInputDialog("Ingrese el color del vehículo");
        String brand = JOptionPane.showInputDialog("Ingrese la marca del vehículo");
        String model = JOptionPane.showInputDialog("Ingrese el modelo del vehículo");

        Customer customer = insertCustomer();
        VehicleType vehicleType = new VehicleType();
        
        Vehicle vehicleToInsert = new Vehicle(plate, color, brand, model, customer, vehicleType);
        
        vehicleController.insertVehicle(vehicleToInsert);
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
    
    // ====================Todo lo del parqueo===========================
    
    
}
