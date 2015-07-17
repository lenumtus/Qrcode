package com.example.qrcode;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.android.Contents;
 
 
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
 
public class GenerateQRCodeActivity extends Activity implements OnClickListener{
 
 private String LOG_TAG = "GenerateQRCode";
 
 @Override
 public void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.generator);
 
  Button button1 = (Button) findViewById(R.id.button1);
  button1.setOnClickListener(this);
 
 }
 
 public void onClick(View v) {
 
  switch (v.getId()) {
  case R.id.button1:
   EditText qrInput = (EditText) findViewById(R.id.qrInput);
   String qrInputText = qrInput.getText().toString();
   Log.v(LOG_TAG, qrInputText);
 
   //Find screen size
   WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
   Display display = manager.getDefaultDisplay();
   Point point = new Point();
   display.getSize(point);
   int width = point.x;
   int height = point.y;
   int smallerDimension = width < height ? width : height;
   smallerDimension = smallerDimension * 3/4;
 
   //Encode with a QR Code image
   QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(qrInputText, 
             null, 
             Contents.Type.TEXT,  
             BarcodeFormat.QR_CODE.toString(), 
             smallerDimension);
   try {
    Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
    ImageView myImage = (ImageView) findViewById(R.id.imageView1);
    myImage.setImageBitmap(bitmap);
    
    ////////////////// save in my device //////////////
    String filename = String.valueOf(System.currentTimeMillis()) ;
    ContentValues values = new ContentValues();   
    values.put(Images.Media.TITLE, filename);
    values.put(Images.Media.DATE_ADDED, System.currentTimeMillis()); 
    values.put(Images.Media.MIME_TYPE, "image/jpeg");
    Context context = this ;
    Uri uri = context.getContentResolver().insert(Images.Media.EXTERNAL_CONTENT_URI, values);
    try {
      OutputStream outStream = context.getContentResolver().openOutputStream(uri);
      bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outStream);
      outStream.flush();
      outStream.close();
      Log.d("done","done");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    
    
    
    ///////////////////////////////////////////////////
    
    
 
   } catch (WriterException e) {
    e.printStackTrace();
   }
 
 
   break;
 
   // More buttons go here (if any) ...
 
  }
 }
 
}
