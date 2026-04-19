
import java.io.FileNotFoundException;
import java.io.PrintWriter;


/*

    1. Funcion objetivo: minimizar num baldosas
    2. Candidatos: baldosas de dimensiones 4x3 como maximo
    3. Seleccionados: {}
    4. Funcion solucion: queda suelo sin cubrir?
    5. Funcion factibilidad: cabe x baldosa en el espacio disponible?
    6. Funcion seleccion: elige la baldosa mas grande que quepa (para utilizar el menor numero de baldosas)

*/

public class Local {


    public static void main(String[] args){
        int[] dimLocal = {10,15};
        Baldosa[] baldosas = generarBaldosas(152, 4, 3);
        int areaCubierta = 0;
        boolean[][] local = new boolean[dimLocal[0]][dimLocal[1]];

        for(int f = 0; f < local.length; f++){
            for(int c = 0; c < local[0].length; c++){
                local[f][c] = false;
            }
        }

        imprimirLocal(local);

        boolean solucion = solucion(dimLocal, areaCubierta);
        while(!solucion && quedanBaldosas(baldosas)){
            System.out.printf("Baldosas disponibles: %d\n", baldosasRestantes(baldosas));
            Baldosa baldosa = seleccionarBaldosa(baldosas);
            if(cabe(baldosa, local)){
                int[] pos = colocarBaldosa(local, baldosa);
                if(pos[0] != -1 && pos[1] != -1){
                    System.out.println("Colocando baldosa de " + baldosa.getLongitud() + "x" + baldosa.getAltura() + " en posicion (" + pos[0] + "," + pos[1] + ")");
                    areaCubierta += baldosa.area();
                    baldosa.colocar();
                    System.out.println("Area cubierta: " + areaCubierta);
                    for(int f = pos[0]; f < pos[0]+baldosa.getLongitud(); f++){
                        for(int c = pos[1]; c < pos[1]+baldosa.getAltura(); c++){
                            local[c][f] = true;
                        }
                    }
                } else {
                    //baldosa.noCabe();
                }
                imprimirLocal(local);
            } else {
                baldosa.girar();
                if(cabe(baldosa, local)){
                    int[] pos = colocarBaldosa(local, baldosa);
                if(pos[0] != -1 && pos[1] != -1){
                    System.out.println("Colocando baldosa GIRADA de " + baldosa.getLongitud() + "x" + baldosa.getAltura() + " en posicion (" + pos[0] + "," + pos[1] + ")");
                    areaCubierta += baldosa.area();
                    baldosa.colocar();
                    System.out.println("Area cubierta: " + areaCubierta);
                    for(int f = pos[0]; f < pos[0]+baldosa.getLongitud(); f++){
                        for(int c = pos[1]; c < pos[1]+baldosa.getAltura(); c++){
                            local[c][f] = true;
                        }
                    }
                } else {
                    //baldosa.noCabe();
                }
                imprimirLocal(local);
                }
                baldosa.noCabe();
            }
            solucion = solucion(dimLocal, areaCubierta);
        }

        if(!solucion){
            System.out.println("No se ha podido llegar a una solución. Las baldosas son insuficientes.");
        } else {
            System.out.println("--- Local Cubierto ---");
            imprimirLocal(local);
        }
      
    }

    public static boolean solucion(int[] dimLocal, int areaCubierta){
        return (dimLocal[0]*dimLocal[1]) == areaCubierta;
    }

    /*
        Selecciona la baldosa más grande (con mayor área), comprobando que no ha sido marcada como "No Cabe" o como "Colocada".
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

    public static int[] colocarBaldosa(boolean[][] local, Baldosa baldosa){
        boolean cabeLargo = false, cabeAlto = false;
        int cuentaLargo = 0, cuentaAlto = 0;
        int[] pos = new int[2];

        for(int f = 0; f < local.length && !cabeAlto; f++){
            for(int c = 0; c < local[f].length && !cabeLargo; c++){
                if(local[f][c]) cuentaLargo = 0;
                else{
                    pos[0] = c - cuentaLargo;
                    cuentaLargo++;
                }

                if(cuentaLargo == baldosa.getLongitud()) cabeLargo = true;
            }
            if(!cabeLargo){
                cuentaAlto = 0;
                cuentaLargo = 0;
            }
            else{
                pos[1] = f - cuentaAlto;
                cuentaAlto++;
            }

            if(cuentaAlto == baldosa.getAltura()) cabeAlto = true;
        }

        if(!cabeLargo || !cabeAlto){
            pos[0] = -1;
            pos[1] = -1;
        } else {
            baldosa.colocar();
        }
        return pos;
    }

    public static boolean cabe(Baldosa baldosa, boolean[][] local){
        boolean cabe = false;
        int cuentaLargo = 0, cuentaAlto = 0;
        boolean cabeLargo = false, cabeAlto = false;

        for(int f = 0; f < local.length && !cabeAlto; f++){
            for(int c = 0; c < local[f].length && !cabeLargo; c++){
                if(local[f][c]){
                    cuentaLargo = 0;
                    cuentaAlto = 0;
                } 
                else cuentaLargo++;

                if(cuentaLargo == baldosa.getLongitud()) cabeLargo = true;
            }
            if(!cabeLargo) cuentaAlto = 0;
            else cuentaAlto++;

            if(cuentaAlto == baldosa.getAltura()) cabeAlto = true;
        }

        if(cabeAlto && cabeLargo) cabe = true;

        return cabe;        
    }

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

    public static int baldosasRestantes(Baldosa[] baldosas){
        int nBaldosas = 0;
        for(int i = 0; i < baldosas.length; i++){
            if(baldosas[i].getCabe()){
                nBaldosas++;
            }
        }
        return nBaldosas;
    }

    public static boolean quedanBaldosas(Baldosa[] baldosas){
        return baldosasRestantes(baldosas) > 0;
    }

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
