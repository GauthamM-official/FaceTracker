/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.android.gms.samples.vision.face.facetracker;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;

import com.google.android.gms.samples.vision.face.facetracker.ui.camera.GraphicOverlay;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.Landmark;

/**
 * Graphic instance for rendering face position, orientation, and landmarks within an associated
 * graphic overlay view.
 */
class FaceGraphic extends GraphicOverlay.Graphic {
    private static final float FACE_POSITION_RADIUS = 10.0f;
    private static final float ID_TEXT_SIZE = 40.0f;
    private static final float ID_Y_OFFSET = 50.0f;
    private static final float ID_X_OFFSET = -50.0f;
    private static final float BOX_STROKE_WIDTH = 5.0f;

    private static final int COLOR_CHOICES[] = {
            Color.BLUE,
            Color.CYAN,
            Color.GREEN,
            Color.MAGENTA,
            Color.RED,
            Color.WHITE,
            Color.YELLOW
    };
    private static final String TAG = "de";
    private static int mCurrentColorIndex = 0;
    private String msg;
    private Paint mFacePositionPaint;
    private Paint mIdPaint;
    private Paint mBoxPaint;
    private Paint mTextPaint;
    private Paint mTextPaint2;
    private volatile Face mFace;
    private int mFaceId;
    private float mFaceHappiness;
    private ProjectActivity f;
    private Point l,r,b;
    private double lr,lb,rb,ratio;
    private int estl,maxl;

    /*private Face f1;
    private Canvas c1;*/

    FaceGraphic(GraphicOverlay overlay) {
        super(overlay);

        mCurrentColorIndex = (mCurrentColorIndex + 1) % COLOR_CHOICES.length;
        final int selectedColor = COLOR_CHOICES[mCurrentColorIndex];

        mFacePositionPaint = new Paint();
        mFacePositionPaint.setColor(selectedColor);

        mIdPaint = new Paint();
        mIdPaint.setColor(selectedColor);
        mIdPaint.setTextSize(ID_TEXT_SIZE);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setDither(true);
        mTextPaint.setTextSize(40);
        mTextPaint.setColor(Color.RED);
        mTextPaint.setStyle(Paint.Style.FILL);

        mTextPaint2 = new Paint();
        mTextPaint2.setAntiAlias(true);
        mTextPaint2.setDither(true);
        mTextPaint2.setTextSize(60);
        mTextPaint2.setFakeBoldText(true);
        mTextPaint2.setColor(Color.BLUE);
        mTextPaint2.setStyle(Paint.Style.FILL);

        mBoxPaint = new Paint();
        mBoxPaint.setColor(selectedColor);
        mBoxPaint.setStyle(Paint.Style.STROKE);
        mBoxPaint.setStrokeWidth(BOX_STROKE_WIDTH);
    }

    void setId(int id) {
        mFaceId = id;
    }


    /**
     * Updates the face instance from the detection of the most recent frame.  Invalidates the
     * relevant portions of the overlay to trigger a redraw.
     */
    void updateFace(Face face) {
        mFace = face;
        postInvalidate();
    }
    public double dist(float x1,float y1,float x2, float y2)
    {
        double dx,dy;
        double dista;
        double i=2;
        dx=x2-x1;
        dy=y2-y1;
        dx=Math.pow(dx,i);
        dy=Math.pow(dx, i);
        dista=Math.sqrt(dx+dy);
        return dista;

    }
    public void getstr(String s) {
        msg = new String(s);
        /*printcanva(msg);*/
    }
    public double Speaker_Distinguisher(float lx,float ly,float rx,float ry,float bx,float by)
    {
        double lr,rb,r;
        lr=dist(lx,ly,rx,ry);
        rb=dist(rx,ry,bx,by);
        r=rb/lr;
        return r;
    }
    /**
     * Draws the face annotations for position on the supplied canvas.
     */

