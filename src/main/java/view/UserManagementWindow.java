/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.AdministratorController;
import controller.ClerkController;
import java.awt.*;
import javax.swing.*;
import model.data.StaffDataFile;
import model.entities.Administrator;
import model.entities.Clerk;

public class UserManagementWindow extends BaseInternalFrame {

    private JTextField txtId, txtName, txtUser, txtCode, txtSchedule;
    private JPasswordField txtPass;
    private JSpinner spinAge;
    private JComboBox<String> comboRole;
    private AdministratorController adminCtrl;
    private ClerkController clerkCtrl;

    public UserManagementWindow(AdministratorController adminCtrl, ClerkController clerkCtrl) {
        super("REGISTRO DE NUEVO PERSONAL");
        this.adminCtrl = adminCtrl;
        this.clerkCtrl = clerkCtrl;
        
        //Configuración básica de la ventana interna
        setClosable(true);
        setIconifiable(true);
        setResizable(false);
        
        initComponents();
    }

    private void initComponents() {
        //Definir tamaño fijo para que no nazca en 0x0
        setSize(450, 550);
        
        //Crear el panel principal con un diseño de rejilla (GridLayout)
        //9 filas, 2 columnas, 10px de espacio entre celdas
        JPanel mainPanel = new JPanel(new GridLayout(9, 2, 10, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        mainPanel.setBackground(Color.WHITE);

        //Campos del formulario
        mainPanel.add(new JLabel("Rol de Usuario:"));
        comboRole = new JComboBox<>(new String[]{"Empleado", "Administrador"});
        mainPanel.add(comboRole);

        mainPanel.add(new JLabel("Identificación (Cédula):"));
        txtId = new JTextField();
        mainPanel.add(txtId);

        mainPanel.add(new JLabel("Nombre Completo:"));
        txtName = new JTextField();
        mainPanel.add(txtName);

        mainPanel.add(new JLabel("Edad:"));
        spinAge = new JSpinner(new SpinnerNumberModel(18, 18, 99, 1));
        mainPanel.add(spinAge);

        mainPanel.add(new JLabel("Nombre de Usuario:"));
        txtUser = new JTextField();
        mainPanel.add(txtUser);

        mainPanel.add(new JLabel("Contraseña:"));
        txtPass = new JPasswordField();
        mainPanel.add(txtPass);

        mainPanel.add(new JLabel("Código de Empleado:"));
        txtCode = new JTextField();
        mainPanel.add(txtCode);

        mainPanel.add(new JLabel("Horario Laboral:"));
        txtSchedule = new JTextField();
        mainPanel.add(txtSchedule);

        //Botón de registrar
        JButton btnSave = new JButton("REGISTRAR");
        btnSave.setBackground(new Color(46, 204, 113)); //Verde
        btnSave.setForeground(Color.WHITE);
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.setOpaque(true);
        btnSave.setBorderPainted(false);
        btnSave.setContentAreaFilled(true);
        btnSave.setFocusPainted(false);
        btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnSave.addActionListener(e -> saveUser());
        
        mainPanel.add(new JLabel("")); //Espacio en blanco
        mainPanel.add(btnSave);

        //Establecer el layout de la ventana y añadir el panel
        this.getContentPane().removeAll();
        this.setLayout(new BorderLayout());
        this.add(mainPanel, BorderLayout.CENTER);
        
        //Refrescar la UI
        this.revalidate();
        this.repaint();
    }

private void saveUser() {
        try {
            //Validar campos vacíos
            if(txtId.getText().isEmpty() || txtName.getText().isEmpty() || txtUser.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor complete todos los campos");
                return;
            }

            String id = txtId.getText();
            String name = txtName.getText();
            int age = (int) spinAge.getValue();
            String user = txtUser.getText();
            String pass = new String(txtPass.getPassword());
            int code = Integer.parseInt(txtCode.getText());
            String schedule = txtSchedule.getText();

            StaffDataFile dataFile = new model.data.StaffDataFile();

            if (comboRole.getSelectedItem().equals("Administrador")) {
                Administrator newAdmin = new Administrator(code, schedule, age, null, id, name, user, pass);
                
                dataFile.insertStaff(newAdmin); 
                
                adminCtrl.insertAdministrator(newAdmin);
                
                JOptionPane.showMessageDialog(this, "Administrador '" + name + "' registrado en archivo.");
            } else {
                Clerk newClerk = new Clerk(code, schedule, age, null, id, name, user, pass);
                
                dataFile.insertStaff(newClerk);
                
                clerkCtrl.insertClerk(newClerk);
                
                JOptionPane.showMessageDialog(this, "Empleado '" + name + "' registrado en archivo.");
            }
            
            this.dispose();
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El código de empleado debe ser un número.");
        } catch (java.io.IOException e) {
            JOptionPane.showMessageDialog(this, "Error al escribir en el archivo Staff.txt: " + e.getMessage());
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}