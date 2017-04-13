package Interfaz;

import javafx.scene.control.Button;

import java.util.ArrayList;

/**
 * Created by Bryan on 4/13/2017.
 */
public class Dependencias {

    public Button botonNegro;
    public ArrayList<int[]> dependecias = new ArrayList<>();

    public Button getBotonNegro() {
        return botonNegro;
    }

    public void setBotonNegro(Button botonNegro) {
        this.botonNegro = botonNegro;
    }

    public ArrayList<int[]> getDependecias() {
        return dependecias;
    }

    public void setDependecias(ArrayList<int[]> dependecias) {
        this.dependecias = dependecias;
    }
}
