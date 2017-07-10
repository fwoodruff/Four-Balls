package FourBalls;

import java.awt.*;
import java.awt.Color;
import java.awt.event.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;

public class Game extends JPanel implements KeyListener,ActionListener {
    /**
    * The Game class keeps track of game properties,
    * recording key presses and updating the GUI.
    */
    
    public static double dt = 0.01;
    private int dispEnergy = 0;
    private int dispMomentum = 0;
    public int height, width;
    private final Timer t = new Timer((int) (dt*1000),this);
    private boolean first;
    private int score = 0;
    private int highScore = 0;
    private int numberOfBalls = 3;

    GravMass StarShip = new GravMass(); // makes a massive object
    GravMass massA = new GravMass();
    GravMass massB = new GravMass();
    GravMass massC;
    GravMass massD;

    Physics nBody = new Physics();
    Geometric board = new Geometric();
    
    private final HashSet<String> keys = new HashSet<>();

    public Game(int n) {
        /**
        * A game is initialised with n masses.
        * Images are assigned to masses and a timer begins.
        */
        numberOfBalls = n;
        
        switch (numberOfBalls) {
            case 5:
                massD = new GravMass();
                massD.setImage("rock.png");
            case 4:
                massC = new GravMass();
                massC.setImage("rock.png");
            case 3:
                break;
        }

        StarShip.scale =1.3;
        StarShip.setImage("Earth.png");
        massA.setImage("rock.png");
        massB.setImage("rock.png");
        
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        first = true;
        t.setInitialDelay(50);
        t.start();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        /**
         * This draws updated graphics in the GUI when called.
         * Sprites are drawn. Scores and Physics information are shown.
         */
        
        
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        height = getHeight();
	width = getWidth();
        
        Sprite thisParticle;
        for (int i = 0; i < Sprite.Sprites.size(); i++) { // draw all particles
            thisParticle = Sprite.Sprites.get(i);
            thisParticle.draw(g2d);
        }

        int line = 15; // line pixel separation

        if (score<highScore) { // sets the high score label to green if this is a new high score
            g2d.setColor(Color.white);
        } else {
            g2d.setColor(Color.green);
        }

        // Draw score
        String scoreA = "Score: " + Integer.toString(score/10);
	g2d.drawString(scoreA, 10, height / 2);
        
        // Draw high score
        String scoreB = "High Score: " + Integer.toString(highScore/10);
	g2d.drawString(scoreB, 10, height / 2 + line);
        
        // draw Energy: 'good' values are shown in green, 'bad' in red.
        String scoreC = "Energy: " + Integer.toString(dispEnergy) + "EJ"; 
        if (dispEnergy>0){
            g2d.setColor(Color.red);
        } else if (dispEnergy<-300) {
            g2d.setColor(Color.green);
        } else {
            g2d.setColor(Color.white);
        }
	g2d.drawString(scoreC, 10, height / 2 + 2*line);
        
        //draw Momentum, 'good' values are shown in green, 'bad' in red.
        String scoreD = "|Momentum|: " + Integer.toString(dispMomentum) + "kg m/s";
        if (dispMomentum>700){
            g2d.setColor(Color.red);
        } else if (dispMomentum<50) {
            g2d.setColor(Color.green);
        } else {
            g2d.setColor(Color.white);
        }
	g2d.drawString(scoreD, 10, height / 2 + 3*line);
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        /**
         * {Up, Down, Left, Right} key presses are recorded.
         */
        int code = e.getKeyCode();
	switch (code) {
            case KeyEvent.VK_LEFT:
                keys.add("LEFT");
                break;
            case KeyEvent.VK_RIGHT:
                keys.add("RIGHT");
                break;
            case KeyEvent.VK_UP:
                keys.add("UP");
                break;
            case KeyEvent.VK_DOWN:
                keys.add("DOWN");
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        /**
         * {Up, Down, Left, Right} key releases are recorded.
         */
	int code = e.getKeyCode();
	switch (code) {
            case KeyEvent.VK_LEFT:
                keys.remove("LEFT");
            case KeyEvent.VK_RIGHT:
                keys.remove("RIGHT");
            case KeyEvent.VK_UP:
                keys.remove("UP");
            case KeyEvent.VK_DOWN:
                keys.remove("DOWN");  
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
            /**
             * This is called when timer ticks.
             * Key presses are translated into accelerations.
             * Scores are updated.
             * Particle positions are updated using an RK4 numerical integration.
             * If a mass ejection or collision has occurred, the game and score reset.
             */
        
            if (first){
                initGame(); // reset
            }
        
            double F=3e18; // manually applied forces
            if (keys.contains("LEFT")) {
                StarShip.force(0,-F);
            }
            if (keys.contains("RIGHT")) {
                StarShip.force(0,F);
            }
            if (keys.contains("UP")){
                StarShip.force(-F,0);
            }
            if (keys.contains("DOWN")) {
                StarShip.force(F,0);
            }
            updateScore();
            
            // numerically integrates to update particle positions.
            nBody.updateParticles(GravMass.GravMasses); 
            if (!board.areInArea(this,Sprite.Sprites)) {
                playSound("FourBalls/gong.wav");
                first=true;
            }
            if (board.areTouching(Sprite.Sprites)) {
                playSound("FourBalls/click.wav");
                first=true;
            }
            repaint();
    }

    void updateScore(){
        /**
         * updates score, high score and physics information.
         */
        score++;
        if (score>highScore){
            highScore=score;
        }
        dispEnergy = (int) nBody.getEnergy(GravMass.GravMasses);
        dispMomentum = (int) nBody.getAbsMomentum(GravMass.GravMasses);
    }
    
    public void playSound(String filename) {
        /**
         * This plays a sound from the input file name.
         */
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(filename);
        try {
          Clip clip = AudioSystem.getClip();
          AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(inputStream));
          clip.open(ais);
          clip.start();
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
          System.err.println("ERROR: Playing sound has failed");
        }
    }
    
    public void initGame(){
        /**
         * initGame places objects in their initial positions
         * with initial velocities and resets displays.
         */
        switch (numberOfBalls) {
            case 3:
                StarShip.setPos(0.5*height,0.5*width); // sets positions
                massA.setPos(0.5*height,0.25*width);
                massB.setPos(0.5*height,0.75*width);
                StarShip.setVel(0,0); // sets velocities
                massA.setVel(100,0);
                massB.setVel(-50,0);
                break;
            case 4:
                StarShip.setPos(0.5*height,0.34*width);
                massA.setPos(0.5*height,0.2*width);
                massB.setPos(0.5*height,0.7*width);
                massC.setPos(0.5*height,0.8*width);
                StarShip.setVel(0,0);
                massA.setVel(-90,0);
                massB.setVel(-20,0);
                massC.setVel(110,0);
                break;
            case 5:
                StarShip.setPos(0.5*height,0.34*width);
                massA.setPos(0.5*height,0.2*width);
                massB.setPos(0.5*height,0.7*width);
                massC.setPos(0.5*height,0.8*width);
                massD.setPos(0.1*height,0.9*width);
                StarShip.setVel(0,0);
                massA.setVel(-90,0);
                massB.setVel(-20,0);
                massC.setVel(110,0);
                massD.setVel(0,0);
                break;
        }
        
        score=0;

        dispEnergy = (int) nBody.getEnergy(GravMass.GravMasses);
        dispMomentum = (int) nBody.getAbsMomentum(GravMass.GravMasses);

        first=false;
    }
}