package Interfaz;

import javafx.scene.control.Button;

import java.util.*;
import java.util.concurrent.RecursiveAction;

/**
 * Created by Bryan on 5/3/2017.
 */
public class Forks extends RecursiveAction {
    Random rand= new Random();
    public boolean pause = false;
    public boolean stop = false;
    public ArrayList<Islas> islas;
    public Islas isla;
    public ControladorVentanaJuegoKakuro controlador;
    public ArrayList<PermutacionPorBoton> permutacionPorBoton;
    public ArrayList<Integer> indicesPermutaciones = new ArrayList<>();
    public ArrayList<Integer> topesPermutaciones = new ArrayList<>();

    public Forks(Islas arg, ControladorVentanaJuegoKakuro control){
        isla = arg;
        controlador=control;
        permutacionPorBoton = new ArrayList<>();
    }

    protected void computeDirectly(){
        ArrayList<Button> revisarFila = new ArrayList<>();
        ArrayList<Button> revisarColumna = new ArrayList<>();
        /*
        if(i == 1){//FIXME SECUENCIAL

        }
        else{//FIXME PARALELO
            stop=true;
            break;
        }*/
        boolean continuar = false;/*
        for (int[] negro : isla.negros) {
             continuar = controlador.filaColumnaSola(negro[0], negro[1], 1);
             if(continuar)
                 break;
        }*/
        if(!continuar) {
            int cont = 0;
            for (int[] ints : isla.negros) {
                Button x = (Button) controlador.buscarNodo(ints[0], ints[1]);
                filaOcolumna(x, revisarColumna, revisarFila);
            }
            genPermutaciones(revisarColumna, 1);
            genPermutaciones(revisarFila, 0);
            for (PermutacionPorBoton porBoton : permutacionPorBoton) {
                for (int[] intersecciones : porBoton.intersecciones) {
                    Button boton = (Button) controlador.buscarNodo(intersecciones[0], intersecciones[1]);
                    PermutacionPorBoton x = null;
                    for (PermutacionPorBoton permutacionPorBoton1 : permutacionPorBoton) {
                        if(permutacionPorBoton1.boton.equals(boton) && !porBoton.clave){
                            x = permutacionPorBoton1;
                            break;
                        }
                    }
                    if(x!=null)
                        porBoton.compararPerm(x.permutaciones);
                }
            }
            //genPermutaciones(revisarFila, 0);
            for(int i =0;i<permutacionPorBoton.size();i++){
                indicesPermutaciones.add(0);
                topesPermutaciones.add(permutacionPorBoton.get(i).permutaciones.size());
            }
            boolean retorno = resolver(permutacionPorBoton, revisarFila);
            while (!retorno) {
                retorno = resolver(permutacionPorBoton, revisarFila);
            }
        }
        stop = true;
    }

    public void genPermutaciones(ArrayList<Button> revisar, int opcion){//opcion 0 o 1
        String[] x = {"1","2","3","4","5","6","7","8","9"};
        int blancos = 0;
        String[] clave;
        boolean existe;
        for(Button boton : revisar){
            ArrayList<String> perm = new ArrayList<>();
            clave = boton.getText().replace("       ", "").split("\n");
            int[] coor = controlador.buscarNodoAux(boton);
            blancos = controlador.verificarBlancos(coor[0], coor[1], opcion+1);
            controlador.Permutaciones(x, "", blancos, 9, perm, Integer.parseInt(clave[opcion]));
            PermutacionPorBoton nueva = new PermutacionPorBoton(perm, boton, controlador, opcion);
            existe = false;
            for (PermutacionPorBoton porBoton : permutacionPorBoton) {
                if(porBoton.boton.equals(boton)){
                    existe = true;
                    break;
                }
            }
            if(!existe)
                permutacionPorBoton.add(nueva);
        }
    }

