/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.CustomerFileController;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
public class CustomerWindow extends JInternalFrame implements ActionListener{
    
    //Componentes para la interfaz
    JPanel panelOfInsertCustomerWindow;
    JLabel labelNumber, labelName, labelDisability, labelEmail, labelAddress, labelPhone;
    public JTextField textFieldNumber, textFieldName, textFieldEmail, textFieldAddress, textFieldPhone;
    public JCheckBox chkDisability;
    JButton buttonInsert, buttonModify, buttonCancel;
    
    //Controlador y datos para customer
    CustomerFileController customerController;
    Customer customerToEdit;
    public static final String FILECUSTOMER = "Customers.txt";

    //Constructor para insertar al cliente
    public CustomerWindow() {
        super("Insertar Cliente", false, true, false, true);
        initController();
        initUI();
        setupWindow();
        
        this.setVisible(true);     
        buttonModify.setVisible(false);
        setNextId();
    }

    //Constructor para modificar al cliente
    public CustomerWindow(Customer customer){
        super("Modificar Cliente", false, true, false, true);
        this.customerToEdit = customer;
        initController();
        initUI();
        setupWindow();
         
        //En modo modificar, ocultamos insertar y mostramos modificar
        buttonInsert.setVisible(false);
        buttonModify.setVisible(true);
        loadCustomerData();
    }
    
    //Establece el tamaño de la ventana
    private void setupWindow(){
        this.setSize(450, 500);
        this.setLocation(215, 50);
        this.setResizable(false);
    }
    
