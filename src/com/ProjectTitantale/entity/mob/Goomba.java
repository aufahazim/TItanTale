package com.ProjectTitantale.entity.mob;

import com.ProjectTitantale.Game;
import com.ProjectTitantale.Handler;
import com.ProjectTitantale.Id;
import com.ProjectTitantale.entity.Entity;
import com.ProjectTitantale.tile.Tile;

import java.awt.*;
import java.util.Random;

public class Goomba extends Entity {

    private Random random = new Random();

    public Goomba(int x, int y, int width, int height, Id id, Handler handler) {
        super(x, y, width, height, id, handler);

        int dir = random.nextInt(2);

        switch (dir) {
            case 0:
                setVelX(-2); //mushroom's speed
                facing = 0;
                break;
            case 1:
                setVelX(2); //mushroom's speed
                facing = 1;
                break;
        }
    }

    public void render(Graphics g){
        if(facing==0){               //frame+x , x refers to num of image frame in spreatsheet
            g.drawImage(Game.goomba[frame+4].getBufferredImage(), x, y, width, height, null);
        }else if(facing==1){
            g.drawImage(Game.goomba[frame].getBufferredImage(), x, y, width, height, null);
        }
    }

    public void tick(){
        x+=velX;
        y+=velY;

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
                facing = 1;
            }
            if (getBoundsRight().intersects(t.getBounds())) {
                setVelX(-2);
                facing = 0;
            }
        }
        if (falling) {
            gravity += 0.1;
            setVelY((int) gravity);
        }
        if(velX!=0) {
            frameDelay++;
            if (frameDelay >= 3) {
                frame++;
                if (frame >= 4) { // 2 refers to num of facing player image in spritesheet
                    frame = 0;
                }
                frameDelay = 0;
            }
        }
    }
}
