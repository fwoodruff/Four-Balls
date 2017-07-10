package FourBalls;

import java.util.List;

public class Physics {
    static double G = 6.67e-11; // gravitational constant
    
    //private double potential=0;
    //private double kinetic=0;
    
    public void updateParticles(List<GravMass> masses){
        GravMass thisParticle;
        GravMass thatParticle;
        for (int i = 0; i < masses.size(); i++) {
            thisParticle = masses.get(i);
            thisParticle.initRK4();
        }
        for (int i = 0; i < masses.size(); i++) {
            thisParticle = masses.get(i);
            for (int j = 0; j < masses.size(); j++) {
                if (i!=j){
                    thatParticle = masses.get(j);
                    thisParticle.updateRK4_k1(thatParticle);
                }
            }
        }
        for (int i = 0; i < masses.size(); i++) {
            thisParticle = masses.get(i);
            thisParticle.updateRK4_m2();
        }
        for (int i = 0; i < masses.size(); i++) {
            thisParticle = masses.get(i);
            for (int j = 0; j < masses.size(); j++) {
                if (i!=j){
                    thatParticle = masses.get(j);
                    thisParticle.updateRK4_k2(thatParticle);
                }
            }
        }
        for (int i = 0; i < masses.size(); i++) {
            thisParticle = masses.get(i);
            thisParticle.updateRK4_m3();
        }
        for (int i = 0; i < masses.size(); i++) {
            thisParticle = masses.get(i);
            for (int j = 0; j < masses.size(); j++) {
                if (i!=j){
                    thatParticle = masses.get(j);
                    thisParticle.updateRK4_k3(thatParticle);
                }
            }
        }
        for (int i = 0; i < masses.size(); i++) {
            thisParticle = masses.get(i);
            thisParticle.updateRK4_m4();
        }
        for (int i = 0; i < masses.size(); i++) {
            thisParticle = masses.get(i);
            for (int j = 0; j < masses.size(); j++) {
                if (i!=j){
                    thatParticle = masses.get(j);
                    thisParticle.updateRK4_k4(thatParticle);
                }
            }
        }
        for (int i = 0; i < masses.size(); i++) {
            thisParticle = masses.get(i);
            thisParticle.updatePhaseSpace();
            thisParticle.accelX=0;
            thisParticle.accelY=0;
        }
        
    }
    public double getEnergy(List<GravMass> masses){
        GravMass thisParticle;
        GravMass thatParticle;
        double kinetic=0;
        double potential=0;
        for (int i = 0; i < masses.size(); i++) {
            thisParticle = masses.get(i);
            kinetic+=0.5*thisParticle.getMass()*
                    (thisParticle.getVelX()*thisParticle.getVelX()+
                    thisParticle.getVelY()*thisParticle.getVelY());
            for (int j = i+1; j < masses.size(); j++) {
                thatParticle = masses.get(j);
                double dispX = thisParticle.getX()-thatParticle.getX();
                double dispY = thisParticle.getY()-thatParticle.getY();
                double dispSq = dispX*dispX+dispY*dispY;
                potential-=G*thisParticle.getMass()*thatParticle.getMass()/Math.sqrt(dispSq);
            }
        }
        return (kinetic+potential)*1e-18;
    }
    
    public double getAbsMomentum(List<GravMass> masses){
        double momentumX=0;
        double momentumY=0;
        GravMass thisParticle;
        for (int i = 0; i < masses.size(); i++) {
            thisParticle = masses.get(i);
            momentumX+=thisParticle.getVelX()*thisParticle.getMass();
            momentumY+=thisParticle.getVelY()*thisParticle.getMass();
        }
        return Math.sqrt(momentumX*momentumX+momentumY*momentumY)*1e-15;
    }
}