    //Carga el controlador que gestiona el archivo de texto
    private void initController() {
        try {
            customerController = new CustomerFileController(FILECUSTOMER);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al inicializar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Crea el panel y coloca todos los elementos usuando coordenadas fijas
    //Modify e Insert tienen las mismas coordenadas para que el constructor
    //decidacuál mostrar
    private void initUI() {
        panelOfInsertCustomerWindow = new JPanel();
        panelOfInsertCustomerWindow.setLayout(null);
        panelOfInsertCustomerWindow.setBackground(Color.WHITE);
        this.add(panelOfInsertCustomerWindow);

        //ID
        createID();

        //Nombre
        createName();
        
        //Discapacidad
        createDisability();     
         
        //Email
        createEmail();
        
        //Dirección
        createAddress();
        
        //Teléfono
        createPhone();
        
        //Botones
        createButtons();
        
        this.setVisible(true);
    }
    
    private void createID() {
        labelNumber = new JLabel("Número: ");
        labelNumber.setBounds(50, 10, 100, 50);
        labelNumber.setForeground(Color.BLUE);
        panelOfInsertCustomerWindow.add(labelNumber);

        textFieldNumber = new JTextField(75);
        textFieldNumber.setBounds(150, 25, 200, 25);
        textFieldNumber.setEditable(false);
        panelOfInsertCustomerWindow.add(textFieldNumber);
    }
    
    private void createName() {
        labelName = new JLabel("Nombre: ");
        labelName.setBounds(50, 70, 100, 50);
        labelName.setForeground(Color.BLUE);
        panelOfInsertCustomerWindow.add(labelName);

        textFieldName = new JTextField(75);
        textFieldName.setBounds(150, 85, 200, 25);
        panelOfInsertCustomerWindow.add(textFieldName);
    }
    
    private void createDisability() {
        labelDisability = new JLabel("Discapacidad: ");
        labelDisability.setBounds(50, 130, 100, 50);
        labelDisability.setForeground(Color.BLUE);
        panelOfInsertCustomerWindow.add(labelDisability);

        chkDisability = new JCheckBox();
        chkDisability.setBounds(150, 145, 250, 25);
        chkDisability.setBackground(Color.WHITE);
        panelOfInsertCustomerWindow.add(chkDisability);
    }
    
    private void createEmail() {
        labelEmail = new JLabel("Email: ");
        labelEmail.setBounds(50, 190, 100, 50);
        labelEmail.setForeground(Color.BLUE);
        panelOfInsertCustomerWindow.add(labelEmail);

        textFieldEmail = new JTextField(75);
        textFieldEmail.setBounds(150, 205, 200, 25);
        panelOfInsertCustomerWindow.add(textFieldEmail);
    }
    
    private void createAddress() {
        labelAddress = new JLabel("Dirección: ");
        labelAddress.setBounds(50, 250, 100, 50);
        labelAddress.setForeground(Color.BLUE);
        panelOfInsertCustomerWindow.add(labelAddress);

        textFieldAddress = new JTextField(75);
        textFieldAddress.setBounds(150, 265, 200, 25);
        panelOfInsertCustomerWindow.add(textFieldAddress);
    }
    
    private void createPhone() {
        labelPhone = new JLabel("Teléfono: ");
        labelPhone.setBounds(50, 310, 100, 50);
        labelPhone.setForeground(Color.BLUE);
        panelOfInsertCustomerWindow.add(labelPhone);

        textFieldPhone = new JTextField(75);
        textFieldPhone.setBounds(150, 325, 200, 25);
        panelOfInsertCustomerWindow.add(textFieldPhone);
    }
    
    private void createButtons(){
        buttonInsert = new JButton("Insertar");
        buttonInsert.setBounds(110, 390, 100, 30);
        buttonInsert.addActionListener(this);
        buttonInsert.setToolTipText("Presione para insertar el cliente");
        panelOfInsertCustomerWindow.add(buttonInsert);
        
        buttonModify = new JButton("Modificar");
        buttonModify.setBounds(110, 390, 100, 30); //Misma posición que insertar
        buttonModify.addActionListener(this);
        buttonModify.setToolTipText("Presione para modificar la información del cliente");
        panelOfInsertCustomerWindow.add(buttonModify);
        
        buttonCancel = new JButton("Cancelar");
        buttonCancel.setBounds(220, 390, 100, 30);
        buttonCancel.addActionListener(this);
        buttonCancel.setToolTipText("Presione para cancelar");
        panelOfInsertCustomerWindow.add(buttonCancel);
    }
        
    private void loadCustomerData(){
        textFieldNumber.setText(String.valueOf(customerToEdit.getId()));
        textFieldName.setText(customerToEdit.getName());
        chkDisability.setSelected(customerToEdit.isDisabilityPresented());
        textFieldEmail.setText(customerToEdit.getEmail());
        textFieldAddress.setText(customerToEdit.getAddress());
        textFieldPhone.setText(customerToEdit.getPhoneNumber());
    }
    
//    Este método establece el ID en el campo
    private void setNextId(){
        try{
            int nextId = getNextAvailableId();
            textFieldNumber.setText(String.valueOf(nextId));
        }catch (Exception e){
            textFieldNumber.setText("1"); //Este es un valor por defecto
        }
    }
    
//    //Este método obtiene el ID
    private int getNextAvailableId(){
        try {
            int maxId = 0;
            for(Customer customer : customerController.getAllCustomers()){
                if (customer.getId() > maxId) {
                    maxId = customer.getId();
                }
            }
            return maxId + 1;
        }catch (Exception e){
            return 1;
        }
    }
    
    //Al hacer clic en un botón, este método identifica cuál fué y realiza
    //la acción correspondiente
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

    //Acción de insertar customer
    private void insertCustomer() {
        if (!validateFields()) return;
        
        try {
            Customer customer = createCustomerFromFields();
            customerController.createCustomer(customer);
            
            JOptionPane.showMessageDialog(this, "Cliente insertado exitosamente");
            clearFields();
//            setNextId();
            
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Error de validación: " + e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,"Error al insertar cliente: " + e.getMessage());
        }
    }

    //Acción de modificar customer
    private void modifyCustomer() {
        if (!validateFields()) return;
        
        try {
            Customer customer = createCustomerFromFields();
            customerController.updateCustomer(customer);
            
            JOptionPane.showMessageDialog(this, "Cliente modificado con éxito");
            this.dispose();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Error de validación: " + e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al modificar cliente: " + e.getMessage());
        }
    }

    //Impide que se guarden clientes con datos vacíos
    private boolean validateFields() {
        if (textFieldName.getText().trim().isEmpty()) {
            showValidationError("El nombre es requerido");
            return false;
        }
        if (textFieldEmail.getText().trim().isEmpty()) {
            showValidationError("El email es requerido");
            return false;
        }
        if (textFieldPhone.getText().trim().isEmpty()) {
            showValidationError("El teléfono es requerido");
            return false;
        }
        return true;
    }

    private void showValidationError(String message) {
        JOptionPane.showMessageDialog(this, message, "Validación", JOptionPane.WARNING_MESSAGE);
    }

    //Convierte los datos ingresados en las cajitas de texto en un objeto
    //Customer.
    private Customer createCustomerFromFields() throws IllegalArgumentException {
        Customer customer = new Customer();

        String idText = textFieldNumber.getText().trim();
        if (!idText.isEmpty()) {
            try {

                customer.setId(Integer.parseInt(idText));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Número de cliente inválido");
            }
        } else {
            throw new IllegalArgumentException("Número de cliente no generado");
        }

        customer.setName(textFieldName.getText().trim());
        customer.setEmail(textFieldEmail.getText().trim());
        customer.setDisabilityPresented(chkDisability.isSelected());
        customer.setAddress(textFieldAddress.getText().trim());
        customer.setPhoneNumber(textFieldPhone.getText().trim());

        return customer;
    }

    //Limpia el formulario
    private void clearFields() {
        textFieldName.setText("");
        chkDisability.setSelected(false);
        textFieldEmail.setText("");      
        textFieldPhone.setText("");
        textFieldAddress.setText("");
    }

    //Gestiona el cierre de la ventana
    private void handleCancel() {
        int option = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea cerrar la ventana?","Confirmar", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            setVisible(false);
        }
    }
}