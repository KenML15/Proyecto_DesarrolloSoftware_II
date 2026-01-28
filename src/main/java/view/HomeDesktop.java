package GUI;

import java.awt.Dimension;
import javax.swing.ImageIcon;
import java.awt.Graphics;
import javax.swing.JDesktopPane;

public class HomeDesktop extends JDesktopPane {
    private ImageIcon backgroundImagen;

    public HomeDesktop() {

        this.setSize(800, 700);
    }

    @Override
    public void paintComponent(Graphics g) {

        Dimension tamano = getSize();
        backgroundImagen = new ImageIcon("background_program.jpg");
        g.drawImage(backgroundImagen.getImage(), 0, 0, tamano.width, tamano.height, null);
        setOpaque(false);
        super.paintComponent(g);

    }
}
