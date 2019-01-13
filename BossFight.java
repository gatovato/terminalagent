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

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by gatovato on 1/3/2018.
 */
//Essentially the same class as Level. Some important things to note:
//1. No view port is needed as this World doesn't scroll
//2. No blocks
//3. tileMap has less tiles as the size of the world is only the screen width by screen height
public class BossFight implements World {

    private Context context;

    //used to draw to screen
    private SurfaceHolder ourHolder;

    //what is actually being drawn to
    private Canvas canvas;

    private Paint paint;

    private float screenX;
    private float screenY;

    private float tileWidth;
    private float tileHeight;

    private boolean alive = true;
    private boolean nextLevel;

    //need ArrayLists to store the objects in
    private ArrayList<Enemy> enemies;

    private ArrayList<String> encryptList;

    private ArrayList<Bullet> bullets;

    private ArrayList<Grenade> nadeList;

    private ArrayList<Grenade> explodeList;

    private ArrayList<Items> items;

    //some booleans to keep track of logic
    private boolean isFiring;
    private boolean isJumping;
    private boolean isItemMenu;

    private Player player;

    private TileMap tileMap;

    private int numLevel;

    //define number of objects
    private int maxEnemies;

    private int numHealth;

    private int numNades;
    private int numShields;
    private int numGains;

    //UI
    private RectF leftButton;
    private RectF rightButton;
    private RectF jetButton;
    private RectF fireButton;
    private RectF itemButton;

    private Rect navButton;
    private Rect jumpButton;
    private Rect shootButton;
    private Rect itemRect;
    private Rect menuButton;

    private Bitmap leftArrow;
    private Bitmap fireButtonOff;
    private Bitmap fireButtonOn;
    private Bitmap rightArrow;
    private Bitmap jetPackOff;
    private Bitmap jetPackOn;
    private Bitmap itemsMenuButton;


    //UI cont..
    private RectF itemBox;
    private RectF nadeButton;
    private RectF shieldButton;
    private RectF gainButton;

    //used in itemBox
    private Bitmap shield;
    private Bitmap lifeGain;
    private Bitmap grenade;


    //used to draw from top of tileMap to as far as user can see when player is jumping and riding top of world
    private RectF background;

    //used to draw from bottom of tileMap to end of screen
    private RectF ground;


    private float groundLevel;

    private Bitmap[][] map;

    //for world mandated variables
    private int partOfEscalate;

    private boolean secretLevel = false;

    //used to generate life
    String[] encryptString = {"a","%","b","$","c","^","7","8","9","*","1","3","f","e"};

    //rect to draw map on
    private Rect mapFrameToDraw;

    //you know what it is :)
    private Zardo zardo;

    //create object when using items
    private Items usingShield;
    private Items usingGain;

    //how many times it takes to get him
    private int zardoHP;

    //how much ammo does player have
    private int numAmmo;

    //time to teleport Zardo
    private long zardoTime;

    //trigger to teleport
    private long maxZardoTime;

    //used to reset teleportation timer
    private int zTimerSwitch;

    //when Zardo is hit by a bullet items will drop
    private boolean spwnItem;

    //if out of life display Game Over banner
    private boolean displayGameOver;

    //what's being displayed
    private Bitmap gameOver;
    private int gameOverWidth;
    private int gameOverHeight;
    public Rect gameOverFrameToDraw;
    public RectF gameOverWhereToDraw;
    //used to animate game over banner
    public int gameOverFrameCount = 3;
    private int gameOverCurrentFrame;
    private long gameOverLastFrameChangeTime = 0;
    private int gameOverFrameLengthInMilliseconds = 1000;
    private int gameOverSwitch = 0;
    //determine which image should be displayed depending on which way Zardo is facing
    private String zFacing;

    //amount of the screen player will move depending ono whichever action
    private float playerSpeed;
    private float jumpSpeed;
    private float fallSpeed;
    private float archSpeed;

    //if playing music, determined by sound on/off in intro scene or within terminal
    private boolean playingMusic;

    //get resource id of song to play with media player
    private MusicLibrary song;

    //what does the playing of the music
    private MediaPlayer mediaPlayer;

    //used for grabbing tileMap, as well as song
    private int numWorld;

