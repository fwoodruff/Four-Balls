package FourBalls;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class Sprite {
    /**
     * Creates a sprite object, an image at a location
     * with 'mass' and' velocity' properties
     */
    public static List<Sprite> Sprites = new ArrayList<>();
    private double posX=0;
    private double posY=0;
    private double velX=0;
    private double velY=0;
    private double mass=1.31e16;
    public int size=20;
    public double accelX=0;
    public double accelY=0;// acceleration due to manually applied forces

    public double scale=1.1;

    
    private BufferedImage img=null;

    public Sprite(){
        /**
         *  Initialises the sprite object
         */
        Sprites.add(this);
    }
    
    public void setImage(String filename) {
        img = null;
        try {
            img = ImageIO.read(getClass().getResource(filename));
        } catch (IOException e) {
            System.out.println("file not found");
	}
    }

    public void draw(Graphics g) {
        /**
         * draws the sprite at its location
         */
        g.drawImage(img, (int)this.getY(),(int)this.getX(),(int)(scale*size),(int)(size*scale), null);
    }
    
    public void setPos(double x, double y){
        this.posX=x;
        this.posY=y;
    }
    public void setVel(double x, double y){
        this.velX=x;
        this.velY=y;
    }
    public void setMass(double m){
        mass=m;
    }
    public double getMass(){
        return mass;
    }
    public void force(double Fx, double Fy){
        /**
         * applies a force to the sprite. 
         */
        
        accelX+=Fx/this.getMass();
        accelY+=Fy/this.getMass();
    }

    public double getX(){
        return posX;
    }
    public double getY(){
        return posY;
    }
    public double getVelX(){
        return velX;
    }
    public double getVelY(){
        return velY;
    }
    public double getAccelX(){
        return accelX;
    }
    public double getAccelY(){
        return accelY;
    }

    public boolean touching(Sprite thatSprite) {
        /**
         * This determines whether two sprites are touching.
         */
        double dispX = thatSprite.getX()-this.getX();
        double dispY = thatSprite.getY()-this.getY();
        
        double dispSq = dispX*dispX +dispY*dispY;

        int rad = (int) ((this.size+thatSprite.size)*0.5);
        return dispSq < rad*rad;
    }
}