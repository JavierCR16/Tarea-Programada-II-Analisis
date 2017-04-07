package Interfaz;
import com.sun.rowset.internal.Row;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.*;

import java.util.Random;
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

    Random rand= new Random();

    public void initialize(URL fxmlLocations, ResourceBundle resources){
        propiedadesFilaColumna();
        for(int i=0;i<14;i++){
            for(int j=0;j<14;j++){
                Button botonJuego = new Button();
                botonJuego.setMaxSize(80,80);
                botonJuego.setDisable(true);
                botonJuego.setBorder(Border.EMPTY);
                matrizJuego.add(botonJuego,i,j,1,1);// i = columna, j=fila

            }
        }
        generarTablero();


    }


    public void propiedadesFilaColumna(){
        for(ColumnConstraints columnaMatriz: matrizJuego.getColumnConstraints()){
            columnaMatriz.setMinWidth(30);//10
            columnaMatriz.setPrefWidth(57);//37
            columnaMatriz.setMaxWidth(196);//176
        }
        for(RowConstraints filaMatriz: matrizJuego.getRowConstraints()){
            filaMatriz.setMinHeight(30);//10
            filaMatriz.setPrefHeight(50);//30
            filaMatriz.setMaxHeight(USE_COMPUTED_SIZE);
        }
        matrizJuego.setGridLinesVisible(false);

    }

    public void generarTablero(){

        for(int i = 0; i<14;i++){

            Button botonTablero = (Button)buscarNodo(i,0);
            Button botonTablero2= (Button)buscarNodo(0,i);


            botonTablero.setStyle("-fx-opacity: 1;");
            botonTablero.setStyle("-fx-base: #000000;");

            botonTablero2.setStyle("-fx-opacity: 1;");
            botonTablero2.setStyle("-fx-base: #000000;");
        }



        int randomNegros = rand.nextInt(162-1+1)+1;;
        int contador= 0;
        int fila;
        int columna;

        while(contador<randomNegros){
            fila = rand.nextInt(13-1+1)+1;
            columna = rand.nextInt(13-1+1)+1;
            Button botonJuego = (Button) buscarNodo(fila, columna);

           /* if(enPenultimas(fila,columna)) {
                Button botonJuego = (Button) buscarNodo(fila, columna);
                Button botonSiguienteFila = (Button)buscarNodo(13,columna);
                Button botonSiguienteColumna = (Button)buscarNodo(fila,13);

                if(fila==12 &  masde1menos10Derecha(fila,columna)){ //&  masde1menos10Derecha(fila,columna) & botonSiguienteFila.getStyle().equals("-fx-base: #000000;")){ //

                    botonJuego.setStyle("-fx-opacity: 1;");
                    botonJuego.setStyle("-fx-base: #000000;");

                }

                else if(fila==12 &  masde1menos10Derecha(fila,columna) &  masde1menos10Derecha(fila+1,columna)){

                    botonJuego.setStyle("-fx-opacity: 1;");
                    botonJuego.setStyle("-fx-base: #000000;");

                    botonSiguienteFila.setStyle("-fx-opacity: 1;");
                    botonSiguienteFila.setStyle("-fx-base: #000000;");
                }

                else if(columna==12 &  masde1menos10Abajo(fila,columna) & botonSiguienteColumna.getStyle().equals("-fx-base: #000000;")){ //

                    botonJuego.setStyle("-fx-opacity: 1;");
                    botonJuego.setStyle("-fx-base: #000000;");

                }

                else if(columna==12 &  masde1menos10Abajo(fila,columna) &  masde1menos10Abajo(fila,columna+1)){

                    botonJuego.setStyle("-fx-opacity: 1;");
                    botonJuego.setStyle("-fx-base: #000000;");

                    botonSiguienteFila.setStyle("-fx-opacity: 1;");
                    botonSiguienteFila.setStyle("-fx-base: #000000;");
                }



            }*/

           if(enUltimas(fila,columna) & fila==13){
               if(!masde1menos10Derecha(fila,columna)){
                   int desplazamiento = rand.nextInt(10-1+1) +1;
                   if(columna+desplazamiento >13)
                       desplazamiento= columna+desplazamiento-13;

                   Button botonAyuda = (Button) buscarNodo(fila,desplazamiento);

                   botonJuego.setStyle("-fx-opacity: 1;");
                   botonJuego.setStyle("-fx-base: #000000;");
                   botonAyuda.setStyle("-fx-opacity: 1;");
                   botonAyuda.setStyle("-fx-base: #000000;");
               }
               else{
                   botonJuego.setStyle("-fx-opacity: 1;");
                   botonJuego.setStyle("-fx-base: #000000;");
               }

           }

           else if(enUltimas(fila,columna) & columna==13){
               if(!masde1menos10Abajo(fila,columna)){
                   int desplazamiento = rand.nextInt(10-1+1) +1;
                   if(fila+desplazamiento >13)
                       desplazamiento= columna+desplazamiento-13;

                   Button botonAyuda = (Button) buscarNodo(desplazamiento,columna);

                   botonJuego.setStyle("-fx-opacity: 1;");
                   botonJuego.setStyle("-fx-base: #000000;");
                   botonAyuda.setStyle("-fx-opacity: 1;");
                   botonAyuda.setStyle("-fx-base: #000000;");;
               }
               else{
                   botonJuego.setStyle("-fx-opacity: 1;");
                   botonJuego.setStyle("-fx-base: #000000;");
               }

           }

           else if(masde1menos10Derecha(fila,columna) & masde1menos10Abajo(fila,columna)){
               botonJuego.setStyle("-fx-opacity: 1;");
               botonJuego.setStyle("-fx-base: #000000;");

           }

           else if(!masde1menos10Derecha(fila,columna) & !masde1menos10Abajo(fila,columna)){

               int desplazamientoFila = rand.nextInt(10-1+1) +1;
               int desplazamientoColumna = rand.nextInt(10-1+1) +1;
               if(columna+desplazamientoColumna >13)
                   desplazamientoColumna= columna+desplazamientoColumna-13;
               if(fila+desplazamientoFila>13)
                   desplazamientoFila= fila+desplazamientoFila-13;


               Button botonFila = (Button) buscarNodo(fila,desplazamientoColumna);
               Button botonColumna = (Button)buscarNodo(desplazamientoFila,columna);

               botonJuego.setStyle("-fx-opacity: 1;");
               botonJuego.setStyle("-fx-base: #000000;");
               botonFila.setStyle("-fx-opacity: 1;");
               botonFila.setStyle("-fx-base: #000000;");
               botonColumna.setStyle("-fx-opacity: 1;");
               botonColumna.setStyle("-fx-base: #000000;");

           }
           else if(masde1menos10Derecha(fila,columna) & !masde1menos10Abajo(fila,columna)){
               int desplazamientoAbajo = rand.nextInt(10-1+1) +1;
               if(fila+desplazamientoAbajo >13)
                   desplazamientoAbajo= columna+desplazamientoAbajo-13;

               Button botonAyuda = (Button) buscarNodo(desplazamientoAbajo,columna);

               botonJuego.setStyle("-fx-opacity: 1;");
               botonJuego.setStyle("-fx-base: #000000;");
               botonAyuda.setStyle("-fx-opacity: 1;");
               botonAyuda.setStyle("-fx-base: #000000;");;


           }

           else{

               int desplazamiento = rand.nextInt(10-1+1) +1;
               if(columna+desplazamiento >13)
                   desplazamiento= columna+desplazamiento-13;

               Button botonAyuda = (Button) buscarNodo(fila,desplazamiento);

               botonJuego.setStyle("-fx-opacity: 1;");
               botonJuego.setStyle("-fx-base: #000000;");
               botonAyuda.setStyle("-fx-opacity: 1;");
               botonAyuda.setStyle("-fx-base: #000000;");



           }
           /*
            if(masde1menos10Derecha(fila,columna) | masde1menos10Abajo(fila,columna)) {
                Button botonMatriz = (Button) buscarNodo(fila, columna);
                botonMatriz.setStyle("-fx-opacity: 1;");
                botonMatriz.setStyle("-fx-base: #000000;");
            }*/

            contador+=1;

        }





    }

    public boolean enPenultimas(int fila, int columna){
        return fila==12 | columna ==12;
    }

    public boolean enUltimas(int fila, int columna){
        return fila==13 | columna ==13;
    }

    public Node buscarNodo(int fila, int columna) {

        System.out.println("Usando fila: " +fila+" usando columna: " + columna);

        for (Node node : matrizJuego.getChildren()) {
            try {
                if (matrizJuego.getRowIndex(node).intValue() == fila && matrizJuego.getColumnIndex(node).intValue() == columna)
                    return node;

            } catch (Exception e) {
                System.out.println("Nodo nulo fantasma");
            }
        }

        return null;
    }

    public boolean masde1menos10Derecha(int fila,int columna){

        int contador=1;
        for (int i=columna+1;i<14;i++){
            Button botonAux =(Button)buscarNodo(fila,columna);
            if(botonAux.getStyle().equals("-fx-base: #000000;"))
                break;
            contador++;
        }
        if(contador >10)
            return false; //
        return true;
    }

    public boolean masde1menos10Abajo(int fila, int columna){
        int contador=1;
        for (int i=fila+1;i<14;i++){
            Button botonAux =(Button)buscarNodo(fila,columna);
            if(botonAux.getStyle().equals("-fx-base: #000000;"))
                break;
            contador++;
        }
        if(contador >10)
            return false; //
        return true;
    }

}
