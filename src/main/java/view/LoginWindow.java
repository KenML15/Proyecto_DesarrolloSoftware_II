/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.ClerkController;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import model.entities.Administrator;
import model.entities.Clerk;
import model.entities.User;

/**
 *
 * @author pablo
 */

public class LoginWindow extends JFrame implements ActionListener {

    JTextField txtUsername;
    JPasswordField txtPassword;
    JButton btnSignIn;

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
        setTitle("Ingreso");
        setSize(380, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        insertUsersTest();

        initComponents();
    }

    private void initComponents() {
        // 1. Panel
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        container.setBackground(Color.white);

        // 2. Title (Usa HTML para las dos líneas)
        JLabel lblTitle = new JLabel("Bienvenido al Parking System");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 3. Username (SIN el "JTextField" al inicio para usar la global)
        JLabel lblUser = new JLabel("Nombre de usuario: ");
        lblUser.setAlignmentX(Component.CENTER_ALIGNMENT);
        txtUsername = new JTextField(); 
        styleTextField(txtUsername);

        // 4. Password (SIN el "JPasswordField" al inicio)
        JLabel lblPass = new JLabel("Contraseña: ");
        lblPass.setAlignmentX(Component.CENTER_ALIGNMENT);
        txtPassword = new JPasswordField();
        styleTextField(txtPassword);

        // 5. Olvidé contraseña
        JLabel lblForgot = new JLabel("olvide mi contraseña");
        lblForgot.setForeground(Color.BLUE);
        lblForgot.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblForgot.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 6. Button (SIN el "JButton" al inicio)
        btnSignIn = new JButton("Ingresar");
        styleButton(btnSignIn);
        btnSignIn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSignIn.addActionListener(this); // IMPORTANTE: Para que el botón funcione

// 7. Carga del Logo adaptada al ejemplo del profesor
    JLabel lblLogo = new JLabel(); 
    try {
        // Usamos la ruta relativa a la carpeta raíz del proyecto
        ImageIcon iconOriginal = new ImageIcon("imagenes/Logo.png");
        
        // Verificamos si la imagen cargó (si no tiene dimensiones, es que no la encontró)
        if (iconOriginal.getImageLoadStatus() == java.awt.MediaTracker.COMPLETE || iconOriginal.getIconWidth() > 0) {
            Image imgEscalada = iconOriginal.getImage().getScaledInstance(120, 100, Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(imgEscalada));
        } else {
            lblLogo.setText("<html><font color='red'>No se encontró: imagenes/Logo.png</font></html>");
        }
    } catch (Exception e) {
        lblLogo.setText("Error al cargar imagen");
    }
    lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 8. AGREGAR TODO AL CONTENEDOR (El orden importa)
        container.add(lblTitle);
        container.add(Box.createVerticalStrut(30));
        container.add(lblUser);
        container.add(txtUsername);
        container.add(Box.createVerticalStrut(15));
        container.add(lblPass);
        container.add(txtPassword);
        container.add(lblForgot);
        container.add(Box.createVerticalStrut(25));
        container.add(btnSignIn);
        
        // Esto empuja el logo hacia abajo
        container.add(Box.createVerticalGlue()); 
        container.add(lblLogo); // ¡ESTA ES LA LÍNEA QUE TE FALTABA!

        add(container);
    }

    // antigua ventana de login;
//    private void initComponents() {
//        // 1. Inicializar componentes UNA SOLA VEZ
//        txtUsername = new JTextField(15);
//        styleTextField(txtUsername);
//
//        txtPassword = new JPasswordField(15);
//        styleTextField(txtPassword);
//
//        btnSignIn = new JButton("Sign In");
//        styleButton(btnSignIn);
//        btnSignIn.addActionListener(this);
//
//        // 2. Armar los paneles
//        JPanel mainPanel = new JPanel(new BorderLayout());
//        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
//
//        JLabel lblTitle = new JLabel("Bienvenido al sistema de ingreso.", SwingConstants.CENTER);
//        lblTitle.setAlignmentX(CENTER_ALIGNMENT);
//        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 17));
//        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
//
//        JPanel formPanel = new JPanel();
//        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
//
//        // Username row
//        JPanel usernamePanel = new JPanel(new BorderLayout(10, 0));
//        usernamePanel.add(new JLabel("Nombre de usuario :"), BorderLayout.WEST);
//        usernamePanel.add(txtUsername, BorderLayout.CENTER);
//
//        // Password row
//        JPanel passwordPanel = new JPanel(new BorderLayout(10, 0));
//        passwordPanel.add(new JLabel("Contraseña :"), BorderLayout.WEST);
//        passwordPanel.add(txtPassword, BorderLayout.CENTER);
//
//        formPanel.add(usernamePanel);
//        formPanel.add(Box.createVerticalStrut(15));
//        formPanel.add(passwordPanel);
//
//        JPanel buttonPanel = new JPanel();
//        buttonPanel.add(btnSignIn);
//
//        mainPanel.add(lblTitle, BorderLayout.NORTH);
//        mainPanel.add(formPanel, BorderLayout.CENTER);
//        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
//
//        add(mainPanel);
//    }
    
    private void styleTextField(JTextField field) {

        field.setMaximumSize(new Dimension(300, 35));
        field.setPreferredSize(new Dimension(300, 35));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);

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

            // Intentar autenticar al usuario
            User userAuthenticated = clerkController.searchUser(username, password);

            if (userAuthenticated != null) {
                JOptionPane.showMessageDialog(this, "Bienvenido " + userAuthenticated.getName());

                // 1. Cerramos la ventana de Login
                this.dispose();

                // 2. Abrimos la clase Menu (El sistema principal con JMenuBar)
                // Nota: Si tu clase Menu requiere el objeto usuario, deberías pasárselo por constructor
                new Menu(userAuthenticated).setVisible(true);

            } else {
                // Lógica de fallo de autenticación
                JOptionPane.showMessageDialog(this, "Invalid user", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

private void styleButton(JButton button) {
    button.setBackground(new Color(52, 152, 219)); // El azul que querías
    button.setForeground(Color.WHITE);             // Texto blanco
    button.setFocusPainted(false);
    button.setBorderPainted(false);                // Importante para que luzca el color plano
    button.setFont(new Font("Segoe UI", Font.BOLD, 14));
    button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    
    // Esto le da un margen interno para que no se vea apretado
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
}
