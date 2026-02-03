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
import model.data.VehicleDataFile;
import model.entities.Vehicle;

/**
 *
 * @author 50687
 */
public class VehicleManagement extends JInternalFrame{
    
    //Atributos necesarios
    JButton buttonDelete, buttonEdit;
    JPanel panelVehicle;
    JTable tableVehicle;
    DefaultTableModel modelDataTable;
    
    //Constantes
    final String[] headings = {"Id", "Placa", "Color", "Marca", "Modelo", "Clientes", "Tipo de vehículo", "Espacio", "Tiempo"};

    //Instancia de clases
    VehicleDataFile vehicleDataFile;
    VehicleWindow vehicleWindow;

    public VehicleManagement() {
        super("Gestión de Vehiculos", false, true, false, true);
        this.setVisible(true);//permite que sea visible
        this.setSize(650, 500);
        this.setLocation(230, 50);
        this.setResizable(false);

        panelVehicle = new JPanel();// Crea el panel
        panelVehicle.setLayout(null);//Ubicación
        panelVehicle.setBackground(Color.WHITE);//color al panel
        panelVehicle.setVisible(true);
        this.add(panelVehicle);//adhiere al panel

        vehicleDataFile = new VehicleDataFile("VehiclesTemp");
        vehicleWindow = new VehicleWindow();
        

        //Creación de la tabla que contiene la lista de clientes.
        tableVehicle = new JTable();
        tableVehicle.setSize(400, 1000);
        tableVehicle.setLocation(95, 300);
        panelVehicle.add(tableVehicle);
        JScrollPane scrollBar = new JScrollPane(tableVehicle);
        scrollBar.setBounds(25, 75, 600, 249);
        panelVehicle.add(scrollBar);

        createTable();

        buttonDelete = new JButton("Borrar");
        buttonDelete.setBounds(140, 375, 100, 25);//le da tamaño y ubicación
        buttonDelete.setToolTipText("Presione para borrar un cliente");
        panelVehicle.add(buttonDelete);
        buttonDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int borrar = JOptionPane.showConfirmDialog(null, "¿Está seguro de que desea borrar el vehiculo?\n", "Borrar vehiculo", JOptionPane.YES_NO_OPTION);

                if (borrar == 0) { //este if se para confirmar de que el usario desea borrar

                    removeVehicle();//remueve el cliente
                    cleanTable();//limpia la tabla

                    createTable();//crea de nuevo la tabla

                    JOptionPane.showMessageDialog(null, "Vehiculo borrado con éxito");

                }

            }
        });

        buttonEdit = new JButton("Editar");// crea el boton insertar
        buttonEdit.setBounds(360, 375, 100, 25);//le da tamaño y ubicación
        buttonEdit.setToolTipText("Para modifiar dar doble Click en la Casilla e ingrese el numero o palabra correcta");
        panelVehicle.add(buttonEdit);
        buttonEdit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                vehicleWindow.dataFromVehicle = fillVehicleFormToModify();
                
             
            }// FIN DEL METODO ACTION PERFORMED
        });

    }

    public String[] fillVehicleFormToModify() {
    
        JDesktopPane desktopPane= this.getDesktopPane();//obtiene el JDesktopPane en el que se encuentran los JInternalFrames
        this.dispose();//cierra la ventana actual para que se vea la de modificar 
        vehicleWindow.setTitle("Modificar Vehiculos");
        vehicleWindow.setVisible(true);
        
        desktopPane.add(vehicleWindow);

        String[] dataFromVehicleToModify = getValueFromTable();

        vehicleWindow.textFieldId.setText(dataFromVehicleToModify[0]);
        vehicleWindow.textFieldPlate.setText(dataFromVehicleToModify[1]);
        vehicleWindow.textFieldModel.setText(dataFromVehicleToModify[2]);
        vehicleWindow.textFieldBrand.setText(dataFromVehicleToModify[3]);
        vehicleWindow.textFieldColor.setText(dataFromVehicleToModify[4]);
        
        vehicleWindow.buttonInsert.setText("Modificar");
        
        return dataFromVehicleToModify;
    }

    public void createTable() {
        ArrayList<Vehicle> vehicles
                = vehicleDataFile.getAllVehicles();

        String[][] vehicleToShow
                = vehicleDataFile.createVehicleMatrix(vehicles);

        modelDataTable
                = new DefaultTableModel(vehicleToShow, headings);

        tableVehicle.setModel(modelDataTable);
    }
    
    public void fillTable(ArrayList<Vehicle> vehicleList) {
        // 1. Crear el modelo con los encabezados
        DefaultTableModel model = new DefaultTableModel(null, headings);

        // 2. Recorrer la lista y añadir filas una por una
        for (Vehicle v : vehicleList) {
            Object[] row = new Object[9];
            row[0] = v.getId();
            row[1] = v.getPlate();
            row[2] = v.getColor();
            row[3] = v.getBrand();
            row[4] = v.getModel();

            // Para evitar errores si el objeto es nulo, usamos un "si-no" rápido
            row[5] = (v.getCustomer() != null) ? "Cliente Registrado" : "N/A";
            row[6] = "Tipo"; // O el valor que corresponda
            row[7] = "Espacio";
            row[8] = v.getEntryTime(); // Suponiendo que ya es String

            model.addRow(row);
        }

        // 3. Setear el modelo a la tabla
        tableVehicle.setModel(model);
    }

    private void cleanTable() {

        DefaultTableModel newModel = (DefaultTableModel) tableVehicle.getModel();
        newModel.setNumRows(0);
    }

    public int getVehicleSelected() {

        int rowSelected = tableVehicle.getSelectedRow();

        return rowSelected;
    }

    public String[] getValueFromTable() {
        int row = tableVehicle.getSelectedRow();
        String[] valuesToReturn = new String[9]; // 9 columnas
        for (int i = 0; i < 9; i++) {
            valuesToReturn[i] = tableVehicle.getValueAt(row, i).toString();
        }
        return valuesToReturn;

        /*int id = Integer.parseInt(tableVehicle.getModel().
                getValueAt(getCustomerSelected(), 0).toString());

        String name
                = tableVehicle.getModel().
                getValueAt(getCustomerSelected(), 1).toString();

        String email
                = tableVehicle.getModel().
                getValueAt(getCustomerSelected(), 2).toString();

        String address
                = tableVehicle.getModel().
                getValueAt(getCustomerSelected(), 3).toString();

        String phone
                = tableVehicle.getModel().
                getValueAt(getCustomerSelected(), 4).toString();

        String valuesToReturn[] = new String[5];
        valuesToReturn[0] = "" + id;
        valuesToReturn[1] = name;
        valuesToReturn[2] = email;
        valuesToReturn[3] = address;
        valuesToReturn[4] = phone;

        return valuesToReturn;*/
    }

    public void removeVehicle() {
        // 1. Obtenemos los valores de la fila seleccionada en un arreglo de Strings
        String[] selectedVehicleData = getValueFromTable();

        // 2. Reconstruimos la "línea original" tal cual está en el archivo .txt
        // Usamos el delimitador ";" para unir los 9 campos
        String lineToRemove = "";
        for (int i = 0; i < selectedVehicleData.length; i++) {
            lineToRemove += selectedVehicleData[i];
            if (i < selectedVehicleData.length - 1) {
                lineToRemove += ";";
            }
        }
        
        // 3. Ahora sí, pasamos la STRING al método que la eliminará
        vehicleDataFile.deleteVehicleFromFile(lineToRemove);

        // 4. Refrescamos la tabla para que el vehículo desaparezca visualmente
        fillTable(vehicleDataFile.getAllVehicles());

        //vehicleDataFile.deleteVehicleFromFile(vehicleDataFile.getVehicleFromFile(getValueFromTable()[1]));
    }
}
