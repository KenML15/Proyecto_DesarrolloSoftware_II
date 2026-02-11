/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.ParkingLotFileController;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import model.entities.Space;

/**
 *
 * @author 50687
 */
public class ParkingLotWindow extends JInternalFrame implements ActionListener{
    
    private JPanel panel;
    private JLabel labelName, labelSpaces;
    private JTextField textFieldName, textFieldSpaces;
    private JButton buttonCreate, buttonCancel;
    
    private ParkingLotFileController controller;

    public ParkingLotWindow(ParkingLotFileController controller) {
        super("Crear Parqueo", false, true, false, true);
        this.controller = controller;
        setupWindow();
    }

    private void setupWindow() {
        setWindowProperties();
        createPanel();
        createComponents();
    }

    private void setWindowProperties() {
        setVisible(true);
        setSize(400, 300);
        setLocation(250, 150);
        setResizable(false);
    }

    private void createPanel() {
        panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);
        add(panel);
    }

    private void createComponents() {
        createNameField();
        createSpacesField();
        createButtons();
    }

    private void createNameField() {
        labelName = new JLabel("Nombre:");
        labelName.setBounds(50, 30, 100, 25);
        panel.add(labelName);

        textFieldName = new JTextField();
        textFieldName.setBounds(200, 30, 150, 25);
        panel.add(textFieldName);
    }

    private void createSpacesField() {
        labelSpaces = new JLabel("Espacios totales:");
        labelSpaces.setBounds(50, 80, 150, 25);
        panel.add(labelSpaces);

        textFieldSpaces = new JTextField();
        textFieldSpaces.setBounds(200, 80, 150, 25);
        panel.add(textFieldSpaces);
    }

    private void createButtons() {
        buttonCreate = new JButton("Crear");
        buttonCreate.setBounds(100, 180, 100, 30);
        buttonCreate.addActionListener(this);
        panel.add(buttonCreate);

        buttonCancel = new JButton("Cancelar");
        buttonCancel.setBounds(220, 180, 100, 30);
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
        String spacesText = textFieldSpaces.getText().trim();
        
        validateInput(name, spacesText);
        createParkingLot(name, spacesText);
    }

    private void validateInput(String name, String spacesText) {
        if (name.isEmpty() || spacesText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
        
        int spaces = parseSpaces(spacesText);
        validateSpacesCount(spaces);
    }

    private int parseSpaces(String spacesText) {
        try {
            return Integer.parseInt(spacesText);
        } catch (NumberFormatException e) {
            showError("Número inválido" + e.getMessage());
        }
        return 0;
    }

    private void validateSpacesCount(int spaces) {
        if (spaces <= 0) {
            JOptionPane.showMessageDialog(this, "El parqueo debe tener mínimo un espacio", "Advertencia", JOptionPane.WARNING_MESSAGE);;
        }
        if (spaces > 100) {
            JOptionPane.showMessageDialog(this, "El parqueo debe tener máximo 100 espacios", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void createParkingLot(String name, String spacesText) {
        try {
            int numberOfSpaces = Integer.parseInt(spacesText);
            Space[] spaces = createSpaces(numberOfSpaces);

            controller.registerParkingLot(name, spaces);

            JOptionPane.showMessageDialog(null, "El parqueo se ha creado con éxito");
            clearFields();
            dispose();
        } catch (NumberFormatException e) {
            showError("Cantidad de espacios inválida");
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());

        } catch (IOException e) {
            showError("Error al guardar el parqueo" + e.getMessage());
        }
    }

    private Space[] createSpaces(int numberOfSpaces) {
        Space[] spaces = new Space[numberOfSpaces];
        
        for (int i = 0; i < numberOfSpaces; i++) {
            spaces[i] = createSpace(i + 1);
        }
        
        return spaces;
    }

    private Space createSpace(int id) {
        Space space = new Space();
        space.setId(id);
        space.setDisabilityAdaptation(false);
        space.setSpaceTaken(false);
        return space;
    }

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
}
