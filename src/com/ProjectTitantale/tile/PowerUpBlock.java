package com.ProjectTitantale.tile;

import com.ProjectTitantale.Game;
import com.ProjectTitantale.Handler;
import com.ProjectTitantale.Id;
import com.ProjectTitantale.entity.powerup.Flower;
import com.ProjectTitantale.entity.powerup.Mushroom;
import com.ProjectTitantale.gfx.Sprite;

import java.awt.*;

public class PowerUpBlock extends Tile{

    public Sprite powerUp;

    private boolean poppedUp = false;

    private int spriteY =getY();
    private int type;


    public PowerUpBlock(int x, int y, int width, int height, boolean solid, Id id, Handler handler, Sprite powerUp, int type) {
        super(x, y, width, height, solid, id, handler);
        this.type = type;
        this.powerUp = powerUp;
    }

    public void render(Graphics g){
        if(!poppedUp) g.drawImage(powerUp.getBufferredImage(), x, spriteY, width, height, null);
        if (!activated) g.drawImage(Game.powerUp.getBufferredImage(),x,y,width,height,null);
        else g.drawImage(Game.usedPowerUp.getBufferredImage(),x,y,width,height,null);
    }

    public void tick(){
        if(activated&&!poppedUp){
            spriteY--;
            if(spriteY<=y-height){
                if(powerUp==Game.mushroom||powerUp==Game.lifeMushroom){
                    handler.addEntity(new Mushroom(x,spriteY,width,height,Id.mushroom,handler, type));
                }
                else if (powerUp==Game.flower){
                    handler.addEntity(new Flower(x,spriteY,width,height,Id.flower,handler));
                }
                poppedUp = true;
            }
        }
    }

}
