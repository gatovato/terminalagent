package com.terminalagent.terminalagent;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by gatovato on 12/7/2017.
 */

 //Most of this class operates on a 'path' string which stores the emulated
 //directory path of the player within the terminal
 //Another important variable is the 'command' string which relays the command to
 //Get given outputs from emulated Linux commands
 //This level uses an emulated keyboard to build a string out of strings that are received
 //From the MotionEvent code per key in the keyboard, when the enter key is pressed the whole string is set as
 //command, which is then interpreted

public class Terminal implements World {

    private Context context;
    private SurfaceHolder ourHolder;
    private Canvas canvas;
    private Paint paint;
    private float screenX;
    private float screenY;
    private boolean nextLevel;

    private ArrayList<Key> keyz;
    //string values that will match the keyboard that is drawn, and input received in MotionEvent
    private String[][] input = {
            {"q","w","e","r","t","y","u","i","o","p"},
            {"a","s","d","f","g","h","j","k","l","enter"},
            {"z","x","c","v","b","n","m","@","enter","enter"},
            {"1","2","3","4","5","6","7","8","9","0"},
            {"#",".","/","-"," "," "," ","!","back","back"}
    };

    //whole keyboard image
    private Bitmap keyboard;


    //keyboards dimensions
    private int boardWidth;
    private int boardHeight;

    //used to interpret actions
    private String command;
    //build up to command
    private ArrayList<String> word;
    //position within file system
    private String path;
    //whats being drawn as the letters are being entered
    private String typingWord;

    //World mandated variables
    private int numNades;
    private int numGains;
    private int numShields;
    private int escalate;

    //for dimensions of key
    private float tile;

    //various flags that will be used to determine state within terminal
    private boolean accessDenied;
    private boolean alive;
    private boolean editingFile;
    private boolean uploadingFile;
    private boolean runningPwnDwn;
    private boolean runningBinary;

    private boolean isRoot;
    private boolean isEnter;

    //used for edit file utility in terminal
    private String editFilePath;

    //starting point of player entry
    private float cliX;
    private float cliY;

    //has the secret command been ran to get the secret level?
    private boolean secretLevel;

    //used for priveledge escalation script
    private long time;

    //used to restrict priveledge escalation to be only used once
    private int timeSwitch;

    //used for tutorial
    private long helpTime;
    private int helpTimeSwitch;

    private float boardYPos;

    //used for tutorial
    private boolean termOne;
    private long tutorialTime;
    private long tutorialIntroTime;
    private boolean runTutorial;
    private boolean tutorialIntro;

    //used to determine if player entered command matches a command in the goodCommand list, if not give error message
    private boolean syntax;
    //acceptable commands, any other commands will give error
    private String[] goodCommand = {"cd ..","cd /home/user","cd toolkits","cd /home/user/toolkits","./uploadfile.sh","rm -rf /","cd /","cd root",
            "cd /root","./editfile.sh","cd home","cd /home","cd user","cleanhaus","pwndwn4wut","quit","grenades","shields","lifegain","quit","ls",
            "ls toolkits","cat welcome.text","cat info.text","help","pwd","ls /","ls /root","ls /home","ls /home/user","ls /home/user/toolkits",
            "0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","yes","no","sound off","sound on",
            "cat sound.text"," "};

    private boolean playingMusic;
    private MusicLibrary song;
    private MediaPlayer mediaPlayer;
    private int numWorld;

    //drawn at the end of the tutorial
    private Bitmap asciismiles;
    private int smilesWidth;
    private int smilesHeight;
    private float smilesXPos;
    private float smilesYPos;


