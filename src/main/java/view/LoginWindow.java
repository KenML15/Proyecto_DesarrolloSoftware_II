/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.ClerkController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
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
        new LoginWindow().setVisible(true);
    }

    public LoginWindow() {

        setTitle("Login"); //Settea el título al frame
        setSize(380, 250); //Settea el tamaño
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Si cierra el frame se cierra todo el programa
        setLocationRelativeTo(null); //Hace que el frame se posicione en un lugar relativo

        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        //  Título
        JLabel lblTitle = new JLabel("Welcome", SwingConstants.LEFT);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        //  Panel de formulario
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        // Username row
        JPanel usernamePanel = new JPanel(new BorderLayout(10, 0));
        JLabel lblUsername = new JLabel("Username:");
        txtUsername = new JTextField(15);
        usernamePanel.add(lblUsername, BorderLayout.WEST);
        usernamePanel.add(txtUsername, BorderLayout.CENTER);

        // Password row
        JPanel passwordPanel = new JPanel(new BorderLayout(10, 0));
        JLabel lblPassword = new JLabel("Password:");
        txtPassword = new JPasswordField(15);
        passwordPanel.add(lblPassword, BorderLayout.WEST);
        passwordPanel.add(txtPassword, BorderLayout.CENTER);

        formPanel.add(usernamePanel);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(passwordPanel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));

        btnSignIn = new JButton("Sign In");
        btnSignIn.addActionListener(this);
        buttonPanel.add(btnSignIn);

        mainPanel.add(lblTitle, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);//Esta clase es un frame, por eso puede acceder directamente a todos los elementos

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSignIn) { //Es para hacer accionar el boton

            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());//El getPassword devuelve un arreglo de char

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Username and password are required",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            } else {

                insertClerk();
                User userAuthenticated = clerkController.searchUser(new Clerk(123, "Random", 43, null, "123456", "Fulano", username, password)); //Como clerk es un user, está bien que se 
                if (userAuthenticated == null) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Username does not exist",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Welcome " + username, "Login successful", JOptionPane.INFORMATION_MESSAGE);

                    // Abrir la ventana de registro y cerrar el login
                   
                    this.dispose();
                }
            }
        }
    }


    private void styleButton(JButton button) {
        button.setFocusPainted(false);
        button.setBackground(new Color(70, 130, 180)); // Azul acero
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180)),
                BorderFactory.createEmptyBorder(10, 25, 10, 25)
        ));
    }

    void insertClerk() {
        Clerk clerkPrueba = new Clerk(123, "Random", 43, null, "123456", "Fulano", "Fuli", "abc123");
        Clerk clerkPrueba2 = new Clerk(123, "Nose", 32, null, "jsjsjs", "Filomeno", "menoFuli", "filo12");

        clerkController.insertClerk(clerkPrueba);
        clerkController.insertClerk(clerkPrueba2);
    }
}
