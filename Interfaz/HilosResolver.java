package Interfaz;

import javafx.scene.control.Button;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Bryan on 4/28/2017.
 */
public class HilosResolver extends Thread {
    Random rand= new Random();
    public boolean pause = false;
    public boolean stop = false;
    public Islas isla;
    public int i;
    public ControladorVentanaJuegoKakuro controlador;
    public ArrayList<PermutacionPorBoton>  permutacionPorBoton;

    public HilosResolver(Islas arg, ControladorVentanaJuegoKakuro control, int id){
        isla = arg;
        controlador=control;
        i=id;
        permutacionPorBoton = new ArrayList<>();
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
                if (!numeros[1].equals("1-9"))
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
            case 2:
                while(coor[0]!=13){
                    coor[0]+=1;
                    aux = (Button) controlador.buscarNodo(coor[0], coor[1]);
                    if(!aux.getStyle().equals("-fx-opacity: 1; -fx-base: #000000;"))
                        result.add(aux);
                    else
                        break;
                }
        }
        return result;
    }

    public int setUno_Nueve(int suma, int[] valores){
        int sumaAux = 0;
        for (int valor : valores) {
            sumaAux+=valor;
        }
        //FIXME VERIFICAFR QUE RETORNO NO ESTE YA SETEEADO
        return suma-sumaAux;
    }

    public boolean validar(ArrayList<int[]> x, ArrayList<Button> fila){
        /*
        for (int[] aux : x) {
            Button button = (Button) controlador.buscarNodo(aux[0], aux[1]);
            int clave = getClave(button);
            int suma = 0;
            int cont = 0;
            ArrayList<Button> propios = getBotonesPropios(button, 1);
            for (Button propio : propios) {
                String text = propio.getText();
                if (!text.equals("")) {
                    cont++;
                    suma += Integer.parseInt(text);
                }
            }
        }*/
        for(Button aux1 : fila){
            int[] coor = controlador.buscarNodoAux(aux1);
            int tam = 0;
            tam+=controlador.verificarNoRepetidosFila(coor[0], coor[1], 1).size();
            tam+=controlador.verificarNoRepetidosFila(coor[0], coor[1], 2).size();
            if(tam!=0){
                return false;
            }
        }
        /*
            if(cont==propios.size())
                if(suma==clave) {
                    //controlador.verificarNoRepetidosFila()
                    return true;
                }
                else
                    return false;
            else{
                if(suma<clave)
                    return true;
                else
                    return false;
            }
        }*/
        return true;
    }

    public int getClave(Button x){
        String tex = x.getText();
        String[] array = tex.replace("       ","").split("\n");
        return Integer.parseInt(array[0]);
    }

    public void run(){
        ArrayList<Button> revisarFila = new ArrayList<>();
        ArrayList<Button> revisarColumna = new ArrayList<>();
        while(!stop){
            for (int[] negro : isla.negros) {
                Button x = (Button)controlador.buscarNodo(negro[0], negro[1]);
            }
            int cont = 0;
            for (int[] ints : isla.negros) {
                Button x = (Button) controlador.buscarNodo(ints[0], ints[1]);
                filaOcolumna(x, revisarColumna, revisarFila);
            }
            for (Button button : revisarFila) {
                ///controlador.setEstilo(button, "-fx-opacity: 1; -fx-base: #0000FF;");
            }
            genPermutaciones(revisarColumna);
            boolean retorno = resolver(permutacionPorBoton, revisarFila);
            while(!retorno){
                retorno = resolver(permutacionPorBoton, revisarFila);
            }
            this.stop=true;
        }
    }

    public void genPermutaciones(ArrayList<Button> revisar){
        String[] x = {"1","2","3","4","5","6","7","8","9"};
        int blancos = 0;
        String[] clave;
        for(Button boton : revisar){
            ArrayList<String> perm = new ArrayList<>();
            clave = boton.getText().replace("       ", "").split("\n");
            int[] coor = controlador.buscarNodoAux(boton);
            blancos = controlador.verificarBlancos(coor[0], coor[1], 2);
            controlador.Permutaciones(x, "", blancos, 9, perm, Integer.parseInt(clave[1]));
            PermutacionPorBoton nueva = new PermutacionPorBoton(perm, boton);
            permutacionPorBoton.add(nueva);
        }
    }

    public void setPermu(PermutacionPorBoton x){
        int index = rand.nextInt(x.permutaciones.size());
        String perm = x.permutaciones.get(index);
        int[] coor = controlador.buscarNodoAux(x.boton);
        controlador.establecerPermutacionACasillasBlancasAbajo(coor[0], coor[1], perm, 2);
    }


    public boolean resolver(ArrayList<PermutacionPorBoton> clase, ArrayList<Button> fila){
        for(PermutacionPorBoton instancia : clase){
            setPermu(instancia);
        }
        if(!validar(isla.negros, fila)){
            return false;
        }
        return true;
    }
}
