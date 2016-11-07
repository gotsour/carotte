package com.example.duduf.version0;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by Duduf on 27/10/2016.
 */
public class Decryptage {
    protected int sizeBits;
    protected int originalWidth;
    protected int originalHeight;
    protected volatile Bitmap imageSortie;
    protected Bitmap imageEntree;
    protected volatile boolean end;

    public Decryptage(){
    }

    public Decryptage(int sizeBits, Bitmap imageEntree, int originalWidth, int originalHeight){
        this.sizeBits=sizeBits;
        this.imageEntree=imageEntree;
        this.originalWidth=originalWidth;
        this.originalHeight=originalHeight;
        end=false;
    }

    public Bitmap calcul() {
        int cpt=0;
        byte tabBits[]=new byte[sizeBits];


        for(int i=0;i<imageEntree.getWidth() && cpt<sizeBits;i++){
            for(int j=0;j<imageEntree.getHeight() &&cpt<sizeBits;j++){
                if((i*imageEntree.getWidth()+j)%10==0 &&cpt+8<sizeBits) {
                    //recuperation du pixel
                    System.out.println("decrypt pixel : "+i+"/"+j);
                    int pixel = imageEntree.getPixel(i, j);
                    /*tabBits=getDataDecrypt(tabBits,Color.alpha(pixel),cpt);
                    cpt+=2;*/
                    tabBits=getDataDecrypt(tabBits, Color.red(pixel),cpt);
                    cpt+=2;
                    tabBits=getDataDecrypt(tabBits,Color.green(pixel),cpt);
                    cpt+=2;
                    tabBits=getDataDecrypt(tabBits,Color.blue(pixel),cpt);
                    cpt+=2;

                }
            }
        }

        //creation de la nouvelle image
        imageSortie = Bitmap.createBitmap(originalWidth, originalHeight, Bitmap.Config.ARGB_8888);
        cpt=0;
        for(int i=0;i<originalWidth && cpt<sizeBits;i++){
            for(int j=0;j<originalHeight && cpt<sizeBits;j++) {
                if(cpt+24<sizeBits) {
                  /*  int a = byteToEntier(tabBits,cpt);
                    cpt += 8;*/
                    System.out.println("decrypt2 pixel : "+i+"/"+j);
                    int r =  byteToEntier(tabBits,cpt);
                    cpt += 8;
                    int g = byteToEntier(tabBits,cpt);
                    cpt += 8;
                    int b = byteToEntier(tabBits,cpt);
                    cpt += 8;
                    imageSortie.setPixel(i, j, Color.rgb(r, g, b));
                    //imageSortie.setPixel(i, j, Color.argb(a, r, g, b));
                }
            }
        }
        return  imageSortie;
    }

    //prend les 2 dernier bits
    public byte[] getDataDecrypt(byte tab[],int entier, int cpt){
        String binaire=Integer.toString(entier,2);
        //optimiser enlever appel methode
        binaire=checkLength(binaire);
        //prend les 2 bit a droite
        for (int i = 6; i <8; i++) {
            tab[cpt] =Byte.parseByte(binaire.substring(i,i+1));
           cpt++;
        }

        return tab;
    }

    public int byteToEntier(byte[] tab,int indice){
        String chaine="";
        for(int i=0;i<8;i++){
            chaine=Byte.toString(tab[indice+i])+chaine;
        }
        return Integer.valueOf(chaine.replaceAll("\\D+",""),2);
    }

    public String checkLength(String chaine){
        String sortie=chaine;
        for (int i = 8-chaine.length(); i >0; i--) {
            sortie="0"+sortie;
        }

        return sortie;
    }
}
