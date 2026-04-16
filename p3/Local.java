
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
        int[] dimLocal = {10,15};
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

        while(!solucion(dimLocal, areaCubierta, baldosas)){
            int[] infoBaldosa = seleccionarBaldosa(baldosas);
            int[] baldosa = {infoBaldosa[0], infoBaldosa[1]};
            if(cabe(baldosa, local)){
                int[] pos = colocarBaldosa(local, baldosa);
                if(pos[0] != -1 && pos[1] != -1){
                    System.out.println("Colocando baldosa de " + baldosa[0] + "x" + baldosa[1] + " en posicion (" + pos[0] + "," + pos[1] + ")");
                    areaCubierta += baldosa[0]*baldosa[1];
                    baldosas[infoBaldosa[2]][0] = 0;
                    baldosas[infoBaldosa[2]][1] = 0;
                    System.out.println("Area cubierta: " + areaCubierta);
                    for(int f = pos[0]; f < pos[0]+baldosa[0]; f++){
                        for(int c = pos[1]; c < pos[1]+baldosa[1]; c++){
                            local[f][c] = true;
                            System.out.println("Marcando posicion (" + f + "," + c + ") como cubierta");
                        }
                    }
                }
            }

        }
      
    }

    public static boolean solucion(int[] dimLocal, int areaCubierta, int[][] baldosas){
        boolean quedanBaldosas = false;
        for(int i = 0; i < baldosas.length; i++){
            if(baldosas[i][0] != 0 && baldosas[i][1] != 0){
                quedanBaldosas = true;
                break;
            }
        }
        return (dimLocal[0]*dimLocal[1]) == areaCubierta || !quedanBaldosas;
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
        int[] infoBaldosa = {baldosa[0], baldosa[1], index};     

        return infoBaldosa;
    }

    public static int[] colocarBaldosa(boolean[][] local, int[] baldosa){
        boolean cabeLargo = false, cabeAlto = false, girada = false;
        int cuentaLargo = 0, cuentaAlto = 0, auxGirar;
        int[] pos = new int[2];

        for(int f = 0; f < local.length && !cabeAlto; f++){
            for(int c = 0; c < local[f].length && !cabeLargo; c++){
                if(local[f][c]) cuentaLargo = 0;
                else{
                    pos[1] = c - cuentaLargo;
                    cuentaLargo++;
                }

                if(cuentaLargo == baldosa[0]) cabeLargo = true;
            }
            if(!cabeLargo) cuentaAlto = 0;
            else{
                pos[0] = f - cuentaAlto;
                cuentaAlto++;
            }

            if(cuentaAlto == baldosa[1]) cabeAlto = true;

            /*if((!cabeAlto && (f==(local.length - 1))) && !girada){
                auxGirar = baldosa[0];
                baldosa[0] = baldosa[1];
                baldosa[1] = auxGirar;
                girada = true;
                f=0;
            }*/
        }

        if(!cabeLargo || !cabeAlto){
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
                    cuentaAlto = 0;
                } 
                else cuentaLargo++;

                if(cuentaLargo == baldosa[1]) cabeLargo = true;
            }
            if(!cabeLargo) cuentaAlto = 0;
            else cuentaAlto++;

            if(cuentaAlto == baldosa[0]) cabeAlto = true;
        }

        if(cabeAlto && cabeLargo) cabe = true;

        return cabe;        
    }

}
