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
import javax.swing.table.DefaultTableModel;
import model.data.StaffDataFile;
import model.entities.Administrator;
import model.entities.Clerk;
import model.entities.User;

public class UserManagement extends BaseInternalFrame {

    private AdministratorController adminCtrl;
    private ClerkController clerkCtrl;
    private JTable userTable;
    private DefaultTableModel tableModel;
    private final String[] COLUMNS = {"Cédula", "Nombre", "Usuario", "Rol", "Código", "Horario"};

    public UserManagement() {
        super("GESTIÓN DE PERSONAL");
        // Usamos los controladores (preferiblemente los que tienen el 'static' que pusimos antes)
        this.adminCtrl = new AdministratorController();
        this.clerkCtrl = new ClerkController();

        initComponents();
        loadAllUsers();
    }

    private void initComponents() {
        setSize(900, 550);
        setLayout(new BorderLayout(10, 10));

        // --- TABLA ---
        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        userTable = new JTable(tableModel);
        userTable.setRowHeight(30);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- BOTONES ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));

        JButton btnAdd = createStyledButton("Nuevo Usuario", new Color(46, 204, 113));
        JButton btnDelete = createStyledButton("Eliminar", new Color(231, 76, 60));
        JButton btnRefresh = createStyledButton("Refrescar", new Color(149, 165, 166));

        btnAdd.addActionListener(e -> openRegistrationWindow());
        btnDelete.addActionListener(e -> deleteSelectedUser());
        btnRefresh.addActionListener(e -> loadAllUsers());

        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnDelete);

        add(new JLabel("  Listado de Administradores y Empleados", SwingConstants.LEFT), BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
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
        System.err.println("Error al cargar tabla: " + ex.getMessage());
    }
}

    private void deleteSelectedUser() {
        int row = userTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un usuario de la tabla para eliminar.");
            return;
        }

        // Obtenemos los datos de la fila seleccionada
        String id = (String) tableModel.getValueAt(row, 0); // Columna Cédula
        String name = (String) tableModel.getValueAt(row, 1); // Columna Nombre
        String rol = (String) tableModel.getValueAt(row, 3); // Columna Rol

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de que desea eliminar a: " + name + " (" + rol + ")?",
                "Confirmar Eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (rol.equals("Administrador")) {
                adminCtrl.deleteAdministrator(id);
            } else {
                clerkCtrl.deleteClerk(id);
            }

            loadAllUsers(); // Refresca la tabla automáticamente
            JOptionPane.showMessageDialog(this, "Usuario eliminado correctamente.");
        }
    }

    private void openRegistrationWindow() {
        UserManagementWindow win = new UserManagementWindow(adminCtrl, clerkCtrl);
        getDesktopPane().add(win);
        win.setVisible(true);
        win.toFront();

        // Refrescar al cerrar la ventana de registro
        win.addInternalFrameListener(new javax.swing.event.InternalFrameAdapter() {
            @Override
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent e) {
                loadAllUsers();
            }
        });
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setPreferredSize(new Dimension(150, 40));

        // --- ESTO ES LO QUE HACE QUE EL COLOR SE VEA ---
        btn.setOpaque(true);
        btn.setBorderPainted(false); // Quita el borde gris de Windows
        btn.setContentAreaFilled(true); // Fuerza el color de fondo
        btn.setFocusPainted(false); // Quita el cuadro de puntos al hacer clic

        return btn;
    }
}
