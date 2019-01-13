package com.terminalagent.terminalagent;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by gatovato on 9/27/2017.
 */

//-----GameEntity-----
//This is what all objects within the Level are extended from. Using GameEntity common methods such as update(), clip() and animate()
//can be accomplished.

public  class GameEntity {

    public Context context;

    public int frameWidth;
    public int frameHeight;

    public float tileWidth;
    public float tileHeight;

    public float xPos;
    public float yPos;

    public Rect frameToDraw;
    public RectF whereToDraw;

    public Bitmap leftImage;
    public Bitmap rightImage;
    public Bitmap stillImage;

    public String imgId;
    public String imgId2;

    public int resourceId;
    public int resourceId2;

    public boolean clipped;
    public boolean isVisible;
    public float speed;

    public int frameCount;
    public int currentFrame;
    public long lastFrameChangeTime = 0;
    public int frameLengthInMilliseconds = 0;




    public Rect getFrameToDraw(){return frameToDraw;}

    public RectF getWhereToDraw(){return whereToDraw;}

    public void setXPos(float x){xPos = x;}

    public void setYPos(float y){yPos = y;};

    public void update(){
        whereToDraw.left = xPos;
        whereToDraw.right = xPos + tileWidth;
        whereToDraw.top = yPos;
        whereToDraw.bottom = yPos + tileHeight;
    }
    public void animate(){
        long time = System.currentTimeMillis();
        if (time > lastFrameChangeTime + frameLengthInMilliseconds) {
            lastFrameChangeTime = time;
            currentFrame++;
            if (currentFrame >= frameCount) {
                currentFrame = 0;
            }


        }
        frameToDraw.left = currentFrame * frameWidth;
        frameToDraw.right = frameToDraw.left + frameWidth;

    }


    //clip is used for ViewPort class. This determines if the entity is shown as the screen moves
    public void clip(){clipped = true;}
    public void unClip(){clipped = false;}
    public boolean isClipped(){return clipped;}

    //At rest each object would look weird if they were caught in mid animation. stillImage is particularly  useful for the Player class
    //becaues of all Entitys it the one that will be stopped frequently
    public Bitmap getStillImg(){return stillImage;}

    //Critical methods for determining entitys x and y positions
    public float getXpos(){return xPos;}
    public float getYpos(){return yPos;}

    //Each entity hsa a Rectangle drawn around it. This method returns the length of that Rectangle.
    public int getFrameHeight(){return frameHeight;}

}
