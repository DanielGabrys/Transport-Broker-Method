package tbm.com.transportbrokermethod;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;



public class Start extends Application
{
    Scene result;

    @Override
    public void start(Stage stage) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("mainPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1800, 1200);
        stage.setTitle("transport Broker Method");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}