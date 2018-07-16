/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package survivetheskies;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;

/**
 *
 * @author Alex Bakker
 */
public class PlayerClass extends GameObject {
    
    private PlayerClass opponent; //The AI will look at the opponent to calculate the moves he should make
    boolean isArtificial; //Boolean will be set to true in the constructor if the player is supposed to be an AI.
    
    public boolean shotOn = false; //This will be set to true whenever the AI is ready to shoot a bullet.
    private String colour; //This is set in the constructor.  Will be used to know which player is being dealt with in the check bounds method and more.
    private int health = 3; //Holds the players health.
    private boolean rightOn; //Rotate right when true in the game loop in SurviveTheSkies class.
    private boolean leftOn; //Rotate left when true in the game loop in SurviveTheSkies class.
    private boolean moveOn;//Moves forward when true in game loop in SurviveTheSkies.
    private int score = 0; //Holds the users score.

    public List <BulletClass> ammo = new ArrayList<>(); //Player each has 3 bullets to shoot at a time. Ex. size of 1 means one bullet has been shot. 
    
    
    public PlayerClass(Image image, String colour, boolean isAI, PlayerClass opponent) { //Constructor takes in Image, Colour, isAI, and the opponent
        super(image);
        this.colour = colour; //Set the colour to colour given
        
        this.opponent = opponent; //Set the opponent 
        isArtificial = isAI; //Set the AI boolean
    }
    
    public int getScore(){
        return this.score;
    }
    
    public void setScore(int score){ 
        this.score = score;
    }
    
    //Setter method for the right on boolean
    public void setRightOn(boolean choice){
        this.rightOn = choice;
    }
    
    //Setter method for the left on boolean
    public void setLeftOn(boolean choice){
        this.leftOn = choice;
    }
    
    //Setter method for the move on boolean.
    public void setMoveOn(boolean choice){
        this.moveOn = choice;
    }
    
    //This method takes in params of a max number and a min number.  Using the params it generates a random number
    public static int randomNumber(int min, int max){
        Random rand = new Random();
        
        int randInt = rand.nextInt((max - min) + 1);
        return randInt;
    }    
    
    //1 in 70 chance of returning true.
    public boolean decideToChange(){
        if (randomNumber(1,70) == 2){
            return true;
        }
        return false;
    }
    
    //This controls the actual movement of the AI.
    public void movement(){
        
        decideImage();
        
        this.shotOn = false;
        this.checkBounds();
        
        /*If the AI is set to move and if the opponent is moving then; set the AI velocity.
          Then if the AI's rotation is within 28 degrees of hitting the player then it will lock on and set the shot on to true.
          If the AI is not moving then set velocity to 0 and line up player and shoot. HOWEVER if the player has shot 3 bullets
          then run away.
        */
            if (opponent.moveOn){   
                this.moveOn = true;
                this.shotOn = false;
                this.setVelocity(new Point2D(Math.cos(Math.toRadians(this.getRotate())) * 3, Math.sin(Math.toRadians(this.getRotate())) * 3));
                
                if (this.getRotate() < this.findRotationForShot() + 25 && this.getRotate() > this.findRotationForShot() - 25){
                    this.lineUpShot();
                    this.shotOn = true;
                    this.setVelocity(Point2D.ZERO);   
                }           
           } else {
                this.moveOn = false;
                this.lineUpShot();
                this.shotOn = true;
                this.setVelocity(Point2D.ZERO);
                    
                if (opponent.ammo.size() > 2){
                    if (this.getView().getTranslateY() < 250){
                    this.getView().setRotate(90);
                    this.setVelocity(new Point2D(Math.cos(Math.toRadians(this.getRotate())) * 3, Math.sin(Math.toRadians(this.getRotate())) * 3));
                } else if (this.getView().getTranslateY() > 300){
                    this.getView().setRotate(270);
                    this.setVelocity(new Point2D(Math.cos(Math.toRadians(this.getRotate())) * 3, Math.sin(Math.toRadians(this.getRotate())) * 3));
                }
            }   
        }
    }
    /* 
    This method decides whether the plane should have the gas turned on or off.
    It does this based on whether or not the plane is moving.  It runs in the move
    method of the AI and of the player, but since both methods use the moveOn boolean
    it is very easy to implement.
    */
    public void decideImage(){
        if (this.colour.equals("Blue")){
            if(moveOn){
                Image image = new Image(this.getClass().getResourceAsStream("/res/Blue Jet.gif"));
                this.getView().setImage(image);
            } else {
                Image image = new Image(this.getClass().getResourceAsStream("/res/Blue Jet Off.gif"));
                this.getView().setImage(image);
            }
        } else {
            if(moveOn){
                Image image = new Image(this.getClass().getResourceAsStream("/res/Red Jet.gif"));
                this.getView().setImage(image);
            } else {
                Image image = new Image(this.getClass().getResourceAsStream("/res/Red Jet Off.gif"));
                this.getView().setImage(image);
            }
        }
    }
    
