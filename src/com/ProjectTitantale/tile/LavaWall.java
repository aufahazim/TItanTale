package com.ProjectTitantale.tile;

import com.ProjectTitantale.Game;
import com.ProjectTitantale.Handler;
import com.ProjectTitantale.Id;

import java.awt.*;

public class LavaWall extends Tile{
    public LavaWall(int x, int y, int width, int height, boolean solid, Id id, Handler handler) {
        super(x, y, width, height, solid, id, handler);
    }

    public void render(Graphics g) {
        g.drawImage(Game.lavaWall.getBufferredImage(), x, y, width, height, null);
    }

    public void tick() {

    }
}
