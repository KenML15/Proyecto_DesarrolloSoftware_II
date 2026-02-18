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
public class FeeWindow extends BaseInternalFrame {

    private JComboBox<String> vehicleTypeCombo;
    private JTextField txtHalfHour, txtHour, txtDay, txtWeek, txtMonth, txtYear;
    private JButton btnSave, btnCancel;
    private FeeController feeController;
    private Fee feeToEdit;

    public FeeWindow(Fee fee, FeeController controller) {
        super(fee == null ? "NUEVA TARIFA" : "EDITAR TARIFA");
        this.feeToEdit = fee;
        this.feeController = controller;
        initUI();
        loadFeeData();
    }

    private void initUI() {
        setSize(500, 550);
        setLayout(new BorderLayout());

        // Panel de Formulario
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 15, 15));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(null, " DATOS DE TARIFARIO ", 0, 0, labelFont, primaryColor),
                BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));

        // Tipo de vehículo
        formPanel.add(createLabel("Tipo de Vehículo*:"));
        vehicleTypeCombo = new JComboBox<>(new String[]{
            "-- Seleccione --", "Motocicleta", "Liviano", "Pesado", "Bicicleta", "Otro"
        });
        vehicleTypeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(vehicleTypeCombo);

        // Creación y estilización de campos
        txtHalfHour = createStyledField(formPanel, "Media Hora (₡)*:");
        txtHour = createStyledField(formPanel, "Hora (₡)*:");
        txtDay = createStyledField(formPanel, "Día (₡)*:");
        txtWeek = createStyledField(formPanel, "Semana (₡)*:");
        txtMonth = createStyledField(formPanel, "Mes (₡)*:");
        txtYear = createStyledField(formPanel, "Año (₡)*:");

        add(formPanel, BorderLayout.CENTER);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttonPanel.setBackground(backgroundColor);

        btnSave = new JButton("GUARDAR");
        styleButton(btnSave);
        btnSave.addActionListener(e -> insertFee());

        btnCancel = new JButton("CANCELAR");
        styleButton(btnCancel);
        btnCancel.setBackground(new Color(127, 140, 141));
        btnCancel.addActionListener(e -> dispose());

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        add(buttonPanel, BorderLayout.SOUTH);
    }

 private JTextField createStyledField(JPanel panel, String labelText) {
    // Aquí usamos createLabel para que procese el HTML del asterisco
    panel.add(createLabel(labelText)); 
    
    JTextField field = new JTextField();
    styleTextField(field); // El método de estilo que ya tenías
    panel.add(field);
    return field;
}

private JLabel createLabel(String text) {
    // Buscamos el * y le ponemos una etiqueta de color mediante HTML
    String htmlText = "<html>" + text.replace("*", "<font color='red'>*</font>") + "</html>";
    JLabel label = new JLabel(htmlText);
    label.setFont(labelFont); // Usamos la fuente moderna de la aplicación
    return label;
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
        if (!validateFields()) {
            return;
        }

        try {
            Fee fee = createFeeFromFields();

            if (feeToEdit == null) {
                feeController.insertFee(fee);
                showSuccess("Tarifa creada exitosamente.");
            } else {
                feeController.updateFee(fee);
                showSuccess("Tarifa actualizada exitosamente.");
            }
            dispose();

        } catch (IOException e) {
            showError("Error al procesar la tarifa: " + e.getMessage());
        }
    }

    private boolean validateFields() {
        if (vehicleTypeCombo.getSelectedIndex() <= 0) {
            showWarning("Por favor, seleccione un tipo de vehículo.");
            return false;
        }

        try {
            // Validar que no estén vacíos y sean números
            double fHalf = parse(txtHalfHour);
            double fHour = parse(txtHour);
            double fDay = parse(txtDay);
            double fWeek = parse(txtWeek);
            double fMonth = parse(txtMonth);
            double fYear = parse(txtYear);

            if (fHalf <= 0 || fHour <= 0 || fDay <= 0 || fWeek <= 0 || fMonth <= 0 || fYear <= 0) {
                showWarning("Todos los montos deben ser mayores a cero.");
                return false;
            }

            // Validación de progresión lógica
            if (fHour < fHalf || fDay < fHour || fWeek < fDay || fMonth < fWeek || fYear < fMonth) {
                showWarning("Error de progresión: Una tarifa de mayor tiempo no puede ser menor a la anterior.");
                return false;
            }

            return true;
        } catch (NumberFormatException e) {
            showWarning("Asegúrese de ingresar solo números válidos en las tarifas.");
            return false;
        }
    }

    private double parse(JTextField field) {
        return Double.parseDouble(field.getText().trim());
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

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }

    private void showSuccess(String tarifa_creada_exitosamente) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
