
import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author gmendez
 */
public class Raster {
 
    int width;
    int height;
    int pixel[];
    
    public Raster(int w, int h) {
        width = w;
        height = h;
        pixel = new int[w*h];
    }
    
    /**
     *  Este constructor crea un Raster inicializado con 
     *  el contenido de una imagen
    */
    public Raster(Image img){

      try {
          
           PixelGrabber grabber = new PixelGrabber(img, 0, 0, -1, -1, true);

           if (grabber.grabPixels()) {
               width = grabber.getWidth();
               height = grabber.getHeight();
               pixel = (int []) grabber.getPixels();
            }

       } catch (InterruptedException e) {

       }

    }    
    
    /* Retorna el número de pixeles   */
    public final int size( ){  
          return pixel.length;
    }	

    /* Rellena el objeto Raster con un color solido */
    public final void fill(Color c){
        int s = size();
        int rgb = c.getRGB();
        for (int i = 0; i < s; i++)
             pixel[i] = rgb;
    }

    /* Convierte la imagen rasterizada a un objeto Image */
    public final Image toImage(Component root){
          return root.createImage(new MemoryImageSource(width, height, pixel, 0, width));
    }    
    
    /*  Obtiene un color desde un Raster */
    public final Color getColor(int x, int y){
           return new Color(pixel[y*width+x]);
    }

    /*  Establece un pixel en un valor dado  */
    public final boolean setPixel(int pix, int x, int y){
          pixel[y*width+x] = pix;
          return true;
    }

    /* Establece un pixel en un color dado */

    public final boolean setColor(Color c, int x, int y){
          pixel[y*width+x] = c.getRGB();
          return true;
    }        
}