    public String setPermu(PermutacionPorBoton x,int posicionBoton){
        //  int index = rand.nextInt(x.permutaciones.size());
        String perm = x.permutaciones.get(indicesPermutaciones.get(posicionBoton));
        int[] coor = controlador.buscarNodoAux(x.boton);
        controlador.establecerPermutacionACasillasBlancasAbajo(coor[0], coor[1], perm, 2);
        return perm;
    }

    public boolean resolver(ArrayList<PermutacionPorBoton> clase, ArrayList<Button> fila){
        String aux = "";

      /*  for(PermutacionPorBoton instancia : clase){
            aux+=setPermu(instancia);

      }*/
        for(int i =0;i<clase.size();i++){
            aux+=setPermu(clase.get(i),i);
        }

        //alterarIndicesPermutaciones();
        verificarSiTope();


        if(isla.permUsadas.contains(aux))
            return false;
        else{
            isla.permUsadas.add(aux);
        }
        if(!validar(fila)){
            return false;
        }
        return true;
    }

    public void alterarIndicesPermutaciones(){

        int indexer = 0;
        boolean movimientoHecho = false;
        boolean verQuenoHayanMasTopes = false;

        while(!movimientoHecho){

            while(!verQuenoHayanMasTopes){
                if(indicesPermutaciones.get(indexer).equals(topesPermutaciones.get(indexer))){
                    indicesPermutaciones.set(indexer,0);
                    indexer++;
                    movimientoHecho=true;

                }
                else{
                    indicesPermutaciones.set(indexer,indicesPermutaciones.get(indexer)+1);

                    movimientoHecho=true;
                    verQuenoHayanMasTopes = true;
                }

            }

        }

    }

    public void verificarSiTope(){

        int indexer = 0;

        boolean topesListos = false;

        while(!topesListos){
            if(indexer==indicesPermutaciones.size()){
                break;
            }
            else {
                if (indicesPermutaciones.get(indexer).equals(topesPermutaciones.get(indexer) - 1)) {
                    indicesPermutaciones.set(indexer, 0);
                    indexer++;
                } else {
                    indicesPermutaciones.set(indexer, indicesPermutaciones.get(indexer) + 1);
                    topesListos = true;
                }
            }
        }

    }

    public void filaOcolumna(Button boton, ArrayList<Button> revisarColumna, ArrayList<Button> revisarFila){
        String texto = boton.getText();
        String[] numeros = texto.replace("       ","").split("\n");
        if(!numeros[1].equals("-") && !numeros[0].equals("-")){
            if(!numeros[1].equals("1-9"))
                revisarColumna.add(boton);
            else if(!numeros[0].equals("1-9"))
                revisarFila.add(boton);
            else if(!numeros[0].equals("1-9") && !numeros[1].equals("1-9")){
                revisarFila.add(boton);
                revisarColumna.add(boton);
            }
        }
        else {
            if (numeros[0].equals("-")) {
                if (!numeros[1].equals("1-9"))
                    revisarColumna.add(boton);
            }
            else if (numeros[1].equals("-")) {
                if (!numeros[0].equals("1-9"))
                    revisarFila.add(boton);
            }
        }
    }

    public ArrayList<Button> getBotonesPropios(Button x, int opcion){
        int[] coor = controlador.buscarNodoAux(x);
        ArrayList<Button> result = new ArrayList<>();
        Button aux = new Button();
        switch (opcion){
            case 1://botones fila
                while(coor[1]!=13){
                    coor[1]+=1;
                    aux = (Button) controlador.buscarNodo(coor[0], coor[1]);
                    if(!aux.getStyle().equals("-fx-opacity: 1; -fx-base: #000000;"))
                        result.add(aux);
                    else
                        break;
                }
                break;
            case 2:
                while(coor[0]!=13){
                    coor[0]+=1;
                    aux = (Button) controlador.buscarNodo(coor[0], coor[1]);
                    if(!aux.getStyle().equals("-fx-opacity: 1; -fx-base: #000000;"))
                        result.add(aux);
                    else
                        break;
                }
                break;
        }
        return result;
    }

