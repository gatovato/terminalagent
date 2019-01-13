package com.terminalagent.terminalagent;


import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;

import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;


//need to create GameLoop object, this is what will be set as content view. 
public class MainActivity extends AppCompatActivity {


GameLoop game;

@Override
        protected void onCreate(Bundle savedInstanceState) {


		    super.onCreate(savedInstanceState);

		    Display display = getWindowManager().getDefaultDisplay();

		    Point size = new Point();

		    display.getSize(size);

		    game = new GameLoop(this, size.x, size.y);

            setContentView(game);


        }


@Override
        protected void onResume(){
            super.onResume();

            game.resume();
        }

@Override
        protected void onPause(){
            super.onPause();

            game.pause();
        }

}
