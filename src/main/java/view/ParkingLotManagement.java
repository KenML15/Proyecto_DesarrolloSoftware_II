/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import model.data.CustomerDataFile;
import model.entities.Customer;

/**
 *
 * @author 50687
 */
public class ParkingLotManagement extends JInternalFrame{
    
    //Atributos necesarios
    JButton buttonDelete, buttonEdit;
    JPanel panelCustomers;
    JTable tableCustomers;
    DefaultTableModel modelDataTable;
    
    //Constantes
    final String[] headings = {"Id", "Nombre", "Correo", "Dirección", "Telefóno"};

    //Instancia de clases
    CustomerDataFile customerDataFile;
    CustomerWindow customerWindow;

    public ParkingLotManagement() {
        super("Gestión de Clientes", false, true, false, true);
        this.setVisible(true);//permite que sea visible
        this.setSize(650, 500);
        this.setLocation(230, 50);
        this.setResizable(false);

        panelCustomers = new JPanel();// Crea el panel
        panelCustomers.setLayout(null);//Ubicación
        panelCustomers.setBackground(Color.WHITE);//color al panel
        panelCustomers.setVisible(true);
        this.add(panelCustomers);//adhiere al panel

        customerDataFile = new CustomerDataFile("Customers");
        customerWindow = new CustomerWindow();
        

        //Creación de la tabla que contiene la lista de clientes.
        tableCustomers = new JTable();
        tableCustomers.setSize(400, 1000);
        tableCustomers.setLocation(95, 300);
        panelCustomers.add(tableCustomers);
        JScrollPane scrollBar = new JScrollPane(tableCustomers);
        scrollBar.setBounds(25, 75, 600, 249);
        panelCustomers.add(scrollBar);

        createTable();

        buttonDelete = new JButton("Borrar");
        buttonDelete.setBounds(140, 375, 100, 25);//le da tamaño y ubicación
        buttonDelete.setToolTipText("Presione para borrar un cliente");
        panelCustomers.add(buttonDelete);
        buttonDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int borrar = JOptionPane.showConfirmDialog(null, "¿Está seguro de que desea borrar el cliente?\n", "Borrar Cliente", JOptionPane.YES_NO_OPTION);

                if (borrar == 0) { //este if se para confirmar de que el usario desea borrar

                    removeCustomer();//remueve el cliente
                    cleanTable();//limpia la tabla

                    createTable();//crea de nuevo la tabla

                    JOptionPane.showMessageDialog(null, "Cliente borrado con éxito");

                }//Fin del if de borrar

            }//FIN DEL ACTIONPERFORMED
        });

        buttonEdit = new JButton("Editar");// crea el boton insertar
        buttonEdit.setBounds(360, 375, 100, 25);//le da tamaño y ubicación
        buttonEdit.setToolTipText("Para modifiar dar doble Click en la Casilla e ingrese el numero o palabra correcta");
        panelCustomers.add(buttonEdit);
        buttonEdit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                customerWindow.dataFromCustomer=fillCustomerFormToModify();
                
             
            }// FIN DEL METODO ACTION PERFORMED
        });

    }//Fin de constructor

    public String[] fillCustomerFormToModify() {
    
        JDesktopPane desktopPane= this.getDesktopPane();//obtiene el JDesktopPane en el que se encuentran los JInternalFrames
        this.dispose();//cierra la ventana actual para que se vea la de modificar 
        customerWindow.setTitle("Modificar Cliente");
        customerWindow.setVisible(true);
        
        desktopPane.add(customerWindow);

        String[] dataFromCustomerToModify = getValueFromTable();

        customerWindow.textFieldNumber.setText(dataFromCustomerToModify[0]);
        customerWindow.textFieldName.setText(dataFromCustomerToModify[1]);
        customerWindow.textFieldEmail.setText(dataFromCustomerToModify[2]);
        //customerWindow.textFieldAddress.setText(dataFromCustomerToModify[3]);
        customerWindow.textFieldPhone.setText(dataFromCustomerToModify[4]);
        
        customerWindow.buttonInsert.setText("Modificar");
        
        return dataFromCustomerToModify;
    }

    public void createTable() {
        ArrayList<Customer> customers
                = customerDataFile.getAllCustomers();

        String[][] customersToShow
                = customerDataFile.createCustomerMatrix(customers);

        modelDataTable
                = new DefaultTableModel(customersToShow, headings);

        tableCustomers.setModel(modelDataTable);
    }

    private void cleanTable() {

        DefaultTableModel newModel = (DefaultTableModel) tableCustomers.getModel();
        newModel.setNumRows(0);
    }

    public int getCustomerSelected() {

        int rowSelected = tableCustomers.getSelectedRow();

        return rowSelected;
    }

    public String[] getValueFromTable() {

        int id = Integer.parseInt(tableCustomers.getModel().
                getValueAt(getCustomerSelected(), 0).toString());

        String name
                = tableCustomers.getModel().
                getValueAt(getCustomerSelected(), 1).toString();

        String email
                = tableCustomers.getModel().
                getValueAt(getCustomerSelected(), 2).toString();

        String address
                = tableCustomers.getModel().
                getValueAt(getCustomerSelected(), 3).toString();

        String phone
                = tableCustomers.getModel().
                getValueAt(getCustomerSelected(), 4).toString();

        String valuesToReturn[] = new String[5];
        valuesToReturn[0] = "" + id;
        valuesToReturn[1] = name;
        valuesToReturn[2] = email;
        valuesToReturn[3] = address;
        valuesToReturn[4] = phone;

        return valuesToReturn;
    }

    public void removeCustomer() {

        //customerDataFile.deleteCustomerFromFile(customerDataFile.getCustomerFromFile(getValueFromTable()[1], getValueFromTable()[2]));

    }
}
