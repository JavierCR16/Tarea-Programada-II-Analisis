package Interfaz;
import com.sun.glass.ui.SystemClipboard;
import com.sun.rowset.internal.Row;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import java.io.*;
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

    @FXML
    public Button saveButton;

    @FXML
    public Button cargarKakuro;

    Random rand= new Random();

    public void initialize(URL fxmlLocations, ResourceBundle resources){
        propiedadesFilaColumna();
        for(int i=0;i<14;i++) {
            for (int j = 0; j < 14; j++) {
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
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                guardarKakuro();
            }
        });
        cargarKakuro.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                cargarTablero();
            }
        });
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
        while(contarCuadros()<=82){
            int cuadrosNegros = contarCuadros();
            //System.out.println("Cuadros negros en el tablero: "+cuadrosNegros);
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
               if(!masde1menos10Derecha(fila,columna)){
                   int desplazamiento = desplazamiento(columna);
                   Button botonAyuda = (Button) buscarNodo(fila,desplazamiento);
                   setEstilo(botonAyuda);
               }
           }
           else if (enPrimeras(fila,columna) & columna==0){
               if(!masde1menos10Abajo(fila,columna)){
                   int desplazamiento = desplazamiento(fila);
                   Button botonAyuda = (Button) buscarNodo(desplazamiento,columna);
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
     //System.out.println("Usando fila: " +fila+" usando columna: " + columna);
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
        if(contador>8)
            return false; //
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
        if(contador > 8)
            return false;
        return true;
    }

    public boolean guardarKakuro(){
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Kakuro.txt"), "utf-8"))){
            int columna, fila;
            for (Node node : matrizJuego.getChildren()) {
                fila = matrizJuego.getRowIndex(node).intValue();
                columna = matrizJuego.getColumnIndex(node).intValue();
                if(fila==0){writer.write("\n"+"----------------------------------------------------"+"\n");}
                else {
                    if (node.getStyle().equals("-fx-opacity: 1; -fx-base: #000000;")) {writer.write("0 | ");}// +  " (" + fila + " " + columna + ") | ");}
                    else {writer.write("1 | ");}// + " (" + fila + " " + columna + ") | ");}
                }
            }
            return true;
        }
        catch(IOException e){e.printStackTrace(); return false;}
    }

    public void cargarTablero(){
        try {
            File file = new File("Kakuro.txt");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
                stringBuffer.append("\n");
            }
            fileReader.close();
            System.out.println("Contents of file:");
            System.out.println(stringBuffer.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setEstilo(Button boton){
        boton.setStyle("-fx-opacity: 1; -fx-base: #000000;");
    }

    public int desplazamiento(int FILCOL){
        int desplazamiento = rand.nextInt(10-1+1) +1;
        if(FILCOL+desplazamiento >13)
            desplazamiento= FILCOL+desplazamiento-13;
        return desplazamiento;
    }

    public boolean enPrimeras(int fila, int columna){
        return fila==0|columna==0;
    }

    public void establecerNumeros(){
        Button botonAux = new Button();
        int blancosDerecha=0;
        int blancosAbajo=0;
        int [] rangoDerecha = new int[2];
        int [] rangoAbajo = new int[2];
        for(int i=1;i<14;i++){
            for(int j=1;j<14;j++){
                botonAux=(Button)buscarNodo(i,j);
                if(botonAux.getStyle().equals("-fx-opacity: 1; -fx-base: #000000;")) {
                    if (enUltimas(i, j) & i == 13) {
                        blancosDerecha = verificarBlancos(i, j, 1);
                        rangoDerecha = establecerLimitesSuma(blancosDerecha, blancosAbajo, 1);
                    } else if (enUltimas(i, j) & j == 13) {
                        blancosAbajo = verificarBlancos(i, j, 2);
                        rangoAbajo = establecerLimitesSuma(blancosDerecha, blancosAbajo, 2);
                    } else {
                        blancosDerecha = verificarBlancos(i, j, 1);
                        blancosAbajo = verificarBlancos(i, j, 2);
                        rangoDerecha = establecerLimitesSuma(blancosDerecha, blancosAbajo, 1);
                        rangoAbajo = establecerLimitesSuma(blancosDerecha, blancosAbajo, 2);
                    }
                    if (blancosDerecha == 1 & blancosAbajo != 1) {
                        botonAux = (Button) buscarNodo(i, j);
                        botonAux.setText("\t1\n  1-9");
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
