
public class Transformaciones2D {
    public static void trasladarTriangulo(TrianguloR triangulo, int dx, int dy) {
        for (Vertex2D v : triangulo.v) {
            v.x += dx;
            v.y += dy;
        }
    }

    // Agrega métodos para rotar y escalar el triángulo
}

