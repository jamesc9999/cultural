package icsfinalproject;

import processing.core.PApplet;

public class BossBullet {

    private float x;
    private float y;
    private float dx;
    private float dy;
    private int speed;
    private PApplet app;

    public BossBullet(PApplet app, float x, float y, float targetX, float targetY) {
        this.app = app;
        this.x = x;
        this.y = y;
        this.speed = 4;
        float distance = PApplet.dist(x, y, targetX, targetY); // distance

        dx = (targetX - x) / distance;
        dy = (targetY - y) / distance;
    }

    public void move() {
        x += dx * speed;
        y += dy * speed;
    }
    
    // Collision
    public boolean isCollidingWith(Character player) {

        boolean isLeftOfOtherRight = x < player.getX() + 150;
        boolean isRightOfOtherLeft = x + 20 > player.getX();
        boolean isAboveOtherBottom = y < player.getY() + 150;
        boolean isBelowOtherTop = y + 20 > player.getY();

        return isLeftOfOtherRight && isRightOfOtherLeft && isAboveOtherBottom && isBelowOtherTop;
    }

    public void draw() {
        app.fill(255, 0, 0);
        app.rect(x, y, 20, 20);
    }
}
