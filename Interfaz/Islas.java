package Interfaz;

import java.util.ArrayList;

/**
 * Created by Bryan on 4/28/2017.
 */
public class Islas {
    public ArrayList<int[]> blancos;
    public ArrayList<int[]> negros;
    public ArrayList<String> permUsadas = new ArrayList<>();
    public Islas(ArrayList<int[]> blancosA, ArrayList<int[]> negrosA){
        blancos = blancosA;
        negros = negrosA;
    }
}
