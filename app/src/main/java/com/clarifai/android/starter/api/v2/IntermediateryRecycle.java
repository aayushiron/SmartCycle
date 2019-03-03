package com.clarifai.android.starter.api.v2;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.clarifai.android.starter.api.v2.activity.RecognizeConceptsActivity;

public class IntermediateryRecycle extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intermediatery_recycle);

        Thread t = new Thread() {
            public void run () {
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("https://www.google.com/maps/dir/37.3875089,-121.9639485/J+%26+B+Enterprises,+1650+Russell+Ave,+Santa+Clara,+CA+95054/@37.3873844,-121.9626202,17z/data=!3m1!4b1!4m16!1m6!3m5!1s0x0:0xab871ec150ff24d8!2sJ+%26+B+Enterprises!8m2!3d37.3868418!4d-121.9566804!4m8!1m1!4e1!1m5!1m1!1s0x808fc99ace703ce5:0xab871ec150ff24d8!2m2!1d-121.956681!2d37.38684"));
                startActivity(intent);
            }
        };
        t.start();
    }
}
