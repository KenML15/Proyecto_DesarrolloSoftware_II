/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import model.data.CustomerDataFile;
import model.entities.Customer;

/**
 *
 * @author 50687
 */
public class CustomerWindow extends JInternalFrame implements ActionListener{
    
    JPanel panelOfInsertCustomerWindow;
    JLabel labelNumber, labelName, labelPhone, labelAddress, labelEmail;
    public JTextField textFieldNumber, textFieldName, textFieldPhone, textFieldAddress, textFieldEmail;
    JButton buttonInsert, buttonCancel;
    CustomerDataFile customerDataFile;
    public String[] dataFromCustomer = new String[5];

    public CustomerWindow() {

        super("Insertar Cliente", false, true, false, true);
        this.setVisible(true);//permite que sea visible
        this.setSize(420, 450);
        this.setLocation(215, 50);
        this.setResizable(false);

        customerDataFile = new CustomerDataFile("Customers");//instanciamos la clase CustomerData

        panelOfInsertCustomerWindow = new JPanel();// Crea el panel
        panelOfInsertCustomerWindow.setLayout(null);//Ubicación
        panelOfInsertCustomerWindow.setBackground(Color.WHITE);//color al panel
        this.add(panelOfInsertCustomerWindow);//adhiere al panel

        labelNumber = new JLabel("Número: ");// crea la etiqueta número
        labelNumber.setBounds(50, 10, 75, 50);//le da tamaño y ubicación
        labelNumber.setForeground(Color.BLUE);// da color
        panelOfInsertCustomerWindow.add(labelNumber);//adhiere la etiqueta

        textFieldNumber = new JTextField(75);// crea el textfield para número
        textFieldNumber.setBounds(125, 25, 200, 25);//le da tamaño y ubicación
        textFieldNumber.setEditable(false);//lo coloca en estado ineditable

        //establece el próximo id del cliente en el textfield
        textFieldNumber.setText("" + (customerDataFile.findLastIdNumberOfCustomer() + 1));

        panelOfInsertCustomerWindow.add(textFieldNumber);//adhiere el textfield

        labelName = new JLabel("Nombre: ");//crea la etiqueta nombre
        labelName.setBounds(50, 70, 75, 50);//le da tamaño y ubicación
        labelName.setForeground(Color.BLUE);// da color 
        panelOfInsertCustomerWindow.add(labelName);//adhiere la etiqueta

        textFieldName = new JTextField(75);// crea el textfield para nombre
        textFieldName.setBounds(125, 85, 200, 25);//le da tamaño y ubicación
        panelOfInsertCustomerWindow.add(textFieldName);//adhiere el textfield

        labelPhone = new JLabel("Teléfono: ");//crea la etiqueta teléfono
        labelPhone.setBounds(50, 140, 75, 50);//le da tamaño y ubicación
        labelPhone.setForeground(Color.BLUE);// da color
        panelOfInsertCustomerWindow.add(labelPhone);//adhiere la etiqueta

        textFieldPhone = new JTextField(75);//crea el textfield para teléfono
        textFieldPhone.setBounds(125, 155, 200, 25);//le da tamaño y ubicación
        panelOfInsertCustomerWindow.add(textFieldPhone);//adhiere el textfield

        labelAddress = new JLabel("Dirección: ");//crea la etiqueta dirección
        labelAddress.setBounds(50, 210, 75, 50);//le da tamaño y ubicación
        labelAddress.setForeground(Color.BLUE);// da color
        panelOfInsertCustomerWindow.add(labelAddress);//adhiere la etiqueta

        textFieldAddress = new JTextField(75);// crea el textfield para dirección
        textFieldAddress.setBounds(125, 225, 200, 25);//le da tamaño y ubicación
        panelOfInsertCustomerWindow.add(textFieldAddress);//adhiere el textfield

        labelEmail = new JLabel("Email: ");//crea la etiqueta email
        labelEmail.setBounds(50, 280, 75, 50);//le da tamaño y ubicación
        labelEmail.setForeground(Color.BLUE);// da color
        panelOfInsertCustomerWindow.add(labelEmail);//adhiere la etiqueta

        textFieldEmail = new JTextField(75);//crea el text para email
        textFieldEmail.setBounds(125, 295, 200, 25);//le da tamaño y ubicación
        panelOfInsertCustomerWindow.add(textFieldEmail);//adhiere el textfield

        buttonInsert = new JButton("Insertar");// crea el boton insertar
        buttonInsert.setBounds(90, 375, 100, 25);//le da tamaño y ubicación
        buttonInsert.setToolTipText("Presione para insertar el cliente");
        buttonInsert.addActionListener(this);
        panelOfInsertCustomerWindow.add(buttonInsert);//adhiere el boton

        buttonCancel = new JButton("Cancelar");// crea el boton insertar
        buttonCancel.setBounds(220, 375, 100, 25);//le da tamaño y ubicación
        buttonCancel.addActionListener(this);//método de escucha del boton
        buttonCancel.setToolTipText("Presione para cancelar");
        panelOfInsertCustomerWindow.add(buttonCancel);//adhiere el boton

        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int numberChosen = JOptionPane.showConfirmDialog(null, "¿Está seguro que desea \n cerrar la ventana?", "Confirmar", JOptionPane.YES_NO_OPTION);
                if (numberChosen == 0) {
                    setVisible(false);
                }
            }// Fin del actionPerformed  
        });// Fin del addActionListener

    } // Fin del construtor CustomerWindow

    /**
     * El método del actionPerformed
     *
     * @param event
     */
    @Override
    public void actionPerformed(ActionEvent event) {

        //forma de saber si el botón fue presionado
        if (event.getActionCommand().equalsIgnoreCase("Insertar")) {

            Customer customer = new Customer();

            //controlamos si alguno de los campos está vacío.
            if (textFieldName.getText().equals("")
                    || textFieldEmail.getText().equals("")
                    || textFieldAddress.getText().equals("")
                    || textFieldPhone.getText().equals("")) {

                JOptionPane.showMessageDialog(null, "Por favor, llene todos los campos del cliente.");

            } else {
                customer.setId(Integer.parseInt(textFieldNumber.getText()));
                customer.setName(textFieldName.getText());
                customer.setEmail(textFieldEmail.getText());
                customer.setAddress(textFieldAddress.getText());
                //customer.setPhone(textFieldPhone.getText());

                int result = customerDataFile.insert(customer);

                if (result == 0) {

                    JOptionPane.showMessageDialog(null, "Cliente insertado exitosamente");

                    //ponemos el id del siguiente cliente por ser insertado:
                    textFieldNumber.setText("" + (customerDataFile.findLastIdNumberOfCustomer() + 1));

                    //limpiamos los campos de inserción.
                    textFieldAddress.setText("");
                    textFieldName.setText("");
                    textFieldEmail.setText("");
                    textFieldAddress.setText("");
                    textFieldPhone.setText("");

                } else {

                    int exception = customerDataFile.exception;

                    if (exception == 1) {

                        JOptionPane.showMessageDialog(null, "Error al abrir "
                                + "o "
                                + "encontrar el archivo de clientes.");

                    } else if (exception == 2) {

                        JOptionPane.showMessageDialog(null,
                                "Error al leer datos del archivo de clientes.");

                    } else if (exception == 3) {

                        JOptionPane.showMessageDialog(null,
                                "El cliente ya existe. Intente de nuevo.");

                    }

                }
            }

        }//fin del action del boton insertar
        else if (event.getActionCommand().equalsIgnoreCase("Modificar")) {

            //controlamos si alguno de los campos está vacío.
            if (textFieldName.getText().equals("")
                    || textFieldEmail.getText().equals("")
                    || textFieldAddress.getText().equals("")
                    || textFieldPhone.getText().equals("")) {

                JOptionPane.showMessageDialog(null, "Por favor, llene todos los campos del cliente.");

            } else {

                Customer customer = new Customer();

                customer.setId(Integer.parseInt(textFieldNumber.getText()));
                customer.setName(textFieldName.getText());
                customer.setEmail(textFieldEmail.getText());
                customer.setAddress(textFieldAddress.getText());
                //customer.setPhone(textFieldPhone.getText());

                String newCustomer = "";
                newCustomer += customer.getId() + ";";
                newCustomer += customer.getName() + ";";
                newCustomer += customer.getEmail() + ";";
                newCustomer += customer.getAddress()+ ";";
                //newCustomer += customer.getPhone();

                //customerDataFile.modifyCustomerFromFile(customerDataFile.getCustomerFromFile(dataFromCustomer[1]/*, dataFromCustomer[2]*/), newCustomer);

                JOptionPane.showMessageDialog(null, "Cliente modificado con éxito.");
                setVisible(false);
                
                CustomerManagement customerManagement= new CustomerManagement();
                customerManagement.setVisible(true);
                JDesktopPane desktopPane= this.getDesktopPane();//obtiene el JDesktopPane en el que se encuentran los JInternalFrames
                desktopPane.add(customerManagement);
            }

        }

    }//Fin del actionPerformed
    
}
