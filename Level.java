package com.terminalagent.terminalagent;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;


import java.util.ArrayList;
import java.util.Random;

/**
 * Created by gatovato on 10/11/2017.
 */

 //Pretty much the same as the Bossfight class. Some important things to note:
 //1.Because of the scrolling action, Level utilizes the ViewPort class
 //2.The World is defined by size. Because it uses the ViewPort class, for drawing, objects are translated into ViewPort size
 //3.There a few algorithms explained in more detail that dynamically create the gameplay

public class Level implements World {

    private Context context;

    private SurfaceHolder ourHolder;

    //this will be used to create the side scrolling action
    private ViewPort vp;

    private Canvas canvas;

    private Paint paint;

    private float screenX;
    private float screenY;

    private float tileWidth;
    private float tileHeight;

    private boolean alive = true;
    private boolean nextLevel;

    //need some containers for all of our objects
    private ArrayList<Enemy> enemies;

    private ArrayList<Items> items;

    private ArrayList<Blocks> blocks;

    private ArrayList<String> encryptList;

    private ArrayList<Bullet> bullets;

    private ArrayList<Grenade> nadeList;

    private ArrayList<Grenade> explodeList;

    private ArrayList<Items> ammo;

    //player actions
    private boolean isFiring;
    private boolean isJumping;
    private boolean isItemMenu;

    private Player player;

    //terminal sprite
    private Items terminal;

    //secret script
    private Items escalate;

    private TileMap tileMap;

    //used for tileMap
    private int numLevel;


    private int worldLength;
    private int worldHeight;

    private int maxEnemies;
    private int maxItems;
    private int maxBlocks;
    private int numAmmo;


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



    private RectF itemBox;
    private RectF nadeButton;
    private RectF shieldButton;
    private RectF gainButton;


    private Bitmap shield;
    private Bitmap lifeGain;
    private Bitmap grenade;


    // for drawing animations
    private Items usingShield;
    private Items usingGain;



    //it takes up a lot less resources to draw the tiles that are only pertenant to the levels via tileMap and draw the
    //background/ground as one big rectangle
    private RectF background;
    private RectF ground;

    //used to convert to Viewport dimensions
    private RectF convertedBackground;
    private RectF convertedGround;
    private RectF convertedNextFloor;

    //used for pseudo-gravity
    private float groundLevel;

    //for tileMap
    private Bitmap[][] map;

    //once player has 3 parts of escalate they can run priveledge escalation script in terminal
    private int partOfEscalate;
    //used to only allow one to be picked up
    private int escalateCounter;

    //for World mandated method
    private boolean secretLevel = false;

    //has player died?
    private boolean displayGameOver;
    //draw game over banner
    private Bitmap gameOver;
    private int gameOverWidth;
    private int gameOverHeight;
    public Rect gameOverFrameToDraw;
    public RectF gameOverWhereToDraw;
    public int gameOverFrameCount = 3;
    private int gameOverCurrentFrame;
    private long gameOverLastFrameChangeTime = 0;
    private int gameOverFrameLengthInMilliseconds = 1000;
    private int gameOverSwitch = 0;

    //number of players health
    private int numHealth;


    //used to generate players encryption (health)
    String[] encryptString = {"a","%","b","$","c","^","7","8","9","*","1","3","f","e"};

    //Rect used for drawing tileMap
    private Rect mapFrameToDraw;

    //variables for moving player
    private float playerSpeed;
    private float jumpSpeed;
    private float fallSpeed;
    private float archSpeed;

    //variables for getting/playing music
    private MusicLibrary song;
    private MediaPlayer mediaPlayer;
    private int numWorld;
    private boolean playingMusic;

    //with default world dimensions there is a black border around the world dimensions and what the player can see when
    //flying/against end of world
    //these cover that up
    private Bitmap extraPanelLeft;
    private Bitmap extraPanelRight;
    private Rect extraPanelFrameToDraw;
    private RectF extraPLWhereToDraw;
    private RectF extraPRWhereToDraw;
    private RectF nextFloor;


