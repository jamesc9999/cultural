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
public class Character {
    private int x, y;
    private PApplet app;
    public int health, pspeed;
    private PImage character;
    private int height, width;
    
    public Character(PApplet p, int x, int y, int health, int pspeed, String character) {
        this.app = p;
        this.x=x;
        this.y=y;
        this.health=health;
        this.pspeed=pspeed;
        this.character = app.loadImage(character);
        this.height=this.character.height;
        this.width=this.character.width;
    }
    
    // Collision
    public boolean isCollidingWith(Character character) {
        boolean isLeftOfOtherRight = x < character.x + character.width;
        boolean isRightOfOtherLeft = x + width > character.x;
        boolean isAboveOtherBottom = y < character.y + character.height;
        boolean isBelowOtherTop = y + height > character.y;
        
        return isLeftOfOtherRight && isRightOfOtherLeft && isAboveOtherBottom && isBelowOtherTop;
    }
    
    public void move(int dx, int dy) {
        x += dx;
        y += dy;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public void draw() {
        app.image(character, x, y, 150, 150);
    }
    
    /**public void move(pspeed) {
        this.pspeed=pspeed;
    } */
}
