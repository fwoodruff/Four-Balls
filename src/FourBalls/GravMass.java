/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FourBalls;

import java.util.ArrayList;
import java.util.List;

public class GravMass extends Sprite {
    /**
     * This adds gravitational mass functionality to the Sprite class.
     */
    
    public static List<GravMass> GravMasses = new ArrayList<>();

    // RK4 integration parameters
    private double k1x;
    private double k2x;
    private double k3x;
    private double k4x;
    private double k1y;
    private double k2y;
    private double k3y;
    private double k4y;
    
    private double m1x;
    private double m2x;
    private double m3x;
    private double m4x;
    private double m1y;
    private double m2y;
    private double m3y;
    private double m4y;
    
    // local and instantaneous displacement parameters
    private double dispX;
    private double dispY;
    private double dispSq;

    public GravMass() {
        /**
         * This initialises a gravitationally massive sprite.
         */
        GravMasses.add(this);
    }

    public void initRK4(){
        /**
         * This resets the RK4 integration parameters.
         */
        this.k1x=this.getAccelX();
        this.k2x=this.getAccelX();
        this.k3x=this.getAccelX();
        this.k4x=this.getAccelX();
        this.k1y=this.getAccelY();
        this.k2y=this.getAccelY();
        this.k3y=this.getAccelY();
        this.k4y=this.getAccelY();
        
        this.m1x=this.getVelX();
        this.m1y=this.getVelY();

    }

    public void updateRK4_k1(GravMass that) {
        /**
         * This updates the RK4 k1 parameter.
         */
        relativeSeparation(this.getX(),this.getY(),that.getX(),that.getY());

        double mag = Physics.G/(dispSq*Math.sqrt(dispSq));
        this.k1x -= dispX*mag*that.getMass();
        this.k1y -= dispY*mag*that.getMass();
    }
    public void updateRK4_m2(){
        /**
         * This calculates the RK4 m2 parameter.
         */
        m2x = this.getVelX() + (Game.dt/2.)*this.k1x;
        m2y = this.getVelY() + (Game.dt/2.)*this.k1y;
    }
    public void updateRK4_k2(GravMass that) {
        /**
         * This updates the RK4 k2 parameter.
         */
        double x1 = this.getX()+(Game.dt/2.)*this.m1x;
        double y1 = this.getY()+(Game.dt/2.)*this.m1y;
        double x2 = that.getX()+(Game.dt/2.)*that.m1x;
        double y2 = that.getY()+(Game.dt/2.)*that.m1y;
        relativeSeparation(x1,y1,x2,y2);

        double mag = Physics.G/(dispSq*Math.sqrt(dispSq));
        this.k2x -= dispX*mag*that.getMass();
        this.k2y -= dispY*mag*that.getMass();

    }
    public void updateRK4_m3(){
        /**
         * This calculates the RK4 m3 parameter.
         */
        m3x = this.getVelX() + (Game.dt/2.)*this.k2x;
        m3y = this.getVelY() + (Game.dt/2.)*this.k2y;
    }
    public void updateRK4_k3(GravMass that) {
        /**
         * This updates the RK4 k3 parameter.
         */
        double x1 = this.getX()+(Game.dt/2.)*this.m2x;
        double y1 = this.getY()+(Game.dt/2.)*this.m2y;
        double x2 = that.getX()+(Game.dt/2.)*that.m2x;
        double y2 = that.getY()+(Game.dt/2.)*that.m2y;
        relativeSeparation(x1,y1,x2,y2);
        
        double mag = Physics.G/(dispSq*Math.sqrt(dispSq));
        this.k3x -= dispX*mag*that.getMass();
        this.k3y -= dispY*mag*that.getMass();

    }
    public void updateRK4_m4(){
        /**
         * This calculates the RK4 m4 parameter.
         */
        m4x = this.getVelX() + (Game.dt)*this.k3x;
        m4y = this.getVelY() + (Game.dt)*this.k3y;
    }
    public void updateRK4_k4(GravMass that) {
        /**
         * This updates the RK4 k4 parameter.
         */
        double x1 = this.getX()+Game.dt*this.m3x;
        double y1 = this.getY()+Game.dt*this.m3y;
        double x2 = that.getX()+Game.dt*that.m3x;
        double y2 = that.getY()+Game.dt*that.m3y;
        relativeSeparation(x1,y1,x2,y2);
        
        double mag = Physics.G/(dispSq*Math.sqrt(dispSq));
        this.k4x -= dispX*mag*that.getMass();
        this.k4y -= dispY*mag*that.getMass();
    }

    private void relativeSeparation(double x1, double y1, double x2, double y2){
        /**
         * This calculates the relative separation {X,Y,Absolute}
         * given two position vectors.
         */
        this.dispX = x1-x2;
        this.dispY = y1-y2;
        this.dispSq = this.dispX*this.dispX + this.dispY*this.dispY;
    }
    
    public void updatePhaseSpace(){
        /**
         * This updates the position and velocity vectors of the particle.
         */
        double x = this.getX() + Game.dt*(m1x + 2*m2x + 2*m3x + m4x)/6.;
        double y = this.getY() + Game.dt*(m1y + 2*m2y + 2*m3y + m4y)/6.;
        double vx = this.getVelX() + Game.dt*(k1x + 2*k2x + 2*k3x + k4x)/6.;
        double vy = this.getVelY() + Game.dt*(k1y + 2*k2y + 2*k3y + k4y)/6.;
        
        this.setPos(x,y);
        this.setVel(vx,vy);
    }
}