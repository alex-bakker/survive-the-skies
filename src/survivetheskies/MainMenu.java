/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package survivetheskies;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

/**
 *
 * @author Dylan Hayes
 */
public class MainMenu extends Application {

    static public boolean mute = true;
    public AudioClip backgroundSong = new AudioClip(getClass().getResource("/res/Background Music.wav").toExternalForm());

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
        Parent root = loader.load();
        stage.setTitle("Main Menu");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        backgroundSong.setVolume(100); 
    }

    public void playBackground() {
        backgroundSong.play();
    }

    public void mute() {
        if (mute == false) {
            backgroundSong.stop();

            mute = true;
            System.out.print("mute");
        } else if (mute == true) {
            backgroundSong.setCycleCount(100);
            backgroundSong.play();
            mute = false;
            System.out.print("unmute");
        }

    }

    public void checkmuteicon(ImageView ivAudioOn, ImageView ivAudioOff) {
        if (mute == true) {
            ivAudioOn.setVisible(false);
            ivAudioOff.setVisible(true);
        } else if (mute == false) {
            ivAudioOff.setVisible(false);
            ivAudioOn.setVisible(true);
        }
    }

    public void CloseOpenScene(Button bt, int fxml) {
        try {
            Stage stage = (Stage) bt.getScene().getWindow();
            stage.setResizable(false);
            stage.close();
            System.out.print("Stage was closed");
            FXMLLoader loader = null;
            switch (fxml) {
                case 0:
                    //Main
                    loader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
                    stage.setTitle("Main Menu");
                    stage.sizeToScene();
                    break;
                case 1:
                    //Player choice

                    loader = new FXMLLoader(getClass().getResource("Select Player.fxml"));
                    stage.setTitle("Select Player");
                    stage.sizeToScene();
                    break;

                case 2:
                    //HowtoPlay
                    loader = new FXMLLoader(getClass().getResource("HowToPlay.fxml"));
                    stage.setTitle("How to Play");
                    stage.sizeToScene();

                    break;
                case 3:
                    //Controls
                    loader = new FXMLLoader(getClass().getResource("Controls.fxml"));
                    stage.setTitle("Controls");
                    stage.sizeToScene();
                    break;
                default:
                    loader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
                    stage.setTitle("Main Menu");
                    stage.sizeToScene();
                    break;
            }
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void startGame(Button bt, int gametype) {        
        Stage stage = (Stage) bt.getScene().getWindow();
        stage.close();
        SurviveTheSkies GameStage = new SurviveTheSkies(gametype);
        GameStage.start(stage);

    }

    public static void main(String[] args) {

        launch(args);

    }

}
