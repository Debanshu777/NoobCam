package com.example.noobcam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ShowPicActivity extends AppCompatActivity {
    Bitmap decodeBitmap;
    Button save;
    OutputStream outputStream;
    ImageView preview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pic);
        Bundle extras=getIntent().getExtras();
        preview=findViewById(R.id.preview);
        save=findViewById(R.id.save);
        byte[] b=extras.getByteArray("capture");
        if(b!=null)
        {
            decodeBitmap= BitmapFactory.decodeByteArray(b,0,b.length);
            Bitmap rotateBitmap=bitmaprotate(decodeBitmap);
            preview.setImageBitmap(rotateBitmap);

        }
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapDrawable drawable=(BitmapDrawable)preview.getDrawable();
                Bitmap bitmap=drawable.getBitmap();
                File filepath= Environment.getExternalStorageDirectory();
                File dir= new File(filepath.getAbsolutePath()+"/Demo/");
                dir.mkdir();
                String filename= String.format("%d.jpg",System.currentTimeMillis());
                File file= new File(dir,filename);
                Toast.makeText(ShowPicActivity.this, "image saved", Toast.LENGTH_SHORT).show();
                try {
                    outputStream = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
                    outputStream.flush();
                    outputStream.close();
                    Intent intent= new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent.setData(Uri.fromFile(file));
                    sendBroadcast(intent);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Bitmap bitmaprotate(Bitmap decodeBitmap) {
        int w= decodeBitmap.getWidth();
        int h=decodeBitmap.getHeight();
        Matrix matrix=new Matrix();
        matrix.setRotate(90);
        return Bitmap.createBitmap(decodeBitmap,0,0,w,h,matrix,true);
    }
}
