package com.othershe.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.othershe.views.view.CircleView;
import com.othershe.views.view.SpeedControl;
import com.othershe.views.view.WaveView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        CircleView circleView = (CircleView) findViewById(R.id.id_circle_view);
//        new Thread(circleView).start();

//        SpeedControl speedControl = (SpeedControl) findViewById(R.id.id_speed_control);
//        speedControl.setType(1);
//        new Thread(speedControl).start();

//        WaveView waveView = (WaveView) findViewById(R.id.wave_view);
//        waveView.start();
    }
}