    //If the decideToChange is true then it sets a new rotation
    public void movingChoice(){
        if (!this.decideToChange()){
            //CONTINUE MOVING
        } else { //If it's deciding to change.
            this.getView().setRotate(randomNumber(0, 361));
            this.moveOn = true;
        }
    }
    
    //For AI player only.  This method finds the rotation for the AI to hit the player using basic triginometry.
    public double findRotationForShot(){
        double xDif = this.getView().getTranslateX() - opponent.getView().getTranslateX();
        double yDif = this.getView().getTranslateY() - opponent.getView().getTranslateY();
        
        if (this.colour.equals("Red")){
            return Math.atan(yDif / xDif) * 70 + 180;
        } else {
            return Math.atan(yDif / xDif) * 70;
        }
    }
    
    //This will use the findRoationForShot method to set the AI's rotation so he is facing the player.
    public void lineUpShot(){
        this.getView().setRotate(this.findRotationForShot());
    }
    
    
    /*
    Always check the bounds when moving.  If moveOn is true then set velocity based on current rotation
    But if moveOn and rightOn or leftOn are true then rotate the plane and then set the velocity.
    If only a rotation boolean is on then do not set a velocity, simply rotate the plane.
    */
    public void move(){
        this.checkBounds(); //Checks the bounds before moving.
        
        decideImage();
        
        if (this.moveOn){ //If the player is pressing the move button.
            this.setVelocity(new Point2D(Math.cos(Math.toRadians(this.getRotate())) * 3, Math.sin(Math.toRadians(this.getRotate())) * 3)); //Set the new velocity.
            if(this.rightOn){ //If he rotates while moving forward.
                this.rotateRight();
                Point2D temp = new Point2D(Math.cos(Math.toRadians(this.getRotate())), Math.sin(Math.toRadians(this.getRotate()))).normalize().multiply(3); 
                this.setVelocity(temp);
            }
            if(this.leftOn){ //If he rotates while moving forward.
                this.rotateLeft();
                Point2D temp = new Point2D(Math.cos(Math.toRadians(this.getRotate())), Math.sin(Math.toRadians(this.getRotate()))).normalize().multiply(3);
                this.setVelocity(temp);
            
            } 
        } else { //Only if rotate with no velocity.
            if (this.rightOn){ 
                this.rotateRight();
            }
            
            if (this.leftOn){
                this.rotateLeft();
            }
        }
    }
    
    //Sets the scoreImage variable to a certain image based on the player's score.
    public Image decideScoreImage(){
        Image scoreImage;
        switch (score) {
            case 0:
                scoreImage = new Image(getClass().getResourceAsStream("/res/ZeroNumber.gif"));
                break;
            case 1:
                scoreImage = new Image(getClass().getResourceAsStream("/res/OneNumber.gif"));
                break;
            case 2:
                scoreImage = new Image(getClass().getResourceAsStream("/res/TwoNumber.gif"));
                break;
            default:
                scoreImage = new Image(getClass().getResourceAsStream("/res/ThreeNumber.gif"));
                break;
        }
        return scoreImage;
    }
    
    public Image decideHealthImage(){
        Image healthImage;
        
        if (this.colour.equals("Blue")) { //Check if this is blue player or red player
            switch (health) {
                case 3:
                    //Based on the health assign a different picture.
                    healthImage = new Image(getClass().getResourceAsStream("/res/Full Health Blue.gif"));
                    break;
                case 2:
                    healthImage = new Image(getClass().getResourceAsStream("/res/Two Thirds Health Blue.gif"));
                    break;
                case 1:
                    healthImage = new Image(getClass().getResourceAsStream("/res/One Third Health Blue.gif"));
                    break;
                default:
                    healthImage = new Image(getClass().getResourceAsStream("/res/No Health.gif"));
                    break;
            }
        } else {
            switch (health) {
                case 3:
                    healthImage = new Image(getClass().getResourceAsStream("/res/Full Health Red.gif"));
                    break;
                case 2:
                    healthImage = new Image(getClass().getResourceAsStream("/res/Two Thirds Health Red.gif"));
                    break;
                case 1:
                    healthImage = new Image(getClass().getResourceAsStream("/res/One Third Health Red.gif"));
                    break;
                default:
                    healthImage = new Image(getClass().getResourceAsStream("/res/No Health.gif"));
                    break;
            }
        }
        return healthImage; //Return the image to be sent to the ImageView health node. 
    }
    
    public boolean hasAmmo(){ //If less than 3 bullets on screen, player can shoot again.
        return ammo.size() <  3;
    }
    
    public void resetPlayers(){
        //Reset the coordinates depending on the colour of the player.
        if (this.colour.equals("Blue")){
            this.getView().setTranslateX(300);
            this.getView().setTranslateY(285);
            this.getView().setRotate(0);
        } else {
            this.getView().setTranslateX(900);
            this.getView().setTranslateY(285);
            this.getView().setRotate(180);
        }
        
        this.health = 3;
    }
    
