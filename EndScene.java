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
import android.view.MotionEvent;
import android.view.SurfaceHolder;

/**
 * Created by gatovato on 1/13/2018.
 */

//2 possible Scenes depending on whether the game is completed as normal, or if rm -rf / is ran in the Terminal
//after running the pwndwn4wut script in the uploadfile.sh after getting 3 priveledge escalated icons
//most variables are the same as other World classes

public class EndScene implements World {

    private Context context;
    //used to draw to screen
    private SurfaceHolder ourHolder;
    //used to draw to screen, what is actually being drawn too
    private Canvas canvas;

    private Paint paint;

    private float screenX;
    private float screenY;

    private float tileWidth;
    private float tileHeight;

    private boolean nextLevel;

    private int numEnding;

    private RectF background;

    private Bitmap[][] map;
    private TileMap tileMap;

    private Rect mapFrameToDraw;

    private Bitmap homie;
    private Rect homieFrameToDraw;
    private RectF homieWhereToDraw;
    private int homieWidth;
    private int homieHeight;

    private Bitmap zardo;
    private Rect zardoFrameToDraw;
    private RectF zardoWhereToDraw;
    private int zardoWidth;
    private int zardoHeight;
    private float zXpos;
    private float zYpos;
    private boolean zMoving;
    private boolean zVisible;

    private Bitmap bus;
    private Rect busFrameToDraw;
    private RectF busWhereToDraw;
    private int busWidth;
    private int busHeight;

    private Bitmap magicBus;
    private Rect magicBusFrameToDraw;
    private RectF magicBusWhereToDraw;
    private int magicBusWidth;
    private int magicBusHeight;

    private int magicBusFrameCount = 8;
    private int currentFrame;
    private long lastFrameChangeTime = 0;
    private int frameLengthInMilliseconds = 1;

    private long time;
    private int timeSwitch;

    private Bitmap end1dialouge1;

    private int e1Width;
    private int e1Height;
    private Rect e1FrameToDraw;
    private RectF e1WhereToDraw;

    private Bitmap end1dialouge2;

    private int e2Width;
    private int e2Height;
    private Rect e2FrameToDraw;
    private RectF e2WhereToDraw;
    private Bitmap end2dialouge1;
    private Bitmap end2dialouge2;
    private Bitmap end2dialouge3;
    private Rect e2Wall;
    private Rect e2Ground;

    //the dialougePath is used to determine which text is shown, when the player touches the screen this dialougePath
    //will change and tell the draw() method which text to draw / animate Zardo (in the case of ending 1)
    private String dialougePath = "frame1";

    boolean alive = true;

    private boolean playingMusic; private MusicLibrary song;
    private MediaPlayer mediaPlayer;

    private int numWorld;


