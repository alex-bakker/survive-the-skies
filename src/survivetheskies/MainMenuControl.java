/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package survivetheskies;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

/**
 *
 * @author Dylan Hayes
 */
public class MainMenuControl {

    @FXML
    private Button btExit;
    @FXML
    private Button btPlay;
    @FXML
    private Button btHow;
    @FXML
    private Button btControl;
    @FXML
    private Button btAudio;
    @FXML
    private ImageView ivAudioOn;
    @FXML
    private ImageView ivAudioOff;
    @FXML
    private VBox MainMenuVbox;

    public void initialize() {

        MainMenu MainStage = new MainMenu();
        //MainStage.playBackground();
        MainStage.checkmuteicon(ivAudioOn, ivAudioOff);
        
        btExit.setOnAction((ActionEvent event) -> {
            System.exit(0);
        });
        btPlay.setOnAction((ActionEvent event) -> {
            MainStage.CloseOpenScene(btPlay, 1);
        });
        btHow.setOnAction((ActionEvent event) -> {
            MainStage.CloseOpenScene(btHow, 2);
        });

        btControl.setOnAction((ActionEvent event) -> {
            MainStage.CloseOpenScene(btControl, 3);
        });
        btAudio.setOnAction((ActionEvent event) -> {
            MainStage.mute();
            MainStage.checkmuteicon(ivAudioOn, ivAudioOff);

            //System.out.print("hello");
            //System.exit(0);
        });
        MainMenuVbox.setOnKeyReleased((KeyEvent e) -> {
            if (e.getCode() == KeyCode.M) {
                MainStage.mute();
                MainStage.checkmuteicon(ivAudioOn, ivAudioOff);
            }
            if (e.getCode() == KeyCode.ESCAPE) {
                System.exit(0);
            }
        });

    }

}
