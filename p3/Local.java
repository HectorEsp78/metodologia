
/*

    1. Funcion objetivo: minimizar num baldosas
    2. Candidatos: {(1,1),(5,5),(2,5)}
    3. Seleccionados: {}
    4. Funcion solucion: queda suelo sin baldosas?
    5. Funcion factibilidad: cabe x baldosa en el espacio disponible?
    6. Funcion seleccion: elige la baldosa mas grande que quepa (para utilizar el menor numero de baldosas)

*/

public class Local {


    public static void main(String[] args){
        int[] dimLocal = {10,10};
        int[][] baldosas = new int[62][2];

        for(int i = 0; i < baldosas.length; i++){
            for(int j = 0; j < baldosas[0].length; j++){
                baldosas[i][j] = 1;
            }
        }

        baldosas[baldosas.length-2][0] = 5;
        baldosas[baldosas.length-2][1] = 5;
        baldosas[baldosas.length-1][0] = 5;
        baldosas[baldosas.length-1][1] = 5;

        int areaCubierta = 0;

        boolean[][] local = new boolean[dimLocal[0]][dimLocal[1]];
        for(int f = 0; f < local.length; f++){
            for(int c = 0; c < local[0].length; c++){
                local[f][c] = false;
            }
        }

        while(!solucion(dimLocal, areaCubierta)){
            int[] baldosa = seleccionarBaldosa(baldosas);
        }
      
    }

    public static boolean solucion(int[] dimLocal, int areaCubierta){
        return (dimLocal[0]*dimLocal[1]) == areaCubierta;
    }

    public static int[] seleccionarBaldosa(int[][] baldosas){
        int[] baldosa = {0,0};
        int index = 0;

        for(int i = 0; i < baldosas.length; i++){
            if((baldosas[i][0] * baldosas[i][1]) > (baldosa[0] * baldosa[1])){
                baldosa[0] = baldosas[i][0];
                baldosa[1] = baldosas[i][1];
                index = i;
            }
        }
        baldosas[index][0] = 0;
        baldosas[index][1] = 0;        

        return baldosa;
    }

    public static int[] colocarBaldosa(int[][] local, int[] baldosa){
        boolean cabeLargo = false, cabeAlto = true;
        int cuentaLargo = 0, cuentaAlto = 0;
        int[] pos = new int[2];

        for(int f = 0; f < local.length && !cabeAlto; f++){
            for(int c = 0; c < local[0].length && !cabeLargo; c++){
                if(local[f][c]){
                    cuentaLargo = 0;
                    pos[1] = c+1;
                }
                else cuentaLargo++;

                if(cuentaLargo == baldosa[0]) cabeLargo = true;
            }
            if(!cabeLargo){
                cuentaAlto = 0;
                pos[0] = f+1;
            }
            else cuentaAlto++;

            if(cuentaAlto == baldosa[1]) cabeAlto = true
        }

        if(!cuentaLargo || !cuentaAlto){
            pos[0] = -1;
            pos[1] = -1;
        }
        return pos;
    }


    public static boolean cabe(int[] baldosa, boolean[][] local){
        boolean cabe = false;
        int cuentaLargo = 0, cuentaAlto = 0;
        boolean cabeLargo = false, cabeAlto = false;

        for(int f = 0; f < local.length && !cabeAlto; f++){
            for(int c = 0; c < local.length && !cabeLargo; c++){
                if(local[f][c]){
                    cuentaLargo = 0;
                } 
                else cuentaLargo++;

                if(cuentaLargo == baldosa[1]) cabeLargo = true;
            }
            if(local[f][c]) cuentaAlto = 0;
            else cuentaAlto++;

            if(cuentaAlto == baldosa[0]) cabeAlto = true;
        }

        if(cuentaAlto && cuentaLargo) cabe = true;

        return cabe;        
    }

}
