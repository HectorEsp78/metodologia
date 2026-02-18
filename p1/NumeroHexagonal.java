import java.util.Scanner;

public class NumeroHexagonal {
    public static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Calculo del numero hexagonal N.");
        System.out.print("Introduce el valor de N: ");
        int n = sc.nextInt();

        System.out.printf("Calculo con formula: %d\n", calculoFormula(n));
        System.out.printf("Calculo con serie iterativa: %d\n", calculoSerieIterativo(n));
        System.out.printf("Calculo con serie recursiva: %d\n", calculoSerieRecursivo(n));
    }

    /*
     * La complejidad asintotica es del orden O(n²), ya que el tamaño del problema
     * se inluye en la fórmula, con n²
     */
    public static int calculoFormula(int n) {
        return (2 * n * n) - n;
    }

    /*
     * La complejidad asintotica es del orden O(n), ya que la parte más costosa del
     * método (bucle for)
     * se ejecuta n veces, el resto son constantes que no aumentan con el tamaño del
     * problema.
     */
    public static int calculoSerieIterativo(int n) {
        int h = 0; // 1
        for (int i = 0; i < n; i++) { // 1 + 2n
            h += 4 * i + 1; // 4
        }
        return h; // 1
    }

    /*
     * Aplicamos la fórmula pero esta vez usando un método recursivo
     * La complejidad asisntótica es del orden O(n), ya que el método se llama n
     * veces,
     * y cada llamada es constante.
     */
    public static int calculoSerieRecursivo(int n) {
        n--; // 1
        int h; // 1
        if (n == 0)  // 1
            h = 1; // 1
        else
            h = 4 * n + 1 + calculoSerieRecursivo(n); // 4 + T(n-1)
        return h; // 1
    }
}
