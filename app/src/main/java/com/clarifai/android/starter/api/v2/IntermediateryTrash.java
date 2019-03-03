package com.clarifai.android.starter.api.v2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.clarifai.android.starter.api.v2.activity.RecognizeConceptsActivity;

public class IntermediateryTrash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intermediatery_trash);

        Thread t = new Thread() {
            public void run () {
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Intent i = new Intent(getApplicationContext(), RecognizeConceptsActivity.class);
                startActivity(i);
            }
        };
        t.start();
    }
}
