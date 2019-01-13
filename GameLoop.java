package com.terminalagent.terminalagent;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

/**
 * Created by gatovato 2018.
 */

//Where all the code comes together
//This is where the game loop is (if you didn't know)
//Each method implemented from the World interface is handled here
//Each World class implementation is created and clobbered as the gameWorld object

public class GameLoop extends SurfaceView implements Runnable {


	private Context context;

	private SurfaceHolder ourHolder;

	//what will contain each view of the game, Scenes, Levels and Bossfights
	private World gameWorld;

	public static  float tileWidth;

	public static float tileHeight;

	//used to keep track of which view is supposed to be shown, this is also passed in initialization to all classes for
	//use for tileMap and songs
	private int numWorld;

	//keep in the loop
	private volatile boolean playing;

	//screen's width
	private float screenX;
	//screen's height
	private float screenY;

	private Thread gameThread = null;

	//used to determine if all the priveledge escalate scripts have been found for secret ending
	private int escalate;

	private int numNades;
	private int numGains;
	private int numShields;


	private long lastFrameTime;

	//used to keep track of initial sound setting, as well as any subsequent setting set through the Terminal class
	private boolean playingMusic;


	public GameLoop(Context context, int x, int y) {


		super(context);

		this.context = context;

		ourHolder = getHolder();

		screenX = x;
		screenY = y;

		tileWidth = screenX / 20;
		tileHeight = screenY / 5;

		playingMusic = true;

	}





@Override
	public void run(){
	//start with intro scene
	if(numWorld == 0){

		Scene intro = new Scene("intro","startbutton", screenX, screenY,context,ourHolder,(tileWidth*7),(tileHeight*3),(tileWidth*2),(tileHeight),
				(screenX / 2) - tileWidth*4, 0,(screenX / 3), (screenY - (screenY/3)),
				0,0,0,0,false,0,numWorld,playingMusic);
		gameWorld=intro;

	}
		//this is what is happening to create the game itself
		//this corresponds to all World mandated methods found within each World implemented class
		while (playing) {


			gameWorld.update();
			gameWorld.draw();
			if (gameWorld.isNxtLevel()) {

				numNades = gameWorld.getNumNades();
				numGains = gameWorld.getNumGains();
				numShields = gameWorld.getNumShields();

				//26 is the last level, after 26 is reached  restart the game
				if(numWorld < 26) {
					numWorld++;
					setLevel(numWorld);
				}else{
					if(numWorld == 26) {
						numWorld = 0;
						setLevel(numWorld);
					}
					if(numWorld == 27){
						numWorld = 0;
						setLevel(numWorld);
					}
				}
			}
			//if player dies, restart to intro scene
			if (!gameWorld.isAlive()) {
				numWorld = 0;
				setLevel(numWorld);
			}
			//used to not produce additional priveledge escalate scripts after 3 have been gathered
			if(escalate <= 3) {
				escalate = gameWorld.getPartOfEscalate();
			}
			//if right sequence / commands has been run after gaining priveledge escalation within terminal (after getting 3 scripts)
			//set to secret level
			if(gameWorld.isSecretLevel()){
				numWorld = 27;
				setLevel(numWorld);
			}
			//Because this loop and all recursive loops within this loop create the game, faster devices will inevitablely run this
			//and subsequent loops quicker.  This controls the rate of this loop to make the game not run too fast on quicker devices
			controlFrameRate();

			//is the game playing music?
			playingMusic = gameWorld.isPlayingMusic();


		}
	}

