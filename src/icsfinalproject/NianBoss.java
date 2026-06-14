/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package icsfinalproject;
import processing.core.PApplet;

/**
 *
 * @author caija
 */

public class NianBoss extends Boss {
    // Angle for boss movement
    private float angle = 0;
    public NianBoss(PApplet p, int x, int y, int bhealth, int bspeed, String img) {
        super(p, x, y, bhealth, bspeed, img);
    }
    
    // Overloaded constructor
    public NianBoss(PApplet p, int x, int y) {
        super(p, x, y, 5000, 10, "images/nianphase2.png");
    }
    
    @Override
    public void bossmove(int dx, int dy) {
        angle += 0.04f;
        int centerX = 400;
        int centerY = 300;
        int radius = 150;
        x = (int)(centerX + radius * Math.cos(angle));
        y = (int)(centerY + radius * Math.sin(angle));
    }
}