    public EndScene (Context context, SurfaceHolder surfaceHolder, float screenX, float screenY, int numEnding,
                     float tileWidth, float tileHeight,int numWorld,boolean playingMusic){

        this.context = context;

        ourHolder = surfaceHolder;

        this.screenX = screenX;

        this.screenY = screenY;

        this.numEnding = numEnding;

        this.tileWidth = tileWidth;

        this.tileHeight = tileHeight;

        this.numWorld = numWorld;

        this.playingMusic = playingMusic;

        paint = new Paint();

        background = new RectF(0,0,screenX,screenY);

        //depending on the ending, 1 is normal, 2 is the secret ending, load the image resources for that level
        switch (numEnding){

            case 1:
                zardoWidth = Math.round(tileWidth);
                zardoHeight = Math.round(tileHeight);
                zardo = BitmapFactory.decodeResource(context.getResources(), R.drawable.zardo);
                zardo = Bitmap.createScaledBitmap(zardo,zardoWidth,zardoHeight,false);
                zardoFrameToDraw = new Rect(0,0,zardoWidth,zardoHeight);
                zXpos = (tileWidth * 6);
                zYpos = (tileHeight);
                zardoWhereToDraw = new RectF(zXpos,zYpos,zXpos + zardoWidth,zYpos + zardoHeight);
                zMoving = false;
                zVisible = true;

                homieWidth = Math.round(tileWidth);
                homieHeight = Math.round(tileHeight);
                homie = BitmapFactory.decodeResource(context.getResources(), R.drawable.homes);
                homie = Bitmap.createScaledBitmap(homie,homieWidth,homieHeight,false);
                homieFrameToDraw = new Rect(0,0,homieWidth,homieHeight);
                homieWhereToDraw = new RectF(tileWidth*4,tileHeight,(tileWidth*4) + homieWidth,(tileHeight) + homieHeight);

                busWidth = Math.round(tileWidth*3);
                busHeight = Math.round(tileHeight);
                bus = BitmapFactory.decodeResource(context.getResources(), R.drawable.bus);
                bus = Bitmap.createScaledBitmap(bus,busWidth,busHeight,false);
                busFrameToDraw = new Rect(0,0,busWidth,busHeight);
                busWhereToDraw = new RectF((tileWidth*5),0,(tileWidth*5) + busWidth, busHeight);

                magicBusWidth = Math.round(tileWidth*3);
                magicBusHeight = Math.round(tileHeight);
                magicBus = BitmapFactory.decodeResource(context.getResources(), R.drawable.magicbus);
                magicBus = Bitmap.createScaledBitmap(magicBus,magicBusWidth * magicBusFrameCount,magicBusHeight,false);
                magicBusFrameToDraw = new Rect(0,0,magicBusWidth,magicBusHeight);
                magicBusWhereToDraw = new RectF(tileWidth*5,0,(tileWidth*5) + busWidth, busHeight);

                e1Width = Math.round(tileWidth*4);
                e1Height = Math.round(tileHeight*2);

                end1dialouge1 = BitmapFactory.decodeResource(context.getResources(),R.drawable.end1dialouge1);
                end1dialouge1 = Bitmap.createScaledBitmap(end1dialouge1,e1Width,e1Height,false);

                e1FrameToDraw = new Rect(0,0,e1Width,e1Height);
                e1WhereToDraw = new RectF((tileWidth*4),(tileHeight*2),(tileWidth*4)+e1Width,(tileHeight*2)+e1Height);

                end1dialouge2 = BitmapFactory.decodeResource(context.getResources(),R.drawable.end1dialouge2);
                end1dialouge2 = Bitmap.createScaledBitmap(end1dialouge2,e1Width,e1Height,false);

                tileMap = new TileMap(10,context,tileWidth,tileHeight);
                map = tileMap.getTileMap();
                mapFrameToDraw = tileMap.getFrameToDraw();
                tileMap = null;

                break;
            case 2:
                e2Width = Math.round(tileWidth *3);
                e2Height = Math.round(tileHeight*2);
                e2FrameToDraw = new Rect(0,0,e2Width,e2Height);
                e2WhereToDraw = new RectF(0,tileHeight,e2Width,(tileHeight)+e2Height);

                end2dialouge1 = BitmapFactory.decodeResource(context.getResources(),R.drawable.end2dialouge1);
                end2dialouge1 = Bitmap.createScaledBitmap(end2dialouge1,e2Width,e2Height,false);

                end2dialouge2 = BitmapFactory.decodeResource(context.getResources(),R.drawable.end2dialouge2);
                end2dialouge2 = Bitmap.createScaledBitmap(end2dialouge2,e2Width,e2Height,false);

                end2dialouge3 = BitmapFactory.decodeResource(context.getResources(),R.drawable.end2dialouge3);
                end2dialouge3 = Bitmap.createScaledBitmap(end2dialouge3,e2Width,e2Height,false);

                e2Wall = new Rect(0,0,Math.round(screenX),Math.round((tileHeight*3)-(tileHeight/10)));
                e2Ground = new Rect(0,Math.round((tileHeight*3)-(tileHeight/10)),Math.round(screenX),Math.round(screenY));

                tileMap = new TileMap(11,context,tileWidth,tileHeight);
                map = tileMap.getTileMap();
                mapFrameToDraw = tileMap.getFrameToDraw();
                //memory saving effort ;)
                tileMap = null;

                break;
        }

        //load music library, this will give us the song
        song = new MusicLibrary(context,numWorld);
        int rId = song.getSong();
        //create it, this will set it too an initialized state
        mediaPlayer = MediaPlayer.create(context,rId);
        //make it loop
        mediaPlayer.setLooping(true);

        //playingMusic is what is set by the intro scene by turning sound on or off. It can also be turned on or off
        //within the terminal
        if(playingMusic) {
            mediaPlayer.start();
        }
        //used for determining which text/animation (in the case of ending 1) to display
        timeSwitch = 0;

    }

