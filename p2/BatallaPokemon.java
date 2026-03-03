
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class BatallaPokemon {

    public static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        List<Integer> danos = leerArchivoDanos("p2/DatosPokemon1.txt");
        System.out.println("Daños leídos: " + danos.toString());

        System.out.println("Iterativo: " + sumaDanosIterativo(danos, "A"));
        System.out.println("Recursivo: " + sumaDanosRecursivo(danos, "A", 0, danos.size()-1));
    }

    public static List<Integer> leerArchivoDanos(String nombreArchivo) {
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

    public static int sumaDanosIterativo(List<Integer> danos, String entrenador){
        int suma = 0;
        int i = 0;
        if(entrenador.toLowerCase().equals("a")){
            while(i < danos.size()){
                if(danos.get(i) > 0){
                    suma += danos.get(i);
                }
                i++;
            }
        } else {
            while(i < danos.size()){
                if(danos.get(i) < 0){
                    suma += danos.get(i)*-1;
                }
                i++;
            }
        }
        
        return suma;
    }

    public static int sumaDanosRecursivo(List<Integer> v, String entrenador, int li, int ls){
        if(ls <= li){
            if((entrenador.toLowerCase().equals("a") && v.get(0) > 0) || (entrenador.toLowerCase().equals("b") && v.get(0) < 0)){
                return v.get(0);
            } else {
                return 0;
            }
        }
        int mitad = (li+ls)/2;
        return sumaDanosRecursivo(v, entrenador, li, mitad-1) + sumaDanosRecursivo(v, entrenador, mitad, ls);
        
        
    }
}