	//As you can see above, if it's time for the next level to be displayed this method is called. This will clobber the
	//gameWorld variable with a new implementer of World
	//this will call all the above methods to be called on this new class, hense changing Scenes
	void setLevel(int numWorld){

		if(numWorld == 0){
			Scene intro = new Scene("intro","startbutton", screenX, screenY,context,ourHolder,(tileWidth*5),
					(tileHeight*2),(tileWidth*2),(tileHeight),(screenX / 3), (screenY / 5),(screenX / 3), (screenY - (screenY/3)),
					0,0,0,0,false,0,numWorld,playingMusic);
			gameWorld=intro;

		}

		if(numWorld == 1){

			Scene mission = new Scene("professor","mission",screenX, screenY,context,ourHolder,tileWidth*3,
					tileHeight,tileWidth*8,tileHeight + (tileHeight/2),(screenX / 5), (screenY / 4),((screenX / 4) + (tileWidth*3)),
					(screenY/4),0,0,0,0,false,0,numWorld,playingMusic);
			gameWorld = mission;

		}

		//If you explore the various inputs in the level / bossfight constructor, you can see how the gameplay can be manipulated
		//The levels / bossfights are generated at random. A few key things to point out:
		//The enemyList will be used within Level and Bossfight to generate random enemies from this list.
		//the tileW and tileH directly correspond to the amount of tiles within the corresponding tilemap class
		if(numWorld == 2){

			int worldWidth = Math.round(screenX * 3);
			int worldHeight = Math.round(screenY - (screenY/4));
			float tileW = worldWidth / 20;
			float tileH = screenY/ 5;
			String enemyList[] = new String[1];
			enemyList[0] = "pwnwlkr";
			Level level1 = new Level(context,ourHolder,screenX,screenY,1,worldWidth,worldHeight,enemyList,10,
					10,30,10,0,0,0,tileW,tileH,0, numWorld, playingMusic);
			gameWorld = level1;
		}
		if(numWorld == 3){

			Terminal terminal = new Terminal(context,ourHolder,screenX,screenY,numNades,numGains,numShields,escalate,true,
			numWorld,playingMusic);
			gameWorld = terminal;
		}
		if(numWorld == 4){

			Scene payload = new Scene("smiley","payload",screenX,screenY,context,ourHolder,(tileWidth*7),(tileHeight*3),
					tileWidth*5,	tileHeight,(screenX / 2) - tileWidth*4, 0,(screenX / 2) - ((tileWidth*2)+(tileWidth/2)),
					((tileHeight*2)+(screenY/7)),escalate,numNades,	numGains,numShields,true,3,numWorld,playingMusic);
			gameWorld = payload;
		}
		if(numWorld == 5){

			Scene deepBriefing = new Scene("professor","aflev1",screenX, screenY,context,ourHolder,tileWidth*3,
					tileHeight,tileWidth*8,tileHeight + (tileHeight/2),(screenX / 5), (screenY / 4),((screenX / 4) + (tileWidth*3)),
					(screenY/4),escalate,numNades,numGains,numShields,false,0,numWorld,playingMusic);
			gameWorld = deepBriefing;
		}
		if(numWorld == 6){
			int worldWidth = Math.round(screenX * 3);
			int worldHeight = Math.round(screenY - (screenY/4));
			float tileW = worldWidth / 20;
			float tileH = screenY/ 5;
			String enemyList[] = new String[2];
			enemyList[0] = "pwnwlkr";
			enemyList[1] = "flyguy";
			Level level2 = new Level(context,ourHolder,screenX,screenY,2,worldWidth,worldHeight,enemyList,10,
					10,30,10,numNades,numShields,numGains,tileW,tileH,escalate,numWorld,playingMusic);
			gameWorld = level2;
		}
		if(numWorld == 7){
			Terminal terminal = new Terminal(context,ourHolder,screenX,screenY,numNades,numGains,numShields,escalate,false,numWorld,
					playingMusic);
			gameWorld = terminal;
		}
		if(numWorld == 8){
			Scene payload = new Scene("smiley","payload",screenX,screenY,context,ourHolder,(tileWidth*7),(tileHeight*3),
					tileWidth*5,	tileHeight,(screenX / 2) - tileWidth*4, 0,(screenX / 2) - ((tileWidth*2)+(tileWidth/2)),
					((tileHeight*2)+(screenY/7)),escalate,numNades,	numGains,numShields,true,3,numWorld,playingMusic);
			gameWorld = payload;
		}
		if(numWorld == 9){
			float tileW = screenX / 12;
			float tileH = screenY / 4;

			String enemyList[] = new String[1];
			enemyList[0] = "pwnwlkr";
			BossFight bossfight = new BossFight(context,ourHolder,screenX,screenY,3, enemyList,10,numNades,
					numShields,numGains,tileW,tileH,escalate,3,4,15,numWorld,playingMusic);

			gameWorld = bossfight;
		}
		if(numWorld == 10){
			Scene helloZardo = new Scene("zardodialouge","zardodialouge1",screenX, screenY,context,ourHolder,
					(tileWidth*4)+(tileWidth/2),tileHeight*2,tileWidth*8,tileHeight + (tileHeight/2)
					,(screenX / 8), (screenY / 4),((screenX / 4) + (tileWidth*3)), (screenY/4),escalate,numNades,numGains,numShields,
					false,0,numWorld,playingMusic);
			gameWorld = helloZardo;
		}
		if(numWorld == 11){
			int worldWidth = Math.round(screenX * 3);
			int worldHeight = Math.round(screenY - (screenY/4));
			float tileW = worldWidth / 20;
			float tileH = screenY/ 5;
			String enemyList[] = new String[3];
			enemyList[0] = "pwnwlkr";
			enemyList[1] = "flyguy";
			enemyList[2] = "trojan";
			Level level3 = new Level(context,ourHolder,screenX,screenY,4,worldWidth,worldHeight,enemyList,12,
					12,30,12,	numNades,numShields,numGains,tileW,tileH,escalate,numWorld,playingMusic);
			gameWorld = level3;
		}
		if(numWorld == 12){
			Terminal terminal = new Terminal(context,ourHolder,screenX,screenY,numNades,numGains,numShields,escalate,false,
					numWorld,playingMusic);
			gameWorld = terminal;
		}
		if(numWorld == 13){
			Scene payload = new Scene("smiley","payload",screenX,screenY,context,ourHolder,(tileWidth*7),(tileHeight*3),
					tileWidth*5,	tileHeight,(screenX / 2) - tileWidth*4, 0,(screenX / 2) - ((tileWidth*2)+(tileWidth/2)),
					((tileHeight*2)+(screenY/7)),escalate,numNades,	numGains,numShields,true,3,numWorld,playingMusic);
			gameWorld = payload;
		}
		if(numWorld == 14){
			int worldWidth = Math.round(screenX * 3);
			int worldHeight = Math.round(screenY - (screenY/4));
			float tileW = worldWidth / 20;
			float tileH = screenY/ 5;
			String enemyList[] = new String[3];
			enemyList[0] = "pwnwlkr";
			enemyList[1] = "flyguy";
			enemyList[2] = "trojan";
			Level level4 = new Level(context,ourHolder,screenX,screenY,5,worldWidth,worldHeight,enemyList,15,
					12,30,15,	numNades,numShields,numGains,tileW,tileH,escalate,numWorld,playingMusic);
			gameWorld = level4;

		}
		if(numWorld == 15){
			Terminal terminal = new Terminal(context,ourHolder,screenX,screenY,numNades,numGains,numShields,escalate,false,
					numWorld,playingMusic);
			gameWorld = terminal;
		}
		if(numWorld == 16){
			Scene payload = new Scene("smiley","payload",screenX,screenY,context,ourHolder,(tileWidth*7),(tileHeight*3),
					tileWidth*5,	tileHeight,(screenX / 2) - tileWidth*4, 0,(screenX / 2) - ((tileWidth*2)+(tileWidth/2)),
					((tileHeight*2)+(screenY/7)),escalate,numNades,	numGains,numShields,true,3,numWorld,playingMusic);
			gameWorld = payload;
		}
		if(numWorld == 17){
			float tileW = screenX / 12;
			float tileH = screenY / 4;

			String enemyList[] = new String[3];
			enemyList[0] = "pwnwlkr";
			enemyList[1] = "flyguy";
			enemyList[2] = "trojan";
			BossFight bossfight = new BossFight(context,ourHolder,screenX,screenY,6, enemyList,10,numNades,
					numShields,numGains,tileW,tileH,escalate,5,6,15,numWorld,playingMusic);

			gameWorld = bossfight;

		}
		if(numWorld == 18){
			Scene hiZardo = new Scene("zardodialouge","zardodialouge2",screenX, screenY,context,ourHolder,
					(tileWidth*4)+(tileWidth/2),tileHeight*2,tileWidth*8,tileHeight + (tileHeight/2)
					,(screenX / 8), (screenY / 4),((screenX / 4) + (tileWidth*3)), (screenY/4),escalate,numNades,numGains,numShields,
					false,0,numWorld,playingMusic);
			gameWorld = hiZardo;
		}
		if(numWorld == 19){
			int worldWidth = Math.round(screenX * 3);
			int worldHeight = Math.round(screenY - (screenY/4));
			float tileW = worldWidth / 20;
			float tileH = screenY/ 5;
			String enemyList[] = new String[3];
			enemyList[0] = "pwnwlkr";
			enemyList[1] = "trojan";
			enemyList[2] = "worm";
			Level level5 = new Level(context,ourHolder,screenX,screenY,7,worldWidth,worldHeight,enemyList,12,
					10,30,5,	numNades,numShields,numGains,tileW,tileH,escalate,numWorld,playingMusic);
			gameWorld = level5;
		}
		if(numWorld == 20){
			Terminal terminal = new Terminal(context,ourHolder,screenX,screenY,numNades,numGains,numShields,escalate,false,
					numWorld,playingMusic);
			gameWorld = terminal;
		}
		if(numWorld == 21){
			Scene payload = new Scene("smiley","payload",screenX,screenY,context,ourHolder,(tileWidth*7),(tileHeight*3),
					tileWidth*5,	tileHeight,(screenX / 2) - tileWidth*4, 0,(screenX / 2) - ((tileWidth*2)+(tileWidth/2)),
					((tileHeight*2)+(screenY/7)),escalate,numNades,	numGains,numShields,true,3,numWorld,playingMusic);
			gameWorld = payload;
		}
		if(numWorld == 22){
			int worldWidth = Math.round(screenX * 3);
			int worldHeight = Math.round(screenY - (screenY/4));
			float tileW = worldWidth / 20;
			float tileH = screenY/ 5;
			String enemyList[] = new String[3];
			enemyList[0] = "pwnwlkr";
			enemyList[1] = "trojan";
			enemyList[2] = "worm";
			Level level6 = new Level(context,ourHolder,screenX,screenY,8,worldWidth,worldHeight,enemyList,12,
					10,30,5,	numNades,numShields,numGains,tileW,tileH,escalate,numWorld,playingMusic);
			gameWorld = level6;
		}
		if(numWorld == 23){
			Terminal terminal = new Terminal(context,ourHolder,screenX,screenY,numNades,numGains,numShields,escalate,false,
					numWorld,playingMusic);
			gameWorld = terminal;
		}
		if(numWorld == 24){
			Scene payload = new Scene("smiley","payload",screenX,screenY,context,ourHolder,(tileWidth*7),(tileHeight*3),
					tileWidth*5,	tileHeight,(screenX / 2) - tileWidth*4, 0,(screenX / 2) - ((tileWidth*2)+(tileWidth/2)),
					((tileHeight*2)+(screenY/7)),escalate,numNades,	numGains,numShields,true,3,numWorld,playingMusic);
			gameWorld = payload;
		}
		if(numWorld == 25){
			float tileW = screenX / 12;
			float tileH = screenY / 4;

			String enemyList[] = new String[4];
			enemyList[0] = "pwnwlkr";
			enemyList[1] = "flyguy";
			enemyList[2] = "trojan";
			enemyList[3] = "worm";
			BossFight bossfight = new BossFight(context,ourHolder,screenX,screenY,9, enemyList,10,numNades,
					numShields,numGains,tileW,tileH,escalate,7,8,15,numWorld,playingMusic);

			gameWorld = bossfight;

		}
		if(numWorld == 26){

		float tileW = screenX / 12;
		float tileH = screenY / 4;

		EndScene end1 = new EndScene(context,ourHolder,screenX,screenY,1,tileW,tileH,numWorld,playingMusic);
		gameWorld = end1;

		}
		if(numWorld == 27){
			float tileW = screenX / 10;
			float tileH = screenY / 5;

			EndScene end2 = new EndScene(context,ourHolder,screenX,screenY,2,tileW,tileH,numWorld,playingMusic);
			gameWorld = end2;

		}

	}

		//overide the android system MotionEvent, this will be utilized by implenters of World
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:



				gameWorld.onTouchEvent(motionEvent);




                break;


            case MotionEvent.ACTION_UP:



				gameWorld.onTouchEvent(motionEvent);



                break;
        }

        return true;
    }


		//the music has to be killed in a particular way, it's important to also note the pauseMusic() code in World implenters classes
    public  void pause() {
		gameWorld.pauseMusic();
		playing = false;
		try {
			gameThread.join();

		} catch (InterruptedException e) {
			Log.e("Error:", "joining thread");
		}
	}


	public void resume() {

		playing = true;
		gameThread = new Thread(this);
		gameThread.start();
	}

	//this will cap it at 60 frames per second, this can be adjusted based on the integer that timeThisFrame is being subtraced from
	//in the 2nd line of this method
	public void controlFrameRate(){
		long timeThisFrame = (System.currentTimeMillis() - lastFrameTime);
		long timeToSleep = 60 - timeThisFrame;
		if(timeToSleep > 0){
			try {
				gameThread.sleep(timeToSleep);
			}catch (InterruptedException e){
			}
		}
		lastFrameTime = System.currentTimeMillis();

	}

}
