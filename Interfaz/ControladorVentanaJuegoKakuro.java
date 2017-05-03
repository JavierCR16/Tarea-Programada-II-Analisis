package Interfaz;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import java.io.*;
import java.lang.reflect.Array;
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

    String[] numeros = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};

    ArrayList<String[]> datosCarga2 = new ArrayList<>();

    ArrayList<Button> revisarFila = new ArrayList<>();

    ArrayList<Button> revisarColumna = new ArrayList<>();

    ArrayList<Button> negros = new ArrayList<>();

    ArrayList<Button> matriz = new ArrayList<>();

    ArrayList<Islas> islasArray = new ArrayList<>();

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
                matriz.add(botonJuego);/*
                botonJuego.setOnAction(event -> {
                    /*
                    for (int[] ints : tmp) {
                        Button yh = (Button) buscarNodo(ints[0], ints[1]);
                        yh.setStyle("-fx-opacity: 1; -fx-base: #550000;");
                    }
                });*/
            }
        }
        saveButton.setOnAction(event ->{
                guardarKakuro();
        });

        resolverKakuro.setOnAction(event -> {

            hilos=hilosRadio.isSelected();
            forks=forksRadio.isSelected();
            if(hilos && forks){
                //System.out.println("Resolver Sin paralelo");
                labelError.setText("Hilos y forks Seleccionados!\nResolviendo con ninguno");
            }
            else if(hilos){
                //System.out.println("Resolver con hilos");
                labelError.setText("Hilos Seleccionados!!");
            }
            else if(forks){
                //System.out.println("Resolver con Forks");
                labelError.setText("Forks Seleccionados!!");
            }
            else{
                //System.out.println("Resolver sin paralelo");
                resolver();
                labelError.setText("Resolviendo con ninguno");
            }
        });
    }

    public void resolver(){
        ArrayList<Button> negrosConClave = new ArrayList<>();
        for (Button negro : negros) {
            if(!negro.getText().equals("")){
                negrosConClave.add(negro);
            }
        }
        int[] coordenadas;
        while(true) {
            ArrayList<int[]> isla = new ArrayList<>();
            ArrayList<int[]> islaAlrededores = null;
            Button primerBlanco = primerBlanco();
            if(primerBlanco==null) break;
            coordenadas = buscarNodoAux(primerBlanco);
            intersecciones(coordenadas[0], coordenadas[1], isla, primerBlanco);
            islaAlrededores=pintarBordes(isla);
            Islas nueva = new Islas(isla, islaAlrededores);
            islasArray.add(nueva);
            for (int[] ints : isla) {
                Button x = (Button) buscarNodo(ints[0], ints[1]);
                x.setText("isla"+islasArray.size());
            }
        }
        clearBlancos();
        ArrayList<HilosResolver> hilos = new ArrayList<>();
        int c =0;
        for (Islas islas : islasArray) {
            c++;
            HilosResolver hilo = new HilosResolver(islas, this, c);
            hilo.run();
            hilos.add(hilo);
        }
        //hilos.get(0).run();
        //hilos.get(1).run();
        //hilos.get(2).run();
        for (Button button : negrosConClave) {
            coordenadas = buscarNodoAux(button);
            String[] texto = button.getText().replace("       ", "").split("\n");
            if(!texto[1].equals("-") && filaColumnaSola(coordenadas[0], coordenadas[1], 1)){
                //columnaSola
                int valorClave=0;
                if(!texto[1].equals("1-9")) {
                    valorClave = Integer.parseInt(texto[1]);
                    ArrayList<String> permutaciones = new ArrayList<>();
                    int blancos = verificarBlancos(coordenadas[0], coordenadas[1], 2);
                    Permutaciones(numeros, "", blancos, 9, permutaciones, valorClave);
                    printearColumna(coordenadas[0], coordenadas[1], permutaciones.get(0), blancos);
                }
                else{
                    Button actual = (Button) buscarNodo(coordenadas[0]+1, coordenadas[1]);
                    actual.setText(rand.nextInt(9)+1 +"");
                }
            }
            if (!texto[0].equals("-") && filaColumnaSola(coordenadas[0], coordenadas[1], 2)){
                int valorClave=0;
                if(!texto[0].equals("1-9")) {
                    valorClave = Integer.parseInt(texto[0]);
                    ArrayList<String> permutaciones = new ArrayList<>();
                    int blancos = verificarBlancos(coordenadas[0], coordenadas[1], 1);
                    Permutaciones(numeros, "", blancos, 9, permutaciones, valorClave);
                    printearFila(coordenadas[0], coordenadas[1], permutaciones.get(0), blancos);
                }
            }
        }
    }

    public Button primerBlanco(){
        Button actual = null;
        int[] coordenadas = {1,1};
        while(coordenadas[0]<=13 && coordenadas[1]<=13){
            actual = (Button) buscarNodo(coordenadas[0], coordenadas[1]);
            if(actual.getText().equals("") && !actual.getStyle().equals("-fx-opacity: 1; -fx-base: #000000;")){
                int blancosDerecha = verificarBlancos(coordenadas[0], coordenadas[1], 1);
                int blancosAbajo = verificarBlancos(coordenadas[0], coordenadas[1], 2);
                if(blancosAbajo != 0 || blancosDerecha != 0){
                    return actual;
                }
                else if(coordenadas[1]+1<=13){
                    coordenadas[1]+=1;
                }
                else{
                    coordenadas[0]+=1;
                    coordenadas[1]=0;
                }
            }
            else if(coordenadas[1]+1<=13){
                coordenadas[1]+=1;
            }
            else{
                coordenadas[0]+=1;
                coordenadas[1]=0;
            }
        }
        return null;
    }

    public void printearColumna(int fila, int columna, String permutacion, int blancos){
        fila++;
        String[] array = permutacion.split(",");
        int cont = 0;
        while(blancos!=0){
            Button actual = (Button) buscarNodo(fila, columna);
            actual.setText(array[cont]);
            cont++;
            blancos--;
            fila++;
        }
    }

    public void printearFila(int fila, int columna,  String permutacion, int blancos){
        columna++;
        String[] array = permutacion.split(",");
        int cont = 0;
        while(blancos!=0){
            Button actual = (Button) buscarNodo(fila, columna);
            actual.setText(array[cont]);
            cont++;
            blancos--;
            columna++;
        }
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
        while(contarCuadros()<=100){
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
            contador+=1;
        }
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
            writer.write("\nClaves: Machote -> fila,columna down left si la clave es nula poner un -\n");
            for(int i = 0; i <= 13; i++){
                for(int j = 0; j <= 13; j++){
                    Button actual = (Button) buscarNodo(i,j);
                    if(actual.getStyle().equals("-fx-opacity: 1; -fx-base: #000000;")){
                        String texto = actual.getText();
                        if(texto!=""){
                            String[] numeros = texto.replace("       ", "").split("\n");
                            texto = "";
                            int[] coordenadas = buscarNodoAux(actual);
                            texto+=coordenadas[0]+","+coordenadas[1]+" "+numeros[1]+" "+numeros[0]+"\n\n";
                            writer.write(texto);
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
        negros.add(boton);
    }

    public void setEstilo(Button boton, String style){
        boton.setStyle(style);
        negros.add(boton);
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
                        botonAux.setText("       -\n1-9");
                    }
                    else  if(blancosDerecha==1 & blancosAbajo == 0){ //solo hay una casilla hacia la derecha y hacia abajo nulo
                        botonAux.setText("       1-9\n-");
                    }
                    else if(blancosDerecha==1 & blancosAbajo ==1){ //solo hay una casilla para arriba y para abajo
                        botonAux.setText("       1-9\n1-9");
                    }
                    else if(blancosAbajo ==0 & blancosDerecha !=0){ // hay mas de una a la derecha y ninguna para abajo
                        if(filaColumnaSola(i,j,2))
                            botonAux.setText("       "+(rand.nextInt(rangoDerecha[1]-rangoDerecha[0]+1) +rangoDerecha[0])+"\n-");
                        else{
                            botonAux.setText("       *\n-");
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
                            botonAux.setText("       -\n"+(rand.nextInt(rangoAbajo[1]-rangoAbajo[0]+1) +rangoAbajo[0]));
                        else{
                            botonAux.setText("       -\n"+(rand.nextInt(rangoAbajo[1]-rangoAbajo[0]+1) +rangoAbajo[0]));
                            revisarFila.add(botonAux);
                        }
                    }
                }
            }
        }
        for (Button button : revisarFila) {
            String textoAnterior = button.getText();
            int valor = Integer.parseInt(button.getText().replace("       ", "").split("\n")[1]);
            int[] coordenadasButton = buscarNodoAux(button);
            int cont = verificarBlancos(coordenadasButton[0], coordenadasButton[1], 2);
            ArrayList<String> permutacionesPosibles = new ArrayList<>();
            String[] elem = {"1","2","3","4","5","6","7","8","9"};
            Permutaciones(elem, "", cont, 9, permutacionesPosibles, valor);
            if(permutacionesPosibles.size()<=1){
                System.out.println(permutacionesPosibles.get(0));
                System.out.println(coordenadasButton[0]+" "+coordenadasButton[1]);
            }
            int permutacionRandom = rand.nextInt(permutacionesPosibles.size());//(max - min +1) + min
            String permutacionEscogida = permutacionesPosibles.get(permutacionRandom); //Escoge una de las posibles permutaciones
            establecerPermutacionACasillasBlancasAbajo(coordenadasButton[0],coordenadasButton[1],permutacionEscogida,2); //Setea el boton actual con la permutacion escogida
            ArrayList<String> hacerListasDerecha =  verificarNoRepetidosFila(coordenadasButton[0],coordenadasButton[1],1); //Guarda las coordenadas del botonNegro si hay repetidos al revisar los blancos, si esta vacio es que no habia repetidos para la derecha
            ArrayList<String> hacerListasIzquierda = verificarNoRepetidosFila(coordenadasButton[0],coordenadasButton[1],2); // Igual que la de arriba solo que verifica para la izquierda
            while(!hacerListasDerecha.isEmpty() | !hacerListasIzquierda.isEmpty()){ //while hasta que ya no hayan repetidos.
                for(int i=0;i<permutacionesPosibles.size();i++) { //En caso de que la aleatoria no sirviera, se prueba con todas las permutaciones
                    permutacionEscogida = permutacionesPosibles.get(i);
                    establecerPermutacionACasillasBlancasAbajo(coordenadasButton[0], coordenadasButton[1], permutacionEscogida, 2);
                    hacerListasDerecha = verificarNoRepetidosFila(coordenadasButton[0], coordenadasButton[1], 1); //Verifico que no se repita hacia la derecha algun numero con la permutacion escogida
                    hacerListasIzquierda = verificarNoRepetidosFila(coordenadasButton[0], coordenadasButton[1], 2); //Lo mismo solo que a la izquerda
                    if(!(!hacerListasDerecha.isEmpty() | !hacerListasIzquierda.isEmpty())){ //En caso de encontras una buena permutacion, salgase del for, se saldra de una vez del while
                        break;
                    }
                    if(i == permutacionesPosibles.size()-1){ //En el caso extremo de que ninguna permutacion sirva, le indico que cambie la clave, actualize el valor, limpie las permutaciones, y vuelva a empezar a verificar permutaciones con la nueva clave.
                        cambiarClave(button,valor, textoAnterior);// ver funcion
                        valor = Integer.parseInt(button.getText().replace("       ", "").split("\n")[1]);
                        permutacionesPosibles.clear();
                        Permutaciones(elem, "", cont, 9, permutacionesPosibles, valor);
                    }
                }
            }
        }
        for (Button button : revisarColumna) {
            String textoAnterior = button.getText();
            int[] coordenadas = buscarNodoAux(button);
            coordenadas[1]+=1;
            Button aux = (Button) buscarNodo(coordenadas[0], coordenadas[1]);
            String tmp = "";
            while(coordenadas[1]<=13 && !aux.getStyle().equals("-fx-opacity: 1; -fx-base: #000000;")){
                if(!(aux.getText().equals("")))
                    tmp+=aux.getText()+",";
                coordenadas[1]+=1;
                aux = (Button) buscarNodo(coordenadas[0], coordenadas[1]);
            }
            String[] valoresSetteados = tmp.split(",");
            ArrayList<Integer> valoresSetteadosInts = new ArrayList<>();
            int cuenta = 0;
            for (String valorSetteado : valoresSetteados) {
                valoresSetteadosInts.add(Integer.parseInt(valorSetteado));
                cuenta += Integer.parseInt(valorSetteado);
            }
            coordenadas = buscarNodoAux(button);
            int blancos = verificarBlancos(coordenadas[0], coordenadas[1], 1);
            if(blancos == valoresSetteados.length){
                textoAnterior = textoAnterior.replace("*", Integer.toString(cuenta));
                button.setText(textoAnterior);
            }
            else{
                while(valoresSetteadosInts.size() != blancos){
                    int valor = rand.nextInt((9-1)+1)+1;
                    while(valoresSetteadosInts.indexOf(valor)>=0){
                        valor = rand.nextInt((9-1)+1)+1;
                    }
                    //printearBoton(valor ,button);
                    valoresSetteadosInts.add(valor);
                }
                cuenta=0;
                for (Integer valorSetteado : valoresSetteadosInts) {
                    cuenta+=valorSetteado;
                }
                textoAnterior = textoAnterior.replace("*", Integer.toString(cuenta));
                button.setText(textoAnterior);
            }
        }
        clearBlancos();
    }

    public void printearBoton(int valor, Button negro){
        int[] coordenadas = buscarNodoAux(negro);
        while(coordenadas[1]!=13){ //Hacia Derecha
            coordenadas[1]+=1;
            negro = (Button) buscarNodo(coordenadas[0],coordenadas[1]);
            if(negro.getStyle().equals("-fx-opacity: 1; -fx-base: #000000;")){
                break;
            }
            if((negro.getText().equals(""))){
                negro.setText(valor+"");
                break;
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
                    columna+=1;
                    botonFinal = (Button) buscarNodo(fila,columna);
                    if(botonFinal.getStyle().equals("-fx-opacity: 1; -fx-base: #000000;"))
                        break;
                    contador+=1;
                }
                break;
            case 2:
               // fila = fila+1;
                while(fila!=13){ //Hacia Abajo
                    fila+=1;
                    botonFinal = (Button) buscarNodo(fila,columna);
                    if(botonFinal.getStyle().equals("-fx-opacity: 1; -fx-base: #000000;"))
                        break;
                    contador+=1;
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
            //botonActual = (Button) buscarNodo(fila, columna);//si encuentra boton negro significa que la casilla en la que esta dependera de el
            //botonActual.setText(tmpFila+","+tmpColumna);
            int[] aux = {tmpFila,tmpColumna};
            respuesta.add(aux);
            return dependencias(fila, columna+1, false, true, respuesta);//por recursividad se va buscando por toda la fila o columna
        }
        if(continuarRight && right && !botonActual.getStyle().equals("-fx-opacity: 1; -fx-base: #000000;")){//buscar dependencia hacia abajo
            while(!botonActual.getStyle().equals("-fx-opacity: 1; -fx-base: #000000;")){//va buscando un boton negro en la fila
                tmpColumna--;
                botonActual = (Button) buscarNodo(tmpFila, tmpColumna);
            }
            //botonActual = (Button) buscarNodo(fila, columna);
            //botonActual.setText(tmpFila+","+tmpColumna);
            int[] aux = {tmpFila,tmpColumna};
            respuesta.add(aux);
            return dependencias(fila+1, columna, true, false, respuesta);
        }
        return respuesta;
    }

    public void establecerPermutacionACasillasBlancasAbajo(int fila, int columna, String permutaciones, int modalidad){
        int contador = 0;
        Button botonASetear;
        permutaciones = permutaciones.replace(",","");
        switch (modalidad){
            case 1:
                //columna = columna+1;
                while(columna!=13){ //Hacia Derecha
                    columna++;
                    botonASetear = (Button) buscarNodo(fila,columna);
                    if(botonASetear.getStyle().equals("-fx-opacity: 1; -fx-base: #000000;"))
                        break;
                    contador++;
                }
                break;
            case 2:
                // fila = fila+1;
                while(fila!=13){ //Hacia Abajo
                    fila++;
                    botonASetear = (Button) buscarNodo(fila,columna);
                    if(botonASetear.getStyle().equals("-fx-opacity: 1; -fx-base: #000000;"))
                        break;
                    botonASetear.setText(Character.toString(permutaciones.charAt(contador)));
                    contador++;
                }

        }


    }

    public ArrayList<String> verificarNoRepetidosFila(int fila, int columna, int opcion){

        ArrayList<String> posicionDondeHacerLista = new ArrayList<String>();
        posicionDondeHacerLista.add(fila + "," + columna);

        int contador =0;
        int cuantosCuadrosRecorrer = verificarBlancos(fila,columna,2);
        if(fila<13)
            fila+=1;
        else
            return new ArrayList<String>();
        int respaldoColumna= columna;
        Button botonRepetido=(Button) buscarNodo(fila,columna);
        String numeroEnBoton = botonRepetido.getText();
        boolean usarPosiciones = false;
        switch (opcion){
            case 1:
                //columna = columna+1;
                if(cuantosCuadrosRecorrer>1) {
                    while (contador < cuantosCuadrosRecorrer) {
                        botonRepetido = (Button) buscarNodo(fila, columna);
                        numeroEnBoton = botonRepetido.getText();
                        while (columna != 13) { //Hacia Derecha
                            //posicionDondeHacerLista.add(fila + "," + columna);
                            columna += 1;
                            botonRepetido = (Button) buscarNodo(fila, columna);
                            if (numeroEnBoton.equals(botonRepetido.getText())) {
                                // posicionDondeHacerLista.add(fila + "," + columna);
                                usarPosiciones = true;
                            }
                            if (botonRepetido.getStyle().equals("-fx-opacity: 1; -fx-base: #000000;"))
                                break;
                            // contador++;
                        }
                        contador += 1;
                        columna = respaldoColumna;
                        fila += 1;
                    }
                }
                break;
            case 2:
                // fila = fila+1;
                if(cuantosCuadrosRecorrer>1) {
                    while (contador < cuantosCuadrosRecorrer) {
                        botonRepetido = (Button) buscarNodo(fila, columna);
                        numeroEnBoton = botonRepetido.getText();
                        while (columna != 0) { //Hacia Izquierda
                            // posicionDondeHacerLista.add(fila + "," + columna);
                            columna -= 1;
                            botonRepetido = (Button) buscarNodo(fila, columna);
                            if (numeroEnBoton.equals(botonRepetido.getText())) {
                                usarPosiciones = true;
                                //  posicionDondeHacerLista.add(fila + "," + columna);
                            }
                            if (!numeroEnBoton.equals("") && botonRepetido.getStyle().equals("-fx-opacity: 1; -fx-base: #000000;"))
                                break;
                            //  contador++;
                        }
                        columna = respaldoColumna;
                        contador += 1;
                        fila += 1;
                    }
                }
                break;
        }
        if(usarPosiciones)
            return posicionDondeHacerLista;
        return new ArrayList<String>();

    }

    public void cambiarClave(Button botonACambias,int claveAnterior, String clave){//FIXME POSIBLE RAZON DE QUE SE ENCICLIE POR LA CLAVE

        int[] coordenadas = buscarNodoAux(botonACambias);

        System.out.println("El boton en :" + coordenadas[0]+","+coordenadas[1]+" ha cambiado su antigua clave "+claveAnterior);

        int blancosDerecha = verificarBlancos(coordenadas[0],coordenadas[1],1);
        int blancosAbajo = verificarBlancos(coordenadas[0],coordenadas[1],2);
        int [] posibleValor = establecerLimitesSuma(blancosDerecha,blancosAbajo,2);// Verifico Blancos para poder establecer el minimo y el maximo posibles.

        int nuevoValor = rand.nextInt(posibleValor[1]-posibleValor[0]+1)+posibleValor[0];// Random para saber la nueva clave

        while(claveAnterior==nuevoValor){ //Para que no sea el mismo valor que el anterior, while hasta que sea distinto
             nuevoValor = rand.nextInt(posibleValor[1]-posibleValor[0]+1)+posibleValor[0];
        }
        String x = botonACambias.getText();
        x = x.replace(String.valueOf(claveAnterior), Integer.toString(nuevoValor));
        botonACambias.setText(x);
    }

    public void clearBlancos(){
        for(int i = 0; i <= 13; i++){
            for(int j = 0; j <= 13; j++){
                Button actual = (Button) buscarNodo(i,j);
                if(!actual.getStyle().equals("-fx-opacity: 1; -fx-base: #000000;")){
                    //actual.setStyle("-fx-opacity: 1; -fx-base: #0000FF;");
                    actual.setText("");
                }
            }
        }
    }//Limpiar los numeros de los botones

    public ArrayList<int[]> pintarBordes(ArrayList<int[]> arregloCoordenadas){
        ArrayList<int []> temp = new ArrayList<>();
        for (int[] arregloCoordenada : arregloCoordenadas) {
            Button botonPrueba = null;
            Button botonPruebaxd = null;
            if (arregloCoordenada[0] - 1 >= 0) {
                botonPrueba = (Button) buscarNodo(arregloCoordenada[0] - 1, arregloCoordenada[1]);
            }
            if (arregloCoordenada[0] - 1 >= 0){
                botonPruebaxd = (Button) buscarNodo(arregloCoordenada[0], arregloCoordenada[1] - 1);
            }
            if(botonPrueba!=null && botonPrueba.getStyle().equals("-fx-opacity: 1; -fx-base: #000000;")){
                int[] coordenadas = buscarNodoAux(botonPrueba);
                temp.add(coordenadas);
            }
            if(botonPruebaxd != null && botonPruebaxd.getStyle().equals("-fx-opacity: 1; -fx-base: #000000;")) {
                int[] coordenadas = buscarNodoAux(botonPruebaxd);
                temp.add(coordenadas);
            }
        }
        return temp;
    }
}