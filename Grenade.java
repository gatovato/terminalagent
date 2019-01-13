package com.terminalagent.terminalagent;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by gatovato on 11/3/2017.
 */
//Independent class, this is fro the reason which is the same as the Bullet class. I needed to add these to an arry of Grenades
//and delete them from the array after the explosion.

public class Grenade {

    //left or right after they are thrown
    private String heading;
    private float nadeWidth;
    private float nadeHeight;
    private long time;
    private float xPos;
    private float yPos;

    //grenade dimensions as a grenade when being thrown
    private int nadeFrameWidth;
    private int nadeFrameHeight;
    private Rect nadeFrameToDraw;
    private RectF nadeWhereToDraw;

    //explosion after grenade has exploded
    private float explodeWidth;
    private float explodeHeight;
    private int explodeFrameWidth;
    private int explodeFrameHeight;

    private Rect explodeFrameToDraw;
    private RectF explodeWhereToDraw;

    private Bitmap explodeImg;
    private Bitmap nadeImg;

    //how fast it moves left or right after being thrown.
    private float speed;

    //speed applied to grenade during pseudo-gravity
    private float fallSpeed;

    private float consFallSpeed;

    private float startX;

    private boolean visible;

    private float tileWidth;
    private float tileHeight;

    public Grenade(Context context, String heading, float xPos, float yPos, float nadeWidth,
                   float nadeHeight, long time, boolean visible, float speed, float tileWidth, float tileHeight){

        this.heading = heading;

        //once the grenade thrown (created) this time value is what is referenced by the corresponding world
        //and used to determine how much time has passed since it was thrown (for the explosion)

        this.time = time;

        this.visible = visible;

        this.xPos = xPos;
        this.yPos = yPos;


        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;

        this.speed = speed;
        fallSpeed = speed / 8;
        consFallSpeed = fallSpeed;

        this.startX = xPos;

        this.nadeWidth = nadeWidth;
        this.nadeHeight = nadeHeight;
        nadeFrameWidth = Math.round(nadeWidth);
        nadeFrameHeight = Math.round(nadeHeight);


        explodeWidth = nadeWidth;
        explodeHeight= nadeHeight;
        explodeFrameWidth = Math.round(explodeWidth);
        explodeFrameHeight = Math.round(explodeHeight);



        nadeFrameToDraw = new Rect(0,0,nadeFrameWidth,nadeFrameHeight);
        nadeWhereToDraw = new RectF(xPos,yPos,xPos + nadeWidth,yPos + nadeHeight);


        explodeFrameToDraw = new Rect(0,0, explodeFrameWidth, explodeFrameHeight);
        explodeWhereToDraw = new RectF(xPos,yPos,xPos + explodeWidth, yPos + explodeHeight);


        nadeImg  = BitmapFactory.decodeResource(context.getResources(), R.drawable.grenade);
        nadeImg = Bitmap.createScaledBitmap(nadeImg, nadeFrameWidth, nadeFrameHeight, false);

        explodeImg = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion);
        explodeImg = Bitmap.createScaledBitmap(explodeImg, explodeFrameWidth, explodeFrameHeight, false);

    }

    //what is being done when the grenade has been thrown.
    public void updateNade(boolean hitGround){

            switch (heading) {
                case "left":
                    if(!hitGround) {
                        //fall over to the left and down (because of gravity)
                        xPos = xPos - speed/2;
                        yPos = yPos + fallSpeed;
                    }else{
                        //hit ground and move slower because of friction
                        xPos = xPos - speed/6;
                    }
                    break;

                case "right":
                    //same as above except other direction
                    if(!hitGround) {
                        xPos = xPos + speed/2;
                        yPos = yPos + fallSpeed;
                    }else{
                        xPos = xPos + speed/6;
                    }
                    break;
            }

            //like everything update position of rectangle coordinates for drawing
            nadeWhereToDraw.left = xPos;
            nadeWhereToDraw.top = yPos;
            nadeWhereToDraw.right = xPos + nadeWidth;
            nadeWhereToDraw.bottom = yPos + nadeHeight;
    }

    //timer is calculated in corresponding world, but once the grenade is thrown and after the alloted time,
    //the world will make the grenade image disappear and will update the explodeWhereToDraw RectF for drawing
    public void updateExplosion(){
        explodeWhereToDraw.left = xPos;
        explodeWhereToDraw.top = yPos;
        explodeWhereToDraw.right = xPos + explodeWidth;
        explodeWhereToDraw.bottom = yPos + explodeHeight;
    }

    //used find out what was the start time (when was grenade thrown?)
    public long getTime(){return time;}

    public void setyPos(float y){yPos = y;}


    public Bitmap getNadeImg(){return  nadeImg;}
    public Bitmap getExplodeImg(){return explodeImg;}

    public Rect getNadeFrameToDraw(){return nadeFrameToDraw;}
    public RectF getNadeWhereToDraw(){return nadeWhereToDraw;}

    public Rect getExplodeFrameToDraw(){return explodeFrameToDraw;}
    public RectF getExplodeWhereToDraw(){return explodeWhereToDraw;}

    public void setVisible(boolean b){visible = b;}
    public boolean isVisible(){return visible;}

    public float getxPos(){return xPos;}
    public float getyPos(){return yPos;}

    public String getHeading(){return heading;}

    public float getNadeHeight(){return nadeHeight;}
    public float getNadeWidth(){return nadeWidth;}

    //used as constant speed for detecting collions in the Level class
    public float getConsFallSpeed(){return consFallSpeed;}
    //used to adjust the fall speed in the event the object will collide with the object
    public void setFallSpeed(float f){fallSpeed = f;}

    public void setHeading(String s){heading = s;}

}
