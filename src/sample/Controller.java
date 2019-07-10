package sample;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;


public class Controller {


    private String filePath;
    private String path;
    private int mediaCounter;
    private ArrayList<File> fileCollector = new ArrayList<>();
    private MediaPlayer mediaPlayer;
    private double speedRate = 1.0;


    @FXML
    private MediaView mediaView;

    @FXML
    private Label duration;

    @FXML
    private Label currentTime;


    @FXML
    private Slider volumeSlider;

    @FXML
    private Slider seekSlider;

    @FXML
    private void pause(ActionEvent e) {
        mediaPlayer.pause();
    }   //pause button

    @FXML
    private void play(ActionEvent e) {
        mediaPlayer.play();
    }   //play button

    @FXML
    private void stop(ActionEvent e) {
        mediaPlayer.stop();
    }   //stop button

    @FXML
    private void before(ActionEvent e) {        //view prior video button

        mediaPlayer.stop();

        try {
            mediaCounter--;
            playFile(fileCollector.get(mediaCounter));

        } catch (Exception er) {
            System.out.println("This is the first video");
        }
    }

    @FXML
    private void faster(ActionEvent e) {        //increase playback speed button
        speedRate+=0.5;
        mediaPlayer.setRate(speedRate);
    }

    @FXML
    private void slower(ActionEvent e) {        //decrease playback speed button
        speedRate-=0.5;
        mediaPlayer.setRate(speedRate);
    }

    @FXML
    private void next(ActionEvent e) {      //view next video button
        mediaPlayer.stop();
        try {
            mediaCounter++;
            playFile(fileCollector.get(mediaCounter));

        } catch (Exception er) {
            System.out.println("This is the last video");
        }
    }

    @FXML
    private void exit(ActionEvent e) {      //exit button
        System.exit(0);
    }


    @FXML
    private void buttonOpenFolder(ActionEvent event) {      //locate folder button


        DirectoryChooser directoryChooser = new DirectoryChooser();         //gets the folder we select and assigns the path the filePath variable.
        directoryChooser.getInitialDirectory();
        File selectedDir = directoryChooser.showDialog(null);
        filePath = selectedDir.toString();
//        System.out.println(filePath);


        fileLister(filePath);       //this function finds all the videos in the given folder path.....Read more about this below.
        mediaCounter = 0;
        playFile(fileCollector.get(mediaCounter));      //plays the first video from the list.....Read more about this below.


        System.out.println("Has a total of " + fileCollector.size() + " videos.");

    }


    //This function adds all video in the chosen directory to an array.
    private void fileLister(String filePath) {


        File folder = new File(filePath);
        File[] listOfFiles = folder.listFiles();    //lists down all files and folders in the current folder

        if (listOfFiles != null) {      //only runs the code if the folder is not empty

            for (File i : listOfFiles) {        //one by one adds the files to array
                if (i.isFile()) {
                    String fileExt = "";

                    try {
                        fileExt = i.getName().substring((i.getName().length()) - 4);           //gets the extension of the file
                    } catch (Exception e) {
                    }


                    if (fileExt.equals(".mp4")) {           //validation to only add videos (mp4) to array by comparing file extension.
                        fileCollector.add(i);


                    }


                } else if (i.isDirectory()) {           //if the file is a folder/directory ,sets the path to that folder and runs the fileLister function again

                    path = i.getPath();
//                    System.out.println("This is a folder.    " + path);
                    fileLister(path);               //I have used recursion here so the program will go on finding videos through all sub-folders

                }

            }

        }


    }


    //To play the video file I used the java mediaPlayer
    private void playFile(File i) {


        Main.getStage().setTitle(i.getName());


//        System.out.println(i.toURI().toString());
        System.out.println(i.getName());
        Media media = new Media(i.toURI().toString());          //Initiates the mediaPlayer
        mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);


        DoubleProperty width = mediaView.fitWidthProperty();        //Stretches the video vertically and horizontally to fit window height
        DoubleProperty height = mediaView.fitHeightProperty();

        width.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
        height.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));


        volumeSlider.setValue(mediaPlayer.getVolume() * 100);             //volume slider control
        volumeSlider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                mediaPlayer.setVolume(volumeSlider.getValue() / 100);
            }
        });


        mediaPlayer.play();         //Plays the video


        mediaPlayer.setOnReady(new Runnable() {
            @Override
            public void run() {


                //synchronises the slider with video play


                seekSlider.setMin(0.0);
                seekSlider.setValue(0.0);
                seekSlider.setMax(mediaPlayer.getTotalDuration().toSeconds());

                mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                    @Override
                    public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                        seekSlider.setValue(newValue.toSeconds());


//                        duration.setText(String.format("%.2f",(newValue.toSeconds()))+" / "+String.format("%.2f",(mediaPlayer.getTotalDuration().toSeconds())));

                        double mediaDur = mediaPlayer.getTotalDuration().toSeconds();

                        currentTime.setText(displayTime(newValue.toSeconds()));

                        duration.setText(displayTime(mediaDur));

                    }
                });


                //allows the user to click on the slider and seek to that time on video

//        seekSlider.setValue(mediaPlayer.getVolume()*100);
                seekSlider.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        mediaPlayer.seek(Duration.seconds(seekSlider.getValue()));
//                        System.out.println(seekSlider.get);
                    }
                });

            }
        });


    }


    private String displayTime(double secs) {


        int hrs = (int) secs / 3600;
        int remain = (int) secs - hrs * 3600;
        int mins = remain / 60;
        remain = remain - mins * 60;
        int sec = remain;


        String time = hrs + ":" + mins + ":" + sec;
        return time;
    }


}
