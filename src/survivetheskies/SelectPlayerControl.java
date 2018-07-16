package survivetheskies;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

/**
 *
 * @author Dylan Hayes
 */
public class SelectPlayerControl {

    @FXML
    private Button btBack;
    @FXML
    public Button btBluePlayer;
    @FXML
    public Button btTwoPlayer;
    @FXML
    public Button btRedPlayer;
    @FXML
    private VBox SelectPlayerControlVbox;

//   
    public void initialize() {
        MainMenu MainStage = new MainMenu();

             
        btBack.setOnAction((ActionEvent event) -> {
            MainStage.CloseOpenScene(btBack, 0);
        });
        btBluePlayer.setOnAction((ActionEvent event) -> {
            MainStage.startGame(btBluePlayer, 0);

        });

        btTwoPlayer.setOnAction((ActionEvent event) -> {
            MainStage.startGame(btTwoPlayer, 1);
        });
        btRedPlayer.setOnAction((ActionEvent event) -> {
            MainStage.startGame(btRedPlayer, 2);
        });
        SelectPlayerControlVbox.setOnKeyReleased((KeyEvent e) -> {
            if (e.getCode() == KeyCode.M) {
                MainStage.mute();
            }
            if (e.getCode() == KeyCode.ESCAPE) {
                MainStage.CloseOpenScene(btBack, 0);
            }
        });
       
    }

}

  
