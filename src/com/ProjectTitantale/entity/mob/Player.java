package com.ProjectTitantale.entity.mob;

import com.ProjectTitantale.Game;
import com.ProjectTitantale.Handler;
import com.ProjectTitantale.Id;
import com.ProjectTitantale.entity.Entity;
import com.ProjectTitantale.entity.Particle;
import com.ProjectTitantale.states.BossState;
import com.ProjectTitantale.states.KoopaState;
import com.ProjectTitantale.states.PlayerState;
import com.ProjectTitantale.tile.Tile;
import com.ProjectTitantale.tile.Trail;

import java.awt.*;
import java.util.Random;

public class Player extends Entity {

    private PlayerState state;

    private int pixelsTravelled = 0;
    private int invincibilityTime = 0;
    private int particleDelay = 0;
    private int restoreTime;

    private Random random;

    private boolean invincible =false;

    private boolean restoring;


    public Player(int x, int y, int width, int height, Id id, Handler handler) {
        super(x, y, width, height, id, handler);
        //setVelX(1); // movement speed

        state = PlayerState.SMALL;

        random = new Random();

    }

    private int frame = 0;
    private int frameDelay =0;

    private boolean animate = false;

    public void render(Graphics g) {
        if(state==PlayerState.FIRE){
            if(facing==0){               //frame+x , x refers to num of image frame in spreatsheet
                g.drawImage(Game.firePlayer[frame+4].getBufferredImage(), x, y, width, height, null);
            }else if(facing==1){
                g.drawImage(Game.firePlayer[frame].getBufferredImage(), x, y, width, height, null);
            }
        }else{
            if(facing==0){               //frame+x , x refers to num of image frame in spreatsheet
                g.drawImage(Game.player[frame+4].getBufferredImage(), x, y, width, height, null);
            }else if(facing==1){
                g.drawImage(Game.player[frame].getBufferredImage(), x, y, width, height, null);
            }
        }
    }

