package com.rx1226.periscopelayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.rx1226.periscopelayout.PeriscopeLayout;
import com.github.rx1226.periscopelayout.Position;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final PeriscopeLayout periscopeLayout = findViewById(R.id.periscope);
        periscopeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                periscopeLayout.addHeart();
            }
        });

        // Set customer icon
        periscopeLayout.setDrawables(getResources().getDrawable(R.mipmap.ic_launcher),
                getResources().getDrawable(R.mipmap.ic_launcher_round));

//        periscopeLayout.setPosition(Position.LEFT);

        // auto play
//        periscopeLayout.startAuto();
    }
}