    public Level(Context context, SurfaceHolder surfaceHolder, float screenX, float screenY,int numLevel, int worldLength,
                 int worldHeight,String enemyList[], int maxEnemies, int maxItems, int maxBlocks, int numHealth, int numNades,
                 int numShields, int numGains, float tileWidth, float tileHeight,int partOfEscalate, int numWorld, boolean playingMusic){

        this.context = context;
        ourHolder = surfaceHolder;
        this.screenX = screenX;
        this.screenY = screenY;

        paint = new Paint();

        this.numLevel = numLevel;

        this.worldLength = worldLength;
        this.worldHeight = worldHeight;

        this.partOfEscalate = partOfEscalate;
        escalateCounter = 0;

        this.numHealth = numHealth;

        this.maxEnemies = maxEnemies;
        this.maxItems =  maxItems;
        this.maxBlocks = maxBlocks;


        this.numNades = numNades;
        this.numShields = numShields;
        this.numGains = numGains;


        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;

        this.numWorld = numWorld;
        this.playingMusic = playingMusic;

        //get random amount of objects
        int randEnemies = makeRandInt(7,maxEnemies);
        int randItems = makeRandInt(3,maxItems);
        int randBlocks = makeRandInt(15,maxBlocks);
        //ammo should at least be able to fend off the enemies, and at most be able to
        //remove everything
        int randAmmo = makeRandInt(randEnemies,(randBlocks+randEnemies));

        //create arrayLists to match
        enemies = new ArrayList<>(randEnemies);
        items = new ArrayList<>(randItems);
        blocks = new ArrayList<>(randBlocks);
        ammo = new ArrayList<>(randAmmo);
        nadeList = new ArrayList<>();
        explodeList = new ArrayList<>();

        //define player speed
        playerSpeed = tileWidth/7;
        jumpSpeed = (tileWidth/4) * (float)1.5;
        fallSpeed = tileWidth/8;
        archSpeed = playerSpeed/8;

        groundLevel = worldHeight + (tileHeight/3);
        player = new Player(tileWidth/2,groundLevel - tileHeight,context,tileWidth, tileHeight,6,"right",playerSpeed,fallSpeed,archSpeed,jumpSpeed);


        vp = new ViewPort(Math.round(screenX),Math.round(screenY));
        //this is based on exactly half of player, see getCenter() for me info
        vp.setWorldCentre(getCenter().x,getCenter().y);


        String makeEnemy[] = enemyList;

        //make random enemies, based on enemyList
        for(int i = 0;i < randEnemies;i++){
            float randX = makeRandInt(Math.round(tileWidth*2),worldLength);
            float randY = makeRandInt(0,worldHeight-(worldHeight/3));

            Random enemyType = new Random();
            int randomEnemy = enemyType.nextInt(makeEnemy.length);

            switch(makeEnemy[randomEnemy]){
                case "pwnwlkr":
                    Enemy pwnwlker = new Enemy(context,"pwnwlkr","pwnwlkrl",randX,randY,tileWidth,tileHeight,tileWidth/40,4,true,false,false);
                    enemies.add(pwnwlker);
                    break;

                case "flyguy":
                    Enemy flyGuy = new Enemy(context,"flyguys","flyguys", randX, randY,tileWidth/2,tileHeight/2,tileWidth/20,2,true,true,false);
                    enemies.add(flyGuy);
                    break;

                case "trojan":
                    Enemy trojan = new Enemy(context,"trojanlft","trojanrght",randX, randY, tileWidth+(tileWidth/2),tileHeight + (tileHeight/2),tileWidth/30,3,true,false,false);
                    enemies.add(trojan);
                    break;

                case "worm":
                    Enemy worm = new Enemy(context,"wurmleft","wurmright",randX, randY, tileWidth*2,tileHeight*1,tileWidth/20,6,true,true,true);
                    enemies.add(worm);
                    break;
            }

        }

        //make random sized/positioned blocks
        for(int i = 0; i < randBlocks; i++){

            //used to determine if block will be envelope or file system
            float randVal = makeRandInt(0,randBlocks);
            float randVal2 = makeRandInt(0,randBlocks);

            //used to make varying sized blocks
            float randSize = makeRandInt(2,4);

            //starting point for dimensions of envelopes and file systems
            float envelopeWidth = tileWidth*2;
            float envelopeHeight = tileWidth*2;

            float filesysWidth = tileWidth;
            float filesysHeight = tileHeight*2;

            //put block randomly in World
            float randX = makeRandInt(Math.round(tileWidth*2),Math.round(worldLength - tileWidth*2));
            float randY = makeRandInt(Math.round(tileHeight),worldHeight-(worldHeight/3));

            //is block envelope or file system? make it a random size
            if(randVal2 < randVal) {
                Blocks block = new Blocks(context,"envelopebg",randX,randY,envelopeWidth/randSize,envelopeHeight/randSize);
                blocks.add(block);
            }
            if(randVal2 >= randVal){
                Blocks block = new Blocks(context,"filesystembg",randX,randY,filesysWidth/randSize,filesysHeight/randSize);
                blocks.add(block);
            }

        }

        //put random items behind random blocks
        for(int i = 0; i < randItems; i++){

            //used to choose which item to choose
            int randValue = makeRandInt(0,2);

            //get random blocks x,y position, this will be the x,y for the item
            int rand = makeRandInt(0,(blocks.size()-1));
            float randX = blocks.get(rand).getXpos();
            float randY = blocks.get(rand).getYpos();

            //initialize new item with x,y of block, add to items list
            if(randValue == 0){
                Items lifegain = new Items(context,"lifegain",randX,randY,tileWidth/2,tileHeight/2,0,false);
                items.add(lifegain);
            }
            if(randValue == 1){
                Items shield = new Items(context,"shield",randX, randY, tileWidth/2,tileHeight/2,0,false);
                items.add(shield);
            }
            if(randValue == 2){
                Items grenade = new Items(context,"grenade",randX,randY,tileWidth/4,tileHeight/4,0,false);
                items.add(grenade);
            }
        }

        //This is ammo that can be found by the player, should be at minimum enough to kill all enemies, and max
        //to remove the blocks
        numAmmo = makeRandInt(enemies.size(),maxBlocks);
        for(int i = 0; i < randAmmo;i++){
            //pick random block, blocks x,y will become ammo item's x,y
            int rand = makeRandInt(0,(blocks.size()-1));
            float randX = blocks.get(rand).getXpos();
            float randY = blocks.get(rand).getYpos();

            Items foundAmmo = new Items(context,"ammo",randX,randY,tileWidth/2,tileHeight/2,0,false);
            ammo.add(foundAmmo);
        }

        //pick random block,it's x,y will become terminal's x,y
        int randum = makeRandInt(0,(blocks.size()-1));
        float randumX = blocks.get(randum).getXpos();
        float randumY = blocks.get(randum).getYpos();
        terminal = new Items (context,"terminal",randumX,randumY,tileWidth,tileHeight,0,false);
        terminal.setBlockNum(randum);

        //pick random block, it's x,y will become priveledge escalate script's x,y
        int random = makeRandInt(0,(blocks.size()-1));
        float randomX = blocks.get(random).getXpos();
        float randomY = blocks.get(random).getYpos();
        escalate = new Items(context,"escalate",randomX,randomY,tileWidth/2,tileHeight/2,5,false);

        //fill life bar with encryptString characters
        encryptList = new ArrayList<>(numHealth);
        for(int i = 0; i < numHealth;i++){
            int randString = makeRandInt(0,(encryptString.length-1));
            String letter = encryptString[randString];
            encryptList.add(letter);

        }




        tileMap = new TileMap(numLevel,context,tileWidth,tileHeight);

        //UI
        int navWidth = Math.round((tileWidth/2) + (tileWidth/8));
        int navHeight = Math.round(tileHeight - (tileHeight/4));

        int jumpWidth = Math.round(tileWidth/2);
        int jumpHeight = Math.round(tileHeight);

        int shootWidth = Math.round((tileWidth/2) + (tileWidth/8));
        int shootHeight = Math.round(tileWidth/2 + (tileWidth/8));

        int itemWidth = Math.round(tileWidth);
        int itemHeight = Math.round(tileHeight/3);

        navButton = new Rect(0,0,navWidth,navHeight);
        jumpButton = new Rect(0,0,jumpWidth,jumpHeight);
        shootButton = new Rect (0,0,shootWidth,shootHeight);
        itemRect = new Rect(0,0,itemWidth,itemHeight);


        leftButton = new RectF(tileWidth/6,worldHeight,
                (tileWidth/6)+ navWidth,worldHeight + navHeight);

        rightButton = new RectF((tileWidth/6)+ (navWidth + (tileWidth/2)),worldHeight,
                ((tileWidth/6) + (navWidth + (tileWidth/2)))+ navWidth, worldHeight + navHeight);

        jetButton = new RectF((screenX/2) + (tileWidth + (tileWidth/3)),worldHeight,
                (screenX/2) + (tileWidth + (tileWidth/3))+ jumpWidth, worldHeight + jumpHeight);

        fireButton = new RectF(screenX - (tileWidth),worldHeight,
                (screenX - (tileWidth)) + shootWidth, worldHeight + shootHeight);

        itemButton = new RectF((screenX/2) - (tileWidth/2), worldHeight + (tileHeight/6),
                ((screenX/2) - (tileWidth/2)) + itemWidth, (worldHeight + (tileHeight/6)) + itemHeight);

        itemBox = new RectF (rightButton.right + (tileWidth/10), worldHeight,
                jetButton.left - (tileWidth/10),worldHeight + (navHeight + (tileHeight/10)));

        int menuItemWidth = Math.round((itemBox.right - itemBox.left)/5);
        int menuItemHeight = Math.round(tileHeight/4);
        menuButton = new Rect(0,0,menuItemWidth,menuItemHeight);

        nadeButton = new RectF(itemBox.left + (menuItemWidth/3),itemBox.top + (tileHeight/25),
                (itemBox.left + (menuItemWidth/3)) + menuItemWidth,itemBox.bottom - (tileHeight/25));

        gainButton = new RectF(nadeButton.right + (menuItemWidth/2),itemBox.top + (tileHeight/25),
                (nadeButton.right + (menuItemWidth/2)) + menuItemWidth,itemBox.bottom - (tileHeight/25));

        shieldButton = new RectF(gainButton.right + (menuItemWidth/2), itemBox.top + (tileHeight/25),
                (gainButton.right + (menuItemWidth/2)) + menuItemWidth, itemBox.bottom - (tileHeight/25));

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



        //for when player is using shield or life gain
        usingShield = new Items(context,"activeshield",player.getWhereToDraw().right,player.getWhereToDraw().top,tileWidth,tileHeight,5,false);
        usingGain = new Items(context,"animelife",player.getWhereToDraw().right,player.getWhereToDraw().top + (tileWidth/3),tileWidth/3,tileHeight/2,3,false);

        //as mentioned above, it's less resource intensive to generate 2 large rectangles for the (ground/floor)/(sky/ceiling )
        if(numLevel > 2) {
            background = new RectF(0 - (tileWidth * 2), 0 - (tileHeight + (tileHeight/2)), worldLength + (tileWidth * 5), worldHeight + (tileHeight / 4));
        }else{
            background = new RectF(0 - (tileWidth * 2), 0 - (tileHeight*3), worldLength + (tileWidth * 5), worldHeight + (tileHeight / 4));
        }
        if(numLevel > 2) {
            ground = new RectF(0 - (tileWidth * 2), worldHeight - (tileHeight/3), worldLength + (tileWidth * 5), screenY + tileHeight);
        }else{
            ground = new RectF(0 - (tileWidth * 2), worldHeight, worldLength + (tileWidth * 5), screenY + tileHeight);
        }

        if(numLevel > 2){
            nextFloor = new RectF(0 -(tileWidth*2),0 - (tileHeight*3),worldLength + (tileWidth*5),0 - (tileHeight + (tileHeight/2)));
        }


        map = tileMap.getTileMap();
        mapFrameToDraw = tileMap.getFrameToDraw();
        tileMap = null;

        bullets = new ArrayList<>();

        //game over animation
        gameOverWidth = Math.round(tileWidth * 4);
        gameOverHeight = Math.round(tileHeight *2);
        gameOver = BitmapFactory.decodeResource(context.getResources(), R.drawable.disconnected);
        gameOver = Bitmap.createScaledBitmap(gameOver, gameOverWidth * gameOverFrameCount,gameOverHeight,false);
        gameOverFrameToDraw = new Rect(0,0,gameOverWidth,gameOverHeight);
        gameOverWhereToDraw = new RectF(0,0,0,0);
        displayGameOver = false;

        //get a song from Music Library, and initialize mediaPlayer, if the flag from the intro scene / terminal is set to play
        //music, then start the mediaPlayer
        song = new MusicLibrary(context,numWorld);
        int rId = song.getSong();
        mediaPlayer = MediaPlayer.create(context,rId);
        mediaPlayer.setLooping(true);

        if(playingMusic) {
            mediaPlayer.start();
        }
        //starting in Level 3, there is a paneling in the manor that needs to be added to cover up some excess black space
        //this is easily done in Level's 1 & 2 by extending the ground/sky
        if(numLevel > 2){
            extraPanelFrameToDraw = new Rect(0,0,Math.round(tileWidth),Math.round(tileHeight));

            extraPanelLeft  = BitmapFactory.decodeResource(context.getResources(),R.drawable.panelleft);
            extraPanelLeft = Bitmap.createScaledBitmap(extraPanelLeft,Math.round(tileWidth),Math.round(tileHeight),false);

            extraPLWhereToDraw = new RectF(0-(tileWidth*2),(worldHeight + (tileHeight/4))-tileHeight,(0-(tileWidth*2))+tileWidth,(worldHeight + (tileHeight/4)));

            extraPanelRight = BitmapFactory.decodeResource(context.getResources(),R.drawable.panelright);
            extraPanelRight = Bitmap.createScaledBitmap(extraPanelRight,Math.round(tileWidth),Math.round(tileHeight),false);

            extraPRWhereToDraw = new RectF(0-(tileWidth),(worldHeight + (tileHeight/4))-tileHeight,(0-tileWidth)+tileWidth,(worldHeight + (tileHeight/4)));
        }
    }