    public void tick() {
        x+=velX;
        y+=velY;

        if(invincible){
            if(facing ==0){
                handler.addTile(new Trail(getX(), getY(), getWidth(), getHeight(),false, Id.trail, handler, Game.player[4+frame].getBufferredImage()));
            }
            else if (facing==1){
                handler.addTile(new Trail(getX(), getY(), getWidth(), getHeight(),false, Id.trail, handler, Game.player[frame].getBufferredImage()));
            }

            particleDelay++;
            if(particleDelay>=3){
                handler.addEntity(new Particle(getX()+ random.nextInt(getWidth()), getY() + random.nextInt(getHeight()), 10, 10, Id.particle, handler));

                particleDelay = 0;
            }

            invincibilityTime++;
            if(invincibilityTime>=600){ // 10 seconds
                invincible = false;
                invincibilityTime = 0;
            }

            if(velX==5) setVelX(8);
            else if(velX==-5) setVelX(-8);
        } else {
            if(velX==8) setVelX(5);
            else if(velX==-8) setVelX(-5);
        }
        // do not want the player to out of bound of the screen
        //if(x<=0) x=0;
        //if(x+width>=1080) x=1080-width;
        //if(y+height>=771) y = 771-height;
        //if(velX!=0) animate = true;
        //else animate=false;

        if(restoring){
            restoreTime++;

            if(restoreTime>=90){ //1.5 seconds
                restoring = false;
                restoreTime = 0;
            }
        }

        //detect collision
        for(int i=0; i<handler.tile.size();i++){
            Tile t =handler.tile.get(i);
            //if(!t.solid) break;
            //if(t.getId()==Id.wall){
            if(getBounds().intersects(t.getBounds())){
                if(t.getId()==Id.flag){
                    Game.switchLevel();
                }
            }
            if (t.isSolid()&&!goingDownPipe) {
                if (getBoundsTop().intersects(t.getBounds())) {
                    setVelY(0);
                    if (jumping && !goingDownPipe) {
                        jumping = false;
                        gravity = 0.8;
                        falling = true;
                    }
                    if (t.getId() == Id.powerUp) {
                        if (getBoundsTop().intersects(t.getBounds())) t.activated = true;
                    }
                }
                if (getBoundsBottom().intersects(t.getBounds())) {
                    setVelY(0);
                    if (falling) falling = false;
                } else {
                    if (!falling && !jumping) {
                        gravity = 0.8;
                        falling = true;
                    }
                }
                if (getBoundsLeft().intersects(t.getBounds())) {
                    setVelX(0);
                    x = t.getX() + t.width;
                }
                if (getBoundsRight().intersects(t.getBounds())) {
                    setVelX(0);
                    x = t.getX() - width;
                }
            }
        }


        for(int i=0; i<handler.entity.size(); i++){
            Entity e = handler.entity.get(i);

            if (e.getId()==Id.mushroom){
                switch (e.getType()){
                    case 0:
                        if(getBounds().intersects(e.getBounds())){
                            int tpX = getX();
                            int tpY = getY();
                            width+=(width/3); // make player big
                            height+=(width/3);
                            setX(tpX-width);
                            setY(tpY-height);
                            if(state ==PlayerState.SMALL) state = PlayerState.BIG;
                            e.die();
                         }
                        break;
                    case 1:
                        if(getBounds().intersects(e.getBounds())){
                            Game.lives++;
                            e.die();
                        }
                }
            } else if (e.getId()== Id.goomba || e.getId() == Id.towerBoss||e.getId()==Id.plant){
               if(invincible&&getBounds().intersects(e.getBounds())) e.die();
               else {
                   if(getBoundsBottom().intersects(e.getBoundsTop())){ // kill goomba
                       if(e.getId()!=Id.towerBoss){
                           e.die();

                           Game.goombaStomp.play();
                       }
                       else if(e.attackable){
                           e.hp--;
                           e.falling = true;
                           e.gravity = 3.0;
                           e.bossState = BossState.RECOVERING;
                           e.attackable = false;
                           e.phaseTime = 0;
                           //make player bounds after hit boss
                           jumping = true;
                           falling = false;
                           gravity = 5.5;
                       }
                   } else if(getBounds().intersects(e.getBounds())){
                       if(state==PlayerState.BIG || state ==PlayerState.SMALL || state==PlayerState.FIRE){
                           takeDamage();
                       }
               }

                }
            } else if(e.getId()==Id.coin){
                if (getBounds().intersects(e.getBounds())&&e.getId()==Id.coin){
                    Game.coins++;
                    e.die();
                }
            } else if (e.getId()==Id.koopa){
                if(invincible&&getBounds().intersects(e.getBounds())) e.die();
                else {
                    if(e.koopaState== KoopaState.WALKING){
                        if(getBoundsBottom().intersects(e.getBoundsTop())){
                            e.koopaState = KoopaState.SHELL;
                            //make player bounds after hit koopa
                            jumping = true;
                            falling = false;
                            gravity = 5.5;
                        } else if(getBounds().intersects(e.getBounds())) takeDamage();

                    } else if (e.koopaState==KoopaState.SHELL){
                        if(getBoundsBottom().intersects(e.getBoundsTop())){
                            e.koopaState = KoopaState.SPINNING;

                            int dir = random.nextInt(2);

                            switch (dir) {
                                case 0:
                                    e.setVelX(-10); //koopa spinning's speed
                                    facing = 0;
                                    break;
                                case 1:
                                    e.setVelX(10); //koopa spinning's speed
                                    facing = 1;
                                    break;
                            }

                            //make player bounds after hit koopa
                            jumping = true;
                            falling = false;
                            gravity = 3.5;
                        }

                        if(getBoundsLeft().intersects(e.getBoundsRight())){
                            e.setVelX(-10);
                            e.koopaState = KoopaState.SPINNING;
                        }
                        if(getBoundsRight().intersects(e.getBoundsLeft())){
                            e.setVelX(10);
                            e.koopaState = KoopaState.SPINNING;
                        }
                    } else if (e.koopaState==KoopaState.SPINNING){
                        if(getBoundsBottom().intersects(e.getBoundsTop())){
                            e.koopaState = KoopaState.SHELL;
                            //make player bounds after hit koopa
                            jumping = true;
                            falling = false;
                            gravity = 5.5;
                        } else if(getBounds().intersects(e.getBounds())) takeDamage();
                    }
                }

            } else if (e.getId()==Id.star) {
                if (getBounds().intersects(e.getBounds())) {
                    invincible = true;
                    e.die();
                }
            } else if(e.getId()==Id.flower){
                if(getBounds().intersects(e.getBounds())){
                    if(state==PlayerState.SMALL){
                        int tpX = getX();
                        int tpY = getY();
                        width+=(width/3); // make player big
                        height+=(width/3);
                        setX(tpX-width);
                        setY(tpY-height);
                    }
                    state = PlayerState.FIRE;
                    e.die();
                }
            }
        }

        if (jumping&&!goingDownPipe){
            gravity-=0.17;
            setVelY((int)-gravity);
            if(gravity<=0.5){
                jumping = false;
                falling = true;
            }
        }
        if(falling&&!goingDownPipe){
            gravity+=0.17;
            setVelY((int)gravity);
        }

        if(velX!=0) {
            frameDelay++;
            if (frameDelay >= 3) {
                frame++;
                if (frame >= 4) { // 4 refers to num of facing player image in spritesheet
                    frame = 0;
                }
                frameDelay = 0;
            }
        }

        if(goingDownPipe){
            for(int i=0; i<Game.handler.tile.size(); i++){
                Tile t = Game.handler.tile.get(i);
                if(t.getId()==Id.pipe){
                    if (getBounds().intersects(t.getBounds())){
                        switch (t.facing){
                            case 0:
                                setVelY(-5);
                                setVelX(0);
                                pixelsTravelled+=-velY;
                                break;
                            case 2:
                                setVelY(5);
                                setVelX(0);
                                pixelsTravelled+=velY;
                                break;
                        }
                        if(pixelsTravelled>=t.height+height){
                            goingDownPipe = false;
                            pixelsTravelled = 0;
                        }
                    }
                }
            }
        }
    }

    public void takeDamage(){
        if(restoring) return;

        if(state==PlayerState.SMALL){
            die();
            return;
        }
        else if (state==PlayerState.BIG){
            width-=(width/4); //make player small
            height-=(height/4);
            x+=width/4;//to stay on ground after getting small
            y+=height/4;//to stay on ground after getting small

            state = PlayerState.SMALL;
            restoring = true;
            restoreTime = 0;
            return;
        }
        else if (state==PlayerState.FIRE){
            state = PlayerState.BIG;

            restoring = true;
            restoreTime = 0;
            return;
        }
    }
}
