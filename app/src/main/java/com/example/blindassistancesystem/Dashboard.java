package com.example.blindassistancesystem;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.telephony.SmsManager;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Dashboard extends AppCompatActivity implements GestureDetector.OnGestureListener {

    Button CameraButton, Place;
    TextToSpeech textToSpeech;
    ImageButton Call, Emergency, Setting;
    Button button;
    String message;

    private float x1, x2, y1, y2;
    private static int MIN_DISTANCE = 150;
    public static int REQUEST_CODE;
    private GestureDetector gestureDetector;

    FusedLocationProviderClient fusedLocationProviderClient;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        if(REQUEST_CODE==1) {
            instruction();
        }
        this.gestureDetector = new GestureDetector(Dashboard.this, this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(Dashboard.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(Dashboard.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

        )
        {
            getlocation();
        } else {
            ActivityCompat.requestPermissions(Dashboard.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 44);
        }

        ActivityCompat.requestPermissions(Dashboard.this, new String[]{Manifest.permission.SEND_SMS},100);


        Call = (ImageButton) findViewById(R.id.CallButton);
        Setting = (ImageButton) findViewById(R.id.Settings);
        Place=(Button) findViewById(R.id.places);
        Place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this,Maps.class);
                startActivity(intent);
            }
        });
        Emergency = (ImageButton) findViewById(R.id.AlertButton);

        //address = (TextView) findViewById(R.id.address);


        Emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendtext();
            }
        });

        CameraButton = (Button) findViewById(R.id.TextButton);
        CameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, textDetection.class);
                startActivity(intent);
            }
        });
        Setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, Settings.class);
                startActivity(intent);
            }
        });


    }

    private void sendtext() {
//        ArrayList<Contact> arrayList=new ArrayList<>();
//        DatabaseHelper helper=new DatabaseHelper(getApplicationContext());
//        arrayList=helper.fetchAlldata();
//        for(int i = 0;i< arrayList.size();i++) {
//            Contact contact=arrayList.get(i);
            String Mobile="01748703341";
            Toast.makeText(Dashboard.this,Mobile,Toast.LENGTH_LONG).show();
            String text = message;

            Intent intent = new Intent(getApplicationContext(), Dashboard.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
            SmsManager smsManager = SmsManager.getDefault();
            if (ActivityCompat.checkSelfPermission(Dashboard.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                smsManager.sendTextMessage(Mobile, null, text, pendingIntent, null);
                Toast.makeText(Dashboard.this, "Message sent", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(Dashboard.this, "Nope", Toast.LENGTH_LONG).show();
            }
        //

       // finish();
       //helper.close();
    }

    @SuppressLint("MissingPermission")
    private void getlocation() {
//        address.setText("hello");
//
       fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
           @Override
           public void onComplete(@NonNull Task<Location> task) {
               Location location = task.getResult();
               if (location != null) {

                   try {
                       Geocoder geocoder = new Geocoder(Dashboard.this, Locale.getDefault());
                       List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);
                    message="Send help, I am in danger, I am currently in \n"+ "Locality: "+ addresses.get(0).getLocality()+","+addresses.get(0).getSubLocality()+","+ "\nAddress: " +addresses.get(0).getAddressLine(0);
                  //address.setText(message);
                    String phone = "01816579009";
                   }
                   catch (IOException e) {
                       e.printStackTrace();
                   }

               }

           }
       });
    }


    @Override
        public boolean onTouchEvent(MotionEvent event) {

            gestureDetector.onTouchEvent(event);
            switch (event.getAction()){

                case MotionEvent.ACTION_DOWN:
                    x1=event.getX();
                    y1=event.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    x2= event.getX();
                    y2= event.getY();

                    float valueX= x2-x1;
                    float valueY=y2-y1;
                    if(Math.abs(valueY)>MIN_DISTANCE) {
                        if(y1>y2){
                            voiceautomation();
                        }
                    }
            }
        return super.onTouchEvent(event);
    }

    private void instruction() {
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i==TextToSpeech.SUCCESS){
                    int lang = textToSpeech.setLanguage(Locale.ENGLISH);
                    String s = "Swipe up to open voice command";
                    textToSpeech.setSpeechRate(1.0f);
                    int speech = textToSpeech.speak(s,TextToSpeech.QUEUE_FLUSH,null);
                }
            }
        });
    };


    private void voiceautomation() {
        Intent voice = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        voice.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        voice.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault());
        voice.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "What do you want...");
        startActivityForResult(voice, 1);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(event.getKeyCode()==KeyEvent.KEYCODE_POWER){
            String Mobile="+8801748703341";
            String text=message;
            Intent intent=new Intent(getApplicationContext(),Dashboard.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent,0);
            SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(Mobile,null,text,pendingIntent,null);
                Toast.makeText(Dashboard.this, "Message sent", Toast.LENGTH_LONG).show();

        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data !=null){
            ArrayList<String> arrayList = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (arrayList.get(0).toString().equals("read")){
                Intent intent = new Intent(Dashboard.this, textDetection.class);
                //Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivity(intent);
            }
            else if (arrayList.get(0).toString().equals("settings")){
                Intent intent = new Intent(Dashboard.this, Settings.class);
                //Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivity(intent);
            }
        }

    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }


}