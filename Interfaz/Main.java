package Interfaz;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        try{
            Parent root = FXMLLoader.load(getClass().getResource("VentanaPrincipal.fxml"));
            primaryStage.setTitle("Generador de Kakuros");
            primaryStage.setScene(new Scene(root, 600, 450));

            primaryStage.setResizable(false);
            primaryStage.show();
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    public static void main(String[] args){
        launch(args);
    }
}
