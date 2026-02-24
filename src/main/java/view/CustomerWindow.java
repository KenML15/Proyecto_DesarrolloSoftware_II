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
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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

    //Componentes para la interfaz
    public JTextField textFieldNumber, textFieldName, textFieldEmail, textFieldAddress, textFieldPhone;
    public JCheckBox chkDisability;
    private JButton buttonInsert, buttonModify, buttonCancel;

    //Controller
    private CustomerFileController customerController;
    private Customer customerToEdit;
    public static final String FILECUSTOMER = "Customers.txt";

    //Constructor para insertar clientes
    public CustomerWindow() {
        super("INSERTAR CLIENTE");
        initController();
        initUI();
        buttonModify.setVisible(false);
        setNextId();
    }

    //Constructor para modificar los clientes
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
            customerController = new CustomerFileController("Customers.txt");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "ERROR DE ARCHIVO: " + e.getMessage());
        }
    }

    private void initUI() {
        textFieldNumber = new JTextField();
        textFieldName = new JTextField();
        textFieldEmail = new JTextField();
        textFieldAddress = new JTextField();
        textFieldPhone = new JTextField();
        chkDisability = new JCheckBox("Posee carnet de discapacidad");

        buttonInsert = new JButton("GUARDAR");
        buttonModify = new JButton("ACTUALIZAR");
        buttonCancel = new JButton("CANCELAR");

        //Aplicar estilos a los JTextField
        styleTextField(textFieldNumber);
        styleTextField(textFieldName);
        styleTextField(textFieldEmail);
        styleTextField(textFieldAddress);
        styleTextField(textFieldPhone);

        styleButton(buttonInsert);
        styleButton(buttonModify);
        styleButton(buttonCancel);
        buttonCancel.setBackground(new Color(231, 76, 60)); //Este es un color rojo

        //A quí configuramos el layout y paneles
        setSize(550, 650);
        setLayout(new BorderLayout());

        //Este es el panel para el título
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(primaryColor);
        headerPanel.setPreferredSize(new Dimension(550, 60));
        JLabel lblTitle = new JLabel(this.getTitle());
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        headerPanel.add(lblTitle);

        //Este es el panel central
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

        //Componentes del formulario de clientes
        addFormField(formPanel, "ID CLIENTE", textFieldNumber, gbc, 0);
        addFormField(formPanel, "NOMBRE COMPLETO", textFieldName, gbc, 1);
        addFormField(formPanel, "CORREO ELECTRÓNICO", textFieldEmail, gbc, 2);
        addFormField(formPanel, "DIRECCIÓN", textFieldAddress, gbc, 3);
        addFormField(formPanel, "TELÉFONO", textFieldPhone, gbc, 4);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 5, 5);
        chkDisability.setBackground(Color.WHITE); //Para que combine con el fondo
        formPanel.add(chkDisability, gbc);

        containerPanel.add(formPanel, BorderLayout.CENTER);

        //Panel de los botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 20));
        buttonPanel.setBackground(backgroundColor);
        buttonPanel.add(buttonCancel);
        buttonPanel.add(buttonInsert);
        buttonPanel.add(buttonModify);

        //Agregamos todo al frame
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
        } catch (HeadlessException | IOException | IllegalArgumentException e) {
            showError("Error al insertar al cliente" + e.getMessage());
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
        } catch (HeadlessException | IOException | IllegalArgumentException e) {
            showError("Error al modificar el clientes" + e.getMessage());
        }
    }

    private boolean validateFields() {
        if (textFieldName.getText().trim().isEmpty() || textFieldEmail.getText().trim().isEmpty()) {
            showWarning("El nombre y el email son obligatorios");
            return false;
        }
        return true;
    }

    private Customer createCustomerFromFields() {
        Customer customer = new Customer();
        customer.setId(Integer.parseInt(textFieldNumber.getText()));
        customer.setName(textFieldName.getText().trim());
        customer.setEmail(textFieldEmail.getText().trim());
        customer.setAddress(textFieldAddress.getText().trim());
        customer.setPhoneNumber(textFieldPhone.getText().trim());
        customer.setDisabilityPresented(chkDisability.isSelected());
        return customer;
    }

    private void handleCancel() {
        int option = JOptionPane.showConfirmDialog(this, "¿Cerrar ventana?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            this.dispose();
        }
    }

    protected void addHoverEffect(JButton button) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {

                if (button.isEnabled()) {
                    button.setBackground(accentColor);
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {

                if (button.getText().equals("CANCELAR")) {
                    button.setBackground(new Color(192, 57, 43));
                } else {
                    button.setBackground(primaryColor);
                }
            }
        });
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }
}
