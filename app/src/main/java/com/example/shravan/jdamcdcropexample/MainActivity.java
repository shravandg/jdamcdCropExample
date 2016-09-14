package com.example.shravan.jdamcdcropexample;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.soundcloud.android.crop.Crop;

import java.io.File;

public class MainActivity extends Activity {
    private static final String TAG = "Arunachala";
    private ImageView resultView;
    static final int REQUEST_IMAGE_CAPTURE = 10;
    static final int PICK_IMAGE = 100;
    Uri imageUri;
    TouchImageView myTVF;
    //ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // iv= (ImageView)findViewById(R.id.myIV);
        myTVF = (TouchImageView)findViewById(R.id.img);
        myTVF.setImageBitmap(null);
    }


    public void clicked(View v){
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, REQUEST_IMAGE_CAPTURE);
    }

    public void picked(View v){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            print("Camera called");
            beginCrop(result.getData());
            print("Crop done");
        }
        else if (requestCode == PICK_IMAGE  && resultCode == Activity.RESULT_OK) {
            print("gallery called");
            beginCrop(result.getData());
            print("crop done");
        }
        else if (requestCode == Crop.REQUEST_CROP) {
            print("Crop.REQUEST_CROP called");
            handleCrop(resultCode, result);
            print("crop handlled");
        }
    }

    public void clear(View v){
        myTVF.setImageDrawable(null);
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            print("result is ok in handleCrop");
            myTVF.setImageURI(Crop.getOutput(result));
            print("myTVF set");
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void print(String s){
        Log.d(TAG, s);
    }


}