    public void update(){
        //center is updated every loop as player's movement will change
        vp.setWorldCentre(getCenter().x,getCenter().y);

      //the following clipping for loops will determine if a player is outside of the viewport, in which case it will
        //be clipped. Clipped objects are not updated or drawn
        for (int i = 0; i < blocks.size();i++) {
            if (vp.clipObjects(blocks.get(i).getWhereToDraw().left, blocks.get(i).getWhereToDraw().top,
                    blocks.get(i).getWhereToDraw().right, blocks.get(i).getWhereToDraw().bottom)) {

                blocks.get(i).clip();
            } else {
                blocks.get(i).unClip();
            }
        }

        if(terminal.getVisible()) {
            if (vp.clipObjects(terminal.getWhereToDraw().left, terminal.getWhereToDraw().top,
                    terminal.getWhereToDraw().right, terminal.getWhereToDraw().bottom)) {

                terminal.clip();
            } else {
                terminal.unClip();
            }
        }

        if(escalate.getVisible()) {
            if (vp.clipObjects(escalate.getWhereToDraw().left, escalate.getWhereToDraw().top,
                    escalate.getWhereToDraw().right, escalate.getWhereToDraw().bottom)) {

                escalate.clip();
            } else {
                escalate.unClip();
            }
        }

        for(int i = 0; i < items.size();i++){
            if(items.get(i).getVisible()){
                if(vp.clipObjects(items.get(i).getWhereToDraw().left,items.get(i).getWhereToDraw().top,
                        items.get(i).getWhereToDraw().right,items.get(i).getWhereToDraw().bottom)){
                    items.get(i).clip();
                }else{
                    items.get(i).unClip();
                }
            }
        }

        for(int i = 0; i < enemies.size();i++){
            if(vp.clipObjects(enemies.get(i).getWhereToDraw().left,enemies.get(i).getWhereToDraw().top,
                    enemies.get(i).getWhereToDraw().right, enemies.get(i).getWhereToDraw().bottom)){
                enemies.get(i).clip();
            }else{
                enemies.get(i).unClip();
            }
        }

        for(int i = 0; i < ammo.size();i++){
            if(vp.clipObjects(ammo.get(i).getWhereToDraw().left,ammo.get(i).getWhereToDraw().top,
                    ammo.get(i).getWhereToDraw().right, ammo.get(i).getWhereToDraw().bottom)){
                ammo.get(i).clip();
            }else{
                ammo.get(i).unClip();
            }
        }

        //iterate through shot bullets
        //because bullet will be removed, and to prevent out of bounds, break the loop
        bulletLoop:
        if(!bullets.isEmpty()){
            for(int i = 0; i < bullets.size();i++) {
                //update bullets position for drawing
                bullets.get(i).update();

                //see if any enemies are hit, if so set them to be removed. Delete bullet
                for (int e = 0; e < enemies.size(); e++) {
                    if (!enemies.get(e).isDeleting()) {
                        if (RectF.intersects(bullets.get(i).getBullet(), enemies.get(e).getWhereToDraw())) {

                            enemies.get(e).setDeleting(true);
                            bullets.remove(i);
                            break bulletLoop;
                        }
                    }
                }

                // see if player is hit by bullet, if so remove bullet, remove a life from the player
                if(RectF.intersects(bullets.get(i).getBullet(), player.getWhereToDraw())){
                    if(!encryptList.isEmpty()) {
                        encryptList.remove(0);
                        bullets.remove(i);
                        break bulletLoop;
                    }
                }

                //see if the bullet hits a block
                for (int b = 0; b < blocks.size(); b++) {
                    if (!blocks.get(b).isDeleting()) {
                        if (RectF.intersects(bullets.get(i).getBullet(), blocks.get(b).getWhereToDraw())) {
                            //check to see if block that was hit matches xPos with any item, if so,  set item to visible
                            for (int n = 0; n < items.size(); n++) {
                                if (blocks.get(b).getXpos() == items.get(n).getXpos()) {
                                    items.get(n).setVisisble(true);
                                }
                            }
                            //check to see if any ammo matches xPos with block, if so, set ammo to visible
                            for (int a = 0; a < ammo.size(); a++) {
                                if (blocks.get(b).getXpos() == ammo.get(a).getXpos()) {
                                    ammo.get(a).setVisisble(true);
                                }
                            }
                            //check to see if the terminal matches the xPos of the block, if so, set to visible
                            if (blocks.get(b).getXpos() == terminal.getXpos()) {
                                terminal.setVisisble(true);
                            }
                            //for deletion animation
                            blocks.get(b).setDeleting(true);
                            bullets.remove(i);
                            break bulletLoop;
                        }
                    }
                }

                //if bullet goes to the players left 1/3 of world width, remove
                if (bullets.get(i).getBullet().left < player.getWhereToDraw().left - (worldLength / 3)) {

                    bullets.remove(i);
                    break bulletLoop;
                }
                //if bullet goes out to the players right 1/3 of the world width, remove
                if (bullets.get(i).getBullet().right > player.getWhereToDraw().right + (worldLength / 3)) {

                    bullets.remove(i);
                    break bulletLoop;
                }
            }
        }
        //from above bullet iteration, remove any enemies that have been flagged for deletion
        //animate the deletion
        for(int i = 0; i < enemies.size();i++){
            if(enemies.get(i).isDeleting()){
                enemies.get(i).updateDeletion();
            }
            if(enemies.get(i).isGone()){
                enemies.remove(i);
            }
        }

        //from bullet interation, remove any blocks that have been flagged for deletion
        //animate deletion
        for(int i = 0; i < blocks.size();i++){
            if(blocks.get(i).isDeleting()){
                blocks.get(i).updateDeletion();
            }
            if(blocks.get(i).isGone()){
                blocks.remove(i);
            }
        }

        //iterate through enemies to do enemy things
        for(int i = 0; i < enemies.size(); i++) {
            //this way if the player runs through a deleted enemy that's animating he won't lose a life
            if (!enemies.get(i).isDeleting()) {
                //if enemie collides with player, lose life
                if (RectF.intersects(enemies.get(i).getWhereToDraw(), player.getWhereToDraw())) {
                    if (!encryptList.isEmpty()) {
                        encryptList.remove(0);
                    }
                }
                //the worm enemy can pass through blocks, the rest should bounce off of blocks and move in the opposite way
                for (int b = 0; b < blocks.size(); b++) {
                    if (!enemies.get(i).isThruBlocks()) {
                        if (RectF.intersects(enemies.get(i).getWhereToDraw(), blocks.get(b).getWhereToDraw())) {
                            if (enemies.get(i).getMovingLeft()) {
                                enemies.get(i).setMovingLeft(false);
                                enemies.get(i).setMovingRight(true);
                            } else {
                                enemies.get(i).setMovingRight(false);
                                enemies.get(i).setMovingLeft(true);
                            }
                        }
                    }
                }
                //if enemy reaches the left side of the screen, send moving right
                if (enemies.get(i).getXpos() < 0) {

                    enemies.get(i).setMovingLeft(false);
                    enemies.get(i).setMovingRight(true);
                }
                //if enemy reaches the right side of the screen, send moving left
                if (enemies.get(i).getWhereToDraw().right > worldLength) {

                    enemies.get(i).setMovingRight(false);
                    enemies.get(i).setMovingLeft(true);

                }

                //if the enemie isn't flying, apply gravity
                if (!enemies.get(i).isFlying()) {
                    if (enemies.get(i).getYpos() < (groundLevel - enemies.get(i).getFrameHeight())) {
                        enemies.get(i).setYPos(enemies.get(i).getYpos() + (tileHeight / 8));
                    } else {
                        enemies.get(i).setYPos(groundLevel - enemies.get(i).getFrameHeight());
                    }
                }

                if (!enemies.get(i).isClipped()) {
                    enemies.get(i).update();
                }
            }
        }

        //if the item is visible, and the player intersects with it, add it to the inventory
        for (int i = 0; i < items.size();i++){
            loop:
            if(items.get(i).getVisible()) {
                if(!items.get(i).isClipped()) {
                    items.get(i).update();
                    if (RectF.intersects(items.get(i).getWhereToDraw(), player.getWhereToDraw())) {
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
                        break loop;
                    }


                }
            }
        }

        //if the player intersects with ammo, add random amount to players ammo
        for(int i = 0; i < ammo.size();i++){
            loop:
            if(ammo.get(i).getVisible()){
                if(!ammo.get(i).isClipped()){
                    ammo.get(i).update();
                    if(RectF.intersects(ammo.get(i).getWhereToDraw(), player.getWhereToDraw())){
                        int rand = makeRandInt(1,(maxEnemies/3));
                        numAmmo = numAmmo + rand;
                        ammo.remove(i);
                        break loop;

                    }
                }
            }
        }


        // if the blocks aren't being deleted, or clipped, update
        for (int i = 0; i < blocks.size();i++) {
            if(!blocks.get(i).isDeleting()) {
                if (!blocks.get(i).isClipped()) {
                    blocks.get(i).update();
                }
            }
        }

        //if the player is using shield, animate the shield
        //if player is facing left/right have the shield on the matching side
        // if an enemy collides with shield send it going in the opposite direction
        //only have the shield active for 5 seconds
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
            if(time < usingShield.getTime()+5000) {
                usingShield.update();
                for(int i = 0; i < enemies.size();i++){
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

        //if using lifegain , have the animation drawn on the side the player is facing
        //for .5 seconds add random amount of life
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

        //iterate through grenades that have been thrown
        for(int i = 0; i < nadeList.size(); i++){
            //set get current time, to compare against time nade was thrown
            long time = System.currentTimeMillis();

            //used to apply friction once it's hit the ground
            boolean hitGround;
            //apply gravity to grenade
            if(nadeList.get(i).getNadeWhereToDraw().bottom < groundLevel - (tileHeight/5)){
                hitGround = false;
            }else{
                hitGround = true;
            }

            //if the grenade is at ground level have it move on x axis at groundlevel,at slower speed
            nadeList.get(i).updateNade(hitGround);
            //check to see if grenade hits the block, if so send it moving in opposite direction
            detectNadeCollision();

            //once it's been 12 seconds since being thrown, explode the grenade
            if(time > nadeList.get(i).getTime() + 1200){
                nadeList.get(i).setVisible(false);
                switch (nadeList.get(i).getHeading()) {
                    case "left":
                        Grenade explosion = new Grenade(context, "null", nadeList.get(i).getxPos()-tileWidth, nadeList.get(i).getyPos() - (tileWidth*2), tileWidth*3, tileWidth*3,
                            System.currentTimeMillis(), true, tileWidth / 4,tileWidth,tileHeight);
                        explodeList.add(explosion);
                        break;
                    case "right":
                        Grenade explosion1 = new Grenade(context,"null",nadeList.get(i).getxPos(),nadeList.get(i).getyPos() - (tileWidth*2),tileWidth*3,tileWidth*3,
                                System.currentTimeMillis(),true,tileWidth/2,tileWidth,tileHeight);
                        explodeList.add(explosion1);
                        break;
                }
            }

        }
        //to prevent errors, once it's exploded above,the grenade will be set invisible then here, it will remove invisilbe
        //grenades
        for(int i = 0; i < nadeList.size();i++){
            if(!nadeList.get(i).isVisible()){
                nadeList.remove(i);
            }
        }
        //see if explosions collide with anything
        for(int i = 0; i < explodeList.size(); i++){
            long time = System.currentTimeMillis();

            //for drawing
            explodeList.get(i).updateExplosion();

            //see if it hits block, if so, set deleting
            for(int b = 0; b < blocks.size(); b++){
                if(blocks.get(b).getWhereToDraw().left > explodeList.get(i).getExplodeWhereToDraw().left - (tileWidth) && blocks.get(b).getWhereToDraw().left <
                        explodeList.get(i).getExplodeWhereToDraw().right + (tileWidth) && blocks.get(b).getWhereToDraw().top > explodeList.get(i).getExplodeWhereToDraw().top - (tileWidth) &&
                        blocks.get(b).getWhereToDraw().top < explodeList.get(i).getExplodeWhereToDraw().bottom + (tileWidth)) {

                    if (blocks.get(b).getXpos() == terminal.getXpos()) {
                        terminal.setVisisble(true);
                    }
                    blocks.get(b).setDeleting(true);
                }
            }

            //see if it hits enemies, if so, set deleting
            for(int e = 0; e < enemies.size();e++){
                if(enemies.get(e).getWhereToDraw().left > explodeList.get(i).getExplodeWhereToDraw().left - (tileWidth) && enemies.get(e).getWhereToDraw().left <
                        explodeList.get(i).getExplodeWhereToDraw().right + (tileWidth) && enemies.get(e).getWhereToDraw().top > explodeList.get(i).getExplodeWhereToDraw().top - (tileWidth)&&
                        enemies.get(e).getWhereToDraw().top < explodeList.get(i).getExplodeWhereToDraw().bottom + (tileWidth)) {

                    enemies.get(e).setDeleting(true);

                }
            }
            //if it hits player, remove life points
            if(RectF.intersects(explodeList.get(i).getExplodeWhereToDraw(), player.getWhereToDraw())){
                if(!encryptList.isEmpty()) {
                    encryptList.remove(0);
                }
            }
            //explosion lasts for 3 seconds, then set it invisible
            if(time > explodeList.get(i).getTime() + 300){
                explodeList.get(i).setVisible(false);
            }
        }
        //if explosion is invisible, remove it
        for(int i = 0; i < explodeList.size();i++){
            if(!explodeList.get(i).isVisible()){
                explodeList.remove(i);
            }
        }

        //if there isn't any life points, send game over message/restart
        if(encryptList.isEmpty()){
            displayGameOver = true;
        }

        //update terminal if visible and not clipped. If player collides with terminal, stop & release mediaPlayer
        //set next level flag
        if(terminal.getVisible()) {
            if(!terminal.isClipped()) {
                terminal.update();

                if (RectF.intersects(terminal.getWhereToDraw(),player.getWhereToDraw())) {
                    mediaPlayer.setLooping(false);
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    nextLevel = true;
                }
            }

        }


        //apply gravity to player
        if(player.getYpos() < (groundLevel - tileHeight)){
            player.setFalling(true);

        }else{
            player.setYPos(groundLevel - tileHeight);
            player.setFalling(false);
        }
        //see if player has intersected anything
        detectPlayerCollision();
        //update for drawing
        player.update();

        //if all the blocks have been removed, show priviledge escalate script
        if(blocks.isEmpty()){
            if(partOfEscalate < 3) {
                if (escalateCounter < 1) {
                    escalate.setVisisble(true);
                    escalateCounter++;
                }
            }

        }

        //if player collides with priviledge escalate script, increase partOfEscalate, which is a variable
        //for a World mandated method which is passed throughout GameLoop
        if (escalate.getVisible()) {
            if(!escalate.isClipped()) {
                escalate.update();
                if (RectF.intersects(player.getWhereToDraw(),escalate.getWhereToDraw())) {
                    partOfEscalate++;
                    escalate.setVisisble(false);
                }
            }
        }

        //if the player doesn't have ammo or grenades and the terminal isn't displayed, the player will be
        //trapped in the game, signal game over
        if(numAmmo == 0 && !terminal.isVisible && numNades == 0){
            displayGameOver = true;
        }
        //update / set timer for game over animation
        if(displayGameOver){
            updateGameOver();
            if(gameOverSwitch == 0){
                gameOverLastFrameChangeTime = System.currentTimeMillis();
                gameOverSwitch++;
            }
        }


    }


    public void draw(){

        if(ourHolder.getSurface().isValid()){
            canvas = this.ourHolder.lockCanvas();

            canvas.drawColor(Color.argb(255, 0, 0, 0));


            //it's better for resources to generate a rectangle for ground/floor and a rectangle for the sky/ceiling
            //than it is to include them in the tileMap (lots of smaller ground/floor/sky/ceiling tiles)
            //instead, depending on the level draw corresponding rectangles
            switch(numLevel){

                case 1: case 2:

                    convertedGround = new RectF(vp.worldToScreen(ground.left, ground.top,
                            ground.right - ground.left,
                            ground.bottom - ground.top));
                    paint.setColor(Color.argb(255, 54, 131, 0));
                    canvas.drawRect(convertedGround,paint);

                    convertedBackground = new RectF(vp.worldToScreen(background.left,background.top,background.right - background.left,
                            background.bottom - background.top));

                    paint.setColor(Color.argb(255,26,128,182));
                    canvas.drawRect(convertedBackground,paint);

                    break;
                case 4:case 5:


                    convertedBackground = new RectF(vp.worldToScreen(background.left,background.top,background.right - background.left,
                            background.bottom - background.top));
                    paint.setColor(Color.argb(255, 227, 153, 216));
                    canvas.drawRect(convertedBackground,paint);

                    convertedGround = new RectF(vp.worldToScreen(ground.left, ground.top,
                            ground.right - ground.left,
                            ground.bottom - ground.top));
                    paint.setColor(Color.argb(255,97,38,13));
                    canvas.drawRect(convertedGround,paint);

                    convertedNextFloor = new RectF(vp.worldToScreen(nextFloor.left, nextFloor.top,
                            nextFloor.right - nextFloor.left,
                            nextFloor.bottom - nextFloor.top));
                    paint.setColor(Color.argb(255,97,38,13));
                    canvas.drawRect(convertedNextFloor,paint);

                    break;
                case 7:case 8:


                    convertedBackground = new RectF(vp.worldToScreen(background.left,background.top,background.right - background.left,
                            background.bottom - background.top));
                    paint.setColor(Color.argb(255, 153, 255, 190));
                    canvas.drawRect(convertedBackground,paint);

                    convertedGround = new RectF(vp.worldToScreen(ground.left, ground.top,
                            ground.right - ground.left,
                            ground.bottom - ground.top));
                    paint.setColor(Color.argb(255,97,38,13));
                    canvas.drawRect(convertedGround,paint);

                    convertedNextFloor = new RectF(vp.worldToScreen(nextFloor.left, nextFloor.top,
                            nextFloor.right - nextFloor.left,
                            nextFloor.bottom - nextFloor.top));
                    paint.setColor(Color.argb(255,97,38,13));
                    canvas.drawRect(convertedNextFloor,paint);

                    break;
            }

            //draw tileMap
            for (int x = 0; x < map.length; x++) {
                for (int y = 0; y < map[x].length; y++) {

                    float xPos = y * tileWidth;
                    float yPos = x * tileHeight;

                    RectF whereToDraw = new RectF(xPos,yPos,xPos+tileWidth,yPos+tileHeight);

                    whereToDraw = vp.worldToScreen(whereToDraw.left,whereToDraw.top,
                            whereToDraw.right - whereToDraw.left,
                            whereToDraw.bottom - whereToDraw.top);

                    if(map[x][y] != null ){
                        canvas.drawBitmap(map[x][y],mapFrameToDraw,whereToDraw,paint);
                    }

                }

            }

            //all levels have black space surrounding the world. This can be viewed easily by flying at the top fo the world y
            //or at the min/max x, this can be easily resolved in the first 2 levels by simply extending the sky/ground rectangles
            //after level 2 each floor of the manor has wall panels, and the wall/floor rectangles extend leaving the wall looking
            //bare without the panels
            //these are the excess panels required to make the wall not look bare
            if(numLevel > 2){

                RectF tmpLeft = vp.worldToScreen(extraPLWhereToDraw.left,extraPLWhereToDraw.top,
                        extraPLWhereToDraw.right - extraPLWhereToDraw.left,
                        extraPLWhereToDraw.bottom - extraPLWhereToDraw.top);

                canvas.drawBitmap(extraPanelLeft,extraPanelFrameToDraw,tmpLeft,paint);

                RectF tmpRight = vp.worldToScreen(extraPRWhereToDraw.left,extraPRWhereToDraw.top,
                        extraPRWhereToDraw.right - extraPRWhereToDraw.left,
                        extraPRWhereToDraw.bottom - extraPRWhereToDraw.top);

                canvas.drawBitmap(extraPanelRight,extraPanelFrameToDraw,tmpRight,paint);

            }

            //convert players position via ViewPort to reflect the World view
            RectF whereToDrawPlayer = vp.worldToScreen(player.getWhereToDraw().left,
                    player.getWhereToDraw().top,
                    player.getWhereToDraw().right - player.getWhereToDraw().left,
                    player.getWhereToDraw().bottom - player.getWhereToDraw().top);


            //if player is standing still, draw him still.
            //this is because fo the moving animation is just stopped his legs will be stuck in an awkward position
            //this is so he will be standing still, with his legs square
            if(!player.getMovingLeft() && !player.getMovingRight() && !player.getJumping()) {

                switch (player.getFacing()) {
                    case "left":
                        canvas.drawBitmap(player.getStillHomieLeft(), player.getHomieRect(), whereToDrawPlayer, paint);
                        break;
                    case "right":
                        canvas.drawBitmap(player.getStillHomie(), player.getHomieRect(), whereToDrawPlayer, paint);
                        break;
                }
            }

            //if the player is jumping, display the corresponding bitmap of player
            if(player.getJumping()){
                switch (player.getFacing()) {
                    case "left":
                        canvas.drawBitmap(player.getHomieJumpLeft(), player.getHomieRect(), whereToDrawPlayer, paint);
                        break;
                    case "right":
                        canvas.drawBitmap(player.getHomieJumpRight(), player.getHomieRect(), whereToDrawPlayer, paint);
                        break;
                }
            }

            //if the player is moving right, animate, display bitmap
            if(player.getMovingRight()){
                player.animate();
                canvas.drawBitmap(player.getRightImage(),player.getFrameToDraw(),whereToDrawPlayer,paint);
            }
            if(player.getMovingLeft()){
                player.animate();
                canvas.drawBitmap(player.getLeftImage(), player.getFrameToDraw(), whereToDrawPlayer, paint);
            }


            //convert enemies position via ViewPort if they aren't being deleted or aren't clipped
            //animate and draw them
            //if they're being deleted, draw deletion
            for(int i = 0; i < enemies.size(); i++) {
                if (!enemies.get(i).isDeleting()) {
                    if (!enemies.get(i).isClipped()) {

                        enemies.get(i).animate();
                        RectF whereToDraw = vp.worldToScreen(enemies.get(i).getWhereToDraw().left,
                                enemies.get(i).getWhereToDraw().top,
                                enemies.get(i).getWhereToDraw().right - enemies.get(i).getWhereToDraw().left,
                                enemies.get(i).getWhereToDraw().bottom - enemies.get(i).getWhereToDraw().top);

                        if (enemies.get(i).getMovingLeft()) {
                            canvas.drawBitmap(enemies.get(i).getLeftImage(), enemies.get(i).getFrameToDraw(), whereToDraw, paint);
                        }
                        if (enemies.get(i).getMovingRight()) {
                            canvas.drawBitmap(enemies.get(i).getRightImage(), enemies.get(i).getFrameToDraw(), whereToDraw, paint);
                        }


                    }
                }else{
                    enemies.get(i).animateDeletion();
                    RectF whereToDraw = vp.worldToScreen(enemies.get(i).getWhereToDraw().left,
                            enemies.get(i).getWhereToDraw().top,
                            enemies.get(i).getWhereToDraw().right - enemies.get(i).getWhereToDraw().left,
                            enemies.get(i).getWhereToDraw().bottom - enemies.get(i).getWhereToDraw().top);

                    canvas.drawBitmap(enemies.get(i).getDeletion(), enemies.get(i).getDeletionFrameToDraw(), whereToDraw, paint);

                }
            }
            //draw blocks if not being deleted or clipped
            //if they are being deleted, draw deletion
            for(int i = 0; i < blocks.size();i++) {
                if (!blocks.get(i).isDeleting()) {
                    if (!blocks.get(i).isClipped()) {
                        RectF whereToDraw = vp.worldToScreen(blocks.get(i).getWhereToDraw().left,
                                blocks.get(i).getWhereToDraw().top,
                                blocks.get(i).getWhereToDraw().right - blocks.get(i).getWhereToDraw().left,
                                blocks.get(i).getWhereToDraw().bottom - blocks.get(i).getWhereToDraw().top);
                        canvas.drawBitmap(blocks.get(i).getStillImg(), blocks.get(i).getFrameToDraw(), whereToDraw, paint);
                    }
                }else{
                    blocks.get(i).animateDeletion();
                    RectF whereToDraw = vp.worldToScreen(blocks.get(i).getWhereToDraw().left,
                            blocks.get(i).getWhereToDraw().top,
                            blocks.get(i).getWhereToDraw().right - blocks.get(i).getWhereToDraw().left,
                            blocks.get(i).getWhereToDraw().bottom - blocks.get(i).getWhereToDraw().top);
                    canvas.drawBitmap(blocks.get(i).getDeletion(), blocks.get(i).getDeletionFrameToDraw(), whereToDraw, paint);
                }
            }
            //draw items that are left from deleted blocks
            for(int i = 0; i < items.size();i++){
                if(items.get(i).getVisible()){
                    if(!items.get(i).isClipped()){
                        RectF whereToDraw = vp.worldToScreen(items.get(i).getWhereToDraw().left,
                                items.get(i).getWhereToDraw().top,
                                items.get(i).getWhereToDraw().right - items.get(i).getWhereToDraw().left,
                                items.get(i).getWhereToDraw().bottom - items.get(i).getWhereToDraw().top);
                        canvas.drawBitmap(items.get(i).getStillImg(),items.get(i).getFrameToDraw(),whereToDraw,paint);

                    }
                }
            }
            //draw ammo that is left from deleted blocks
            for(int i = 0; i < ammo.size();i++){
                if(ammo.get(i).getVisible()){
                    if(!ammo.get(i).isClipped()){
                        RectF whereToDraw = vp.worldToScreen(ammo.get(i).getWhereToDraw().left,
                                ammo.get(i).getWhereToDraw().top,
                                ammo.get(i).getWhereToDraw().right - ammo.get(i).getWhereToDraw().left,
                                ammo.get(i).getWhereToDraw().bottom - ammo.get(i).getWhereToDraw().top);
                        canvas.drawBitmap(ammo.get(i).getStillImg(),ammo.get(i).getFrameToDraw(),whereToDraw,paint);
                    }
                }
            }
            //draw terminal if visible and not clipped
            if(terminal.getVisible()){
                if(!terminal.isClipped()) {
                    RectF whereToDraw = vp.worldToScreen(terminal.getWhereToDraw().left,
                            terminal.getWhereToDraw().top,
                            terminal.getWhereToDraw().right - terminal.getWhereToDraw().left,
                            terminal.getWhereToDraw().bottom - terminal.getWhereToDraw().top);
                    canvas.drawBitmap(terminal.getStillImg(), terminal.getFrameToDraw(), whereToDraw, paint);
                }

            }

            //if bullets have been fired, draw them
            if(!bullets.isEmpty()) {
                paint.setColor(Color.argb(255, 255, 255, 57));
                for (int i = 0; i < bullets.size(); i++) {

                    RectF whereToDraw = vp.worldToScreen(bullets.get(i).getBullet().left,
                            bullets.get(i).getBullet().top,
                            bullets.get(i).getBullet().right - bullets.get(i).getBullet().left,
                            bullets.get(i).getBullet().bottom - bullets.get(i).getBullet().top);
                    canvas.drawRect(whereToDraw, paint);

                }
            }

            //if grenades have been thrown, draw them
            if(!nadeList.isEmpty()) {
                for (int i = 0; i < nadeList.size(); i++) {
                    RectF nadeRectF = vp.worldToScreen(nadeList.get(i).getNadeWhereToDraw().left,
                            nadeList.get(i).getNadeWhereToDraw().top,
                            nadeList.get(i).getNadeWhereToDraw().right - nadeList.get(i).getNadeWhereToDraw().left,
                            nadeList.get(i).getNadeWhereToDraw().bottom - nadeList.get(i).getNadeWhereToDraw().top);


                    canvas.drawBitmap(nadeList.get(i).getNadeImg(), nadeList.get(i).getNadeFrameToDraw(), nadeRectF, paint);
                }
            }
            //if a grenade is exploding, draw it
            if(!explodeList.isEmpty()){
                for (int i = 0; i < explodeList.size(); i++){
                    RectF whereToDraw = vp.worldToScreen(explodeList.get(i).getExplodeWhereToDraw().left,
                            explodeList.get(i).getExplodeWhereToDraw().top,
                            explodeList.get(i).getExplodeWhereToDraw().right - explodeList.get(i).getExplodeWhereToDraw().left,
                            explodeList.get(i).getExplodeWhereToDraw().bottom - explodeList.get(i).getExplodeWhereToDraw().top);

                    canvas.drawBitmap(explodeList.get(i).getExplodeImg(), explodeList.get(i).getExplodeFrameToDraw(), whereToDraw, paint);

                }
            }

            //if life gain is being used, animate and draw it
            if(usingGain.isUsing()){
                usingGain.animate();
                RectF whereToDraw = vp.worldToScreen(usingGain.getWhereToDraw().left,
                        usingGain.getWhereToDraw().top,
                        usingGain.getWhereToDraw().right - usingGain.getWhereToDraw().left,
                        usingGain.getWhereToDraw().bottom - usingGain.getWhereToDraw().top);
                canvas.drawBitmap(usingGain.getActiveImg(), usingGain.getFrameToDraw(), whereToDraw,paint);
            }

            //if shield is being used, animate and draw it
            if(usingShield.isUsing()){
                usingShield.animate();
                RectF whereToDraw = vp.worldToScreen(usingShield.getWhereToDraw().left,
                        usingShield.getWhereToDraw().top,
                        usingShield.getWhereToDraw().right - usingShield.getWhereToDraw().left,
                        usingShield.getWhereToDraw().bottom - usingShield.getWhereToDraw().top);
                canvas.drawBitmap(usingShield.getActiveImg(),usingShield.getFrameToDraw(),whereToDraw,paint);
            }


            //items button
            canvas.drawBitmap(itemsMenuButton,itemRect,itemButton,paint);

            //draw life points
            paint.setTextSize(tileWidth/8);
            paint.setColor(Color.argb(255,255,255,255));

            paint.setTextSize(tileWidth/6);
            String formattedString = encryptList.toString()
                    .replace(",", "")  //remove the commas
                    .replace("[", "")  //remove the right bracket
                    .replace("]", "")  //remove the left bracket
                    .trim();
            canvas.drawText(""+formattedString, (tileWidth*2) + (tileWidth/10), screenY - (tileHeight/2),paint);

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

            //if the item button has been clicked, draw the items in the inventory
            if(isItemMenu) {
                paint.setColor(Color.argb(255, 199, 198, 255));
                canvas.drawRect(itemBox, paint);
                paint.setTextSize(tileWidth/8);
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
            paint.setTextSize(tileWidth/6);
            if(numAmmo > 0){
                canvas.drawText("" + numAmmo, fireButton.left + ((tileWidth/6) + (tileWidth/16)),fireButton.top + ((tileWidth/4) + (tileHeight/7)),paint);
            }

            //if the priveledge escalation script has been found, animate and draw it
            if(escalate.getVisible()){
                if(!escalate.isClipped()){
                    RectF whereToDraw = vp.worldToScreen(escalate.getWhereToDraw().left,
                            escalate.getWhereToDraw().top,
                            escalate.getWhereToDraw().right - escalate.getWhereToDraw().left,
                            escalate.getWhereToDraw().bottom - escalate.getWhereToDraw().top);

                    escalate.animate();

                    canvas.drawBitmap(escalate.getActiveImg(),escalate.getFrameToDraw(),whereToDraw,paint);
                }
            }

            //if player has been decrypted or ran out of munitions, animate and draw game over banner
            if(displayGameOver){
                animateGameOver();
                RectF convertedWhereToDraw = vp.worldToScreen(gameOverWhereToDraw.left,
                        gameOverWhereToDraw.top,
                        gameOverWhereToDraw.right - gameOverWhereToDraw.left,
                        gameOverWhereToDraw.bottom - gameOverWhereToDraw.top);

                canvas.drawBitmap(gameOver,gameOverFrameToDraw,convertedWhereToDraw,paint);
            }

            ourHolder.unlockCanvasAndPost(canvas);

        }


    }

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

                if(isItemMenu){
                    //if there aren't any items, the item menu can be closed by clicking on it
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
                //if the item button is clicked, display item menu
                if(!isItemMenu) {
                    if (motionEvent.getX() > itemButton.left && motionEvent.getX() < itemButton.right
                            && motionEvent.getY() > itemButton.top && motionEvent.getY() < itemButton.bottom) {
                        isItemMenu = true;
                    }
                }

                break;

            case MotionEvent.ACTION_UP:
                //if no input, no actions
                player.setMovingLeft(false);
                player.setMovingRight(false);
                player.setJumping(false);
                isFiring = false;
                isJumping = false;


                break;
        }

        return true;
    }


    //used for randoms within Level
    public int makeRandInt(int min, int max){
        Random random = new Random();

        int randomNumber = random.nextInt((max - min) + 1) + min;
        return randomNumber;
    }
    //used for ViewPort, based center of player
    public PointF getCenter(){
        PointF center =  new PointF();
        center.x = (player.getWhereToDraw().left + (tileWidth/2));
        center.y = (player.getWhereToDraw().top - (tileHeight));
        return center;
    }

    //create Bullet object, send it in it's direction based on which way the player is facing
    public void shoot(){
        float yPos = (player.getWhereToDraw().top + tileWidth/3);
        switch (player.getFacing()) {
            case "left":
                Bullet leftBullet = new Bullet("left",player.getWhereToDraw().left,yPos,tileWidth,true,tileWidth/4);
                bullets.add(leftBullet);
                break;
            case "right":

                Bullet rightBullet = new Bullet("right", player.getWhereToDraw().right, yPos, tileWidth,true,tileWidth/4);
                bullets.add(rightBullet);
                break;
        }

    }

    //create Grenade object, send it in it's direction based on the player's direction
    public void throwGrenade(){
        switch (player.getFacing()) {
            case "left":
                Grenade leftNade = new Grenade(context,"left",player.getWhereToDraw().left,
                        (player.getWhereToDraw().top),tileWidth/4,tileHeight/4,System.currentTimeMillis(),
                        true,tileWidth/4,tileWidth,tileHeight);
                nadeList.add(leftNade);
                break;
            case "right":
                Grenade rightNade = new Grenade(context,"right",player.getWhereToDraw().right - (tileWidth/4),
                        (player.getWhereToDraw().top), tileWidth/4, tileHeight/4, System.currentTimeMillis(),
                        true,tileWidth/4,tileWidth,tileHeight);
                nadeList.add(rightNade);
                break;
        }

    }


    //beast of a method
    //this method is for primarily detecting blocks so if he is flying upward and hits one he is can't go through it
    //also so that he can land on it.
    //Basically this method draws multiple rectangles around the player
    //when the invisible rectangle intersects with the block it triggers the logic
    //the players speed (which will be input via setter methods) will be changed to the distance between the block and the side of the
    //detectBlock rectangle which touches the player
    //this will limit the speed incrementally until the speed reaches 0, stopping the players movement in that direction

    //this is the adjusted speed until the gap is closed between the collision, this is what will be input via setter methods
    float speedTil;
    float tilBlock;
    float jumpTil;
    float jSpeedTil;
    //trigger if the detectBlock rectangle has collided with object
    boolean limited;
    boolean fallLimited;
    boolean archLimited;
    boolean jumpLimited;
    public void detectPlayerCollision() {

        limited = false;
        fallLimited = false;
        archLimited = false;
        jumpLimited = false;

        float midX = ((player.getWhereToDraw().right - player.getWhereToDraw().left)/2)+ player.getWhereToDraw().left;
        float midY = ((player.getWhereToDraw().bottom - player.getWhereToDraw().top)/2) + player.getWhereToDraw().top;
        float tweak = ((player.getWhereToDraw().bottom - player.getWhereToDraw().top)/2)/20;

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

        for (int i = 0; i < blocks.size(); i++) {

            if (!blocks.get(i).isClipped()) {

                //see if the player intersects with a block, if so, trigger the corresponding flag, set corresponding speed
                switch (player.getFacing()) {
                    case "right":
                        if (RectF.intersects(dbR, blocks.get(i).getWhereToDraw()) && !player.getJumping()) {
                            speedTil = blocks.get(i).getWhereToDraw().left - dbR.left;
                            limited = true;

                        }
                        if (RectF.intersects(dbUR, blocks.get(i).getWhereToDraw()) && player.getJumping()){
                            jSpeedTil = dbUR.bottom - blocks.get(i).getWhereToDraw().bottom;
                            jumpLimited = true;

                        }
                        if(RectF.intersects(dbDR, blocks.get(i).getWhereToDraw()) && player.isFalling() && !player.getJumping()) {
                            tilBlock = blocks.get(i).getWhereToDraw().top - dbDR.top;
                            fallLimited = true;


                        }
                        if(RectF.intersects(dbJR, blocks.get(i).getWhereToDraw()) && player.getJumping()){
                            jumpTil = blocks.get(i).getWhereToDraw().left - dbJR.left;
                            archLimited = true;
                        }
                        break;

                    case "left":
                        if(RectF.intersects(dbL, blocks.get(i).getWhereToDraw()) && !player.getJumping()){
                            speedTil = dbL.right - blocks.get(i).getWhereToDraw().right;
                            limited = true;

                        }
                        if(RectF.intersects(dbUL, blocks.get(i).getWhereToDraw()) && player.getJumping()){
                            jSpeedTil = dbUL.bottom - blocks.get(i).getWhereToDraw().bottom;
                            jumpLimited = true;

                        }
                        if(RectF.intersects(dbDL,blocks.get(i).getWhereToDraw()) && player.isFalling() && !player.getJumping()){
                            tilBlock = blocks.get(i).getWhereToDraw().top - dbDL.top;
                            fallLimited = true;

                        }
                        if(RectF.intersects(dbJL,blocks.get(i).getWhereToDraw()) && player.getJumping()){
                            jumpTil = dbJL.right - blocks.get(i).getWhereToDraw().right;
                            archLimited = true;
                        }
                        break;
                }

            }
        }

        //see if player collides with the World limits, if so trigger the corresponding flag, set corresponding speed
        switch (player.getFacing()){
            case "right":
                if((dbR.right > worldLength)){
                    speedTil = worldLength - dbR.left;
                    limited = true;
                }
                if((dbJR.right > worldLength) && player.getJumping()){
                    jumpTil = worldLength - dbJR.left;
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

        //per flag, set speed to either the speed til collision (limited speed) or set to regular speed
        if(limited){
            player.setSpeed(speedTil);
        }else{
            player.setSpeed(playerSpeed);
        }
        if(fallLimited){
            player.setFallSpeed(tilBlock);
        }else{
            player.setFallSpeed(fallSpeed);
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

    //some World Mandated methods
    public boolean isAlive(){
        return alive;
    }


    public boolean isNxtLevel(){ return nextLevel; }

    public int getPartOfEscalate(){return partOfEscalate;}


    public boolean isSecretLevel(){return secretLevel;}

    public int getNumNades(){return numNades;}
    public int getNumGains(){return numGains;}
    public int getNumShields(){return numShields;}

    //update game over banner for drawing
    public void updateGameOver(){
        gameOverWhereToDraw.left = getCenter().x;
        gameOverWhereToDraw.top = getCenter().y;
        gameOverWhereToDraw.right = getCenter().x + gameOverWidth;
        gameOverWhereToDraw.bottom = getCenter().y + gameOverHeight;
    }

    //animate game over banner
    public void animateGameOver(){
        long time = System.currentTimeMillis();
        if (time > gameOverLastFrameChangeTime + gameOverFrameLengthInMilliseconds) {
            gameOverLastFrameChangeTime = time;
            gameOverCurrentFrame++;
            //once the banner is fully animated, stop mediaPlayer and set alive flag to false, this will restart game in GameLoop
            if (gameOverCurrentFrame >= gameOverFrameCount) {
                mediaPlayer.setLooping(false);
                mediaPlayer.stop();
                mediaPlayer.release();
                alive = false;
            }


        }
        gameOverFrameToDraw.left = gameOverCurrentFrame * gameOverWidth;
        gameOverFrameToDraw.right = gameOverFrameToDraw.left + gameOverWidth;

    }

    //similar code as the above detectPlayerCollision
    float nadeSpeed;
    boolean nadeLimited;
    private void detectNadeCollision(){
        float nadeHeight = nadeList.get(0).getNadeHeight();
        float nadeWidth = nadeList.get(0).getNadeWidth();
        nadeLimited= false;

        for(int n = 0; n < nadeList.size(); n++) {
            RectF dB = new RectF(nadeList.get(n).getNadeWhereToDraw().left,nadeList.get(n).getNadeWhereToDraw().top,
                    nadeList.get(n).getNadeWhereToDraw().right,nadeList.get(n).getNadeWhereToDraw().bottom - ((nadeHeight/3)*2));

            RectF dBD = new RectF(nadeList.get(n).getNadeWhereToDraw().left,nadeList.get(n).getNadeWhereToDraw().bottom,
                    nadeList.get(n).getNadeWhereToDraw().right,nadeList.get(n).getNadeWhereToDraw().bottom + nadeList.get(n).getConsFallSpeed());
            for(int b = 0; b < blocks.size(); b++) {
                if (RectF.intersects(dB,blocks.get(b).getWhereToDraw())){
                    switch (nadeList.get(n).getHeading()){
                        case "left":
                            nadeList.get(n).setHeading("right");
                            break;
                        case "right":
                            nadeList.get(n).setHeading("left");
                            break;
                    }
                }
                if(RectF.intersects(dBD,blocks.get(b).getWhereToDraw())){
                    nadeSpeed =  blocks.get(b).getWhereToDraw().top - dBD.top;
                    nadeLimited = true;
                }
            }
            if(nadeLimited){
                nadeList.get(n).setFallSpeed(nadeSpeed);
            }else{
                nadeList.get(n).setFallSpeed(nadeList.get(n).getConsFallSpeed());
            }
        }
    }
    //World Mandated methods 
    public boolean isPlayingMusic(){return playingMusic;}
    public void pauseMusic(){mediaPlayer.pause();}

}
