/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package icsfinalproject;

import icsfinalproject.BossBullet;
import processing.core.PApplet;
import processing.core.PImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.sound.sampled.*; // For music
import java.io.File;
import static processing.core.PApplet.cos;
import static processing.core.PApplet.sin;
import static processing.core.PConstants.CENTER;

/**
 *
 * @author 342822160
 */
public class mysketch extends PApplet {

    int stage = 0;
    int speed = 5;
    private Projectile arrow;
    private Character mc;
    private Character npc;
    private PImage background;
    private PImage background2;
    private PImage waterfall;
    private PImage textbox;
    Projectile[] arrows = new Projectile[10000];
    private Boss ox;
    private Boss nian;
    private boolean oxalive = true;
    private boolean questcomplete = false;
    private int oxdeathtime = -1;
    int arrowcount = 0;
    int starttime;
    private PImage dialog1;
    private String[] dialogue;
    private int dialogueIndex = 0;
    float playX = 250, playY = 350, playW = 300, playH = 80;
    float instX = 250, instY = 460, instW = 300, instH = 80;
    float backX = 50, backY = 700, backW = 150, backH = 50;
    // Boss
    private BossBullet[] bossBullets = new BossBullet[10000];
    private int bossBulletCount = 0;
    private float angle = 0;
    boolean bossIntro = true;
    Clip music;
    Clip music2;

    public void settings() {
        size(800, 800);
    }

    public void setup() {
        background(255);
        textSize(50);
        try {
            dialogue = loadStrings("dialogue.txt"); // Read from flat file
            if (dialogue == null) {
                throw new Exception("File could not be loaded.");
            }
        } catch (Exception e) { // Catch error
            println("Error loading dialogue file.");
            dialogue = new String[]{"Dialogue file missing."}; // Make array with 1 element to say this
        }
        mc = new Character(this, 400, 400, 100, 10, "images/villagershrunk.png");
        npc = new Character(this, 700, 275, 100, 10, "images/oldman.png");
        textbox = loadImage("images/box.png");
        background = loadImage("images/level1.png");
        background2 = loadImage("images/level2.png");
        waterfall = loadImage("images/waterfall.png");
        ox = new Boss(this, 50, 0, 100, 10, "images/ox.png");
        nian = new Boss(this, 400, 300, 1500, 10, "images/nian.png");
        starttime = millis(); // start time
        try { // For music
            AudioInputStream audio1 = AudioSystem.getAudioInputStream(
                new File("data/asgore.wav")
            );
            music = AudioSystem.getClip();
            music.open(audio1);
            AudioInputStream audio2 = AudioSystem.getAudioInputStream(
                new File("data/battleagainstatruehero.wav")
            );
            music2 = AudioSystem.getClip();
            music2.open(audio2);
        } catch (Exception e) {
            e.printStackTrace();
        }
}

