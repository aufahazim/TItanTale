package com.ProjectTitantale.entity.powerup;

import com.ProjectTitantale.Game;
import com.ProjectTitantale.Handler;
import com.ProjectTitantale.Id;
import com.ProjectTitantale.entity.Entity;
import com.ProjectTitantale.tile.Tile;

import java.awt.*;
import java.util.Random;

public class PowerStar extends Entity {

    private Random random;

    public PowerStar(int x, int y, int width, int height, Id id, Handler handler) {
        super(x, y, width, height, id, handler);
        random = new Random();

        int dir = random.nextInt(2);

        switch (dir) {
            case 0:
                setVelX(-4); //powerStar's speed
                break;
            case 1:
                setVelX(4); //powerStar's speed
                break;
        }
        falling = true;
        gravity = 0.17;

    }

    public void render(Graphics g) {
        g.drawImage(Game.star.getBufferredImage(),getX(),getY(),getWidth(), getHeight(), null);
    }

    public void tick() {
        x+=velX;
        y+=velY;

        //to make star powerUp jump
        for(int i =0; i<handler.tile.size(); i++){
            Tile t = handler.tile.get(i);
            if(t.isSolid()){
                if(getBoundsBottom().intersects(t.getBounds())){
                    falling = false;
                    jumping = true;
                    gravity = 9.0;
                }

                if(getBoundsLeft().intersects(t.getBounds())) setVelX(4);
                if(getBoundsRight().intersects(t.getBounds())) setVelX(-4);
            }
        }

        if (jumping){
            gravity-=0.17;
            setVelY((int)-gravity);
            if(gravity<=0.5){
                jumping = false;
                falling = true;
            }
        }

        if(falling){
            gravity+=0.17;
            setVelY((int)gravity);
        }
    }
}
