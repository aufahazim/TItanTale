package com.ProjectTitantale.tile;

import com.ProjectTitantale.Handler;
import com.ProjectTitantale.Id;

import java.awt.*;

public abstract class Tile {

    public int x, y; //this.x and this.y in line 11 and 12 refers to this x and y
    public int width, height;

    public boolean solid = false;
    public boolean activated = false;

    public int velX, velY;
    public int facing;

    public Id id;

    public Handler handler;

    public Tile(int x, int y, int width, int height, boolean solid, Id id, Handler handler){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.solid = solid;
        this.id = id;
        this.handler = handler;
    }

    public abstract void render(Graphics g);
    public abstract void tick();

    public void die(){
        handler.removeTile(this);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Id getId() {
        return id;
    }

    public boolean isSolid() {
        return solid;
    }

    public void setVelX(int velX) {
        this.velX = velX;
    }

    public void setVelY(int velY) {
        this.velY = velY;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Rectangle getBounds(){
        return new Rectangle(getX(), getY(), width, height);
    }
}
