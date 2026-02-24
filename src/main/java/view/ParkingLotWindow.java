/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.ParkingLotFileController;
import controller.SpaceFileController;
import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import org.jdom2.JDOMException;

/**
 *
 * @author 50687
 */
public class ParkingLotWindow extends BaseInternalFrame implements ActionListener {

    private JPanel panel;
    private JLabel labelName, labelAddress, labelSpaces;
    private JTextField textFieldName, textFieldSpaces;
    private JComboBox comboBoxAddress;
    private JButton buttonCreate, buttonCancel;

    private ParkingLotFileController parkingLotController;
    private SpaceFileController spaceController;

    public ParkingLotWindow(ParkingLotFileController controller) throws IOException, JDOMException {
        super("CREAR NUEVO PARQUEO"); // Título para el encabezado azul
        this.parkingLotController = controller;
        this.spaceController = new SpaceFileController();
        setupWindow();
    }

    private void setupWindow() {
        setWindowProperties();
        createPanel();
        createComponents();
    }

    private void setWindowProperties() {
        setSize(450, 380);
        setLocation(250, 150);
        setResizable(false);
        setVisible(true);

        // Forzar el frente y el foco después de la renderización inicial
        SwingUtilities.invokeLater(() -> {
            try {
                this.toFront();
                this.setSelected(true);
                this.requestFocus();
            } catch (java.beans.PropertyVetoException e) {
                e.printStackTrace();
            }
        });
    }

    private void createPanel() {
        panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.WHITE); //Fondo limpio
        add(panel);
    }

    private void createComponents() {
        //Nombre del parqueo
        labelName = new JLabel("Nombre del Parqueo:");
        labelName.setBounds(50, 40, 150, 25);
        labelName.setFont(labelFont);
        panel.add(labelName);

        textFieldName = new JTextField();
        textFieldName.setBounds(200, 40, 180, 30);
        styleTextField(textFieldName);
        panel.add(textFieldName);

        //Dirección del parqueo
        labelAddress = new JLabel("Dirección:");
        labelAddress.setBounds(50, 140, 150, 25);
        panel.add(labelAddress);

        comboBoxAddress = new JComboBox<>(addresses);
        comboBoxAddress.setBounds(200, 140, 180, 30);
        comboBoxAddress.setBackground(Color.WHITE);
        comboBoxAddress.setFont(labelFont);
        panel.add(comboBoxAddress);

        //Espacios del parqueo
        labelSpaces = new JLabel("Espacios totales:");
        labelSpaces.setBounds(50, 90, 180, 25);
        labelSpaces.setFont(labelFont);
        panel.add(labelSpaces);

        textFieldSpaces = new JTextField();
        textFieldSpaces.setBounds(200, 90, 180, 30);
        styleTextField(textFieldSpaces); // Aplicamos borde azul y padding
        panel.add(textFieldSpaces);

        createButtons();
    }

    private void createButtons() {
        buttonCreate = new JButton("CREAR");
        buttonCreate.setBounds(100, 220, 120, 35);
        styleButton(buttonCreate); // Botón azul profesional
        buttonCreate.addActionListener(this);
        panel.add(buttonCreate);

        buttonCancel = new JButton("CANCELAR");
        buttonCancel.setBounds(230, 220, 120, 35);
        styleButton(buttonCancel);
        buttonCancel.setBackground(new Color(127, 140, 141));
        buttonCancel.addActionListener(this);
        panel.add(buttonCancel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == buttonCreate) {
                handleCreate();
            } else if (e.getSource() == buttonCancel) {
                handleCancel();
            }
        } catch (Exception ex) {
            showError("Ocurrió un error al procesar la acción del botón" + ex.getMessage());
        }
    }

    private void handleCreate() {
        String name = textFieldName.getText().trim();
        String address = (String) comboBoxAddress.getSelectedItem();
        String spacesText = textFieldSpaces.getText().trim();

        validateInput(name, address, spacesText);
        createParkingLot(name, address, spacesText);
    }

    private void validateInput(String name, String address, String spacesText) {
        if (name.isEmpty()) {
            showWarning("Ingrese el nombre del parqueo");
            return;
        }

        if (address.equals("-- Seleccione --")) {
            showWarning("Seleccione una dirección válida");
            return;
        }

        if (spacesText.isEmpty()) {
            showWarning("Ingrese la cantidad de espacios");
            return;
        }
    }

    private void createParkingLot(String name, String address, String spacesText) {
        try {
            int numberOfSpaces = Integer.parseInt(spacesText);
            //Enviamos null o un arreglo vacío, el Controller debe generar los objetos finales
            parkingLotController.createParkingLot(name, address, numberOfSpaces);

            JOptionPane.showMessageDialog(null, "El parqueo se ha creado con éxito");
            clearFields();
            dispose();
        } catch (HeadlessException | IOException | NumberFormatException e) {
            showError("Error: " + e.getMessage());
        }
    }

    // Opciones predefinidas para las direcciones
    private final String[] addresses = {
        "-- Seleccione --",
        "San José",
        "Alajuela",
        "Cartago",
        "Heredia",
        "Guanacaste",
        "Puntarenas",
        "Limón"
    };

    private void clearFields() {
        textFieldName.setText("");
        textFieldSpaces.setText("");
    }

    private void handleCancel() {
        dispose();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }
}