    public void playShotAudio(){
        AudioClip shotAudio;
        if (this.colour.equals("Blue")){
            shotAudio = new AudioClip(this.getClass().getResource("/res/blueShot.wav").toExternalForm());
        } else {
            shotAudio = new AudioClip(this.getClass().getResource("/res/redShot.wav").toExternalForm());
        }
        shotAudio.play();
    }
    
    public int getHealth(){
        return health;
    }
    
    public void setHealth(int health){
        this.health = health;
    }
    
    public void hit(){
        if (health > 0){
            health-= 1;
        }
    }
    
    public void addPoint(){
        this.score += 1;
    }
    
    public void checkBounds(){ //Restricts the player to the game screen.

        if (this.colour.equals("Blue") && this.isArtificial == false){ //Blue is on left
            if (this.getView().getTranslateY() <= 0){
                this.getView().setTranslateY(1);
            }
        
            if (this.getView().getTranslateY() >= 555){
                this.getView().setTranslateY(554);
            }
            
            if (this.getView().getTranslateX() <= 0 ){
                this.getView().setTranslateX(1);
            }
            if (this.getView().getTranslateX() >= 555 ){
                this.getView().setTranslateX(554);
            }  
        } else if (this.colour.equals("Red") && this.isArtificial == false){ 
            if (this.getView().getTranslateY() <= 0){
                this.getView().setTranslateY(1);
            }
        
            if (this.getView().getTranslateY() >= 555){
                this.getView().setTranslateY(554);
            }
            
            if (this.getView().getTranslateX() <= 635 ){
                this.getView().setTranslateX(636);
            }
            if (this.getView().getTranslateX() >= 1175 ){
                this.getView().setTranslateX(1174);
            }
           
        } else if (this.colour.equals("Blue") && this.isArtificial){
            if (this.getView().getTranslateY() <= 0){
                this.getView().setTranslateY(1);
                this.getView().setRotate(this.getRotate() + randomNumber(100, 200));
                this.setVelocity(new Point2D(Math.cos(Math.toRadians(this.getRotate())) * 3, Math.sin(Math.toRadians(this.getRotate())) * 3));
            }
        
            if (this.getView().getTranslateY() >= 555){
                this.getView().setTranslateY(554);
                this.getView().setRotate(this.getRotate() + randomNumber(100, 200));
                this.setVelocity(new Point2D(Math.cos(Math.toRadians(this.getRotate())) * 3, Math.sin(Math.toRadians(this.getRotate())) * 3));
            }
            
            if (this.getView().getTranslateX() <= 0 ){
                this.getView().setTranslateX(1);
                this.getView().setRotate(this.getRotate() + randomNumber(100, 200));
                this.setVelocity(new Point2D(Math.cos(Math.toRadians(this.getRotate())) * 3, Math.sin(Math.toRadians(this.getRotate())) * 3));
            }
            if (this.getView().getTranslateX() >= 555 ){
                this.getView().setTranslateX(554);
                this.getView().setRotate(this.getRotate() + randomNumber(100, 200));
                this.setVelocity(new Point2D(Math.cos(Math.toRadians(this.getRotate())) * 3, Math.sin(Math.toRadians(this.getRotate())) * 3));
            }  
        } else if (this.colour.equals("Red") && this.isArtificial){
            
            if (this.getView().getTranslateY() <= 0){
                this.getView().setTranslateY(1);
                this.getView().setRotate(this.getRotate() + randomNumber(100, 200));
                this.setVelocity(new Point2D(Math.cos(Math.toRadians(this.getRotate())) * 3, Math.sin(Math.toRadians(this.getRotate())) * 3));
            }
        
            if (this.getView().getTranslateY() >= 555){
                this.getView().setTranslateY(554);
                this.getView().setRotate(this.getRotate() + randomNumber(100, 200));
                this.setVelocity(new Point2D(Math.cos(Math.toRadians(this.getRotate())) * 3, Math.sin(Math.toRadians(this.getRotate())) * 3));
            }
            
            if (this.getView().getTranslateX() >= 1180 ){
                this.getView().setTranslateX(1179);
                this.getView().setRotate(this.getRotate() + randomNumber(100, 200));
                this.setVelocity(new Point2D(Math.cos(Math.toRadians(this.getRotate())) * 3, Math.sin(Math.toRadians(this.getRotate())) * 3));
            }
            if (this.getView().getTranslateX() <= 625 ){
                this.getView().setTranslateX(623);
                this.getView().setRotate(this.getRotate() + randomNumber(100, 200));
                this.setVelocity(new Point2D(Math.cos(Math.toRadians(this.getRotate())) * 3, Math.sin(Math.toRadians(this.getRotate())) * 3));
            }
        }
    }
}

