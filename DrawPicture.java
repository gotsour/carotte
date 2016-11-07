package com.example.duduf.version0;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Duduf on 14/10/2016.
 */
public class DrawPicture extends View {
    Context context;
    public DrawPicture(Context context) {
        super(context);
        this.context=context;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /*Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.image_1);
        Bitmap b = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        Bitmap originalsize= Bitmap.createScaledBitmap(bm, bm.getWidth(), bm.getHeight(), true);
        for(int i=0;i<100;i++){
            for(int j=0;j<100;j++){
                b.setPixel(i,j, Color.BLUE);
            }
        }
        //canvas.drawBitmap(bm,0,0,null);
        canvas.drawBitmap(originalsize,0,0,null);
        ImageView imgView= (ImageView)findViewById(R.id.imageView);
        imgView.setImageResource(R.drawable.image_1);*/

       /* ImageView largeImage = (ImageView)findViewById(R.id.imageView);
        largeImage.setImageResource(R.drawable.image_1);*/

       /* Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.image_1);
        ImageView largeImage = (ImageView)findViewById(R.id.imageView);
        Display display =((Activity)context).getWindowManager().getDefaultDisplay();
         int displayWidth = display.getWidth();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.drawable.image_1, options);
        int width = options.outWidth;
        if (width > displayWidth) {
        int widthRatio = Math.round((float) width / (float) displayWidth);
        options.inSampleSize = widthRatio;
        }
        options.inJustDecodeBounds = false;
        Bitmap scaledBitmap =  BitmapFactory.decodeResource(getResources(),R.drawable.image_1, options);
        largeImage.setImageBitmap(bm);*/

    }
}
