package com.example.duduf.version0;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.provider.Settings;

/**
 * Created by Duduf on 27/10/2016.
 */
public class Cryptage  {
    protected Bitmap imageGallerie;
    protected volatile Bitmap imagefinal;
    protected volatile int sizeBits;

    public Cryptage(){

    }

    public Cryptage(Bitmap imagefinal, Bitmap imageGallerie){
        this.imageGallerie=imageGallerie;
        this.imagefinal=imagefinal;
    }

    public Bitmap calcul() {
        //creation tableau des bits à cacher
        byte tabBits[] = new byte[imageGallerie.getWidth() * imageGallerie.getHeight() * 24];
        sizeBits = tabBits.length;
        int cpt = 0;

        //découpe de l'image à crypter
        for (int i = 0; i < imageGallerie.getWidth(); i++) {
            for (int j = 0; j < imageGallerie.getHeight(); j++) {
                int pixel = imageGallerie.getPixel(i, j);
                /*tabBits = getData(tabBits, Color.alpha(pixel), cpt);
                cpt += 8;*/
                tabBits = getData(tabBits, Color.red(pixel), cpt);
                cpt += 8;
                tabBits = getData(tabBits, Color.green(pixel), cpt);
                cpt += 8;
                tabBits = getData(tabBits, Color.blue(pixel), cpt);
                cpt += 8;
            }
        }
        cpt = 0;

        /*System.out.println("Cryptage");
        for(int i=0;i<200;i++){
            if(i%10==0)
            System.out.println(tabBits[i]);
            else
                System.out.print(tabBits[i]);
        }*/
        ///ajout des bits dans l'image originale
        for (int i = 0; i < imagefinal.getWidth() && cpt < tabBits.length; i++) {
            for (int j = 0; j < imagefinal.getHeight() && cpt < tabBits.length; j++) {
                if ((i * imagefinal.getWidth() + j) % 10 == 0 && cpt + 8 < tabBits.length) {
                    //recuperation du pixel
                    int pixel = imagefinal.getPixel(i, j);
                    //System.out.println("crypt : "+i+" "+j);
                   /* int a = Integer.valueOf(checkLength(Integer.toString(Color.alpha(pixel), 2)) + tabBits[cpt] +""+ tabBits[cpt + 1], 2);
                    cpt += 2;*/
                    int r = Integer.valueOf(checkLength(Integer.toString(Color.red(pixel), 2)) + tabBits[cpt] +""+ tabBits[cpt + 1], 2);
                    cpt += 2;
                    int g = Integer.valueOf(checkLength(Integer.toString(Color.green(pixel), 2)) + tabBits[cpt] +""+ tabBits[cpt + 1], 2);
                    cpt += 2;
                    int b = Integer.valueOf(checkLength(Integer.toString(Color.blue(pixel), 2)) + tabBits[cpt] +""+ tabBits[cpt + 1], 2);
                    cpt += 2;

                    //modification du pixel
                   // imagefinal.setPixel(i, j, Color.argb(a, r, g, b));
                    imagefinal.setPixel(i, j, Color.rgb( r,g,b));
                }
            }
        }
    return imagefinal;
    }

    public byte[] getData(byte tab[],int entier, int cpt){
        String binaire=Integer.toString(entier,2);

        //prend les bits dispo
        for (int i = binaire.length(); i >0; i--) {  // assuming a 32 bit int
            tab[cpt] =Byte.parseByte(binaire.substring(i-1,i));
            cpt++;
        }
        //si la chaine et indefirieur à 8 on met des 0
        for (int i = 8-binaire.length(); i >0; i--) {
            tab[cpt] =0;
            cpt++;
        }
        return tab;
    }

    public String checkLength(String chaine){
        String sortie=chaine;
        for (int i = 8-chaine.length(); i >0; i--) {  // assuming a 32 bit int
            sortie="0"+sortie;
        }

        return sortie.substring(0,6);
    }

    public int getSizeBits() {
        return sizeBits;
    }
}
