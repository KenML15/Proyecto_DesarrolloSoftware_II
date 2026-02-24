/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.AdministratorController;
import controller.ClerkController;
import controller.ParkingLotFileController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import model.entities.Administrator;
import model.entities.ParkingLot;
import model.entities.User;
import org.jdom2.JDOMException;

/**
 *
 * @author Pablo
 */
public class Menu_Admin extends JFrame {

    private User currentUser;
    private JPanel sidePanel;
    private HomeDesktop desktop;

    public Menu_Admin(User user) {
        super("SISTEMA DE GESTIÓN DE PARQUEOS");
        this.currentUser = user;
        initComponents();
        applyPermissions();
    }

    private void initComponents() {
        setSize(1200, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        //Sidebar
        sidePanel = new JPanel();
        sidePanel.setBackground(new Color(44, 62, 80)); // Azul oscuro
        sidePanel.setPreferredSize(new Dimension(250, 800));
        sidePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

        //Encabezado del SideBar
        JLabel lblBrand = new JLabel("PARK-SYSTEM", SwingConstants.CENTER);
        lblBrand.setForeground(Color.WHITE);
        lblBrand.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblBrand.setPreferredSize(new Dimension(250, 80));
        lblBrand.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(52, 73, 94)));
        sidePanel.add(lblBrand);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Esto maximiza la ventana al abrirse
        setSize(1200, 800); // Tamaño mínimo si el usuario decide restaurarla
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        desktop = new HomeDesktop();
        desktop.setBackground(new Color(236, 240, 241));

        //botones de navegación
        addSidebarButton("CLIENTES", e -> openCustomerManagement(desktop));
        addSidebarButton("VEHÍCULOS", e -> openVehicleManagement(desktop));
        addSidebarButton("PARQUEOS", e -> openParkingLotManagement(desktop));
        addSidebarButton("TARIFAS", e -> openFeeManagement(desktop));
        addSidebarButton("NUEVO PARQUEO", e -> openParkingLotWindow(desktop));
        addSidebarButton("VER PARQUEO", e -> openParkingVisualView(desktop));
        addSidebarButton("GESTIÓN DE PERSONAL", e -> openUserManagement());
        addSidebarButton("REPORTES", e -> openReportsWindow(desktop));

        JButton btnExit = addSidebarButton("CERRAR SESIÓN", e -> {
            this.dispose();
            new LoginWindow().setVisible(true);
        });
        btnExit.setBackground(new Color(192, 57, 43));

        add(sidePanel, BorderLayout.WEST);
        add(desktop, BorderLayout.CENTER);

        this.setVisible(true);
    }

    private void openUserManagement() {
        UserManagement window = new UserManagement();
        addWindowToDesktop(desktop, window);
    }

    private JButton addSidebarButton(String text, ActionListener action) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(250, 55));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(new Color(236, 240, 241));
        btn.setBackground(new Color(44, 62, 80));

        btn.setContentAreaFilled(false); //Quita el fondo gris por defecto
        btn.setOpaque(true);             //Permite que se vea nuestro color
        btn.setFocusPainted(false);      //Quita el borde de enfoque
        btn.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 0));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addActionListener(action);

        //Efecto Hover
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (btn.getBackground().equals(new Color(44, 62, 80))) {
                    btn.setBackground(new Color(52, 73, 94));
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (btn.getBackground().equals(new Color(52, 73, 94))) {
                    btn.setBackground(new Color(44, 62, 80));
                }
            }
        });

        sidePanel.add(btn);
        return btn;
    }

    private void applyPermissions() {
        if (!(currentUser instanceof Administrator)) {
            for (Component c : sidePanel.getComponents()) {
                if (c instanceof JButton) {
                    JButton btn = (JButton) c;
                    String txt = btn.getText().toUpperCase();
                    //Ocultamos las opciones sensibles para usuarios normales
                    if (txt.equals("PARQUEOS") || txt.equals("TARIFAS") || txt.equals("CONFIGURAR ESPACIOS")) {
                        btn.setVisible(false);
                    }
                }
            }
        }
    }

    private void addWindowToDesktop(HomeDesktop desktop, JInternalFrame window) {
        try {
            // 1. Evitar duplicados (Tu lógica actual está perfecta)
            for (Component c : desktop.getComponents()) {
                if (c.getClass() == window.getClass()) {
                    JInternalFrame win = (JInternalFrame) c;
                    win.setSelected(true);
                    win.toFront();
                    return;
                }
            }

            // 2. Añadir en una capa superior (MODAL o POPUP)
            desktop.add(window, JLayeredPane.MODAL_LAYER);

            window.setVisible(true);

            // 3. El truco del invokeLater: asegura que el foco ocurra DESPUÉS de mostrarse
            SwingUtilities.invokeLater(() -> {
                try {
                    window.setSelected(true);
                    window.toFront();
                    window.requestFocus();
                } catch (PropertyVetoException e) {
                    e.printStackTrace();
                }
            });

            desktop.revalidate();
            desktop.repaint();

        } catch (PropertyVetoException e) {
            showError("Error: " + e.getMessage());
        }
    }

    private void openCustomerManagement(HomeDesktop desktop) {
        CustomerManagement window = new CustomerManagement();
        addWindowToDesktop(desktop, window);
    }

    private void openParkingVisualView(HomeDesktop desktop) {
        try {
            ParkingLotFileController controller = new ParkingLotFileController();
            ParkingVisualWindow window = new ParkingVisualWindow(controller);
            addWindowToDesktop(desktop, window);
        } catch (IOException | JDOMException e) {
            showError("Error al abrir vista visual: " + e.getMessage());
        }
    }

    private void openVehicleManagement(HomeDesktop desktop) {
        addWindowToDesktop(desktop, new VehicleManagement());
    }

    private void openParkingLotManagement(HomeDesktop desktop) {
        addWindowToDesktop(desktop, new ParkingLotManagement());
    }

    private void openFeeManagement(HomeDesktop desktop) {
        addWindowToDesktop(desktop, new FeeManagement());
    }

    private void openSpaceConfiguration(HomeDesktop desktop) {
        try {
            ParkingLotFileController controller = new ParkingLotFileController();
            ArrayList<ParkingLot> parkingLots = controller.getAllParkingLots();
            if (parkingLots.isEmpty()) {
                showMessage("Cree un parqueo primero", "Información");
                return;
            }
        } catch (IOException | JDOMException e) {
            showError("Error: " + e.getMessage());
        }
    }

    private void openParkingLotWindow(HomeDesktop desktop) {
        try {
            ParkingLotFileController controller = new ParkingLotFileController();
            ParkingLotWindow window = new ParkingLotWindow(controller);

            desktop.add(window, JDesktopPane.PALETTE_LAYER);

            desktop.setComponentZOrder(window, 0);

            window.setVisible(true);
            window.toFront();
            window.setSelected(true);
        } catch (PropertyVetoException | IOException | JDOMException e) {
            showError("Error: " + e.getMessage());
        }
    }

    private void openReportsWindow(HomeDesktop desktop) {
        try {
            //Obtener el nombre del usuario actual para mostrarlo en los reportes
            String userName = currentUser.getName();

            //Crear y mostrar la ventana de reportes
            ReportsWindow reportsWindow = new ReportsWindow(userName);
            addWindowToDesktop(desktop, reportsWindow);

        } catch (Exception e) {
            showError("Error al abrir ventana de reportes: " + e.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showMessage(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
}
