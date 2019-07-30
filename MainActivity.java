package com.example.graphi_v;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public  class MainActivity extends AppCompatActivity
{   //private static final String TAG="MainActivity";
    SurfaceView cameraview;

    GraphView mGraph;
    CameraSource cameraSource;
    TextView textView;
    final int RequestCameraPermissionID = 1001;
    LineGraphSeries<DataPoint> series;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            case RequestCameraPermissionID:
            {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if (ActivityCompat.checkSelfPermission(this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                    {
                        return;
                    }
                    try {
                        cameraSource.start(cameraview.getHolder());
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    { super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //SurfaceView cameraview;
        cameraview = (SurfaceView) findViewById(R.id.surface_view);
        mGraph=findViewById(R.id.graph);
        series= new LineGraphSeries<DataPoint>();
        textView =  findViewById(R.id.text_view);
        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        if (!textRecognizer.isOperational())
        {
            Log.w("MainActivity", "Detector dependencies not available");
        }
        else
        { cameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setRequestedFps(2.0f)
                    .setAutoFocusEnabled(true)
                    .build();

            cameraview.getHolder().addCallback(new SurfaceHolder.Callback()
            {
                @Override
                public void surfaceCreated(SurfaceHolder surfaceHolder)
                {

                    try {
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    RequestCameraPermissionID);
                            return;
                        }
                        cameraSource.start(cameraview.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder surfaceHolder)
                { cameraSource.stop();
                }
            });

            textRecognizer.setProcessor(new Detector.Processor<TextBlock>()
            {
                @Override
                public void release() {

                }

                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections)
                {

                    final SparseArray<TextBlock> items = detections.getDetectedItems();
                    if (items.size() != 0)
                    {
                        textView.post(new Runnable() {
                            @Override
                            public void run()
                            {

                                DataSample data=new DataSample();
                                String[] Arr=new String[50];
                                List<String[]> dataList=new ArrayList<String[]>();
                                StringBuilder stringBuilder = new StringBuilder();
                                mGraph.addSeries(series);
                                String Lastitem="11";
                                for(int i=0; i<items.size()-1; ++i)
                                { String[] item = items.valueAt(i).getValue().split(" ");
                                    Log.d("MAINACTIVITY", item[0]);
                                   // stringBuilder.append(item.getValue());
                                    TextBlock item1 = items.valueAt(i);
                                    stringBuilder.append(item1.getValue());
                                    stringBuilder.append("\n");

                                    //Log.d("MAINACTIVITY", item[1]);

                                                textView.setText(item1.getValue());
                                                try {

                                                    data.set(Integer.valueOf((item1.getValue())) , Integer.valueOf(Lastitem));
                                                    DataPoint point = data.get();
                                                    try {
                                                        series.appendData(point, false, 7);
                                                    } catch (IllegalArgumentException e) {
                                                        e.printStackTrace();
                                                    }
                                                } catch (NumberFormatException e) {
                                                    e.printStackTrace();
                                                }
                                                    Lastitem = item1.getValue();

                                        createLineGraph(series);

                                    }
                                }
                        });
                    }
                }

                private void createLineGraph(LineGraphSeries<DataPoint> series)
                {
                        try
                        {
                            mGraph.addSeries(series);
                            Viewport viewport = mGraph.getViewport();
                            viewport.setYAxisBoundsManual(true);
                            viewport.setMinY(0);
                            viewport.setMaxY(100);
                            viewport.setMaxX(100);
                            viewport.setMinX(0);
                            viewport.setScrollable(true);
                            mGraph.getViewport().setYAxisBoundsManual(true);
                            mGraph.getViewport().setXAxisBoundsManual(true);
                        }
                        catch (NumberFormatException e)
                        {
                            e.printStackTrace();
                        }
                }

            });
        }
    }


}