    public int setUno_Nueve(int suma, ArrayList<Integer> valores, int cantidad){
        int sumaAux = 0;
        for (int valor : valores) {
            sumaAux+=valor;
        }

        return suma-sumaAux;
    }

    public boolean validar(ArrayList<Button> x){
        for (Button button : x) {
            int clave = getClave(button);
            if (clave != -1) {
                int suma = 0;
                int cont = 0;
                int[] coor = controlador.buscarNodoAux(button);
                int blancos = blancos = controlador.verificarBlancos(coor[0], coor[1], 1);
                ArrayList<String> setteados = new ArrayList<>();
                ArrayList<Button> propios = getBotonesPropios(button, 1);
                ArrayList<Button> unoNueves = new ArrayList<>();
                for (Button propio : propios) {
                    String text = propio.getText();
                    if (!text.equals("")) {
                        setteados.add(text);
                        cont++;
                        suma += Integer.parseInt(text);
                    } else {
                        unoNueves.add(propio);
                        int[] c = controlador.buscarNodoAux(button);
                    }
                }
                if (suma > clave) {
                    return false;
                } else if (suma < clave && blancos == cont) {
                    return false;
                } else if (suma == clave && blancos != cont) {
                    return false;
                } else if (suma != clave && blancos != cont) {
                    int cantUnoNueve = unoNueves.size();
                    int diferencia = clave - suma;

                    String[] elementos = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
                    List<String> elementosArray = new ArrayList<>(Arrays.asList(elementos));
                    elementosArray.removeAll(setteados);
                    String[] nuevosElementos = new String[elementosArray.size()];
                    nuevosElementos = elementosArray.toArray(nuevosElementos);

                    ArrayList<String> permutacionesPosibles = new ArrayList<>();

                    if (cantUnoNueve != 1) {
                        controlador.Permutaciones(nuevosElementos, "", cantUnoNueve, nuevosElementos.length
                                , permutacionesPosibles, diferencia);
                        if (permutacionesPosibles.size() <= 1) {
                            return false;
                        }
                        int sumaTemporal = suma;
                        boolean sirvePermutacion = false;
                        for (int i = 0; i < permutacionesPosibles.size(); i++) {
                            String[] permActual = permutacionesPosibles.get(i).split(",");
                            for (int j = 0; j < permActual.length; j++) {
                                sumaTemporal += Integer.parseInt(permActual[j]);
                            }
                            if (sumaTemporal == clave) {
                                sirvePermutacion = true;
                                for (int w = 0; w < unoNueves.size(); w++) {
                                    unoNueves.get(w).setText(permActual[w]);
                                }
                                break;
                            } else {
                                sirvePermutacion = false;
                                sumaTemporal = suma;
                            }
                        }
                        if (!sirvePermutacion)
                            return false;

                    } else {
                        int masCercano = 0;
                        if (!elementosArray.contains(diferencia) && diferencia >= 1 && diferencia <= 9) {
                            masCercano = diferencia;
                            unoNueves.get(0).setText(diferencia + "");
                        } else
                            return false;
                    }
                }
            }
        }
        for(int[] coor : isla.negros){
            int tam = 0;
            tam+=controlador.verificarNoRepetidosFila(coor[0], coor[1], 1).size();
            tam+=controlador.verificarNoRepetidosFila(coor[0], coor[1], 2).size();
            if(tam!=0){
                return false;
            }
        }
        return true;
    }

    public int getClave(Button x){
        String tex = x.getText();
        String[] array = tex.replace("       ","").split("\n");
        try{
            return Integer.parseInt(array[0]);
        }
        catch (Exception e){
            return -1;
        }
    }

    public void compute(){
        ArrayList<Forks> caca = new ArrayList<>();
        for (Islas isla : islas) {
            Forks nuevo = new Forks(isla, controlador);
            caca.add(nuevo);
        }
        invokeAll(caca);
    }
}
