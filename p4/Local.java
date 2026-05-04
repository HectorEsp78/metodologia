import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * =====================================================================
 * COMPLEJIDAD DEL PROGRAMA COMPLETO
 * =====================================================================
 *
 * Sea:
 *   N  = número de baldosas disponibles
 *   F  = número de filas del local
 *   C  = número de columnas del local
 *   A  = F * C  (área total del local, en celdas)
 *
 * ------ Árbol de búsqueda (sin podas) --------------------------------
 * En cada llamada recursiva se elige una baldosa de entre las no usadas
 * y, para ella, hasta 2 orientaciones. En el peor caso, el árbol tiene:
 *
 *   Nivel 0: 1 nodo (raíz)
 *   Nivel 1: 2N candidatos (N baldosas × 2 orientaciones)
 *   Nivel k: 2^k · N! / (N-k)! nodos
 *
 * La profundidad máxima del árbol es N (se usan todas las baldosas).
 * Por tanto, el número de nodos en el peor caso es O(2^N · N!).
 *
 * ------ Trabajo por nodo --------------------------------------------
 * Cada nodo ejecuta:
 *   · primeraCeldaLibre: O(A)
 *   · cabe:              O(A)     (recorre filas y columnas)
 *   · colocarBaldosa /
 *     quitarBaldosa:     O(A)     (en el caso de una baldosa del tamaño del local)
 *   · verificarSolucion: O(A)     (solo cuando se llega a solución)
 *
 * Trabajo por nodo: O(A) = O(F·C)
 *
 * ------ Complejidad total (sin podas) --------------------------------
 *   O(2^N · N! · F·C)
 *
 * Esta complejidad es muy alta; las podas introducidas la reducen
 * significativamente en la práctica (ver comentarios en las podas).
 *
 * ------ Podas incorporadas ------------------------------------------
 * P1. Área residual: si el área libre restante es menor que el área de
 *     la baldosa más pequeña disponible, no puede haber solución en esa
 *     rama → poda de ramas completas del árbol.
 *
 * P2. Cota optimista: si el número de baldosas ya usadas en la solución
 *     parcial supera o iguala el número de baldosas de la mejor solución
 *     conocida, esa rama no puede mejorar → poda de toda la rama.
 *
 * P3. Área total: si el área total de las baldosas disponibles (no
 *     colocadas) es insuficiente para cubrir el área libre restante,
 *     no puede haber solución en esa rama.
 *
 * Estas podas no cambian el orden de complejidad en el peor caso, pero
 * reducen drásticamente el espacio de búsqueda en instancias reales.
 * =====================================================================
 */
public class Local {

    // Mejor solución encontrada hasta el momento
    static int mejorNBaldosas;          // número de baldosas de la mejor sol.
    static boolean[][] mejorLocal;      // copia del local en la mejor sol.
    static boolean[] mejorColocada;     // qué baldosas estaban colocadas

    public static void main(String[] args) {
        int[] dimLocal = {3, 5};
        Baldosa[] baldosas = generarBaldosas(10, 3, 2);
        boolean[][] local = new boolean[dimLocal[0]][dimLocal[1]];

        // Inicializar el local a false (libre)
        for (int f = 0; f < local.length; f++)
            for (int c = 0; c < local[0].length; c++)
                local[f][c] = false;

        imprimirLocal(local);

        // Inicializar variables globales de la mejor solución
        mejorNBaldosas = Integer.MAX_VALUE;
        mejorLocal     = null;
        mejorColocada  = new boolean[baldosas.length];

        // Llamada al backtracking de optimización
        backtracking(local, baldosas, 0);

        if (mejorLocal == null) {
            System.out.println("No existe solución con las baldosas disponibles.");
        } else {
            System.out.println("\n=== MEJOR SOLUCIÓN ENCONTRADA ===");
            System.out.println("Número de baldosas utilizadas: " + mejorNBaldosas);
            System.out.println("Baldosas usadas:");
            for (int i = 0; i < baldosas.length; i++)
                if (mejorColocada[i])
                    System.out.println("  Baldosa " + i
                            + " [" + baldosas[i].getLongitud()
                            + "x" + baldosas[i].getAltura() + "]");
            imprimirLocal(mejorLocal);


        }
    }