    public Terminal (Context context,SurfaceHolder ourHolder, float screenX, float screenY, int numNades, int numGains,
                     int numShields, int escalate,boolean termOne,int numWorld, boolean playingMusic){

        this.context = context;
        this.screenX = screenX;
        this.screenY = screenY;
        this.numNades = numNades;
        this.numGains = numGains;
        this.numShields = numShields;
        this.escalate = escalate;
        this.termOne = termOne;

        this.numWorld = numWorld;
        this.playingMusic = playingMusic;

        this.ourHolder = ourHolder;
        paint = new Paint();

        keyz = new ArrayList<>();
        word = new ArrayList<>();

        tile = screenX/20;

        //starting command
        command = " ";
        //starting path
        path = "/home/user";

        boardWidth = Math.round(tile*20);
        boardHeight = Math.round(tile*5);

        keyboard = BitmapFactory.decodeResource(context.getResources(), R.drawable.keyboard);
        keyboard = Bitmap.createScaledBitmap(keyboard, boardWidth,boardHeight,false);

        boardYPos = screenY - (boardHeight + tile);

        //match key width to amount of keys
        int keyWidth = boardWidth/10;

        //iterate through 2D input array to get string for virtual (invisible) keyboard
        //set the dimensions of the virtual keyboard
        for(int x = 0; x < input.length; x++){
            for(int y = 0; y < input[x].length; y++){

                float xPos = y * keyWidth;
                float yPos = boardYPos + (x * tile);

                Key key = new Key(input[x][y],xPos,yPos,tile*2);
                keyz.add(key);
            }


        }
        alive = true;
        nextLevel = false;
        accessDenied = false;
        isRoot = false;
        isEnter = false;
        uploadingFile = false;
        runningPwnDwn = false;
        editingFile = false;
        runningBinary = false;
        secretLevel = false;
        editFilePath = "start";
        time = 0;
        timeSwitch = 0;

        helpTimeSwitch = 0;

        cliX = (screenX/100);
        cliY = (tile);

        //determines if the tutorial welcome message needs to be displayed (first terminal only)
        //or if just the help message needs to be displayed
        if(termOne){
            tutorialIntroTime = System.currentTimeMillis();
            tutorialIntro = true;
        }else{
            helpTime = System.currentTimeMillis();
        }

        syntax = true;

        song = new MusicLibrary(context,numWorld);
        int rId = song.getSong();
        mediaPlayer = MediaPlayer.create(context,rId);
        mediaPlayer.setLooping(true);
        if(playingMusic) {
            mediaPlayer.start();
        }

        smilesWidth = Math.round(boardYPos - cliY);
        smilesHeight = Math.round(boardYPos - cliY);
        asciismiles = BitmapFactory.decodeResource(context.getResources(),R.drawable.asciismiley);
        asciismiles = Bitmap.createScaledBitmap(asciismiles,smilesWidth,smilesHeight,false);
        smilesXPos = tile * 7;
        smilesYPos = 0;

    }

