package GUI;

import java.awt.Dimension;
import javax.swing.ImageIcon;
import java.awt.Graphics;
import javax.swing.JDesktopPane;

public class HomeDesktop extends JDesktopPane {

    // Declara variables
    private ImageIcon backgroundImagen;

    public HomeDesktop() {

        this.setSize(800, 700);// tamaño de la backgroundImagen

    }//fin del constructor

    @Override
    public void paintComponent(Graphics g) {

        Dimension tamano = getSize();
        backgroundImagen = new ImageIcon("background_program.jpg");// La ubicación del archivo que se utiliza como backgroundImagen de fondo
        g.drawImage(backgroundImagen.getImage(), 0, 0, tamano.width, tamano.height, null);
        setOpaque(false);// Esto hace que la backgroundImagen sea visible
        super.paintComponent(g);

    }//Fin del método paintComponent
}//Fin de la clase HomeDesktop
