package com.terminalagent.terminalagent;

import android.view.MotionEvent;

/**
 * Created by gatovato on 9/17/2017.
 */

//Establish all things that implement it have these methods, used in GameLoop to switch between Level/Scene/Terminal/Bossfight
public interface World {

    void update();
    void draw();

    boolean onTouchEvent(MotionEvent motionEvent);

    boolean isAlive();
    boolean isNxtLevel();
    int getPartOfEscalate();
    boolean isSecretLevel();
    int getNumNades();
    int getNumShields();
    int getNumGains();
    boolean isPlayingMusic();
    void pauseMusic();



}
