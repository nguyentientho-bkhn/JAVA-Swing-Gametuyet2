package com.t3h.game;

import java.awt.*;

public class Player extends Object2D {
    void draw(Graphics2D g2d){
        g2d.drawImage(img, x, y, w, h, null);
    }
}