    public void update(){

        switch (numEnding){
            case 1:

                updateHomes();
                //after the last dialouge zardo will travel up to the bus, once his Y is greater than the buses
                //it will initate the bus launch animation
                if(zYpos <= busWhereToDraw.top){
                    zMoving = false;
                    zVisible = false;
                    dialougePath = "takeoff";
                }
                if(zVisible){
                    updateZardo();
                }
                if(dialougePath == "takeoff") {
                    updateMagicBus();
                }else{
                    updateBus();
                }
                if(dialougePath == "frame1" || dialougePath =="frame2"){
                    updateE1Dialouge();
                }


                break;

            case 2:
                //not much here, just need to update the dialouge RectF so it can be drawn
                updateE2Dialouge();


                break;
        }

    }

    public void draw() {
        if (ourHolder.getSurface().isValid()) {
            canvas = this.ourHolder.lockCanvas();

            switch (numEnding) {
                case 1:

                    paint.setColor(Color.argb(255,0, 78, 152));
                    canvas.drawRect(background, paint);
                    //load tileMap of ending 1
                    for (int x = 0; x < map.length; x++) {
                        for (int y = 0; y < map[x].length; y++) {

                            float xPos = y * tileWidth;
                            float yPos = x * tileHeight;

                            RectF whereToDraw = new RectF(xPos, yPos, xPos + tileWidth, yPos + tileHeight);


                            if (map[x][y] != null) {
                                canvas.drawBitmap(map[x][y], mapFrameToDraw, whereToDraw, paint);
                            }

                        }

                    }

                    canvas.drawBitmap(homie,homieFrameToDraw,homieWhereToDraw,paint);
                    if(zVisible){
                        canvas.drawBitmap(zardo,zardoFrameToDraw,zardoWhereToDraw,paint);
                    }
                    //once zardo flys up and collides with the bus, it will animate and teleport away

                    if(dialougePath == "takeoff"){
                        animate();
                        canvas.drawBitmap(magicBus,magicBusFrameToDraw,magicBusWhereToDraw,paint);
                    }else{
                        canvas.drawBitmap(bus,busFrameToDraw,busWhereToDraw,paint);
                    }

                    if(dialougePath == "frame1"){
                        canvas.drawBitmap(end1dialouge1,e1FrameToDraw,e1WhereToDraw,paint);
                    }
                    if(dialougePath == "frame2"){
                        canvas.drawBitmap(end1dialouge2,e1FrameToDraw,e1WhereToDraw,paint);
                    }
                    paint.setColor(Color.argb(255,0,0,0));
                    paint.setTextSize(tileWidth/4);
                    canvas.drawText("Touch Screen To Continue...",tileWidth/4,(tileHeight/3),paint);

                    break;

                case 2:

                    paint.setColor(Color.argb(255,158,189,158));
                    canvas.drawRect(e2Wall, paint);

                    paint.setColor(Color.argb(255,116,81,81));
                    canvas.drawRect(e2Ground, paint);

                    for (int x = 0; x < map.length; x++) {
                        for (int y = 0; y < map[x].length; y++) {

                            float xPos = y * tileWidth;
                            float yPos = x * tileHeight;

                            RectF whereToDraw = new RectF(xPos, yPos, xPos + tileWidth, yPos + tileHeight);


                            if (map[x][y] != null) {
                                canvas.drawBitmap(map[x][y], mapFrameToDraw, whereToDraw, paint);
                            }

                        }

                    }
                    //draw different dialouge Bitmaps depending on which frame, based on input found in MotionEvent
                    switch (dialougePath){
                        case "frame1":
                            canvas.drawBitmap(end2dialouge1,e2FrameToDraw,e2WhereToDraw,paint);
                            break;
                        case "frame2":
                            canvas.drawBitmap(end2dialouge2,e2FrameToDraw,e2WhereToDraw,paint);
                            break;
                        case "frame3":
                            canvas.drawBitmap(end2dialouge3,e2FrameToDraw,e2WhereToDraw,paint);
                            break;
                    }

                    paint.setColor(Color.argb(255,0,0,0));
                    paint.setTextSize(tileWidth/4);
                    //friendly reminder
                    canvas.drawText("Touch Screen To Continue...",tileWidth*7,(tileHeight*4),paint);

                    break;
            }

            ourHolder.unlockCanvasAndPost(canvas);
        }

    }

    //not used in this World, but required for World interface
    public boolean isAlive(){
        return alive;
    }

    //used to restart to the intro Scene
    public boolean isNxtLevel(){

        return nextLevel;

    }

    //also required methods of the World interface
    public int getNumNades(){return 0;}
    public int getNumGains(){return 0;}
    public int getNumShields(){return 0;}

