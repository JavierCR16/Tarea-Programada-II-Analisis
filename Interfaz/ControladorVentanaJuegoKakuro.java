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
                 botonJuego.setOnMouseEntered(event -> {

                     //botonJuego.setText("Profe \n playo");
                    //if(botonJuego.getStyle().equals("-fx-base: #000000;"))
                     //   botonJuego.setStyle("-fx-focus-color: transparent; -fx-opacity: 1; -fx-base: #000000;  ");

                   // else{
                     System.out.println(botonJuego.getStyle());
                   //  }


                 });

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

            setEstilo(botonTablero);
            setEstilo(botonTablero2);
        }



        int randomNegros = rand.nextInt(162-1+1)+1;;
        int contador= 0;
        int fila;
        int columna;

        while(contarCuadros()<=82){
            int cuadrosNegros = contarCuadros();
            System.out.println("Cuadros negros en el tablero: "+cuadrosNegros);

            fila = rand.nextInt(13-0+1)+0;
            columna = rand.nextInt(13-0+1)+0;
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

           if (enPrimeras(fila,columna) & fila==0){

               if(!masde1menos10Abajo(fila,columna)){
                   int desplazamiento = desplazamiento(fila);

                   Button botonAyuda = (Button) buscarNodo(desplazamiento,fila);

                   setEstilo(botonAyuda);
               }
           }

           else if (enPrimeras(fila,columna) & columna==0){ //FIX THAT
               if(!masde1menos10Derecha(fila,columna)){ //Antes abajo
                   int desplazamiento = desplazamiento(columna);

                   Button botonAyuda = (Button) buscarNodo(fila,desplazamiento);
                   setEstilo(botonAyuda);
               }
           }

          else if(enUltimas(fila,columna) & fila==13){

              if(!masde1menos10Derecha(fila,columna)){
                  int desplazamiento = desplazamiento(columna);

                  Button botonAyuda = (Button) buscarNodo(fila,desplazamiento);

                  setEstilo(botonJuego);
                  setEstilo(botonAyuda);
              }
              else{
                  setEstilo(botonJuego);
              }

           }

           else if(enUltimas(fila,columna) & columna==13){

               if(!masde1menos10Abajo(fila,columna)){
                   int desplazamiento = desplazamiento(fila) ;

                    Button botonAyuda = (Button) buscarNodo(desplazamiento,columna);
                    setEstilo(botonJuego);
                    setEstilo(botonAyuda);
               }
               else{
                   setEstilo(botonJuego);

               }

           }

           else if(masde1menos10Derecha(fila,columna) & masde1menos10Abajo(fila,columna)){
               setEstilo(botonJuego);
           }

           else if(!masde1menos10Derecha(fila,columna) & !masde1menos10Abajo(fila,columna)){

               int desplazamientoFila = desplazamiento(fila);
               int desplazamientoColumna = desplazamiento(columna);


               Button botonFila = (Button) buscarNodo(fila,desplazamientoColumna);
               Button botonColumna = (Button)buscarNodo(desplazamientoFila,columna);

               setEstilo(botonJuego);
               setEstilo(botonFila);
               setEstilo(botonColumna);

           }
           else if(masde1menos10Derecha(fila,columna) & !masde1menos10Abajo(fila,columna)){
               int desplazamientoAbajo = desplazamiento(fila);

               Button botonAyuda = (Button) buscarNodo(desplazamientoAbajo,columna);

               setEstilo(botonJuego);
               setEstilo(botonAyuda);

           }
           else{

               int desplazamiento = desplazamiento(columna);
               Button botonAyuda = (Button) buscarNodo(fila,desplazamiento);

               setEstilo(botonJuego);
               setEstilo(botonAyuda);

           }
        }
        establecerNumeros();
    }

    public boolean enPenultimas(int fila, int columna){
        return fila==12 | columna ==12;
    }

    public int contarCuadros(){

        int contador=0;

        for(int i=1;i<14;i++){
            for(int j=1;j<14;j++){
                Button botonAuxiliar = (Button)buscarNodo(i,j);
                if(botonAuxiliar.getStyle().equals("-fx-opacity: 1; -fx-base: #000000;"))
                    contador+=1;

            }
        }

        return contador;
    }

    public boolean enUltimas(int fila, int columna){
        return fila==13 | columna ==13;
    }

    public Node buscarNodo(int fila, int columna) {

     //   System.out.println("Usando fila: " +fila+" usando columna: " + columna);

        for (Node node : matrizJuego.getChildren()) {
            try {
                if (matrizJuego.getRowIndex(node).intValue() == fila && matrizJuego.getColumnIndex(node).intValue() == columna)
                    return node;

            } catch (Exception e) {
           //     System.out.println("Nodo nulo fantasma");
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
        if(contador >9)
            return false;
        return true;
    }

    public boolean masde1menos10Abajo(int fila, int columna){
        int contador=1;
        for (int i=fila+1;i<14;i++){
            Button botonAux =(Button)buscarNodo(fila,columna);
            if(botonAux.getStyle().equals("-fx-opacity: 1; -fx-base: #000000;"))//-fx-base: #000000;
                break;
            contador++;
        }
        if(contador >9)
            return false;
        return true;
    }

    public void setEstilo(Button boton){


        boton.setStyle("-fx-opacity: 1; -fx-base: #000000;");


    }

    public int desplazamiento(int FILCOL){

        int desplazamiento = rand.nextInt(9-1+1) +1;//VERIFICAR
        if(FILCOL+desplazamiento >13)
            desplazamiento= FILCOL+desplazamiento-13;
        return desplazamiento;
    }

    public boolean enPrimeras(int fila, int columna){

        return fila==0|columna==0;
    }

    public void establecerNumeros(){
        //MAE NO SÉ SI QUITANDO TODOxd Y DEJAR EL ULTIMO ELSE, HACE LO MISMO QUE TODOS LOS IF ANTERIORES, EMPEZANDO DESDE IF (enUltimas(i, j) & i == 13)


        Button botonAux = new Button();
        int blancosDerecha=0;
        int blancosAbajo=0;
        int [] rangoDerecha = new int[2];
        int [] rangoAbajo = new int[2];

        for(int i=0;i<14;i++){
            for(int j=0;j<14;j++){
                botonAux=(Button)buscarNodo(i,j);
                if(botonAux.getStyle().equals("-fx-opacity: 1; -fx-base: #000000;")) {
                    if (enUltimas(i, j) & i == 13) {
                        blancosDerecha = verificarBlancos(i, j, 1);
                        rangoDerecha = establecerLimitesSuma(blancosDerecha, blancosAbajo, 1);
                        if(blancosDerecha==1)
                            botonAux.setText("       1-9");
                        else if(blancosDerecha!=0)
                            botonAux.setText("       "+(rand.nextInt(rangoDerecha[1]-rangoDerecha[0]+1) +rangoDerecha[0]));

                    } else if (enUltimas(i, j) & j == 13) {
                        blancosAbajo = verificarBlancos(i, j, 2);
                        rangoAbajo = establecerLimitesSuma(blancosDerecha, blancosAbajo, 2);

                        if(blancosAbajo==1)
                            botonAux.setText("\n1-9");
                        else if(blancosAbajo!=0)
                            botonAux.setText("\n"+(rand.nextInt(rangoAbajo[1]-rangoAbajo[0]+1) +rangoAbajo[0]));

                    } else {
                        blancosDerecha = verificarBlancos(i, j, 1);
                        blancosAbajo = verificarBlancos(i, j, 2);
                        rangoDerecha = establecerLimitesSuma(blancosDerecha, blancosAbajo, 1);
                        rangoAbajo = establecerLimitesSuma(blancosDerecha, blancosAbajo, 2);

                        // No pongo condicion de si para cualquier lado es nulo porque al final no va a hacer nada

                        if(blancosDerecha==0 & blancosAbajo == 1){ //solo hay una casilla hacia abajo y a la derecha nulo
                            botonAux.setText("\n1-9");
                        }
                        else  if(blancosDerecha==1 & blancosAbajo == 0){ //solo hay una casilla hacia la derecha y hacia abajo nulo
                            botonAux.setText("       1-9");
                        }

                        else if(blancosDerecha==1 & blancosAbajo ==1){ //solo hay una casilla para arriba y para abajo
                            botonAux.setText("       1-9\n1-9");
                        }
                        else if(blancosAbajo ==0 & blancosDerecha !=0){ // hay mas de una a la derecha y ninguna para abajo
                            botonAux.setText("       "+(rand.nextInt(rangoDerecha[1]-rangoDerecha[0]+1) +rangoDerecha[0]));
                        }
                        else if(blancosAbajo !=0 & blancosDerecha ==1){ //hay más de una para abajo y una para la derecha
                            botonAux.setText("       1-9\n"+(rand.nextInt(rangoAbajo[1]-rangoAbajo[0]+1) +rangoAbajo[0]));
                        }
                        else if(blancosAbajo ==1 & blancosDerecha !=0){ //hay más de una para la derecha y una para abajo
                            botonAux.setText("       "+(rand.nextInt(rangoDerecha[1]-rangoDerecha[0]+1) +rangoDerecha[0])+"\n1-9");
                        }
                        else if(blancosAbajo !=0 & blancosDerecha!=0){ //hay mas de una para abajo y mas de una para la derecha
                            botonAux.setText("       "+(rand.nextInt(rangoDerecha[1]-rangoDerecha[0]+1) +rangoDerecha[0])+"\n"+(rand.nextInt(rangoAbajo[1]-rangoAbajo[0]+1) +rangoAbajo[0]));
                        }

                        else if(blancosAbajo !=0 & blancosDerecha ==0){ // hay mas de una para abajo y ninguna para la derecha
                            botonAux.setText("\n"+(rand.nextInt(rangoAbajo[1]-rangoAbajo[0]+1) +rangoAbajo[0]));
                        }
                    }
                }
            }
        }
    }

    public int verificarBlancos(int fila, int columna, int modalidad){
        int contador =0;

        Button botonFinal;
        switch (modalidad){
            case 1:
                //columna = columna+1;
                while(columna!=13){ //Hacia Derecha
                    columna++;
                    botonFinal = (Button) buscarNodo(fila,columna);

                    if(botonFinal.getStyle().equals("-fx-opacity: 1; -fx-base: #000000;"))
                        break;
                    contador++;
                }
                break;
            case 2:
               // fila = fila+1;
                while(fila!=13){ //Hacia Abajo
                    fila++;
                    botonFinal = (Button) buscarNodo(fila,columna);

                    if(botonFinal.getStyle().equals("-fx-opacity: 1; -fx-base: #000000;"))
                        break;
                    contador++;
                }
                break;
        }

        return contador;
    }

    public int[] establecerLimitesSuma(int blancosDerecha,int blancosAbajo,int modalidad){
        int [] rango = new int[2];
        int sumaMenor = 0;
        int sumaMayor = 0;
        switch(modalidad){

            case 1:
                for (int i=1;i<=blancosDerecha;i++)
                    sumaMenor+=i;
                rango[0]=sumaMenor;
                for(int j=9; j> 9-blancosDerecha;j--)
                    sumaMayor+=j;
                rango[1]=sumaMayor;
                break;
            case 2:
                for (int i=1;i<=blancosAbajo;i++)
                    sumaMenor+=i;
                rango[0]=sumaMenor;
                for(int j=9; j> 9-blancosAbajo;j--)
                    sumaMayor+=j;
                rango[1]=sumaMayor;
                break;

        }

        return rango;
    }

}
