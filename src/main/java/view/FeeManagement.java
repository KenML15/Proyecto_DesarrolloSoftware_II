/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.FeeController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import model.entities.Fee;
import org.jdom2.JDOMException;

/**
 *
 * @author 50687
 */
public class FeeManagement extends BaseInternalFrame {

    private JTable feeTable;
    private DefaultTableModel tableModel;
    private JButton btnAdd, btnEdit, btnDelete, btnRefresh;
    private JComboBox<String> vehicleTypeCombo;
    private FeeController feeController;

    private final String[] COLUMNS = {"Tipo de Vehículo", "Media Hora", "Hora", "Día", "Semana", "Mes", "Año"};

    public FeeManagement() {
        super("GESTIÓN DE TARIFAS"); //Título para la barra superior azul
        init();
        createUI();
        loadFees();
    }

    private void init() {
        try {
            feeController = new FeeController();
        } catch (JDOMException | IOException e) {
            showError("Error al inicializar el controlador de tarifas: " + e.getMessage());
            if (e instanceof IOException) {
                dispose();
            }
        }
    }

    private void createUI() {
        setSize(1000, 600);
        setLayout(new BorderLayout(0, 0));

        //Panel Central que contiene el filtro y la tabla
        JPanel centerPanel = new JPanel(new BorderLayout(15, 15));
        centerPanel.setBackground(backgroundColor);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        centerPanel.add(createFilterPanel(), BorderLayout.NORTH);
        centerPanel.add(createTablePanel(), BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panel.setBackground(backgroundColor);

        JLabel lblFilter = new JLabel("Filtrar por Vehículo:");
        lblFilter.setFont(labelFont);
        panel.add(lblFilter);

        vehicleTypeCombo = new JComboBox<>(new String[]{"Todos", "Moto", "Liviano", "Pesado", "Bicicleta", "Otro"});
        vehicleTypeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        vehicleTypeCombo.setBackground(Color.WHITE);
        vehicleTypeCombo.addActionListener(e -> filterFees());
        panel.add(vehicleTypeCombo);

        return panel;
    }

    private JScrollPane createTablePanel() {
        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        feeTable = new JTable(tableModel);
        styleTable(feeTable);

        feeTable.getColumnModel().getColumn(0).setPreferredWidth(150);

        JScrollPane scroll = new JScrollPane(feeTable);
        scroll.setBorder(BorderFactory.createLineBorder(fieldBorderColor));
        return scroll;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        panel.setBackground(backgroundColor);

        btnAdd = createStyledButton("AGREGAR TARIFA", new Color(39, 174, 96), panel);
        btnEdit = createStyledButton("EDITAR SELECCIÓN", new Color(230, 126, 34), panel);
        btnDelete = createStyledButton("ELIMINAR TARIFA", new Color(192, 57, 43), panel);
        btnRefresh = createStyledButton("ACTUALIZAR", primaryColor, panel);

        btnAdd.addActionListener(e -> openFeeWindow(null));
        btnEdit.addActionListener(e -> editSelectedFee());
        btnDelete.addActionListener(e -> deleteSelectedFee());
        btnRefresh.addActionListener(e -> loadFees());

        return panel;
    }

    private JButton createStyledButton(String text, Color bg, JPanel panel) {
        JButton btn = new JButton(text);
        styleButton(btn);
        btn.setBackground(bg);
        panel.add(btn);
        return btn;
    }

    private void loadFees() {
        try {
            ArrayList<Fee> fees = feeController.getAllFees();
            updateTable(fees);
            vehicleTypeCombo.setSelectedIndex(0);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar las tarifas" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTable(ArrayList<Fee> fees) {
        tableModel.setRowCount(0);

        for (Fee fee : fees) {
            Object[] row = {
                fee.getVehicleType(),
                String.format("₡%.2f", fee.getHalfHourRate()),
                String.format("₡%.2f", fee.getHourlyRate()),
                String.format("₡%.2f", fee.getDailyRate()),
                String.format("₡%.2f", fee.getWeeklyRate()),
                String.format("₡%.2f", fee.getMonthlyRate()),
                String.format("₡%.2f", fee.getAnnualRate())
            };
            tableModel.addRow(row);
        }
    }

    private void filterFees() {
        try {
            String selectedType = (String) vehicleTypeCombo.getSelectedItem();
            ArrayList<Fee> allFees = feeController.getAllFees();
            ArrayList<Fee> filteredFees = new ArrayList<>();

            if ("Todos".equals(selectedType)) {
                filteredFees = allFees;
            } else {
                for (Fee fee : allFees) {
                    if (fee.getVehicleType().equalsIgnoreCase(selectedType)) {
                        filteredFees.add(fee);
                    }
                }
            }

            updateTable(filteredFees);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al filtrar las tarifas" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openFeeWindow(Fee fee) {
        FeeWindow window = new FeeWindow(fee, feeController);
        window.addInternalFrameListener(new javax.swing.event.InternalFrameAdapter() {
            @Override
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent e) {
                loadFees();
            }
        });

        getDesktopPane().add(window);
        window.setVisible(true);
        window.toFront();
    }

    private void editSelectedFee() {
        int row = feeTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una tarifa para editar", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String vehicleType = (String) tableModel.getValueAt(row, 0);
            Fee fee = feeController.getFeeByVehicleType(vehicleType);
            if (fee != null) {
                openFeeWindow(fee);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "No se pudo encontrar la información del tipo de vehículo en el archivo" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedFee() {
        int row = feeTable.getSelectedRow();
        if (row < 0) {
            showWarning("Seleccione una tarifa de la tabla para eliminarla.");
            return;
        }

        String vehicleType = (String) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar permanentemente la tarifa para: " + vehicleType + "?",
                "Confirmar Eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                feeController.deleteFee(vehicleType);
                showSuccess("La tarifa se ha eliminado correctamente.");
                loadFees();
            } catch (IOException e) {
                showError("Error al intentar eliminar la tarifa: " + e.getMessage());
            }
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }

    private void showSuccess(String string) {
        JOptionPane.showMessageDialog(this, string);
    }
}
