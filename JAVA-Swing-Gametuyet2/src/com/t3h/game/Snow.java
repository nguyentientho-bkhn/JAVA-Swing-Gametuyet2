package com.t3h.game;

import java.awt.*;

public class Snow extends Object2D {
    void draw(Graphics2D g2d){
        g2d.setColor(Color.WHITE);
        g2d.drawString("*", x, y);
    }
}
