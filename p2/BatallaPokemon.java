
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class BatallaPokemon {

    public static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        long tInicio, tFinal;
        int sumaDanos, masFuerte;

        /*********************
        *    COMBATE LIGA    *
        **********************/
        
        System.out.println("--- Leyendo fichero 'combate_liga.txt' ---");
        List<Integer> datosLiga = leerArchivo("combate_liga.txt");
        System.out.println("Numero de registros: " + datosLiga.size());

        tInicio = System.nanoTime();
        sumaDanos = sumaDanosIterativo(datosLiga, false);
        tFinal = System.nanoTime();
        System.out.printf("Daño neto A (Iterativo - liga): %d (Tiempo %,dns)\n", sumaDanos, (tFinal-tInicio));

        tInicio = System.nanoTime();
        sumaDanos = sumaDanosRecursivo(datosLiga, false, 0, datosLiga.size()-1);
        tFinal = System.nanoTime();
        System.out.printf("Daño neto A (Recursivo - liga): %d (Tiempo %,dns)\n", sumaDanos, (tFinal-tInicio));

        tInicio = System.nanoTime();
        sumaDanos = sumaDanosIterativo(datosLiga, true);
        tFinal = System.nanoTime();
        System.out.printf("Daño neto B (Iterativo - liga): %d (Tiempo %,dns)\n", sumaDanos, (tFinal-tInicio));

        tInicio = System.nanoTime();
        sumaDanos = sumaDanosRecursivo(datosLiga, true, 0, datosLiga.size()-1);
        tFinal = System.nanoTime();
        System.out.printf("Daño neto B (Recursivo - liga): %d (Tiempo %,dns)\n", sumaDanos*-1, (tFinal-tInicio));

        int ganador = ganador(datosLiga);
        System.out.print("Ganador (liga): ");
        if(ganador == 0) System.out.println("Entrenador A");
        if(ganador == 1) System.out.println("Entrenador B");

        System.out.println();

        tInicio = System.nanoTime();
        masFuerte = ataqueMasFuerteIt(datosLiga, false);
        tFinal = System.nanoTime();
        System.out.printf("Ataque Pokemon más fuerte A (Iterativo - liga): pos=%d, daño=%d Tiempo: %,dns)\n", masFuerte, datosLiga.get(masFuerte), (tFinal - tInicio));

        tInicio = System.nanoTime();
        masFuerte = ataqueMasFuerteDYV(datosLiga, 0, datosLiga.size()-1, false);
        tFinal = System.nanoTime();
        System.out.printf("Ataque Pokemon más fuerte A (Recursivo - liga): pos=%d, daño=%d Tiempo: %,dns)\n", masFuerte, datosLiga.get(masFuerte), (tFinal - tInicio));

        tInicio = System.nanoTime();
        masFuerte = ataqueMasFuerteIt(datosLiga, true);
        tFinal = System.nanoTime();
        System.out.printf("Ataque Pokemon más fuerte B (Iterativo - liga): pos=%d, daño=%d Tiempo: %,dns)\n", masFuerte, datosLiga.get(masFuerte)*-1, (tFinal - tInicio));

        tInicio = System.nanoTime();
        masFuerte = ataqueMasFuerteDYV(datosLiga, 0, datosLiga.size()-1, true);
        tFinal = System.nanoTime();
        System.out.printf("Ataque Pokemon más fuerte B (Recursivo - liga): pos=%d, daño=%d Tiempo: %,dns)\n", masFuerte, datosLiga.get(masFuerte)*-1, (tFinal - tInicio));

        /*********************
        *   COMBATE MUNDIAL  *
        **********************/
        System.out.println();
        System.out.println("--- Leyendo fichero 'combate_mundial.txt' ---");
        List<Integer> datosMundial = leerArchivo("combate_mundial.txt");
        System.out.println("Numero de registros: " + datosMundial.size());

        tInicio = System.nanoTime();
        sumaDanos = sumaDanosIterativo(datosMundial, false);
        tFinal = System.nanoTime();
        System.out.printf("Daño neto A (Iterativo - mundial): %d (Tiempo %,dns)\n", sumaDanos, (tFinal-tInicio));

        tInicio = System.nanoTime();
        sumaDanos = sumaDanosRecursivo(datosMundial, false, 0, datosMundial.size()-1);
        tFinal = System.nanoTime();
        System.out.printf("Daño neto A (Recursivo - mundial): %d (Tiempo %,dns)\n", sumaDanos, (tFinal-tInicio));

        tInicio = System.nanoTime();
        sumaDanos = sumaDanosIterativo(datosMundial, true);
        tFinal = System.nanoTime();
        System.out.printf("Daño neto B (Iterativo - mundial): %d (Tiempo %,dns)\n", sumaDanos, (tFinal-tInicio));

        tInicio = System.nanoTime();
        sumaDanos = sumaDanosRecursivo(datosMundial, true, 0, datosMundial.size()-1);
        tFinal = System.nanoTime();
        System.out.printf("Daño neto B (Recursivo - mundial): %d (Tiempo %,dns)\n", sumaDanos*-1, (tFinal-tInicio));

        ganador = ganador(datosLiga);
        System.out.print("Ganador (mundial): ");
        if(ganador == 0) System.out.println("Entrenador A");
        if(ganador == 1) System.out.println("Entrenador B");

        System.out.println();

        tInicio = System.nanoTime();
        masFuerte = ataqueMasFuerteIt(datosMundial, false);
        tFinal = System.nanoTime();
        System.out.printf("Ataque Pokemon más fuerte A (Iterativo - mundial): pos=%d, daño=%d Tiempo: %,dns)\n", masFuerte, datosMundial.get(masFuerte), (tFinal - tInicio));

        tInicio = System.nanoTime();
        masFuerte = ataqueMasFuerteDYV(datosMundial, 0, datosMundial.size()-1, false);
        tFinal = System.nanoTime();
        System.out.printf("Ataque Pokemon más fuerte A (Recursivo - mundial): pos=%d, daño=%d Tiempo: %,dns)\n", masFuerte, datosMundial.get(masFuerte), (tFinal - tInicio));

        tInicio = System.nanoTime();
        masFuerte = ataqueMasFuerteIt(datosMundial, true);
        tFinal = System.nanoTime();
        System.out.printf("Ataque Pokemon más fuerte B (Iterativo - mundial): pos=%d, daño=%d Tiempo: %,dns)\n", masFuerte, datosMundial.get(masFuerte)*-1, (tFinal - tInicio));

        tInicio = System.nanoTime();
        masFuerte = ataqueMasFuerteDYV(datosMundial, 0, datosMundial.size()-1, true);
        tFinal = System.nanoTime();
        System.out.printf("Ataque Pokemon más fuerte B (Recursivo - mundial): pos=%d, daño=%d Tiempo: %,dns)\n\n", masFuerte, datosMundial.get(masFuerte)*-1, (tFinal - tInicio));
    
        int ataque = 20, rango = 150;
        System.out.println("--- Batalla generada aleatoriamente ---");
        System.out.printf("Ataque: %d Rango: %d\n", ataque, rango);
        List<Integer> batallaAleatoria = generar(ataque, rango);
        System.out.println(batallaAleatoria.toString());
    }

    public static List<Integer> leerArchivo(String nombre) {
        List<Integer> datos = new ArrayList<>();
        try {
            Scanner lector = new Scanner(new File(nombre));
            while (lector.hasNextInt()) {
                datos.add(lector.nextInt());
            }
            lector.close();
        } catch (Exception e) {
            System.out.printf("Error leyendo el archivo %s\n", nombre);
            System.exit(1);
        }
        return datos;
    }

    public static int sumaDanosIterativo(List<Integer> danos, boolean entrenador) {
        int suma = 0;
        int i = 0;

        if (!entrenador) {
            while (i < danos.size()) {
                if (danos.get(i) > 0) {
                    suma += danos.get(i);
                }
                i++;
            }
        } else {
            while (i < danos.size()) {
                if (danos.get(i) < 0) {
                    suma += danos.get(i) * -1;
                }
                i++;
            }
        }

        return suma;
    }

    public static int sumaDanosRecursivo(List<Integer> v, boolean entrenador, int li, int ls) {
        if (ls == li) {
            if ((!entrenador && v.get(li) > 0)
                    || (entrenador && v.get(li) < 0)) {
                return v.get(li);
            } else {
                return 0;
            }
        }
        int mitad = (li + ls) / 2;
        int sumaIzq = sumaDanosRecursivo(v, entrenador, li, mitad);
        int sumaDer = sumaDanosRecursivo(v, entrenador, mitad + 1, ls);
        
        return sumaIzq + sumaDer;
    }

    public static int ataqueMasFuerteIt(List<Integer> danos, boolean entrenador) {
        int pos = 0, tamano = danos.size();

        for (int i = 0; i < tamano; i++) {
            if (!entrenador && danos.get(i) > danos.get(pos))
                pos = i;
            if (entrenador && danos.get(i) < danos.get(pos))
                pos = i;
        }

        return pos;
    }

    public static int ataqueMasFuerteDYV(List<Integer> v, int li, int ls, boolean entrenador) {
        if (li == ls)
            return li;

        int mitad = (li + ls) / 2;
        int masFuerteIzq = ataqueMasFuerteDYV(v, li, mitad, entrenador);
        int masFuerteDer = ataqueMasFuerteDYV(v, mitad + 1, ls, entrenador);

        int pos = masFuerteIzq;
        if (!entrenador && v.get(masFuerteDer) > v.get(masFuerteIzq))
            pos = masFuerteDer;
        if (entrenador && v.get(masFuerteDer) < v.get(masFuerteIzq))
            pos = masFuerteDer;

        return pos;
    }

    public static List<Integer> generar(int N, int R){
        List<Integer> v = new ArrayList<>();

        for(int i = 0; i < N; i++){
            v.add((int)(Math.random() * (2*R + 1)) - R);
        }
        return v;
    }

    public static int ganador(List<Integer> danos) {
        int ganador;

        int danoA = sumaDanosRecursivo(danos, false, 0, danos.size() - 1);
        int danoB = sumaDanosRecursivo(danos, true, 0, danos.size() - 1) * -1;

        if (danoA > danoB)
            ganador = 0;
        else if (danoB < danoA)
            ganador = 1;
        else
            ganador = -1;

        return ganador;
    }
}
