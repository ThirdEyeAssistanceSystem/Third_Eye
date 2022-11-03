package com.example.blindassistancesystem;

//import static com.example.blindassistancesystem.Dashboard.ISDISABLE;

import static com.example.blindassistancesystem.Dashboard.REQUEST_CODE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class Settings extends AppCompatActivity {
    RelativeLayout emergency,fontsize;
    //Switch aSwitch;
    RelativeLayout font;

    SwitchCompat switchCompat;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        fontsize=(RelativeLayout) findViewById(R.id.FontSize);
        fontsize.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.this, Fontsize.class);
                startActivity(intent);
            }
        });

        switchCompat=findViewById(R.id.SoundSwitch);
        SharedPreferences sharedPreferences=getSharedPreferences("save",MODE_PRIVATE);
        switchCompat.setChecked(sharedPreferences.getBoolean("value",true));
        emergency=(RelativeLayout)findViewById(R.id.EmergencyCont);
        emergency.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.this,EmergencyContact.class);
                startActivity(intent);
            }
        });

        //aSwitch=(Switch)findViewById(R.id.SoundSwitch);
//        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if(b){
//                    REQUEST_CODE = -1;
//                    aSwitch.setClickable(false);
//                }
//                else{
//                    REQUEST_CODE=1;
//                    aSwitch.setClickable(true);
//                }
//            }
//        });

        switchCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (switchCompat.isChecked())
                {
                    // When switch checked
                    REQUEST_CODE = -1;
                    SharedPreferences.Editor editor=getSharedPreferences("save",MODE_PRIVATE).edit();
                    editor.putBoolean("value",true);
                    editor.apply();
                    switchCompat.setChecked(true);
                }
                else
                {
                    // When switch unchecked
                    REQUEST_CODE=1;
                    SharedPreferences.Editor editor=getSharedPreferences("save",MODE_PRIVATE).edit();
                    editor.putBoolean("value",false);
                    editor.apply();
                    switchCompat.setChecked(false);
                }
            }
        }
        );


    }



}