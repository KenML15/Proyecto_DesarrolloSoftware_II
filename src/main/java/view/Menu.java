/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import GUI.HomeDesktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.WindowConstants;

/**
 *
 * @author 50687
 */
public class Menu extends JFrame{
    public Menu() {

        super("Nombre del programa");
        
        final HomeDesktop desktop = new HomeDesktop();// Obtiene la clase HomeDesktop.
        this.add(desktop);// Pega el HomeDesktop.
        this.setSize(1000, 700);// Tamaño.
        this.setVisible(true);// Hace visible la ventana.
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false); // Hace la ventana no maximizable.

        //Barra de menú
        JMenuBar menuBar = new JMenuBar(); // Crea la menuBar del menú.
        this.setJMenuBar(menuBar);

        //Menu de clientes
        JMenu customerMenu = new JMenu("Clientes");// Crea el menú Customer.
        menuBar.add(customerMenu);

        JMenuItem insertCustomerMenuOption = new JMenuItem("Insertar");
        customerMenu.add(insertCustomerMenuOption);
        
        insertCustomerMenuOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                desktop.add(new CustomerWindow());

            }// Fin del actionPerformed
        });// Fin del addActionListener
        
        
        JMenuItem viewCustomerMenuOption = new JMenuItem("Gestionar");
        customerMenu.add(viewCustomerMenuOption);
        
        viewCustomerMenuOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                desktop.add(new CustomerManagement());

            }// Fin del actionPerformed
        });// Fin del addActionListener
        
        //Menu de vehiculos
        JMenu vehicleMenu = new JMenu("Vehículos");
        menuBar.add(vehicleMenu);
        
        JMenuItem insertVehicle = new JMenuItem("Insertar");
        vehicleMenu.add(insertVehicle);
        insertVehicle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VehicleWindow vWin = new VehicleWindow();
                desktop.add(vWin);
                vWin.toFront();
            }
        });
        
        JMenuItem manageVehicle = new JMenuItem("Gestionar");
        vehicleMenu.add(manageVehicle);
        manageVehicle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VehicleManagement vManage = new VehicleManagement();
                desktop.add(vManage);
                vManage.toFront();
            }
        });
        
        menuBar.updateUI(); //Refresca interfaz gráfica

    }//Fin del constructor
}
