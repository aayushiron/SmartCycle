package com.clarifai.android.starter.api.v2.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.clarifai.android.starter.api.v2.R;

import org.w3c.dom.Text;

public class Allergies extends AppCompatActivity {

    String[][] ingredients = {
        {"dough", "cheese", "pepporoni"},
        {"bread", "lettuce", "tomatoes"},
        {"flour", "tomatoes", "egg"}
    };
    String[] items = {"pizza", "burger", "pasta"};

    String[][] allergies = {{"Wheat Allergy", "Dairy Allergy (Cheese)"},
            {"Cheese Allergy", "Dairy Allergy"},
            {"Dairy Allergy", "Wheat Allergy"}};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allergies);

        Intent intent = getIntent();
        String message = intent.getStringExtra(RecognizeConceptsActivity.EXTRA_MESSAGE);

        TextView textView = (TextView)findViewById(R.id.tView);
        textView.setText(message);


        TextView t1 = (TextView)findViewById(R.id.textView2);
        TextView t2 = (TextView)findViewById(R.id.textView3);
        TextView t3 = (TextView)findViewById(R.id.textView4);
        TextView t4 = (TextView)findViewById(R.id.textView6);
        TextView t5 = (TextView)findViewById(R.id.textView7);
        if (message.equals("pizza")){
            t1.setText(ingredients[0][0]);
            t2.setText(ingredients[0][1]);
            t3.setText(ingredients[0][2]);
            t4.setText(allergies[0][0]);
            t5.setText(allergies[0][1]);
        }else if (message.equals("burger")){
            t1.setText(ingredients[1][0]);
            t2.setText(ingredients[1][1]);
            t3.setText(ingredients[1][2]);
            t4.setText(allergies[1][0]);
            t5.setText(allergies[1][1]);
        }else if (message.equals("pasta")){
            t1.setText(ingredients[2][0]);
            t2.setText(ingredients[2][1]);
            t3.setText(ingredients[2][2]);
            t4.setText(allergies[2][0]);
            t5.setText(allergies[2][1]);
        }


    }
}
