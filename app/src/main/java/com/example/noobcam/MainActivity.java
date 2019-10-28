package com.example.noobcam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import 	android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.security.Permission;
import java.security.Policy;
import java.util.List;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback2{
     Camera camera;
     SurfaceView surfaceView;
     SurfaceHolder surfaceHolder;
     Button capture;
     Camera.PictureCallback jpegcallbcak;
     int CAMERA_REQUEST_CODE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        surfaceView=findViewById(R.id.surfaceView);
        surfaceHolder=surfaceView.getHolder();
        capture=findViewById(R.id.capture);
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.CAMERA},CAMERA_REQUEST_CODE);
        }
        else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(surfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImages();
            }
        });
        jpegcallbcak=new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                Intent intent=new Intent(MainActivity.this,ShowPicActivity.class);
                intent.putExtra("capture",data);
                startActivity(intent);
                return;
            }
        };
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        camera=Camera.open();
        Parameters parameters;
        parameters=camera.getParameters();
        camera.setDisplayOrientation(90);
        parameters.setPreviewFrameRate(30);
        parameters.setFocusMode(Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);

        Camera.Size bestsize=null;
        List<Camera.Size> sizeList=camera.getParameters().getSupportedPreviewSizes();
        bestsize=sizeList.get(0);
        for(int i=1;i<sizeList.size();i++)
        {
            if((sizeList.get(i).width*sizeList.get(i).height)>(bestsize.width*bestsize.height))
            {
                bestsize=sizeList.get(i);
            }
        }
        parameters.setPreviewSize(bestsize.width,bestsize.height);
        camera.setParameters(parameters);

        try {
            camera.setPreviewDisplay(surfaceHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.startPreview();

    }

    private void captureImages() {
        camera.takePicture(null,null,jpegcallbcak);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }


    @Override
    public void surfaceRedrawNeeded(SurfaceHolder holder) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case 1:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    surfaceHolder.addCallback(this);
                    surfaceHolder.setType(surfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
                }
                else
                {
                    Toast.makeText(this, "please grant the permissions", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
