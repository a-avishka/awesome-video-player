package sample;

import javafx.application.Application;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;


public class Main extends Application {


    private static Stage stage;
    public boolean fullScr = false;



    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        primaryStage.setTitle("VideoPlayer");

        Scene scene = new Scene(root);


        scene.setOnMouseClicked(new EventHandler<MouseEvent>() {





            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    fullScr = (!fullScr);
                    primaryStage.setFullScreen(fullScr);


                }

            }


        });

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if((event.getCode()).equals(KeyCode.SHIFT)) {
                    fullScr = (!fullScr);
                    primaryStage.setFullScreen(fullScr);
                }
            }
        });

        primaryStage.setScene(scene);
        primaryStage.show();

        stage = primaryStage;


        primaryStage.addEventFilter(KeyEvent.KEY_PRESSED, k -> {
            if ( k.getCode() == KeyCode.SPACE){
                k.consume();
            }
        });



    }


    public static void main(String[] args) {
        launch(args);
    }


    public static Stage getStage() {
        return stage;
    }




}
