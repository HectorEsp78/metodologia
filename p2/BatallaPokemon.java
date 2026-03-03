
import java.util.Scanner;

class BatallaPokemon {
static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int[] danos = leerArchivoDanos("p2/DatosPokemon1.txt");
        mostrarDatosBatalla(danos);

        System.out.println("\n\nIntroduce el entrnador cuyo daños quiere evaluar (A o B): ");
        String opcion= scanner.nextLine().toUpperCase();
        mostrarDanosNetosEntrenadorRecursivo(opcion, danos);

    }

    public static int[] leerArchivoDanos(String nombreArchivo) {

        try (Scanner scanner = new Scanner(new java.io.File(nombreArchivo))) {
            java.util.List<Integer> lista = new java.util.ArrayList<>();
            while (scanner.hasNextInt()) {
                lista.add(scanner.nextInt());
            }
            int[] danos = new int[lista.size()];
            for (int i = 0; i < lista.size(); i++) {
                danos[i] = lista.get(i);
            }
            return danos;
        } catch (java.io.FileNotFoundException e) {
            System.out.println("Archivo no encontrado: " + nombreArchivo);
            return null;
        }
    }

    public static void mostrarDatosBatalla(int[] danos) {
        if (danos == null) {
            System.out.println("No se pudieron cargar los datos de daños.");
            return;
        } else {
            System.out.println("\n\nDaños de la batalla:");
            for(int i=0; i<danos.length; i++){
                System.out.print(danos[i] + " ");
            }
        }
    }

    public static void mostrarDanosNetosEntrenadorRecursivo(String opcion, int[] danos){
        int danoTotal = 0;
        for(int i=0; i<danos.length; i++){
            if(opcion.equals("A") && danos[i]>0){
                danoTotal += danos[i];
            } else if(opcion.equals("B") && danos[i]<0){
                danoTotal += danos[i];
            }
        }
        if(opcion.equals("B"))
            danoTotal *= -1; // Convertir a positivo para mostrar el daño neto
        System.out.println("Daño neto del entrenador " + opcion + ": " + danoTotal);
    }
}