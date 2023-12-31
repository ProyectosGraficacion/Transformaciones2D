import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JColorChooser;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

public class Pizarra2 extends javax.swing.JFrame {

    static final int LINEA     = 0;
    static final int TRIANGULO = 1;

    static final int ANCHO     = 640;
    static final int ALTO      = 480;

    Raster  raster;

    Point   p1, p2, p3;
    boolean bP1=false, bP2=false, bP3=false;
    int     figura = LINEA;


    JPanel      panelRaster;
    JPanel      panelControles;
    JPanel      panelFiguras;
    JScrollPane scrollFiguras;

    JList             listFiguras;
    DefaultListModel  listModel;
    ArrayList<Figura> aFiguras;

    JButton       btnColor;
    JToggleButton rbLinea;
    JToggleButton rbTriang;

    ButtonGroup   bg;


    Color         color;
    JColorChooser colorChooser;
    JButton       btnGuardarRast;
    JButton       btnGuardarVect;

    public Pizarra2() {



        p1 = new Point();
        p2 = new Point();
        p3 = new Point();

        bP1 = false; bP2 = false; bP3 = false;

        raster = new Raster(ANCHO,ALTO);

        panelRaster = new MyPanel(raster);

        panelControles = new JPanel();
        panelControles.setLayout(new BoxLayout(panelControles,BoxLayout.Y_AXIS));

        this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        this.setLayout(new BorderLayout());

        color = Color.black;

        btnGuardarRast = new JButton("Guardar");
        btnColor       = new JButton("Color");

        btnColor.setBorderPainted(false);
        btnColor.setFocusPainted(false);

        btnColor.setBackground(color);
        btnColor.setForeground(color);

        rbLinea  = new JToggleButton("Linea");
        rbTriang = new JToggleButton("Triangulo");

        rbTriang.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Establece el foco en el panelRaster al hacer clic en el botón Triángulo
                panelRaster.requestFocusInWindow();
            }
        });
        bg = new ButtonGroup();

        rbLinea.setSelected(true);
        bg.add(rbLinea);
        bg.add(rbTriang);

        this.panelRaster.setBackground(Color.white);
        this.add(panelRaster,BorderLayout.CENTER);
        this.panelRaster.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                System.out.println("panelRaster ha obtenido el foco.");
            }

            @Override
            public void focusLost(FocusEvent e) {
                System.out.println("panelRaster ha perdido el foco.");
                panelRaster.requestFocusInWindow(); // Recuperar el foco
            }
        });

        this.panelControles.add(rbLinea);
        this.panelControles.add(rbTriang);
        this.panelControles.add(new JSeparator());
        this.panelControles.add(btnColor);

        this.panelControles.add(btnGuardarRast);

        // Ahora el pane de figuras

        btnGuardarVect = new JButton("Guardar");

        scrollFiguras = new JScrollPane();
        panelFiguras  = new JPanel();
        panelFiguras.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                panelRaster.requestFocusInWindow(); // Recuperar el foco
            }
        });
        panelFiguras.setLayout(new BoxLayout(panelFiguras,BoxLayout.Y_AXIS));

        listFiguras = new JList();
        listFiguras.setModel(new DefaultListModel());
        listModel = (DefaultListModel) listFiguras.getModel();

        scrollFiguras.setViewportView(listFiguras);

        scrollFiguras.setPreferredSize(new Dimension(50,100));
        panelFiguras.add(scrollFiguras);
        panelFiguras.add(new JSeparator());
        panelFiguras.add(btnGuardarVect);

        aFiguras = new ArrayList<Figura>();

        this.add(panelFiguras,BorderLayout.EAST);
        this.add(panelControles,BorderLayout.WEST);

        this.panelRaster.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent evt) {
                jPanel1MouseClicked(evt);
            }
        });


        this.panelRaster.addKeyListener(new KeyAdapter(){
            @Override
            public void keyReleased(KeyEvent ke){

                jPanel1KeyReleased(ke);

            }
        });

        this.btnColor.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                color = JColorChooser.showDialog(null,"Seleccione un color",color);
                btnColor.setBackground(color);
            }
        });

        this.btnGuardarRast.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarImagen();
            }
        });

        this.btnGuardarVect.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarVectores();
            }
        });


        this.setVisible(true);
        this.pack();


        this.panelRaster.setFocusable(true);
        this.panelRaster.requestFocusInWindow();

        this.panelRaster.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent ke) {
                // Verifica si hay una figura seleccionada en la lista de figuras
                int selectedIndex = listFiguras.getSelectedIndex();
                if (selectedIndex >= 0 && selectedIndex < aFiguras.size()) {
                    Figura figuraSeleccionada = aFiguras.get(selectedIndex);
                    if (figuraSeleccionada instanceof TrianguloR) {
                        TrianguloR triangulo = (TrianguloR) figuraSeleccionada;
                        int tx = 0;
                        int ty = 0;
                        double tam = 1;
                        double rotar = 0;

                        // aqui se calcula el centro de rotacion
                        double centroX = (triangulo.v[0].x + triangulo.v[1].x + triangulo.v[2].x) / 3.0;
                        double centroY = (triangulo.v[0].y + triangulo.v[1].y + triangulo.v[2].y) / 3.0;

                        switch (ke.getKeyChar()) {
                            case 'w':
                                System.out.println("Tecla presionada: w");
                                ty = -5; // Mover hacia arriba
                                break;
                            case 's':
                                ty = 5; // Mover hacia abajo
                                break;
                            case 'a':
                                tx = -5; // Mover hacia la izquierda
                                break;
                            case 'd':
                                tx = 5; // Mover hacia la derecha
                                break;
                            case '+':
                                tam = 1.1; // Mas grande
                                break;
                            case '-':
                                tam = 0.9; // mas pequeño
                                break;
                            case 'e':
                                rotar = 2; // horario
                                break;
                            case 'r':
                                rotar = -2; // antihorario
                                break;
                        }


                        Matrix m = new Matrix();
                        m.traslacion(tx, ty);
                        m.rotacion(rotar, centroX, centroY);
                        m.escalar(tam, centroX, centroY);
                        // Aplicar la traslación a los vértices del triángulo
                        for (int i = 0; i < 3; i++) {
                            double[] punto = { triangulo.v[i].x, triangulo.v[i].y, 1 };
                            double[] puntoTraslado = m.pprima(punto);
                            triangulo.v[i].x = (int) puntoTraslado[0];
                            triangulo.v[i].y = (int) puntoTraslado[1];
                        }
                        // Borra el contenido del raster
                        clear();



                        // Dibuja todas las figuras, incluida la figura seleccionada que se ha movido
                        for (Figura figura : aFiguras) {
                            figura.dibujar(raster);
                        }

                        panelRaster.repaint();
                        selectedIndex = listFiguras.getSelectedIndex();
                        if (selectedIndex >= 0) {
                            listFiguras.setSelectedIndex(selectedIndex);
                            panelRaster.requestFocusInWindow();
                        }
                    }
                }
            }

            @Override
            public void keyPressed(KeyEvent ke) {
                // Permanece vacío
            }

            @Override
            public void keyReleased(KeyEvent ke) {
                // Permanece vacío
            }
        });
        this.setFocusable(true); // Asegura que el JFrame tenga el foco para recibir eventos de teclado
        this.setVisible(true);

        this.panelRaster.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                System.out.println("panelRaster ha obtenido el foco.");
            }

            @Override
            public void focusLost(FocusEvent e) {
                System.out.println("panelRaster ha perdido el foco.");
            }
        });

    }




    public static BufferedImage toBufferedImage(Image img){
        if (img instanceof BufferedImage)
        {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }


    public static Color hex2Rgb(String colorStr) {
        return new Color(
                Integer.valueOf( colorStr.substring( 1, 3 ), 16 ),
                Integer.valueOf( colorStr.substring( 3, 5 ), 16 ),
                Integer.valueOf( colorStr.substring( 5, 7 ), 16 ) );
    }

    public void guardarImagen(){

        BufferedImage img = toBufferedImage(raster.toImage(this));
        try {
            File outputfile = new File("saved.png");
            ImageIO.write(img, "png", outputfile);
        } catch (IOException e) {

        }
    }

    public void guardarVectores() {
        FileWriter fw = null;
        String linea="";

        try {
            fw = new FileWriter("vectores.txt");
            for (int i=0;i<aFiguras.size();i++){
                Figura f = aFiguras.get(i);

                if (f instanceof Linea){
                    Linea l = (Linea)f;

                    linea=String.format("L,%.0f,%.0f,%.0f,%.0f,%x\n",l.punto1.getX(),l.punto1.getY(),
                            l.punto2.getX(),l.punto2.getY(),
                            l.color.getRGB());
                }

                if (f instanceof TrianguloR){
                    TrianguloR t = (TrianguloR)f;
                    linea=String.format("T,%d,%d,%d,%d,%d,%d,%x\n",t.v[0].x,t.v[0].y,
                            t.v[1].x,t.v[1].y,
                            t.v[2].x,t.v[2].y,
                            t.color_int);
                }

                fw.write(linea);
            }
        } catch (IOException ex) {
            Logger.getLogger(Pizarra2.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(Pizarra2.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void clear() {
        int s = raster.size();
        for (int i = 0; i < s; i++) {
            raster.pixel[i] = 0xFFFFFFFF; // Establece todos los píxeles en blanco
        }
        panelRaster.repaint();
    }


    public void lineFast(int x0, int y0, int x1, int y1, Color color) {
        int pix = color.getRGB();
        int dy = y1 - y0;  int dx = x1 - x0;  int stepx, stepy;
        if (dy < 0) { dy = -dy;  stepy = -raster.width; } else { stepy = raster.width; }
        if (dx < 0) { dx = -dx;  stepx = -1; } else { stepx = 1; }
        dy <<= 1;   dx <<= 1;
        y0 *= raster.width;  y1 *= raster.width;   raster.pixel[x0+y0] = pix;
        if (dx > dy) {
            int fraction = dy - (dx >> 1);
            while (x0 != x1) {
                if (fraction >= 0) {
                    y0 += stepy; fraction -= dx;
                }
                x0 += stepx;  fraction += dy;
                raster.pixel[x0+y0] = pix;
            }
        } else {
            int fraction = dx - (dy >> 1);
            while (y0 != y1) {
                if (fraction >= 0) {
                    x0 += stepx; fraction -= dy;
                }
                y0 += stepy; fraction += dx;
                raster.pixel[x0+y0] = pix;
            }
        }
    }

    private void dibujarLinea(Point _p1, Point _p2, Color color) {
        long inicio=0, fin=0;
        //inicio = System.nanoTime();
        // lineaMejorada(_p1.x,_p1.y,_p2.x,_p2.y,color);
        //fin    = System.nanoTime();

        //System.out.printf("Tiempo transcurrido, simple: %d\n",(fin-inicio));

        //inicio = System.nanoTime();
        lineFast(_p1.x,_p1.y,_p2.x,_p2.y,color);
        //fin    = System.nanoTime();
        panelRaster.repaint();
        //System.out.printf("Tiempo transcurrido, fast  : %d\n",(fin-inicio));
    }

    private int contadorTriangulos = 1;
    private void dibujarTriangulo(Point p1, Point p2, Point p3, Color c){
        // TODO add your handling code here:
        Vertex2D v1 = new Vertex2D(p1.x,p1.y,c.getRGB());
        Vertex2D v2 = new Vertex2D(p2.x,p2.y,c.getRGB());
        Vertex2D v3 = new Vertex2D(p3.x,p3.y,c.getRGB());

        TrianguloR tri = new TrianguloR(v1,v2,v3,c);

        tri.dibujar(raster);

        aFiguras.add(tri);

        listModel.addElement("Triangulo " + contadorTriangulos); // Etiqueta con un número
        contadorTriangulos++;
        panelRaster.repaint();

    }

    private void jPanel1MouseClicked(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        figura = this.rbLinea.isSelected()?LINEA:TRIANGULO;

        if (figura==TRIANGULO && bP1 && bP2 && !bP3){
            p3.x=evt.getX();
            p3.y=evt.getY();
            bP3 = true;
            System.out.println("Tercer punto");

        }

        if (bP1 && !bP2){
            p2.x=evt.getX();
            p2.y=evt.getY();
            bP2 = true;

            dibujarLinea(p2,p2,color);

            if(figura == LINEA){

                Linea l = new Linea(p1,p2, color);

                String linea=String.format("L,%.0f,%.0f,%.0f,%.0f,%x\n",l.punto1.getX(),l.punto1.getY(),
                        l.punto2.getX(),l.punto2.getY(),
                        l.color.getRGB());


                aFiguras.add(l);
                listModel.addElement(linea);
            }
        }

        if (!bP1){
            p1.x=evt.getX();
            p1.y=evt.getY();
            bP1 = true;
            dibujarLinea(p1,p1,color);
        }
        if(figura==LINEA && bP1 && bP2){
            dibujarLinea(p1,p2,color);
            bP1=false;bP2=false;bP3=false;
        }

        if(figura==TRIANGULO && bP1 &&bP2 &&bP3){
            System.out.println("Dibujando triangulo");
            dibujarTriangulo(p1,p2,p3,color);
            bP1=false;bP2=false;bP3=false;
        }
    }

    public void jPanel1KeyReleased(KeyEvent ke) {

        if(ke.getKeyCode()==KeyEvent.VK_T){
            this.figura = 1;
        }

        if(ke.getKeyCode()==KeyEvent.VK_L){
            this.figura = 0;
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Pizarra.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Pizarra.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Pizarra.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Pizarra.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>


        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Pizarra2 pizarra = new Pizarra2();
                pizarra.setVisible(true);

            }
    });
}

}