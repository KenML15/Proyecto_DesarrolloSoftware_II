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
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import model.entities.Customer;

/**
 *
 * @author 50687
 */
public class CustomerWindow extends BaseInternalFrame implements ActionListener {

    // Componentes de la interfaz
    public JTextField textFieldNumber, textFieldName, textFieldEmail, textFieldAddress, textFieldPhone;
    public JCheckBox chkDisability;
    private JButton buttonInsert, buttonModify, buttonCancel;

    // Controlador y datos
    private CustomerFileController customerController;
    private Customer customerToEdit;
    public static final String FILECUSTOMER = "Customers.txt";

    // Constructor para insertar
    public CustomerWindow() {
        super("INSERTAR CLIENTE");
        initController();
        initUI();
        buttonModify.setVisible(false);
        setNextId();
    }

    // Constructor para modificar
    public CustomerWindow(Customer customer) {
        super("MODIFICAR CLIENTE");
        this.customerToEdit = customer;
        initController();
        initUI();
        buttonInsert.setVisible(false);
        buttonModify.setVisible(true);
        loadCustomerData();
    }

    private void initController() {
        try {
            // Asegúrate de que la ruta sea igual en Management y Window
            customerController = new CustomerFileController("Customers.txt");
        } catch (IOException e) {
            // Si entra aquí, la ventana no se termina de crear y por eso no abre
            JOptionPane.showMessageDialog(this, "ERROR DE ARCHIVO: " + e.getMessage());
        }
    }

    private void initUI() {
        // 1. PRIMERÍSIMO: Crear las instancias (El "new")
        textFieldNumber = new JTextField();
        textFieldName = new JTextField();
        textFieldEmail = new JTextField();
        textFieldAddress = new JTextField();
        textFieldPhone = new JTextField();
        chkDisability = new JCheckBox("Posee carnet de discapacidad");

        buttonInsert = new JButton("GUARDAR");
        buttonModify = new JButton("ACTUALIZAR");
        buttonCancel = new JButton("CANCELAR");

        // 2. SEGUNDO: Aplicar estilos (Maquillar lo que ya existe)
        styleTextField(textFieldNumber);
        styleTextField(textFieldName);
        styleTextField(textFieldEmail);
        styleTextField(textFieldAddress);
        styleTextField(textFieldPhone);

        styleButton(buttonInsert);
        styleButton(buttonModify);
        styleButton(buttonCancel);
        buttonCancel.setBackground(new Color(231, 76, 60)); // Rojo vibrante

        // 3. TERCERO: Configurar el Layout y Paneles
        setSize(550, 650);
        setLayout(new BorderLayout());

        // --- PANEL DE TÍTULO ---
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(primaryColor);
        headerPanel.setPreferredSize(new Dimension(550, 60));
        JLabel lblTitle = new JLabel(this.getTitle());
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        headerPanel.add(lblTitle);

        // --- PANEL CENTRAL (Blanco y elegante) ---
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setBackground(backgroundColor);
        containerPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);

        // Añadir componentes al panel de formulario
        addFormField(formPanel, "ID CLIENTE", textFieldNumber, gbc, 0);
        addFormField(formPanel, "NOMBRE COMPLETO", textFieldName, gbc, 1);
        addFormField(formPanel, "CORREO ELECTRÓNICO", textFieldEmail, gbc, 2);
        addFormField(formPanel, "DIRECCIÓN FÍSICA", textFieldAddress, gbc, 3);
        addFormField(formPanel, "TELÉFONO", textFieldPhone, gbc, 4);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 5, 5);
        chkDisability.setBackground(Color.WHITE); // Para que combine con el fondo
        formPanel.add(chkDisability, gbc);

        containerPanel.add(formPanel, BorderLayout.CENTER);

        // --- PANEL DE BOTONES ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 20));
        buttonPanel.setBackground(backgroundColor);
        buttonPanel.add(buttonCancel);
        buttonPanel.add(buttonInsert);
        buttonPanel.add(buttonModify);

        // --- AGREGAR TODO AL FRAME ---
        add(headerPanel, BorderLayout.NORTH);
        add(containerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        buttonInsert.addActionListener(this);
        buttonModify.addActionListener(this);
        buttonCancel.addActionListener(this);
    }

    private void addFormField(JPanel panel, String labelText, JTextField textField, GridBagConstraints gbc, int row) {
        JLabel label = new JLabel(labelText);
        label.setFont(labelFont);
        label.setForeground(textColor);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        textField.setPreferredSize(new Dimension(200, 30));
        panel.add(textField, gbc);
    }

    // --- LÓGICA DE NEGOCIO ---
    private void loadCustomerData() {
        if (customerToEdit != null) {
            textFieldNumber.setText(String.valueOf(customerToEdit.getId()));
            textFieldName.setText(customerToEdit.getName());
            chkDisability.setSelected(customerToEdit.isDisabilityPresented());
            textFieldEmail.setText(customerToEdit.getEmail());
            textFieldAddress.setText(customerToEdit.getAddress());
            textFieldPhone.setText(customerToEdit.getPhoneNumber());
        }
    }

    private void setNextId() {
        textFieldNumber.setText(String.valueOf(getNextAvailableId()));
    }

    private int getNextAvailableId() {
        try {
            int maxId = 0;
            for (Customer c : customerController.getAllCustomers()) {
                if (c.getId() > maxId) {
                    maxId = c.getId();
                }
            }
            return maxId + 1;
        } catch (IOException e) {
            return 1;
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == buttonInsert) {
            insertCustomer();
        } else if (event.getSource() == buttonModify) {
            modifyCustomer();
        } else if (event.getSource() == buttonCancel) {
            handleCancel();
        }
    }

    private void insertCustomer() {
        if (!validateFields()) {
            return;
        }
        try {
            customerController.createCustomer(createCustomerFromFields());
            JOptionPane.showMessageDialog(this, "Cliente guardado exitosamente");
            this.dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void modifyCustomer() {
        if (!validateFields()) {
            return;
        }
        try {
            customerController.updateCustomer(createCustomerFromFields());
            JOptionPane.showMessageDialog(this, "Cliente actualizado exitosamente");
            this.dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private boolean validateFields() {
        if (textFieldName.getText().trim().isEmpty() || textFieldEmail.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nombre y Email son obligatorios", "Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private Customer createCustomerFromFields() {
        Customer c = new Customer();
        c.setId(Integer.parseInt(textFieldNumber.getText()));
        c.setName(textFieldName.getText().trim());
        c.setEmail(textFieldEmail.getText().trim());
        c.setAddress(textFieldAddress.getText().trim());
        c.setPhoneNumber(textFieldPhone.getText().trim());
        c.setDisabilityPresented(chkDisability.isSelected());
        return c;
    }

    private void handleCancel() {
        int option = JOptionPane.showConfirmDialog(this, "¿Cerrar ventana?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            this.dispose();
        }
    }

    protected void addHoverEffect(JButton btn) {
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {

                if (btn.isEnabled()) {
                    btn.setBackground(accentColor);
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {

                if (btn.getText().equals("CANCELAR")) {
                    btn.setBackground(new Color(192, 57, 43));
                } else {
                    btn.setBackground(primaryColor);
                }
            }
        });
    }
}
