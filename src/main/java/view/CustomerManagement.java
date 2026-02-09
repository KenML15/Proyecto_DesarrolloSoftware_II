/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.CustomerFileController;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableModel;
import model.entities.Customer;

/**
 *
 * @author 50687
 */
public class CustomerManagement extends JInternalFrame {

    private CustomerFileController controller;
    private JTable customerTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;

    private final String[] COLUMNS = {"ID", "Nombre", "Discapacidad", "Email", "Dirección", "Teléfono"};
    
    public CustomerManagement() {
        super("Gestión de Clientes", true, true, true, true);
        setupController();
        createInterface();
        loadAllCustomers();
    }
    
    //Setup de controller
    private void setupController() {
        try {
            controller = new CustomerFileController("Customers.txt");
        } catch (Exception e) {
            showMessage("Error al iniciar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            this.dispose();
        }
    }
    
    //Crea la interfaz de usuario
    private void createInterface() {
        this.setSize(900, 550);
        this.setLocation(50, 50);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        //Panel de busqueda
        mainPanel.add(createSearchPanel(), BorderLayout.NORTH);
        
        //Tabla con los clientes
        mainPanel.add(createTablePanel(), BorderLayout.CENTER);
        
        //Botones de acción
        mainPanel.add(createButtonsPanel(), BorderLayout.SOUTH);
        
        this.setContentPane(mainPanel);
    }
    
    //Crea un panel de busqueda con botones
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JLabel label = new JLabel("Buscar:");
        panel.add(label);
        
        searchField = new JTextField(20);
        panel.add(searchField);
        
        JButton searchButton = new JButton("Buscar");
        searchButton.addActionListener(e -> searchCustomers());
        panel.add(searchButton);
        
        JButton showAllButton = new JButton("Mostrar Todos");
        showAllButton.addActionListener(e -> loadAllCustomers());
        panel.add(showAllButton);
        
        return panel;
    }
    
    //Crea una tabla para mostrar a los clientes
    private JScrollPane createTablePanel() {
        tableModel = new DefaultTableModel(COLUMNS, 0);
        customerTable = new JTable(tableModel);
        
        //Solo se puede seleccionar una fila a la vez
        customerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        return new JScrollPane(customerTable);
    }
    
    //Crea un panel con botones de acción
    private JPanel createButtonsPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        
        panel.add(createButton("Agregar", e -> openAddWindow()));
        panel.add(createButton("Editar", e -> openEditWindow()));
        panel.add(createButton("Eliminar", e -> deleteCustomer()));
        panel.add(createButton("Actualizar", e -> loadAllCustomers()));
        
        return panel;
    }
    
    //Ayuda a crear botones (es para que haya más modularidad)
    private JButton createButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.addActionListener(action);
        return button;
    }
    
    //Carga o trae todos los clientes del controlador
    private void loadAllCustomers() {
        try {
            ArrayList<Customer> customers = controller.getAllCustomers();
            showCustomersInTable(customers);
            searchField.setText("");
        } catch (Exception e) {
            showError("No se pudieron cargar los clientes: " + e.getMessage());
        }
    }
    
    //Muestra a los clientes en la tabla 
    private void showCustomersInTable(ArrayList<Customer> customers) {
        //limpia la tabla
        tableModel.setRowCount(0);
        
        //Añade a cada cliente en una fila
        for (Customer customer : customers) {
            addCustomerToTable(customer);
        }
    }
    
    //Añade a un cliente en la tabla
    private void addCustomerToTable(Customer customer) {
        Object[] row = {
            customer.getId(),
            customer.getName(),
            customer.isDisabilityPresented() ? "Sí" : "No",
            customer.getEmail(),
            customer.getAddress(),
            customer.getPhoneNumber()
        };
        
        tableModel.addRow(row);
    }
    
    //Buscador de clientes
    private void searchCustomers() {
        String searchText = searchField.getText().trim();
        
        if (searchText.isEmpty()) {
            loadAllCustomers();
            return;
        }
        
        try {
            ArrayList<Customer> results = controller.searchCustomers(searchText);
            showCustomersInTable(results);
        } catch (Exception e) {
            showError("Error en la búsqueda: " + e.getMessage());
        }
    }
    
    //Ventana para añadir a un nuevo cliente
    private void openAddWindow() {
        openCustomerWindow(null);
    }
    
    //Ventana para editar al cliente seleccionado
    private void openEditWindow() {
        int selectedRow = customerTable.getSelectedRow();
        
        if (selectedRow < 0) {
            showWarning("Seleccione un cliente de la tabla");
            return;
        }
        
        try {
            int customerId = (int) tableModel.getValueAt(selectedRow, 0);
            Customer customer = controller.getCustomerById(customerId);
            
            if (customer != null) {
                openCustomerWindow(customer);
            }
        } catch (Exception e) {
            showError("Error: " + e.getMessage());
        }
    }
    
    //Elimina al cliente seleccionado
    private void deleteCustomer() {
        int selectedRow = customerTable.getSelectedRow();
        
        if (selectedRow < 0) {
            showWarning("Seleccione un cliente de la tabla");
            return;
        }
        
        int customerId = (int) tableModel.getValueAt(selectedRow, 0);
        String customerName = (String) tableModel.getValueAt(selectedRow, 1);
        
        //Pregunta de confirmación
        int answer = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de eliminar a " + customerName + "?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION);
        
        if (answer == JOptionPane.YES_OPTION) {
            try {
                controller.deleteCustomer(customerId);
                showInfo("Cliente eliminado correctamente");
                loadAllCustomers(); //Refresca la tablaº    
            } catch (Exception e) {
                showError("Error al eliminar: " + e.getMessage());
            }
        }
    }
    
    //Ventana de clientes
    private void openCustomerWindow(Customer customer) {
        try {
            CustomerWindow window;
            
            if (customer == null) {
                window = new CustomerWindow();
            } else {
                window = new CustomerWindow(customer);
            }
            
            //Añade la ventana al desktop
            getDesktopPane().add(window);
            window.toFront();
            
            //Cuando la ventana se cierra la tabla se refresca
            window.addInternalFrameListener(new InternalFrameAdapter() {
                public void internalFrameClosed(InternalFrameEvent e) {
                    loadAllCustomers();
                }
            });
            
        } catch (Exception e) {
            showError("Error al abrir ventana: " + e.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }

    private void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showMessage(String text, String title, int type) {
        JOptionPane.showMessageDialog(this, text, title, type);
    }
}