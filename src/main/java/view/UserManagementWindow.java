/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.AdministratorController;
import controller.ClerkController;
import java.awt.*;
import java.io.IOException;
import javax.swing.*;
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

        setClosable(true);
        setIconifiable(true);
        setResizable(false);
        SwingUtilities.invokeLater(() -> centerInDesktop());

        setupWindow();
    }

    private void setupWindow() {
        setSize(450, 600);
        setLayout(new BorderLayout());

        //Panel Principal con fondo blanco y margen
        JPanel mainPanel = new JPanel(new GridLayout(9, 2, 10, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        mainPanel.setBackground(Color.WHITE);

        mainPanel.add(createStyledLabel("Rol de Usuario:"));
        comboRole = new JComboBox<>(new String[]{"Empleado", "Administrador"});
        comboRole.setBackground(Color.WHITE);
        mainPanel.add(comboRole);

        mainPanel.add(createStyledLabel("Identificación:"));
        txtId = new JTextField();
        styleTextField(txtId);
        mainPanel.add(txtId);

        mainPanel.add(createStyledLabel("Nombre Completo:"));
        txtName = new JTextField();
        styleTextField(txtName);
        mainPanel.add(txtName);

        mainPanel.add(createStyledLabel("Edad:"));
        spinAge = new JSpinner(new SpinnerNumberModel(18, 18, 99, 1));

        ((JSpinner.DefaultEditor) spinAge.getEditor()).getTextField().setBackground(Color.WHITE);
        mainPanel.add(spinAge);

        mainPanel.add(createStyledLabel("Nombre de Usuario:"));
        txtUser = new JTextField();
        styleTextField(txtUser);
        mainPanel.add(txtUser);

        mainPanel.add(createStyledLabel("Contraseña:"));
        txtPass = new JPasswordField();
        styleTextField(txtPass);
        mainPanel.add(txtPass);

        mainPanel.add(createStyledLabel("Código de Empleado:"));
        txtCode = new JTextField();
        styleTextField(txtCode);
        mainPanel.add(txtCode);

        mainPanel.add(createStyledLabel("Horario Laboral:"));
        txtSchedule = new JTextField();
        styleTextField(txtSchedule);
        mainPanel.add(txtSchedule);

        //botón registrar
        JButton btnSave = new JButton("REGISTRAR");
        styleButton(btnSave);
        btnSave.setBackground(new Color(46, 204, 113)); //Verde
        btnSave.addActionListener(e -> saveUser());

        mainPanel.add(new JLabel(""));
        mainPanel.add(btnSave);

        add(mainPanel, BorderLayout.CENTER);

        setLocation(350, 50);
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(new Color(44, 62, 80)); //Azul oscuro
        return label;
    }

    private void saveUser() {
        try {
            if (txtId.getText().isEmpty() || txtName.getText().isEmpty() || txtUser.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor complete todos los campos", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String id = txtId.getText();
            String name = txtName.getText();
            int age = (int) spinAge.getValue();
            String user = txtUser.getText();
            String pass = new String(txtPass.getPassword());
            int code = Integer.parseInt(txtCode.getText());
            String schedule = txtSchedule.getText();

            model.data.StaffDataFile dataFile = new model.data.StaffDataFile();

            if (comboRole.getSelectedItem().equals("Administrador")) {
                Administrator newAdmin = new Administrator(code, schedule, age, null, id, name, user, pass);
                dataFile.insertStaff(newAdmin);
                adminCtrl.insertAdministrator(newAdmin);
                JOptionPane.showMessageDialog(this, "Administrador registrado exitosamente.");
            } else {
                Clerk newClerk = new Clerk(code, schedule, age, null, id, name, user, pass);
                dataFile.insertStaff(newClerk);
                clerkCtrl.insertClerk(newClerk);
                JOptionPane.showMessageDialog(this, "Empleado registrado exitosamente.");
            }

            this.dispose();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El código de empleado debe ser un número numérico.", "Error de formato", JOptionPane.ERROR_MESSAGE);
        } catch (HeadlessException | IOException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error de Sistema", JOptionPane.ERROR_MESSAGE);
        }
    }
}
