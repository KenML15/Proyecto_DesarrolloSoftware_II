/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.ParkingLotFileController;
import controller.VehicleTypeController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import model.entities.ParkingLot;
import model.entities.Space;
import model.entities.VehicleType;
import org.jdom2.JDOMException;

/**
 *
 * @author 50687
 */
public class SpaceConfigurationWindow extends BaseInternalFrame implements ActionListener {

    private JPanel mainPanel;
    private JScrollPane scrollPane;
    private JComboBox<VehicleType>[] typeComboBoxes;
    private JCheckBox[] disabledCheckBoxes;
    private JButton buttonSave, buttonCancel;

    private ParkingLot parkingLot;
    private ParkingLotFileController parkingLotController;
    private VehicleTypeController vehicleTypeController;
    private ArrayList<VehicleType> vehicleTypes;

    public SpaceConfigurationWindow(ParkingLot parkingLot, ParkingLotFileController controller) throws IOException, JDOMException {
        super("CONFIGURAR ESPACIOS: " + parkingLot.getName());
        this.parkingLot = parkingLot;
        this.parkingLotController = controller;
        this.vehicleTypeController = new VehicleTypeController();

        loadVehicleTypes();
        setupWindow();
    }

    private void loadVehicleTypes() {
        try {
            this.vehicleTypes = new ArrayList<>();
            vehicleTypes = vehicleTypeController.getAllVehicleTypes();
        } catch (IOException e) {
            showError("No se puede acceder al archivo de los tipos de vehículo: " + e.getMessage());
        }
    }

    private void setupWindow() {
        setWindowProperties();
        createMainPanel();
        createControlButtons();
        loadCurrentConfiguration();
    }

    private void setWindowProperties() {
        setVisible(true);
        setSize(600, 500);
        setLocation(200, 100);
        setResizable(true);
    }

