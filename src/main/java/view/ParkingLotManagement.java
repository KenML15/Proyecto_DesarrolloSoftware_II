/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.ParkingLotFileController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableModel;
import model.entities.ParkingLot;
import model.entities.Space;
import org.jdom2.JDOMException;

/**
 *
 * @author 50687
 */
public class ParkingLotManagement extends BaseInternalFrame {

    private JButton buttonDelete, buttonConfigure, buttonStatus, buttonExit;
    private JPanel panelParkingLots;
    private JTable tableParkingLots;
    private DefaultTableModel modelDataTable;
    private final String[] headings = {"ID", "Nombre", "Espacios Totales", "Preferenciales", "Ocupados", "Encargado"};
    private ParkingLotFileController parkingLotController;

    public ParkingLotManagement() {
        super("GESTIÓN DE PARQUEOS"); //Título para la barra superior
        initializeController();
        initializeComponents();
        loadParkingLots();
        SwingUtilities.invokeLater(() -> centerInDesktop());
    }

    private void initializeController() {
        try {
            this.parkingLotController = new ParkingLotFileController();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "No se puede acceder al archivo de parqueos" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (JDOMException ex) {
            Logger.getLogger(ParkingLotManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initializeComponents() {
        setSize(800, 550);
        setLocation(150, 50);
        setLayout(new BorderLayout(0, 0));

        //Panel Central para la Tabla
        JPanel centerPanel = new JPanel(new BorderLayout(15, 15));
        centerPanel.setBackground(backgroundColor);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        tableParkingLots = new JTable();
        styleTable(tableParkingLots); // Método de BaseInternalFrame

        JScrollPane scrollPane = new JScrollPane(tableParkingLots);
        scrollPane.setBorder(BorderFactory.createLineBorder(fieldBorderColor));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        //Panel de Botones Inferior
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(backgroundColor);

        createButtons(buttonPanel);

        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void createButtons(JPanel panel) {
        buttonDelete = createStyledButton("ELIMINAR", new Color(192, 57, 43), panel);
        buttonConfigure = createStyledButton("CONFIGURAR", new Color(39, 174, 96), panel);
        buttonStatus = createStyledButton("VER ESTADO", primaryColor, panel);
        buttonExit = createStyledButton("REGISTRAR SALIDA", new Color(230, 126, 34), panel);

        buttonDelete.addActionListener(e -> deleteParkingLot());
        buttonConfigure.addActionListener(e -> configureSpaces());
        buttonStatus.addActionListener(e -> showParkingLotStatus());
        buttonExit.addActionListener(e -> openVehicleExitWindow());
    }

    private JButton createStyledButton(String text, Color bg, JPanel panel) {
        JButton btn = new JButton(text);
        styleButton(btn); //Método de BaseInternalFrame
        btn.setBackground(bg);
        panel.add(btn);
        return btn;
    }

    private void openVehicleExitWindow() {
        int selectedRow = tableParkingLots.getSelectedRow();
        if (selectedRow < 0) {
            showWarning("Seleccione un parqueo primero");
            return;
        }

        try {
            int parkingLotId = Integer.parseInt(modelDataTable.getValueAt(selectedRow, 0).toString());
            ParkingLot selectedLot = parkingLotController.getParkingLotById(parkingLotId);

            VehicleExitWindow exitWindow = new VehicleExitWindow(selectedLot);

            // 1. Agregar y subir de capa
            getDesktopPane().add(exitWindow);
            getDesktopPane().setLayer(exitWindow, JLayeredPane.MODAL_LAYER);

            exitWindow.setVisible(true);

            // 2. Forzar frente y foco
            SwingUtilities.invokeLater(() -> {
                try {
                    exitWindow.setSelected(true);
                    exitWindow.toFront();
                    exitWindow.requestFocus();
                } catch (java.beans.PropertyVetoException e) {
                    e.printStackTrace();
                }
            });

            exitWindow.addInternalFrameListener(new InternalFrameAdapter() {
                @Override
                public void internalFrameClosed(InternalFrameEvent e) {
                    loadParkingLots();
                }
            });

        } catch (IOException | NumberFormatException e) {
            showError("Error al abrir ventana de salida: " + e.getMessage());
        }
    }

    private void loadParkingLots() {
        try {
            ArrayList<ParkingLot> parkingLots = parkingLotController.getAllParkingLots();
            String[][] data = createDataMatrix(parkingLots);
            modelDataTable = new DefaultTableModel(data, headings);
            tableParkingLots.setModel(modelDataTable);

        } catch (IOException e) {
            showError("Error al cargar los parqueos" + e.getMessage());
        }
    }

    private int countOccupiedSpaces(ParkingLot parkingLot) {
        int count = 0;
        if (parkingLot != null && parkingLot.getSpaces() != null) {
            for (Space space : parkingLot.getSpaces()) {
                if (space != null && space.isSpaceTaken()) {
                    count++;
                }
            }
        }
        return count;
    }

    private String[][] createDataMatrix(ArrayList<ParkingLot> parkingLots) {
        String[][] data = new String[parkingLots.size()][6];

        for (int i = 0; i < parkingLots.size(); i++) {
            ParkingLot parkingLot = parkingLots.get(i);

            int disability = 0;
            for (Space space : parkingLot.getSpaces()) {
                if (space.isDisabilityAdaptation()) {
                    disability++;
                }
            }

            data[i][0] = String.valueOf(parkingLot.getId());
            data[i][1] = parkingLot.getName();
            data[i][2] = String.valueOf(parkingLot.getNumberOfSpaces());
            data[i][3] = String.valueOf(disability);
            data[i][4] = String.valueOf(countOccupiedSpaces(parkingLot));
            data[i][5] = (parkingLot.getAssignedClerk() != null)
                    ? parkingLot.getAssignedClerk().getName()
                    : "Sin asignar";
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
                    "¿Está seguro de eliminar el parqueo? Esta acción no se puede deshacer.",
                    "Confirmar Eliminación",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                parkingLotController.deleteParkingLot(id);
                loadParkingLots();
                showSuccess("Parqueo eliminado exitosamente.");
            }

        } catch (IllegalStateException ex) {
            showError("No se puede eliminar: " + ex.getMessage());
        } catch (HeadlessException | IOException e) {
            showError("Ocurrió un error inesperado: " + e.getMessage());
        }
    }

    private void configureSpaces() {
        try {
            int id = getSelectedParkingLotId();
            ParkingLot parkingLot = parkingLotController.getParkingLotById(id);
            SpaceConfigurationWindow window = new SpaceConfigurationWindow(parkingLot, parkingLotController);

            getDesktopPane().add(window);
            getDesktopPane().setLayer(window, JLayeredPane.DRAG_LAYER); // Capa muy alta

            window.setVisible(true);

            // Aseguramos que salte al frente inmediatamente
            SwingUtilities.invokeLater(() -> {
                try {
                    window.setSelected(true);
                    window.toFront();
                } catch (java.beans.PropertyVetoException e) {
                    e.printStackTrace();
                }
            });

        } catch (Exception e) {
            showError("Error al abrir ventana: " + e.getMessage());
        }
    }

    private void showParkingLotStatus() {
        try {
            int id = getSelectedParkingLotId();
            ParkingLot parkingLot = parkingLotController.getParkingLotById(id);

            Space[] spaces = parkingLot.getSpaces();
            int occupied = 0;
            int disability = 0;

            for (Space space : spaces) {
                if (space.isSpaceTaken()) {
                    occupied++;
                }
                if (space.isDisabilityAdaptation()) {
                    disability++;
                }

            }

            String status = String.format("""
    ╔══════════════════════════════════════════╗
    ║           ESTADO DEL PARQUEO             ║
    ╠══════════════════════════════════════════╣
    ║  Parqueo:   %-25s║
    ║  Encargado: %-25s║
    ║  ──────────────────────────────────────  ║
    ║  [Σ] Espacios Totales:      %-12d ║
    ║  [X] Espacios Ocupados:     %-12d ║
    ║  [O] Espacios Libres:       %-12d ║
    ╚══════════════════════════════════════════╝
    """,
                    parkingLot.getName(),
                    (parkingLot.getAssignedClerk() != null ? parkingLot.getAssignedClerk().getName() : "NO ASIGNADO"),
                    spaces.length,
                    occupied,
                    spaces.length - occupied
            );

            JOptionPane.showMessageDialog(this, status, "Estado del Parqueo",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            showError("Información de estado no disponible" + e.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void showInfo(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
}
