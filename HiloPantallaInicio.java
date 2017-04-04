package Interfaz;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * Created by Francisco Contreras on 30/03/2017.
 */
public class HiloPantallaInicio extends Thread {

   public boolean pause = false;
   public boolean stop = false;
   public double contador = 1;

    Label instruccionTexto;
    Button botonInicioPrincipal;


   public HiloPantallaInicio(Label instruccion, Button botonInicio){
        instruccionTexto=instruccion;
        botonInicioPrincipal=botonInicio;

    }

    public void run(){
        while(!stop){

                instruccionTexto.setOpacity(contador);
                contador-= 0.01;
            System.out.println(contador);
                try {
                    sleep(50);
                    if (contador <= 0.0) {

                        while (contador < 1.0) {
                           instruccionTexto.setOpacity(contador);
                            sleep(50);
                            contador += 0.01;
                            System.out.println(contador);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            //});
        }
    }


}
