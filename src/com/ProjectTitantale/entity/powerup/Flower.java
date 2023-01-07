package com.ProjectTitantale.entity.powerup;

import com.ProjectTitantale.Game;
import com.ProjectTitantale.Handler;
import com.ProjectTitantale.Id;
import com.ProjectTitantale.entity.Entity;

import java.awt.*;

public class Flower extends Entity {
    public Flower(int x, int y, int width, int height, Id id, Handler handler) {
        super(x, y, width, height, id, handler);
    }
    public void render(Graphics g){
        g.drawImage(Game.flower.getBufferredImage(),getX(),getY(),getWidth(),getHeight(), null);
    }

    public void tick(){

    }
}
