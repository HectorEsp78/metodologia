public class Baldosa {

    int id, altura, longitud;
    boolean girada, cabe, colocada;

    public Baldosa(int id, int altura, int longitud){
        this.id = id;
        this.altura = altura;
        this.longitud = longitud;
        girada = false;
        cabe = true;
    }

    public int getAltura(){
        return altura;
    }
    public int getLongitud(){
        return longitud;
    }

    public int getId(){
        return id;
    }

    public void girar(){
        girada = true;
        int aux = altura;
        altura = longitud;
        longitud = aux;
    }

    public boolean esGirada(){
        return girada;
    }

    public boolean getCabe(){
        return cabe;
    }

    public void noCabe(){
        cabe = false;
    }

    public boolean getColocada(){
        return colocada;
    }

    public void colocar(){
        colocada = true;
    }

    public int area(){
        return altura*longitud;
    }
}
