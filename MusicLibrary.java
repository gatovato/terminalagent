package com.terminalagent.terminalagent;


import android.content.Context;

//this is used as a convenient way to store get the resourceID in R.raw so it wouldn't have to be done within the World
public class MusicLibrary {

    private int resourceId;


    public MusicLibrary(Context context,int numScene){

        switch (numScene){
            case 0:
                resourceId = context.getResources().getIdentifier("mistyflute","raw",context.getPackageName());
                break;
            case 1:
                resourceId = context.getResources().getIdentifier("profcomm","raw",context.getPackageName());
                break;
            case 2:
                resourceId = context.getResources().getIdentifier("lamabeet","raw",context.getPackageName());
                break;
            case 3:
                resourceId = context.getResources().getIdentifier("ninetyelectro","raw",context.getPackageName());
                break;
            case 4:
                resourceId = context.getResources().getIdentifier("cheerpwn","raw",context.getPackageName());
                break;
            case 5:
                resourceId = context.getResources().getIdentifier("profcomm","raw",context.getPackageName());
                break;
            case 6:
                resourceId = context.getResources().getIdentifier("lamabeet","raw",context.getPackageName());
                break;
            case 7:
                resourceId = context.getResources().getIdentifier("ninetyelectro","raw",context.getPackageName());
                break;
            case 8:
                resourceId = context.getResources().getIdentifier("cheerpwn","raw",context.getPackageName());
                break;
            case 9:
                resourceId = context.getResources().getIdentifier("fonky","raw",context.getPackageName());
                break;
            case 10:
                resourceId = context.getResources().getIdentifier("zardcomm","raw",context.getPackageName());
                break;
            case 11:
                resourceId = context.getResources().getIdentifier("dreamz","raw",context.getPackageName());
                break;
            case 12:
                resourceId = context.getResources().getIdentifier("ninetyelectro","raw",context.getPackageName());
                break;
            case 13:
                resourceId = context.getResources().getIdentifier("cheerpwn","raw",context.getPackageName());
                break;
            case 14:
                resourceId = context.getResources().getIdentifier("dreamz","raw",context.getPackageName());
                break;
            case 15:
                resourceId = context.getResources().getIdentifier("ninetyelectro","raw",context.getPackageName());
                break;
            case 16:
                resourceId = context.getResources().getIdentifier("cheerpwn","raw",context.getPackageName());
                break;
            case 17:
                resourceId = context.getResources().getIdentifier("allbeets","raw",context.getPackageName());
                break;
            case 18:
                resourceId = context.getResources().getIdentifier("zardcomm","raw",context.getPackageName());
                break;
            case 19:
                resourceId = context.getResources().getIdentifier("progress","raw",context.getPackageName());
                break;
            case 20:
                resourceId = context.getResources().getIdentifier("ninetyelectro","raw",context.getPackageName());
                break;
            case 21:
                resourceId = context.getResources().getIdentifier("cheerpwn","raw",context.getPackageName());
                break;
            case 22:
                resourceId = context.getResources().getIdentifier("progress","raw",context.getPackageName());
                break;
            case 23:
                resourceId = context.getResources().getIdentifier("ninetyelectro","raw",context.getPackageName());
                break;
            case 24:
                resourceId = context.getResources().getIdentifier("cheerpwn","raw",context.getPackageName());
                break;
            case 25:
                resourceId = context.getResources().getIdentifier("ninetyelectro","raw",context.getPackageName());
                break;
            case 26:
                resourceId = context.getResources().getIdentifier("mistyflute","raw",context.getPackageName());
                break;
            case 27:
                resourceId = context.getResources().getIdentifier("mistyflute","raw",context.getPackageName());
                break;
        }

    }
    public int getSong(){return resourceId;}
}
