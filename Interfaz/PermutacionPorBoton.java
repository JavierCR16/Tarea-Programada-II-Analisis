package Interfaz;

import javafx.scene.control.Button;

import java.util.ArrayList;

/**
 * Created by Bryan on 4/30/2017.
 */
public class PermutacionPorBoton {
    public ArrayList<String> permutaciones;
    public ArrayList<int[]> intersecciones = new ArrayList<>();
    public ControladorVentanaJuegoKakuro controlador;
    public Button boton;

    public PermutacionPorBoton(ArrayList<String> s, Button b, ControladorVentanaJuegoKakuro c){
        controlador = c;
        permutaciones = s;
        boton = b;
        int [] coor = controlador.buscarNodoAux(boton);
        intersecciones = c.dependencias(coor[0]+1,coor[1],true, false, intersecciones);
    }
}
