/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.ParkingLotFileController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import model.entities.ParkingLot;
import model.entities.Space;

/**
 *
 * @author user
 */
public class ParkingVisualWindow extends BaseInternalFrame {

    private JPanel gridPanel;
    private ParkingLotFileController controller;
    private JComboBox<ParkingLot> comboLots;

    public ParkingVisualWindow(ParkingLotFileController controller) {
        super("MAPA OCUPACIÓN DE PARQUEOS");
        this.controller = controller;
        this.setClosable(true);
        this.setResizable(true);
        this.setMaximizable(true);
        SwingUtilities.invokeLater(() -> centerInDesktop());

        setLayout(new BorderLayout());

        gridPanel = new JPanel();
        add(new JScrollPane(gridPanel), BorderLayout.CENTER);

        setupSelectionPanel();

        setSize(800, 600);
        setVisible(true);
    }

    private void setupSelectionPanel() {
        JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        northPanel.setBackground(Color.WHITE);

        try {
            ArrayList<ParkingLot> lots = controller.getAllParkingLots();

            //Si no hay parqueos, informamos al usuario sin lanzar error
            if (lots == null || lots.isEmpty()) {
                northPanel.add(new JLabel("No hay parqueos registrados."));
            } else {
                comboLots = new JComboBox<>(lots.toArray(new ParkingLot[0]));

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

                comboLots.addActionListener(e -> {
                    ParkingLot selected = (ParkingLot) comboLots.getSelectedItem();
                    if (selected != null) {
                        renderMap(selected);
                    }
                });

                northPanel.add(new JLabel("Seleccione Parqueo: "));
                northPanel.add(comboLots);

                //Cargar el primero por defecto
                renderMap(lots.get(0));
            }

        } catch (IOException e) {
            // Solo mostramos el error si el fallo es crítico (ej. archivo no existe)
            JLabel lblError = new JLabel("Error de lectura: Datos inconsistentes en archivos");
            lblError.setForeground(Color.RED);
            northPanel.add(lblError);
            e.printStackTrace();
        }

        add(northPanel, BorderLayout.NORTH);
    }

    private void renderMap(ParkingLot lot) {
        gridPanel.removeAll();
        //Usamos un GridLayout dinámico (columnas fijas, filas según espacios)
        gridPanel.setLayout(new GridLayout(0, 5, 10, 10));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        gridPanel.setBackground(new Color(240, 240, 240));

        for (Space s : lot.getSpaces()) {
            JPanel spaceCard = new JPanel(new BorderLayout());
            spaceCard.setPreferredSize(new Dimension(100, 80));

            //Lógica de colores según Spaces.txt
            if (s.isSpaceTaken()) {
                spaceCard.setBackground(new Color(231, 76, 60)); //Rojo: Ocupado
            } else {
                spaceCard.setBackground(new Color(46, 204, 113)); //Verde: Libre
            }

            JLabel lblId = new JLabel("#" + s.getId(), SwingConstants.CENTER);
            lblId.setForeground(Color.WHITE);
            lblId.setFont(new Font("Segoe UI", Font.BOLD, 14));

            String type = (s.getVehicleType() != null) ? s.getVehicleType().getDescription() : "Cualquiera";
            JLabel lblType = new JLabel(type, SwingConstants.CENTER);
            lblType.setForeground(new Color(255, 255, 255, 200));
            lblType.setFont(new Font("Segoe UI", Font.PLAIN, 10));

            spaceCard.add(lblId, BorderLayout.CENTER);
            spaceCard.add(lblType, BorderLayout.SOUTH);
            spaceCard.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

            gridPanel.add(spaceCard);
        }

        gridPanel.revalidate();
        gridPanel.repaint();
    }
}
