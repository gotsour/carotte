package com.example.duduf.version0;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by Duduf on 27/10/2016.
 */
public class DecryptageR implements Runnable {
    protected int sizeBits;
    protected int originalWidth;
    protected int originalHeight;
    protected volatile Bitmap imageSortie;
    protected Bitmap imageEntree;

    public DecryptageR(int sizeBits, Bitmap imageEntree, int originalWidth, int originalHeight){
        this.sizeBits=sizeBits;
        this.imageEntree=imageEntree;
        this.originalWidth=originalWidth;
        this.originalHeight=originalHeight;
    }
    @Override
    public void run() {
        int cpt=0;
        byte tabBits[]=new byte[sizeBits];
        tabBits[0]=2;
        tabBits[1]=2;

        for(int i=0;i<imageEntree.getWidth() && cpt<sizeBits;i++){
            for(int j=0;j<imageEntree.getHeight() &&cpt<sizeBits;j++){
                if((i*imageEntree.getWidth()+j)%10==0 &&cpt+8<sizeBits) {
                    //recuperation du pixel
                    int pixel = imageEntree.getPixel(i, j);
                    tabBits=getDataDecrypt(tabBits, Color.red(pixel),cpt);
                    cpt+=2;
                    tabBits=getDataDecrypt(tabBits,Color.green(pixel),cpt);
                    cpt+=2;
                    tabBits=getDataDecrypt(tabBits,Color.blue(pixel),cpt);
                    cpt+=2;
                    tabBits=getDataDecrypt(tabBits,Color.alpha(pixel),cpt);
                    cpt+=2;
                }
            }
            //System.out.print("decryptage du pixel : "+i+"/"+bm.getWidth());
        }

        //creation de la nouvelle image
        imageSortie = Bitmap.createBitmap(originalWidth, originalHeight, Bitmap.Config.ARGB_8888);

        for(int i=0;i<originalWidth && cpt<sizeBits;i++){
            for(int j=0;j<originalHeight && cpt<sizeBits;j++) {
                if(cpt+32<sizeBits) {
                    int r =  byteToEntier(tabBits,cpt);
                    cpt += 8;
                    int g = byteToEntier(tabBits,cpt);
                    cpt += 8;
                    int b = byteToEntier(tabBits,cpt);
                    cpt += 8;
                    int a = byteToEntier(tabBits,cpt);
                    cpt += 8;
                    System.out.println("creation pixel :rgba"+r+" "+g+" "+b+" "+a);
                    imageSortie.setPixel(i, j, Color.argb(a, r, g, b));
                }
            }
        }
    }

    //prend les 2 dernier bits
    public byte[] getDataDecrypt(byte tab[],int entier, int cpt){
        String binaire=Integer.toString(entier,2);
        //prend les 2 bit a droite
        for (int i = binaire.length(); i >6; i--) {  // assuming a 32 bit int
            //System.out.println("decryptage entier : "+entier+" binaire: "+binaire+" sortie : "+Byte.parseByte(binaire.substring(i-1,i)));
            tab[cpt] =Byte.parseByte(binaire.substring(i-1,i));
            //System.out.println("bit crypté : "+binaire+" => "+tab[cpt]);
            cpt++;
        }
        return tab;
    }

    public int byteToEntier(byte[] tab,int indice){
        String chaine="";
        for(int i=0;i<8;i++){
            chaine=Byte.toString(tab[indice+i])+chaine;
        }
        return Integer.valueOf(chaine,2);
    }

    public Bitmap getImageSortie() {
        return imageSortie;
    }

}
