package com.terminalagent.terminalagent;

import android.graphics.RectF;

/**
 * Created by gatovato on 12/7/2017.
 */

//Key class is used when making the keyboard in the Terminal class. This is for determining which string belongs to which
//rectangle within the keyboard array
public class Key {

    private String character;
    private float x;
    private float y;
    private float width;
    private float height;
    private RectF whereToDraw;

    public Key(String character, float x, float y, float length){
        this.character = character;
        this.x = x;
        this.y = y;
        this.width = length;
        this.height = length/2;
        whereToDraw = new RectF(x,y,x+width,y+height);
    }

    public String getCharacter(){return character;}
    public RectF getWhereToDraw(){return whereToDraw;}
}
