import java.io.FileNotFoundException;
import java.io.PrintWriter;


/*

    1. Funcion objetivo: minimizar num baldosas
    2. Candidatos: 40 baldosas de dimensiones 4x3 como maximo (generadas aleatoriamente)
    3. Seleccionados: {}
    4. Funcion solucion: queda suelo sin cubrir?
    5. Funcion factibilidad: cabe x baldosa en el espacio disponible?
    6. Funcion seleccion: elige la baldosa mas grande que quepa (para utilizar el menor numero de baldosas)

*/

public class Local {


    public static void main(String[] args){
        /*
        Contraejemplo: 
        int[] dimLocal = {10,10};
        Baldosa[] baldosas = new Baldosa[45];
        for(int i = 0; i < 40; i++){
            baldosas[i] = new Baldosa(i, 1, 1);
        }
        baldosas[40] = new Baldosa(40, 5, 5);
        baldosas[41] = new Baldosa(41, 5, 5);
        baldosas[42] = new Baldosa(42, 5, 5);
        baldosas[43] = new Baldosa(43, 5, 5);
        baldosas[44] = new Baldosa(44, 10, 6);
        */

        int[] dimLocal = {10,15};
        Baldosa[] baldosas = generarBaldosas(40, 4, 3);
        int areaCubierta = 0, baldosasUtilizadas = 0;
        boolean[][] local = new boolean[dimLocal[0]][dimLocal[1]];

        for(int f = 0; f < local.length; f++){
            for(int c = 0; c < local[0].length; c++){
                local[f][c] = false;
            }
        }

        imprimirLocal(local);

        int[] pos = new int[2];


        /*
         * COMPLEJIDAD DEL BUCLE PRINCIPAL: O(B · (N·M + B))
         *
         * Donde:
         *   N = filas del local
         *   M = columnas del local
         *   B = número de baldosas
         *
         * Cada baldosa pasa como máximo por 2 estados antes de descartarse
         * (orientación normal → girada → marcada como "no cabe"), por lo que
         * el bucle ejecuta como mucho 2·B iteraciones.
         *
         * Cada iteración realiza:
         *   - baldosasRestantes()  → O(B)
         *   - seleccionarBaldosa() → O(B)
         *   - cabe()               → O(N·M)
         *   - colocarBaldosa()     → O(N·M)
         *   - solucion()           → O(N·M)
         *   - imprimirLocal()      → O(N·M)
         *
         * Coste por iteración: O(N·M + B)
         * Total: 2·B · O(N·M + B) = O(B·N·M + B²)
         *
         * Si B << N·M (caso habitual), domina O(B·N·M).
         */


        boolean solucion = solucion(local);
        while(!solucion && quedanBaldosas(baldosas)){
            System.out.printf("Baldosas disponibles: %d\n", baldosasRestantes(baldosas));
            Baldosa baldosa = seleccionarBaldosa(baldosas);
            if(cabe(baldosa, local, pos)){
                System.out.print("Colocando baldosa ");
                if(baldosa.esGirada()) System.out.print("GIRADA ");
                System.out.println("de " + baldosa.getLongitud() + "x" + baldosa.getAltura() + " en posicion (" + pos[0] + "," + pos[1] + ")");
                colocarBaldosa(local, baldosa, pos);
                baldosasUtilizadas++;

                areaCubierta += baldosa.area();
                System.out.println("Area cubierta: " + areaCubierta);
                
                imprimirLocal(local);
            } else {
                if(baldosa.esGirada()) baldosa.noCabe();
                else baldosa.girar();
            }
            solucion = solucion(local);
        }

        if(!solucion){
            System.out.println("No se ha podido llegar a una solución. Las baldosas son insuficientes.");
        } else {
            System.out.println("--- Local Cubierto ---");
            imprimirLocal(local);
            System.out.println("\nBaldosas utilizadas: " + baldosasUtilizadas);
        }
      
    }


    /*
     * COMPLEJIDAD: O(N·M)
     *
     * Recorre todas las celdas de la matriz (N filas × M columnas) para
     * comprobar si alguna sigue siendo false (sin cubrir).
     * En cuanto encuentra una celda vacía devuelve false; en el peor caso
     * (local completamente cubierto) visita las N·M celdas.
     */
    public static boolean solucion(boolean[][] local){
        boolean solucion = true;

        for (int i = 0; i < local.length; i++) {
            for (int j = 0; j < local[i].length; j++) {
                if(!local[i][j]) solucion = false;   
            }
        }
        return solucion;
    }

    /*
     * COMPLEJIDAD: O(B)
     *
     * Donde B = número de baldosas.
     *
     * Realiza dos pasadas lineales sobre el array:
     *   1ª pasada (bucle do-while): localiza la primera baldosa disponible → O(B)
     *   2ª pasada (bucle for):      busca la baldosa de mayor área entre
     *                               las disponibles → O(B)
     *
     * Total: O(B) + O(B) = O(B)
     *
     * Criterio de selección: escoge la baldosa con mayor área que no esté
     * marcada como "no cabe" ni como "colocada" (estrategia greedy).
     */
    public static Baldosa seleccionarBaldosa(Baldosa[] baldosas){
        int i = 0;
        Baldosa baldosa = null;
        do { 
            baldosa = baldosas[i];
            i++;
        } while (!baldosa.getCabe() || baldosa.getColocada());

        for(int j = 0; j < baldosas.length; j++){
            if(baldosas[j].getCabe() && !baldosas[j].getColocada() && baldosas[j].area() > baldosa.area()) baldosa = baldosas[j];
        }     

        return baldosa;
    }