    public int getPartOfEscalate(){return 0;}

    public boolean isSecretLevel(){return false;}

    //every time the player touches the screen increment the dialougePath and change the dialouge box / trigger Zardo (ending 1)
    public boolean onTouchEvent(MotionEvent motionEvent){
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN:

                switch (numEnding){
                    case 1:
                        switch (dialougePath){
                            case "frame1":
                                dialougePath = "frame2";
                                break;
                            case "frame2":
                                zMoving = true;
                                break;
                        }
                        break;
                        //if you compare the ending of ending 1 with 2, you'll notice that the media player isn't killed
                        //this is because it's not nextLevel after frame2, but rather after Zardo bus disappears, see below
                    case 2:
                        switch (dialougePath){
                            case "frame1":
                                dialougePath = "frame2";
                                break;
                            case "frame2":
                                dialougePath = "frame3";
                                break;
                            case "frame3":
                            //as mentioned above, kill media player, to free up memory as well as make it so the next song
                            //can play
                                mediaPlayer.setLooping(false);
                                mediaPlayer.stop();
                                mediaPlayer.release();
                                nextLevel = true;
                                break;
                        }
                        break;
                }




                break;

            case MotionEvent.ACTION_UP:

                break;
        }

        return true;
    }

    //this is where the bus will be animated in ending 1. Once the bus disappears, kill media player, to free up memory
    //as well as allow the next song to play.
    public void animate(){
        long time = System.currentTimeMillis();
        if (time > lastFrameChangeTime + frameLengthInMilliseconds) {
            lastFrameChangeTime = time;
            currentFrame++;
            if (currentFrame >= magicBusFrameCount) {
                mediaPlayer.setLooping(false);
                mediaPlayer.stop();
                mediaPlayer.release();
                nextLevel = true;
            }


        }
        magicBusFrameToDraw.left = currentFrame * magicBusWidth;
        magicBusFrameToDraw.right = magicBusFrameToDraw.left + magicBusWidth;

    }

    //this will update Zardo's position and after frame2 is displayed move Zardo up to the bus
    public void updateZardo(){
        if(!zMoving) {
            zardoWhereToDraw.left = zXpos;
            zardoWhereToDraw.top = zYpos;
            zardoWhereToDraw.right = zXpos + zardoWidth;
            zardoWhereToDraw.bottom = zYpos + zardoHeight;
        }else{
            zYpos = zYpos - (tileHeight/15);
            zardoWhereToDraw.left = zXpos;
            zardoWhereToDraw.top = zYpos;
            zardoWhereToDraw.right = zXpos + zardoWidth;
            zardoWhereToDraw.bottom = zYpos + zardoHeight;
        }
    }

    //keep him stationary
    public void updateHomes(){
        homieWhereToDraw.left = (tileWidth*4);
        homieWhereToDraw.top = (tileHeight);
        homieWhereToDraw.right = (tileWidth*4) + homieWidth;
        homieWhereToDraw.bottom = (tileHeight) + homieHeight;
    }

    //keep the bus stationary, it actually doesn't move (just in animation)
    public void updateBus(){
        busWhereToDraw.left = (tileWidth*5);
        busWhereToDraw.top = 0;
        busWhereToDraw.right = (tileWidth*5) + busWidth;
        busWhereToDraw.bottom =  busHeight;
    }

    //update for bus animation
    public void updateMagicBus(){
        magicBusWhereToDraw.left = (tileWidth*5);
        magicBusWhereToDraw.top = 0;
        magicBusWhereToDraw.right = (tileWidth*5) + busWidth;
        magicBusWhereToDraw.bottom = busHeight;
    }

    //update all the dialouges so they can be drawn
    public void updateE1Dialouge(){
        e1WhereToDraw.left = tileWidth*4;
        e1WhereToDraw.top = tileHeight*2;
        e1WhereToDraw.right = (tileWidth*4)+e1Width;
        e1WhereToDraw.bottom =  (tileHeight*2)+e1Height;
    }

    public void updateE2Dialouge(){
        e2WhereToDraw.left = 0;
        e2WhereToDraw.top = tileHeight;
        e2WhereToDraw.right = e2Width;
        e2WhereToDraw.bottom = (tileHeight)+e2Height;

    }

    //used for pause() methods in GameLoop
    public boolean isPlayingMusic(){return playingMusic;}
    public void pauseMusic(){mediaPlayer.pause();}

}
