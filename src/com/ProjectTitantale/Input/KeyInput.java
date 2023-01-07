package com.ProjectTitantale.Input;

import com.ProjectTitantale.Game;
import com.ProjectTitantale.Id;
import com.ProjectTitantale.entity.Blizzard;
import com.ProjectTitantale.entity.Entity;
import com.ProjectTitantale.entity.Fireball;
import com.ProjectTitantale.states.PlayerState;
import com.ProjectTitantale.tile.Tile;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyInput implements KeyListener {

    private boolean fire;
    private boolean blizz;

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        for (int i = 0; i < Game.handler.entity.size(); i++) {
            Entity en = Game.handler.entity.get(i);
            if (en.getId() == Id.player) {
                if (en.goingDownPipe) return;
                switch (key) {
                    case KeyEvent.VK_W:
                        for (int q = 0; q < Game.handler.tile.size(); q++) {
                            Tile t = Game.handler.tile.get(q);
                            if (t.getId() == Id.pipe) {
                                if (en.getBoundsTop().intersects(t.getBounds())) {
                                    if (!en.goingDownPipe) en.goingDownPipe = true;
                                }
                            }
                            if (t.isSolid()) {
                                if (en.getBoundsBottom().intersects(t.getBounds())) {
                                    if (!en.jumping) {
                                        en.jumping = true;
                                        en.gravity = 9.0; //jumping height

                                        Game.jump.play();
                                    }
                                }
                            }
                        }

                        break;
                    case KeyEvent.VK_S:
                        for (int q = 0; q < Game.handler.tile.size(); q++) {
                            Tile t = Game.handler.tile.get(q);
                            if (t.getId() == Id.pipe) {
                                if (en.getBoundsBottom().intersects(t.getBounds())) {
                                    if (!en.goingDownPipe) en.goingDownPipe = true;
                                }
                            }
                        }
                        break;
                    case KeyEvent.VK_A:
                        en.setVelX(-5);
                        en.facing = 0;
                        break;
                    case KeyEvent.VK_D:
                        en.setVelX(5);
                        en.facing = 1;
                        break;
                    case KeyEvent.VK_SPACE:
                        if (/*en.state== PlayerState.FIRE&&*/!fire) {
                            switch (en.facing) {
                                case 0: //player facing left
                                    Game.handler.addEntity(new Fireball(en.getX() - 24, en.getY() + 12, 24, 24, Id.fireball, Game.handler, en.facing));
                                    fire = true;
                                    break;
                                case 1: //player facing left
                                    Game.handler.addEntity(new Fireball(en.getX() + en.getWidth(), en.getY() + 12, 24, 24, Id.fireball, Game.handler, en.facing));
                                    fire = true;
                                    break;
                            }
                        }
                    case KeyEvent.VK_E:
                        if (/*en.state== PlayerState.FIRE&&*/!fire) {
                            switch (en.facing) {
                                case 0: //player facing left
                                    Game.handler.addEntity(new Blizzard(en.getX() - 24, en.getY() , 48, 48, Id.blizzard, Game.handler, en.facing));
                                    blizz = true;
                                    break;
                                case 1: //player facing left
                                    Game.handler.addEntity(new Blizzard(en.getX() + en.getWidth(), en.getY(), 48, 48, Id.blizzard, Game.handler, en.facing));
                                    blizz = true;
                                    break;
                            }
                        }

                }
            }
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        for(int i=0; i<Game.handler.entity.size(); i++){
            Entity en =Game.handler.entity.get(i);
            if(en.getId()== Id.player) {
                switch (key) {
                    case KeyEvent.VK_W:
                        en.setVelY(0);
                        break;
                    case KeyEvent.VK_S:
                        en.setVelY(0);
                        break;
                    case KeyEvent.VK_A:
                        en.setVelX(0);
                        break;
                    case KeyEvent.VK_D:
                        en.setVelX(0);
                        break;
                    case KeyEvent.VK_SPACE:
                        fire = false;
                        break;
                    case KeyEvent.VK_E:
                        blizz = false;
                        break;
                }
            }
        }
    }

    public void keyTyped(KeyEvent e) {
        //not using
    }

}
