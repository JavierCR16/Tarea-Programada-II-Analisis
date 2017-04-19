package Interfaz;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.net.URL;

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
    public Button resolverKakuro;

    @FXML
    public RadioButton hilosRadio;

    @FXML
    public RadioButton forksRadio;

    @FXML
    public Label labelError;

    @FXML
    public TextField numParalelos;

    Random rand= new Random();

    String datosCarga = "";

    ArrayList<String[]> datosCarga2 = new ArrayList<>();

    ArrayList<int[]> x = new ArrayList<>();

    ArrayList<Button> revisarFila = new ArrayList<>();

    ArrayList<Button> revisarColumna = new ArrayList<>();

    boolean hilos, forks;

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    Date date = new Date();

    public void initialize(URL fxmlLocations, ResourceBundle resources){


        propiedadesFilaColumna();
        for(int i=0;i<14;i++){
            for(int j=0;j<14;j++){
                Button botonJuego = new Button();
                botonJuego.setMaxSize(80,80);
                matrizJuego.add(botonJuego,i,j,1,1);// i = columna, j=fila
            }
        }
        saveButton.setOnAction(event ->{
                guardarKakuro();
        });

        resolverKakuro.setOnAction(event -> {

            hilos=hilosRadio.isSelected();
            forks=forksRadio.isSelected();
            if(hilos && forks){
                System.out.println("Resolver Sin paralelo");
                labelError.setText("Hilos y forks Seleccionados!\nResolviendo con ninguno");
            }
            else if(hilos){
                System.out.println("Resolver con hilos");
                labelError.setText("Hilos Seleccionados!!");
            }
            else if(forks){
                System.out.println("Resolver con Forks");
                labelError.setText("Forks Seleccionados!!");
            }
            else{
                System.out.println("Resolver sin paralelo");
                labelError.setText("Resolviendo con ninguno");
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
            setEstilo(botonTablero);
            setEstilo(botonTablero2);
        }

        int randomNegros = rand.nextInt(162-1+1)+1;;
        int contador= 0;

        int fila;
        int columna;
        while(contarCuadros()<=82){
            fila = rand.nextInt(13-0+1)+0;
            columna = rand.nextInt(13-0+1)+0;
            Button botonJuego = (Button) buscarNodo(fila, columna);
           if (enPrimeras(fila,columna) & fila==0){
               if(!masde1menos10Abajo(fila,columna)){
                   int desplazamiento = desplazamiento(fila);
                   Button botonAyuda = (Button) buscarNodo(desplazamiento,columna);
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
           else {
               int desplazamiento = desplazamiento(columna);
               Button botonAyuda = (Button) buscarNodo(fila, desplazamiento);
               setEstilo(botonJuego);
               setEstilo(botonAyuda);
           }
        }
        verificarTodoBien();
        establecerNumeros();
    }

    public void setDatosCarga(String datos, ArrayList datos2){
        this.datosCarga = datos;
        this.datosCarga2 = datos2;
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
        for (Node node : matrizJuego.getChildren()) {
            try {
                if (matrizJuego.getRowIndex(node).intValue() == fila && matrizJuego.getColumnIndex(node).intValue() == columna)
                    return node;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public boolean masde1menos10Derecha(int fila,int columna){
        int contador=0;

        for (int i=columna+1;i<14;i++){
            Button botonAux =(Button)buscarNodo(fila,i);
            if(botonAux.getStyle().equals("-fx-opacity: 1; -fx-base: #000000;"))
                break;
            contador++;
        }
       // System.out.println("Blancos Derecha: "+contador);
        if(contador >9)
            return false;
        return true;
    }

    public boolean masde1menos10Abajo(int fila, int columna){
        int contador=0;
        for (int i=fila+1;i<14;i++){
            Button botonAux =(Button)buscarNodo(i,columna);
            if(botonAux.getStyle().equals("-fx-opacity: 1; -fx-base: #000000;"))//-fx-base: #000000;
                break;
            contador+=1;
        }
      //  System.out.println("Blancos Abajo: "+contador);
        if(contador >9)
            return false;
        return true;
    }

    public boolean guardarKakuro(){
        String nombreArchivo = "Kakuro"+dateFormat.format(date)+".txt";

        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(nombreArchivo), "utf-8"))){
            for(int i=0;i<14;i++){
                for(int j=0;j<14;j++){
                    Button botonAuxiliar = (Button)buscarNodo(i,j);
                    if(j==0){writer.write("\n"+"--------------------------------------------------------"+"\n 0 | ");}
                    else {
                        if (botonAuxiliar.getStyle().equals("-fx-opacity: 1; -fx-base: #000000;")) {
                            writer.write("0 | ");
                        }
                        else {
                            writer.write("1 | ");
                        }
                    }
                }
            }
            return true;
        }
        catch(IOException e){e.printStackTrace(); return false;}
    }

    public void setEstilo(Button boton){
        boton.setStyle("-fx-opacity: 1; -fx-base: #000000;");
    }

    public int desplazamiento(int FILCOL){
        int desplazamiento = rand.nextInt(9-1+1) +1;
        while(desplazamiento==0){
            desplazamiento = rand.nextInt(9-1+1) +1;
        }
        desplazamiento = FILCOL+desplazamiento;
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
        for(int i=0;i<14;i++){
            for(int j=0;j<14;j++){
                botonAux=(Button)buscarNodo(i,j);
                if(botonAux.getStyle().equals("-fx-opacity: 1; -fx-base: #000000;")) {
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
                        if(filaColumnaSola(i,j,2))
                            botonAux.setText("       "+(rand.nextInt(rangoDerecha[1]-rangoDerecha[0]+1) +rangoDerecha[0]));
                        else{
                            botonAux.setText("       *");
                            revisarColumna.add(botonAux);
                        }
                    }
                    else if(blancosAbajo !=0 & blancosDerecha ==1){ //hay más de una para abajo y una para la derecha
                        if(filaColumnaSola(i,j,1))
                            botonAux.setText("       1-9\n"+(rand.nextInt(rangoAbajo[1]-rangoAbajo[0]+1) +rangoAbajo[0]));
                        else{
                            botonAux.setText("       1-9\n"+(rand.nextInt(rangoAbajo[1]-rangoAbajo[0]+1) +rangoAbajo[0]));
                            revisarFila.add(botonAux);
                        }

                    }
                    else if(blancosAbajo ==1 & blancosDerecha !=0){ //hay más de una para la derecha y una para abajo
                        if(filaColumnaSola(i,j,2))
                            botonAux.setText("       "+(rand.nextInt(rangoDerecha[1]-rangoDerecha[0]+1) +rangoDerecha[0])+"\n1-9");
                        else{
                            botonAux.setText("       *\n1-9");
                            revisarColumna.add(botonAux);
                        }
                    }
                    else if(blancosAbajo !=0 & blancosDerecha!=0){ //hay mas de una para abajo y mas de una para la derecha
                        if(filaColumnaSola(i,j,1) && filaColumnaSola(i,j,2)){
                            botonAux.setText("       "+(rand.nextInt(rangoDerecha[1]-rangoDerecha[0]+1) +rangoDerecha[0])+"\n"+(rand.nextInt(rangoAbajo[1]-rangoAbajo[0]+1) +rangoAbajo[0]));
                        }
                        else{
                            if(filaColumnaSola(i,j,1)){
                                botonAux.setText("       *\n"+(rand.nextInt(rangoAbajo[1]-rangoAbajo[0]+1) +rangoAbajo[0]));
                                revisarColumna.add(botonAux);
                            }
                            if(filaColumnaSola(i,j,2)){
                                botonAux.setText("       "+(rand.nextInt(rangoDerecha[1]-rangoDerecha[0]+1) +rangoDerecha[0])+"\n"+(rand.nextInt(rangoAbajo[1]-rangoAbajo[0]+1) +rangoAbajo[0]));
                                revisarFila.add(botonAux);
                            }
                            else{
                                botonAux.setText("       *\n"+(rand.nextInt(rangoAbajo[1]-rangoAbajo[0]+1) +rangoAbajo[0]));
                                revisarColumna.add(botonAux);
                                revisarFila.add(botonAux);
                            }
                        }
                    }
                    else if(blancosAbajo !=0 & blancosDerecha ==0){ // hay mas de una para abajo y ninguna para la derecha
                        if(filaColumnaSola(i,j,1))
                            botonAux.setText("\n"+(rand.nextInt(rangoAbajo[1]-rangoAbajo[0]+1) +rangoAbajo[0]));
                        else{
                            botonAux.setText("\n"+(rand.nextInt(rangoAbajo[1]-rangoAbajo[0]+1) +rangoAbajo[0]));
                            revisarFila.add(botonAux);
                        }
                    }
                }
            }
        }
        for (Button button : revisarFila) {
            //button.setStyle("-fx-opacity: 1; -fx-base: #FF0000;");
            int valor = Integer.parseInt(button.getText().replace("       ", "").replace("1-9", "").replace("\n", "").replace("*",""));
            int[] coordenadasButton = buscarNodoAux(button);
            int cont = verificarBlancos(coordenadasButton[0], coordenadasButton[1], 2);
            ArrayList<String> permutacionesPosibles = new ArrayList<>();
            String[] elem = {"1","2","3","4","5","6","7","8","9"};
            Permutaciones(elem, "", cont, 9, permutacionesPosibles, valor);
            System.out.println("Valor es: "+valor);
            for (String permutacionesPosible : permutacionesPosibles) {
                System.out.println(permutacionesPosible);
            }
        }/*
        for (Button button : revisarColumna) {
            //button.setStyle("-fx-opacity: 1; -fx-base: #00FF00;");
        }*/

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

    public void verificarTodoBien(){
        Button botonMatriz;
        for(int i=0;i<14;i++){
            for (int j=0;j<14;j++){
                botonMatriz= (Button) buscarNodo(i,j);
                if(botonMatriz.getStyle().equals("-fx-opacity: 1; -fx-base: #000000;")) {
                    if (!masde1menos10Abajo(i, j) & !masde1menos10Derecha(i, j)) {
                        int desplazamientoFila = desplazamiento(i);
                        int desplazamientoColumna = desplazamiento(j);
                        Button botonFila = (Button) buscarNodo(i, desplazamientoColumna);
                        Button botonColumna = (Button) buscarNodo(desplazamientoFila, j);
                        setEstilo(botonMatriz);
                        setEstilo(botonFila);
                       setEstilo(botonColumna);

                        System.out.println("Se hizo cambio en los botones estaba malo el boton" + i + "," + j + " se pinto el boton: " + i + "," + desplazamientoColumna + " y el boton" + desplazamientoFila + "," + j);

                    } else if (!masde1menos10Abajo(i, j)) {
                        int desplazamientoFila = desplazamiento(i);
                        Button botonaPintar = (Button) buscarNodo(desplazamientoFila, j);
                        setEstilo(botonaPintar);
                        System.out.println("xd");
                        System.out.println("Se hizo cambio en los botones estaba malo el boton" + i + "," + j + " se pinto el boton: " + desplazamientoFila + "," + j);
                    } else if (!masde1menos10Derecha(i, j)) {
                        int desplazamientoColumna = desplazamiento(j);
                        Button botonaPintar = (Button) buscarNodo(i, desplazamientoColumna);
                        setEstilo(botonaPintar);
                        System.out.println("Se hizo cambio en los botones estaba malo el boton" + i + "," + j + " se pinto el boton: " + i + "," + desplazamientoColumna);
                    }
                }
            }
        }
    }

    public void Permutaciones(String[] elem, String restricciones, int n, int r, ArrayList<String> permutaciones, int valor) {
        if (n == 0) {
            int suma=0;
            String[] array = restricciones.split(",");
            for(int i=0; i<array.length ; i++){
                suma+=Integer.parseInt(array[i]);
            }
            if(suma==valor)
                permutaciones.add(restricciones);
        } else {
            for (int i = 0; i < r; i++) {
                if (!restricciones.contains(elem[i])) { // Controla que no haya repeticiones
                    Permutaciones(elem, restricciones + elem[i] +"," , n - 1, r,permutaciones, valor);
                }
            }
        }
    }

    public void cargarTablero(){
        int cont=0, fila = -1, columna=0;
        while(cont<196){
            if(cont%14==0){fila++; columna=0;}
            Button botonJuego = (Button) buscarNodo(fila, columna);
            if(datosCarga.charAt(cont)=='0'){
                setEstilo(botonJuego);
            }
            columna++;
            cont++;
        }
        for (String[] clave : this.datosCarga2) {
            cargarTexto(clave);
        }
    }

    public void cargarTexto(String[] clave){
        String tmp;
        tmp = clave[0];
        String[] array = tmp.split(",");
        int fila = Integer.parseInt(array[0]);
        int columna = Integer.parseInt(array[1]);
        String down="", left="";
        if(!clave[1].equals("0"))
            down = "\n"+clave[1];
        if(!clave[2].equals("0"))
            left="       "+clave[2];
        tmp = left+down;
        Button botonTablero = (Button)buscarNodo(fila,columna);
        botonTablero.setText(tmp);
    }

    public boolean filaColumnaSola(int fila,int columna,int opcion){
        int  contador =1;
        boolean solo = true;
        Button botonAuxiliarAdelante;
        Button botonAuxiliarAtras;
        Button botonDetenerse;
        switch(opcion) {
            case 1: //ver si hay columna sola
                for(int i = fila+1;i<14;i++){
                    botonDetenerse = (Button)buscarNodo(i,columna);
                    if(botonDetenerse.getStyle().equals("-fx-opacity: 1; -fx-base: #000000;"))
                        break;

                    if(columna!=13) {
                        if(!arribaAbajoNulo(i,columna+1))
                            solo=false;
                    }
                    if(columna!=0){
                        if(!arribaAbajoNulo(i,columna-1))
                            solo =false;
                    }

                }
                break;
            case 2: //ver si hay fila sola
                for(int j= columna+1;j<14;j++){
                    botonDetenerse = (Button)buscarNodo(fila,j);
                    if(botonDetenerse.getStyle().equals("-fx-opacity: 1; -fx-base: #000000;"))
                        break;

                    if(fila!=13) {
                        if(!arribaAbajoNulo(fila+1,j))
                            solo=false;
                    }
                    if(fila!=0){
                        if(!arribaAbajoNulo(fila-1,j))
                            solo =false;
                    }
                }
                break;

        }
        return solo;
    }

    public boolean arribaAbajoNulo(int fila, int columna){

        Button botonBuscar = (Button)buscarNodo(fila,columna);

      return botonBuscar.getStyle().equals("-fx-opacity: 1; -fx-base: #000000;");
    }

    public int [] buscarNodoAux(Button boton){
        int [] coordenadas = new int[2];
        for (Node node : matrizJuego.getChildren()) {
            try {
                if(boton.equals(node)){
                    coordenadas[0]=matrizJuego.getRowIndex(node).intValue();
                    coordenadas[1] = matrizJuego.getColumnIndex(node).intValue();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return coordenadas;
    }

    public void intersecciones(int filaInicial, int columnaInicial, ArrayList<int[]> array, Button botonActual){
        boolean contiuar = true;
        int[] siguiente = {filaInicial, columnaInicial};
        if(!array.isEmpty())
            for (int[] ints : array) {
                if(Arrays.equals(ints, siguiente))
                    contiuar=false;
            }
        if(!botonActual.getStyle().equals("-fx-opacity: 1; -fx-base: #000000;") && contiuar){
            array.add(buscarNodoAux(botonActual));
            if(enUltimas(filaInicial,columnaInicial) && filaInicial==13 && columnaInicial==13) {
                intersecciones(filaInicial, columnaInicial - 1, array, (Button) buscarNodo(filaInicial, columnaInicial - 1));
                intersecciones(filaInicial - 1, columnaInicial, array, (Button) buscarNodo(filaInicial - 1, columnaInicial));
            }
            else if(enUltimas(filaInicial,columnaInicial) && columnaInicial==13){
                intersecciones(filaInicial+1, columnaInicial, array, (Button)buscarNodo(filaInicial+1, columnaInicial));
                intersecciones(filaInicial-1, columnaInicial, array, (Button)buscarNodo(filaInicial-1, columnaInicial));
                intersecciones(filaInicial, columnaInicial-1, array, (Button)buscarNodo(filaInicial, columnaInicial-1));
            }
            else if(enUltimas(filaInicial,columnaInicial) && filaInicial==13){
                intersecciones(filaInicial, columnaInicial + 1, array, (Button) buscarNodo(filaInicial, columnaInicial + 1));
                intersecciones(filaInicial, columnaInicial - 1, array, (Button) buscarNodo(filaInicial, columnaInicial - 1));
                intersecciones(filaInicial - 1, columnaInicial, array, (Button) buscarNodo(filaInicial - 1, columnaInicial));
            }
            else if(enPrimeras(filaInicial, columnaInicial) && filaInicial==0){
                intersecciones(filaInicial, columnaInicial + 1, array, (Button) buscarNodo(filaInicial, columnaInicial + 1));
                intersecciones(filaInicial+1, columnaInicial, array, (Button)buscarNodo(filaInicial+1, columnaInicial));
                intersecciones(filaInicial, columnaInicial-1, array, (Button)buscarNodo(filaInicial, columnaInicial-1));
            }
            else if(enPrimeras(filaInicial, columnaInicial) && columnaInicial==0) {
                intersecciones(filaInicial, columnaInicial + 1, array, (Button) buscarNodo(filaInicial, columnaInicial + 1));
                intersecciones(filaInicial + 1, columnaInicial, array, (Button) buscarNodo(filaInicial + 1, columnaInicial));
                intersecciones(filaInicial - 1, columnaInicial, array, (Button) buscarNodo(filaInicial - 1, columnaInicial));
            }
            else{
                intersecciones(filaInicial, columnaInicial + 1, array, (Button) buscarNodo(filaInicial, columnaInicial + 1));
                intersecciones(filaInicial + 1, columnaInicial, array, (Button) buscarNodo(filaInicial + 1, columnaInicial));
                intersecciones(filaInicial - 1, columnaInicial, array, (Button) buscarNodo(filaInicial - 1, columnaInicial));
                intersecciones(filaInicial, columnaInicial-1, array, (Button)buscarNodo(filaInicial, columnaInicial-1));
            }
        }
    }

    public ArrayList<int[]> dependencias(int fila, int columna, boolean right, boolean up, ArrayList<int[]> respuesta){
        Button botonActual = (Button) buscarNodo(fila, columna);
        boolean continuar = fila <= 13 && columna <= 13 && fila > 0 && columna >= 0;
        boolean continuarRight = fila <= 13 && columna <= 13 && fila >= 0 && columna > 0;
        int tmpFila = fila;
        int tmpColumna = columna;
        if(continuar && up && !botonActual.getStyle().equals("-fx-opacity: 1; -fx-base: #000000;")){//buscar dependencias hacia arriba
            while(!botonActual.getStyle().equals("-fx-opacity: 1; -fx-base: #000000;")){//va buscando un boton negro en la columna
                tmpFila--;
                botonActual = (Button) buscarNodo(tmpFila, tmpColumna);
            }
            botonActual = (Button) buscarNodo(fila, columna);//si encuentra boton negro significa que la casilla en la que esta dependera de el
            botonActual.setText(tmpFila+","+tmpColumna);
            int[] aux = {tmpFila,tmpColumna};
            respuesta.add(aux);
            return dependencias(fila, columna+1, false, true, respuesta);//por recursividad se va buscando por toda la fila o columna
        }
        if(continuarRight && right && !botonActual.getStyle().equals("-fx-opacity: 1; -fx-base: #000000;")){//buscar dependencia hacia abajo
            while(!botonActual.getStyle().equals("-fx-opacity: 1; -fx-base: #000000;")){//va buscando un boton negro en la fila
                tmpColumna--;
                botonActual = (Button) buscarNodo(tmpFila, tmpColumna);
            }
            botonActual = (Button) buscarNodo(fila, columna);
            botonActual.setText(tmpFila+","+tmpColumna);
            int[] aux = {tmpFila,tmpColumna};
            respuesta.add(aux);
            return dependencias(fila+1, columna, true, false, respuesta);
        }
        return respuesta;
    }
}