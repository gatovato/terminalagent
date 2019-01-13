package com.terminalagent.terminalagent;

import android.graphics.PointF;
import android.graphics.RectF;

/**
 * Created by gatovato on 10/11/2017.
 */

//real meat and potatoes behind side scrolling action in Level

public class ViewPort {

    //based on players center (x,y), for more see Level->getCenter()
    private PointF currentViewportWorldCentre;

    private RectF convertedRect;
    private PointF convertedPoint;
    private int pixelsPerMetreX;
    private int pixelsPerMetreY;
    private int screenCentreX;
    private int screenCentreY;
    private int metresToShowX;
    private int metresToShowY;
    //new test post view_port_test1
    private int screenX;
    private int screenY;



    public ViewPort(int screenXResolution, int screenYResolution){

        screenCentreX = screenXResolution / 2;
        screenCentreY = screenYResolution / 2;

        screenX = screenXResolution/4;
        screenY = screenYResolution/4;



        pixelsPerMetreX = 1;
        pixelsPerMetreY = 1;



        metresToShowX  = (pixelsPerMetreX)*3200;
        metresToShowY = (pixelsPerMetreY)*2800;




        convertedRect = new RectF();
        convertedPoint = new PointF();

        currentViewportWorldCentre = new PointF();

    }



    void setWorldCentre(float x, float y){
        currentViewportWorldCentre.x = x;
        currentViewportWorldCentre.y = y;
    }



    //create viewable frame relative to players center, anything outside of this frame will be 'clipped' in Level
    public RectF worldToScreen(float objectX, float objectY, float objectWidth, float objectHeight){
        int left = (int) (screenX - ((currentViewportWorldCentre.x - objectX)*pixelsPerMetreX));
        int top = (int)(screenY-((currentViewportWorldCentre.y - objectY)*pixelsPerMetreY));
        int right = (int) (left + (objectWidth)* pixelsPerMetreX);
        int bottom = (int)(top + (objectHeight) * pixelsPerMetreY);
        convertedRect.set(left,top,right,bottom);
        return convertedRect;
    }



    //this is used within the left to set the objects outside of the viewport invisible, this will create the scrolling affect
    public boolean clipObjects(float objectX,
                               float objectY,
                               float objectWidth,
                               float objectHeight
    ){

        boolean clipped = true;



        if(objectX - objectWidth < currentViewportWorldCentre.x + (metresToShowX/2)){
            if(objectX + objectWidth > currentViewportWorldCentre.x- (metresToShowX/2 )){
                if(objectY - objectHeight < currentViewportWorldCentre.y+ (metresToShowY/2)){
                    if(objectY + objectHeight > currentViewportWorldCentre.y- (metresToShowY/2)){
                        clipped = false;
                    }
                }
            }
        }
        return clipped;
    }







}
