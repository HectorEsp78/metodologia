import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Local {

    public static void main(String[] args){
        int[] dimLocal = {6, 5};
        Baldosa[] baldosas = generarBaldosas(40, 3, 2);
        boolean[][] local = new boolean[dimLocal[0]][dimLocal[1]];

        // Inicializar el local a false (libre)
        for(int f = 0; f < local.length; f++)
            for(int c = 0; c < local[0].length; c++)
                local[f][c] = false;

        imprimirLocal(local);

        // Llamada al backtracking
        boolean[] encontrada = {false};   // array para pasar boolean por referencia
        backtracking(local, baldosas, encontrada);

        if(!encontrada[0]){
            System.out.println("No existe solución con las baldosas disponibles.");
        } else {
            System.out.println("--- Local Cubierto ---");
            imprimirLocal(local);
        }
    }


    /**
     * BACKTRACKING recursivo.
     *
     * Elementos propios de la técnica:
     *
     * REPRESENTACIÓN DE LA SOLUCIÓN:
     *   La matriz boolean[][] local representa el estado actual: true = celda cubierta.
     *   El array Baldosa[] baldosas registra qué baldosas están colocadas (colocada=true).
     *
     * DECISIÓN EN CADA PASO:
     *   Para la primera celda libre que se encuentre, se prueba a colocar cada baldosa
     *   disponible (no colocada) en sus dos orientaciones posibles (normal y girada).
     *
     * SOLUCIÓN PARCIAL VÁLIDA (factibilidad):
     *   Una baldosa es candidata si cabe() devuelve true en la posición del primer hueco.
     *
     * SOLUCIÓN COMPLETA:
     *   solucion() devuelve true → no queda ninguna celda sin cubrir.
     *
     * RETROCESO:
     *   Si ningún candidato conduce a solución, se deshace la colocación de la baldosa
     *   actual (quitarBaldosa), se restaura su orientación si fue girada, y se continúa
     *   probando con la siguiente baldosa.
     */
    public static void backtracking(boolean[][] local, Baldosa[] baldosas, boolean[] encontrada){

        // FUNCIÓN SOLUCIÓN: ¿está el local completamente cubierto?
        if(solucion(local)){
            encontrada[0] = true;
            return;
        }

        // Encontrar la primera celda libre (para anclar la búsqueda)
        int[] primerHueco = primeraCeldaLibre(local);
        if(primerHueco == null) return;   // no debería ocurrir si solucion() es correcto

        int[] pos = new int[2];

        // Iterar sobre todos los candidatos
        for(int i = 0; i < baldosas.length && !encontrada[0]; i++){
            Baldosa b = baldosas[i];

            if(b.getColocada()) continue;  // ya usada en esta rama

            // Probar las dos orientaciones: normal y girada
            for(int orientacion = 0; orientacion < 2 && !encontrada[0]; orientacion++){

                if(orientacion == 1) b.girar();   // segunda pasada: girar la baldosa

                // FUNCIÓN DE FACTIBILIDAD: ¿cabe esta baldosa en el primer hueco?
                if(cabe(b, local, pos)){

                    // Seleccionar y colocar
                    System.out.println("Colocando baldosa " + b.getId()
                        + (b.esGirada() ? " GIRADA" : "")
                        + " [" + b.getLongitud() + "x" + b.getAltura() + "]"
                        + " en (" + pos[0] + "," + pos[1] + ")");

                    colocarBaldosa(local, b, pos);
                    imprimirLocal(local);

                    // Llamada recursiva
                    backtracking(local, baldosas, encontrada);

                    // RETROCESO: si la rama no llegó a solución, deshacer
                    if(!encontrada[0]){
                        System.out.println("Retrocediendo: quitando baldosa " + b.getId());
                        quitarBaldosa(local, b, pos);
                    }
                }

                // Restaurar orientación al salir de la segunda pasada
                if(orientacion == 1) b.girar();
            }
        }
    }


    /**
     * Devuelve la posición [fila, col] de la primera celda libre del local,
     * o null si no hay ninguna.
     * COMPLEJIDAD: O(N·M)
     */
    public static int[] primeraCeldaLibre(boolean[][] local){
        for(int f = 0; f < local.length; f++)
            for(int c = 0; c < local[f].length; c++)
                if(!local[f][c]) return new int[]{f, c};
        return null;
    }


    /**
     * Retira una baldosa del local (inverso de colocarBaldosa).
     * COMPLEJIDAD: O(largo · alto) ≤ O(N·M)
     */
    public static void quitarBaldosa(boolean[][] local, Baldosa baldosa, int[] pos){
        baldosa.descolocar();
        for(int f = pos[0]; f < pos[0] + baldosa.getLongitud(); f++)
            for(int c = pos[1]; c < pos[1] + baldosa.getAltura(); c++)
                local[c][f] = false;
    }


    // ---------------------------------------------------------------
    // Métodos auxiliares reutilizados sin cambios de la práctica anterior
    // ---------------------------------------------------------------

    public static boolean solucion(boolean[][] local){
        for(int i = 0; i < local.length; i++)
            for(int j = 0; j < local[i].length; j++)
                if(!local[i][j]) return false;
        return true;
    }

    public static void colocarBaldosa(boolean[][] local, Baldosa baldosa, int[] pos){
        baldosa.colocar();
        for(int f = pos[0]; f < pos[0] + baldosa.getLongitud(); f++)
            for(int c = pos[1]; c < pos[1] + baldosa.getAltura(); c++)
                local[c][f] = true;
    }

    public static boolean cabe(Baldosa baldosa, boolean[][] local, int[] pos){
        int cuentaLargo = 0, cuentaAlto = 0;
        boolean cabeLargo = false, cabeAlto = false;

        for(int f = 0; f < local.length && !cabeAlto; f++){
            cabeLargo = false;
            cuentaLargo = 0;
            for(int c = 0; c < local[f].length && !cabeLargo; c++){
                if(local[f][c]){ cuentaLargo = 0; }
                else {
                    if(cuentaLargo == 0) pos[0] = c;
                    cuentaLargo++;
                }
                if(cuentaLargo == baldosa.getLongitud()) cabeLargo = true;
            }
            if(!cabeLargo){ cuentaAlto = 0; }
            else {
                if(cuentaAlto == 0) pos[1] = f;
                cuentaAlto++;
            }
            if(cuentaAlto == baldosa.getAltura()) cabeAlto = true;
        }
        return cabeAlto && cabeLargo;
    }

    public static void imprimirLocal(boolean[][] local){
        System.out.println("\n///// ESTADO ACTUAL DEL LOCAL /////");
        for(int i = 0; i < local.length; i++){
            for(int j = 0; j < local[i].length; j++)
                System.out.print(local[i][j] ? "X " : "O ");
            System.out.println();
        }
    }

    public static Baldosa[] generarBaldosas(int nBaldosas, int alturaMax, int longMax){
        Baldosa[] baldosas = new Baldosa[nBaldosas];
        try{
            PrintWriter pw = new PrintWriter("baldosas.txt");
            pw.println(nBaldosas);
            for(int i = 0; i < nBaldosas; i++){
                baldosas[i] = new Baldosa(i,
                    (int)(Math.random() * alturaMax) + 1,
                    (int)(Math.random() * longMax) + 1);
                pw.println(baldosas[i].getAltura() + " " + baldosas[i].getLongitud());
            }
            pw.close();
        } catch(FileNotFoundException e){
            e.printStackTrace(); System.exit(1);
        }
        return baldosas;
    }
}