package com.terminalagent.terminalagent;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

/**
 * Created by gatovato on 9/20/2017.
 */
//Used for all scenes but the end scenes
public class Scene implements World {

    //in a regular scene there is only going to be 2 rectangles, one for the person's avitar that's talking
    //and one for the dialouge
    //if the screen is touched any where it will move on to the next level
    //to keep items persistent they are passed through the scenes via GameLoop
    private int frameWidth1;
    private int frameHeight1;

    private int frameWidth2;
    private int frameHeight2;


    private float tileWidth1;
    private float tileHeight1;

    private float tileWidth2;
    private float tileHeight2;


    private Bitmap image1;
    private Bitmap image2;


    private float screenX;
    private float screenY;

    private float xPos1;
    private float yPos1;

    private float xPos2;
    private float yPos2;

    private Context context;
    private SurfaceHolder ourHolder;
    private Canvas canvas;
    private Paint paint;


    private boolean alive = true;

    private boolean nextLevel;



    private Rect frameToDraw1 = new Rect();
    private RectF whereToDraw1 = new RectF();

    private Rect frameToDraw2 = new Rect();
    private RectF whereToDraw2 = new RectF();

    private String imgId;
    private String imgId2;

    private int resourceId;
    private int resourceId2;

    private int escalate;

    private boolean secretLevel = false;

    private int numNades;
    private int numGains;
    private int numShields;


    private int frameCount;
    private int currentFrame;
    private long lastFrameChangeTime = 0;
    private int frameLengthInMilliseconds = 0;
    private boolean animating;


    private MusicLibrary song;
    private MediaPlayer mediaPlayer;

    private Rect musicFrameToDraw = new Rect();
    private RectF musicWhereToDraw = new RectF();

    private float musicXPos;
    private float musicYPos;

    private Bitmap musicOn;
    private Bitmap musicOff;

    private float musicWidth;
    private float musicHeight;

    //this is important in the first scene because it's one of 2 ways the sound can be disabled for the game
    //the other method is through the terminal
    private boolean playingMusic;
    private boolean isReleased = false;

    public Scene(String imgId, String imgId2, float screenX, float screenY, Context context, SurfaceHolder surfaceHolder, float tileWidth1, float tileHeight1, float tileWidth2,
                 float tileHeight2,float xPos1, float yPos1, float xPos2, float yPos2, int escalate, int numNades, int numGains, int numShields, boolean animating, int frameCount,
                 int numWorld,boolean playingMusic) {

        this.context = context;

        this.imgId = imgId;
        this.imgId2 = imgId2;


        resourceId = context.getResources().getIdentifier(imgId,"drawable",context.getPackageName());
        resourceId2 = context.getResources().getIdentifier(imgId2, "drawable", context.getPackageName());

        this.screenX = screenX;
        this.screenY = screenY;



        ourHolder = surfaceHolder;
        paint = new Paint();

        this.escalate = escalate;

        this.tileWidth1 = tileWidth1;
        this.tileHeight1 = tileHeight1;

        this.tileWidth2 = tileWidth2;
        this.tileHeight2 = tileHeight2;


        frameWidth1 = (Math.round(tileWidth1));
        frameHeight1 = (Math.round(tileHeight1));

        frameWidth2 = (Math.round(tileWidth2));
        frameHeight2 = (Math.round(tileHeight2));


        this.xPos1 = xPos1;
        this.yPos1 = yPos1;

        this.xPos2 = xPos2;
        this.yPos2 = yPos2;

        this.animating = animating;
        this.frameCount = frameCount;

        this.playingMusic = playingMusic;

        image1 = BitmapFactory.decodeResource(context.getResources(), resourceId);
        image1 = Bitmap.createScaledBitmap(image1, frameWidth1, frameHeight1, false);

        if(animating){
            image2 = BitmapFactory.decodeResource(context.getResources(), resourceId2);
            image2 = Bitmap.createScaledBitmap(image2,frameWidth2 * frameCount, frameHeight2, false);
        }else {
            image2 = BitmapFactory.decodeResource(context.getResources(), resourceId2);
            image2 = Bitmap.createScaledBitmap(image2, frameWidth2, frameHeight2, false);
        }


        frameToDraw1.set(0, 0, frameWidth1, frameHeight1);
        whereToDraw1.set(xPos1, yPos1, xPos1 + tileWidth1, yPos1 + tileHeight1);

        frameToDraw2.set(0,0,frameWidth2, frameHeight2);
        whereToDraw2.set(xPos2, yPos2, xPos2 + tileWidth2, yPos2 + tileHeight2);

        this.numGains = numGains;
        this.numNades = numNades;
        this.numShields = numShields;

        //get song for scene number, initiate song
        song = new MusicLibrary(context,numWorld);
        int rId = song.getSong();
        mediaPlayer = MediaPlayer.create(context,rId);
        mediaPlayer.setLooping(true);

        musicXPos = xPos2 + (tileWidth2 * 2);
        musicYPos = yPos2 + (tileHeight2/6);

        musicWidth = tileWidth2 - (tileWidth2/4);
        musicHeight = musicWidth;

        musicFrameToDraw.set(0,0,Math.round(musicWidth),Math.round(musicHeight));
        musicWhereToDraw.set(musicXPos,musicYPos,musicXPos + musicWidth,musicYPos + musicHeight);

        musicOn = BitmapFactory.decodeResource(context.getResources(),R.drawable.soundon);
        musicOn = Bitmap.createScaledBitmap(musicOn,Math.round(musicWidth),Math.round(musicHeight),false);

        musicOff = BitmapFactory.decodeResource(context.getResources(),R.drawable.soundoff);
        musicOff = Bitmap.createScaledBitmap(musicOff,Math.round(musicWidth),Math.round(musicHeight),false);

        //will default to true for level 1 and be toggled there and can be changed throughout the game via the terminal
        if(playingMusic) {
            mediaPlayer.start();
        }

    }


