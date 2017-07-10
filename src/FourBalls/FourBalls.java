package FourBalls;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class FourBalls {
    public static void main(String[] args) {
        /**
	* @param args
        * Main creates a GUI asking the user which difficulty they would like to play.
        * The game begins.
	*/
        
        Object[] options = {"Easy","Hard","Extreme"};
        
        JFrame f = new JFrame(); // dialog box
        
        int n=-1;
        n = JOptionPane.showOptionDialog(f, "What game mode would you like to play?","Choose a mode",
                            JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,options, options[1]);
        
        if(n==-1){
            System.exit(0);
        } else {
            GameView game = new GameView(n+3); // starts a new game with {3,4,5} balls.
        }
    }
}