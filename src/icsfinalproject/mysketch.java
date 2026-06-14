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
 * Main game class for The Legend of Nian.
 * Controls game states, rendering, user input,
 * boss fights, dialogue, and file handling.
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
    private PImage cliffside;
    private PImage textbox;
    Projectile[] arrows = new Projectile[10000];
    private Boss ox;
    private Boss nian;
    private Boss finalnian;
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
    int[][] enemyPositions = { // 2d array for positions
    {100, 200},
    {300, 400},
    {500, 250}
    };
    private String[] endingdialogue = {
        "I saved the village...",
        "Nian has finally been defeated.",
        "The fireworks scared him away forever.",
        "The old man was right to believe in me."
    };
    private int endingindex = 0;
    private boolean statssaved = false;

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
        mc = new Character(this, 400, 400, 100, 10, "images/villagershrunk.png"); // Characters
        npc = new Character(this, 700, 275, 100, 10, "images/oldman.png");
        textbox = loadImage("images/box.png");
        background = loadImage("images/level1.png"); // Backgrounds
        background2 = loadImage("images/level2.png");
        waterfall = loadImage("images/waterfall.png");
        cliffside = loadImage("images/cliffside.png");
        ox = new Boss(this, 50, 0, 100, 10, "images/ox.png");
        nian = new Boss(this, 400, 300, 1500, 10, "images/nian.png");
        finalnian = new NianBoss(this,400,300,5000,10,"images/nianphase2.png");
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
        // Phase 1
        image(waterfall, 0, 0);
        // Play music 1
        if (music != null && !music.isRunning()) {
            music.loop(Clip.LOOP_CONTINUOUSLY);
        }
        // Draw player
        mc.draw();
        // Player HP
        fill(0, 255, 0);
        textSize(20);
        textAlign(CENTER);
        text("HP: " + mc.health, mc.getX() + 75, mc.getY() + 170);
        // Nian movement
        angle += 0.03;
        int centerX = 400;
        int centerY = 300;
        int radius = 150;
        nian.x = (int)(centerX + radius * cos(angle));
        nian.y = (int)(centerY + radius * sin(angle));
        // Draw boss
        nian.draw();
        // Boss HP
        fill(255, 0, 0);
        text("HP: " + nian.bhealth, nian.x + 75, nian.y + 170);
        // Shooting
        if (frameCount % 60 == 0 && bossBulletCount < bossBullets.length) {
            bossBullets[bossBulletCount] =
                new BossBullet(this, nian.x, nian.y, mc.getX(), mc.getY());
            bossBulletCount++;
        }
        // Bullets
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
        // Boss death -> stage 4
        if (nian.bhealth <= 0) {
            stage = 4;
            bossIntro = true;
            // reset for phase 2
            finalnian.bhealth = 5000;
            mc.health = 1500;
            bossBulletCount = 0;
            if (music != null) {
                music.stop();
            }
        }
        fill(255);
        textSize(30);
        textAlign(CENTER);
        text("NIAN", 400, 50);
    } else if (stage == 4) {
        // PHASE 2 INTRO + FINAL FIGHT
        image(waterfall, 0, 0);
        speed=10;
        // stop old music safely
        if (music != null && music.isRunning()) {
            music.stop();
        }
        // INTRO DIALOGUE PHASE
        if (bossIntro) {
            fill(0, 0, 0, 180);
            rect(0, 600, 800, 200);
            fill(255);
            textSize(25);
            textAlign(CENTER);
            text("Nian: You survived... impossible.\nIt's time to show you my true power.", 400, 650);
            text("Press ENTER to continue", 400, 750);
            mc.health = 1500;
            return;
        }
        // Play phase 2 music
        if (music2 != null && !music2.isRunning()) {
            music2.loop(Clip.LOOP_CONTINUOUSLY);
        }
        // Draw player
        mc.draw();
        // Player HP
        fill(0, 255, 0);
        textSize(20);
        textAlign(CENTER);
        text("HP: " + mc.health, mc.getX() + 75, mc.getY() + 170);
        // Boss movement
        angle += 0.05;
        int centerX = 400;
        int centerY = 300;
        int radius = 180;
        finalnian.x = (int)(centerX + radius * cos(angle));
        finalnian.y = (int)(centerY + radius * sin(angle));
        // Draw boss
        finalnian.draw();
        // Boss HP
        fill(255, 0, 0);
        text("HP: " + finalnian.bhealth,
             finalnian.x + 75,
             finalnian.y + 170);
        // Shooting
        if (frameCount % 30 == 0 &&
            bossBulletCount < bossBullets.length) {
            bossBullets[bossBulletCount] = new BossBullet(this,finalnian.x,finalnian.y,mc.getX(),mc.getY());
            bossBulletCount++;
        }
        // bullets
        for (int i = 0; i < bossBulletCount; i++) {
            if (bossBullets[i] != null) {
                bossBullets[i].move();
                bossBullets[i].draw();
                if (bossBullets[i].isCollidingWith(mc)) {
                    mc.health -= 8;
                    bossBullets[i] = null;
                    if (mc.health <= 0) {
                        stage = 0;
                        if (music2 != null) {
                            music2.stop();
                        }
                    }
                }
            }
        }
        // title
        fill(255);
        textSize(30);
        textAlign(CENTER);
        text("FINAL NIAN", 400, 50);
        // Boss death
        if (finalnian.bhealth <= 0) {
            if (music2 != null) {
                music2.stop();
            }
            stage = 5;
        }
    } else if (stage == 5) { // After scene
        image(cliffside,0,0);
        mc = new Character(this, 300, 300, mc.health, 10, "images/villagershrunk.png");
        mc.draw();
        image(textbox, 50, 500);
        fill(0);
        textSize(25);
        textAlign(LEFT);
        if (endingindex < endingdialogue.length) {
            text(endingdialogue[endingindex], 120, 610);
        }
        fill(255);
        textSize(18);
        text("Press ENTER", 550, 700);
    } else if (stage == 6) { // End screen
        background(0);
        fill(255);
        textAlign(CENTER);
        textSize(80);
        text("THE END", width / 2, 300);
        textSize(35);
        text("Thank you for playing", width / 2, 400);
        textSize(25);
        text("Projectiles Fired: " + Projectile.totalProjectiles,
        width / 2, 500);
        if (!statssaved) {
            String[] stats = {
                "The Legend of Nian",
                "------------------",
                "Total Projectiles Fired: " + Projectile.totalProjectiles,
                "Player Health Remaining: " + mc.health
            };

            saveStrings("projectilestats.txt", stats);
            statssaved = true;
        }
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
        // Final Nian fight
        if (stage == 4 && arrows[i] != null &&
            arrows[i].isCollidingWith(finalnian)) {
            finalnian.bhealth -= arrows[i].getdamage();
            arrows[i] = null;
        }
    }
 }
    // Key presses
    public void keyPressed() {
        if (stage == 0) {
            if (keyCode == ENTER) {
                stage = 1;
            }
        }
        if (key == 'z' && arrowcount < arrows.length) { // Arrow shooting
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
        if (stage == 5 && keyCode == ENTER) {
            if (endingindex < endingdialogue.length - 1) {
                endingindex++;
            } else {
                stage = 6; // end screen
            }
        }
    }
    
    // Mouse presses
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
