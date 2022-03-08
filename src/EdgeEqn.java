/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author macpro1
 */
class EdgeEqn  {
    public final static int FRACBITS = 12;
    public int A, B, C;
    public int flag;
    
    public EdgeEqn(Vertex2D v0, Vertex2D v1){
        double a = v0.y - v1.y;   
        double b = v1.x - v0.x;  
        double c = -0.5f*(a*(v0.x + v1.x) + b*(v0.y + v1.y)); 
       
        A = (int) (a * (1<<FRACBITS));
        B = (int) (b * (1<<FRACBITS));
        C = (int) (c * (1<<FRACBITS));
        
        flag = 0;
        if (A >= 0) flag += 8;
        if (B >= 0) flag += 1;
    }
    
    public void flip(){
        A = -A;
        B = -B;
        C = -C;
    }
    
    public int evaluate(int x, int y){
        return (A*x + B*y + C);
    }
}
