package Interfaz;

import javafx.scene.control.Button;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Bryan on 4/30/2017.
 */
public class PermutacionPorBoton {
    public ArrayList<String> permutaciones;
    public ArrayList<int[]> intersecciones = new ArrayList<>();
    public ControladorVentanaJuegoKakuro controlador;
    public Button boton;
    public boolean clave; //up = true && left = false

    public PermutacionPorBoton(ArrayList<String> s, Button b, ControladorVentanaJuegoKakuro c, int claveArg){
        if(claveArg == 0)
            clave = false;
        else
            clave = true;
        controlador = c;
        permutaciones = s;
        boton = b;
        int [] coor = controlador.buscarNodoAux(boton);
        intersecciones = c.dependencias(coor[0]+1,coor[1],true, false, intersecciones);
    }

    public void compararPerm(ArrayList<String> p){
        ArrayList<String> borrarArray = new ArrayList<>();
        for (String s : p) {
            String[] x = s.split(",");
            String[] aux2;
            List<String> aux = Arrays.asList(x);
            for (String permutacion : permutaciones) {
                aux2 = permutacion.split(",");
                boolean borrar = false;
                for (String s1 : aux2) {
                    if(aux.contains(s1)){
                       borrar = false;
                       break;
                    }
                    else borrar = true;
                }
                if(borrar){
                    borrarArray.add(s);
                    break;
                }
            }
        }
        p.removeAll(borrarArray);
    }
}