    public BossFight(Context context, SurfaceHolder surfaceHolder, float screenX, float screenY,int numLevel,String enemyList[],
                   int numHealth, int numNades, int numShields, int numGains, float tileWidth, float tileHeight,int partOfEscalate,int zardoLife,int maxEnemies, int numAmmo,
                     int numWorld,boolean playingMusic){

        this.context = context;

        ourHolder = surfaceHolder;

        this.screenX = screenX;

        this.screenY = screenY;

        paint = new Paint();

        this.numLevel = numLevel;


        String makeEnemy[] = enemyList;

        this.numNades = numNades;

        this.numShields = numShields;

        this.numGains = numGains;

        this.tileWidth = tileWidth;

        this.tileHeight = tileHeight;

        this.partOfEscalate = partOfEscalate;

        this.numHealth = numHealth;

        groundLevel = (tileHeight*3)+(tileHeight/10);

        this.numWorld = numWorld;

        this.playingMusic = playingMusic;

        //start Zardo in a random part of the screen
        int zStartX = makeRandInt(Math.round(tileWidth*3),Math.round(screenX));
        int zStartY = makeRandInt(0,Math.round(groundLevel - tileHeight));
        //if his x position is on the left of the screen, have him face right
        if(zStartX < screenX/2){
            zFacing = "right";
        }
        //if his x position is on the right side of the screen, have him face left
        if(zStartX >= screenX/2){
            zFacing ="left";
        }
        playerSpeed = (tileWidth/3);
        jumpSpeed = (tileWidth/2) * (1 + (1/2));
        fallSpeed = tileHeight/8;
        archSpeed = playerSpeed/8;

        zardo = new Zardo(zStartX,zStartY,context,tileWidth+(tileWidth/3),tileHeight+(tileHeight/4),screenX,(groundLevel - tileHeight),zFacing);
        player = new Player(tileWidth/2,(groundLevel - tileHeight),context,tileWidth + (tileWidth/2), tileHeight,6,"right",playerSpeed,fallSpeed,archSpeed,jumpSpeed);

        //used for generating a random amount of enemies
        int randEnemies = makeRandInt(2,maxEnemies);
        enemies = new ArrayList<>(randEnemies);
        nadeList = new ArrayList<>();
        explodeList = new ArrayList<>();
        items = new ArrayList<>();

        //get random amount of enemies
        for(int i = 0;i < randEnemies;i++){
            //get random x y's for enemy within world
            float randX = makeRandInt(Math.round(tileWidth*3),Math.round(screenX));
            float randY = makeRandInt(0,Math.round(screenY-(screenY/3)));

            //get random array position from enemyList
            Random enemyType = new Random();
            int randomEnemy = enemyType.nextInt(makeEnemy.length);

            //based on the random object in enemyList, initialize the appropriate enemy
            switch(makeEnemy[randomEnemy]){
                case "pwnwlkr":
                    Enemy pwnwlker = new Enemy(context,"pwnwlkr","pwnwlkrl",randX,randY,tileWidth,tileHeight,tileWidth/40,4,true,false,false);
                    enemies.add(pwnwlker);
                    break;

                case "flyguy":
                    Enemy flyGuy = new Enemy(context,"flyguys","flyguys", randX, randY,tileWidth - (tileWidth/3),tileHeight/2,tileWidth/20,2,true,true,false);
                    enemies.add(flyGuy);
                    break;

                case "trojan":
                    Enemy trojan = new Enemy(context,"trojanlft","trojanrght",randX, randY, tileWidth*2,tileHeight+(tileHeight/3),tileWidth/30,3,true,false,false);
                    enemies.add(trojan);
                    break;

                case "worm":
                    Enemy worm = new Enemy(context,"wurmleft","wurmright",randX, randY, tileWidth*2,tileHeight*1,tileWidth/20,6,true,true,true);
                    enemies.add(worm);
                    break;
            }

        }

        //get random values from encryptString and add to encryptList (life)
        encryptList = new ArrayList<>(numHealth);
        for(int i = 0; i < numHealth;i++){
            int randString = makeRandInt(0,(encryptString.length-1));
            String letter = encryptString[randString];
            encryptList.add(letter);

        }

        tileMap = new TileMap(numLevel,context,tileWidth,tileHeight);

        //UI dimensions
        int navWidth = Math.round((tileWidth/2) + (tileWidth/8));
        int navHeight = Math.round(tileHeight - (tileHeight/2));

        int jumpWidth = Math.round(tileWidth/2);
        int jumpHeight = Math.round(tileHeight - (tileHeight/4));

        int shootWidth = Math.round((tileWidth) - (tileWidth/6));
        int shootHeight = Math.round(tileWidth/2 + (tileWidth/5));

        int itemWidth = Math.round(tileWidth);
        int itemHeight = Math.round(tileHeight/3);

        navButton = new Rect(0,0,navWidth,navHeight);
        jumpButton = new Rect(0,0,jumpWidth,jumpHeight);
        shootButton = new Rect (0,0,shootWidth,shootHeight);
        itemRect = new Rect(0,0,itemWidth,itemHeight);

        float worldHeight = groundLevel;

        //UI RectFs
        leftButton = new RectF(tileWidth/2,worldHeight,
                (tileWidth/2)+ navWidth,worldHeight + navHeight);

        rightButton = new RectF(leftButton.right + (tileWidth + (tileWidth/8)),worldHeight,
                (leftButton.right + (tileWidth + (tileWidth/8)))+ navWidth, worldHeight + navHeight);

        jetButton = new RectF((screenX/2) + ((tileWidth*2) + (tileWidth/2) + (tileWidth/6)),worldHeight,
                (screenX/2) + ((tileWidth*2) + (tileWidth/2) + (tileWidth/8))+ jumpWidth, worldHeight + jumpHeight);

        fireButton = new RectF(screenX - (tileWidth + (tileWidth/2)),worldHeight + (tileWidth/6),
                (screenX - (tileWidth + (tileWidth/2))) + shootWidth, (worldHeight + (tileWidth/6))+ shootHeight);

        itemButton = new RectF((screenX/2) - ((tileWidth) - (tileWidth/4)), worldHeight ,
                (screenX/2) - ((tileWidth) - (tileWidth/4)) + itemWidth, worldHeight  + itemHeight);

        itemBox = new RectF (rightButton.right + (tileWidth/3), worldHeight,
                jetButton.left - (tileWidth/3),worldHeight + (navHeight + (tileHeight/10)));

        int menuItemWidth = Math.round((itemBox.right - itemBox.left)/5);
        int menuItemHeight = Math.round(tileHeight/4);
        menuButton = new Rect(0,0,menuItemWidth,menuItemHeight);

        nadeButton = new RectF(itemBox.left + (menuItemWidth/3),itemBox.top + (tileHeight/25),
                (itemBox.left + (menuItemWidth/3)) + menuItemWidth,itemBox.bottom - (tileHeight/25));

        gainButton = new RectF(nadeButton.right + (menuItemWidth/2),itemBox.top + (tileHeight/25),
                (nadeButton.right + (menuItemWidth/2)) + menuItemWidth,itemBox.bottom - (tileHeight/25));

        shieldButton = new RectF(gainButton.right + (menuItemWidth/2), itemBox.top + (tileHeight/25),
                (gainButton.right + (menuItemWidth/2)) + menuItemWidth, itemBox.bottom - (tileHeight/25));

        //UI images
        leftArrow = BitmapFactory.decodeResource(context.getResources(), R.drawable.leftarrow);
        leftArrow = Bitmap.createScaledBitmap(leftArrow,navWidth,navHeight,false);

        rightArrow = BitmapFactory.decodeResource(context.getResources(),R.drawable.rightarrow);
        rightArrow = Bitmap.createScaledBitmap(rightArrow,navWidth,navHeight,false);

        jetPackOff = BitmapFactory.decodeResource(context.getResources(),R.drawable.jetpack0);
        jetPackOff = Bitmap.createScaledBitmap(jetPackOff, jumpWidth, jumpHeight,false);

        jetPackOn = BitmapFactory.decodeResource(context.getResources(),R.drawable.jetpack1);
        jetPackOn = Bitmap.createScaledBitmap(jetPackOn, jumpWidth, jumpHeight,false);

        fireButtonOff = BitmapFactory.decodeResource(context.getResources(), R.drawable.fireoff);
        fireButtonOff = Bitmap.createScaledBitmap(fireButtonOff,shootWidth,shootHeight,false);

        fireButtonOn = BitmapFactory.decodeResource(context.getResources(), R.drawable.fireon);
        fireButtonOn = Bitmap.createScaledBitmap(fireButtonOn,shootWidth, shootHeight, false);

        itemsMenuButton = BitmapFactory.decodeResource(context.getResources(),R.drawable.items);
        itemsMenuButton = Bitmap.createScaledBitmap(itemsMenuButton,itemWidth,itemHeight,false);

        grenade = BitmapFactory.decodeResource(context.getResources(),R.drawable.grenade);
        grenade = Bitmap.createScaledBitmap(grenade, menuItemWidth, menuItemHeight,false);

        lifeGain = BitmapFactory.decodeResource(context.getResources(),R.drawable.lifegain);
        lifeGain = Bitmap.createScaledBitmap(lifeGain,menuItemWidth,menuItemHeight,false);

        shield = BitmapFactory.decodeResource(context.getResources(),R.drawable.shield);
        shield = Bitmap.createScaledBitmap(shield, menuItemWidth, menuItemHeight,false);


        //using item objects
        usingShield = new Items(context,"activeshield",player.getWhereToDraw().right,player.getWhereToDraw().top,tileWidth,tileHeight,5,false);
        usingGain = new Items(context,"animelife",player.getWhereToDraw().right,player.getWhereToDraw().top + (tileWidth/3),tileWidth/3,tileHeight/2,3,false);

        //part above the tile map player can see when maxing out jump
        background = new RectF(0,0,screenX,tileHeight * 3);

        //part from the bottom of the tileMap to the bottom of the screen
        ground = new RectF(0,tileHeight * 3,screenX,screenY);



        map = tileMap.getTileMap();
        mapFrameToDraw = tileMap.getFrameToDraw();
        tileMap = null;

        //store / keep track of bullets once fired
        bullets = new ArrayList<>();

        zardoHP = zardoLife;

        this.numAmmo = numAmmo;
        zTimerSwitch = 0;

        spwnItem = false;

        gameOverWidth = Math.round(tileWidth * 4);
        gameOverHeight = Math.round(tileHeight *2);
        gameOver = BitmapFactory.decodeResource(context.getResources(), R.drawable.disconnected);
        gameOver = Bitmap.createScaledBitmap(gameOver, gameOverWidth * gameOverFrameCount,gameOverHeight,false);
        gameOverFrameToDraw = new Rect(0,0,gameOverWidth,gameOverHeight);
        gameOverWhereToDraw = new RectF(0,0,0,0);
        displayGameOver = false;

        song = new MusicLibrary(context,numWorld);
        int rId = song.getSong();
        mediaPlayer = MediaPlayer.create(context,rId);
        mediaPlayer.setLooping(true);
        //initialize the player, but only start it if the game is playing music
        if(playingMusic) {
            mediaPlayer.start();
        }
    }

