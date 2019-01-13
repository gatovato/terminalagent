package com.terminalagent.terminalagent;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by gatovato on 10/2/2017.
 */

public class Blocks extends GameEntity {

  //used to create deletion animation
    private Bitmap deletion;
    private int deletionFrameCount = 6;
    private int deletionCurrentFrame;
    private long deletionLastFrameChangeTime = 0;
    private int deletionFrameLengthInMilliseconds = 0;
      //is it being deleted?
    private boolean deleting;
      //is it time for everthing to be gone? and remove animating images
    private boolean gone;
    private int deletionFrameWidth;
    private int deletionFrameHeight;
    private Rect deletionFrameToDraw;
  //


    public Blocks (Context context, String imgId, float xPos, float yPos, float tileWidth, float tileHeight){

        this.context = context;
        this.imgId = imgId;

        //so imgId that matches R.drawable name can be input in and an ID can be generated
        resourceId = context.getResources().getIdentifier(imgId,"drawable",context.getPackageName());

        this.xPos = xPos;
        this.yPos = yPos;

        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;

        frameWidth = Math.round(tileWidth);
        frameHeight = Math.round(tileHeight);

        deletionFrameWidth = frameWidth;
        deletionFrameHeight = frameHeight;


        frameToDraw = new Rect(0,0, frameWidth, frameHeight);
        whereToDraw = new RectF(xPos,yPos,xPos + tileWidth, yPos + tileHeight);

        stillImage = BitmapFactory.decodeResource(context.getResources(), resourceId);
        stillImage = Bitmap.createScaledBitmap(stillImage, frameWidth, frameHeight, false);

        deletionFrameToDraw = new Rect(0,0,deletionFrameWidth,deletionFrameHeight);
        deletion = BitmapFactory.decodeResource(context.getResources(), R.drawable.deletion);
        deletion = Bitmap.createScaledBitmap(deletion,deletionFrameWidth * deletionFrameCount,deletionFrameHeight,false);

        deleting = false;
        gone = false;

    }

    public Bitmap getDeletion(){
        return deletion;
    }

    //keep calculating RectF position, essential for drawing
    public void updateDeletion(){
        whereToDraw.left = xPos;
        whereToDraw.right = xPos + tileWidth;
        whereToDraw.top = yPos;
        whereToDraw.bottom = yPos + tileHeight;
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
