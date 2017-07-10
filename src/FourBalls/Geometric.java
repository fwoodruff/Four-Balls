
package FourBalls;

import java.util.List;

public class Geometric {
    /**
     * This class determines geometric properties of the game,
     * which can then be used in the Game Over conditions.
     */
    
    Geometric(){}
    
    public boolean areTouching(List<Sprite> sprites) {
        /**
         * This checks whether any sprites are touching.
        */
        Sprite thisParticle;
        Sprite thatParticle;
        for (int i = 0; i < sprites.size(); i++) {
            thisParticle = sprites.get(i); // gets one sprite
            for (int j = i+1; j < sprites.size(); j++) {
                thatParticle = sprites.get(j); // gets another
                if (thisParticle.touching(thatParticle)) {
                    // checks if they are touching
                    return true;
                }
            }
        }
        return false;
    }
        
    private boolean isInArea(Game g,Sprite s) {
        /**
         * This checks whether a sprite is in the game area.
         */
        return !(s.getX()>g.height-s.size || s.getY()>g.width-s.size || s.getX()<0 || s.getY()<0);
    }
    
    public boolean areInArea(Game g, List<Sprite> masses){
        /**
         * This determines whether all sprites are in the game area.
         */
        Sprite thisParticle;
        for (int i = 0; i < masses.size(); i++) {
            thisParticle = masses.get(i);
            if (!this.isInArea(g, thisParticle)){
                return false;
            }
        }
        return true;
    }
}