    public void draw() {
    // Main menu
    if (stage == 0) {
        background(0, 150, 150);
        fill(255);
        textSize(50);
        textAlign(CENTER);
        text("The Legend of Nian", 400, 180);
        // Play button
        fill(220);
        rect(playX, playY, playW, playH, 10);
        fill(0);
        textSize(35);
        text("Play", 400, playY + 55);
        // Instructions button
        fill(220);
        rect(instX, instY, instW, instH, 10);
        fill(0);
        text("Instructions", 400, instY + 55);
    } else if (stage == 1) {
        background(255);
        image(background, 0, 0);
        mc.draw();
        if (oxalive) {
            ox.draw();
            ox.bossmove(0, 2);
        }
    } else if (stage == 2) {
        textAlign(LEFT); // realign text
        background(0);
        image(background2, 0, 0);
        mc.draw();
        npc.draw();
        if (mc.isCollidingWith(npc)) {
            image(textbox, 50, 500);
            textSize(25);
            int maxLines = 6;
            if (!questcomplete) { // dialogue if quest is not complete
                if (dialogueIndex < maxLines) {
                    text(dialogue[dialogueIndex], 120, 610);
                }
            } else {
                if (dialogueIndex < dialogue.length) {
                    text(dialogue[dialogueIndex], 120, 610);
                }
            }
        }
        // TIMER (ONLY IN STAGE 2)
        int elapsed = (millis() - starttime) / 1000;
        if (elapsed >= 30) {
            questcomplete = true;
        }
        fill(255);
        textSize(20);
        textAlign(CENTER);
        text("Time: " + elapsed + "s", 400, 780);
        textAlign(LEFT);
    } else if (stage == 99) {
        textAlign(LEFT); // realign text
        background(0);
        fill(255);
        textSize(30);
        text("Arrows to move", 200, 300);
        text("Z/X to shoot", 200, 350);
        textSize(20);
        text("This is the final project/summative for grade 12 computer science, ICS4U1.\n It's a game; hopefully you enjoy.", 0, 450);
        // Back button
        fill(200);
        rect(backX, backY, backW, backH);
        fill(0);
        textSize(30);
        text("Back", backX + 40, backY + 35);
    } else if (stage == 3) {
        image(waterfall, 0, 0);
        // Play music
        if (music != null && !music.isRunning()) {
            music.loop(Clip.LOOP_CONTINUOUSLY);
        }
        // Draw player
        mc.draw();
        // Player HP
        fill(0, 255, 0);
        textSize(20);
        textAlign(CENTER);
        text("HP: " + mc.health,mc.getX() + 75,mc.getY() + 170);
        // Nian moves in a circle
        angle += 0.03;
        int centerX = 400;
        int centerY = 300;
        int radius = 150;
        nian.x = (int)(centerX + radius * cos(angle));
        nian.y = (int)(centerY + radius * sin(angle));
        // Draw Nian
        nian.draw();
        // Nian HP
        fill(255, 0, 0);
        text("HP: " + nian.bhealth,nian.x + 75,nian.y + 170);
        // Nian shoots once every second
        if (frameCount % 60 == 0 && bossBulletCount < bossBullets.length) {
            bossBullets[bossBulletCount] =new BossBullet(this,nian.x,nian.y,mc.getX(),mc.getY());
            bossBulletCount++;
        }
    // Move and draw boss bullets
    for (int i = 0; i < bossBulletCount; i++) {
        if (bossBullets[i] != null) {
            bossBullets[i].move();
            bossBullets[i].draw();
            if (bossBullets[i].isCollidingWith(mc)) {
                mc.health -= 5; // damage amount
                bossBullets[i] = null; // remove bullet
                if (mc.health <= 0) {
                    stage = 0; // game over or return to menu
                }
            }
        }
    }
    // Boss title
    fill(255);
    textSize(30);
    textAlign(CENTER);
    text("NIAN", 400, 50);
    textAlign(LEFT);
    
    // Going to stage 4
    if (nian.bhealth <= 0) {
        stage = 4;
        bossIntro = true;

        // reset HP for phase 2
        nian.bhealth = 5000;
        mc.health = 1000;

        if (music != null) {
            music.stop();
        }
    }
    } else if (stage == 4) {
        // Background
        image(waterfall, 0, 0);
        // Music check to stop
        if (music != null && music.isRunning()) {
            music.stop();
        }
        // Intro dialogue
        if (bossIntro) {
            fill(0, 0, 0, 180);
            rect(0, 600, 800, 200);
            fill(255);
            textSize(25);
            textAlign(CENTER);
            text("Nian: You dare challenge me?\n It's time to show you my true power.", 400, 650);
            text("Press ENTER to begin the battle", 400, 700);
            return; // stop game logic until fight starts
        }
        // FIGHT PHASE
        // Play music
        if (music != null && !music.isRunning()) {
            music.loop(Clip.LOOP_CONTINUOUSLY);
        }
        mc.draw();
        fill(0, 255, 0);
        textSize(20);
        textAlign(CENTER);
        text("HP: " + mc.health, mc.getX() + 75, mc.getY() + 170);
        angle += 0.03;
        int centerX = 400;
        int centerY = 300;
        int radius = 150;
        nian.x = (int)(centerX + radius * cos(angle));
        nian.y = (int)(centerY + radius * sin(angle));
        nian.draw();
        fill(255, 0, 0);
        text("HP: " + nian.bhealth, nian.x + 75, nian.y + 170);
        // bullets
        if (frameCount % 60 == 0 && bossBulletCount < bossBullets.length) {
            bossBullets[bossBulletCount] =
                new BossBullet(this, nian.x, nian.y, mc.getX(), mc.getY());
            bossBulletCount++;
        }
        for (int i = 0; i < bossBulletCount; i++) {
            if (bossBullets[i] != null) {
                bossBullets[i].move();
                bossBullets[i].draw();

                if (bossBullets[i].isCollidingWith(mc)) {
                    mc.health -= 5;
                    bossBullets[i] = null;

                    if (mc.health <= 0) {
                        stage = 0;
                    }
                }
            }
        }
        fill(255);
        textSize(30);
        textAlign(CENTER);
        text("NIAN", 400, 50);
    }
    // movement (GLOBAL, always runs)
    if (keyPressed) {
        if (keyCode == LEFT) {
            mc.move(-speed, 0);
        }
        if (keyCode == RIGHT) {
            mc.move(speed, 0);
        }
        if (keyCode == UP) {
            mc.move(0, -speed);
        }
        if (keyCode == DOWN) {
            mc.move(0, speed);
        }
    }
    // arrows (GLOBAL, always runs)
    for (int i = 0; i < arrowcount; i++) {
        if (arrows[i] != null) {
            arrows[i].movement();
            arrows[i].draw();
            // Ox fight
            if (oxalive && arrows[i].isCollidingWith(ox)) {
                oxalive = false;
                stage = 2;
                oxdeathtime = millis();
                arrows[i] = null;
            }
            // Nian fight
            if (stage == 3 && arrows[i].isCollidingWith(nian)) {
                nian.bhealth -= arrows[i].getdamage();
                arrows[i] = null;
                if (nian.bhealth <= 0) {
                    stage = 4; // victory screen
                }
            }
        }
    }
 }

