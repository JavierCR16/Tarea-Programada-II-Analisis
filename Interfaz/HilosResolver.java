package Interfaz;

import java.util.ArrayList;

/**
 * Created by Bryan on 4/28/2017.
 */
public class HilosResolver extends Thread {
    public boolean pause = false;
    public boolean stop = false;
    public Islas isla;
    public HilosResolver(Islas arg){
        isla = arg;
    }

    public void run(){
        while(!stop){
            while(pause){
                try {
                    sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //codigo para resolver
        }
    }
}
