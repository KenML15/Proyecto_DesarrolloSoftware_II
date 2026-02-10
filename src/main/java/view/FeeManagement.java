/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.FeeController;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import model.entities.Fee;
import org.jdom2.JDOMException;

/**
 *
 * @author 50687
 */
public class FeeManagement extends JInternalFrame{

    private JTable feeTable;
    private DefaultTableModel tableModel;
    private JButton btnAdd, btnEdit, btnDelete, btnRefresh;
    private JComboBox<String> vehicleTypeCombo;
    private FeeController feeController;
    
    private final String[] COLUMNS = {"Tipo de Vehículo", "Media Hora", "Hora", "Día", "Semana", "Mes", "Año"};

    public FeeManagement() {
        super("Gestión de Tarifas", true, true, true, true);
        init();
        createUI();
        loadFees();
    }
    
    private void init() {
        try {
            feeController = new FeeController();
        } catch (JDOMException e) {
            JOptionPane.showMessageDialog(this, "El archivo tiene un formato inválido" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "No se pudo acceder al archivo" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
        }
    }
    
    private void createUI() {
        setSize(900, 500);
        setLocation(100, 100);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        mainPanel.add(createFilterPanel(), BorderLayout.NORTH);
        mainPanel.add(createTablePanel(), BorderLayout.CENTER);
        mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
    }
    
    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        panel.add(new JLabel("Tipo de Vehículo:"));
        vehicleTypeCombo = new JComboBox<>();
        vehicleTypeCombo.addItem("Todos");
        vehicleTypeCombo.addItem("Motocicleta");
        vehicleTypeCombo.addItem("Liviano");
        vehicleTypeCombo.addItem("Pesado");
        vehicleTypeCombo.addItem("Bicicleta");
        vehicleTypeCombo.addItem("Otro");
        
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
        feeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        return new JScrollPane(feeTable);
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        
        btnAdd = new JButton("Agregar Tarifa");
        btnEdit = new JButton("Editar Tarifa");
        btnDelete = new JButton("Eliminar Tarifa");
        btnRefresh = new JButton("Actualizar");
        
        btnAdd.addActionListener(e -> openFeeWindow(null));
        btnEdit.addActionListener(e -> editSelectedFee());
        btnDelete.addActionListener(e -> deleteSelectedFee());
        btnRefresh.addActionListener(e -> loadFees());
        
        panel.add(btnAdd);
        panel.add(btnEdit);
        panel.add(btnDelete);
        panel.add(btnRefresh);
        
        return panel;
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
            JOptionPane.showMessageDialog(this, "Seleccione una tarifa para eliminar", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            String vehicleType = (String) tableModel.getValueAt(row, 0);
            
            int confirm = JOptionPane.showConfirmDialog(this,
                "¿Eliminar tarifa para " + vehicleType + "?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                feeController.deleteFee(vehicleType);
                JOptionPane.showMessageDialog(this, "Tarifa eliminada exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                loadFees();
            }
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "No se pudo eliminar la tarifa del sistema" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
