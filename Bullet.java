package com.terminalagent.terminalagent;


import android.graphics.RectF;

/**
 * Created by gatovato on 10/30/2017.
 */

//This is a stand-alone class. It is a class because it needs to be added to an array list and didn't really share
//similar traits as other entitys

public class Bullet {

    //heading either left or right whent he bullet is shot (created)
    private String heading;

    private RectF bullet;
    private float xPos;
    private float yPos;
    private float width;
    private float height;
    private float tileWidth;

    //not used, legacy code
    private boolean visible;

    private float speed;

    public Bullet(String heading,float x, float y, float tileWidth, boolean visible,float speed){
        this.heading = heading;
        xPos = x;
        yPos = y;
        width = tileWidth/8;
        height = tileWidth/40;
        bullet = new RectF(x,y,xPos+width,yPos+height);
        this.tileWidth = tileWidth;
        this.visible = visible;
        this.speed = speed;
    }
    public void update(){
        if(heading == "left"){
            xPos = xPos - speed;
        }
        if(heading == "right"){
            xPos = xPos + speed;
        }
        bullet.set(xPos,yPos,xPos+width,yPos+height);
    }
    public RectF getBullet(){return bullet;}

    //not used, legacy code (too scared to remove ;) )
    public void setVisible(boolean b){visible = b;}

}
