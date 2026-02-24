/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.AdministratorController;
import controller.ParkingLotFileController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import model.data.StaffDataFile;
import model.entities.Clerk;
import model.entities.ParkingLot;
import org.jdom2.JDOMException;

/**
 *
 * @author user
 */
public class AssignClerkWindow extends BaseInternalFrame {

    private JComboBox<Clerk> comboClerks;
    private JComboBox<ParkingLot> comboLots;
    private ParkingLotFileController lotController;
    private AdministratorController adminController; // Para obtener la lista de dependientes

    public AssignClerkWindow() {
        super("ASIGNAR ENCARGADO A PARQUEO");
        try {
            this.lotController = new ParkingLotFileController();
            this.adminController = new AdministratorController();
            initComponents();
        } catch (IOException | JDOMException e) {
            showError("Error al inicializar: " + e.getMessage());
        }
    }

    private void initComponents() {
        setSize(450, 300);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridLayout(3, 2, 10, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        mainPanel.setBackground(Color.WHITE);

        // --- Selectores ---
        mainPanel.add(new JLabel("Seleccionar Parqueo:"));
        try {
            mainPanel.add(new JLabel("Seleccionar Parqueo:"));
            ArrayList<ParkingLot> lots = lotController.getAllParkingLots();
            comboLots = new JComboBox<>(lots.toArray(new ParkingLot[0]));

            // Renderer para mostrar el nombre del parqueo
            comboLots.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (value instanceof ParkingLot) {
                        setText(((ParkingLot) value).getName());
                    }
                    return this;
                }
            });
            mainPanel.add(comboLots);

            mainPanel.add(new JLabel("Seleccionar Dependiente:"));
            StaffDataFile staffFile = new StaffDataFile();
            ArrayList<Clerk> clerks = staffFile.getAllStaff();
            comboClerks = new JComboBox<>(clerks.toArray(new Clerk[0]));

            // Renderer para mostrar el nombre del dependiente
            comboClerks.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (value instanceof Clerk) {
                        setText(((Clerk) value).getName());
                    }
                    return this;
                }
            });
            mainPanel.add(comboClerks);

        } catch (Exception e) {
            showError("Error cargando datos: " + e.getMessage());
        }

        // --- Botón Guardar ---
        JButton btnSave = new JButton("ASIGNAR ENCARGADO");
        styleButton(btnSave);
        btnSave.setBackground(primaryColor);
        btnSave.addActionListener(e -> saveAssignment());

        add(mainPanel, BorderLayout.CENTER);
        add(btnSave, BorderLayout.SOUTH);
    }

    private void saveAssignment() {
        ParkingLot selectedLot = (ParkingLot) comboLots.getSelectedItem();
        Clerk selectedClerk = (Clerk) comboClerks.getSelectedItem();

        if (selectedLot == null || selectedClerk == null) {
            showError("Debes seleccionar un parqueo y un encargado.");
            return;
        }

        try {
            // 1. Asignar en memoria
            selectedLot.setAssignedClerk(selectedClerk);

            // 2. Persistencia real (Esto es lo que te falta para que "se asigne en algún lado")
            lotController.updateParkingLot(selectedLot);

            showSuccess("¡Asignación exitosa! " + selectedClerk.getName() + " ahora cuida " + selectedLot.getName());
            this.dispose();
        } catch (Exception e) {
            showError("Error al guardar en el archivo: " + e.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }
}