    public void update(){
        //binaries are the scripts that can be run within the terminal, if they are running we want to restrict the input to
        //their functionality, and not have additional input be checked/apply
        //if your familar with Linux terminals this should be straight forward, for most of these we will be dealing with
        //editing the path to reflect the corresponding path value
        //I won't go into the path changing commands, but for more info check out their actual function in Unix systems
        if(!runningBinary) {
            switch (command) {
                case "cd ..":
                    switch (path) {
                        case "/home/user":
                            if(isRoot){
                                path = "/home";
                                command = " ";
                            }else {
                                accessDenied = true;
                            }
                            break;

                        case "/home":
                            if(isRoot){
                                path = "/";
                            }
                            command = " ";
                            break;

                        case "/home/user/toolkits":
                            path = "/home/user";
                            command = " ";
                            break;


                        case "/root":
                            if (isRoot) {
                                path = "/";
                            }
                            command = " ";
                            break;

                    }
                    break;

                case "cd /home/user":
                    path = "/home/user";
                    break;

                case "cd toolkits":
                    if (path == "/home/user") {
                        path = "/home/user/toolkits";
                    }
                    break;
                case "cd /home/user/toolkits":
                    path = "/home/user/toolkits";
                    break;


                case "./uploadfile.sh":
                    if (path == "/home/user/toolkits") {
                        uploadingFile = true;
                        runningBinary = true;
                    }
                    break;

                //command to initiate the secret level
                //this is a system kill command in Linux (will destroy system)
                //if the player has root access, which can be gained by running the priveledge escalation script
                //and they are in the / path, they can run this.
                //If so, stop the mediaPlayer and have GameLoop change to the secret level
                case "rm -rf /":
                    if (isRoot) {
                        if (path == "/") {
                            mediaPlayer.setLooping(false);
                            mediaPlayer.stop();
                            mediaPlayer.release();
                            secretLevel = true;
                        }
                    }
                    break;

                case "cd /":
                    if (isRoot) {
                        path = "/";
                    } else {
                        accessDenied = true;
                    }
                    break;

                case "cd root":
                    if(path == "/"){
                        path = "/root";
                    }
                    break;
                case "cd /root":
                    if(isRoot){
                        path = "/root";
                    } else {
                        accessDenied = true;
                    }
                    break;

                case "./editfile.sh":
                    if(isRoot) {
                        if (path == "/root") {
                            editingFile = true;
                            runningBinary = true;
                        }
                    }
                    break;

                case "cd home":
                    if(isRoot){
                        if(path == "/") {
                            path = "/home";
                        }
                    }
                    break;
                case "cd /home":
                    if(isRoot){
                        path = "/home";
                    } else {
                        accessDenied = true;
                    }
                    break;

                case "cd user":
                    if(path == "/home"){
                        path = "/home/user";
                    }
                    break;

                //the other method of toggling sound
                //this will pause/start mediaPlayer
                case "sound on":
                    if(!playingMusic) {
                        mediaPlayer.start();
                        playingMusic = true;
                    }
                    break;

                case "sound off":
                    if(playingMusic) {
                        mediaPlayer.pause();
                        playingMusic = false;
                    }
                    break;

            }


        }else {
          //we are checking to see if we are running binaries now
            //if running the upload file script, and 'cleanhaus' is entered
            //stop mediaPlayer, and have GameLoop move onto the next level
            //if the player has collected 3 priveledge escalation scripts
            //pwndwn4wut will show up in the uploadfile script
            //if pwndwn4wut is ran, this will grant the player root access to the
            //system
            if (uploadingFile) {
                switch(command){
                    case "cleanhaus":
                        mediaPlayer.setLooping(false);
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        nextLevel = true;
                        break;
                    case "pwndwn4wut":
                        if(escalate == 3){
                            if(timeSwitch == 0){
                                time = System.currentTimeMillis();
                                timeSwitch++;
                            }
                            isRoot = true;
                            runningPwnDwn = true;
                        }
                        break;
                    case "quit":
                        runningBinary = false;
                        uploadingFile = false;

                }

            }
              //if the player has gained root access, they can get to the /root directory and
              //run the editfile script, this will allow them to change the amount of items they have (max of 20) in their
              //inventory
             if(isRoot){
                if(editingFile){
                    switch (command){
                        case "grenades":
                            editFilePath = "grenades";
                            command = " ";
                            break;
                        case "shields":
                            editFilePath = "shields";
                            command = " ";
                            break;
                        case "lifegain":
                            editFilePath = "lifegain";
                            command = " ";
                            break;
                        case "quit":
                            editingFile = false;
                            runningBinary = false;
                            break;
                    }

                    switch (editFilePath){
                        case "grenades":
                            int testNades = numNades;

                            try {
                                numNades = Integer.parseInt(command);
                                if(numNades > 20){
                                    numNades = 20;
                                }
                            //if player enters something besides numbers, since the command is being interpreted as an int
                            //it needs to be one
                            }catch(NumberFormatException e){
                                numNades = testNades;
                            }
                            break;

                        case "shields":
                            int testShields = numShields;

                            try {
                                numShields = Integer.parseInt(command);
                                if(numShields > 20){
                                    numShields = 20;
                                }
                            }catch(NumberFormatException e){
                                numShields = testShields;
                            }
                            break;

                        case "lifegain":
                            int testGain = numGains;

                            try {
                                numGains = Integer.parseInt(command);
                                if(numGains > 20){
                                    numGains = 20;
                                }
                            }catch(NumberFormatException e){
                                numGains = testGain;
                            }
                            break;




                    }


                }
            }
        }

        // display the word as the player types
        typingWord = getStringFromList(word);
        //core of level input
        //if player presses enter, get the word the word the player has typed
        //assign this to command
        // remove everything from word
        //check to see if the command is valid syntax, if so, process it, if not
        //display error message
        if(isEnter){
            command = getStringFromList(word);
            word.clear();
            isEnter = false;
            syntax = isSyntaxValid(command);

        }




    }

