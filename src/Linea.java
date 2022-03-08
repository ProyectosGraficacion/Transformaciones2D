
import java.awt.Color;
import java.awt.Point;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author LaboratorioU005_11
 */
public class Linea extends Figura {
    Point punto1;
    Point punto2;

    Linea(Point _p1, Point _p2, Color _color) {
        punto1 = new Point(_p1.x,_p1.y);
        punto2 = new Point(_p2.x,_p2.y);
        color  = _color;
    }    
}
