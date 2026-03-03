
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class BatallaPokemon {

    public static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        // es importante usar la ruta correcta relativa al directorio desde el
        // que se ejecuta el programa; en nuestro caso el fichero está en p2/
        List<Integer> danos = leerArchivoDanos("p2/DatosPokemon1.txt");
        System.out.println("Daños leídos: " + danos.toString());
    }

    public static List<Integer> leerArchivoDanos(String nombreArchivo) {
        // el archivo de ejemplo no tiene un tamaño en la primera posición,
        // por eso iteraremos hasta que no queden enteros. Usamos una lista
        // para poder almacenar una cantidad desconocida de valores.
        List<Integer> danos = new ArrayList<>();
        try {
            Scanner sc = new Scanner(new File(nombreArchivo));
            while (sc.hasNextInt()) {
                danos.add(sc.nextInt());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return danos;
    }
}