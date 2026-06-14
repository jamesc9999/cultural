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
public class Projectile {
    private int x;
    private int y;
    private int speed;
    private int dmg;
    private int height, width;
    private PImage image;
    private PApplet app;
    public static int totalProjectiles = 0;
    
    public Projectile(PApplet app, int speed, int dmg, int x, int y, String image) {
        this.app = app;
        this.speed = speed;
        this.dmg = dmg;
        this.x=x;
        this.y=y;
        this.image=app.loadImage(image);
        this.height=this.image.height;
        this.width=this.image.width;
        totalProjectiles++;
    }
    
    public void movement() {
        y+=-speed;
    }
    
    public int getdamage() {
        return dmg;
    }
    
    // Collision
    public boolean isCollidingWith(Boss boss) {
        boolean isLeftOfOtherRight = x < boss.x + boss.width;
        boolean isRightOfOtherLeft = x + width > boss.x;
        boolean isAboveOtherBottom = y < boss.y + boss.height;
        boolean isBelowOtherTop = y + height > boss.y;
        
        return isLeftOfOtherRight && isRightOfOtherLeft && isAboveOtherBottom && isBelowOtherTop;
    }
    
    public int gety() {
        return y;
    }
    
    public void draw() {
        app.image(image,x,y, 35, 75);
    }
}
