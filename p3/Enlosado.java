import java.io.*;
import java.util.*;

/**
 * Programa principal para el problema del enlosado.
 *
 * ESTRATEGIA VORAZ:
 *   Criterio de selección: en cada paso, elegir la baldosa disponible
 *   (en su orientación más favorable) de mayor área que quepa en el
 *   espacio restante del local.
 *   Objetivo: minimizar el número de baldosas usadas (y por tanto el coste),
 *   cubriendo el mayor área posible en cada paso.
 *
 * OPTIMALIDAD:
 *   La estrategia NO es óptima en general.
 *   (Ver contraejemplo al final del método voraz.)
 */
public class Enlosado {

    /**
     * Estrategia voraz para enlosar el local.
     *
     * ELEMENTOS DEL ALGORITMO VORAZ:
     *   - Conjunto de candidatos: lista de baldosas disponibles (con ambas orientaciones)
     *   - Función de selección: baldosa de mayor área que quepa en el espacio restante
     *   - Función de factibilidad: la baldosa (en alguna orientación) cabe en el espacio restante
     *   - Función solución: área cubierta == área total del local
     *   - Función objetivo: minimizar el número de baldosas utilizadas
     *
     * @param local     El local a enlosar
     * @param baldosas  Lista de baldosas disponibles (suministro ilimitado de cada tipo)
     * @return          Lista de baldosas seleccionadas (con su orientación elegida)
     */
    public static List<Baldosa> voraz(Local local, List<Baldosa> baldosas) {

        // --- CONJUNTO DE CANDIDATOS ---
        // Generamos todas las variantes (horizontal y vertical) de cada baldosa
        List<Baldosa> candidatos = new ArrayList<>();
        for (Baldosa b : baldosas) {
            candidatos.add(b);
            if (!b.esCuadrada()) {
                candidatos.add(b.girar());
            }
        }

        // Ordenamos los candidatos por área descendente (criterio voraz)
        candidatos.sort((a, b) -> b.area() - a.area());

        List<Baldosa> solucion = new ArrayList<>();
        int areaCubierta = 0;
        int areaTotal = local.area();

        System.out.println("=== ESTRATEGIA VORAZ ===");
        System.out.println("Local: " + local);
        System.out.println("Área total a cubrir: " + areaTotal);
        System.out.println();

        // --- FUNCIÓN SOLUCIÓN ---
        while (areaCubierta < areaTotal) {

            // --- FUNCIÓN DE SELECCIÓN ---
            // Escogemos la baldosa de mayor área que quepa en el espacio restante
            Baldosa elegida = null;
            int espacioRestante = areaTotal - areaCubierta;

            for (Baldosa candidato : candidatos) {
                // --- FUNCIÓN DE FACTIBILIDAD ---
                // La baldosa es factible si su área no supera el espacio restante
                // y sus dimensiones son compatibles con las del local
                if (candidato.area() <= espacioRestante
                        && candidato.getAncho() <= local.getAncho()
                        && candidato.getLargo() <= local.getLargo()) {
                    elegida = candidato;
                    break; // ya están ordenadas: la primera que quepa es la mejor
                }
            }

            if (elegida == null) {
                // No existe ninguna baldosa que quepa en el espacio restante
                System.out.println("No se puede cubrir todo el local con las baldosas disponibles.");
                System.out.println("Área cubierta: " + areaCubierta + " / " + areaTotal);
                break;
            }

            solucion.add(elegida);
            areaCubierta += elegida.area();
            System.out.println("Seleccionada: " + elegida
                    + " | Área cubierta acumulada: " + areaCubierta + "/" + areaTotal);
        }

        System.out.println();
        System.out.println("Total baldosas usadas: " + solucion.size());

        // ---------------------------------------------------------------
        // CONTRAEJEMPLO (la estrategia NO es óptima):
        //
        // Local: 4x4 (área = 16)
        // Baldosas disponibles: 3x3 (área=9) y 2x2 (área=4)
        //
        // Solución voraz:
        //   Paso 1: elige 3x3 → cubre 9. Restante: 7.
        //   Paso 2: el espacio restante es 7, pero la siguiente baldosa (2x2=4) cabe.
        //           Elige 2x2 → cubre 4. Restante: 3.
        //   Paso 3: la 2x2 ya no cabe (4 > 3) y la 3x3 tampoco. NO se puede completar.
        //
        // Solución óptima:
        //   4 baldosas de 2x2 → cubren 4×4 = 16. ¡Completo con 4 baldosas!
        //
        // La estrategia voraz (mayor área primero) falla porque al elegir la 3x3
        // deja un espacio residual que ninguna baldosa puede cubrir, mientras que
        // la alternativa con baldosas más pequeñas sí completa el local.
        // ---------------------------------------------------------------

        return solucion;
    }

