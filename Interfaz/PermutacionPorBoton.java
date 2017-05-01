package Interfaz;

import javafx.scene.control.Button;

import java.util.ArrayList;

/**
 * Created by Bryan on 4/30/2017.
 */
public class PermutacionPorBoton {
    public ArrayList<String> permutaciones;
    public Button boton;

    public PermutacionPorBoton(ArrayList<String> s, Button b){
        permutaciones = s;
        boton = b;
    }
}
