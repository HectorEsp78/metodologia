
/*

    1. Funcion objetivo: minimizar num baldosas
    2. Candidatos: {(1,1),(5,5),(2,5)}
    3. Seleccionados: {}
    4. Funcion solucion: queda suelo sin baldosas?
    5. Funcion factibilidad: cabe x baldosa en el espacio disponible?
    6. Funcion seleccion: elige la baldosa mas grande que quepa

*/

public class Local {


    public static void main(String[] args){
        int[] dimLocal = {100,100};
        int[][] baldosas = {{1,1}, {2,5}, {5,5}};
        int areaCubierta = 0;

        boolean[][] local = new boolean[dimLocal[0]][dimLocal[1]];
        for(int f = 0; f < local.length; f++){
            for(int c = 0; c < local[0].length; c++){
                local[f][c] = false;
            }
        }

        while(!solucion(dimLocal, areaCubierta)){
            for(int i = 0; i < baldosas.length; i++){

            }
        }
      
    }

    public static boolean solucion(int[] dimLocal, int areaCubierta){
        return (dimLocal[0]*dimLocal[1]) == areaCubierta;
    }

    public static boolean cabe(int[] baldosa, boolean[][] local){
        boolean cabe = false;
        int cuentaLargo = 0, cuentaAlto = 0;

        for(int f = 0; f < local.length && cuentaLargo < baldosa[0]; f++){
            for(int c = 0; c < local.length && cuentaAlto < baldosa[1]; c++){
                if(local[f][c]) cuentaAlto = 0; 
                else cuentaAlto++;
            }
            if(cuentaAlto != baldosa[1]) cuentaLargo = 0;
            else cuentaLargo++;
        }

        return cabe;        
    }
}
