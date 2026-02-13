/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.ParkingLotFileController;
import java.awt.Color;
import java.awt.HeadlessException;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import model.entities.ParkingLot;
import model.entities.Space;

/**
 *
 * @author 50687
 */
public class ParkingLotManagement extends JInternalFrame {

    private JButton buttonDelete, buttonConfigure, buttonStatus;
    private JPanel panelParkingLots;
    private JTable tableParkingLots;
    private DefaultTableModel modelDataTable;
    
    private final String[] headings = {"ID", "Nombre", "Espacios Totales", "Espacios Ocupados"};

    private ParkingLotFileController controller;

    public ParkingLotManagement() {
        super("Gestión de Parqueos", false, true, false, true);
        initializeController();
        initializeComponents();
        loadParkingLots();
    }

    private void initializeController() {
        try {
            this.controller = new ParkingLotFileController();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "No se puede acceder al archivo de parqueos" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initializeComponents() {
        setVisible(true);
        setSize(650, 500);
        setLocation(230, 50);
        setResizable(false);

        panelParkingLots = new JPanel();
        panelParkingLots.setLayout(null);
        panelParkingLots.setBackground(Color.WHITE);
        panelParkingLots.setVisible(true);
        add(panelParkingLots);

        createTable();
        createButtons();
    }

    private void createTable() {
        tableParkingLots = new JTable();
        tableParkingLots.setSize(400, 1000);
        tableParkingLots.setLocation(95, 300);
        panelParkingLots.add(tableParkingLots);

        JScrollPane scrollBar = new JScrollPane(tableParkingLots);
        scrollBar.setBounds(25, 75, 600, 249);
        panelParkingLots.add(scrollBar);
    }

    private void createButtons() {
        buttonDelete = createButton("Eliminar", 50);
        buttonConfigure = createButton("Configurar", 200);
        buttonStatus = createButton("Estado", 350);

        buttonDelete.addActionListener(e -> deleteParkingLot());
        buttonConfigure.addActionListener(e -> configureSpaces());
        buttonStatus.addActionListener(e -> showParkingLotStatus());

    }
    
    private JButton createButton(String text, int x) {
        JButton button = new JButton(text);
        button.setBounds(x, 350, 120, 30);
        panelParkingLots.add(button);
        return button;
    }

    private void loadParkingLots() {
        try {
            ArrayList<ParkingLot> parkingLots = controller.getAllParkingLots();
            String[][] data = createDataMatrix(parkingLots);
            modelDataTable = new DefaultTableModel(data, headings);
            tableParkingLots.setModel(modelDataTable);

            for (ParkingLot parkingLot : parkingLots) {
                modelDataTable.addRow(new Object[]{
                    parkingLot.getId(),
                    parkingLot.getName(),
                    parkingLot.getNumberOfSpaces(),
                    countOccupiedSpaces(parkingLot)
                });
            }
        } catch (IOException e) {
            showError("Error al cargar los parqueos" + e.getMessage());
        }
    }
    
    private int countOccupiedSpaces(ParkingLot parkingLot) {
        int count = 0;
        if (parkingLot.getSpaces() != null) {
            for (Space space : parkingLot.getSpaces()) {
                if (space != null && space.isSpaceTaken()) {
                    count++;
                }
            }
        }
        return count;
    }

    private String[][] createDataMatrix(ArrayList<ParkingLot> parkingLots) {
        String[][] data = new String[parkingLots.size()][4];

        for (int i = 0; i < parkingLots.size(); i++) {
            ParkingLot pl = parkingLots.get(i);
            data[i][0] = String.valueOf(pl.getId());
            data[i][1] = pl.getName();
            data[i][2] = String.valueOf(pl.getNumberOfSpaces());
            data[i][3] = String.valueOf(pl.getVehicles().size());
        }

        return data;
    }

    private int getSelectedParkingLotId() {
        int row = tableParkingLots.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un parqueo", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
        return Integer.parseInt(modelDataTable.getValueAt(row, 0).toString());
    }
    
    private void deleteParkingLot() {
        try {
            int id = getSelectedParkingLotId();
            int confirm = JOptionPane.showConfirmDialog(this,
                    "¿Eliminar el parqueo?", "Confirmar",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                controller.removeParkingLot(id);
                loadParkingLots();
                showSuccess("Parqueo eliminado");
            }

        } catch (HeadlessException e) {
            showError("Error al procesar la eliminación del parqueo" + e.getMessage());
        }
    }

    private void configureSpaces() {
        try {
            int id = getSelectedParkingLotId();
            ParkingLot parkingLot = controller.findParkingLotById(id);

            if (countOccupiedSpaces(parkingLot) > 0) {
                showError("No se puede configurar con vehículos estacionados");
                return;
            }
            
            SpaceConfigurationWindow window = new SpaceConfigurationWindow(parkingLot, controller);
            getDesktopPane().add(window);

        } catch (IOException e) {
            showError("Error al configurar parqueos" + e.getMessage());
        }
    }

    private void showParkingLotStatus() {
        try {
            int id = getSelectedParkingLotId();
            String status = controller.getParkingLotStatusById(id);
            JOptionPane.showMessageDialog(this, status, "Estado",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            showError("Información de estado no disponible" + e.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void showInfo(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
}
