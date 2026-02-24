import java.util.Scanner;

public class NumeroHexagonal {
    public static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.print("Cómo desea ver los tiempos de ejecución? (nano/mili): ");
        String opcion = sc.nextLine();
        boolean nano = opcion.equalsIgnoreCase("nano");
        System.out.println("\nTabla de números hexagonales (calculado con una media de 30 ejecuciones) :");
        MostrarTabla(nano);
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
        if (n == 0) // 1
            h = 1; // 1
        else
            h = 4 * n + 1 + calculoSerieRecursivo(n); // 4 + T(n-1)
        return h; // 1
    }

    public static double[] calcularTiemposMedios(int n){
        double[] tiempos = new double[3];
        long inicio, fin;
        // Calcular tiempo promedio para la fórmula
        long total = 0;
        for (int i = 0; i < 30; i++) {
            inicio = System.nanoTime();
            calculoFormula(n);
            fin = System.nanoTime();
            total += (fin - inicio);
        }
        tiempos[0] = total / 30;

        // Calcular tiempo promedio para el método iterativo
        total = 0;
        for (int i = 0; i < 30; i++) {
            inicio = System.nanoTime();
            calculoSerieIterativo(n);
            fin = System.nanoTime();
            total += (fin - inicio);
        }
        tiempos[1] = total / 30;

        // Calcular tiempo promedio para el método recursivo
        total = 0;
        for (int i = 0; i < 30; i++) {
            inicio = System.nanoTime();
            calculoSerieRecursivo(n);
            fin = System.nanoTime();
            total += (fin - inicio);
        }
        tiempos[2] = total / 30;

        return tiempos;
    }

    public static void MostrarTabla(boolean nano) {
        int[] n = { 10, 100, 500, 1000, 5000, 8000, 10000, 11000, 12000 };
        System.out.println("----------------------------------------------------------------------------");
        System.out.printf("| %-5s | %-10s | %-15s | %-15s | %-15s |%n", "N", "Valor", "Fórmula", "Iterativo",
                "Recursivo");
        System.out.println("----------------------------------------------------------------------------");

        for (int i = 0; i < n.length; i++) {
            double[] tiempos = calcularTiemposMedios(n[i]);
            int valorFormula = calculoFormula(n[i]);
            double duracionFormula = tiempos[0];
            double duracionIterativo = tiempos[1];
            double duracionRecursivo = tiempos[2];
            if (!nano) {
                duracionFormula /= 1000000; // Convertir a milisegundos
                duracionIterativo /= 1000000; // Convertir a milisegundos
                duracionRecursivo /= 1000000; // Convertir a milisegundos

                System.out.printf("| %-5d | %-10d | %-12.6f ms | %-12.6f ms | %-12.6f ms |%n", n[i], valorFormula,
                        duracionFormula, duracionIterativo, duracionRecursivo);
            } else {
                System.out.printf("| %-5d | %-10d | %-12.2f ns | %-12.2f ns | %-12.2f ns |%n", n[i], valorFormula,
                        duracionFormula, duracionIterativo, duracionRecursivo);
            }

        }

    }
}
