package com.terminalagent.terminalagent;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by gatovato on 10/1/2017.
 */

//many different abilities/functionalities separate the Player from Enemies

public class Player extends GameEntity {

    //this is the jumping (jetpack on) images
    private Bitmap homieJumpLeft;
    private Bitmap homieJumpRight;

    //when the player isn't moving in order to make the feet not look weird, these still images are displayed
    private Bitmap stillHomie;
    private Bitmap stillHomieLeft;

    //is the player moving, jumping, or falling?
    private boolean movingLeft;
    private boolean movingRight;
    private boolean jumping;
    private boolean falling;

    //is the player facing left or right?
    private String facing;

    private Rect homieRect;

    //used for calculating pseudo-gravity and jumping/jumping-arc speed
    private float fallSpeed;
    private float archSpeed;
    private float jumpSpeed;


    public Player (float x,float y,Context context,float tileWidth, float tileHeight, int frameCount, String facing, float speed, float fallSpeed,float archSpeed,float jumpSpeed){

        xPos = x;
        yPos = y;

        this.context = context;

        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;

        this.frameCount = frameCount;

        this.facing = facing;

        frameWidth = Math.round(tileWidth);
        frameHeight = Math.round(tileHeight);

        this.speed = speed;
        this.jumpSpeed = jumpSpeed;
        this.fallSpeed = fallSpeed;
        this.archSpeed = archSpeed;

        homieRect = new Rect(0,0,frameWidth,frameHeight);


        frameToDraw = new Rect(0,0,frameWidth,frameHeight);
        whereToDraw = new RectF(xPos,yPos,xPos + tileWidth, yPos + tileHeight);

        rightImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.homeslyyz);
        rightImage = Bitmap.createScaledBitmap(rightImage, frameWidth * frameCount, frameHeight, false);

        leftImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.homeslyyzleft);
        leftImage = Bitmap.createScaledBitmap(leftImage, frameWidth * frameCount, frameHeight, false);

        homieJumpLeft = BitmapFactory.decodeResource(context.getResources(), R.drawable.homeslyyzjumpl);
        homieJumpLeft = Bitmap.createScaledBitmap(homieJumpLeft, frameWidth, frameHeight, false);

        homieJumpRight = BitmapFactory.decodeResource(context.getResources(), R.drawable.homeslyyzjump);
        homieJumpRight = Bitmap.createScaledBitmap(homieJumpRight, frameWidth, frameHeight, false);

        stillHomie = BitmapFactory.decodeResource(context.getResources(), R.drawable.stillhomie);
        stillHomie = Bitmap.createScaledBitmap(stillHomie,frameWidth,frameHeight,false);

        stillHomieLeft = BitmapFactory.decodeResource(context.getResources(), R.drawable.stillhomieleft);
        stillHomieLeft = Bitmap.createScaledBitmap(stillHomieLeft,frameWidth,frameHeight,false);




    }


    public void update(){

        if (movingRight) {
            xPos = xPos + speed;
        }
        if (movingLeft) {
            xPos = xPos - speed;
        }
        //here's where the slight arch gomes in when jumping
        if (jumping) {
            yPos = yPos - jumpSpeed;
            if(facing == "right") {
                xPos = xPos + (archSpeed);
            }else{
                xPos = xPos - (archSpeed);
            }
        }
        if(falling){
            yPos = yPos + fallSpeed;
        }

        whereToDraw.left = xPos;
        whereToDraw.right = xPos + tileWidth;
        whereToDraw.top = yPos;
        whereToDraw.bottom = yPos + tileHeight;

    }



    public Bitmap getLeftImage(){
        return leftImage;
    }

    public Bitmap getRightImage(){
        return rightImage;
    }

    public Bitmap getHomieJumpLeft(){ return  homieJumpLeft;}

    public Bitmap getHomieJumpRight(){ return homieJumpRight;}

    public Bitmap getStillHomie(){ return  stillHomie;}
    public Bitmap getStillHomieLeft(){return stillHomieLeft;}


    public void setJumping(boolean b){jumping = b;}
    public void setMovingLeft(boolean b){movingLeft = b;}
    public void setMovingRight(boolean b){movingRight = b;}


    public boolean getJumping(){return jumping;}
    public boolean getMovingLeft(){return movingLeft;}
    public boolean getMovingRight(){return movingRight;}


    public String getFacing(){return facing;}
    public void setFacing(String s){facing = s;}

    public Rect getHomieRect(){return homieRect;}

    //this is used for object collision with blocks (for more detail, see description in Level)
    public void setSpeed(float f){speed = f;}

    //also used for object collision with blocks
    public void setFallSpeed(float f){fallSpeed = f;}
    public void setFalling(boolean b){falling = b;}
    public boolean isFalling(){return falling;}

    //also used for object collision with blocks
    public void setArchSpeed(float f){archSpeed = f;}
    public void setJumpSpeed(float f){jumpSpeed = f;};






}