    //mandated World method. This is where all object positions / timers are kept track of
    public void update(){

        //if bullets have been shot, keep track of their position
        bulletLoop:
        if(!bullets.isEmpty()){
            for(int i = 0; i < bullets.size();i++) {
                bullets.get(i).update();

                //check to see if a bullet, collides with an enemy
                //if so break out of the main loop (otherwise an error will occur because of subsequent bullet references to i)
                for (int e = 0; e < enemies.size(); e++) {
                    if (RectF.intersects(bullets.get(i).getBullet(), enemies.get(e).getWhereToDraw())) {
                        if(enemies.get(e).getVisible()) {
                            enemies.get(e).setVisisble(false);
                            enemies.get(e).setTime(System.currentTimeMillis());
                            bullets.remove(i);
                            break bulletLoop;
                        }

                    }
                }

                //check to see if the bullet hit the player, if so (hard to do, but still) remove a life
                //from the player
                if(RectF.intersects(bullets.get(i).getBullet(), player.getWhereToDraw())){
                    if(!encryptList.isEmpty()) {

                        encryptList.remove(0);
                        bullets.remove(i);
                        break bulletLoop;
                    }
                }

                //check to see if the bullet hits Zardo
                //if so take a life off of him, drop an item
                if(RectF.intersects(bullets.get(i).getBullet(), zardo.getWhereToDraw())){

                    zardoHP--;
                    bullets.remove(i);
                    spwnItem = true;
                    break bulletLoop;
                }

                //if the bullet hits the left edge of the screen, remove it
                if (bullets.get(i).getBullet().left < 0) {

                    bullets.remove(i);
                    break bulletLoop;
                }

                //if the bullet hits the right edge of the screen, remove it
                if (bullets.get(i).getBullet().right > screenX) {

                    bullets.remove(i);
                    break bulletLoop;
                }
            }
        }

        //if bullet hits Zardo, drop a random object
        if(spwnItem){

            //depends on which way he's facing to position the item
            switch (zardo.getFacing()){

                case "left":
                    float x = zardo.getXpos() - (tileWidth/4);
                    float y = zardo.getYpos();
                    int itemGen = makeRandInt(0,2);
                    if(itemGen == 0){
                        Items lifegain = new Items(context,"lifegain",x,y,tileWidth/2,tileHeight/2,0,true);
                        items.add(lifegain);
                    }
                    if(itemGen == 1){
                        Items shield = new Items(context,"shield",x, y, tileWidth/2,tileHeight/2,0,true);
                        items.add(shield);
                    }
                    if(itemGen == 2){
                        Items grenade = new Items(context,"grenade",x,y,tileWidth/4,tileHeight/4,0,true);
                        items.add(grenade);
                    }
                    break;
                case "right":
                    float eX = zardo.getWhereToDraw().right + (tileWidth/4);
                    float wY = zardo.getYpos();
                    int itemGen2 = makeRandInt(0,2);
                    if(itemGen2 == 0){
                        Items lifegain = new Items(context,"lifegain",eX,wY,tileWidth/2,tileHeight/2,0,true);
                        items.add(lifegain);
                    }
                    if(itemGen2 == 1){
                        Items shield = new Items(context,"shield",eX, wY, tileWidth/2,tileHeight/2,0,true);
                        items.add(shield);
                    }
                    if(itemGen2 == 2){
                        Items grenade = new Items(context,"grenade",eX,wY,tileWidth/4,tileHeight/4,0,true);
                        items.add(grenade);
                    }
                    break;
            }

            spwnItem = false;
        }

        //items apply to gravity, once fallen from Zardo, fall down until ground is reached
        //update items position
        itemLoop:
        for(int i = 0; i < items.size(); i++){

            if(items.get(i).getYpos() < (groundLevel - items.get(i).getFrameHeight())){
                items.get(i).setYPos(items.get(i).getYpos() + (tileHeight / 8));
            }else{
                items.get(i).setYPos(groundLevel - items.get(i).getFrameHeight());
            }

            items.get(i).update();


            //if player runs into item, add to inventory
            if(RectF.intersects(player.getWhereToDraw(),items.get(i).getWhereToDraw())){
                if (items.get(i).getImgId() == "lifegain") {
                    numGains++;
                }
                if (items.get(i).getImgId() == "grenade") {
                    numNades++;
                }
                if (items.get(i).getImgId() == "shield") {
                    numShields++;
                }
                items.remove(i);
                break itemLoop;
            }
        }


        //keep track of enemies position
        for(int i = 0; i < enemies.size(); i++) {

            //if enemy reaches the left side of the screen, send enemy moving right
            if (enemies.get(i).getXpos() < 0) {

                enemies.get(i).setMovingLeft(false);
                enemies.get(i).setMovingRight(true);
            }
            //if enemy reaches the right side of the screen, reverse enemy and make move left
            if (enemies.get(i).getWhereToDraw().right > screenX) {

                enemies.get(i).setMovingRight(false);
                enemies.get(i).setMovingLeft(true);

            }

            //gravity applies to all non-flyer enemies
            if(!enemies.get(i).isFlying()) {
                if (enemies.get(i).getYpos() < (groundLevel - enemies.get(i).getFrameHeight())) {
                    enemies.get(i).setYPos(enemies.get(i).getYpos() + (tileHeight / 8));
                } else {
                    enemies.get(i).setYPos(groundLevel - enemies.get(i).getFrameHeight());
                }
            }

            //if they are visible (already spawned) enemies and player collide, remove life from player
            if (enemies.get(i).getVisible()) {
                if (RectF.intersects(enemies.get(i).getWhereToDraw(), player.getWhereToDraw())) {
                    if (!encryptList.isEmpty()) {
                        encryptList.remove(0);
                    }
                }

                enemies.get(i).update();
            }else{
                //if they are invisible this means they have been hit once and need to be respawned. This is based
                //on a timer
                enemies.get(i).setTimer(System.currentTimeMillis());
                if(enemies.get(i).getTimer() > enemies.get(i).getTime()+5000){
                    zardo.setSpwning(true);
                    //star animation
                    zardo.updateSpwnAnime();
                }
                if(enemies.get(i).getTimer() > enemies.get(i).getTime()+7000){
                    zardo.setSpwning(false);
                    enemies.get(i).setVisisble(true);
                }
            }
        }

        //when player is using shield, animate shield
        if(usingShield.isUsing()){
            switch (player.getFacing()){
                case "left":
                    usingShield.setXPos(player.getWhereToDraw().left-tileWidth);
                    break;
                case "right":
                    usingShield.setXPos(player.getWhereToDraw().right);
                    break;
            }
            usingShield.setYPos(player.getWhereToDraw().top);
            long time = System.currentTimeMillis();
            //shield is only active for so long
            if(time < usingShield.getTime()+5000) {
                usingShield.update();
                for(int i = 0; i < enemies.size();i++){
                    //if enemies collide with shield send them moving in the opposite direction
                    if(RectF.intersects(usingShield.getWhereToDraw(), enemies.get(i).getWhereToDraw())){
                        if(enemies.get(i).getMovingLeft()){
                            enemies.get(i).setMovingLeft(false);
                            enemies.get(i).setMovingRight(true);
                        }else{
                            enemies.get(i).setMovingRight(false);
                            enemies.get(i).setMovingLeft(true);
                        }
                    }
                }
            }else{
                usingShield.setUsing(false);
            }

        }

        //if using lifegain, animate is appropriate direction, and gain some life (encryption)
        if(usingGain.isUsing()){
            switch (player.getFacing()){
                case "left":
                    usingGain.setXPos(player.getWhereToDraw().left);
                    break;
                case "right":
                    usingGain.setXPos(player.getWhereToDraw().right);
                    break;
            }
            usingGain.setYPos(player.getWhereToDraw().top);

            long time = System.currentTimeMillis();
            if(time < usingGain.getTime()+500) {
                usingGain.update();
            }else{
                usingGain.setUsing(false);
                int rand = makeRandInt(1,numHealth/2);
                for(int i =0; i < rand; i++){
                    int randLetter = makeRandInt(0,(encryptString.length-1));
                    encryptList.add(encryptString[randLetter]);
                }
            }
        }

        //once grenades are thrown they enter the nadeList
        for(int i = 0; i < nadeList.size(); i++){
            long time = System.currentTimeMillis();
            boolean hitGround;
            //gravity applies to grenades
            if(nadeList.get(i).getNadeWhereToDraw().bottom < groundLevel - (tileHeight/5)){
                hitGround = false;
            }else{
                hitGround = true;
            }

            //grenades can bounce off the ends of the screen
            if(nadeList.get(i).getNadeWhereToDraw().left < 0){
                nadeList.get(i).setHeading("right");
            }
            if(nadeList.get(i).getNadeWhereToDraw().right > screenX){
                nadeList.get(i).setHeading("left");
            }

            //update grenades position for drawing
            nadeList.get(i).updateNade(hitGround);

            //grenades have timer
            //at the end of the timer, make new grenade which will animate the explosion, stored in explodeList
            if(time > nadeList.get(i).getTime() + 1500){
                nadeList.get(i).setVisible(false);
                switch (nadeList.get(i).getHeading()){
                    case "left":
                        Grenade explosion = new Grenade(context,"null",nadeList.get(i).getxPos() - (tileWidth*2),nadeList.get(i).getyPos() - (tileWidth*3),tileWidth*5,tileWidth*5,
                                System.currentTimeMillis(),true,tileWidth/2,tileWidth,tileHeight);
                        explodeList.add(explosion);
                        break;
                    case "right":
                        Grenade explosion1 = new Grenade(context,"null",nadeList.get(i).getxPos(),nadeList.get(i).getyPos() - (tileWidth*3),tileWidth*5,tileWidth*5,
                                System.currentTimeMillis(),true,tileWidth/2,tileWidth,tileHeight);
                        explodeList.add(explosion1);
                        break;

                }

            }

        }

        //once the grenades explode the physical grenade is set to invisible. Once they are set to invisible remove them
        for(int i = 0; i < nadeList.size();i++){
            if(!nadeList.get(i).isVisible()){
                nadeList.remove(i);
            }
        }

        //update the explosions
        for(int i = 0; i < explodeList.size(); i++){
            long time = System.currentTimeMillis();

            explodeList.get(i).updateExplosion();

            //if enemies are within the blast area, set them invisible
            for(int e = 0; e < enemies.size();e++){
                if(enemies.get(e).getWhereToDraw().left > explodeList.get(i).getExplodeWhereToDraw().left - (tileWidth) && enemies.get(e).getWhereToDraw().left <
                        explodeList.get(i).getExplodeWhereToDraw().right + (tileWidth) && enemies.get(e).getWhereToDraw().top > explodeList.get(i).getExplodeWhereToDraw().top - (tileWidth)&&
                        enemies.get(e).getWhereToDraw().top < explodeList.get(i).getExplodeWhereToDraw().bottom + (tileWidth)) {

                    enemies.get(e).setVisisble(false);
                    enemies.get(e).setTime(System.currentTimeMillis());

                }

            }
            //if Zardo is within the blast area, remove a life from him
            if(zardo.getWhereToDraw().left > explodeList.get(i).getExplodeWhereToDraw().left - (tileWidth) && zardo.getWhereToDraw().left <
                    explodeList.get(i).getExplodeWhereToDraw().right + (tileWidth) && zardo.getWhereToDraw().top > explodeList.get(i).getExplodeWhereToDraw().top - (tileWidth)&&
                    zardo.getWhereToDraw().top < explodeList.get(i).getExplodeWhereToDraw().bottom + (tileWidth)) {
                zardoHP--;

            }
            //if player is within the blast area, remove a life from him
            if(RectF.intersects(explodeList.get(i).getExplodeWhereToDraw(), player.getWhereToDraw())){
                if(!encryptList.isEmpty()) {
                    encryptList.remove(0);
                }
            }
            //once the explosion time is up, set it invisible
            if(time > explodeList.get(i).getTime() + 300){
                explodeList.get(i).setVisible(false);
            }
        }

        //remove invisible explosions
        for(int i = 0; i < explodeList.size();i++){
            if(!explodeList.get(i).isVisible()){
                explodeList.remove(i);
            }
        }

        //when the player is out of life, start the game over animation
        if(encryptList.isEmpty()){
            displayGameOver = true;
        }

        //if zardo's HP is reduces to 0 kill the media player, and set flag for next level
        if(zardoHP == 0){
            mediaPlayer.setLooping(false);
            mediaPlayer.stop();
            mediaPlayer.release();
            nextLevel = true;
        }

        //gravity applies to player
        if(player.getYpos() < (groundLevel - tileHeight)){
            player.setFalling(true);
        }else{
            player.setYPos(groundLevel - tileHeight);
            player.setFalling(false);
        }
        //see if hes hit anything
        detectPlayerCollision();
        //update him
        player.update();

        //based on time, teleport Zardo's position
        zardoTime = System.currentTimeMillis();
        if(zTimerSwitch == 0){
            maxZardoTime = System.currentTimeMillis() +7000;
            zTimerSwitch = 1;
        }
        if(zardoTime < maxZardoTime){
            //update his position
            zardo.update();
        }else{
            //set his position to random one
            zardo.resetZardo();
            zTimerSwitch = 0;
        }

        //if zardo intersects the player, player loses a life
        if(RectF.intersects(zardo.getWhereToDraw(),player.getWhereToDraw())){
            if(!encryptList.isEmpty()){
                encryptList.remove(0);
            }
        }

        //if players out of life, update game over for animation, there are 3 frames
        if(displayGameOver){
            updateGameOver();
            if(gameOverSwitch == 0){
                gameOverLastFrameChangeTime = System.currentTimeMillis();
                gameOverSwitch++;
            }
        }


    }