    // =====================================================================
    // BACKTRACKING DE OPTIMIZACIÓN
    // =====================================================================
    /**
     * Explora sistemáticamente todas las formas de cubrir el local y
     * guarda la que utilice el menor número de baldosas.
     *
     * REPRESENTACIÓN DE LA SOLUCIÓN:
     *   boolean[][] local  → celdas cubiertas (true) / libres (false).
     *   Baldosa[].colocada → qué baldosas están activas en la rama actual.
     *
     * DECISIÓN EN CADA PASO:
     *   Para la primera celda libre, probar cada baldosa disponible en sus
     *   dos orientaciones.
     *
     * SOLUCIÓN COMPLETA (solucion()):
     *   El local está completamente cubierto.
     *
     * SOLUCIÓN ÓPTIMA:
     *   Se almacena la solución cuyo número de baldosas sea menor que el
     *   de cualquier solución anterior.
     *
     * RETROCESO:
     *   Si la rama no mejora la mejor solución conocida, o si ningún
     *   candidato puede completar la cobertura, se deshace la colocación
     *   y se continúa probando.
     *
     */
    public static void backtracking(boolean[][] local, Baldosa[] baldosas, int usadas) {

        // FUNCIÓN SOLUCIÓN: ¿está el local completamente cubierto?
        if (solucion(local)) {
            if (usadas < mejorNBaldosas) {
                mejorNBaldosas = usadas;
                mejorLocal     = copiarLocal(local);
                mejorColocada  = new boolean[baldosas.length];
                for (int i = 0; i < baldosas.length; i++)
                    mejorColocada[i] = baldosas[i].getColocada();
                System.out.println("[Nueva mejor solución con " + usadas + " baldosas]");
            }
            return;
        }

        // ---- PODA P2: cota optimista ----------------------------------------
        // Si ya hemos colocado tantas baldosas como la mejor solución conocida,
        // no podemos mejorarla añadiendo más → podar.
        if (usadas >= mejorNBaldosas) return;

        // ---- PODA P1: área residual -----------------------------------------
        // Contamos el área libre y el área mínima de las baldosas sin colocar.
        int areaLibre = areaLibre(local);
        int areaDisponible = 0;
        int areaMinima = Integer.MAX_VALUE;
        for (Baldosa b : baldosas) {
            if (!b.getColocada()) {
                areaDisponible += b.area();
                if (b.area() < areaMinima) areaMinima = b.area();
            }
        }

        // Si el área total disponible no alcanza para cubrir lo que falta → podar.
        // (Poda P3 integrada aquí)
        if (areaDisponible < areaLibre) return;

        // Si la baldosa más pequeña es mayor que el área libre → nunca cabrá → podar.
        if (areaMinima > areaLibre) return;

        // Encontrar la primera celda libre
        int[] primerHueco = primeraCeldaLibre(local);
        if (primerHueco == null) return;

        int[] pos = new int[2];

        // Iterar sobre todos los candidatos
        for (int i = 0; i < baldosas.length; i++) {
            Baldosa b = baldosas[i];

            if (b.getColocada()) continue;  // ya usada en esta rama

            // ---- PODA P1 por baldosa individual --------------------------------
            // Si esta baldosa es mayor que el área libre, no puede caber en ningún hueco.
            if (b.area() > areaLibre) continue;

            // Probar las dos orientaciones: normal y girada
            for (int orientacion = 0; orientacion < 2; orientacion++) {

                if (orientacion == 1) b.girar();

                // FUNCIÓN DE FACTIBILIDAD: ¿cabe en el primer hueco?
                if (cabe(b, local, pos)) {

                    colocarBaldosa(local, b, pos);

                    // Llamada recursiva
                    backtracking(local, baldosas, usadas + 1);

                    // RETROCESO: deshacer siempre (buscamos la solución óptima)
                    quitarBaldosa(local, b, pos);
                }

                // Restaurar orientación al salir de la segunda pasada
                if (orientacion == 1) b.girar();
            }
        }
    }


    // =====================================================================
    // MÉTODOS AUXILIARES NUEVOS
    // =====================================================================

