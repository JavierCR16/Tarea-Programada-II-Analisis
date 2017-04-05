package Interfaz;

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
import javafx.stage.Stage;

import java.net.URL;
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

    public void initialize(URL fxmlLocations, ResourceBundle resources){
        labelInstruccion.setText("Haga clic en cualquier lado \n \t    para comenzar");
        Image imagen = new Image(getClass().getResource("Imagenes\\puzzle.jpg").toExternalForm());
        imagenFondo.setImage(imagen);
        HiloPantallaInicio hiloInicio = new HiloPantallaInicio(labelInstruccion,botonPrincipal);
        hiloInicio.start();

        botonPrincipal.setOnAction(event->{

            try{
                Stage escenario = new Stage();
                FXMLLoader loader = new FXMLLoader();
                Parent root = loader.load(getClass().getResource("VentanaJuegoKakuro.fxml").openStream());
               // ControladorVentanaJuegoKakuro controladorJuego = loader.getController();
              //  controladorJuego.generarTabla();
                escenario.setScene(new Scene(root,600,400));
                escenario.setTitle("Kakuro");
                hiloInicio.stop =true;
                Stage escenarioActual = (Stage)botonPrincipal.getScene().getWindow();
                escenarioActual.close();

                escenario.show();

            }catch(Exception e){
                e.printStackTrace();
            }




        });


    }

}
