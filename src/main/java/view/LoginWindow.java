/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.ClerkController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import model.entities.Administrator;
import model.entities.Clerk;
import model.entities.User;

/**
 *
 * @author 50687
 */
public class LoginWindow extends JFrame implements ActionListener {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnSignIn;

    ClerkController clerkController = new ClerkController();

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            // Personalización técnica de componentes
            UIManager.put("Button.font", new Font("Segoe UI", Font.PLAIN, 13));
            UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 13));
            UIManager.put("Panel.background", new Color(245, 246, 250));
            UIManager.put("OptionPane.background", Color.WHITE);
            UIManager.put("OptionPane.messageForeground", new Color(44, 62, 80));

        } catch (Exception e) {
            e.printStackTrace();
        }
        new LoginWindow().setVisible(true);
    }

    public LoginWindow() {
        setTitle("Login");
        setSize(380, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        insertUsersTest();

        initComponents();
    }

    private void initComponents() {
        // 1. Inicializar componentes UNA SOLA VEZ
        txtUsername = new JTextField(15);
        styleTextField(txtUsername);

        txtPassword = new JPasswordField(15);
        styleTextField(txtPassword);

        btnSignIn = new JButton("Sign In");
        styleButton(btnSignIn);
        btnSignIn.addActionListener(this);

        // 2. Armar los paneles
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel lblTitle = new JLabel("Welcome", SwingConstants.LEFT);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        // Username row
        JPanel usernamePanel = new JPanel(new BorderLayout(10, 0));
        usernamePanel.add(new JLabel("Username:"), BorderLayout.WEST);
        usernamePanel.add(txtUsername, BorderLayout.CENTER);

        // Password row
        JPanel passwordPanel = new JPanel(new BorderLayout(10, 0));
        passwordPanel.add(new JLabel("Password:"), BorderLayout.WEST);
        passwordPanel.add(txtPassword, BorderLayout.CENTER);

        formPanel.add(usernamePanel);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(passwordPanel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnSignIn);

        mainPanel.add(lblTitle, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

private void styleTextField(JTextField field) {
    field.setPreferredSize(new Dimension(200, 35));
    // Línea azul inferior estilo moderno
    field.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(52, 152, 219)));
    field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    field.setBackground(Color.WHITE);
}

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSignIn) {
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());

            User userAuthenticated = clerkController.searchUser(username, password);

            if (userAuthenticated != null) {
                JOptionPane.showMessageDialog(this, "Welcome " + userAuthenticated.getName());
                this.dispose();

                new RegistrationWindow(userAuthenticated).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid user", "Error", JOptionPane.ERROR_MESSAGE);

                insertClerk();
                userAuthenticated = clerkController.searchUser(new Clerk(2, null, 19, null, "123", "Kenneth Miranda", username, password)); //Como clerk es un user, está bien que se 
                if (userAuthenticated == null) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Username does not exist",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Welcome " + username, "Login successful", JOptionPane.INFORMATION_MESSAGE);

                    // Abrir la ventana de registro y cerrar el login
                   //new RegistrationWindow().setVisible(true);
                    this.dispose();
                }
            }
        }
    }

    private void styleButton(JButton button) {
    button.setFocusPainted(false);
    button.setBackground(new Color(52, 152, 219));
    button.setForeground(Color.WHITE);
    button.setFont(new Font("Segoe UI", Font.BOLD, 14));
    button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
}

    public void insertUsersTest() {

        clerkController.insertClerk(new Clerk(1, "7am-3pm", 25, null, "1-111", "Pablo Operador", "pablo", "123"));

        clerkController.insertClerk(new Administrator(
                99,
                "Full Time",
                30,
                null,
                "0-000",
                "Admin Supreme",
                "admin", // username (String)
                "admin123" // password (String)
        ));
    }

    void insertClerk() {
        Clerk clerkPrueba = new Clerk(1, null, 18, null, "123", "Pablo Solano", "Pablo", "Pablo123");
        Clerk clerkPrueba2 = new Clerk(2, null, 19, null, "123", "Kenneth Miranda", "Kenneth", "Kenneth123");
        Clerk clerkPrueba3 = new Clerk(3, null, 19, null, "123", "Eilyn Rivera", "Eilyn", "Eilyn123");

        clerkController.insertClerk(clerkPrueba);
        clerkController.insertClerk(clerkPrueba2);
        clerkController.insertClerk(clerkPrueba3);
    }

   
}
