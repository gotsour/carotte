package com.example.duduf.version0;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by Duduf on 27/10/2016.
 */
public class Cryptage implements Runnable {
    Bitmap imageGallerie;
    Bitmap imagefinal;

    public Cryptage(Bitmap imagefinal, Bitmap imageGallerie){
        this.imageGallerie=imageGallerie;
        this.imagefinal=imagefinal;
    }

    @Override
    public void run() {


        //creation tableau des bits à cacher
        byte tabBits[] = new byte[imageGallerie.getWidth() * imageGallerie.getHeight() * 24];
        gallerieHeight = imageGallerie.getHeight();
        gallerieWidth = imageGallerie.getWidth();
        sizeBits = tabBits.length;
        int cpt = 0;
        for (int i = 0; i < imageGallerie.getWidth(); i++) {
            for (int j = 0; j < imageGallerie.getHeight(); j++) {
                int pixel = imageGallerie.getPixel(i, j);
                tabBits = getData(tabBits, Color.red(pixel), cpt);
                cpt += 8;
                tabBits = getData(tabBits, Color.green(pixel), cpt);
                cpt += 8;
                tabBits = getData(tabBits, Color.blue(pixel), cpt);
                cpt += 8;
                tabBits = getData(tabBits, Color.alpha(pixel), cpt);
                cpt += 8;
            }
        }
        cpt = 0;

        ///ajout des bits dans l'image originale
        for (int i = 0; i < bm.getWidth() && cpt < tabBits.length; i++) {

            for (int j = 0; j < bm.getHeight() && cpt < tabBits.length; j++) {
                if ((i * bm.getWidth() + j) % 10 == 0 && cpt + 8 < tabBits.length) {
                    //recuperation du pixel
                    int pixel = bm.getPixel(i, j);

                    int r = Integer.valueOf(checkLength(Integer.toString(Color.red(pixel), 2)) + tabBits[cpt] + tabBits[cpt + 1], 2)/10;
                    cpt += 2;
                    int g = Integer.valueOf(checkLength(Integer.toString(Color.green(pixel), 2)) + tabBits[cpt] + tabBits[cpt + 1], 2)/10;
                    cpt += 2;
                    int b = Integer.valueOf(checkLength(Integer.toString(Color.blue(pixel), 2)) + tabBits[cpt] + tabBits[cpt + 1], 2)/10;
                    cpt += 2;
                    int a = Integer.valueOf(checkLength(Integer.toString(Color.alpha(pixel), 2)) + tabBits[cpt] + tabBits[cpt + 1], 2)/10;
                    cpt += 2;

                    //modification du pixel
                    bm.setPixel(i, j, Color.argb(a, r, g, b));
                }
            }
        }
        largeImage.setImageBitmap(bm);

    }
    public byte[] getData(byte tab[],int entier, int cpt){
        String binaire=Integer.toString(entier,2);
        // System.out.println("binaire :"+binaire);
        //prend les 8 bits
        for (int i = binaire.length(); i >0; i--) {  // assuming a 32 bit int
            tab[cpt] =Byte.parseByte(binaire.substring(i-1,i));
            //System.out.print(+Byte.parseByte(binaire.substring(i-1,i)));
            cpt++;
        }
        //si la chaine et indefirieur à 8 on met des 0
        for (int i = 8-binaire.length(); i >0; i--) {  // assuming a 32 bit int
            tab[cpt] =0;
            //System.out.print("0");
            cpt++;
        }
        return tab;
    }
}
