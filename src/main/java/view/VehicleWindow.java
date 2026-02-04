/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import static java.time.format.DateTimeFormatter.ofPattern;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import model.data.CustomerDataFile;
import model.data.VehicleDataFile;
import model.entities.Customer;
import model.entities.Vehicle;

/**
 *
 * @author user
 */
public class VehicleWindow extends JInternalFrame implements ActionListener{
    
    JPanel panelOfInsertVehicleWindow;
    JLabel labelId, labelPlate, labelColor, labelBrand, labelModel, labelCustomer, labelVehicleType, labelSpace;
    public JTextField textFieldId, textFieldPlate, textFieldColor, textFieldBrand, textFieldModel, textFieldCustomer, textFieldVehicleType, textFieldSpace;
    JButton buttonInsert, buttonCancel;
    VehicleDataFile vehicleDataFile;
    public String[] dataFromVehicle = new String[9];

    public VehicleWindow() {

        super("Insertar Cliente", false, true, false, true);
        this.setVisible(true);//permite que sea visible
        this.setSize(450, 650);
        this.setLocation(215, 30);
        this.setResizable(false);

        vehicleDataFile = new VehicleDataFile("Vehicles");//instanciamos la clase CustomerData

        panelOfInsertVehicleWindow = new JPanel();// Crea el panel
        panelOfInsertVehicleWindow.setLayout(null);//Ubicación
        panelOfInsertVehicleWindow.setBackground(Color.WHITE);//color al panel
        this.add(panelOfInsertVehicleWindow);//adhiere al panel

        labelId = new JLabel("ID: ");// crea la etiqueta número
        labelId.setBounds(50, 20, 100, 30);//le da tamaño y ubicación
        labelId.setForeground(Color.BLUE);// da color
        panelOfInsertVehicleWindow.add(labelId);//adhiere la etiqueta

        textFieldId = new JTextField(75);// crea el textfield para número
        textFieldId.setBounds(180, 25, 200, 25);//le da tamaño y ubicación
        textFieldId.setEditable(false);//lo coloca en estado ineditable

        //establece el próximo id del cliente en el textfield
        textFieldId.setText("" + (vehicleDataFile.findLastIdNumberOfVehicle() + 1));

        panelOfInsertVehicleWindow.add(textFieldId);//adhiere el textfield

        labelPlate = new JLabel("Placa: ");//crea la etiqueta nombre
        labelPlate.setBounds(50, 70, 100, 30);//le da tamaño y ubicación
        labelPlate.setForeground(Color.BLUE);// da color 
        panelOfInsertVehicleWindow.add(labelPlate);//adhiere la etiqueta

        textFieldPlate = new JTextField(75);// crea el textfield para nombre
        textFieldPlate.setBounds(180, 75, 200, 25);//le da tamaño y ubicación
        panelOfInsertVehicleWindow.add(textFieldPlate);//adhiere el textfield

        labelColor = new JLabel("Color: ");//crea la etiqueta teléfono
        labelColor.setBounds(50, 120, 100, 30);//le da tamaño y ubicación
        labelColor.setForeground(Color.BLUE);// da color
        panelOfInsertVehicleWindow.add(labelColor);//adhiere la etiqueta

        textFieldColor = new JTextField(75);//crea el textfield para teléfono
        textFieldColor.setBounds(180, 125, 200, 25);//le da tamaño y ubicación
        panelOfInsertVehicleWindow.add(textFieldColor);//adhiere el textfield

        labelBrand = new JLabel("Marca: ");//crea la etiqueta dirección
        labelBrand.setBounds(50, 170, 100, 30);//le da tamaño y ubicación
        labelBrand.setForeground(Color.BLUE);// da color
        panelOfInsertVehicleWindow.add(labelBrand);//adhiere la etiqueta

        textFieldBrand = new JTextField(75);// crea el textfield para dirección
        textFieldBrand.setBounds(180, 175, 200, 25);//le da tamaño y ubicación
        panelOfInsertVehicleWindow.add(textFieldBrand);//adhiere el textfield

        labelModel = new JLabel("Modelo: ");//crea la etiqueta email
        labelModel.setBounds(50, 220, 100, 30);//le da tamaño y ubicación
        labelModel.setForeground(Color.BLUE);// da color
        panelOfInsertVehicleWindow.add(labelModel);//adhiere la etiqueta

        textFieldModel = new JTextField(75);//crea el text para email
        textFieldModel.setBounds(180, 225, 200, 25);//le da tamaño y ubicación
        panelOfInsertVehicleWindow.add(textFieldModel);//adhiere el textfield
        
        labelCustomer = new JLabel("ID Clientes: ");//crea la etiqueta email
        labelCustomer.setBounds(50, 270, 100, 30);//le da tamaño y ubicación
        labelCustomer.setForeground(Color.BLUE);// da color
        panelOfInsertVehicleWindow.add(labelCustomer);//adhiere la etiqueta

        textFieldCustomer = new JTextField(75);//crea el text para email
        textFieldCustomer.setBounds(180, 275, 200, 25);//le da tamaño y ubicación
        panelOfInsertVehicleWindow.add(textFieldCustomer);//adhiere el textfield
        
        labelVehicleType = new JLabel("ID Tipo de vehículo: ");//crea la etiqueta email
        labelVehicleType.setBounds(50, 320, 120, 30);//le da tamaño y ubicación
        labelVehicleType.setForeground(Color.BLUE);// da color
        panelOfInsertVehicleWindow.add(labelVehicleType);//adhiere la etiqueta

        textFieldVehicleType = new JTextField(75);//crea el text para email
        textFieldVehicleType.setBounds(180, 325, 200, 25);//le da tamaño y ubicación
        panelOfInsertVehicleWindow.add(textFieldVehicleType);//adhiere el textfield
        
        labelSpace = new JLabel("ID Espacio: ");//crea la etiqueta email
        labelSpace.setBounds(50, 370, 100, 30);//le da tamaño y ubicación
        labelSpace.setForeground(Color.BLUE);// da color
        panelOfInsertVehicleWindow.add(labelSpace);//adhiere la etiqueta

        textFieldSpace = new JTextField(75);//crea el text para email
        textFieldSpace.setBounds(180, 375, 200, 25);//le da tamaño y ubicación
        panelOfInsertVehicleWindow.add(textFieldSpace);//adhiere el textfield
        
        //Botones
        buttonInsert = new JButton("Insertar");// crea el boton insertar
        buttonInsert.setBounds(100, 450, 100, 30);//le da tamaño y ubicación
        buttonInsert.setToolTipText("Presione para insertar el vehículo");
        buttonInsert.addActionListener(this);
        panelOfInsertVehicleWindow.add(buttonInsert);//adhiere el boton

        buttonCancel = new JButton("Cancelar");// crea el boton insertar
        buttonCancel.setBounds(230, 450, 100, 30);//le da tamaño y ubicación
        buttonCancel.addActionListener(this);//método de escucha del boton
        buttonCancel.setToolTipText("Presione para cancelar");
        panelOfInsertVehicleWindow.add(buttonCancel);//adhiere el boton

        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int numberChosen = JOptionPane.showConfirmDialog(null, "¿Está seguro que desea \n cerrar la ventana?", "Confirmar", JOptionPane.YES_NO_OPTION);
                if (numberChosen == 0) {
                    setVisible(false);
                }
            }
        });

    }
    
    @Override
    public void actionPerformed(ActionEvent event) {

        //forma de saber si el botón fue presionado
        if (event.getActionCommand().equalsIgnoreCase("Insertar")) {

            Vehicle vehicle = new Vehicle();

            //controlamos si alguno de los campos está vacío.
            if (textFieldPlate.getText().equals("")
                    || textFieldModel.getText().equals("")
                    || textFieldBrand.getText().equals("")
                    || textFieldColor.getText().equals("")
                    || textFieldCustomer.getText().equals("")
                    || textFieldVehicleType.getText().equals("")) {

                JOptionPane.showMessageDialog(null, "Por favor, llene todos los campos del vehiculo.");

            } else {
                vehicle.setId(Integer.parseInt(textFieldId.getText()));
                vehicle.setPlate(textFieldPlate.getText());
                vehicle.setModel(textFieldModel.getText());
                vehicle.setBrand(textFieldBrand.getText());
                vehicle.setColor(textFieldColor.getText());
                
                CustomerDataFile customerData = new CustomerDataFile("CustomersTemp");
                int customerId = Integer.parseInt(textFieldCustomer.getText());
                Customer customerFound = customerData.getCustomerFromFile(customerId);

                if (customerFound != null) {
                    // Como tu entidad pide un ArrayList<Customer>, creamos uno rápido
                    ArrayList<Customer> customerList = new ArrayList<>();
                    customerList.add(customerFound);
                    vehicle.setCustomer(customerList);
                } else {
                    JOptionPane.showMessageDialog(null, "Error: El ID de cliente no existe.");
                    return; // Detiene el proceso
                }
                
                int result = vehicleDataFile.insertVehicle(vehicle);

                if (result == 0) {

                    JOptionPane.showMessageDialog(null, "Cliente insertado exitosamente");

                    //ponemos el id del siguiente cliente por ser insertado:
                    textFieldId.setText("" + (vehicleDataFile.findLastIdNumberOfVehicle() + 1));

                    //limpiamos los campos de inserción.
                    textFieldBrand.setText("");
                    textFieldPlate.setText("");
                    textFieldModel.setText("");
                    textFieldBrand.setText("");
                    textFieldColor.setText("");

                } else {

                    int exception = vehicleDataFile.exception;

                    if (exception == 1) {

                        JOptionPane.showMessageDialog(null, "Error al abrir "
                                + "o "
                                + "encontrar el archivo de vehiculos.");

                    } else if (exception == 2) {

                        JOptionPane.showMessageDialog(null,
                                "Error al leer datos del archivo de vehiculos.");

                    } else if (exception == 3) {

                        JOptionPane.showMessageDialog(null,
                                "El vehiculo ya existe. Intente de nuevo.");

                    }

                }
            }

        } else if (event.getActionCommand().equalsIgnoreCase("Modificar")) {

            //controlamos si alguno de los campos está vacío.
            if (textFieldPlate.getText().equals("")
                    || textFieldModel.getText().equals("")
                    || textFieldBrand.getText().equals("")
                    || textFieldColor.getText().equals("")) {

                JOptionPane.showMessageDialog(null, "Por favor, llene todos los campos del vehiculo.");

            } else {

                Vehicle vehicle = vehicleDataFile.getVehicleFromFile(dataFromVehicle[1]);
                
                String oldLine = "";
                for (int i = 0; i < dataFromVehicle.length; i++) {
                    oldLine += dataFromVehicle[i] + (i == dataFromVehicle.length - 1 ? "" : ";");
                }

                /*vehicle.setId(Integer.parseInt(textFieldId.getText()));
                vehicle.setPlate(textFieldPlate.getText());
                vehicle.setColor(textFieldColor.getText());
                vehicle.setBrand(textFieldBrand.getText());
                vehicle.setModel(textFieldModel.getText());
                
                CustomerDataFile customerData = new CustomerDataFile("CustomersTemp");
                int customerId = Integer.parseInt(textFieldCustomer.getText());
                Customer customerFound = customerData.getCustomerFromFile(customerId);

                if (customerFound != null) {
                    // Como tu entidad pide un ArrayList<Customer>, creamos uno rápido
                    ArrayList<Customer> customerList = new ArrayList<>();
                    customerList.add(customerFound);
                    vehicle.setCustomer(customerList);
                } else {
                    JOptionPane.showMessageDialog(null, "Error: El ID de cliente no existe.");
                    return; // Detiene el proceso
                }

                String newVehicle = "";
                newVehicle += vehicle.getId() + ";";
                newVehicle += vehicle.getPlate() + ";";
                newVehicle += vehicle.getColor() + ";";
                newVehicle += vehicle.getBrand() + ";";
                newVehicle += vehicle.getModel() + ";";
                newVehicle += vehicle.getCustomer() + ";";
                newVehicle += textFieldVehicleType.getText() + ";";
                newVehicle += textFieldSpace.getText() + ";";
                newVehicle += vehicle.getEntryTime() + ";";*/
                
                String newLine = textFieldId.getText() + ";"
                + textFieldPlate.getText() + ";"
                + textFieldColor.getText() + ";"
                + textFieldBrand.getText() + ";"
                + textFieldModel.getText() + ";"
                + textFieldCustomer.getText() + ";"
                + textFieldVehicleType.getText() + ";"
                + textFieldSpace.getText() + ";"
                + dataFromVehicle[8];
                
                
                vehicleDataFile.modifyVehicleFromFile(oldLine, newLine);

                //customerDataFile.modifyCustomerFromFile(customerDataFile.getCustomerFromFile(dataFromCustomer[1]/*, dataFromCustomer[2]*/), newCustomer);

                JOptionPane.showMessageDialog(null, "Vehicle modificado con éxito.");
                setVisible(false);
                
                VehicleManagement vehicleManagement= new VehicleManagement();
                vehicleManagement.setVisible(true);
                JDesktopPane desktopPane= this.getDesktopPane();//obtiene el JDesktopPane en el que se encuentran los JInternalFrames
                desktopPane.add(vehicleManagement);
            }
        }
    }  
}
