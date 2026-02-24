/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.AdministratorController;
import controller.ClerkController;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableModel;
import model.data.StaffDataFile;
import model.entities.Administrator;
import model.entities.Clerk;

public class UserManagement extends BaseInternalFrame {

    private AdministratorController adminCtrl;
    private ClerkController clerkCtrl;
    private JTable userTable;
    private DefaultTableModel tableModel;
    private final String[] COLUMNS = {"Cédula", "Nombre", "Usuario", "Rol", "Código", "Horario"};

    public UserManagement() {
        super("GESTIÓN DE PERSONAL");
        this.adminCtrl = new AdministratorController();
        this.clerkCtrl = new ClerkController();

        initComponents();
        loadAllUsers();
        SwingUtilities.invokeLater(() -> centerInDesktop());
    }

    private void initComponents() {
        setSize(900, 550);
        setLocation(100, 50);
        setLayout(new BorderLayout(0, 0));

        JPanel centerPanel = new JPanel(new BorderLayout(15, 15));
        centerPanel.setBackground(backgroundColor);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        //Configuración de la Tabla
        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        userTable = new JTable(tableModel);
        styleTable(userTable); //Método heredado para diseño profesional

        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(fieldBorderColor));

        centerPanel.add(new JLabel("Listado de Administradores y Empleados registrados"), BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(backgroundColor);

        createButtons(buttonPanel);

        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void createButtons(JPanel panel) {
        JButton btnRefresh = createStyledButton("REFRESCAR", primaryColor, panel);
        JButton btnAdd = createStyledButton("NUEVO USUARIO", new Color(39, 174, 96), panel); //Verde
        JButton btnDelete = createStyledButton("ELIMINAR", new Color(192, 57, 43), panel); //Rojo

        btnRefresh.addActionListener(e -> loadAllUsers());
        btnAdd.addActionListener(e -> openRegistrationWindow());
        btnDelete.addActionListener(e -> deleteSelectedUser());
    }

    private JButton createStyledButton(String text, Color bg, JPanel panel) {
        JButton btn = new JButton(text);
        styleButton(btn);
        btn.setBackground(bg);
        panel.add(btn);
        return btn;
    }

    private void loadAllUsers() {
        tableModel.setRowCount(0);
        try {
            StaffDataFile dataFile = new StaffDataFile();
            ArrayList<Clerk> staff = dataFile.getAllStaff();

            for (Clerk e : staff) {
                String role = (e instanceof Administrator) ? "Administrador" : "Dependiente";
                tableModel.addRow(new Object[]{
                    e.getIdentification(), e.getName(), e.getUsername(),
                    role, e.getEmployeeCode(), e.getSchedule()
                });
            }
        } catch (IOException ex) {
            showError("Error al cargar la tabla de usuarios: " + ex.getMessage());
        }
    }

    private void deleteSelectedUser() {
        int row = userTable.getSelectedRow();
        if (row == -1) {
            showWarning("Seleccione un usuario de la tabla para eliminar.");
            return;
        }

        String id = (String) tableModel.getValueAt(row, 0);
        String name = (String) tableModel.getValueAt(row, 1);
        String rol = (String) tableModel.getValueAt(row, 3);

        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea eliminar a: " + name + " (" + rol + ")?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (rol.equals("Administrador")) {
                adminCtrl.deleteAdministrator(id);
            } else {
                clerkCtrl.deleteClerk(id);
            }

            loadAllUsers();
            showSuccess("Usuario eliminado correctamente.");
        }
    }

    private void openRegistrationWindow() {
        UserManagementWindow win = new UserManagementWindow(adminCtrl, clerkCtrl);
        JDesktopPane desktop = getDesktopPane();

        if (desktop != null) {
            // 1. Añadir a la capa superior (MODAL_LAYER) para que no se esconda
            desktop.add(win, JLayeredPane.MODAL_LAYER);

            // 2. Centrar la ventana respecto al escritorio actual
            Dimension desktopSize = desktop.getSize();
            Dimension frameSize = win.getSize();
            win.setLocation((desktopSize.width - frameSize.width) / 2,
                    (desktopSize.height - frameSize.height) / 2);

            win.setVisible(true);

            // 3. Forzar el frente y el foco después de procesar el evento del clic
            SwingUtilities.invokeLater(() -> {
                try {
                    win.setSelected(true);
                    win.toFront();
                    win.requestFocus();
                } catch (java.beans.PropertyVetoException e) {
                    e.printStackTrace();
                }
            });

            win.addInternalFrameListener(new InternalFrameAdapter() {
                @Override
                public void internalFrameClosed(InternalFrameEvent e) {
                    loadAllUsers();
                }
            });
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }
}
