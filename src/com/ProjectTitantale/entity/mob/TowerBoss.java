package com.ProjectTitantale.entity.mob;

import com.ProjectTitantale.Game;
import com.ProjectTitantale.Handler;
import com.ProjectTitantale.Id;
import com.ProjectTitantale.entity.Entity;
import com.ProjectTitantale.states.BossState;
import com.ProjectTitantale.tile.Tile;

import java.awt.*;
import java.util.Random;

public class TowerBoss extends Entity {

    public int jumpTime = 0;

    public boolean addJumpTime = false;
    //public boolean gameComplete = false;

    private Random random;

    public TowerBoss(int x, int y, int width, int height, Id id, Handler handler, int hp) {
        super(x, y, width, height, id, handler);
        this.hp = hp;

        bossState = BossState.IDLE;

        random = new Random();
    }

    public void render(Graphics g) {

        if(bossState==BossState.IDLE||bossState==BossState.SPINNING){
            if(facing==0){
                g.drawImage(Game.towerBoss[5].getBufferredImage(),x,y,width,height,null);
            }
            else if (facing==1){
                g.drawImage(Game.towerBoss[0].getBufferredImage(),x,y,width,height,null);
            }
        }
        else if(bossState==BossState.RECOVERING){
            if (facing ==0){
                g.drawImage(Game.towerBoss[4].getBufferredImage(),x,y,width,height,null);
            }
            else if (facing ==1){
                g.drawImage(Game.towerBoss[1].getBufferredImage(),x,y,width,height,null);
            }
        }
        else {
            if(facing ==0){
                g.drawImage(Game.towerBoss[3].getBufferredImage(),x,y,width,height,null);
            }
            else if (facing ==1){
                g.drawImage(Game.towerBoss[2].getBufferredImage(),x,y,width,height,null);
            }
        }
        /*
        if(Game.gameComplete){
            g.setColor(Color.BLACK);
            g.fillRect(0,0,getWidth(),getHeight());
            g.setColor(Color.WHITE);
            g.setFont(new Font("Courier",Font.BOLD,50));
            g.drawString("Congratulations. You have finished the Game!", 400, 400);
            g.drawString("Total Coins Collected: "+ Game.coins, 610, 400);
        }*/

    }

    public void tick() {
        x+=velX;
        y+=velY;

        if(hp<=0) {
            die();
            Game.gameFinished = true;
        }

        phaseTime++;

        if((phaseTime>=180&&bossState==BossState.IDLE)||(phaseTime>=600&&bossState!=BossState.SPINNING)) chooseState(); //180 equals to 3 seconds becouse 1 seconds is 60

        if(bossState==BossState.RECOVERING &&phaseTime>=180){
            bossState = BossState.SPINNING;
            phaseTime = 0;
        }

        if (phaseTime>=360 && bossState==BossState.SPINNING){
            phaseTime = 0;
            bossState = BossState.IDLE;
        }

        if(bossState==BossState.IDLE||bossState==BossState.RECOVERING){
            setVelX(0);
            setVelY(0);
        }

        if(bossState==BossState.JUMPING||bossState==BossState.RUNNING) attackable = true;
        else attackable=false;

        if(bossState!=BossState.JUMPING){
            addJumpTime = false;
            jumpTime = 0;
        }

        if(addJumpTime){
            jumpTime++;
            if(jumpTime>=30){
                addJumpTime = false;
                jumpTime=0;
            }

            if(!jumping&&!falling){
                jumping = true;
                gravity = 8.0;
            }
        }

        //detect collision
        for(int i=0; i<handler.tile.size();i++){
            Tile t =handler.tile.get(i);
            //if(!t.solid) break;
            //if(t.getId()==Id.wall){
            if (t.isSolid()&&!goingDownPipe) {
                if (getBoundsTop().intersects(t.getBounds())) {
                    setVelY(0);
                    if (jumping ) {
                        jumping = false;
                        gravity = 0.8;
                        falling = true;
                    }
                }
                if (getBoundsBottom().intersects(t.getBounds())) {
                    setVelY(0);
                    if (falling) {
                        falling = false;
                        addJumpTime = true;
                    }
                }
                if (getBoundsLeft().intersects(t.getBounds())) {
                    setVelX(0);
                    if(bossState==BossState.RUNNING) setVelX(4);
                    x = t.getX() + t.width;
                }
                if (getBoundsRight().intersects(t.getBounds())) {
                    setVelX(0);
                    if(bossState==BossState.RUNNING) setVelX(-4);
                    x = t.getX() - t.width;
                }
            }
        }

        for(int i=0; i<handler.entity.size();i++){
            Entity e = handler.entity.get(i);
            if(e.getId()==Id.player){
                if(bossState==BossState.JUMPING){
                    if(jumping||falling){
                        if(getX()>=e.getX()-4&&getX()<=e.getX()+4) setVelX(0);
                        else if(e.getX()<getX()) setVelX(-3);
                        else if (e.getX()>getX()) setVelX(3);
                    } else setVelX(0);
                } else if (bossState==BossState.SPINNING){
                    if(e.getX()<getX()) setVelX(-3);
                    else if (e.getX()>getX()) setVelX(3);
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

    }

    public void chooseState(){
        int nextPhase = random.nextInt(2);
        if(nextPhase==0){
            bossState = BossState.RUNNING;
            int dir = random.nextInt(2);
            if(dir==0) setVelX(-4);
            else setVelX(4);
        } else if (nextPhase==1){
            bossState = BossState.JUMPING;

            jumping = true;
            gravity = 8.0;
        }

        phaseTime = 0;
    }

}
