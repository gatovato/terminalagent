package com.terminalagent.terminalagent;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.ArrayList;

/**
 * Created by gatovato on 10/8/2017.
 */

//standalone class to easily create 2D Bitmap arrays based on World number
//workflow is: based on World number store all Bitmaps in 2D array tileMap and return with getTileMap()
public class TileMap {

    private  float tileWidth;
    private float tileHeight;

    private int frameWidth;
    private int frameHeight;

    private int numMap;

    private Bitmap [][] tileMap;

    private Rect frameToDraw;





    public TileMap(int map, Context context, float tileWidth, float tileHeight){

        numMap = map;

        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;

        frameWidth = Math.round(tileWidth);
        frameHeight = Math.round(tileHeight);

        frameToDraw = new Rect(0,0,frameWidth,frameHeight);

        switch (numMap){

            case 1:

                Bitmap cloud;
                cloud = BitmapFactory.decodeResource(context.getResources(), R.drawable.cloud3);
                cloud = Bitmap.createScaledBitmap(cloud, frameWidth, frameHeight,false);

                Bitmap tree;
                tree = BitmapFactory.decodeResource(context.getResources(), R.drawable.tree);
                tree = Bitmap.createScaledBitmap(tree, frameWidth, frameHeight,false);

                Bitmap flower;
                flower = BitmapFactory.decodeResource(context.getResources(), R.drawable.flowers);
                flower = Bitmap.createScaledBitmap(flower, frameWidth, frameHeight, false);

                Bitmap fancyTree;
                fancyTree = BitmapFactory.decodeResource(context.getResources(), R.drawable.fancytree);
                fancyTree = Bitmap.createScaledBitmap(fancyTree, frameWidth, frameHeight, false);

                Bitmap shrub;
                shrub = BitmapFactory.decodeResource(context.getResources(), R.drawable.shrub);
                shrub = Bitmap.createScaledBitmap(shrub, frameWidth, frameHeight, false);

                Bitmap flowerField;
                flowerField = BitmapFactory.decodeResource(context.getResources(), R.drawable.flowerfield);
                flowerField = Bitmap.createScaledBitmap(flowerField, frameWidth, frameHeight, false);

                Bitmap fountain;
                fountain = BitmapFactory.decodeResource(context.getResources(), R.drawable.fountain);
                fountain = Bitmap.createScaledBitmap(fountain, frameWidth, frameHeight, false);

                tileMap = new Bitmap[][]{

                        {null,null,null,cloud,null,null,null,cloud,null,null,null,cloud,null,null,null,cloud,null,null,null,cloud},
                        {null,null,cloud,null,null,null,cloud,null,null,null,cloud,null,null,null,cloud,null,null,null,cloud,null},
                        {null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null},
                        {tree,flower,flower,tree,flower,flower,fancyTree,shrub,flowerField,fountain,flowerField,shrub,fancyTree,flower,flower,tree,flower,flower,tree,flower},
                        {null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null}

                };
                break;

            case 2:
                Bitmap cloud1;
                cloud1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.cloud3);
                cloud1 = Bitmap.createScaledBitmap(cloud1, frameWidth, frameHeight,false);

                Bitmap tree1;
                tree1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.tree);
                tree1 = Bitmap.createScaledBitmap(tree1, frameWidth, frameHeight,false);

