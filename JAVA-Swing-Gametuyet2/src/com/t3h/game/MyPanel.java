package com.t3h.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JPanel;

public class MyPanel extends JPanel {
    ArrayList<Snow> snows = new ArrayList<>();
    ArrayList<SnowBar> snowBars = new ArrayList<>();
    Player player;
    long currentSnow;
    long currentSnowBar;
    long currentPlayerY;
    long currentPlayerX;
    boolean isPressLeft;
    boolean isPressRight;

    MyPanel() {
        setLocation(0, 0);
        setSize(500, 800);
        setBackground(Color.CYAN);

        createListSnow();
        createListSnowBar();
        createPlayer();

        movePlayerX();
        createThread();
    }

    void createListSnow(){
        for (int i = 0; i < 150; i++) {
            Snow snow = createSnow();
            snows.add(snow);
        }
    }

    void createPlayer() {
        player = new Player();
        player.w = 30;
        player.h = 30;
        player.x = (500 - 30) / 2;
        player.y = 100;
        player.img = new ImageIcon(
                getClass().getResource("/imgs/nguoituyet.png")
        ).getImage();
    }

    void moveSnow(long curr) {
        if (curr - currentSnow >= 15) {
            currentSnow = curr;
            for (int i = 0; i < snows.size(); i++) {
                Snow s = snows.get(i);
                s.y++;
                if (s.y == 800) {
                    s.y = 0;
                    Random rd = new Random();
                    s.x = rd.nextInt(500);
                }
            }
        }
    }

    void moveSnowBar(long curr) {
        if (curr - currentSnowBar >= 8) {
            currentSnowBar = curr;
            for (int i = 0; i < snowBars.size(); i++) {
                SnowBar sb = snowBars.get(i);
                sb.y--;
                if (sb.y == -15) {
                    sb.y = 800;
                    Random rd = new Random();
                    sb.x = rd.nextInt(500 - 70);
                }
            }
        }
    }

    void movePlayer(long curr) {
        if (curr - currentPlayerY >= 8) {
            currentPlayerY = curr;
            player.y += 2;
        }
        if (curr - currentPlayerX >= 5) {
            currentPlayerX = curr;
            if (isPressLeft == true) {
                player.x -= 2;
            }
            if (isPressRight == true) {
                player.x += 2;
            }
        }

        interactPlaySnowBar();
    }

    void interactPlaySnowBar() {
        Rectangle rPlayer = new Rectangle(
                player.x, player.y,
                player.w, player.h
        );

        for ( int i = 0; i < snowBars.size(); i++){
            SnowBar sb = snowBars.get(i);
            Rectangle rSnowBar = new Rectangle(
                    sb.x, sb.y,
                    sb.w, sb.h
            );
            if ( rPlayer.intersects(rSnowBar)){
                if (player.y+player.h - sb.y <=5){
                    //va cham
                    //update lai y player
                    player.y = sb.y-player.h-1;
                }
            }
        }
    }

    void checkDie(){
        if (player.y <=0 || player.y >= 800-player.h){
            int result =
                    JOptionPane.showConfirmDialog(this,
                    "Player died................",
                    "Die",
                    JOptionPane.YES_NO_OPTION);
            if ( result == JOptionPane.YES_OPTION){
                snows.clear();
                createListSnow();
                snowBars.clear();
                createListSnowBar();
                createPlayer();
            }else {
                System.exit(0);
            }

        }
    }

    void movePlayerX() {
        requestFocus();
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();
                if (code == 37) {
                    isPressLeft = true;
                }
                if (code == 39) {
                    isPressRight = true;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int code = e.getKeyCode();
                if (code == 37) {
                    isPressLeft = false;
                }
                if (code == 39) {
                    isPressRight = false;
                }
            }
        });
    }

    void createThread() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    long curr = System.currentTimeMillis();
                    moveSnow(curr);
                    moveSnowBar(curr);
                    movePlayer(curr);
                    checkDie();
                    repaint();
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Thread t = new Thread(r);
        t.start();
    }

    Snow createSnow() {
        Snow snow = new Snow();
        Random rd = new Random();
        snow.w = 10;
        snow.h = 10;
        snow.x = rd.nextInt(500);
        snow.y = rd.nextInt(800);
        return snow;
    }

    void createListSnowBar() {
        for (int i = 0; i < 10; i++) {
            int y = 800 + 85 * i;
            SnowBar snowBar = createSnowBar();
            snowBar.y = y;
            snowBars.add(snowBar);
        }
    }

    SnowBar createSnowBar() {
        SnowBar snowBar = new SnowBar();
        snowBar.w = new Random().nextInt(31) + 40;
        snowBar.h = 15;
        snowBar.y = 800;
        snowBar.x = new Random().nextInt(
                500 - snowBar.w);
        snowBar.img =
                new ImageIcon(
                        getClass().getResource("/imgs/tas3.png")
                ).getImage();
        snowBar.delay = 10;
        return snowBar;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );
        Image image = new ImageIcon(
                getClass().getResource("/imgs/nen1.png")
        ).getImage();
        g2d.drawImage(image, 0, 0, 500, 800,
                null);

        for (int i = 0; i < snowBars.size(); i++) {
            SnowBar sB = snowBars.get(i);
            sB.draw(g2d);
        }

        for (int i = 0; i < snows.size(); i++) {
            Snow s = snows.get(i);
            s.draw(g2d);
        }
        player.draw(g2d);

    }


}
