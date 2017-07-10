package FourBalls;

import java.awt.Color;
import javax.swing.JFrame;

public class GameView {
    /**
     * This class sets up the GUI frame and calls a Game.
     * @param n 
     */
    public GameView(int n){
        JFrame frame = new JFrame();
        switch (n){
            case 3:
                frame.setTitle("Three Balls");
                break;
            case 4:
                frame.setTitle("Four Balls");
                break;
            case 5:
                frame.setTitle("Five Balls");
                break;
        }
        
        Game g = new Game(n);
        frame.setContentPane(g);
        frame.getContentPane().setBackground(Color.black);
        frame.setSize(700, 700);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
