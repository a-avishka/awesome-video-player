package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {


    private static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        primaryStage.setTitle("VideoPlayer");

        Scene scene = new Scene(root,600,337.5);
        scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
            boolean fullScr = false;

            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 2){
                    fullScr = (!fullScr);
                    primaryStage.setFullScreen(fullScr);
                }
            }
        });

        primaryStage.setScene(scene);
        primaryStage.show();

        stage = primaryStage;
    }


    public static void main(String[] args) {
        launch(args);
    }


    public static Stage getStage() {
        return stage;
    }






}
