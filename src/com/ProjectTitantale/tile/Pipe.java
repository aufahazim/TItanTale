package com.ProjectTitantale.tile;

import com.ProjectTitantale.Game;
import com.ProjectTitantale.Handler;
import com.ProjectTitantale.Id;
import com.ProjectTitantale.entity.mob.Plant;

import java.awt.*;

public class Pipe extends Tile{
    public Pipe(int x, int y, int width, int height, boolean solid, Id id, Handler handler, int facing, boolean plant) {
        super(x, y, width, height, solid, id, handler);
        this.facing = facing;

        if(plant) handler.addEntity(new Plant(getX(), getY()-64, getWidth(), 64, Id.plant, handler));
    }

    public void render(Graphics g) {
        g.drawImage(Game.pipe.getBufferredImage(),x,y,width,height,null);
       //g.setColor(new Color(128,128,128)); // pipe color
       // g.fillRect(x,y,width,height);
    }

    public void tick() {

    }

}
