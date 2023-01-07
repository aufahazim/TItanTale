package com.ProjectTitantale.entity;

import com.ProjectTitantale.Game;
import com.ProjectTitantale.Handler;
import com.ProjectTitantale.Id;
import com.ProjectTitantale.tile.Tile;

import java.awt.*;

public class Coin extends Entity {
    public Coin(int x, int y, int width, int height, Id id, Handler handler) {
        super(x, y, width, height, id, handler);
    }

    public void render(Graphics g) {
        g.drawImage(Game.coin.getBufferredImage(),x,y,width,height,null);
    }

    public void tick() {

    }
}
