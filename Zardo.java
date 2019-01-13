package com.terminalagent.terminalagent;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.Random;

/**
 * Created by gatovato on 10/3/2017.
 */

//Has unique functionalities compared to other GameEntity's such as Enemy and Player, such as teleporting, animating
//second image and spawning enemies
public class Zardo extends GameEntity {

    //used for generating random position when Zardo teleports
    private Random generator = new Random();

    //star image when the spawning is about to happen
    private Bitmap spwnImg;

    //anything spwn refers the the star animation which indicates the enemies are respawning
    private float spwnX;
    private float spwnY;

    private float screenX;
    private float screenY;

    private float spwnTileWidth;
    private float spwnTileHeight;

    private int spwnFrameWidth;
    private int spwnFrameHeight;

    private Rect spwnFrameToDraw;
    private RectF spwnWhereToDraw;

    private int spwnFrameCount = 3;
    private int spwnCurrentFrame;

    private boolean spwning;

    //is Zardo facing left or right
    String facing;



    public Zardo(float x, float y, Context context, float tileWidth, float tileHeight, float screenX, float screenY,String facing){
        xPos = x;
        yPos = y;
        this.context = context;

        this.screenX = screenX;
        this.screenY = screenY;

        speed = tileWidth/10;
        frameCount = 9;

        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;

        this.facing = facing;

        frameWidth = Math.round(tileWidth);
        frameHeight = Math.round(tileHeight);


        leftImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.zardol);
        leftImage = Bitmap.createScaledBitmap(leftImage, frameWidth * frameCount, frameHeight,false);

        rightImage = BitmapFactory.decodeResource(context.getResources(),R.drawable.zardor);
        rightImage = Bitmap.createScaledBitmap(rightImage, frameWidth * frameCount, frameHeight, false);

        frameToDraw = new Rect(0,0, frameWidth, frameHeight);
        whereToDraw = new RectF(xPos,yPos,xPos + tileWidth, yPos + tileHeight);


        spwnFrameWidth = Math.round(tileWidth/4);
        spwnFrameHeight = Math.round(tileHeight/4);
        spwnTileWidth = Math.round(tileWidth/4);
        spwnTileHeight = Math.round(tileHeight/4);

        spwnFrameToDraw = new Rect(0,0,spwnFrameWidth, spwnFrameHeight);
        spwnWhereToDraw = new RectF(spwnX,spwnY, spwnX + spwnTileWidth, spwnY + spwnTileHeight);

        spwnImg = BitmapFactory.decodeResource(context.getResources(), R.drawable.spawnanime);
        spwnImg = Bitmap.createScaledBitmap(spwnImg, spwnFrameWidth * spwnFrameCount, spwnFrameHeight, false);


    }

    //based on a random number within the screen parameters find a new position for Zardo
    public void resetZardo(){

        float randomNumberX;
        float randomNumberY;

        float minX = screenX/100;
        float maxX = screenX;
        float minY = screenY/100;
        float maxY = screenY;

        randomNumberX = generator.nextFloat() * (maxX-minX)+minX;
        randomNumberY = generator.nextFloat() * (maxY-minY)+minY;

        xPos = randomNumberX;
        yPos = randomNumberY;

        whereToDraw.left = xPos;
        whereToDraw.right = xPos + tileWidth;
        whereToDraw.top = yPos;
        whereToDraw.bottom = yPos + tileHeight;

    }

    //animate star to display enemies are about to spawn
    public void getSpwnCurrentFrame(){
        long time = System.currentTimeMillis();
        if(time > lastFrameChangeTime + frameLengthInMilliseconds) {
            lastFrameChangeTime = time;
            spwnCurrentFrame++;
            if (spwnCurrentFrame >= spwnFrameCount) {
                spwnCurrentFrame = 0;
            }
        }
        spwnFrameToDraw.left = spwnCurrentFrame * spwnFrameWidth;
        spwnFrameToDraw.right = spwnFrameToDraw.left + spwnFrameWidth;

    }

    //update it so it can be drawn
    public void updateSpwnAnime(){
        switch (facing){
            case "left":
                spwnX = whereToDraw.left + (tileWidth/10);
                break;
            case "right":
                spwnX = whereToDraw.right + (tileWidth/10);
                break;
        }
        spwnY = whereToDraw.top;

        spwnWhereToDraw.left =spwnX;
        spwnWhereToDraw.top =spwnY;
        spwnWhereToDraw.right =spwnX + spwnTileWidth;
        spwnWhereToDraw.bottom =spwnY + spwnTileHeight;
    }

    public Rect getSpwnFrameToDraw(){ return spwnFrameToDraw;}
    public RectF getSpwnWhereToDraw(){ return spwnWhereToDraw;}
    public Bitmap getSpwnImg(){ return spwnImg;}

    //is Zardo spawning? If so update the spwn image and animation for drawing
    public boolean isSpwning(){return spwning;}
    public void setSpwning(boolean b){spwning = b;}

    public Bitmap getZardoLeftImg(){return leftImage;}
    public Bitmap getZardoRightImg(){return rightImage;}

    //when Zardo teleports change facing position relative to which side of the screen he is
    public void setFacing(String s){facing = s;}
    public String getFacing(){return facing;}

}
