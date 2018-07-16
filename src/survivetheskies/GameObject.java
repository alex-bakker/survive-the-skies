/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package survivetheskies;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author Alex Bakker
 */
public class GameObject {
    
    private final ImageView view = new ImageView(); //Will be the view of the node.
    private Point2D velocity = new Point2D(0, 0); //Tracks and changes the nodes velocity 2D
    private boolean alive = true; //Test to see if the node is alive or not. 

    public GameObject(Image node){
        view.setImage(node);
    }
    
    public ImageView getView(){
        return view;
    }
    
    public void setVelocity (Point2D velocity){ //Set the nodes velocity
        this.velocity = velocity;
    }
    
    public void update() {
        view.setTranslateX(view.getTranslateX() + velocity.getX());
        view.setTranslateY(view.getTranslateY() + velocity.getY());
    }
    
    public Point2D getVelovity(){ //Get the nodes velocity.
        return this.velocity;
    }
    
    public boolean isAlive(){ //Check if node is alive
        return alive;
    }
    
    public boolean isDead(){ //Check if node is dead
        return !alive;
    }
    
    public void setAlive(boolean alive){ //Set the node to alive or dead
        this.alive = alive;
    }
    
    public double getRotate(){ //Get the nodes rotation
      return view.getRotate();
    }
    
    public void rotateRight(){ //Rotate the node right
        view.setRotate(view.getRotate() + 3);
    }
    
    public void rotateLeft(){ //Rotate the node left
        view.setRotate(view.getRotate() - 3);
    }
    
    public boolean isColliding(GameObject other){ //Check collision of two nodes.
        return getView().getBoundsInParent().intersects(other.getView().getBoundsInParent());
    }
}
