package com.terminalagent.terminalagent;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by gatovato on 10/4/2017.
 */

public class Items extends GameEntity {

    private boolean using;

    private long time;

    //when the item is being used
    private Bitmap activeImg;

    //used for terminal, to determine which random block it's behind
    private int blockNum;


    public Items (Context context, String imgId, float xPos, float yPos, float tileWidth, float tileHeight, int frameCount, boolean isVisible) {

        this.context = context;
        this.imgId = imgId;
        resourceId = context.getResources().getIdentifier(imgId,"drawable",context.getPackageName());

        this.xPos = xPos;
        this.yPos = yPos;

        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;

        this.frameCount = frameCount;

        this.isVisible = isVisible;

        frameWidth = Math.round(tileWidth);
        frameHeight = Math.round(tileHeight);

        frameToDraw = new Rect(0,0, frameWidth, frameHeight);
        whereToDraw = new RectF(xPos,yPos,xPos + tileWidth, yPos + tileHeight);

        stillImage = BitmapFactory.decodeResource(context.getResources(), resourceId);
        stillImage = Bitmap.createScaledBitmap(stillImage, frameWidth, frameHeight, false);

        //cut animation sprite sheet down to frame by frame
        if(frameCount != 0) {
            activeImg = BitmapFactory.decodeResource(context.getResources(), resourceId);
            activeImg = Bitmap.createScaledBitmap(activeImg, frameWidth * frameCount, frameHeight, false);
        }

        speed = tileWidth/30;


    }

    //if the block hasn't been deleted item is invisible. If it has, make it visible
    public boolean getVisible(){return isVisible;}
    public void setVisisble(boolean b){isVisible = b;}


    //used to determine R.drawable ID to draw image
    public String getImgId(){return imgId;}

    //check to see if the item is being used, and to set the item to being used
    public void setUsing(boolean b){using = b;}
    public boolean isUsing(){return using;}

    //used for keeping track of how long it's been since the item has been used
    public void setTime(long t){time = t;}
    public long getTime(){return time;}

    //using item vs item being shown after removing block
    public Bitmap getActiveImg(){return activeImg;}

    //used for setting Block number for terminal
    public void setBlockNum(int b){blockNum = b;}


}
