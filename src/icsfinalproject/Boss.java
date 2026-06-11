/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package icsfinalproject;
import processing.core.PApplet;
import processing.core.PImage;

/**
 *
 * @author 342822160
 */
public class Boss {
    public int x, y;
    public PApplet app;
    public PImage bosspic;
    public int bhealth, bspeed;
    public int height, width;
    
    public Boss(PApplet p, int x, int y, int bhealth, int bspeed, String img) {
        this.app=p;
        this.x=x;
        this.y=y;
        this.bhealth=bhealth;
        this.bspeed=bspeed;
        this.bosspic=app.loadImage(img);
        this.width=bosspic.width;
        this.height=bosspic.height;
    }
    
    public void bossmove(int dx, int dy) {
        x += dx;
        y += dy;
    }
    
    public void draw() {
        app.image(bosspic,x,y);
    }
}
