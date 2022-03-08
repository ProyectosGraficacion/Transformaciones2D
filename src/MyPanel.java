
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author LaboratorioU005_11
 */
class MyPanel extends JPanel {
    Raster raster;
    Dimension prefSize;
    public MyPanel() {
        setBorder(BorderFactory.createLineBorder(Color.black));
    }       

    public MyPanel(Raster _raster) {
        prefSize = new Dimension(_raster.width,_raster.height);
        this.setSize(prefSize);
        this.raster = _raster;
        setBorder(BorderFactory.createLineBorder(Color.black));
    }

    public Dimension getPreferredSize() {
        return prefSize;
    }
    
    public void setRaster(Raster _raster){
        this.raster = _raster;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);       
        Image output =  raster.toImage(this);
        g.drawImage(output, 0, 0, this);
    }  
}