package com.example.blindassistancesystem;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

public class Fontsize extends AppCompatActivity {
    RadioGroup radioGroup;
    RadioButton small, medium, large;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fontsize);
        sharedPreferences = getSharedPreferences("pref", 0);
        int fontSP = sharedPreferences.getInt("fontSP",4);

        editor = sharedPreferences.edit();
        small=findViewById(R.id.small);
        medium=findViewById(R.id.medium);
        large=findViewById(R.id.large);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.small){
                    editor.putInt("fontSP",1);
                    editor.apply();
                    small.setChecked(true);
                }
                else if(i == R.id.medium){
                    editor.putInt("fontSP",2);
                    editor.apply();
                    medium.setChecked(true);
                }
                else if(i == R.id.large){
                    editor.putInt("fontSP",3);
                    editor.apply();
                    large.setChecked(true);
                }

            }
        });
    }


}