    public void draw(){

        if(ourHolder.getSurface().isValid()) {
            canvas = this.ourHolder.lockCanvas();

            canvas.drawColor(Color.argb(255,0,0,0));


            canvas.drawBitmap(keyboard,0,boardYPos,paint);


            paint.setColor(Color.argb(255,255,255,255));

            paint.setTextSize(screenX/40);

            //if not the first terminal
            if(!termOne) {
                //this will be shown if the tutorial is not chosen or on all other terminals besides the first one
                //a brief message that guides the player on how to seek help, if needed
                if (helpTimeSwitch == 0) {
                    long tempTime = System.currentTimeMillis();
                    if (tempTime < helpTime + 8000) {
                        canvas.drawText("Need help? Use the help command", cliX + (tile * 10), boardYPos - (tile / 5), paint);
                    } else {
                        helpTimeSwitch++;
                    }
                }

                //same as the update() method, we only want to filter input for specific states of the terminal
                //in this instance, traversing the file system
                if (!runningBinary) {
                    //these have to deal with drawing the prompt based on the players path
                    switch (path) {
                        case "/":
                            canvas.drawText("[root@system /]#" + "  " + typingWord, cliX, cliY, paint);
                            break;
                        case "/root":
                            canvas.drawText("[root@system ~]#" + "  " + typingWord, cliX, cliY, paint);
                            break;
                        case "/home":
                            canvas.drawText("[root@system home]#" + "  " + typingWord, cliX, cliY, paint);
                            break;
                        case "/home/user":
                            if (!isRoot) {
                                canvas.drawText("[user@system ~]$" + "  " + typingWord, cliX, cliY, paint);
                            } else {
                                canvas.drawText("[root@system user]#" + "  " + typingWord, cliX, cliY, paint);
                            }
                            break;
                        case "/home/user/toolkits":
                            if (!isRoot) {
                                canvas.drawText("[user@system toolkits]$" + "  " + typingWord, cliX, cliY, paint);
                            } else {
                                canvas.drawText("[root@system toolkits]#" + "  " + typingWord, cliX, cliY, paint);
                            }
                            break;


                    }

                    switch (command) {
                        //what is draw as output for the ls command, this matches the corresponding path
                        case "ls":
                            switch (path) {
                                case "/":
                                    if (isRoot) {
                                        paint.setColor(Color.argb(255, 48, 114, 203));
                                        canvas.drawText("bin  boot  dev  etc  home  lib  mnt  proc  root  run  sbin  srv  sys  tmp  usr  var", cliX, cliY + (tile), paint);
                                    }
                                    break;
                                case "/root":
                                    if (isRoot) {
                                        canvas.drawText("info.text", cliX, cliY + (tile), paint);
                                        paint.setColor(Color.argb(255, 0, 255, 0));
                                        canvas.drawText("editfile.sh", cliX + (tile * 5), cliY + (tile), paint);
                                    }
                                    break;
                                case "/home":
                                    if (isRoot) {
                                        paint.setColor(Color.argb(255, 48, 114, 203));
                                        canvas.drawText("user", cliX, cliY + (tile), paint);
                                    }
                                    break;
                                case "/home/user":
                                    canvas.drawText("welcome.text", cliX, cliY + (tile), paint);
                                    canvas.drawText("sound.text",cliX + (tile * 4), cliY + (tile), paint);
                                    paint.setColor(Color.argb(255, 48, 114, 203));
                                    canvas.drawText("toolkits", cliX + (tile * 8), cliY + (tile), paint);
                                    break;
                                case "/home/user/toolkits":
                                    canvas.drawText("info.text", cliX, cliY + (tile), paint);
                                    paint.setColor(Color.argb(255, 0, 255, 0));
                                    canvas.drawText("uploadfile.sh", cliX + (tile * 5), cliY + (tile), paint);
                                    break;


                            }
                            break;
                        //specific folders can also be listed out
                        case "ls toolkits":
                            if(path == "/home/user"){
                                canvas.drawText("info.text", cliX, cliY + (tile), paint);
                                paint.setColor(Color.argb(255, 0, 255, 0));
                                canvas.drawText("uploadfile.sh", cliX + (tile * 5), cliY + (tile), paint);
                            }
                            break;

                        case "ls /":
                            if (isRoot) {
                                paint.setColor(Color.argb(255, 48, 114, 203));
                                canvas.drawText("bin  boot  dev  etc  home  lib  mnt  proc  root  run  sbin  srv  sys  tmp  usr  var", cliX, cliY + (tile), paint);
                            }else{
                                accessDenied = true;
                            }
                            break;

                        case "ls /root":
                            if (isRoot) {
                                canvas.drawText("info.text", cliX, cliY + (tile), paint);
                                paint.setColor(Color.argb(255, 0, 255, 0));
                                canvas.drawText("editfile.sh", cliX + (tile * 5), cliY + (tile), paint);
                            }else{
                                accessDenied = true;
                            }
                            break;

                        case "ls /home":
                            if (isRoot) {
                                paint.setColor(Color.argb(255, 48, 114, 203));
                                canvas.drawText("user", cliX, cliY + (tile), paint);
                            }else{
                                accessDenied = true;
                            }
                            break;

                        case "ls /home/user":
                            canvas.drawText("welcome.text", cliX, cliY + (tile), paint);
                            paint.setColor(Color.argb(255, 48, 114, 203));
                            canvas.drawText("toolkits", cliX + (tile * 5), cliY + (tile), paint);
                            break;

                        case "ls /home/user/toolkits":
                            canvas.drawText("info.text", cliX, cliY + (tile), paint);
                            paint.setColor(Color.argb(255, 0, 255, 0));
                            canvas.drawText("uploadfile.sh", cliX + (tile * 5), cliY + (tile), paint);
                            break;

                        //the content of text files can be displayed
                        case "cat welcome.text":
                            if (path == "/home/user") {
                                canvas.drawText("Welcome my Dear traveler, to my domain", cliX, cliY + (tile), paint);
                                canvas.drawText("I hope what you see intrigues you, and entertains you", cliX, cliY + (tile * 2), paint);
                                canvas.drawText("Well, Enjoy. Tadaaaaa :)", cliX, cliY + (tile * 3), paint);
                            }
                            break;

                        case "cat sound.text":
                            if (path == "/home/user") {
                                canvas.drawText("Wanna toggle the sound?", cliX, cliY + (tile), paint);
                                canvas.drawText("sound on  (will turn on the sound)", cliX, cliY + (tile * 2), paint);
                                canvas.drawText("sound off (will turn off the sound)", cliX, cliY + (tile * 3), paint);
                            }
                            break;

                        case "cat info.text":
                            if (path == "/home/user/toolkits") {
                                canvas.drawText("My Dear traveler,", cliX, cliY + (tile), paint);
                                canvas.drawText("This utility is for any files you brought along", cliX, cliY + (tile * 2), paint);
                            }
                            if (path == "/root") {
                                canvas.drawText("Welcome my Dear traveler, you've managed to find the vulnerability", cliX, cliY + (tile), paint);
                                canvas.drawText("Now you can have some fun.", cliX, cliY + (tile * 2), paint);
                                canvas.drawText("Well, Enjoy. Tadaaaaa :)", cliX, cliY + (tile * 3), paint);
                            }
                            break;


                        //the help screen
                        case "help":
                            paint.setTextSize(screenX/55);
                            float size = (boardYPos - cliY) / 10;
                            canvas.drawText("ls         list contents of current directory (folder).", cliX + (tile / 5), cliY + size, paint);
                            canvas.drawText("cat        view the contents of a file. i.e. cat welcome.text", cliX + (tile / 5), cliY + (size * 2), paint);
                            canvas.drawText("cd         change current directory(folder). i.e. cd /home/user/toolkits ", cliX + (tile / 5), cliY + (size * 3), paint);
                            canvas.drawText("               can change to folder inside of current folder i.e. cd toolkits", cliX + (tile / 5), cliY + (size * 4), paint);
                            canvas.drawText("cd ..      move back a directory. If in /home/user/toolkits,  ", cliX + (tile / 5), cliY + (size * 5), paint);
                            canvas.drawText("               cd ..  would put you back in /home/user", cliX + (tile / 5), cliY + (size * 6), paint);
                            canvas.drawText("./         runs a script in current directory. i.e. ./uploadfile.sh", cliX + (tile / 5), cliY + (size * 7), paint);
                            canvas.drawText("pwd        see what directory your in", cliX + (tile / 5), cliY + (size * 8), paint);
                            canvas.drawText("files", cliX + (tile / 5), cliY + (size * 9), paint);
                            paint.setColor(Color.argb(255, 48, 114, 203));
                            canvas.drawText("folders", cliX + (tile * 3), cliY + (size * 9), paint);
                            paint.setColor(Color.argb(255, 0, 255, 0));
                            canvas.drawText("scripts", cliX + (tile * 6), cliY + (size * 9), paint);
                            break;

                        //print players working directory
                        case "pwd":
                            canvas.drawText(path, cliX, cliY + (tile), paint);
                            break;

                    }
                } else {
                  //if running binary
                    //if running editfile
                    if (editingFile) {
                        canvas.drawText("Enter file name to edit(or quit):" + " " + typingWord, cliX, cliY, paint);
                        canvas.drawText("grenades", cliX, cliY + (tile * 2), paint);
                        canvas.drawText("shields", cliX + (tile * 3), cliY + (tile * 2), paint);
                        canvas.drawText("lifegain", cliX + (tile * 6), cliY + (tile * 2), paint);
                        canvas.drawText(numNades + "", cliX, cliY + ((tile * 2) + (tile / 2)), paint);
                        canvas.drawText(numShields + "", cliX + (tile * 3), cliY + ((tile * 2) + (tile / 2)), paint);
                        canvas.drawText(numGains + "", cliX + (tile * 6), cliY + ((tile * 2) + (tile / 2)), paint);

                        if (editFilePath == "grenades") {
                            canvas.drawText("Enter number of Grenades(max 20):", cliX, cliY + tile, paint);
                        }
                        if (editFilePath == "shields") {
                            canvas.drawText("Enter number of Shields(max 20):", cliX, cliY + tile, paint);
                        }
                        if (editFilePath == "lifegain") {
                            canvas.drawText("Enter number of LifeGain(max 20):", cliX, cliY + tile, paint);
                        }


                    }
                    //if running uploadfile
                    if (uploadingFile) {
                        //if player hasn't gathered 3 priviledge escalate scripts, only show the cleanhaus option
                        //if they have, display pwndwn4wut option
                        if (!runningPwnDwn) {
                            canvas.drawText("File to upload from your storage(or quit):" + " " + typingWord, cliX, cliY, paint);
                            canvas.drawText("cleanhaus", cliX, cliY + (tile), paint);
                            if (escalate == 3) {
                                canvas.drawText("pwndwn4wut", cliX, cliY + (tile + (tile / 2)), paint);
                            }
                        //if running pwndwn4wut, based on time draw various states of the script to emulate a script being ran
                        } else {
                            long tempTime = System.currentTimeMillis();
                            if (tempTime < time + 10000) {
                                if (tempTime < time + 2000) {
                                    canvas.drawText("gatheringSUIDVulns()", cliX, cliY + (tile), paint);
                                }
                                if (tempTime > time + 2000 && tempTime < time + 4000) {
                                    canvas.drawText("hasVuln=true", cliX, cliY + (tile + (tile / 2)), paint);
                                }
                                if (tempTime > time + 4000 && tempTime < time + 6000) {
                                    canvas.drawText("executingVulnExploit()", cliX, cliY + (tile * 2), paint);
                                }
                                if (tempTime > time + 6000 && tempTime < time + 7000) {
                                    canvas.drawText("exploitSuccess=true", cliX, cliY + ((tile * 2) + (tile / 2)), paint);
                                }
                                if (tempTime > time + 7000 && tempTime < time + 8000) {
                                    canvas.drawText("rootGranted=true", cliX, cliY + (tile * 3), paint);
                                }
                                if (tempTime > time + 8000 && tempTime < time + 9999) {
                                    runningPwnDwn = false;
                                    runningBinary = false;
                                    uploadingFile = false;
                                }
                            }

                        }
                    }
                }

                paint.setColor(Color.argb(255,255,255,255));

                paint.setTextSize(screenX/40);

                //when player attempts to do something that requires root access, display this message
                if (accessDenied) {
                    canvas.drawText("Permission Denied", cliX, cliY + (tile * 4), paint);
                    accessDenied = false;
                }
                //when syntax is incorrect, provide error message
                if(!syntax) {
                        canvas.drawText("Unrecognized Command: Try \'help\' for list of commands", cliX, cliY + (tile * 4), paint);
                }

            }else{
              //tutorial welcome message is shown on the first terminal only
              //the player has the option to opt select or skip the tutorial
              //this is all based on time.
              //the player isn't able to skip forward in it
                long currentTime = System.currentTimeMillis();
                if(tutorialIntro) {
                    canvas.drawText("Welcome user! I'm ttyY2K", cliX, cliY, paint);
                    canvas.drawText("Do you want to see how to use me?(yes/no)" + typingWord, cliX, cliY + tile, paint);
                    switch (command) {
                        case "yes":
                            runTutorial = true;
                            tutorialIntro = false;
                            //used to mark when the tutorial has started
                            tutorialTime = System.currentTimeMillis();
                            break;
                        case "no":
                            runTutorial = false;
                            termOne = false;
                            break;
                    }
                    //if the player hasn't opted in 10 seconds, run the tutorial
                    if(currentTime > tutorialIntroTime + 10000){
                        runTutorial = true;
                        tutorialIntro = false;
                        tutorialTime = System.currentTimeMillis();
                    }
                }
                //based on time from when the tutorial started
                //display varying states of the tutorial
                if(runTutorial) {
                    long curTime = System.currentTimeMillis();
                    float size = tile/2;
                    paint.setColor(Color.argb(255,255,255,255));
                    paint.setTextSize(screenX/40);
                    if(curTime > tutorialTime && curTime < tutorialTime + 10000){
                        canvas.drawText("[user@system ~]",cliX,cliY,paint);
                        canvas.drawText("right now we are in a folder, the home folder, that's what the ~ in the",cliX,cliY + size,paint);
                        canvas.drawText("[user@system ~] prompt tells us, lets see what's in it",cliX,cliY+(size*2),paint);
                    }
                    if(curTime > tutorialTime + 10000 && curTime < tutorialTime + 13000){
                        canvas.drawText("[user@system ~] ls",cliX,cliY,paint);
                    }
                    if(curTime > tutorialTime + 13000 && curTime < tutorialTime + 25000){
                        canvas.drawText("[user@system ~]",cliX,cliY,paint);
                        canvas.drawText("welcome.text", cliX, cliY + (size), paint);
                        paint.setColor(Color.argb(255, 48, 114, 203));
                        canvas.drawText("coolfolder", cliX + (tile * 5), cliY + (size), paint);
                        paint.setColor(Color.argb(255,255,255,255));
                        canvas.drawText("After each command you type make sure to press the enter key, this executes it. ",cliX,cliY+(size*2),paint);
                        canvas.drawText("The ls command lists the contents of the folder your in,",cliX,cliY + (size*3),paint);
                        canvas.drawText("there's a welcome.txt file, and the blue one is another folder, coolfolder.",cliX,cliY+(size*4),paint);
                        canvas.drawText("Wonder what the file says?",cliX,cliY +(size*5),paint);
                    }
                    if(curTime > tutorialTime + 25000 && curTime < tutorialTime + 29000){
                        canvas.drawText("[user@system ~] cat welcome.text",cliX,cliY,paint);
                    }
                    if(curTime > tutorialTime+ 29000 && curTime < tutorialTime + 33000){
                        canvas.drawText("Welcome! I hope you have a great time exploring!",cliX,cliY,paint);
                    }
                    if(curTime > tutorialTime + 33000 && curTime < tutorialTime + 41000){
                        canvas.drawText("[user@system ~]",cliX,cliY,paint);
                        canvas.drawText("what to do next? hmmm what was in this folder again?",cliX,cliY+tile,paint);
                    }
                    if(curTime > tutorialTime + 41000 && curTime < tutorialTime + 43000){
                        canvas.drawText("[user@system ~] ls",cliX,cliY,paint);
                    }
                    if(curTime > tutorialTime + 43000 && curTime < tutorialTime + 53000){
                        canvas.drawText("[user@system ~]",cliX,cliY,paint);
                        canvas.drawText("welcome.text", cliX, cliY + (size), paint);
                        paint.setColor(Color.argb(255, 48, 114, 203));
                        canvas.drawText("coolfolder", cliX + (tile * 5), cliY + (size), paint);
                        paint.setColor(Color.argb(255,255,255,255));
                        canvas.drawText("wonder what's in the coolfolder ? Sounds fun :)",cliX,cliY+(size*2),paint);
                    }
                    if(curTime > tutorialTime + 53000 && curTime < tutorialTime + 57000){
                        canvas.drawText("[user@system ~] cd coolfolder",cliX,cliY,paint);
                    }
                    if(curTime > tutorialTime + 57000 && curTime < tutorialTime + 66000){
                        canvas.drawText("[user@system coolfolder]",cliX,cliY,paint);
                        canvas.drawText("Neat! The cd command changes the directory(folder) to the one you tell it too.",cliX,cliY + size,paint);
                        canvas.drawText("Shall we see what's in it?",cliX,cliY + size*2,paint);
                    }
                    if(curTime > tutorialTime + 66000 && curTime < tutorialTime + 69000){
                        canvas.drawText("[user@system coolfolder] ls",cliX,cliY,paint);
                    }
                    if(curTime > tutorialTime + 69000 && curTime < tutorialTime + 79000){
                        canvas.drawText("[user@system coolfolder]",cliX,cliY,paint);
                        canvas.drawText("info.text", cliX, cliY + (size), paint);
                        paint.setColor(Color.argb(255, 0, 255, 0));
                        canvas.drawText("funscript.sh", cliX + (tile * 5), cliY + (size), paint);
                        paint.setColor(Color.argb(255,255,255,255));
                        canvas.drawText(":0 oooo a green one! This is a script we can run, lets run it and see what it does!",cliX,cliY + (size*2),paint);
                    }
                    if(curTime > tutorialTime + 79000 && curTime < tutorialTime + 84000){
                        canvas.drawText("[user@system coolfolder] ./funscript.sh",cliX,cliY,paint);
                    }
                    if(curTime > tutorialTime + 84000 && curTime < tutorialTime + 86000){
                        canvas.drawText("Pirate vs Ninja?",cliX,cliY,paint);
                    }
                    if(curTime > tutorialTime + 86000 && curTime < tutorialTime + 88000){
                        canvas.drawText("Pirate vs Ninja?  Ninja",cliX,cliY,paint);
                    }
                    if(curTime > tutorialTime + 88000 && curTime < tutorialTime + 97000){
                        canvas.drawText("Pirate vs Ninja?  Ninja",cliX,cliY,paint);
                        canvas.drawText("Arrrrrr ye arrogant scallywag! Ye think only one ya can handle 50 o' us?",cliX,cliY + (tile),paint);
                    }
                    if(curTime > tutorialTime + 97000 && curTime < tutorialTime + 107000){
                        canvas.drawText("[user@system coolfolder]",cliX,cliY,paint);
                        canvas.drawText("Quite the opinionated script... If you need to go back to the previous folder you were",cliX,cliY + (tile),paint);
                        canvas.drawText("in you'd use the cd .. command", cliX, cliY + (tile +(tile/2)),paint);
                    }
                    if(curTime > tutorialTime + 107000 && curTime < tutorialTime + 111000){
                        canvas.drawText("[user@system coolfolder] cd ..",cliX,cliY,paint);
                    }
                    if(curTime > tutorialTime + 111000 && curTime < tutorialTime + 121000){
                        canvas.drawText("[user@system ~]",cliX,cliY,paint);
                        canvas.drawText("now we are back to the home folder. Well, that should about do it for now",cliX,cliY + (tile/2),paint);
                        canvas.drawText("have fun in the digital world friend :)",cliX,cliY + tile,paint);
                    }
                    if(curTime > tutorialTime + 121000 && curTime < tutorialTime + 124000){
                        canvas.drawBitmap(asciismiles,smilesXPos,smilesYPos,paint);
                    }
                    if(curTime > tutorialTime + 124000){
                        runTutorial = false;
                        termOne = false;
                        //after tutorial display help message
                        helpTime = System.currentTimeMillis();
                    }
                }
            }

            ourHolder.unlockCanvasAndPost(canvas);

        }


    }