    @Override
    public void draw(Resources resources, Canvas canvas, String text) {
        Face face = mFace;
        //givefc(face,canvas);
        if (face == null) {
            canvas.drawText(msg, 100, 100, mTextPaint2);
            return;
        }

        // Draws a circle at the position of the detected face, with the face's track id below.
        float x = translateX(face.getPosition().x + face.getWidth() / 2);
        float y = translateY(face.getPosition().y + face.getHeight() / 2);
        canvas.drawCircle(x, y, FACE_POSITION_RADIUS, mFacePositionPaint);
        //canvas.drawText("id: " + mFaceId, x + ID_X_OFFSET, y + ID_Y_OFFSET, mIdPaint);
        //canvas.drawText("happiness: " + String.format("%.2f", face.getIsSmilingProbability()), x - ID_X_OFFSET, y - ID_Y_OFFSET, mIdPaint);
        //canvas.drawText("right eye: " + String.format("%.2f", face.getIsRightEyeOpenProbability()), x + ID_X_OFFSET * 2, y + ID_Y_OFFSET * 2, mIdPaint);
        //canvas.drawText("left eye: " + String.format("%.2f", face.getIsLeftEyeOpenProbability()), x - ID_X_OFFSET*2, y - ID_Y_OFFSET*2, mIdPaint);

        // Draws a bounding box around the face.
        float xOffset = scaleX(face.getWidth() / 2.0f);
        float yOffset = scaleY(face.getHeight() / 2.0f);
        float left = x - xOffset;
        float top = y - yOffset;
        float right = x + xOffset;
        float bottom = y + yOffset;
        Bitmap _scratch = BitmapFactory.decodeResource(resources, R.drawable.bubble4);
        RectF rect=new RectF();
        rect.left=left;
        rect.right=right;
        rect.bottom=bottom;
        rect.top=top;
        RectF bubb=new RectF();
        bubb=rect;
        bubb.left=bubb.left+250;
        bubb.right=bubb.right+250;
        //bubb.bottom-=100;
        float bx = 0, by = 0, lx = 0, ly = 0, rx = 0, ry = 0;
        for (Landmark landmark : face.getLandmarks()) {
            if(landmark.getType()==Landmark.LEFT_MOUTH)
            {
                lx = (translateX(landmark.getPosition().x));
                ly = (translateY(landmark.getPosition().y));
                canvas.drawCircle(lx, ly, 10, mIdPaint);
            }
            if(landmark.getType()==Landmark.BOTTOM_MOUTH)
            {
                bx = (translateX(landmark.getPosition().x));
                by = (translateY(landmark.getPosition().y));
                canvas.drawCircle(bx, by, 10, mIdPaint);
            }
            if(landmark.getType()==Landmark.RIGHT_MOUTH)
            {
                rx = (translateX(landmark.getPosition().x));
                ry = (translateY(landmark.getPosition().y));
                canvas.drawCircle(rx, ry, 10, mIdPaint);
            }


        }
        msg=text;
        ratio=Speaker_Distinguisher(lx,ly,rx,ry,bx,by);
        canvas.drawText(Double.toString(ratio), bx+100,by+300, mTextPaint);
        if(ratio>0.26) {

            if (msg != null) {
                if (msg.length() > 0) {

                    /*estl=msg.length()*25;
                    maxl=375;
                    if(estl<maxl)
                    {
                        bubb.right=bubb.left+estl;
                        canvas.drawBitmap(_scratch, null, bubb, null);
                        canvas.drawText(msg, bubb.left + 50, bubb.centerY(), mTextPaint);
                    }
                    else
                    {
                       double lno;
                        double temp=estl/maxl;
                        lno=Math.ceil(temp);
                        bubb.bottom= (float) (bubb.bottom+(lno*25));
                        char[] tmsg=msg.toCharArray();

                        canvas.drawBitmap(_scratch, null, bubb, null);
                        char[] tempc = new char[15];
                        for(int i=0;i<lno;i++)
                        {
                            msg.getChars(15*i,15*i+14,tempc,0);
                            *//*for(int j=0;j<15&&(tmsg[15*i+j]!='\0');j++)
                            {
                                tempc[j]=tmsg[15*i+j];
                            }*//*
                            canvas.drawText(tempc.toString(), bubb.left + 50, (float) (bubb.centerY()-50+(lno*150)), mTextPaint);
                        }

                    }*/
                    canvas.drawBitmap(_scratch, null, bubb, null);
                    canvas.drawText(msg, bubb.left + 20, bubb.centerY(), mTextPaint);
                    msg=null;
                    ratio=0;
                    bx = 0; by = 0; lx = 0; ly = 0; rx = 0; ry = 0;
                }
            }
        }
        else
        {
            if (msg != null) {
                canvas.drawText("other : " + msg, 20, 100, mTextPaint2);
                msg = null;
            }
        }


    }
}