                Bitmap flower1;
                flower1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.flowers);
                flower1 = Bitmap.createScaledBitmap(flower1, frameWidth, frameHeight, false);

                Bitmap fancyTree1;
                fancyTree1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.fancytree);
                fancyTree1 = Bitmap.createScaledBitmap(fancyTree1, frameWidth, frameHeight, false);

                Bitmap shrub1;
                shrub1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.shrub);
                shrub1 = Bitmap.createScaledBitmap(shrub1, frameWidth, frameHeight, false);

                Bitmap flowerField1;
                flowerField1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.flowerfield);
                flowerField1 = Bitmap.createScaledBitmap(flowerField1, frameWidth, frameHeight, false);

                Bitmap manora1;
                manora1 = BitmapFactory.decodeResource(context.getResources(),R.drawable.manora1);
                manora1 = Bitmap.createScaledBitmap(manora1,frameWidth,frameHeight,false);

                Bitmap manora2;
                manora2 = BitmapFactory.decodeResource(context.getResources(),R.drawable.manora2);
                manora2 = Bitmap.createScaledBitmap(manora2,frameWidth,frameHeight,false);

                Bitmap manora3;
                manora3 = BitmapFactory.decodeResource(context.getResources(),R.drawable.manora3);
                manora3 = Bitmap.createScaledBitmap(manora3,frameWidth,frameHeight,false);

                Bitmap manora4;
                manora4 = BitmapFactory.decodeResource(context.getResources(),R.drawable.manora4);
                manora4 = Bitmap.createScaledBitmap(manora4,frameWidth,frameHeight,false);

                Bitmap manora5;
                manora5 = BitmapFactory.decodeResource(context.getResources(),R.drawable.manora5);
                manora5 = Bitmap.createScaledBitmap(manora5,frameWidth,frameHeight,false);

                Bitmap manora6;
                manora6 = BitmapFactory.decodeResource(context.getResources(),R.drawable.manora6);
                manora6 = Bitmap.createScaledBitmap(manora6,frameWidth,frameHeight,false);

                Bitmap manorb1;
                manorb1 = BitmapFactory.decodeResource(context.getResources(),R.drawable.manorb1);
                manorb1 = Bitmap.createScaledBitmap(manorb1,frameWidth,frameHeight,false);

                Bitmap manorb2;
                manorb2 = BitmapFactory.decodeResource(context.getResources(),R.drawable.manorb2);
                manorb2 = Bitmap.createScaledBitmap(manorb2,frameWidth,frameHeight,false);

                Bitmap manorb3;
                manorb3 = BitmapFactory.decodeResource(context.getResources(),R.drawable.manorb3);
                manorb3 = Bitmap.createScaledBitmap(manorb3,frameWidth,frameHeight,false);

                Bitmap manorb4;
                manorb4 = BitmapFactory.decodeResource(context.getResources(),R.drawable.manorb4);
                manorb4 = Bitmap.createScaledBitmap(manorb4,frameWidth,frameHeight,false);

                Bitmap manorb5;
                manorb5 = BitmapFactory.decodeResource(context.getResources(),R.drawable.manorb5);
                manorb5 = Bitmap.createScaledBitmap(manorb5,frameWidth,frameHeight,false);

                Bitmap manorb6;
                manorb6 = BitmapFactory.decodeResource(context.getResources(),R.drawable.manorb6);
                manorb6 = Bitmap.createScaledBitmap(manorb6,frameWidth,frameHeight,false);

                Bitmap manorc1;
                manorc1 = BitmapFactory.decodeResource(context.getResources(),R.drawable.manorc1);
                manorc1 = Bitmap.createScaledBitmap(manorc1,frameWidth,frameHeight,false);

                Bitmap manorc2;
                manorc2 = BitmapFactory.decodeResource(context.getResources(),R.drawable.manorc2);
                manorc2 = Bitmap.createScaledBitmap(manorc2,frameWidth,frameHeight,false);

                Bitmap manorc3;
                manorc3 = BitmapFactory.decodeResource(context.getResources(),R.drawable.manorc3);
                manorc3 = Bitmap.createScaledBitmap(manorc3,frameWidth,frameHeight,false);

                Bitmap manorc4;
                manorc4 = BitmapFactory.decodeResource(context.getResources(),R.drawable.manorc4);
                manorc4 = Bitmap.createScaledBitmap(manorc4,frameWidth,frameHeight,false);

                Bitmap manorc5;
                manorc5 = BitmapFactory.decodeResource(context.getResources(),R.drawable.manorc5);
                manorc5 = Bitmap.createScaledBitmap(manorc5,frameWidth,frameHeight,false);

                Bitmap manorc6;
                manorc6 = BitmapFactory.decodeResource(context.getResources(),R.drawable.manorc6);
                manorc6 = Bitmap.createScaledBitmap(manorc6,frameWidth,frameHeight,false);

                tileMap = new Bitmap[][]{

                        {null,null,null,cloud1,null,null,null,cloud1,null,null,null,cloud1,null,null,null,cloud1,null,null,null,cloud1},
                        {null,null,cloud1,null,null,null,cloud1,manora1,manora2,manora3,manora4,manora5,manora6,null,cloud1,null,null,null,cloud1,null},
                        {null,null,null,null,null,null,null,manorb1,manorb2,manorb3,manorb4,manorb5,manorb6,null,null,null,null,null,null,null},
                        {tree1,flower1,flower1,tree1,flower1,flower1,fancyTree1,manorc1,manorc2,manorc3,manorc4,manorc5,manorc6,fancyTree1,flower1,flower1,tree1,flower1,flower1,tree1},
                        {null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null}

                };
                break;

            case 3:

                Bitmap panel1;
                panel1 = BitmapFactory.decodeResource(context.getResources(),R.drawable.panelleft);
                panel1 = Bitmap.createScaledBitmap(panel1,frameWidth,frameHeight,false);

                Bitmap panel2;
                panel2 = BitmapFactory.decodeResource(context.getResources(),R.drawable.panelright);
                panel2 = Bitmap.createScaledBitmap(panel2,frameWidth,frameHeight,false);

                Bitmap rail1;
                rail1 = BitmapFactory.decodeResource(context.getResources(),R.drawable.railleft);
                rail1 = Bitmap.createScaledBitmap(rail1,frameWidth,frameHeight,false);

                Bitmap rail2;
                rail2 = BitmapFactory.decodeResource(context.getResources(),R.drawable.railright);
                rail2 = Bitmap. createScaledBitmap(rail2,frameWidth,frameHeight,false);

                Bitmap staircasea1;
                staircasea1 = BitmapFactory.decodeResource(context.getResources(),R.drawable.staircasea1);
                staircasea1 = Bitmap.createScaledBitmap(staircasea1,frameWidth,frameHeight,false);

                Bitmap staircasea2;
                staircasea2 = BitmapFactory.decodeResource(context.getResources(),R.drawable.staircasea2);
                staircasea2 = Bitmap.createScaledBitmap(staircasea2,frameWidth,frameHeight,false);

                Bitmap staircasea3;
                staircasea3 = BitmapFactory.decodeResource(context.getResources(),R.drawable.staircasea3);
                staircasea3 = Bitmap.createScaledBitmap(staircasea3,frameWidth,frameHeight,false);

                Bitmap staircaseb1;
                staircaseb1 = BitmapFactory.decodeResource(context.getResources(),R.drawable.staircaseb1);
                staircaseb1 = Bitmap.createScaledBitmap(staircaseb1,frameWidth,frameHeight,false);

                Bitmap staircaseb2;
                staircaseb2 = BitmapFactory.decodeResource(context.getResources(),R.drawable.staircaseb2);
                staircaseb2 = Bitmap.createScaledBitmap(staircaseb2,frameWidth,frameHeight,false);

                Bitmap staircaseb3;
                staircaseb3 = BitmapFactory.decodeResource(context.getResources(),R.drawable.staircaseb3);
                staircaseb3 = Bitmap.createScaledBitmap(staircaseb3,frameWidth,frameHeight,false);

                Bitmap staircasec1;
                staircasec1 = BitmapFactory.decodeResource(context.getResources(),R.drawable.staircasec1);
                staircasec1 = Bitmap.createScaledBitmap(staircasec1,frameWidth,frameHeight,false);

                Bitmap staircasec2;
                staircasec2 = BitmapFactory.decodeResource(context.getResources(),R.drawable.staircasec2);
                staircasec2 = Bitmap.createScaledBitmap(staircasec2,frameWidth,frameHeight,false);

                Bitmap staircasec3;
                staircasec3 = BitmapFactory.decodeResource(context.getResources(),R.drawable.staircasec3);
                staircasec3 = Bitmap.createScaledBitmap(staircasec3,frameWidth,frameHeight,false);


                tileMap = new Bitmap[][]{

                        {rail1,rail2,rail1,rail2,staircasea1,staircasea2,staircasea3,rail2,rail1,rail2,rail1,rail2},
                        {null,null,null,null,staircaseb1,staircaseb2,staircaseb3,null,null,null,null,null},
                        {panel2,panel1,panel2,panel1,staircasec1,staircasec2,staircasec3,panel2,panel1,panel2,panel1,panel2},
                        {null,null,null,null,null,null,null,null,null,null,null,null}

                };
                break;

            case 4:

                Bitmap light;
                light = BitmapFactory.decodeResource(context.getResources(),R.drawable.light);
                light = Bitmap.createScaledBitmap(light,frameWidth,frameHeight,false);

                Bitmap window;
                window = BitmapFactory.decodeResource(context.getResources(),R.drawable.window);
                window = Bitmap.createScaledBitmap(window,frameWidth,frameHeight,false);

                Bitmap tokoil;
                tokoil = BitmapFactory.decodeResource(context.getResources(),R.drawable.tokoil);
                tokoil = Bitmap.createScaledBitmap(tokoil,frameWidth,frameHeight,false);

                Bitmap denalioil;
                denalioil = BitmapFactory.decodeResource(context.getResources(),R.drawable.denalioil);
                denalioil = Bitmap.createScaledBitmap(denalioil,frameWidth,frameHeight,false);

                Bitmap berdineau;
                berdineau = BitmapFactory.decodeResource(context.getResources(),R.drawable.berdineau);
                berdineau = Bitmap.createScaledBitmap(berdineau,frameWidth,frameHeight,false);

                Bitmap panelleft;
                panelleft = BitmapFactory.decodeResource(context.getResources(),R.drawable.panelleft);
                panelleft = Bitmap.createScaledBitmap(panelleft,frameWidth,frameHeight,false);

                Bitmap panelright;
                panelright = BitmapFactory.decodeResource(context.getResources(),R.drawable.panelright);
                panelright = Bitmap.createScaledBitmap(panelright,frameWidth,frameHeight,false);

                tileMap = new Bitmap[][]{

                        {null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null},
                        {window,null,null,null,null,null,window,null,null,null,null,null,window,null,null,null,null,null,window,null,null,null,null,null,window},
                        {null,null,light,tokoil,light,null,null,null,light,denalioil,light,null,null,null,light,berdineau,light,null,null,null,null,null,null,null,null},
                        {panelleft,panelright,panelleft,panelright,panelleft,panelright,panelleft,panelright,panelleft,panelright,panelleft,panelright,panelleft,panelright,panelleft,panelright,panelleft,panelright,panelleft,panelright,panelleft,panelright,panelleft,panelright,panelleft},
                        {null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null}
                };
                break;

            case 5:

                light = BitmapFactory.decodeResource(context.getResources(),R.drawable.light);
                light = Bitmap.createScaledBitmap(light,frameWidth,frameHeight,false);

                Bitmap togwoteeoil;
                togwoteeoil = BitmapFactory.decodeResource(context.getResources(),R.drawable.togwoteeoil);
                togwoteeoil = Bitmap.createScaledBitmap(togwoteeoil,frameWidth,frameHeight,false);

                Bitmap norcaloil;
                norcaloil = BitmapFactory.decodeResource(context.getResources(),R.drawable.norcaloil);
                norcaloil = Bitmap.createScaledBitmap(norcaloil,frameWidth,frameHeight,false);

                Bitmap beachoil;
                beachoil = BitmapFactory.decodeResource(context.getResources(),R.drawable.beachoil);
                beachoil = Bitmap.createScaledBitmap(beachoil,frameWidth,frameHeight,false);


                window = BitmapFactory.decodeResource(context.getResources(),R.drawable.window);
                window = Bitmap.createScaledBitmap(window,frameWidth,frameHeight,false);


                panelleft = BitmapFactory.decodeResource(context.getResources(),R.drawable.panelleft);
                panelleft = Bitmap.createScaledBitmap(panelleft,frameWidth,frameHeight,false);


                panelright = BitmapFactory.decodeResource(context.getResources(),R.drawable.panelright);
                panelright = Bitmap.createScaledBitmap(panelright,frameWidth,frameHeight,false);

                tileMap = new Bitmap[][]{

                        {null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null},
                        {window,null,null,null,null,null,window,null,null,null,null,null,window,null,null,null,null,null,window,null,null,null,null,null,window},
                        {null,null,light,togwoteeoil,light,null,null,null,light,norcaloil,light,null,null,null,light,beachoil,light,null,null,null,null,null,null,null,null},
                        {panelleft,panelright,panelleft,panelright,panelleft,panelright,panelleft,panelright,panelleft,panelright,panelleft,panelright,panelleft,panelright,panelleft,panelright,panelleft,panelright,panelleft,panelright,panelleft,panelright,panelleft,panelright,panelleft},
                        {null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null}
                };
                break;

            case 6:


                light = BitmapFactory.decodeResource(context.getResources(),R.drawable.light);
                light = Bitmap.createScaledBitmap(light,frameWidth,frameHeight,false);


                window = BitmapFactory.decodeResource(context.getResources(),R.drawable.window);
                window = Bitmap.createScaledBitmap(window,frameWidth,frameHeight,false);

                Bitmap couch;
                couch = BitmapFactory.decodeResource(context.getResources(),R.drawable.couch);
                couch = Bitmap.createScaledBitmap(couch,frameWidth,frameHeight,false);

                Bitmap a1study;
                a1study = BitmapFactory.decodeResource(context.getResources(),R.drawable.a1study);
                a1study = Bitmap.createScaledBitmap(a1study,frameWidth,frameHeight,false);

                Bitmap a2study;
                a2study = BitmapFactory.decodeResource(context.getResources(),R.drawable.a2study);
                a2study = Bitmap.createScaledBitmap(a2study,frameWidth,frameHeight,false);

                Bitmap a3study;
                a3study = BitmapFactory.decodeResource(context.getResources(),R.drawable.a3study);
                a3study = Bitmap.createScaledBitmap(a3study,frameWidth,frameHeight,false);

                Bitmap a4study;
                a4study = BitmapFactory.decodeResource(context.getResources(),R.drawable.a4study);
                a4study = Bitmap.createScaledBitmap(a4study,frameWidth,frameHeight,false);

                Bitmap b1study;
                b1study = BitmapFactory.decodeResource(context.getResources(),R.drawable.b1study);
                b1study = Bitmap.createScaledBitmap(b1study,frameWidth,frameHeight,false);

                Bitmap b2study;
                b2study = BitmapFactory.decodeResource(context.getResources(),R.drawable.b2study);
                b2study = Bitmap.createScaledBitmap(b2study,frameWidth,frameHeight,false);

                Bitmap b3study;
                b3study = BitmapFactory.decodeResource(context.getResources(),R.drawable.b3study);
                b3study = Bitmap.createScaledBitmap(b3study,frameWidth,frameHeight,false);

                Bitmap b4study;
                b4study = BitmapFactory.decodeResource(context.getResources(),R.drawable.b4study);
                b4study = Bitmap.createScaledBitmap(b4study,frameWidth,frameHeight,false);

                Bitmap c1study;
                c1study = BitmapFactory.decodeResource(context.getResources(),R.drawable.c1study);
                c1study = Bitmap.createScaledBitmap(c1study,frameWidth,frameHeight,false);

                Bitmap c2study;
                c2study = BitmapFactory.decodeResource(context.getResources(),R.drawable.c2study);
                c2study = Bitmap.createScaledBitmap(c2study,frameWidth,frameHeight,false);

                Bitmap c3study;
                c3study = BitmapFactory.decodeResource(context.getResources(),R.drawable.c3study);
                c3study = Bitmap.createScaledBitmap(c3study,frameWidth,frameHeight,false);

                Bitmap c4study;
                c4study = BitmapFactory.decodeResource(context.getResources(),R.drawable.c4study);
                c4study = Bitmap.createScaledBitmap(c4study,frameWidth,frameHeight,false);



                tileMap = new Bitmap[][]{

                        {null,null,window,null,a1study,a2study,a3study,a4study,null,window,null,null},
                        {null,null,null,null,b1study,b2study,b3study,b4study,null,null,null,null},
                        {null,couch,light,couch,c1study,c2study,c3study,c4study,couch,light,couch,null},
                        {null,null,null,null,null,null,null,null,null,null,null,null}

                };
                break;

            case 7:

                light = BitmapFactory.decodeResource(context.getResources(),R.drawable.light);
                light = Bitmap.createScaledBitmap(light,frameWidth,frameHeight,false);

                window = BitmapFactory.decodeResource(context.getResources(),R.drawable.window);
                window = Bitmap.createScaledBitmap(window,frameWidth,frameHeight,false);


                panelleft = BitmapFactory.decodeResource(context.getResources(),R.drawable.panelleft);
                panelleft = Bitmap.createScaledBitmap(panelleft,frameWidth,frameHeight,false);


                panelright = BitmapFactory.decodeResource(context.getResources(),R.drawable.panelright);
                panelright = Bitmap.createScaledBitmap(panelright,frameWidth,frameHeight,false);

                Bitmap wyforest;
                wyforest = BitmapFactory.decodeResource(context.getResources(),R.drawable.wyforest);
                wyforest = Bitmap.createScaledBitmap(wyforest,frameWidth,frameHeight,false);

                Bitmap bridgeroil;
                bridgeroil = BitmapFactory.decodeResource(context.getResources(),R.drawable.bridgeroil);
                bridgeroil = Bitmap.createScaledBitmap(bridgeroil,frameWidth,frameHeight,false);

                Bitmap grandteton;
                grandteton = BitmapFactory.decodeResource(context.getResources(),R.drawable.grandteton);
                grandteton = Bitmap.createScaledBitmap(grandteton,frameWidth,frameHeight,false);

                tileMap = new Bitmap[][]{

                        {null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null},
                        {window,null,null,null,null,null,window,null,null,null,null,null,window,null,null,null,null,null,window,null,null,null,null,null,window},
                        {null,null,light,wyforest,light,null,null,null,light,bridgeroil,light,null,null,null,light,grandteton,light,null,null,null,null,null,null,null,null},
                        {panelleft,panelright,panelleft,panelright,panelleft,panelright,panelleft,panelright,panelleft,panelright,panelleft,panelright,panelleft,panelright,panelleft,panelright,panelleft,panelright,panelleft,panelright,panelleft,panelright,panelleft,panelright,panelleft},
                        {null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null}
                };
                break;

            case 8:

                light = BitmapFactory.decodeResource(context.getResources(),R.drawable.light);
                light = Bitmap.createScaledBitmap(light,frameWidth,frameHeight,false);

                window = BitmapFactory.decodeResource(context.getResources(),R.drawable.window);
                window = Bitmap.createScaledBitmap(window,frameWidth,frameHeight,false);


                panelleft = BitmapFactory.decodeResource(context.getResources(),R.drawable.panelleft);
                panelleft = Bitmap.createScaledBitmap(panelleft,frameWidth,frameHeight,false);


                panelright = BitmapFactory.decodeResource(context.getResources(),R.drawable.panelright);
                panelright = Bitmap.createScaledBitmap(panelright,frameWidth,frameHeight,false);

                Bitmap bubs;
                bubs = BitmapFactory.decodeResource(context.getResources(),R.drawable.bubs);
                bubs = Bitmap.createScaledBitmap(bubs,frameWidth,frameHeight,false);

                Bitmap zardopic;
                zardopic = BitmapFactory.decodeResource(context.getResources(),R.drawable.zardopic);
                zardopic = Bitmap.createScaledBitmap(zardopic,frameWidth,frameHeight,false);

                Bitmap couchpanel;
                couchpanel = BitmapFactory.decodeResource(context.getResources(),R.drawable.couchpanel);
                couchpanel = Bitmap.createScaledBitmap(couchpanel,frameWidth,frameHeight,false);



                tileMap = new Bitmap[][]{

                        {null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null},
                        {null,null,window,null,null,window,null,window,null,null,window,null,window,null,null,window,null,null,window,null,null,null,null,null,window},
                        {null,null,null,null,null,null,null,light,bubs,zardopic,light,null,null,null,null,null,null,null,null,null,null,null,null,null},
                        {panelleft,panelright,panelleft,panelright,panelleft,panelright,panelleft,panelright,couchpanel,couchpanel,panelleft,panelright,panelleft,panelright,panelleft,panelright,panelleft,panelright,panelleft,panelright,panelleft,panelright,panelleft,panelright,panelleft},
                        {null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null}
                };
                break;

            case 9:

                window = BitmapFactory.decodeResource(context.getResources(),R.drawable.window);
                window = Bitmap.createScaledBitmap(window,frameWidth,frameHeight,false);

                Bitmap cloudwindow1;
                cloudwindow1 = BitmapFactory.decodeResource(context.getResources(),R.drawable.cloudwindow1);
                cloudwindow1 = Bitmap.createScaledBitmap(cloudwindow1,frameWidth,frameHeight,false);

                Bitmap cloudwindow2;
                cloudwindow2 = BitmapFactory.decodeResource(context.getResources(),R.drawable.cloudwindow2);
                cloudwindow2 = Bitmap.createScaledBitmap(cloudwindow2,frameWidth,frameHeight,false);

                Bitmap cloudwindow3;
                cloudwindow3 = BitmapFactory.decodeResource(context.getResources(),R.drawable.cloudwindow3);
                cloudwindow3 = Bitmap.createScaledBitmap(cloudwindow3,frameWidth,frameHeight,false);

                panelleft = BitmapFactory.decodeResource(context.getResources(),R.drawable.panelleft);
                panelleft = Bitmap.createScaledBitmap(panelleft,frameWidth,frameHeight,false);


                panelright = BitmapFactory.decodeResource(context.getResources(),R.drawable.panelright);
                panelright = Bitmap.createScaledBitmap(panelright,frameWidth,frameHeight,false);

                couchpanel = BitmapFactory.decodeResource(context.getResources(),R.drawable.couchpanel);
                couchpanel = Bitmap.createScaledBitmap(couchpanel,frameWidth,frameHeight,false);

                tileMap = new Bitmap[][]{

                        {window,window,cloudwindow1,window,window,cloudwindow3,window,window,cloudwindow2,window,window,cloudwindow1},
                        {cloudwindow2,window,window,cloudwindow3,window,window,cloudwindow2,window,window,cloudwindow1,window,window},
                        {panelleft,couchpanel,panelleft,couchpanel,panelleft,couchpanel,panelleft,couchpanel,panelleft,couchpanel,panelleft,panelright},
                        {null,null,null,null,null,null,null,null,null,null,null,null}

                };
                break;

            case 10:

                cloud = BitmapFactory.decodeResource(context.getResources(), R.drawable.finalcloud);
                cloud = Bitmap.createScaledBitmap(cloud, frameWidth, frameHeight,false);

                Bitmap leftend;
                leftend = BitmapFactory.decodeResource(context.getResources(),R.drawable.finalleftendcloud);
                leftend = Bitmap.createScaledBitmap(leftend,frameWidth,frameHeight,false);

                Bitmap rightend;
                rightend = BitmapFactory.decodeResource(context.getResources(),R.drawable.finalrightendcloud);
                rightend = Bitmap.createScaledBitmap(rightend,frameWidth,frameHeight,false);

                Bitmap a1finalbg;
                a1finalbg = BitmapFactory.decodeResource(context.getResources(),R.drawable.a1finalbg);
                a1finalbg = Bitmap.createScaledBitmap(a1finalbg,frameWidth,frameHeight,false);

                Bitmap a2finalbg;
                a2finalbg = BitmapFactory.decodeResource(context.getResources(),R.drawable.a2finalbg);
                a2finalbg = Bitmap.createScaledBitmap(a2finalbg,frameWidth,frameHeight,false);

                Bitmap b1finalbg;
                b1finalbg = BitmapFactory.decodeResource(context.getResources(),R.drawable.b1finalbg);
                b1finalbg = Bitmap.createScaledBitmap(b1finalbg,frameWidth,frameHeight,false);

                Bitmap b2finalbg;
                b2finalbg = BitmapFactory.decodeResource(context.getResources(),R.drawable.b2finalbg);
                b2finalbg = Bitmap.createScaledBitmap(b2finalbg,frameWidth,frameHeight,false);

                Bitmap b3finalbg;
                b3finalbg = BitmapFactory.decodeResource(context.getResources(),R.drawable.b3finalbg);
                b3finalbg = Bitmap.createScaledBitmap(b3finalbg,frameWidth,frameHeight,false);

                Bitmap cfinalbg;
                cfinalbg = BitmapFactory.decodeResource(context.getResources(),R.drawable.cfinalbg);
                cfinalbg = Bitmap.createScaledBitmap(cfinalbg,frameWidth,frameHeight,false);



                tileMap = new Bitmap[][]{

                        {null,a1finalbg,null,null,null,null,null,null,null,null,a2finalbg,null},
                        {null,null,null,b1finalbg,b2finalbg,b3finalbg,b1finalbg,b2finalbg,b3finalbg,null,null,null},
                        {null,null,leftend,cfinalbg,cfinalbg,cfinalbg,cfinalbg,cfinalbg,cfinalbg,rightend,null,null},
                        {null,leftend,cfinalbg,cfinalbg,cfinalbg,cfinalbg,cfinalbg,cfinalbg,cfinalbg,cfinalbg,rightend,null}

                };
                break;

            case 11:

                Bitmap a1end2;
                a1end2 = BitmapFactory.decodeResource(context.getResources(),R.drawable.a1end2);
                a1end2 = Bitmap.createScaledBitmap(a1end2,frameWidth,frameHeight,false);

                Bitmap a2end2;
                a2end2 = BitmapFactory.decodeResource(context.getResources(),R.drawable.a2end2);
                a2end2 = Bitmap.createScaledBitmap(a2end2,frameWidth,frameHeight,false);

                Bitmap a3end2;
                a3end2 = BitmapFactory.decodeResource(context.getResources(),R.drawable.a3end2);
                a3end2 = Bitmap.createScaledBitmap(a3end2,frameWidth,frameHeight,false);

                Bitmap b1end2;
                b1end2 = BitmapFactory.decodeResource(context.getResources(),R.drawable.b1end2);
                b1end2 = Bitmap.createScaledBitmap(b1end2,frameWidth,frameHeight,false);

                Bitmap b2end2;
                b2end2 = BitmapFactory.decodeResource(context.getResources(),R.drawable.b2end2);
                b2end2 = Bitmap.createScaledBitmap(b2end2,frameWidth,frameHeight,false);

                Bitmap b3end2;
                b3end2 = BitmapFactory.decodeResource(context.getResources(),R.drawable.b3end2);
                b3end2 = Bitmap.createScaledBitmap(b3end2,frameWidth,frameHeight,false);

                Bitmap c1end2;
                c1end2 = BitmapFactory.decodeResource(context.getResources(),R.drawable.c1end2);
                c1end2 = Bitmap.createScaledBitmap(c1end2,frameWidth,frameHeight,false);

                Bitmap c2end2;
                c2end2 = BitmapFactory.decodeResource(context.getResources(),R.drawable.c2end2);
                c2end2 = Bitmap.createScaledBitmap(c2end2,frameWidth,frameHeight,false);

                Bitmap c3end2;
                c3end2 = BitmapFactory.decodeResource(context.getResources(),R.drawable.c3end2);
                c3end2 = Bitmap.createScaledBitmap(c3end2,frameWidth,frameHeight,false);

                Bitmap c4end2;
                c4end2 = BitmapFactory.decodeResource(context.getResources(),R.drawable.c4end2);
                c4end2 = Bitmap.createScaledBitmap(c4end2,frameWidth,frameHeight,false);

                Bitmap d1end2;
                d1end2 = BitmapFactory.decodeResource(context.getResources(),R.drawable.d1end2);
                d1end2 = Bitmap.createScaledBitmap(d1end2,frameWidth,frameHeight,false);

                Bitmap d2end2;
                d2end2 = BitmapFactory.decodeResource(context.getResources(),R.drawable.d2end2);
                d2end2 = Bitmap.createScaledBitmap(d2end2,frameWidth,frameHeight,false);

                Bitmap d3end2;
                d3end2 = BitmapFactory.decodeResource(context.getResources(),R.drawable.d3end2);
                d3end2 = Bitmap.createScaledBitmap(d3end2,frameWidth,frameHeight,false);

                Bitmap d4end2;
                d4end2 = BitmapFactory.decodeResource(context.getResources(),R.drawable.d4end2);
                d4end2 = Bitmap.createScaledBitmap(d4end2,frameWidth,frameHeight,false);

                tileMap = new Bitmap[][]{

                        {null,null,null,a1end2,a2end2,a3end2,null,null,null,null},
                        {null,null,null,b1end2,b2end2,b3end2,null,null,null,null},
                        {null,null,null,c1end2,c2end2,c3end2,c4end2,null,null,null},
                        {null,null,null,d1end2,d2end2,d3end2,d4end2,null,null,null},
                        {null,null,null,null,null,null,null,null,null,null}

                };
                break;
        }





    }


    public Bitmap [][] getTileMap(){ return tileMap;}

    public Rect getFrameToDraw(){return frameToDraw;}





}
