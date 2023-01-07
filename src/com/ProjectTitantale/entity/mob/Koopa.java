package com.ProjectTitantale.entity.mob;

import com.ProjectTitantale.Game;
import com.ProjectTitantale.Handler;
import com.ProjectTitantale.Id;
import com.ProjectTitantale.entity.Entity;
import com.ProjectTitantale.states.KoopaState;
import com.ProjectTitantale.tile.Tile;

import java.awt.*;
import java.util.Random;

public class Koopa extends Entity {

    private Random random;

    private int shellCount;

    public Koopa(int x, int y, int width, int height, Id id, Handler handler) {
        super(x, y, width, height, id, handler);

        random = new Random();

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

        koopaState = KoopaState.WALKING;
    }

    public void render(Graphics g) {
        if(koopaState==KoopaState.WALKING) g.drawImage(Game.koopa[0].getBufferredImage(),x,y,width,height, null);
        else{
            g.drawImage(Game.koopa[1].getBufferredImage(),x,y,width,height, null);
        }
    }

    public void tick() {
        x+=velX;
        y+=velY;

        if(koopaState==KoopaState.SHELL) {
            setVelX(0);

            shellCount++;

            if(shellCount>=300){
                shellCount = 0;
                koopaState = KoopaState.WALKING;
            }

            if(koopaState==KoopaState.WALKING||koopaState==KoopaState.SPINNING){
                shellCount = 0;

                if(velX==0){
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
            }
        }

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
                if(koopaState==KoopaState.SPINNING){
                    setVelX(10);
                    facing = 1;
                }
                else setVelX(2);
            }
            if (getBoundsRight().intersects(t.getBounds())) {
                if(koopaState==KoopaState.SPINNING){
                    setVelX(-10);
                    facing = 0;
                }
                else setVelX(-2);
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
