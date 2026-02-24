/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.AdministratorController;
import controller.ClerkController;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import model.data.StaffDataFile;
import model.entities.Administrator;

/**
 *
 * @author pablo
 */
public class LoginWindow extends JFrame implements ActionListener {

    JTextField txtUsername;
    JPasswordField txtPassword;
    JComboBox<String> comboRol;
    JButton btnSignIn;

    //Controladores específicos
    ClerkController clerkController = new ClerkController();
    AdministratorController adminController = new AdministratorController();

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            UIManager.put("Button.font", new Font("Segoe UI", Font.PLAIN, 13));
            UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 13));
            UIManager.put("Panel.background", new Color(245, 246, 250));
            UIManager.put("OptionPane.background", Color.WHITE);
            UIManager.put("OptionPane.messageForeground", new Color(44, 62, 80));

        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        AdministratorController adminCtrl = new AdministratorController();
        ClerkController clerkCtrl = new ClerkController();
        adminCtrl.loadFromDisk();
        clerkCtrl.loadFromDisk();

        new LoginWindow().setVisible(true);
    }

    public LoginWindow() {
        setTitle("Ingreso");
        setSize(380, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        container.setBackground(new Color(245, 246, 250));

        //Título 
        JLabel lblTitle = new JLabel("Bienvenido al Parking System");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Logo 
        JLabel lblLogo = new JLabel();
        try {
            ImageIcon iconOriginal = new ImageIcon("imagenes/Logo.png");
            Image imgEscalada = iconOriginal.getImage().getScaledInstance(100, 80, Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(imgEscalada));
        } catch (Exception e) {
            lblLogo.setText("[Logo]");
        }
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Casilla de Rol 
        JLabel lblRol = new JLabel("Seleccione su Rol:");
        lblRol.setAlignmentX(Component.CENTER_ALIGNMENT);

        String[] roles = {"Empleado", "Administrador"};
        comboRol = new JComboBox<>(roles);
        styleComboBox(comboRol);

        //Nombre de Usuario
        JLabel lblUser = new JLabel("Nombre de usuario:");
        lblUser.setAlignmentX(Component.CENTER_ALIGNMENT);
        txtUsername = new JTextField();
        styleTextField(txtUsername);

        //Contraseña
        JLabel lblPass = new JLabel("Contraseña:");
        lblPass.setAlignmentX(Component.CENTER_ALIGNMENT);
        txtPassword = new JPasswordField();
        styleTextField(txtPassword);

        //Botón de Ingreso
        btnSignIn = new JButton("INGRESAR");
        styleButton(btnSignIn);
        btnSignIn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSignIn.addActionListener(this);

        container.add(lblTitle);
        container.add(Box.createVerticalStrut(15));
        container.add(lblLogo);
        container.add(Box.createVerticalStrut(20));

        container.add(lblRol);
        container.add(comboRol);
        container.add(Box.createVerticalStrut(15));

        container.add(lblUser);
        container.add(txtUsername);
        container.add(Box.createVerticalStrut(15));

        container.add(lblPass);
        container.add(txtPassword);
        container.add(Box.createVerticalStrut(10));

        container.add(Box.createVerticalGlue());
        container.add(btnSignIn);
        container.add(Box.createVerticalStrut(10));

        add(container);
    }

    private void styleComboBox(JComboBox<String> combo) {
        combo.setMaximumSize(new Dimension(180, 35));
        combo.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Estilo del texto
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setForeground(new Color(44, 62, 80));

        //Ayuda a que el botón de la flecha no rompa la estética
        combo.putClientProperty("JComponent.roundRect", true);
    }

    private void styleTextField(JTextField field) {
        field.setMaximumSize(new Dimension(300, 35));
        field.setPreferredSize(new Dimension(300, 35));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Transparencia total
        field.setOpaque(false);
        field.setBackground(new Color(0, 0, 0, 0));

        //Solo línea inferior azul
        field.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(52, 152, 219)));

        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(new Color(44, 62, 80));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSignIn) {
            String user = txtUsername.getText();
            String pass = new String(txtPassword.getPassword());
            String role = comboRol.getSelectedItem().toString();

            StaffDataFile staffFile = new StaffDataFile();
            ArrayList<model.entities.Clerk> allStaff = staffFile.getAllStaff();
            model.entities.User userAuth = null;
            //Buscamos en el archivo Staff.txt
            for (model.entities.Clerk emp : allStaff) {
                if (emp.getUsername().equals(user) && emp.getPassword().equals(pass)) {
                    userAuth = emp;
                    break;
                }
            }
            if (userAuth != null) {
                boolean isAdmin = (userAuth instanceof model.entities.Administrator);
                if (role.equals("Administrador") && isAdmin) {
                    this.dispose();
                    new Menu_Admin(userAuth).setVisible(true);
                } else if (role.equals("Empleado") && !isAdmin) {
                    this.dispose();
                    new Menu_Clerk(userAuth).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "El rol no coincide.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Credenciales incorrectas.");
            }
        }
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(52, 152, 219));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
    }
}