    public void update(){

            //update both images for drawing
            whereToDraw1.left = xPos1;
            whereToDraw1.top = yPos1;
            whereToDraw1.right = xPos1 + tileWidth1;
            whereToDraw1.bottom = yPos1 + tileHeight1;

            whereToDraw2.left = xPos2;
            whereToDraw2.top = yPos2;
            whereToDraw2.right = xPos2 + tileWidth2;
            whereToDraw2.bottom = yPos2 + tileHeight2;

            //on the first scene, there is 3 images, with the 3rd being the sound toggle button
            if(imgId == "intro") {
                musicWhereToDraw.left = musicXPos;
                musicWhereToDraw.top = musicYPos;
                musicWhereToDraw.right = musicXPos + musicWidth;
                musicWhereToDraw.bottom = musicYPos + musicHeight;
            }

    }


    public void draw(){

        if(ourHolder.getSurface().isValid()){
            canvas = this.ourHolder.lockCanvas();

            canvas.drawColor(Color.argb(255,0,0,0));

                if(animating){
                    animate();
                }
                //draw both bitmaps
                canvas.drawBitmap(image1, frameToDraw1, whereToDraw1, paint);
                canvas.drawBitmap(image2, frameToDraw2, whereToDraw2, paint);

            paint.setColor(Color.argb(255,255,255,255));

            //after the intro, tell users to touch the screen to continue, so they don't wait for a load
            if(imgId != "intro") {
                float txtSize = screenX/60;
                paint.setTextSize(txtSize);
                canvas.drawText("Touch to continue",(screenX-(screenX/3)),(screenY-(screenY/5)),paint);
            }

            //intro has 3 bitmaps to draw, with the 3rd being the sound button
            if(imgId == "intro"){
                if(playingMusic){
                    canvas.drawBitmap(musicOn,musicFrameToDraw,musicWhereToDraw,paint);
                }else{
                    canvas.drawBitmap(musicOff,musicFrameToDraw,musicWhereToDraw,paint);
                }
            }

            ourHolder.unlockCanvasAndPost(canvas);


        }



    }
    //World mandated methods
    public boolean isAlive(){
        return alive;
    }


    public boolean isNxtLevel(){

        return nextLevel;

    }
    public int getNumNades(){return numNades;}
    public int getNumGains(){return numGains;}
    public int getNumShields(){return numShields;}

    public int getPartOfEscalate(){return escalate;}

    public boolean isSecretLevel(){return secretLevel;}

    public void animate(){
        long time = System.currentTimeMillis();
        if (time > lastFrameChangeTime + frameLengthInMilliseconds) {
            lastFrameChangeTime = time;
            currentFrame++;
            if (currentFrame >= frameCount) {
                currentFrame = 0;
            }


        }
        frameToDraw2.left = currentFrame * frameWidth2;
        frameToDraw2.right = frameToDraw2.left + frameWidth2;

    }


    public boolean onTouchEvent(MotionEvent motionEvent){
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN:

                if(imgId == "intro") {
                    //if the ./start button is pressed, stop mediaPlayer, go to next level
                    if(motionEvent.getX() > whereToDraw2.left && motionEvent.getX() < whereToDraw2.right
                            && motionEvent.getY() > whereToDraw2.top && motionEvent.getY() < whereToDraw2.bottom){

                        mediaPlayer.setLooping(false);
                        mediaPlayer.stop();
                        mediaPlayer.release();

                        nextLevel = true;
                    }
                    //if sound on is toggled to sound off, puase or start music player, this will be passed on to the rest of the game
                    if(motionEvent.getX() > musicWhereToDraw.left && motionEvent.getX() < musicWhereToDraw.right
                            && motionEvent.getY() > musicWhereToDraw.top && motionEvent.getY() < musicWhereToDraw.bottom){
                        if(playingMusic){
                            mediaPlayer.pause();
                            playingMusic = false;
                        }else{
                            mediaPlayer.start();
                            playingMusic = true;
                        }
                    }


                }else{
                  //if normal level go to next level
                  //the isReleased is a way to fix a bug I noticed if the user presses a system UI button and the game screen
                  //at the same time, the app would crash
                    if(!isReleased) {
                        mediaPlayer.setLooping(false);
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        isReleased = true;

                        nextLevel = true;
                    }
                }



                break;

            case MotionEvent.ACTION_UP:

                break;
        }

        return true;
    }
    //World mandated methods
    public boolean isPlayingMusic(){return playingMusic;}
    //Bug fix for issue described above 
    public void pauseMusic(){
            if(!isReleased) {
                mediaPlayer.pause();
            }
    }



}
