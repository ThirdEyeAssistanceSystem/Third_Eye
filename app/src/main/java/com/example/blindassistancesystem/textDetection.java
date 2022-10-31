package com.example.blindassistancesystem;

import static com.example.blindassistancesystem.Dashboard.REQUEST_CODE;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.util.List;
import java.util.Locale;

public class textDetection extends AppCompatActivity implements GestureDetector.OnGestureListener {

    private float x1, x2, y1, y2;
    private static int MIN_DISTANCE=150;
    private GestureDetector gestureDetector;



    private Button captureButton, detectButton;
    private ImageView imageView;
    private TextView textView;
    private Bitmap imageBitMap;
    static final  int REQUEST_IMAGE_CAPTURE=1;
    TextToSpeech textToSpeech;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_detection);
        if(REQUEST_CODE==1) {
            instruction();
        }

        this.gestureDetector= new GestureDetector(textDetection.this, this);



        captureButton = findViewById(R.id.capture);
        detectButton= findViewById(R.id.detect);
        imageView=findViewById(R.id.TextImage);
        textView=findViewById(R.id.detected_text);

        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
                textView.setText("");
            }
        });

        detectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detectTextFromImage();
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

                    if(Math.abs(valueY)>MIN_DISTANCE){
                        if(y1>y2){
                            captureButton.performClick();
                        }
                        else
                        {
                            detectButton.performClick();
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
                    String s = "Swipe up to capture image, " +
                            "Swipe down to detect";
                    textToSpeech.setSpeechRate(1.0f);
                    int speech = textToSpeech.speak(s,TextToSpeech.QUEUE_FLUSH,null);
                }
            }
        });
    }

    private void detectTextFromImage() {
        FirebaseVisionImage firebaseVisionImage= FirebaseVisionImage.fromBitmap(imageBitMap);
        FirebaseVisionTextRecognizer firebaseVisionTextDetector= FirebaseVision.getInstance().getOnDeviceTextRecognizer();
        firebaseVisionTextDetector.processImage(firebaseVisionImage).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                displayText(firebaseVisionText);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(textDetection.this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                String text = "No text found";
                texttospeech(text);
            }
        });
    }

    private void displayText(FirebaseVisionText firebaseVisionText) {
        List<FirebaseVisionText.TextBlock> blockList = firebaseVisionText.getTextBlocks();
        if(blockList.size()==0){
            Toast.makeText(this, "No Text Image Found", Toast.LENGTH_SHORT).show();
            String text = "No text found";
            texttospeech(text);
        }
        else{
            for (FirebaseVisionText.TextBlock block:firebaseVisionText.getTextBlocks()){

                String  text = block.getText();
                textView.setText(text);
                texttospeech(text);
            }
        }
    }

    private void texttospeech(String s) {
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i==TextToSpeech.SUCCESS){
                    int lang = textToSpeech.setLanguage(Locale.ENGLISH);
                    textToSpeech.setSpeechRate(1.0f);
                    int speech = textToSpeech.speak(s,TextToSpeech.QUEUE_FLUSH,null);
                }
            }
        });
    }

    public void takePicture(){
        Intent takepictureIntent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takepictureIntent.resolveActivity(getPackageManager())!= null) {
            startActivityForResult(takepictureIntent,REQUEST_IMAGE_CAPTURE);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            imageBitMap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitMap);
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