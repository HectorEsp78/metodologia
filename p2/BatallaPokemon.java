
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class BatallaPokemon {

    public static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        List<Integer> danos = leerArchivoDanos("p2/DatosPokemon1.txt");
        System.out.println("Daños leídos: " + danos.toString());

        System.out.print("Elige un entrenador (A/B): ");
        String entrenador = sc.next().toUpperCase();
        System.out.println("\n");

        if (entrenador.equals("A") || entrenador.equals("B")) {
            probarMetodos(danos, entrenador);
            int ganador = ganador(danos);
            switch (ganador) {
                case 0:
                    System.out.println("EL GANADOR DE LA BATALLA ES: A");
                    break;
                case 1:
                    System.out.println("EL GANADOR DE LA BATALLA ES: B");
                    break;
                default:
                    System.out.println("EMPATE!");
                    break;
            }
        } else
            System.out.println("Opcion invalida, finalizando...");

    }

    public static List<Integer> leerArchivoDanos(String nombreArchivo) {
        List<Integer> danos = new ArrayList<>();
        try {
            Scanner lector = new Scanner(new File(nombreArchivo));
            while (lector.hasNextInt()) {
                danos.add(lector.nextInt());
            }
            lector.close();
        } catch (Exception e) {
            System.out.printf("Error leyendo el archivo %s\n", nombreArchivo);
            System.exit(1);
        }
        return danos;
    }

    public static int sumaDanosIterativo(List<Integer> danos, String entrenador) {
        int suma = 0;
        int i = 0;
        if (entrenador.toLowerCase().equals("a")) {
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

    public static int sumaDanosRecursivo(List<Integer> v, String entrenador, int li, int ls) {
        if (ls == li) {
            if ((entrenador.toUpperCase().equals("A") && v.get(li) > 0)
                    || (entrenador.toUpperCase().equals("B") && v.get(li) < 0)) {
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

    public static void probarMetodos(List<Integer> danos, String entrenador) {
        long tInicio, tFinal;
        int resIterativo, resRecursivo;

        tInicio = System.nanoTime();
        resIterativo = sumaDanosIterativo(danos, entrenador);
        tFinal = System.nanoTime();

        System.out.printf("Daño neto del entrenador %s (Iterativo): %d (Tiempo: %,dns)\n", entrenador, resIterativo,
                (tFinal - tInicio));

        tInicio = System.nanoTime();
        resRecursivo = sumaDanosRecursivo(danos, entrenador, 0, danos.size() - 1);
        tFinal = System.nanoTime();

        if (resRecursivo < 0)
            resRecursivo *= -1;

        System.out.printf("Daño neto del entrenador %s (Recursivo): %d (Tiempo: %,dns)\n", entrenador, resRecursivo,
                (tFinal - tInicio));

    }

    public static int ganador(List<Integer> danos) {
        int ganador;

        int danoA = sumaDanosRecursivo(danos, "A", 0, danos.size() - 1);
        int danoB = sumaDanosRecursivo(danos, "B", 0, danos.size() - 1) * -1;

        if (danoA > danoB)
            ganador = 0;
        else if (danoB < danoA)
            ganador = 1;
        else
            ganador = -1;

        return ganador;
    }
}
