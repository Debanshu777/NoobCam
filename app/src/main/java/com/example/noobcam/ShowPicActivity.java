package com.example.noobcam;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.widget.ImageView;

public class ShowPicActivity extends AppCompatActivity {
    Bitmap decodeBitmap;
    ImageView preview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pic);
        Bundle extras=getIntent().getExtras();
        preview=findViewById(R.id.preview);
        byte[] b=extras.getByteArray("capture");
        if(b!=null)
        {
            decodeBitmap= BitmapFactory.decodeByteArray(b,0,b.length);
            Bitmap rotateBitmap=bitmaprotate(decodeBitmap);
            preview.setImageBitmap(rotateBitmap);
        }
    }

    private Bitmap bitmaprotate(Bitmap decodeBitmap) {
        int w= decodeBitmap.getWidth();
        int h=decodeBitmap.getHeight();
        Matrix matrix=new Matrix();
        matrix.setRotate(90);
        return Bitmap.createBitmap(decodeBitmap,0,0,w,h,matrix,true);
    }
}
