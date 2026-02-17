/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicInternalFrameUI;

/**
 *
 * @author user
 */
public abstract class BaseInternalFrame extends JInternalFrame {
    
    // Paleta de colores consistente con el Menu
    protected Color primaryColor = new Color(44, 62, 80);      // Azul oscuro
    protected Color accentColor = new Color(52, 73, 94);       // Azul hover
    protected Color backgroundColor = new Color(236, 240, 241); // Gris claro
    protected Color textColor = new Color(44, 62, 80);
    protected Color fieldBorderColor = new Color(210, 210, 210);
    
    protected Font titleFont = new Font("Segoe UI", Font.BOLD, 18);
    protected Font labelFont = new Font("Segoe UI", Font.BOLD, 13); // Un poco más pequeña y negrita para labels

    public BaseInternalFrame(String title) {
        super(title, true, true, true, true);
        setupBaseDesign();
    }

    private void setupBaseDesign() {
        getContentPane().setBackground(backgroundColor);
        // Borde exterior de la ventana
        setBorder(BorderFactory.createLineBorder(primaryColor, 2));
    }

    /**
     * Estiliza los campos de texto con bordes redondeados y padding interno.
     */
    protected void styleTextField(JTextField field) {
        field.setPreferredSize(new Dimension(250, 35));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(primaryColor);
        field.setBackground(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(fieldBorderColor, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }

    /**
     * Estiliza los botones con el look moderno del sidebar.
     */
 protected void styleButton(JButton btn) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setBackground(primaryColor);
        
        // --- FUERZA EL COLOR ---
        btn.setOpaque(true);
        btn.setContentAreaFilled(true); 
        btn.setBorderPainted(false);
        // -----------------------
        
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addHoverEffect(btn);
    }

    protected void addHoverEffect(JButton btn) {
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (btn.isEnabled()) btn.setBackground(accentColor);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                // Mantener el rojo si es cancelar, de lo contrario volver al azul
                if (btn.getText().equalsIgnoreCase("CANCELAR")) {
                    btn.setBackground(new Color(192, 57, 43));
                } else {
                    btn.setBackground(primaryColor);
                }
            }
        });
    }

    /**
     * Estiliza las tablas para que parezcan hojas de cálculo modernas.
     */
 protected void styleTable(JTable table) {
        // Estilo del Encabezado (La parte de arriba)
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setOpaque(false);
        table.getTableHeader().setBackground(primaryColor);
        table.getTableHeader().setForeground(Color.WHITE); // Texto blanco
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));
        
        // Estilo de las Celdas
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setGridColor(new Color(230, 230, 230));
        table.setSelectionBackground(new Color(52, 152, 219));
        table.setSelectionForeground(Color.WHITE);
        
        // Esto quita el borde por defecto que a veces causa ruido visual
        table.setShowVerticalLines(false);
    }
 
 
}