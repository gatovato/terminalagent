package com.terminalagent.terminalagent;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by gatovato on 10/2/2017.
 */

public class Enemy extends GameEntity {

    private boolean movingRight;
    private boolean movingLeft;

    //determines whether enemy will fall to ground and walk from screen 0 - screen max or whether they will start in random
    //place above the ground

    private boolean flying;
    private long time;
    private long timer;

  //used to create deletion animation
    private Bitmap deletion;
    private int deletionFrameCount = 6;
    private int deletionCurrentFrame;
    private long deletionLastFrameChangeTime = 0;
    private int deletionFrameLengthInMilliseconds = 0;
    private boolean deleting;
    private boolean gone;
    private int deletionFrameWidth;
    private int deletionFrameHeight;
    private Rect deletionFrameToDraw;
  //

    //the only enemy which is currently capable of this is the Worm, any could be made able to go through Blocks witht his flag
    private boolean thruBlocks;


    public Enemy(Context context, String imgId, String imgId2, float xPos, float yPos, float tileWidth, float tileHeight, float speed, int frameCount, boolean movingLeft,
                 boolean flying, boolean thruBlocks){

        this.context = context;

        //left and right image
        this.imgId = imgId;
        this.imgId2 = imgId2;

        //so imgId that matches R.drawable name can be input in and an ID can be generated
        resourceId = context.getResources().getIdentifier(imgId,"drawable",context.getPackageName());
        resourceId2 = context.getResources().getIdentifier(imgId2, "drawable", context.getPackageName());

        this.xPos = xPos;
        this.yPos = yPos;

        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;

        frameWidth = Math.round(tileWidth);
        frameHeight = Math.round(tileHeight);

        deletionFrameWidth = frameWidth;
        deletionFrameHeight = frameHeight;

        this.speed = speed;

        this.frameCount = frameCount;

        this.movingLeft = movingLeft;

        this.flying = flying;

        this.thruBlocks = thruBlocks;

        leftImage = BitmapFactory.decodeResource(context.getResources(), resourceId);
        leftImage = Bitmap.createScaledBitmap(leftImage, frameWidth * frameCount, frameHeight,false);

        rightImage = BitmapFactory.decodeResource(context.getResources(), resourceId2);
        rightImage = Bitmap.createScaledBitmap(rightImage,frameWidth * frameCount, frameHeight, false);


        frameToDraw = new Rect(0,0, frameWidth, frameHeight);
        whereToDraw = new RectF(xPos,yPos,xPos + tileWidth, yPos + tileHeight);

        deletionFrameToDraw = new Rect(0,0,deletionFrameWidth,deletionFrameHeight);
        deletion = BitmapFactory.decodeResource(context.getResources(), R.drawable.deletion);
        deletion = Bitmap.createScaledBitmap(deletion,deletionFrameWidth * deletionFrameCount,deletionFrameHeight,false);

        deleting = false;
        gone = false;

    }

    //required to update RectF position for drawing, update position to relfect whichever direction enemy is moving
    public void update(){


        if(movingRight){
            movingRight = true;
            xPos = xPos + speed;
        }
        if(movingLeft){
            movingLeft = true;
            xPos = xPos - speed;
        }


        whereToDraw.left = xPos;
        whereToDraw.right = xPos + tileWidth;
        whereToDraw.top = yPos;
        whereToDraw.bottom = yPos + tileHeight;

    }

    public Bitmap getLeftImage() {
        return leftImage;
    }

    public Bitmap getRightImage() {
        return rightImage;
    }

    //when enemy collides with either screen 0 or screen max, or a Block, this will set the enemy moving in the opposite direction
    public void setMovingRight(boolean b){movingRight = b;}
    public void setMovingLeft(boolean b){movingLeft =b;}
    public boolean getMovingRight(){return movingRight;}
    public boolean getMovingLeft(){return movingLeft;}

    public float getTileHeight(){return tileHeight;}

    public boolean isFlying(){return flying;}

    public boolean isThruBlocks(){return thruBlocks;}

    public boolean getVisible(){return isVisible;}
    public void setVisisble(boolean b){isVisible = b;}

    public void setTime(long t){time = t;}
    public long getTime(){return time;}

    public void setTimer(long t){timer = t;}
    public long getTimer(){return timer;}


    public void updateDeletion(){
        whereToDraw.left = xPos;
        whereToDraw.right = xPos + tileWidth;
        whereToDraw.top = yPos;
        whereToDraw.bottom = yPos + tileHeight;
    }

    public Bitmap getDeletion(){
        return deletion;
    }

    public Rect getDeletionFrameToDraw(){return deletionFrameToDraw;}

    public boolean isDeleting(){return deleting;}
    public void setDeleting(boolean b){deleting = b;}

    public boolean isGone(){return gone;}

    public void animateDeletion(){
        long time = System.currentTimeMillis();
        if (time > deletionLastFrameChangeTime + deletionFrameLengthInMilliseconds) {
            deletionLastFrameChangeTime = time;
            deletionCurrentFrame++;
            if (deletionCurrentFrame >= deletionFrameCount) {
                gone = true;

            }


        }
        deletionFrameToDraw.left = deletionCurrentFrame * deletionFrameWidth;
        deletionFrameToDraw.right = deletionFrameToDraw.left + deletionFrameWidth;

    }


}
