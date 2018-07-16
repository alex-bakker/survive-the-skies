/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package survivetheskies;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author Alex Bakker
 */
public class SurviveTheSkies extends Application {
    int gameType;   
    public SurviveTheSkies(int gameType){
        this.gameType = gameType;
    }
    
    AnimationTimer timer; //This is the game loop. Must be local to entire class so it can be toggled in the game loop method.
    
    private final String STYLE_NORMAL = "-fx-background-color: transparent; -fx-padding: 5, 5, 5, 5;"; //This CSS is used for the endGame buttons.
    
    ImageView winnerView = new ImageView(); //This displays either blue player or red player wins.
    
    Button playAgain = new Button();
    Button goHome = new Button ();
    
    PlayerClass redPlayer;
    PlayerClass bluePlayer;
 
    //Images for the players health. 
    ImageView blueHealth = new ImageView();
    ImageView redHealth = new ImageView();

    ImageView blueScore = new ImageView();
    ImageView redScore = new ImageView();

    Image redImage; //These are the character images
    Image blueImage;//There are the character images
    Image redBullet; //Bullet image
    Image blueBullet; //BulletImage

    Image background; //This will hold the image that will be sent to the backView
    ImageView backView = new ImageView(); //The view where the background image will be displayed to

    private Pane root; //Create the pane