    /**
     * Devuelve el número de celdas libres (false) del local.
     * COMPLEJIDAD: O(F·C)
     */
    public static int areaLibre(boolean[][] local) {
        int libre = 0;
        for (boolean[] fila : local)
            for (boolean celda : fila)
                if (!celda) libre++;
        return libre;
    }

    /**
     * Devuelve una copia profunda del local.
     * COMPLEJIDAD: O(F·C)
     */
    public static boolean[][] copiarLocal(boolean[][] local) {
        boolean[][] copia = new boolean[local.length][local[0].length];
        for (int f = 0; f < local.length; f++)
            copia[f] = local[f].clone();
        return copia;
    }


    // =====================================================================
    // MÉTODOS AUXILIARES HEREDADOS (sin cambios de sesión 1)
    // =====================================================================

    /**
     * Devuelve la posición [fila, col] de la primera celda libre del local,
     * o null si no hay ninguna.
     * COMPLEJIDAD: O(F·C)
     */
    public static int[] primeraCeldaLibre(boolean[][] local) {
        for (int f = 0; f < local.length; f++)
            for (int c = 0; c < local[f].length; c++)
                if (!local[f][c]) return new int[]{f, c};
        return null;
    }

    /**
     * Retira una baldosa del local (inverso de colocarBaldosa).
     * COMPLEJIDAD: O(largo · alto) ≤ O(F·C)
     */
    public static void quitarBaldosa(boolean[][] local, Baldosa baldosa, int[] pos) {
        baldosa.descolocar();
        for (int f = pos[0]; f < pos[0] + baldosa.getLongitud(); f++)
            for (int c = pos[1]; c < pos[1] + baldosa.getAltura(); c++)
                local[c][f] = false;
    }

    public static boolean solucion(boolean[][] local) {
        for (boolean[] fila : local)
            for (boolean celda : fila)
                if (!celda) return false;
        return true;
    }

    public static void colocarBaldosa(boolean[][] local, Baldosa baldosa, int[] pos) {
        baldosa.colocar();
        for (int f = pos[0]; f < pos[0] + baldosa.getLongitud(); f++)
            for (int c = pos[1]; c < pos[1] + baldosa.getAltura(); c++)
                local[c][f] = true;
    }

    public static boolean cabe(Baldosa baldosa, boolean[][] local, int[] pos) {
        int cuentaLargo = 0, cuentaAlto = 0;
        boolean cabeLargo = false, cabeAlto = false;

        for (int f = 0; f < local.length && !cabeAlto; f++) {
            cabeLargo = false;
            cuentaLargo = 0;
            for (int c = 0; c < local[f].length && !cabeLargo; c++) {
                if (local[f][c]) { cuentaLargo = 0; } else {
                    if (cuentaLargo == 0) pos[0] = c;
                    cuentaLargo++;
                }
                if (cuentaLargo == baldosa.getLongitud()) cabeLargo = true;
            }
            if (!cabeLargo) { cuentaAlto = 0; } else {
                if (cuentaAlto == 0) pos[1] = f;
                cuentaAlto++;
            }
            if (cuentaAlto == baldosa.getAltura()) cabeAlto = true;
        }
        return cabeAlto && cabeLargo;
    }

    public static void imprimirLocal(boolean[][] local) {
        System.out.println("\n///// ESTADO ACTUAL DEL LOCAL /////");
        for (boolean[] fila : local) {
            for (boolean celda : fila)
                System.out.print(celda ? "X " : "O ");
            System.out.println();
        }
    }

    public static Baldosa[] generarBaldosas(int nBaldosas, int alturaMax, int longMax) {
        Baldosa[] baldosas = new Baldosa[nBaldosas];
        try {
            PrintWriter pw = new PrintWriter("baldosas.txt");
            pw.println(nBaldosas);
            for (int i = 0; i < nBaldosas; i++) {
                baldosas[i] = new Baldosa(i,
                        (int) (Math.random() * alturaMax) + 1,
                        (int) (Math.random() * longMax) + 1);
                pw.println(baldosas[i].getAltura() + " " + baldosas[i].getLongitud());
            }
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return baldosas;
    }
}