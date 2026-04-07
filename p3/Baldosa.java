/**
 * Representa una baldosa con ancho y largo.
 * Puede colocarse en horizontal o en vertical.
 */
public class Baldosa {
    private int ancho;
    private int largo;

    public Baldosa(int ancho, int largo) {
        if (ancho <= 0 || largo <= 0)
            throw new IllegalArgumentException("Las dimensiones deben ser enteras positivas.");
        this.ancho = ancho;
        this.largo = largo;
    }

    public int getAncho() { return ancho; }
    public int getLargo() { return largo; }

    /** Área de la baldosa */
    public int area() { return ancho * largo; }

    /**
     * Devuelve una nueva baldosa girada 90 grados (intercambia ancho y largo).
     * Solo tiene sentido si la baldosa no es cuadrada.
     */
    public Baldosa girar() {
        return new Baldosa(largo, ancho);
    }

    /** True si la baldosa es cuadrada (girarla no cambia nada) */
    public boolean esCuadrada() { return ancho == largo; }

    @Override
    public String toString() {
        return "Baldosa(" + ancho + "x" + largo + ", área=" + area() + ")";
    }
}
