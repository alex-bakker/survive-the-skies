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
public class ControlsControl {

    @FXML
    private Button btBack;
    @FXML
    private VBox ControlVbox;

    public void initialize() {
        MainMenu MainStage = new MainMenu();

        btBack.setOnAction((ActionEvent event) -> {
            MainStage.CloseOpenScene(btBack, 0);
        });
        ControlVbox.setOnKeyReleased((KeyEvent e) -> {
            if (e.getCode() == KeyCode.M) {
                 MainStage.mute();
            }
            if (e.getCode() == KeyCode.ESCAPE) {
                 MainStage.CloseOpenScene(btBack, 0);
            }
        });
    }

}
