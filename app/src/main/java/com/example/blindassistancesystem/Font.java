package com.example.blindassistancesystem;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

public class Font extends AppCompatActivity{

    RadioGroup radioGroup;
    RadioButton small, medium, large;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_font);

        sharedPreferences = getSharedPreferences("pref", 0);
        int fontSP = sharedPreferences.getInt("fontSP",4);

        editor = sharedPreferences.edit();
        small=findViewById(R.id.small);
        medium=findViewById(R.id.medium);
        large=findViewById(R.id.large);
        if(fontSP==1){
            small.setChecked(true);
        }
        else if (fontSP==2){
            medium.setChecked(true);
        }
        else if(fontSP==3){
            large.setChecked(true);
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.small){
                    editor.putInt("fontSP",1);
                }
                else if(i == R.id.medium){
                    editor.putInt("fontSP",2);
                }
                else if(i == R.id.large){
                    editor.putInt("fontsp",3);
                }
                editor.commit();
            }
        });
    }


}