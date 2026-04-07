/**
 * Representa el local rectangular que se debe enlosar.
 */
public class Local {
    private int ancho;
    private int largo;

    public Local(int ancho, int largo) {
        if (ancho <= 0 || largo <= 0)
            throw new IllegalArgumentException("Las dimensiones del local deben ser enteras positivas.");
        this.ancho = ancho;
        this.largo = largo;
    }

    public int getAncho() { return ancho; }
    public int getLargo() { return largo; }

    /** Área total del local */
    public int area() { return ancho * largo; }

    @Override
    public String toString() {
        return "Local(" + ancho + "x" + largo + ", área=" + area() + ")";
    }
}
