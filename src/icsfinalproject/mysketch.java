/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package icsfinalproject;

import processing.core.PApplet;
import processing.core.PImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

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
        starttime = millis(); // start time
    }

    public void draw() {
    // Main menu
    if (stage == 0) {
        background(0, 150, 150);
        fill(255);
        textSize(50);
        textAlign(CENTER);
        text("Welcome", 400, 180);
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
        image(waterfall,0,0);
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
        arrows[i].movement();
        arrows[i].draw();

        if (oxalive && arrows[i].isCollidingWith(ox)) {
            oxalive = false;
            stage = 2;
            oxdeathtime = millis();
        }
    }
    // Firecracker
    for (int i = 0; i < arrowcount; i++) {
        arrows[i].movement();
        arrows[i].draw();
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
            arrows[arrowcount] = new Projectile(this, 30, 50, mc.getX(), mc.getY(), "images/arrowupright.png");
            arrowcount++;
        }
        if (stage == 2 && keyCode == ENTER) { // Progress dialogue if enter is pressed
            if (dialogueIndex < dialogue.length - 1) {
                dialogueIndex++;
            }
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
