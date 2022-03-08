/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author LaboratorioU005_11
 */
import java.math.*;

public class Matrix {
    double[][] matrix = new double[3][3];
    double xp,yp,hp;
    
    Matrix(){
        // Matriz Identidad
        loadIdentity();
    }
    
    double[] pprima(double punto[]){
        double[] res = new double[3];
        
        xp = punto[0]*matrix[0][0]+punto[1]*matrix[0][1]+punto[2]*matrix[0][2];     // | x' | =  | A  B  C |   | x |
        yp = punto[0]*matrix[1][0]+punto[1]*matrix[1][1]+punto[2]*matrix[1][2];     // | y' | =  | D  E  F | * | y |
        hp = punto[0]*matrix[2][0]+punto[1]*matrix[2][1]+punto[2]*matrix[2][2];     // | h' | =  | G  H  I |   | z |
        
        res[0] = xp;
        res[1] = yp;
        res[2] = hp;
        
        return res;
    }
    
    double[][] producto(double matrizMul[][],double matriz[][]){
        double[][] resultante = new double[3][3];
        
        resultante[0][0] = matriz[0][0]*matrizMul[0][0] + matriz[1][0]*matrizMul[0][1] + matriz[2][0]*matrizMul[0][2];
        resultante[1][0] = matriz[0][0]*matrizMul[1][0] + matriz[1][0]*matrizMul[1][1] + matriz[2][0]*matrizMul[1][2];
        resultante[2][0] = matriz[0][0]*matrizMul[2][0] + matriz[1][0]*matrizMul[2][1] + matriz[2][0]*matrizMul[2][2];

        resultante[0][1] = matriz[0][1]*matrizMul[0][0] + matriz[1][1]*matrizMul[0][1] + matriz[2][1]*matrizMul[0][2];
        resultante[1][1] = matriz[0][1]*matrizMul[1][0] + matriz[1][1]*matrizMul[1][1] + matriz[2][1]*matrizMul[1][2];
        resultante[2][1] = matriz[0][1]*matrizMul[2][0] + matriz[1][1]*matrizMul[2][1] + matriz[2][1]*matrizMul[2][2];

        resultante[0][2] = matriz[0][2]*matrizMul[0][0] + matriz[1][2]*matrizMul[0][1] + matriz[2][2]*matrizMul[0][2];
        resultante[1][2] = matriz[0][2]*matrizMul[1][0] + matriz[1][2]*matrizMul[1][1] + matriz[2][2]*matrizMul[1][2];
        resultante[2][2] = matriz[0][2]*matrizMul[2][0] + matriz[1][2]*matrizMul[2][1] + matriz[2][2]*matrizMul[2][2];    
        
        return resultante;
    }
    
        
    void loadIdentity(){
                // Matriz Identidad
        matrix[0][0] = 1;
        matrix[0][1] = 0;
        matrix[0][2] = 0;
        matrix[1][0] = 0;
        matrix[1][1] = 1;
        matrix[1][2] = 0;
        matrix[2][0] = 0;
        matrix[2][1] = 0;
        matrix[2][2] = 1;
    }
    
    void escalar(int escala){
        // Matriz Identidad
        double[][] matrizEscala = new double[3][3];
        
        matrizEscala[0][0] = escala;
        matrizEscala[0][1] = 0;
        matrizEscala[0][2] = 0;
        matrizEscala[1][0] = 0;
        matrizEscala[1][1] = escala;
        matrizEscala[1][2] = 0;
        matrizEscala[2][0] = 0;
        matrizEscala[2][1] = 0;
        matrizEscala[2][2] = 1;
        
        matrix = producto(matrizEscala,matrix);
                
    }    
    
    void escalarXY(int escalaX, int escalaY){
        double[][] matrizEscala = new double[3][3];
        
        matrizEscala[0][0] = escalaX;
        matrizEscala[0][1] = 0;
        matrizEscala[0][2] = 0;
        matrizEscala[1][0] = 0;
        matrizEscala[1][1] = escalaY;
        matrizEscala[1][2] = 0;
        matrizEscala[2][0] = 0;
        matrizEscala[2][1] = 0;
        matrizEscala[2][2] = 1;
        
        matrix = producto(matrizEscala,matrix);

    }     
    
    
    void rotacion(double angulo){
        double[][] matrizRotacion = new double[3][3];
        matrizRotacion[0][0] = Math.cos(Math.toRadians(angulo)) ;    // cos +
        matrizRotacion[0][1] = Math.sin(Math.toRadians(angulo))*-1;  // -sen +
        matrizRotacion[0][2] = 0;    // 0
        matrizRotacion[1][0] = Math.sin(Math.toRadians(angulo));     // sen
        matrizRotacion[1][1] = Math.cos(Math.toRadians(angulo));     // cos
        matrizRotacion[1][2] = 0;
        matrizRotacion[2][0] = 0;
        matrizRotacion[2][1] = 0;
        matrizRotacion[2][2] = 1;
        
        matrix = producto(matrizRotacion,matrix);
    }     

    
    void traslacion(int tx, int ty){
        double[][] matrizTraslacion = new double[3][3];
        matrizTraslacion[0][0] = 1 ;    // cos +
        matrizTraslacion[0][1] = 0;    // -sen +
        matrizTraslacion[0][2] = tx;    // 0
        matrizTraslacion[1][0] = 0;
        matrizTraslacion[1][1] = 1;
        matrizTraslacion[1][2] = ty;
        matrizTraslacion[2][0] = 0;
        matrizTraslacion[2][1] = 0;
        matrizTraslacion[2][2] = 1;
        
        matrix = producto(matrizTraslacion,matrix);
    }         
    
}