    private Parent createTwoPlayer() {

        winnerView.setTranslateX(385);
        winnerView.setTranslateY(77.5);
        
        winnerView.setVisible(false);
        
        background = new Image(getClass().getResourceAsStream("/res/Game Background.gif")); //Set the new image.
        backView.setImage(background); //Add the image to the ImageView node.

        //Set the background of the endGame buttons.
        playAgain.setStyle(STYLE_NORMAL);
        goHome.setStyle(STYLE_NORMAL);
        
        playAgain.setGraphic(new ImageView(new Image(this.getClass().getResourceAsStream("/res/PlayAgainButton.gif"))));
        goHome.setGraphic(new ImageView(new Image(this.getClass().getResourceAsStream("/res/MainMenuButton.gif"))));
        
        playAgain.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                newGame();
                playAgain.setVisible(false);
                goHome.setVisible(false);
                winnerView.setVisible(false);
            }
        });
        
        goHome.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                close();
            }  
        });

        playAgain.setVisible(false);
        goHome.setVisible(false);
        
        //Set the coordinates of the end game buttons.
        playAgain.setLayoutX(388);
        playAgain.setLayoutY(277.5);
        
        goHome.setLayoutX(655.5);
        goHome.setLayoutY(277.5);
        
        //Set the coordinates of the health bars
        redHealth.setY(5);
        redHealth.setX(1125);
        blueHealth.setY(5);
        blueHealth.setX(5);

        blueScore.setX(580);
        blueScore.setY(7);
        redScore.setX(660);
        redScore.setY(7);

        redScore.setImage(new Image(this.getClass().getResourceAsStream("/res/ZeroNumber.gif")));
        blueScore.setImage(new Image(this.getClass().getResourceAsStream("/res/ZeroNumber.gif")));
        
        root = new Pane(); //Set the pane to be of type pane.
        root.setPrefSize(1250, 625); //Set the size of the pane

        
        root.getChildren().add(backView); //Add the ImageView node.

        root.getChildren().add(blueScore);
        root.getChildren().add(redScore);

        root.getChildren().add(redHealth); //Add Health node
        root.getChildren().add(blueHealth); //add health node
        
        root.getChildren().add(playAgain); //The end game buttons are added to root at end so they are on the top of the pane.
        root.getChildren().add(goHome);
        
        root.getChildren().add(winnerView);
        
        switch (gameType) {
            case 0:
                bluePlayer = new PlayerClass(blueImage, "Blue", false, redPlayer);
                redPlayer = new PlayerClass(redImage, "Red", true, bluePlayer);
                break;
            case 1:
                bluePlayer = new PlayerClass(blueImage, "Blue", false, redPlayer);
                redPlayer = new PlayerClass(redImage, "Red", false, bluePlayer);
                break;
            default:
                redPlayer = new PlayerClass(redImage, "Red", false, bluePlayer);
                bluePlayer = new PlayerClass(blueImage, "Blue", true, redPlayer);
                break;
        }

        addGameObject(redPlayer, 900, 285);
        addGameObject(bluePlayer, 300, 285);
        
        redPlayer.getView().setRotate(180);

        timer = new AnimationTimer() {

            @Override
            public void handle(long now) {
                twoPlayerUpdate(); //Run twoPlayerUpdate() method through the timer(this is the game clock).
            }
        };
        timer.start();
        
        return root;
    }

    private void newGame(){
        bluePlayer.resetPlayers();
        redPlayer.resetPlayers();
        
        redPlayer.setScore(0);
        bluePlayer.setScore(0);
        
       redPlayer.setHealth(3);
       bluePlayer.setHealth(3);
        
        bluePlayer.setAlive(true);
        redPlayer.setAlive(true);
        
        playAgain.setVisible(false);
        goHome.setVisible(false);
        
        redScore.setImage(redPlayer.decideScoreImage());
        blueScore.setImage(bluePlayer.decideScoreImage());
        
        timer.start();
    }
    
    private void addBlueBullet(BulletClass bullet, double x, double y) { //Adds the blue bullet to blue player ammo and to game
        bluePlayer.ammo.add(bullet);
        addGameObject(bullet, x + 30, y + 30);

    }

    private void addRedBullet(BulletClass bullet, double x, double y) { //Adds the red bullet to red palyer ammo and to game
        redPlayer.ammo.add(bullet);
        addGameObject(bullet, x + 30, y + 30);
    }

    private void addGameObject(GameObject object, double x, double y) {
        object.getView().setTranslateX(x);
        object.getView().setTranslateY(y);
        root.getChildren().add(object.getView());
    }

    //Function for game loop to check the bullets boundaries.
    private void checkBulletBounds() {
        //Check the boundaries of the redBullet
        for (BulletClass redBullet : redPlayer.ammo) {
            if (redBullet.outOfBounds()) {
                redBullet.setAlive(false);
                root.getChildren().removeAll((redBullet.getView()));
            }
        }

        //Check the boundaries of the blue bullet
        for (BulletClass blueBullet : bluePlayer.ammo) {
            if (blueBullet.outOfBounds()) {
                blueBullet.setAlive(false);
                root.getChildren().removeAll((blueBullet.getView()));
            }
        }

    }

    
    
    //Can be used in OnePlayer or Two Player
    private void checkBulletCollision() {
        //Check collision of redBullet and BluePlayer
        for (BulletClass redBullet : redPlayer.ammo) {
            if (redBullet.isColliding(bluePlayer)) {
                redBullet.setAlive(false);
                root.getChildren().removeAll((redBullet.getView()));
            
                bluePlayer.hit();
            }
        
        }
        
        
        //Check collistion of blueBullet and redPlayer
        for (BulletClass blueBullet : bluePlayer.ammo) {
            if (blueBullet.isColliding(redPlayer)) {
                blueBullet.setAlive(false);
                root.getChildren().removeAll((blueBullet.getView()));

                redPlayer.hit();
            }
        }
        
    }
    
    private boolean isGameDone(){
        return redPlayer.getScore() == 3 || bluePlayer.getScore() == 3;
    }
    
    //Returns true if one of the players has 0 health which means they are dead.
    private boolean isRoundDone(){
        return bluePlayer.getHealth() == 0 || redPlayer.getHealth() == 0;
    }
    
    //Can be used in OnePlayer or TwoPlayer scene
    private void updateGameObject() {
        //Remove bullets from list
        redPlayer.ammo.removeIf(BulletClass::isDead);
        bluePlayer.ammo.removeIf(BulletClass::isDead);

        //Update bullets and players
        bluePlayer.ammo.forEach(BulletClass::update);
        redPlayer.ammo.forEach(BulletClass::update);

        bluePlayer.update();
        redPlayer.update();
    }

    //New round runs when a player has no health and dies.
    //This resets the coordinates of the players, removes stray bullets, and adds a point to player.
    private void newRound() {
        
        if (bluePlayer.getHealth() == 0){
            redPlayer.addPoint();
            redScore.setImage(redPlayer.decideScoreImage());
            
        } else if (redPlayer.getHealth() == 0){
            bluePlayer.addPoint();
            blueScore.setImage(bluePlayer.decideScoreImage());
        } 
        
        for (BulletClass blueBullet : bluePlayer.ammo) {
            blueBullet.setAlive(false);
            root.getChildren().removeAll(blueBullet.getView());
        }
        
        for (BulletClass redBullet : redPlayer.ammo) {
            redBullet.setAlive(false);
            root.getChildren().removeAll(redBullet.getView());
        }
        
        redPlayer.ammo.removeIf(BulletClass::isDead);
        bluePlayer.ammo.removeIf(BulletClass::isDead);
        
        redPlayer.resetPlayers();
        bluePlayer.resetPlayers();
    }

    /*
    The endGame checks if either player has a score of 3.  
    If they do, then stop the game timer, and open up scene that allows the option
    to either "Play Again" or "Back" as well as displaying who won the game.. 
    */
    private void endGame() {
         Image image = new Image(this.getClass().getResourceAsStream("/res/explosion.gif"));
        if (redPlayer.getScore() == 3){ //If red wins then blue should be explosion and vice versa
            bluePlayer.getView().setImage(image);
            winnerView.setImage(new Image(this.getClass().getResourceAsStream("/res/RedWins.gif")));
        } else {
            redPlayer.getView().setImage(image);
            winnerView.setImage(new Image(this.getClass().getResourceAsStream("/res/BlueWins.gif")));
        }
            playAgain.setVisible(true);
            goHome.setVisible(true);
            winnerView.setVisible(true);
    }

    //This loops the background song 1000 times.
    private void twoPlayerUpdate() {
        if (!redPlayer.isArtificial && !bluePlayer.isArtificial) { //This is if the player selects two player.


            checkBulletBounds();
            checkBulletCollision();

            //Check the health and change image
            redHealth.setImage(redPlayer.decideHealthImage());
            blueHealth.setImage(bluePlayer.decideHealthImage());

            redPlayer.move();
            bluePlayer.move();

        } else if (bluePlayer.isArtificial && !redPlayer.isArtificial){ //If the player selects to be red.

            checkBulletBounds();
            checkBulletCollision();

            //Check the health and change image
            redHealth.setImage(redPlayer.decideHealthImage());
            blueHealth.setImage(bluePlayer.decideHealthImage());

            redPlayer.move();

            bluePlayer.movingChoice();
            bluePlayer.movement();

            if (bluePlayer.shotOn) {
                if (PlayerClass.randomNumber(1, 50) == 5 && bluePlayer.hasAmmo()) {
                    if (MainMenu.mute == false) {
                        bluePlayer.playShotAudio();
                    }
                    BulletClass bullet = new BulletClass(blueBullet);
                    bullet.setVelocity((new Point2D(Math.cos(Math.toRadians(bluePlayer.getRotate())), Math.sin(Math.toRadians(bluePlayer.getRotate()))).normalize().multiply(6)));
                    addBlueBullet(bullet, bluePlayer.getView().getTranslateX(), bluePlayer.getView().getTranslateY());
                }
            }

            
        } else if (!bluePlayer.isArtificial && redPlayer.isArtificial){ //If the player selects to be Blue.

            checkBulletBounds();
            checkBulletCollision();

            //Check the health and change image
            redHealth.setImage(redPlayer.decideHealthImage());
            blueHealth.setImage(bluePlayer.decideHealthImage());

            bluePlayer.move();

            redPlayer.movingChoice();
            redPlayer.movement();

            if (redPlayer.shotOn) {
                if (PlayerClass.randomNumber(1, 50) == 5 && redPlayer.hasAmmo()) {
                    if (MainMenu.mute == false) {
                        redPlayer.playShotAudio();
                    }
                    BulletClass bullet = new BulletClass(redBullet);
                    bullet.setVelocity((new Point2D(Math.cos(Math.toRadians(redPlayer.getRotate())), Math.sin(Math.toRadians(redPlayer.getRotate()))).normalize().multiply(6)));
                    addRedBullet(bullet, redPlayer.getView().getTranslateX(), redPlayer.getView().getTranslateY());
                }
            }
        } 
        if(isRoundDone()){ //If the round has ended, reset everything to create a new round
            newRound();
        }
       
        updateGameObject();
        
        if (isGameDone()){ //If the game is over than the timer should be disabled so the user(s) cannot move.
            timer.stop();
            endGame();
        }
       
        
        
        //----------------------------------------------------------------------------------------
    }

    //This close method will close the game stage and take the user(s) back to the main menu.
    //It does this by loading the FXML file from the ressource class.
    private void close() {

        try {
            Stage stage = (Stage) root.getScene().getWindow();
            stage.close();
            System.out.print("Stage was closed");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
            stage.setTitle("Main Menu");
            Parent root2 = loader.load();
            Scene scene = new Scene(root2);
            stage.setScene(scene);
            stage.sizeToScene();
            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    MainMenu MainStage = new MainMenu();
    
    /*
    The start method instantiates some of the Images.  It sets the scene. 
    And it sets all of the players movement variables based on keyPressed and keyReleased
    */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("     Survive The Skies");
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        
        //Plays the background song.
        blueBullet = new Image(getClass().getResourceAsStream("/res/Blue Bullet.gif"));
        redBullet = new Image(getClass().getResourceAsStream("/res/Red Bullet.gif"));

        redImage = new Image(getClass().getResourceAsStream("/res/Red Jet.gif"));
        blueImage = new Image(getClass().getResourceAsStream("/res/Blue Jet.gif"));

        primaryStage.setScene(new Scene(createTwoPlayer())); //Create the instance of the scene.
        //blue is on left side
        primaryStage.getScene().setOnKeyPressed(e -> {
            if (null != e.getCode()) {
                switch (e.getCode()) {
                    case A:
                        if(!bluePlayer.isArtificial){
                            bluePlayer.setLeftOn(true);
                        }
                        break;
                    case D:
                        if(!bluePlayer.isArtificial){
                            bluePlayer.setRightOn(true);
                        }
                        break;
                    case W:
                        if(!bluePlayer.isArtificial){
                            bluePlayer.setMoveOn(true);
                        }
                        break;
                    case SHIFT:
                        if(!bluePlayer.isArtificial){
                            if (bluePlayer.hasAmmo()) {
                                if (MainMenu.mute == false) {
                                    bluePlayer.playShotAudio();
                                }
                                BulletClass bullet = new BulletClass(blueBullet);
                             bullet.setVelocity((new Point2D(Math.cos(Math.toRadians(bluePlayer.getRotate())), Math.sin(Math.toRadians(bluePlayer.getRotate()))).normalize().multiply(6)));
                                addBlueBullet(bullet, bluePlayer.getView().getTranslateX(), bluePlayer.getView().getTranslateY());
                        }
                        }
                        break;
                    case LEFT:
                        if (!redPlayer.isArtificial){
                            redPlayer.setLeftOn(true);
                        }
                        break;
                    case RIGHT:
                        if (!redPlayer.isArtificial){
                            redPlayer.setRightOn(true);
                        }
                        break;
                    case UP:
                        if (!redPlayer.isArtificial){
                            redPlayer.setMoveOn(true);
                        }
                        break;
                    case ESCAPE:
                        close();
                        break;
                    case M:
                        MainStage.mute();
                        break;
                    case SPACE:
                            if (!redPlayer.isArtificial){
                            if (redPlayer.hasAmmo()) {
                                if (MainMenu.mute == false) {
                                    redPlayer.playShotAudio();
                                }
                                BulletClass bullet = new BulletClass(redBullet);
                                bullet.setVelocity((new Point2D(Math.cos(Math.toRadians(redPlayer.getRotate())), Math.sin(Math.toRadians(redPlayer.getRotate()))).normalize().multiply(6)));
                                addRedBullet(bullet, redPlayer.getView().getTranslateX(), redPlayer.getView().getTranslateY());
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        });

        primaryStage.getScene().setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.LEFT) {
                if (!redPlayer.isArtificial){
                    redPlayer.setLeftOn(false);
                }
            }
            if (e.getCode() == KeyCode.RIGHT) {
                if (!redPlayer.isArtificial){
                    redPlayer.setRightOn(false);
                }
            }
            if (e.getCode() == KeyCode.A) {
                if (!bluePlayer.isArtificial){
                    bluePlayer.setLeftOn(false);;
                }
            }
            if (e.getCode() == KeyCode.D) {
                if (!bluePlayer.isArtificial){
                    bluePlayer.setRightOn(false);
                }
            }
            if (e.getCode() == KeyCode.UP) {
                if (!redPlayer.isArtificial){
                    redPlayer.setVelocity(new Point2D(0, 0));
                    redPlayer.setMoveOn(false);
                }
            }
            if (e.getCode() == KeyCode.W) {
                if (!bluePlayer.isArtificial){
                    bluePlayer.setVelocity(new Point2D(0, 0));
                    bluePlayer.setMoveOn(false);
                }
            }
        });

        primaryStage.show(); //Display the screen once loaded. 
    }

    /**
     * @param args the command line arguments
     */
}
