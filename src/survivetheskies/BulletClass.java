/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package survivetheskies;

import javafx.scene.image.Image;

/**
 *
 * @author Alex Bakker
 */
public class BulletClass extends GameObject {
    
    public BulletClass(Image image){
        super(image);
    }
    
    public boolean outOfBounds(){
        return this.getView().getTranslateX() > 1250 || this.getView().getTranslateX() < 0 || this.getView().getTranslateY() > 650 || this.getView().getTranslateY() < 0;
    }
}
