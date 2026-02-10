/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.FeeController;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import model.entities.Fee;

/**
 *
 * @author 50687
 */
public class FeeWindow extends JInternalFrame{

    private JComboBox<String> vehicleTypeCombo;
    private JTextField txtHalfHour, txtHour, txtDay, txtWeek, txtMonth, txtYear;
    private JButton btnSave, btnCancel;
    private FeeController feeController;
    private Fee feeToEdit;
    
    public FeeWindow(Fee fee, FeeController controller) {
        super(fee == null ? "Nueva Tarifa" : "Editar Tarifa", true, true, true, true);
        this.feeToEdit = fee;
        this.feeController = controller;
        initUI();
        loadFeeData();
    }
    
    private void initUI() {
        setSize(450, 400);
        setLocation(200, 150);
        
        JPanel mainPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        //Tipo de vehículo
        mainPanel.add(new JLabel("Tipo de Vehículo*:"));
        vehicleTypeCombo = new JComboBox<>();
        vehicleTypeCombo.addItem("-- Seleccione --");
        vehicleTypeCombo.addItem("Motocicleta");
        vehicleTypeCombo.addItem("Liviano");
        vehicleTypeCombo.addItem("Pesado");
        vehicleTypeCombo.addItem("Bicicleta");
        vehicleTypeCombo.addItem("Otro");
        mainPanel.add(vehicleTypeCombo);
        
        //Campos de tarifas
        mainPanel.add(new JLabel("Media Hora (₡)*:"));
        txtHalfHour = new JTextField();
        mainPanel.add(txtHalfHour);
        
        mainPanel.add(new JLabel("Hora (₡)*:"));
        txtHour = new JTextField();
        mainPanel.add(txtHour);
        
        mainPanel.add(new JLabel("Día (₡)*:"));
        txtDay = new JTextField();
        mainPanel.add(txtDay);
        
        mainPanel.add(new JLabel("Semana (₡)*:"));
        txtWeek = new JTextField();
        mainPanel.add(txtWeek);
        
        mainPanel.add(new JLabel("Mes (₡)*:"));
        txtMonth = new JTextField();
        mainPanel.add(txtMonth);
        
        mainPanel.add(new JLabel("Año (₡)*:"));
        txtYear = new JTextField();
        mainPanel.add(txtYear);
        
        //Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnSave = new JButton("Guardar");
        btnCancel = new JButton("Cancelar");
        
        btnSave.addActionListener(e -> insertFee());
        btnCancel.addActionListener(e -> dispose());
        
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        
        //Agregar al frame
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void loadFeeData() {
        if (feeToEdit != null) {
            //Seleccionar tipo de vehículo
            for (int i = 0; i < vehicleTypeCombo.getItemCount(); i++) {
                if (vehicleTypeCombo.getItemAt(i).equals(feeToEdit.getVehicleType())) {
                    vehicleTypeCombo.setSelectedIndex(i);
                    vehicleTypeCombo.setEnabled(false); //No se puede cambiar el tipo
                    break;
                }
            }
            
            txtHalfHour.setText(String.valueOf(feeToEdit.getHalfHourRate()));
            txtHour.setText(String.valueOf(feeToEdit.getHourlyRate()));
            txtDay.setText(String.valueOf(feeToEdit.getDailyRate()));
            txtWeek.setText(String.valueOf(feeToEdit.getWeeklyRate()));
            txtMonth.setText(String.valueOf(feeToEdit.getMonthlyRate()));
            txtYear.setText(String.valueOf(feeToEdit.getAnnualRate()));
        }
    }
    
    private void insertFee() {
        try {
            validateFields();
            Fee fee = createFeeFromFields();
            
            if (feeToEdit == null) {
                feeController.insertFee(fee);
                JOptionPane.showMessageDialog(this, "Tarifa creada exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                feeController.updateFee(fee);
                JOptionPane.showMessageDialog(this, "Tarifa actualizada exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
            
            dispose();
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al insertar o actualizar la tarifa");
        }
    }
    
    private void validateFields() {
        if (vehicleTypeCombo.getSelectedIndex() <= 0) {
            throw new IllegalArgumentException("Seleccione un tipo de vehículo");
        }
        
        try {
            double halfHour = Double.parseDouble(txtHalfHour.getText().trim());
            double hour = Double.parseDouble(txtHour.getText().trim());
            double day = Double.parseDouble(txtDay.getText().trim());
            double week = Double.parseDouble(txtWeek.getText().trim());
            double month = Double.parseDouble(txtMonth.getText().trim());
            double year = Double.parseDouble(txtYear.getText().trim());
            
            if (halfHour <= 0 || hour <= 0 || day <= 0 || week <= 0 || month <= 0 || year <= 0) {
                throw new IllegalArgumentException("Todas las tarifas deben ser positivas");
            }
            
            //Validar progresión lógica
            if (hour < halfHour) {
                JOptionPane.showMessageDialog(this, "La tarifa por hora debe ser mayor o igual a media hora");
            }
            if (day < hour) {
                JOptionPane.showMessageDialog(this, "La tarifa diaria debe ser mayor o igual a la horaria");
            }
            if (week < day) {
                JOptionPane.showMessageDialog(this, "La tarifa semanal debe ser mayor o igual a la diaria");
            }
            if (month < week) {
                JOptionPane.showMessageDialog(this, "La tarifa mensual debe ser mayor o igual a la semanal");
            }
            if (year < month) {
                JOptionPane.showMessageDialog(this, "La tarifa anual debe ser mayor o igual a la mensual");
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "¡Error! El monto de la tarifa debe ser mayor al anterior.");
        }
    }
    
    private Fee createFeeFromFields() {
        String vehicleType = (String) vehicleTypeCombo.getSelectedItem();
        float halfHour = Float.parseFloat(txtHalfHour.getText().trim());
        float hour = Float.parseFloat(txtHour.getText().trim());
        float day = Float.parseFloat(txtDay.getText().trim());
        float week = Float.parseFloat(txtWeek.getText().trim());
        float month = Float.parseFloat(txtMonth.getText().trim());
        float year = Float.parseFloat(txtYear.getText().trim());
        
        return new Fee(vehicleType, halfHour, hour, day, week, month, year);
    }
}
