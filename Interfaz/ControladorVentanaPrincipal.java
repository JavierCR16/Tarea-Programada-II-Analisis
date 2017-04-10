package Interfaz;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ControladorVentanaPrincipal implements Initializable {

    @FXML
    Button botonPrincipal;

    @FXML
    Pane cuadroPrincipal;

    @FXML
    Label labelInstruccion;

    @FXML
    ImageView imagenFondo;

    @FXML
    public Button cargarKakuro;

    Stage escenario;

    public void initialize(URL fxmlLocations, ResourceBundle resources){
        labelInstruccion.setText("Haga clic en cualquier \n lado para comenzar");
        Image imagen = new Image(getClass().getResource("Imagenes\\puzzle.jpg").toExternalForm());
        imagenFondo.setImage(imagen);
        HiloPantallaInicio hiloInicio = new HiloPantallaInicio(labelInstruccion,botonPrincipal);
        hiloInicio.start();

        labelInstruccion.setOnMouseClicked(event -> {
            try{
                escenario = new Stage();
                FXMLLoader loader = new FXMLLoader();
                Parent root = loader.load(getClass().getResource("VentanaJuegoKakuro.fxml").openStream());
                ControladorVentanaJuegoKakuro controladorJuego = loader.getController();
                controladorJuego.generarTablero();
                escenario.setScene(new Scene(root,1000,780));
                escenario.setTitle("Kakuro");
                hiloInicio.stop =true;
                Stage escenarioActual = (Stage)botonPrincipal.getScene().getWindow();
                escenarioActual.close();
                escenario.show();
            }catch(Exception e){
                e.printStackTrace();
            }
        });

        botonPrincipal.setOnAction(event->{
            try{
                escenario = new Stage();
                FXMLLoader loader = new FXMLLoader();
                Parent root = loader.load(getClass().getResource("VentanaJuegoKakuro.fxml").openStream());
                ControladorVentanaJuegoKakuro controladorJuego = loader.getController();
                controladorJuego.generarTablero();
                escenario.setScene(new Scene(root,1000,780));
                escenario.setTitle("Kakuro");
                hiloInicio.stop =true;
                Stage escenarioActual = (Stage)botonPrincipal.getScene().getWindow();
                escenarioActual.close();
                escenario.show();
            }catch(Exception e){
                e.printStackTrace();
            }
        });
        cargarKakuro.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            configurarFileChooser(fileChooser);
            ArrayList<String[]> ArrayTmp = new ArrayList<>();
            File contactos = fileChooser.showOpenDialog(this.escenario);
            if (contactos != null) {
                try {
                    FileReader fileReader = new FileReader(contactos);
                    BufferedReader bufferedReader = new BufferedReader(fileReader);
                    StringBuffer stringBuffer = new StringBuffer();
                    String line;
                    int cont=0;
                    while ((line = bufferedReader.readLine()) != null) {
                        if(cont%2==0) {
                            if(cont<30){//line.toString().charAt(1)!='*') {
                                stringBuffer.append(line);
                                stringBuffer.append("\n");
                            }
                            else{
                                String[] array = line.toString().split(" ");
                                ArrayTmp.add(array);
                            }
                        }
                        cont++;
                    }
                    //System.out.print(ArrayTmp.size());
                    fileReader.close();
                    String datos = stringBuffer.toString();
                    cont = 0;
                    String tmp = "";
                    while(cont!=datos.length()){
                        if(datos.charAt(cont)!=' ' && datos.charAt(cont)!='|' && datos.charAt(cont)!='\n'){
                            tmp+=datos.charAt(cont);
                        }
                        cont++;
                    }
                    //System.out.print(tmp);
                    escenario = new Stage();
                    FXMLLoader loader = new FXMLLoader();
                    Parent root = loader.load(getClass().getResource("VentanaJuegoKakuro.fxml").openStream());
                    ControladorVentanaJuegoKakuro controladorJuego = loader.getController();
                    escenario.setScene(new Scene(root,1000,780));
                    escenario.setTitle("Kakuro");
                    hiloInicio.stop =true;
                    Stage escenarioActual = (Stage)botonPrincipal.getScene().getWindow();
                    escenarioActual.close();
                    escenario.show();
                    controladorJuego.setDatosCarga(tmp, ArrayTmp);
                    controladorJuego.cargarTablero();
                }
                catch (IOException e){e.printStackTrace();}
            }
        });
    }

    private static void configurarFileChooser(FileChooser fileChooser) {
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("TXT", "*.txt")
        );
    }
}
