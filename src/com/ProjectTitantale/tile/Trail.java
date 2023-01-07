package com.ProjectTitantale.tile;

import com.ProjectTitantale.Handler;
import com.ProjectTitantale.Id;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Trail extends Tile{

    private float alpha = 1.0F; // transparancy

    private BufferedImage image;

    public Trail(int x, int y, int width, int height, boolean solid, Id id, Handler handler, BufferedImage image) {
        super(x, y, width, height, solid, id, handler);

        this.image = image;
    }

    public void render(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha));
        g.drawImage(image, getX(), getY(), getWidth(), getHeight(), null);
    }

    public void tick() {
        alpha -=0.05;

        if(alpha<0.06) die(); // delete trail
    }
}
