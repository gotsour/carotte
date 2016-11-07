package com.example.duduf.version0;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.test.mock.MockContext;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CryptageTest  {

   /* @Mock
    Context mMockContext;
    @Mock
    protected static Bitmap imageSortie;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        imageSortie = Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888);
        for(int i=0;i<2;i++){
            for(int j=0;j<2;j++){
                imageSortie.setPixel(i,j, Color.BLUE);
            }

        }
        int pixel=imageSortie.getPixel(0,0);
        System.out.println("sortie bleu  A:"+Color.alpha(pixel)+"R:"+Color.red(pixel)+" G:"+Color.green(pixel)+" B:"+Color.blue(pixel));
    }*/
   static Cryptage c;
    static Decryptage d;
   @BeforeClass
   public static void setUpBeforeClass() throws Exception {
       c=new Cryptage();
       d=new Decryptage();
   }

   /* @Test
    public void testCheckLength8() throws Exception {
        assertEquals("00011111", c.checkLength8("11111"));
    }
    @Test
    public void testGetData() throws Exception {
        byte tab[]=new byte[16];
        c.getData(tab,255,0);
        assertEquals("11111111", tab[0]+""+tab[1]+""+tab[2]+""+tab[3]+""+tab[4]+""+tab[5]+""+tab[6]+""+tab[7]);
        c.getData(tab,128,8);
        assertEquals("10000000", tab[8]+""+tab[9]+""+tab[10]+""+tab[11]+""+tab[12]+""+tab[13]+""+tab[14]+""+tab[15]);
    }*/


    //decryptage
   /* @Test
    public void testGetDataDecrypt() throws Exception {
        byte tab[]=new byte[8];

        d.getDataDecrypt(tab,128,0);
        assertEquals("00", tab[0]+""+tab[1]);
        d.getDataDecrypt(tab,255,2);
        assertEquals("11", tab[2]+""+tab[3]);
        d.getDataDecrypt(tab,1,4);
        assertEquals("01", tab[4]+""+tab[5]);

    }*/

   /* @Test
    public void testByteToEntier() throws Exception {
        byte tab[]=new byte[8];
        c.getData(tab,61,0);
        d.getDataDecrypt(tab,128,0);//00
        d.getDataDecrypt(tab,255,2);//11
        d.getDataDecrypt(tab,255,4);//11
        d.getDataDecrypt(tab,1,6);//01
        //00111101 =>61
        assertEquals(61,d.byteToEntier(tab,0));

    }*/

    @Test
    public void testByteToEntier2() throws Exception {
        byte tab[]=new byte[8];
        c.getData(tab,132,0);

       // assertEquals("10000000",c.checkLength(Integer.toString(128,2))+tab[0]+tab[1]);
       // assertEquals("11111111",c.checkLength(Integer.toString(255,2))+tab[2]+tab[3]);
        for(int i=0;i<tab.length;i++){
            System.out.print(tab[i]);
        }
        System.out.println();
        int a=Integer.valueOf(c.checkLength(Integer.toString(255,2))+tab[0]+""+tab[1],2);
        //System.out.println(c.checkLength(Integer.toString(255,2)));
        assertEquals(252,a);
        assertEquals("00",tab[0]+""+tab[1]);
        int r=Integer.valueOf(c.checkLength(Integer.toString(3,2))+tab[2]+""+tab[3],2);
        assertEquals(2,r);
        assertEquals("10",tab[2]+""+tab[3]);
        int g=Integer.valueOf(c.checkLength(Integer.toString(22,2))+tab[4]+""+tab[5],2);
        assertEquals(20,g);
        assertEquals("00",tab[4]+""+tab[5]);
        int b=Integer.valueOf(c.checkLength(Integer.toString(12,2))+tab[6]+""+tab[7],2);
        assertEquals(13,b);
        assertEquals("01",tab[6]+""+tab[7]);

        tab=new byte[8];
        d.getDataDecrypt(tab,a,0);//10
        assertEquals("00",tab[0]+""+tab[1]);
        d.getDataDecrypt(tab,r,2);//11
        //assertEquals("10",tab[2]+""+tab[3]);
        d.getDataDecrypt(tab,g,4);//11
        d.getDataDecrypt(tab,b,6);//01
        //00111101 =>61
        //10000100 132
        for(int i=0;i<tab.length;i++){
            System.out.print(tab[i]);
        }
        System.out.println();
        System.out.println(d.byteToEntier(tab,0));
        assertEquals(132,d.byteToEntier(tab,0));

    }

}