    public void keyPressed() {
        if (stage == 0) {
            if (keyCode == ENTER) {
                stage = 1;
            }
        }
        if (key == 'z' && arrowcount < arrows.length) {
            arrows[arrowcount] = new Projectile(this, 10, 10, mc.getX(), mc.getY(), "images/arrowupright.png");
            arrowcount++;
        }
        if (key == 'x' && questcomplete) {
            arrows[arrowcount] = new Projectile(this, 30, 50, mc.getX(), mc.getY(), "images/firecracker.png");
            arrowcount++;
        }
        if (stage == 2 && keyCode == ENTER) { // Progress dialogue if enter is pressed
            if (!questcomplete) {
                if (dialogueIndex < 5) { // only first 6 lines (0-5)
                    dialogueIndex++;
                }
            } else {
                if (dialogueIndex < dialogue.length - 1) {
                    dialogueIndex++;
                } else {
                    stage = 3; // dialogue finished, go to stage 3
                }
            }
        }
        if (stage == 4 && bossIntro && keyCode == ENTER) {
            bossIntro = false;
        }
    }
    
    public void mousePressed() {
    if (stage == 0) {
        // Play button
        if (mouseX > playX && mouseX < playX + playW &&
            mouseY > playY && mouseY < playY + playH) {
            stage = 1;
        }
        // Instructions button
        if (mouseX > instX && mouseX < instX + instW &&
            mouseY > instY && mouseY < instY + instH) {
            stage = 99;
        }
    }
    else if (stage == 99) {
        // Back button
        if (mouseX > backX && mouseX < backX + backW &&
            mouseY > backY && mouseY < backY + backH) {
            stage = 0;
        }
    }
}
}