    //Mandated World method, draw the updated positions of the objects
    public void draw() {

        //needed to draw to screen
        if (ourHolder.getSurface().isValid()) {
            //lock canvas for drawing
            canvas = this.ourHolder.lockCanvas();

            //set initial color to black
            canvas.drawColor(Color.argb(255, 0, 0, 0));

            //each level will have a different color background, ground is the same color
            //the tile map only draws certain bitmaps with much space in between them
            //this is because its much less intensive to draw a couple large rectangles
            //than many to fill in the background/ground gaps individually
            switch (numLevel) {

                case 3:
                    paint.setColor(Color.argb(255, 227, 153, 216));
                    canvas.drawRect(background, paint);

                    paint.setColor(Color.argb(255,97,38,13));
                    canvas.drawRect(ground, paint);
                    break;
                case 6:

                    paint.setColor(Color.argb(255, 153, 255, 190));
                    canvas.drawRect(background, paint);

                    paint.setColor(Color.argb(255,97,38,13));
                    canvas.drawRect(ground, paint);
                    break;
                case 9:
                    paint.setColor(Color.argb(255, 153, 255, 190));
                    canvas.drawRect(background, paint);

                    paint.setColor(Color.argb(255,97,38,13));
                    canvas.drawRect(ground, paint);
                    break;

            }

            //draw tileMap
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


            //if player isn't walking or jumping, draw still homie
            //this is because you get a weird animation of his legs when he is supposed to be standing still
            if (!player.getMovingLeft() && !player.getMovingRight() && !player.getJumping()) {

                switch (player.getFacing()) {
                    case "left":
                        canvas.drawBitmap(player.getStillHomieLeft(), player.getHomieRect(), player.getWhereToDraw(), paint);
                        break;
                    case "right":
                        canvas.drawBitmap(player.getStillHomie(), player.getHomieRect(), player.getWhereToDraw(), paint);
                        break;
                }
            }

            //if player is jumping draw homie with jetpack turned on
            if (player.getJumping()) {
                switch (player.getFacing()) {
                    case "left":
                        canvas.drawBitmap(player.getHomieJumpLeft(), player.getHomieRect(), player.getWhereToDraw(), paint);
                        break;
                    case "right":
                        canvas.drawBitmap(player.getHomieJumpRight(), player.getHomieRect(), player.getWhereToDraw(), paint);
                        break;
                }
            }

            //if player is jumping, animate homie walking
            if (player.getMovingRight()) {
                player.animate();
                canvas.drawBitmap(player.getRightImage(), player.getFrameToDraw(), player.getWhereToDraw(), paint);
            }
            if (player.getMovingLeft()) {
                player.animate();
                canvas.drawBitmap(player.getLeftImage(), player.getFrameToDraw(), player.getWhereToDraw(), paint);
            }

            //draw / animate all enemies
            for (int i = 0; i < enemies.size(); i++) {

                if(enemies.get(i).getVisible()) {

                    enemies.get(i).animate();

                    if (enemies.get(i).getMovingLeft()) {
                        canvas.drawBitmap(enemies.get(i).getLeftImage(), enemies.get(i).getFrameToDraw(), enemies.get(i).getWhereToDraw(), paint);
                    }
                    if (enemies.get(i).getMovingRight()) {
                        canvas.drawBitmap(enemies.get(i).getRightImage(), enemies.get(i).getFrameToDraw(), enemies.get(i).getWhereToDraw(), paint);
                    }
                }

            }

            //draw all items that have fallen from Zardo
            for (int i = 0; i < items.size(); i++) {
                canvas.drawBitmap(items.get(i).getStillImg(),items.get(i).getFrameToDraw(),items.get(i).getWhereToDraw(),paint);
            }

            //draw all bullets that have been fired
            if(!bullets.isEmpty()) {
                paint.setColor(Color.argb(255, 255, 255, 57));
                for (int i = 0; i < bullets.size(); i++) {

                    canvas.drawRect(bullets.get(i).getBullet(), paint);

                }
            }

            //draw all grenades that have been thrown
            if(!nadeList.isEmpty()) {
                for (int i = 0; i < nadeList.size(); i++) {

                    canvas.drawBitmap(nadeList.get(i).getNadeImg(), nadeList.get(i).getNadeFrameToDraw(), nadeList.get(i).getNadeWhereToDraw(), paint);
                }
            }
            //draw all exploded grenades
            if(!explodeList.isEmpty()){
                for (int i = 0; i < explodeList.size(); i++){

                    canvas.drawBitmap(explodeList.get(i).getExplodeImg(), explodeList.get(i).getExplodeFrameToDraw(), explodeList.get(i).getExplodeWhereToDraw(), paint);

                }
            }

            //if using life gain, animate / draw red crosses
            if(usingGain.isUsing()){
                usingGain.animate();

                canvas.drawBitmap(usingGain.getActiveImg(), usingGain.getFrameToDraw(), usingGain.getWhereToDraw(),paint);
            }

            //if using shield, animate / draw shield
            if(usingShield.isUsing()){
                usingShield.animate();

                canvas.drawBitmap(usingShield.getActiveImg(),usingShield.getFrameToDraw(),usingShield.getWhereToDraw(),paint);
            }


            //draw items box
            canvas.drawBitmap(itemsMenuButton,itemRect,itemButton,paint);


            paint.setColor(Color.argb(255,255,255,255));

            //draw life points
            paint.setTextSize(tileWidth/4);
            String formattedString = encryptList.toString()
                    .replace(",", "")  //remove the commas
                    .replace("[", "")  //remove the right bracket
                    .replace("]", "")  //remove the left bracket
                    .trim();
            canvas.drawText(""+formattedString, rightButton.right + tileWidth, screenY - ((tileWidth/2) + (tileWidth/6)),paint);

            //draw UI
            canvas.drawBitmap(leftArrow,navButton,leftButton,paint);
            canvas.drawBitmap(rightArrow,navButton,rightButton,paint);
            if(isJumping) {
                canvas.drawBitmap(jetPackOn, jumpButton, jetButton, paint);
            }else {
                canvas.drawBitmap(jetPackOff, jumpButton, jetButton, paint);
            }
            if(isFiring){
                canvas.drawBitmap(fireButtonOn,shootButton,fireButton,paint);
            }else{
                canvas.drawBitmap(fireButtonOff,shootButton,fireButton,paint);
            }

            //if there are items that have been picked up, one of each in the items menu
            //as well as the number of them next to it
            if(isItemMenu) {
                paint.setColor(Color.argb(255, 199, 198, 255));
                canvas.drawRect(itemBox, paint);
                paint.setTextSize(tileWidth/5);
                paint.setColor(Color.argb(255,255,255,255));
                if(numNades > 0){
                    canvas.drawBitmap(grenade,menuButton,nadeButton,paint);
                    canvas.drawText("" + numNades,nadeButton.right + (tileWidth/18),nadeButton.top + (tileHeight/2),paint);
                }
                if(numGains > 0){
                    canvas.drawBitmap(lifeGain,menuButton,gainButton,paint);
                    canvas.drawText(""+ numGains,gainButton.right,gainButton.top + (tileHeight/2),paint);
                }
                if(numShields > 0){
                    canvas.drawBitmap(shield,menuButton,shieldButton,paint);
                    canvas.drawText(""+ numShields,shieldButton.right,shieldButton.top + (tileHeight/2), paint);
                }
            }

            //draw amount of ammount in fire button
            paint.setTextSize(tileWidth/5);
            if(numAmmo > 0){
                canvas.drawText("" + numAmmo, fireButton.left + ((tileWidth/6) + (tileWidth/9)),fireButton.top + ((tileWidth/5) + (tileHeight/8)),paint);
            }

            //depending on which side of the screen zardo is, draw him facing left/right and animate
            if(zardo.getXpos() < screenX/2){
                zardo.setFacing("right");
                zardo.animate();
                canvas.drawBitmap(zardo.getZardoRightImg(),zardo.getFrameToDraw(),zardo.getWhereToDraw(),paint);
            }
            if(zardo.getXpos() >= screenX/2){
                zardo.setFacing("left");
                zardo.animate();
                canvas.drawBitmap(zardo.getZardoLeftImg(),zardo.getFrameToDraw(),zardo.getWhereToDraw(),paint);
            }
            //if he's spawning the enemies, draw / animate the star
            if(zardo.isSpwning()){
                zardo.getSpwnCurrentFrame();
                canvas.drawBitmap(zardo.getSpwnImg(),zardo.getSpwnFrameToDraw(),zardo.getSpwnWhereToDraw(),paint);
            }

            //if the player has ran out of life, display / animate game over
            if(displayGameOver){
                animateGameOver();
                canvas.drawBitmap(gameOver,gameOverFrameToDraw,gameOverWhereToDraw,paint);
            }


            //unlock canvas (required for drawing)
            ourHolder.unlockCanvasAndPost(canvas);

        }
    }