    /*
     * COMPLEJIDAD: O(largo · alto)
     *
     * Donde 'largo' y 'alto' son las dimensiones de la baldosa a colocar.
     * En el peor caso la baldosa ocupa toda la matriz → O(N·M).
     *
     * Marca como true todas las celdas del local que cubre la baldosa,
     * iterando sobre su área rectangulara (largo × alto).
     */
    public static void colocarBaldosa(boolean[][] local, Baldosa baldosa, int[] pos){
        baldosa.colocar();
        for(int f = pos[0]; f < pos[0]+baldosa.getLongitud(); f++){
            for(int c = pos[1]; c < pos[1]+baldosa.getAltura(); c++){
                local[c][f] = true;
            }
        }
    }


    /*
     * COMPLEJIDAD: O(N·M)
     *
     * Donde N = filas del local, M = columnas del local.
     *
     * Recorre la matriz fila a fila buscando un hueco rectangular libre
     * donde quepa la baldosa (dimensiones getLongitud() × getAltura()):
     *   - Bucle externo: recorre las N filas.
     *   - Bucle interno: recorre las M columnas de cada fila contando celdas
     *     libres consecutivas (cuentaLargo). Cuando la racha llega a
     *     getLongitud(), marca esa fila como válida y cuenta filas
     *     consecutivas válidas (cuentaAlto).
     *   - En cuanto cuentaAlto alcanza getAltura(), la baldosa cabe y
     *     devuelve true con la posición almacenada en pos[].
     *
     * En el peor caso (la baldosa no cabe) se visitan todas las N·M celdas.
     */
    public static boolean cabe(Baldosa baldosa, boolean[][] local, int[] pos) {
    int cuentaLargo = 0, cuentaAlto = 0;
    boolean cabeLargo = false, cabeAlto = false;

    for (int f = 0; f < local.length && !cabeAlto; f++) {
        cabeLargo = false;
        cuentaLargo = 0;

        for (int c = 0; c < local[f].length && !cabeLargo; c++) {
            if (local[f][c]) {
                cuentaLargo = 0;
            } else {
                if (cuentaLargo == 0) pos[0] = c;
                cuentaLargo++;
            }
            if (cuentaLargo == baldosa.getLongitud()) cabeLargo = true;
        }

        if (!cabeLargo) {
            cuentaAlto = 0;
        } else {
            if (cuentaAlto == 0) pos[1] = f;
            cuentaAlto++;
        }

        if (cuentaAlto == baldosa.getAltura()) cabeAlto = true;
    }

    return cabeAlto && cabeLargo;
}


    /*
     * COMPLEJIDAD: O(N·M)
     *
     * Recorre la matriz completa (N filas × M columnas) para imprimir
     * el estado de cada celda. Siempre visita las N·M celdas sin atajos.
     */
    public static void imprimirLocal(boolean[][] local){
        System.out.println();
        System.out.println("///// ESTADO ACTUAL DEL LOCAL /////");
        for(int i = 0; i < local.length; i++){
            for(int j = 0; j < local[i].length; j++){
                if(local[i][j]) System.out.print("X ");
                else System.out.print("O ");
            }
            System.out.println();
        }
    }


    /*
     * COMPLEJIDAD: O(B)
     *
     * Una única pasada lineal sobre el array de B baldosas,
     * contando las que siguen disponibles (getCabe() && !getColocada()).
     */
    public static int baldosasRestantes(Baldosa[] baldosas){
        int nBaldosas = 0;
        for(int i = 0; i < baldosas.length; i++){
            if(baldosas[i].getCabe() && !baldosas[i].getColocada()){
                nBaldosas++;
            }
        }
        return nBaldosas;
    }


    /*
     * COMPLEJIDAD: O(B)
     *
     * Delega en baldosasRestantes() → O(B).
     * La comparación con 0 es O(1), por lo que el total es O(B).
     */
    public static boolean quedanBaldosas(Baldosa[] baldosas){
        return baldosasRestantes(baldosas) > 0;
    }


    /*
     * COMPLEJIDAD: O(B)
     *
     * Donde B = nBaldosas (número de baldosas a generar).
     *
     * Un único bucle de B iteraciones: crea cada baldosa con dimensiones
     * aleatorias en O(1) y la escribe en el fichero en O(1).
     * La apertura/cierre del PrintWriter es O(1).
     *
     * Total: O(B)
     */
    public static Baldosa[] generarBaldosas(int nBaldosas, int alturaMax, int longMax){
        Baldosa[] baldosas = new Baldosa[nBaldosas];

        try{
            PrintWriter pw = new PrintWriter("baldosas.txt");
        
            pw.println(nBaldosas);

            for(int i = 0; i < nBaldosas; i++){
                baldosas[i] = new Baldosa(i, ((int) (Math.random() * alturaMax) + 1), ((int) (Math.random() * longMax) + 1));
                pw.print(baldosas[i].getAltura() + " ");
                pw.println(baldosas[i].getLongitud());
            }
            pw.close();
        } catch(FileNotFoundException e){
            e.printStackTrace();
            System.exit(1);
        }
        return baldosas;
    }


}
