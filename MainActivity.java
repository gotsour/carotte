package com.example.duduf.version0;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigInteger;
import java.nio.ByteBuffer;

public class MainActivity extends AppCompatActivity {
    private static int RESULT_LOAD_IMG = 1;
    private static int RESULT_DECRYPT = 2;
    ImageView largeImage;
    String imgDecodableString;
    long tailleMaxImage;
    Bitmap bm;
    int gallerieWidth;
    int gallerieHeight;
    int sizeBits;
    Runnable myRunnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.image_1);
        bm = b.copy(Bitmap.Config.ARGB_8888, true);
        TextView tw=(TextView)findViewById(R.id.textView);

        tailleMaxImage=(bm.getWidth()*bm.getHeight())/10*8;
        tw.setText("Nombre de bits pouvant etre altéré : "+tailleMaxImage +" \n nomnbre max de pixel :"
                +tailleMaxImage/24 + " \n"+b.getWidth()+"/"+b.getHeight());

        largeImage = (ImageView) findViewById(R.id.imageView);
        largeImage.setImageBitmap(bm);

        /***********************************************************************
         ******* OUVERTURE GALERIE + LANCEMENT CRYPTAGE ********************
         ***********************************************************************/
        Button button=(Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                // Create intent to Open Image applications like Gallery, Google Photos
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
            }

        });
        //bouton pour le decryptage
        Button button2=(Button)findViewById(R.id.button2);
        button2.setOnClickListener(onClickDecrypt);

    }

    /***********************************************************************
     ******* DECRYPTAGE ET CREATION DE LA NOUVELLE IMAGE ********************
     ***********************************************************************/

    public View.OnClickListener onClickDecrypt= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new Thread(new Runnable() {
                @Override
                public void run() {

            int cpt=0;
            byte tabBits[]=new byte[sizeBits];

            for(int i=0;i<bm.getWidth() && cpt<sizeBits;i++){
                for(int j=0;j<bm.getHeight() &&cpt<sizeBits;j++){
                    if((i*bm.getWidth()+j)%10==0 &&cpt+8<sizeBits) {
                        //recuperation du pixel
                        System.out.print("decryptage du pixel : "+i+"/"+bm.getWidth());
                        System.out.println(" #### decryptage du pixel : "+j+"/"+bm.getHeight());
                        int pixel = bm.getPixel(i, j);
                        tabBits=getDataDecrypt(tabBits,Color.red(pixel),cpt);
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

            for(int i=0;i<tabBits.length;i++){
                System.out.print(" "+tabBits[i]);
            }
            System.out.println("");

            //creation de la nouvelle image
               /* Bitmap nouvelleImage = Bitmap.createBitmap(gallerieWidth, gallerieHeight, Bitmap.Config.ARGB_8888);

                for(int i=0;i<gallerieWidth && cpt<sizeBits;i++){
                    for(int j=0;j<gallerieHeight && cpt<sizeBits;j++) {
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
                            nouvelleImage.setPixel(i, j, Color.argb(a, r, g, b));
                        }
                    }
                }
               /* Bitmap nouvelleImage = Bitmap.createBitmap(2000, 2000, Bitmap.Config.ARGB_8888);
                for(int i=0;i<50;i++){
                    for(int j=0;j<50;j++) {
                        nouvelleImage.setPixel(i, j, Color.GREEN);
                    }
                }*/
             //largeImage.setImageBitmap(nouvelleImage);
                }
            }).start();
        }
    };


    /***************************************************************************
     * **************** RETOUR DE LA GALLERIE : LANCEMENT CRYPTAGE *************
     ***************************************************************************/
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

       // try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {


                        // Get the Image from data
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        // Get the cursor
                        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                        // Move to first row
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        imgDecodableString = cursor.getString(columnIndex);
                        cursor.close();

                 new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap imageGallerie = BitmapFactory.decodeFile(imgDecodableString);
                        if (imageGallerie.getHeight() * imageGallerie.getWidth() > tailleMaxImage / 32) {
                           // Toast.makeText(this, "Image trop lourde pour etre achée dans l'image actuelle",Toast.LENGTH_LONG).show();
                        } else {

                            //creation tableau des bits à cacher
                            byte tabBits[] = new byte[imageGallerie.getWidth() * imageGallerie.getHeight() * 32];
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

                    }}).start();


            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }

       /* } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }*/

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

    //prend les 2 dernier bits
    public byte[] getDataDecrypt(byte tab[],int entier, int cpt){
        String binaire=Integer.toString(entier,2);
        //prend les 2 bit a droite
        for (int i = binaire.length(); i >6; i--) {  // assuming a 32 bit int
            //System.out.println("decryptage entier : "+entier+" binaire: "+binaire+" sortie : "+Byte.parseByte(binaire.substring(i-1,i)));
            tab[cpt] =Byte.parseByte(binaire.substring(i-1,i));
            System.out.print(+Byte.parseByte(binaire.substring(i-1,i)));
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

    public byte[] decodeData(String chaine, byte tab[],int cpt){
        String sortie=chaine;
        for (int i = 8-chaine.length(); i >0; i--) {  // assuming a 32 bit int
            sortie="0"+sortie;

        }
        //tab[cpt]=sortie.substring(6,8)
        //return sortie.substring(6,8);
        return tab;
    }

    public int byteToEntier(byte[] tab,int indice){
        String chaine="";
        for(int i=0;i<8;i++){
            chaine=Byte.toString(tab[indice+i])+chaine;
        }
        System.out.println("chaine decyptée : "+chaine);
        return Integer.valueOf(chaine,2);
    }
}

/*System.out.println(Byte.parseByte("01100110", 2));
102*/
      /*  int r = (argb)&0xFF;
        int g = (argb>>8)&0xFF;
        int b = (argb>>16)&0xFF;
        int a = (argb>>24)&0xFF;*/