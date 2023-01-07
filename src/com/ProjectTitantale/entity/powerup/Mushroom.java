package com.ProjectTitantale.entity.powerup;

import com.ProjectTitantale.Game;
import com.ProjectTitantale.Handler;
import com.ProjectTitantale.Id;
import com.ProjectTitantale.entity.Entity;
import com.ProjectTitantale.tile.Tile;

import java.awt.*;
import java.util.Random;

public class Mushroom extends Entity {

    private Random random = new Random();

    public Mushroom(int x, int y, int width, int height, Id id, Handler handler, int type) {
        super(x, y, width, height, id, handler);
        this.type = type;

        int dir = random.nextInt(2);

        switch (dir) {
            case 0:
                setVelX(-2); //mushroom's speed
                break;
            case 1:
                setVelX(2); //mushroom's speed
                break;
        }
    }

    public void render(Graphics g) {
        switch (getType()){
            case 0:
                g.drawImage(Game.mushroom.getBufferredImage(), x, y, width, height, null);
                break;
            case 1:
                g.drawImage(Game.lifeMushroom.getBufferredImage(), x, y, width, height, null);
                break;
        }
    }

    public void tick() {
        x += velX;
        y += velY;

        for (int i=0; i< handler.tile.size(); i++) {
            Tile t = handler.tile.get(i);
            if (!t.solid) break;
            if (t.isSolid()) {
                if (getBoundsBottom().intersects(t.getBounds())) {
                    setVelY(0);
                    if (falling) falling = false;
                } else if(!falling&&!jumping){
                        gravity = 0.8;
                        falling = true;
                    }
                }
                if (getBoundsLeft().intersects(t.getBounds())) {
                    setVelX(2);
                }
                if (getBoundsRight().intersects(t.getBounds())) {
                    setVelX(-2);
                }
            }
            if (falling) {
                gravity += 0.1;
                setVelY((int) gravity);
            }
        }
    }