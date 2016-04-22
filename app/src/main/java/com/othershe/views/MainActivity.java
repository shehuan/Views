package com.othershe.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.othershe.views.view.CircleView;

public class MainActivity extends AppCompatActivity {
    private CircleView circleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        circleView = (CircleView) findViewById(R.id.id_circle_view);

        new Thread(circleView).start();
    }
}