    //mandated World method, overides Android method for getting input for game
    //Motion.Event.ACTION_DOWN gets pushes down
    //Motion.Event.ACTION_UP is when the touch lifts up
    public boolean onTouchEvent(MotionEvent motionEvent){
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN:

                //UI
                if(motionEvent.getX() > leftButton.left && motionEvent.getX() < leftButton.right
                        && motionEvent.getY() > leftButton.top && motionEvent.getY() < leftButton.bottom){

                    player.setMovingLeft(true);
                    player.setFacing("left");
                }
                if(motionEvent.getX() > rightButton.left && motionEvent.getX() < rightButton.right
                        && motionEvent.getY() > rightButton.top && motionEvent.getY() < rightButton.bottom){

                    player.setMovingRight(true);
                    player.setFacing("right");
                }
                if(motionEvent.getX() > jetButton.left && motionEvent.getX() < jetButton.right
                        && motionEvent.getY() > jetButton.top && motionEvent.getY() < jetButton.bottom){
                    player.setJumping(true);
                    isJumping = true;
                }
                if(motionEvent.getX() > fireButton.left && motionEvent.getX() < fireButton.right
                        && motionEvent.getY() > fireButton.top && motionEvent.getY() < fireButton.bottom){

                    isFiring = true;
                    if(numAmmo > 0) {
                        numAmmo--;
                        shoot();
                    }
                }

                //toggle if item button or menu is being displayed
                if(isItemMenu){
                    if(numNades == 0 && numGains == 0 && numShields == 0){
                        if(motionEvent.getX() > itemBox.left && motionEvent.getX() < itemBox.right
                                && motionEvent.getY() > itemBox.top && motionEvent.getY() < itemBox.bottom){
                            isItemMenu = false;
                        }
                    }
                    if(numNades > 0) {
                        if (motionEvent.getX() > nadeButton.left && motionEvent.getX() < nadeButton.right
                                && motionEvent.getY() > nadeButton.top && motionEvent.getY() < nadeButton.bottom) {
                            numNades--;
                            throwGrenade();
                            isItemMenu = false;
                            break;
                        }
                    }
                    if(numGains > 0) {
                        if (motionEvent.getX() > gainButton.left && motionEvent.getX() < gainButton.right
                                && motionEvent.getY() > gainButton.top && motionEvent.getY() < gainButton.bottom) {
                            numGains--;
                            usingGain.setTime(System.currentTimeMillis());
                            usingGain.setUsing(true);
                            isItemMenu = false;
                            break;
                        }
                    }
                    if(numShields > 0) {
                        if (motionEvent.getX() > shieldButton.left && motionEvent.getX() < shieldButton.right
                                && motionEvent.getY() > shieldButton.top && motionEvent.getY() < shieldButton.bottom) {
                            numShields--;
                            usingShield.setTime(System.currentTimeMillis());
                            usingShield.setUsing(true);
                            isItemMenu = false;
                            break;
                        }
                    }
                }

                if(!isItemMenu) {
                    if (motionEvent.getX() > itemButton.left && motionEvent.getX() < itemButton.right
                            && motionEvent.getY() > itemButton.top && motionEvent.getY() < itemButton.bottom) {
                        isItemMenu = true;
                    }
                }

                break;

            case MotionEvent.ACTION_UP:

                //no player input
                player.setMovingLeft(false);
                player.setMovingRight(false);
                player.setJumping(false);
                isFiring = false;
                isJumping = false;


                break;
        }

