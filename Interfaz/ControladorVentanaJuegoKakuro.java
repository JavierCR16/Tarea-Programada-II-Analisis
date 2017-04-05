package Interfaz;

import com.sun.rowset.internal.Row;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;

import java.net.URL;
import java.util.ResourceBundle;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

/**
 * Created by Francisco Contreras on 30/03/2017.
 */
public class ControladorVentanaJuegoKakuro implements Initializable {

    @FXML
    public Pane ventanaJuego;

    @FXML
    public GridPane matrizJuego;

    public void initialize(URL fxmlLocations, ResourceBundle resources){
        propiedadesFilaColumna();
        for(int i=0;i<14;i++){
            for(int j=0;j<14;j++){
                Button botonJuego = new Button();
                botonJuego.setMaxSize(60,40);
                matrizJuego.add(botonJuego,i,j,1,1); //

            }
        }

    }


    public void propiedadesFilaColumna(){
        for(ColumnConstraints columnaMatriz: matrizJuego.getColumnConstraints()){
            columnaMatriz.setMinWidth(10);
            columnaMatriz.setPrefWidth(37);
            columnaMatriz.setMaxWidth(176);
        }
        for(RowConstraints filaMatriz: matrizJuego.getRowConstraints()){
            filaMatriz.setMinHeight(10);
            filaMatriz.setPrefHeight(30);
            filaMatriz.setMaxHeight(USE_COMPUTED_SIZE);
        }

    }

}