    //World mandated methods
    public boolean isSecretLevel(){return secretLevel;}

    public boolean isAlive(){return alive;}

    public boolean isNxtLevel(){return nextLevel;}

    public int getNumNades(){return numNades;}
    public int getNumGains(){return numGains;}
    public int getNumShields(){return numShields;}

    public int getPartOfEscalate(){return escalate;}

    //used to get typingWord and command from word
    //use separate list to get characters from word, send the new list to string
    String getStringFromList(ArrayList<String> list){

        StringBuilder builder = new StringBuilder(list.size());


        for(String s : list){
            builder.append(s);
        }
        return builder.toString();
    }

    //check command to see if its in the goodCommand list
    //if so, syntax = true
    //if not, syntax = false
    private boolean isSyntaxValid(String s){
        boolean localsyn = false;
        for(int i = 0; i < goodCommand.length;i++) {
            if (s.equals(goodCommand[i])){
                localsyn = true;
                break;
            }
        }
        return localsyn;
    }


    public boolean onTouchEvent(MotionEvent motionEvent){
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN:

                //check to see which key the input resides in
                for(int i = 0; i < keyz.size(); i++){

                    if(motionEvent.getX() > keyz.get(i).getWhereToDraw().left && motionEvent.getX() < keyz.get(i).getWhereToDraw().right
                            && motionEvent.getY() > keyz.get(i).getWhereToDraw().top && motionEvent.getY() < keyz.get(i).getWhereToDraw().bottom){
                        //if it's enter, set enter true so word will be set to command
                        if(keyz.get(i).getCharacter() == "enter"){
                            isEnter = true;
                        }
                        if(!isEnter) {
                            //can delete characters in word
                            if(keyz.get(i).getCharacter() == "back"){
                                if(!word.isEmpty()) {
                                    word.remove(word.size() - 1);
                                }
                            }else{
                                //add charcters to word
                                word.add(keyz.get(i).getCharacter());
                            }
                        }

                    }

                }


                break;

            case MotionEvent.ACTION_UP:

                break;
        }

        return true;
    }

    //Word mandated methods 
    public boolean isPlayingMusic(){return playingMusic;}
    public void pauseMusic(){mediaPlayer.pause();}


}