        return true;
    }


    //fire bullet, set it going in the direction, based on players direction when the bullet was fired
    public void shoot(){
        float yPos = (player.getWhereToDraw().top + ((player.getFrameHeight()/3) + (tileHeight/12)));
        switch (player.getFacing()) {
            case "left":
                Bullet leftBullet = new Bullet("left",player.getWhereToDraw().left,yPos,tileWidth,true,(tileWidth/2)+(tileWidth/4));
                bullets.add(leftBullet);
                break;
            case "right":

                Bullet rightBullet = new Bullet("right", player.getWhereToDraw().right, yPos, tileWidth,true,(tileWidth/2)+(tileWidth/4));
                bullets.add(rightBullet);
                break;
        }

    }

    //create the grenade object for nadeList
    //base the initial position of the grenade based off of players initial direction when the grenade was thrown
    public void throwGrenade(){
        switch (player.getFacing()) {
            case "left":
                Grenade leftNade = new Grenade(context,"left",player.getWhereToDraw().left,
                        (player.getWhereToDraw().top + tileWidth/3),tileWidth/4,tileHeight/4,System.currentTimeMillis(),
                        true,tileWidth/3,tileWidth,tileHeight);
                nadeList.add(leftNade);
                break;
            case "right":
                Grenade rightNade = new Grenade(context,"right",player.getWhereToDraw().right,
                        (player.getWhereToDraw().top + tileWidth/3), tileWidth/4, tileHeight/4, System.currentTimeMillis(),
                        true,tileWidth/3,tileWidth,tileHeight);
                nadeList.add(rightNade);
                break;
        }

    }

    //determine if player has collided with things
    //this method is especially pertinnent in the Level class
    //it essentially draws invisible rectangles around the player
    //when a target object enters the box, the players speed is reduced to the distance between the detect box
    //and the object
    //this is what causes the player to stop when jumping at the top of the screen and at the left / right side of the screen
    float speedTil;
    float jumpTil;
    float jSpeedTil;
    boolean limited;
    boolean archLimited;
    boolean jumpLimited;
    public void detectPlayerCollision() {

        limited = false;
        archLimited = false;
        jumpLimited = false;

        float midX = ((player.getWhereToDraw().right - player.getWhereToDraw().left)/2)+ player.getWhereToDraw().left;
        float tweak = ((player.getWhereToDraw().bottom - player.getWhereToDraw().top)/2)/20;

        //naming convention is as follows:
        //dbR = detect block right
        //dbL = detect block left
        //dbUR = detect block up right
        //dbUL = detect block up left
        //dbDR = detect block down right
        //dbDL = detect block down left
        //dbJR = detect block jump right
        //dbJL = detect block jump left
        //tweak is the modifier to get size of box just right
        RectF dbR = new RectF(midX + (tweak * 3), player.getWhereToDraw().top + (tweak * 3),
                (midX + (tweak * 3)) + playerSpeed, player.getWhereToDraw().bottom - (tweak * 11));
        RectF dbL = new RectF((midX-(tweak*2) - playerSpeed),player.getWhereToDraw().top + (tweak*3),
                midX-(tweak*2),player.getWhereToDraw().bottom - (tweak*11));
        RectF dbUR = new RectF(midX - (tweak*15),(player.getWhereToDraw().top - jumpSpeed),
                midX, player.getWhereToDraw().top + (tweak*7));
        RectF dbUL = new RectF(midX, player.getWhereToDraw().top - jumpSpeed,
                midX + (tweak*15), player.getWhereToDraw().top + (tweak*7));
        RectF dbDR = new RectF(midX - (tweak*15), player.getWhereToDraw().bottom,
                midX, player.getWhereToDraw().bottom + fallSpeed);
        RectF dbDL = new RectF(midX,player.getWhereToDraw().bottom,
                midX + (tweak*15),player.getWhereToDraw().bottom + fallSpeed);
        RectF dbJR = new RectF(midX + (tweak * 3), player.getWhereToDraw().top + (tweak * 3),
                (midX + (tweak * 3)) + archSpeed, player.getWhereToDraw().bottom - (tweak * 11));
        RectF dbJL = new RectF((midX-(tweak*2) - (archSpeed)),player.getWhereToDraw().top + (tweak*3),
                midX-(tweak*2),player.getWhereToDraw().bottom - (tweak*11));


        switch (player.getFacing()){
            case "right":
                if((dbR.right > screenX)){
                    speedTil = screenX - dbR.left;
                    limited = true;
                }
                if((dbJR.right > screenX) && player.getJumping()){
                    jumpTil = screenX - dbJR.left;
                    archLimited = true;
                }
                if(dbUR.top < 0){
                    jSpeedTil = 0 + dbUR.bottom;
                    jumpLimited = true;
                }
                break;
            case "left":
                if((dbL.left < 0)){
                    speedTil = 0 + dbL.right;
                    limited = true;
                }
                if((dbJL.left < 0) && player.getJumping()){
                    jumpTil = 0 + dbJL.right;
                    archLimited = true;
                }
                if(dbUL.top < 0){
                    jSpeedTil = 0 + dbUL.bottom;
                    jumpLimited = true;
                }
                break;
        }

        //if there's a collision with the invisible rectangle and the object, reduce players speed in that speed
        if(limited){
            player.setSpeed(speedTil);
        }else{
            player.setSpeed(playerSpeed);
        }
        if(archLimited){
            player.setArchSpeed(jumpTil);
        }else{
            player.setArchSpeed(archSpeed);
        }
        if(jumpLimited){
            player.setJumpSpeed(jSpeedTil);
        }else{
            player.setJumpSpeed(jumpSpeed);
        }

    }


    //used in setting random values earlier in class
    public int makeRandInt(int min, int max){
        Random random = new Random();

        int randomNumber = random.nextInt((max - min) + 1) + min;
        return randomNumber;
    }

    //manded World methods
    public boolean isAlive(){
        return alive;
    }
    public boolean isNxtLevel(){ return nextLevel; }
    public int getPartOfEscalate(){return partOfEscalate;}
    public boolean isSecretLevel(){return secretLevel;}
    public int getNumNades(){return numNades;}
    public int getNumGains(){return numGains;}
    public int getNumShields(){return numShields;}

    //update game over message position for drawing
    public void updateGameOver(){
        gameOverWhereToDraw.left = screenX/3;
        gameOverWhereToDraw.top = (screenY/2) - (screenY/3);
        gameOverWhereToDraw.right = (screenX/3) + gameOverWidth;
        gameOverWhereToDraw.bottom = ((screenY/2) - (screenY/3))+ gameOverHeight;
    }
    //update animation of game over message for drawing
    public void animateGameOver(){
        long time = System.currentTimeMillis();
        if (time > gameOverLastFrameChangeTime + gameOverFrameLengthInMilliseconds) {
            gameOverLastFrameChangeTime = time;
            gameOverCurrentFrame++;
            if (gameOverCurrentFrame >= gameOverFrameCount) {
                //after frame count kill media player and set alive flag for GameLoop to restart game
                mediaPlayer.setLooping(false);
                mediaPlayer.stop();
                mediaPlayer.release();
                alive = false;
            }


        }
        gameOverFrameToDraw.left = gameOverCurrentFrame * gameOverWidth;
        gameOverFrameToDraw.right = gameOverFrameToDraw.left + gameOverWidth;

    }
    //World mandated methods for pausing the game in Gameloop, for Android override
    public boolean isPlayingMusic(){return playingMusic; }
    public void pauseMusic(){mediaPlayer.pause();}


}
