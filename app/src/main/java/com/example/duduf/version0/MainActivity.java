package com.example.duduf.version0;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static int LOAD_IMG_MODELE = 1;
    private static int LOAD_IMG_SECRET = 2;
    ImageView largeImage;
    String imgDecodableString;
    long tailleMaxImage;
    Bitmap imageCryptee;
    int gallerieWidth;
    int gallerieHeight;
    int sizeBits;
    Bitmap nouvelleImage;
    Bitmap imageModel;
    Decryptage d;
    TextView tw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.image_1);
        //bm = b.copy(Bitmap.Config.ARGB_8888, true);
        tw = (TextView) findViewById(R.id.textView);
        largeImage = (ImageView) findViewById(R.id.imageView);


        /***********************************************************************
         ******* OUVERTURE GALERIE AJOUT MODEL ********************
         ***********************************************************************/
        Button buttonModel = (Button) findViewById(R.id.buttonModele);
        buttonModel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                // Create intent to Open Image applications like Gallery, Google Photos
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, LOAD_IMG_MODELE);
            }

        });
        /***********************************************************************
         ******* OUVERTURE GALERIE AJOUT SECRET ********************
         ***********************************************************************/

        Button buttonSecret = (Button) findViewById(R.id.buttonSecret);
        buttonSecret.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                // Create intent to Open Image applications like Gallery, Google Photos
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, LOAD_IMG_SECRET);
            }

        });

        //bouton pour le decryptage
        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(onClickDecrypt);

    }

    /***********************************************************************
     * ****** DECRYPTAGE ET CREATION DE LA NOUVELLE IMAGE ********************
     ***********************************************************************/

    public View.OnClickListener onClickDecrypt = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            d = new Decryptage(sizeBits, imageCryptee, gallerieWidth, gallerieHeight);
            new Thread(new Runnable() {
                public void run() {
                    final Bitmap bitmap = d.calcul();
                    largeImage.post(new Runnable() {
                        public void run() {
                            largeImage.setImageBitmap(bitmap);
                            tw.setText(" largeur : " + bitmap.getWidth() + " hauteur : " + bitmap.getHeight());
                        }
                    });
                }
            }).start();
        }
    };



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /***************************************************************************
         * **************** RETOUR DE LA GALLERIE : LANCEMENT CRYPTAGE *************
         ***************************************************************************/

        if (requestCode == LOAD_IMG_SECRET && resultCode == RESULT_OK && null != data) {
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

            Bitmap imageGallerie = BitmapFactory.decodeFile(imgDecodableString);
            gallerieWidth = imageGallerie.getWidth();
            gallerieHeight = imageGallerie.getHeight();

            if (imageGallerie.getHeight() * imageGallerie.getWidth() > tailleMaxImage) {
                tw.setText("Image trop lourde pour etre achée dans l'image actuelle");
                Toast.makeText(this, "Image trop lourde pour etre achée dans l'image actuelle", Toast.LENGTH_LONG).show();
            } else {
                //sizeBits=
                Cryptage c = new Cryptage(imageModel, imageGallerie);
                imageCryptee=c.calcul();
                sizeBits = c.getSizeBits();
                largeImage.setImageBitmap(imageCryptee);
            }
        } else {
            Toast.makeText(this, "You haven't picked Image",
                    Toast.LENGTH_LONG).show();
        }

        /***************************************************************************
         * **************** RETOUR DE LA GALLERIE : AJOUT IMG MODEL *************
         ***************************************************************************/

        if (requestCode == LOAD_IMG_MODELE && resultCode == RESULT_OK && null != data) {
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
           // BitmapFactory.Options options = new BitmapFactory.Options();
           // options.inJustDecodeBounds = true;
            Bitmap b = BitmapFactory.decodeFile(imgDecodableString);
            imageModel = b.copy(Bitmap.Config.ARGB_8888, true);
            largeImage.setImageBitmap(imageModel);
            tailleMaxImage=imageModel.getWidth()*imageModel.getHeight()/30;
            tw.setText("Dimension : hauteur ="+imageModel.getWidth()+" largeur ="+imageModel.getHeight()+" \n Taille max pixel :");


        }
    }

}
