package com.ProjectTitantale.tile;

import com.ProjectTitantale.Game;
import com.ProjectTitantale.Handler;
import com.ProjectTitantale.Id;

import java.awt.*;

public class Flag extends Tile{
    public Flag(int x, int y, int width, int height, boolean solid, Id id, Handler handler) {
        super(x, y, width, height, solid, id, handler);
    }

    public void render(Graphics g) { //draw flag
       // g.drawImage(Game.flag[1].getBufferredImage(),getX(),getY(), width, 64, null);
        g.drawImage(Game.flag.getBufferredImage(),getX(),getY(), width+32, 128, null);

       // g.drawImage(Game.flag[2].getBufferredImage(),getX(), getY()+64,width,64, null);
       // g.drawImage(Game.flag[2].getBufferredImage(),getX(), getY()+128,width,64, null);
       // g.drawImage(Game.flag[2].getBufferredImage(),getX(), getY()+192,width,64, null);

      //  g.drawImage(Game.flag[0].getBufferredImage(),getX(),getY()+height-64,width,64,null);
    }

    public void tick() {

    }

}
