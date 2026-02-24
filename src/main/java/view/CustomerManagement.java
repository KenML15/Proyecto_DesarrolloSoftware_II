/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.CustomerFileController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableModel;
import model.entities.Customer;

/**
 *
 * @author 50687
 */
public class CustomerManagement extends BaseInternalFrame {

    private CustomerFileController controller;
    private JTable customerTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private final String[] COLUMNS = {"ID", "Nombre", "Discapacidad", "Email", "Dirección", "Teléfono"};

    public CustomerManagement() {
        super("GESTIÓN DE CLIENTES"); //Título para la barra superior
        setupController();
        createInterface();
        loadAllCustomers();
        SwingUtilities.invokeLater(() -> centerInDesktop());
    }

    private void setupController() {
        try {
            controller = new CustomerFileController("Customers.txt");
        } catch (IOException e) {
            showError("No se puede acceder al archivo: " + e.getMessage());
            this.dispose();
        }
    }

    private void createInterface() {
        this.setSize(1000, 600); //Un poco más ancha para la tabla
        
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(backgroundColor);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        //Barra de busqueda
        mainPanel.add(createTopPanel(), BorderLayout.NORTH);

        //Tabla con los datos del cliente
        mainPanel.add(createTablePanel(), BorderLayout.CENTER);

        //Acciones
        mainPanel.add(createActionsPanel(), BorderLayout.SOUTH);

        this.setContentPane(mainPanel);
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        //Buscador
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchPanel.setOpaque(false);
        
        JLabel lblSearch = new JLabel("Buscar Cliente:");
        lblSearch.setFont(labelFont);
        
        searchField = new JTextField(25);
        styleTextField(searchField); //Método de BaseInternalFrame
        
        JButton btnSearch = new JButton("BUSCAR");
        styleButton(btnSearch);
        btnSearch.addActionListener(e -> searchCustomers());

        searchPanel.add(lblSearch);
        searchPanel.add(searchField);
        searchPanel.add(btnSearch);

        //Esto es para refrescar y mostrar todos los clientes
        JButton btnReload = new JButton("MOSTRAR TODOS");
        styleButton(btnReload);
        btnReload.setBackground(accentColor);//Le puse un color diferente para que sea más distintivo
        btnReload.addActionListener(e -> loadAllCustomers());

        panel.add(searchPanel, BorderLayout.WEST);
        panel.add(btnReload, BorderLayout.EAST);

        return panel;
    }

    private JScrollPane createTablePanel() {
        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        customerTable = new JTable(tableModel);
        styleTable(customerTable); //Método de BaseInternalFrame
        
        JScrollPane scroll = new JScrollPane(customerTable);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        scroll.getViewport().setBackground(Color.WHITE);
        
        return scroll;
    }

    private JPanel createActionsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        panel.setOpaque(false);

        JButton btnAdd = new JButton("NUEVO CLIENTE");
        JButton btnEdit = new JButton("EDITAR");
        JButton btnDelete = new JButton("ELIMINAR");

        styleButton(btnAdd);
        styleButton(btnEdit);
        styleButton(btnDelete);
        
        //Color diferente para eliminar
        btnDelete.setBackground(new Color(192, 57, 43)); 

        btnAdd.addActionListener(e -> openAddWindow());
        btnEdit.addActionListener(e -> openEditWindow());
        btnDelete.addActionListener(e -> deleteCustomer());

        panel.add(btnDelete);
        panel.add(btnEdit);
        panel.add(btnAdd);

        return panel;
    }

    private void loadAllCustomers() {
        try {
            ArrayList<Customer> customers = controller.getAllCustomers();
            showCustomersInTable(customers);
            searchField.setText("");
        } catch (IOException e) {
            showError("Error al cargar: " + e.getMessage());
        }
    }

    private void showCustomersInTable(ArrayList<Customer> customers) {
        tableModel.setRowCount(0);
        for (Customer c : customers) {
            Object[] row = {
                c.getId(), c.getName(), c.isDisabilityPresented() ? "Sí" : "No",
                c.getEmail(), c.getAddress(), c.getPhoneNumber()
            };
            tableModel.addRow(row);
        }
    }

    private void searchCustomers() {
        String text = searchField.getText().trim();
        if (text.isEmpty()) { loadAllCustomers(); return; }
        try {
            showCustomersInTable(controller.searchCustomers(text));
        } catch (IOException e) {
            showError("Error en búsqueda: " + e.getMessage());
        }
    }

    private void openAddWindow() { openCustomerWindow(null); }

    private void openEditWindow() {
        int row = customerTable.getSelectedRow();
        if (row < 0) { 
            showWarning("Seleccione un cliente"); return; }
        try {
            int id = (int) tableModel.getValueAt(row, 0);
            openCustomerWindow(controller.getCustomerById(id));
        } catch (IOException e) {
            showError("Error al recuperar datos");
        }
    }

    private void deleteCustomer() {
        int row = customerTable.getSelectedRow();
        if (row < 0) { showWarning("Seleccione un cliente"); return; }
        
        int id = (int) tableModel.getValueAt(row, 0);
        String name = (String) tableModel.getValueAt(row, 1);

        int ans = JOptionPane.showConfirmDialog(this, "¿Eliminar a " + name + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (ans == JOptionPane.YES_OPTION) {
            try {
                controller.deleteCustomer(id);
                loadAllCustomers();
                showInfo("Cliente eliminado");
            } catch (IOException | IllegalArgumentException e) {
                showError("Error al eliminar");
            }
        }
    }

private void openCustomerWindow(Customer customer) {
    CustomerWindow window = (customer == null) ? new CustomerWindow() : new CustomerWindow(customer);
    JDesktopPane desktop = getDesktopPane();
    
    if (desktop != null) {
        // 1. Añadir a una capa superior (MODAL_LAYER)
        desktop.add(window, JLayeredPane.MODAL_LAYER);
        
        // 2. Centrar la ventana en el escritorio
        Dimension desktopSize = desktop.getSize();
        Dimension frameSize = window.getSize();
        window.setLocation((desktopSize.width - frameSize.width) / 2, 
                           (desktopSize.height - frameSize.height) / 2);

        window.setVisible(true);

        // 3. Forzar el foco y el frente de forma asíncrona
        SwingUtilities.invokeLater(() -> {
            try {
                window.setSelected(true);
                window.toFront();
                window.requestFocus();
            } catch (java.beans.PropertyVetoException e) {
                e.printStackTrace();
            }
        });

        window.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosed(InternalFrameEvent e) {
                loadAllCustomers();
            }
        });
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
}