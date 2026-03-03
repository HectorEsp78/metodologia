
import java.util.Scanner;

class BatallaPokemon{
    
    public static void main(String[] args) {
        // es importante usar la ruta correcta relativa al directorio desde el
        // que se ejecuta el programa; en nuestro caso el fichero está en p2/
        int[] danos = leerArchivoDanos("p2/DatosPokemon1.txt");
        System.out.println("Daños leídos: " + java.util.Arrays.toString(danos));
    }

    public static int[] leerArchivoDanos(String nombreArchivo) {
        // el archivo de ejemplo no tiene un tamaño en la primera posición,
        // por eso iteraremos hasta que no queden enteros. Usamos una lista
        // para poder almacenar una cantidad desconocida de valores.
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
}