    private void createMainPanel() {
        mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        int numSpaces = parkingLot.getNumberOfSpaces();

        mainPanel.setLayout(new GridLayout(numSpaces + 1, 3, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        addHeaders();
        createControls(numSpaces);

        scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void addHeaders() {
        String[] titles = {"N° Espacio", "Tipo de Vehículo", "¿Discapacidad?"};
        for (String title : titles) {
            JLabel header = new JLabel(title);
            header.setFont(labelFont);
            header.setForeground(primaryColor);
            header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, primaryColor));
            mainPanel.add(header);
        }
    }

    private void createControls(int numSpaces) {
        typeComboBoxes = new JComboBox[numSpaces];
        disabledCheckBoxes = new JCheckBox[numSpaces];

        for (int i = 0; i < numSpaces; i++) {
            // Número de espacio
            JLabel lblNum = new JLabel("Espacio #" + (i + 1));
            lblNum.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            mainPanel.add(lblNum);

            // Combo de Tipo
            typeComboBoxes[i] = createTypeComboBox();
            mainPanel.add(typeComboBoxes[i]);

            // Checkbox
            disabledCheckBoxes[i] = new JCheckBox();
            disabledCheckBoxes[i].setBackground(Color.WHITE);
            disabledCheckBoxes[i].setHorizontalAlignment(SwingConstants.CENTER);
            mainPanel.add(disabledCheckBoxes[i]);
        }
    }

    private void createControlButtons() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttonPanel.setBackground(backgroundColor);

        buttonSave = new JButton("GUARDAR CONFIGURACIÓN");
        styleButton(buttonSave);
        buttonSave.addActionListener(this);

        buttonCancel = new JButton("CANCELAR");
        styleButton(buttonCancel);
        buttonCancel.setBackground(new Color(127, 140, 141));
        buttonCancel.addActionListener(this);

        buttonPanel.add(buttonSave);
        buttonPanel.add(buttonCancel);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JComboBox<VehicleType> createTypeComboBox() {
        JComboBox<VehicleType> comboBox = new JComboBox<>();

        for (VehicleType type : vehicleTypes) {
            comboBox.addItem(type);
        }

        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list,
                    Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof VehicleType) {
                    VehicleType vehicleType = (VehicleType) value;
                    setText(vehicleType.getDescription());
                }
                return this;
            }
        });

        return comboBox;
    }

    private void loadCurrentConfiguration() {
        Space[] spaces = parkingLot.getSpaces();

        for (int i = 0; i < spaces.length; i++) {
            if (spaces[i] != null) {
                disabledCheckBoxes[i].setSelected(spaces[i].isDisabilityAdaptation());
                selectVehicleType(i, spaces[i].getVehicleType());
            }
        }
    }

    private void selectVehicleType(int index, VehicleType vehicleType) {
        if (vehicleType == null) {
            return;
        }

        for (int i = 0; i < typeComboBoxes[index].getItemCount(); i++) {
            if (typeComboBoxes[index].getItemAt(i).getId() == vehicleType.getId()) {
                typeComboBoxes[index].setSelectedIndex(i);
                break;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == buttonSave) {
                saveConfiguration();
            } else if (e.getSource() == buttonCancel) {
                cancelConfiguration();
            }
        } catch (Exception ex) {
            showError("Ocurrió un error al provcesar la acción del botón" + ex.getMessage());
        }
    }

    private void saveConfiguration() {
        try {
            if (parkingLot.getVehicles() != null && !parkingLot.getVehicles().isEmpty()) {
                showError("No se puede modificar la configuración: El parqueo tiene vehículos estacionados en este momento.");
                return;
            }

            Space[] newSpaces = createSpacesFromForm();

            if (countDisabledSpaces(newSpaces) == 0) {
                int result = JOptionPane.showConfirmDialog(this,
                        "ADVERTENCIA: No ha asignado espacios para personas con discapacidad.\n"
                        + "¿Desea guardar la configuración de todos modos?",
                        "Validación de Espacios", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                if (result != JOptionPane.YES_OPTION) {
                    return;
                }
            }

            String result = parkingLotController.updateParkingLotSpaces(parkingLot.getId(), newSpaces);
            showSuccess(result);
            dispose();

        } catch (IOException ex) {

            showError("Error al guardar en el archivo: " + ex.getMessage());
        } catch (Exception ex) {
            showError("Ocurrió un error inesperado: " + ex.getMessage());
        }
    }

    private Space[] createSpacesFromForm() {
        Space[] newSpaces = new Space[parkingLot.getNumberOfSpaces()];

        for (int i = 0; i < newSpaces.length; i++) {
            newSpaces[i] = createSpace(i);
        }

        return newSpaces;
    }

    private Space createSpace(int index) {
        Space space = new Space();
        int spaceId = (parkingLot.getId() * 100) + (index + 1);
        space.setId(spaceId);
        space.setVehicleType((VehicleType) typeComboBoxes[index].getSelectedItem());
        space.setDisabilityAdaptation(disabledCheckBoxes[index].isSelected());
        space.setSpaceTaken(false);
        return space;
    }

    private void validateConfiguration(Space[] spaces) {
        if (countDisabledSpaces(spaces) == 0) {
            askForConfirmation();
        }
    }

    private int countDisabledSpaces(Space[] spaces) {
        int count = 0;
        for (Space space : spaces) {
            if (space.isDisabilityAdaptation()) {
                count++;
            }
        }
        return count;
    }

    private void askForConfirmation() {
        int result = JOptionPane.showConfirmDialog(this,
                "No hay espacios para discapacidad. ¿Continuar?",
                "Confirmar", JOptionPane.YES_NO_OPTION);

        if (result != JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this, "Operación cancelada", "Información", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void cancelConfiguration() {
        int result = JOptionPane.showConfirmDialog(this,
                "¿Cancelar configuración?",
                "Confirmar", JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            dispose();
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
}
