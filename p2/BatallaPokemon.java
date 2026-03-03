
import java.util.Scanner;

class BatallaPokemon{
    
    public static void main(String[] args) {
        int [] danos = leerArchivoDanos("DatosPokemon1.txt");
        System.out.println("Daños leídos: " + java.util.Arrays.toString(danos));
    }

    public static int[] leerArchivoDanos(String nombreArchivo) {
        int[] danos;
            try (Scanner scanner = new Scanner(new java.io.File(nombreArchivo))) {
                int size = scanner.nextInt();
                danos = new int[size];
                for (int i = 0; i < size; i++) {
                    danos[i] = scanner.nextInt();
                }
            } catch (java.io.FileNotFoundException e) {
                System.out.println("Archivo no encontrado: " + nombreArchivo);
                return null;
            }
        return danos;
    }
}