    /**
     * Genera un archivo de texto con datos de baldosas aleatorias.
     *
     * Formato del archivo:
     *   - Primera línea: número de baldosas
     *   - Siguientes líneas: ancho largo  (una baldosa por línea)
     *
     * @param nombreArchivo  Ruta/nombre del archivo a crear
     * @param numBaldosas    Número de baldosas a generar
     * @param maxAncho       Ancho máximo de cada baldosa
     * @param maxLargo       Largo máximo de cada baldosa
     */
    public static void generarArchivoBaldosas(String nombreArchivo, int numBaldosas,
                                               int maxAncho, int maxLargo) {
        if (numBaldosas <= 0 || maxAncho <= 0 || maxLargo <= 0)
            throw new IllegalArgumentException("Los parámetros deben ser enteros positivos.");

        Random rnd = new Random();

        try (PrintWriter pw = new PrintWriter(new FileWriter(nombreArchivo))) {
            pw.println(numBaldosas);
            for (int i = 0; i < numBaldosas; i++) {
                int ancho = rnd.nextInt(maxAncho) + 1; // [1, maxAncho]
                int largo = rnd.nextInt(maxLargo) + 1; // [1, maxLargo]
                pw.println(ancho + " " + largo);
            }
            System.out.println("Archivo generado: " + nombreArchivo
                    + " (" + numBaldosas + " baldosas, max " + maxAncho + "x" + maxLargo + ")");
        } catch (IOException e) {
            System.err.println("Error al escribir el archivo: " + e.getMessage());
        }
    }

    /**
     * Lee baldosas desde un archivo generado por generarArchivoBaldosas().
     */
    public static List<Baldosa> leerBaldosasDeArchivo(String nombreArchivo) throws IOException {
        List<Baldosa> lista = new ArrayList<>();
        try (Scanner sc = new Scanner(new File(nombreArchivo))) {
            int n = sc.nextInt();
            for (int i = 0; i < n; i++) {
                int ancho = sc.nextInt();
                int largo = sc.nextInt();
                lista.add(new Baldosa(ancho, largo));
            }
        }
        return lista;
    }

    // ------------------------------------------------------------------
    // MAIN: demostración
    // ------------------------------------------------------------------
    public static void main(String[] args) throws IOException {

        System.out.println("=== DEMO 1: Caso normal ===");
        Local local1 = new Local(6, 8);
        List<Baldosa> baldosas1 = new ArrayList<>(Arrays.asList(
                new Baldosa(3, 4),
                new Baldosa(2, 2),
                new Baldosa(1, 1)
        ));
        voraz(local1, baldosas1);

        System.out.println();
        System.out.println("=== DEMO 2: Contraejemplo de no optimalidad ===");
        Local localCE = new Local(4, 4);
        List<Baldosa> baldosasCE = new ArrayList<>(Arrays.asList(
                new Baldosa(3, 3),
                new Baldosa(2, 2)
        ));
        System.out.println("(La estrategia voraz no puede completar el local;");
        System.out.println(" 4 baldosas 2x2 sería la solución óptima.)");
        System.out.println();
        voraz(localCE, baldosasCE);

        System.out.println();
        System.out.println("=== DEMO 3: Generación de archivo aleatorio ===");
        generarArchivoBaldosas("baldosas.txt", 10, 5, 5);

        System.out.println();
        System.out.println("=== DEMO 4: Lectura del archivo generado y ejecución voraz ===");
        Local local2 = new Local(10, 10);
        List<Baldosa> baldosasArchivo = leerBaldosasDeArchivo("baldosas.txt");
        System.out.println("Baldosas leídas: " + baldosasArchivo);
        System.out.println();
        voraz(local2, baldosasArchivo);
    }
}
