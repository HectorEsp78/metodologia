
import java.util.Arrays;


/*

    1. Funcion objetivo: minimizar num baldosas
    2. Candidatos: {(1,1),(10,10),(15,15),(50,50)}
    3. Seleccionados: {}
    4. Funcion solucion: queda suelo sin baldosas?
    5. Funcion factibilidad: cabe x baldosa en el espacio disponible?
    6. Funcion seleccion: elige la baldosa mas grande que quepa

*/

public class Local{
    public static void main(String[] args) {
        int[][] candidatos = {{50,50},{15,15},{10,10},{1,1}};
        int[] dimLocal = {200,201};

        System.out.println(Arrays.toString(nBaldosas(candidatos, dimLocal)));

    }

    public static int[] nBaldosas(int[][] candidatos, int[] dimLocal){
        int[] nBaldosas = new int[candidatos.length];
        for(int i = 0; i < nBaldosas.length; i++){
            nBaldosas[i] = 0;
        }

        boolean[][] cubierto = new boolean[dimLocal[0]][dimLocal[1]];
        for(int i = 0; i < cubierto.length; i++){
            for(int j = 0; j < cubierto[0].length; j++){
                cubierto[i][j] = false;
            }
        }
        
        while(!solucionado(cubierto)){
            int i = 0;
            int[] baldosa = candidatos[i];

            int[] pos = cabe(baldosa, cubierto);
            while(pos[0] == -1){
                i++;
                baldosa = candidatos[i];
            }
            colocar(baldosa, cubierto, pos);
            nBaldosas[i]++;
        }

        return nBaldosas;
    }

    public static int[] cabe(int[] baldosa, boolean[][] cubierto){
        int filas = cubierto.length;
        int cols  = cubierto[0].length;
        int alto  = baldosa[0];
        int ancho = baldosa[1];

        for (int i = 0; i <= filas - alto; i++) {
            for (int j = 0; j <= cols - ancho; j++) {
                // Comprobar si el rectángulo completo está libre
                boolean encaja = true;
                for (int di = 0; di < alto && encaja; di++)
                    for (int dj = 0; dj < ancho && encaja; dj++)
                        if (cubierto[i + di][j + dj]) encaja = false;

                if (encaja) return new int[]{i, j};
            }
        }
        return new int[]{-1, -1}; // no cabe
    }

    public static boolean solucionado(boolean[][] cubierto){
        boolean solucionado = true;
        for(int i = 0; i < cubierto.length; i++){
            for(int j = 0; j < cubierto[0].length; j++){
                if(!cubierto[i][j]) solucionado = false;
            }
        }
        return solucionado;
    }

    public static void colocar(int[] baldosa, boolean[][] cubierto, int[] pos){

        for(int i = pos[0]; i < pos[0]+baldosa[0]; i++){
            for(int j = pos[1]; j < pos[1]+baldosa[1]; j++){
                cubierto[i][j] = true;
            }
        }
    }
}