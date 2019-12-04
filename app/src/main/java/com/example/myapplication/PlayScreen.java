package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toast;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class PlayScreen extends AppCompatActivity implements View.OnClickListener {

    final int ZERO = 0, STEP = 350, END = 700, MOVE_SPIKE = 30, LIFE = 3;
    private Random random;
    Vibrator vib;
    int divideScreen[] = {ZERO, STEP, END};
    //image
    private ImageView car, spike1, spike2,heart1,heart2,heart3;
    //position
    private float spike1X, spike1Y, spike2X, spike2Y;
    private float carX;
    //score && life
    /*To HW2:private TextView scoreText, highScoreText;*/
    private int /*To HW2: score, timeCount,*/ lifes;
    // time
    private Timer timer;
    private Handler handler = new Handler();
    //status
    private boolean playingNow = false, actionLeftFlag = false, actionRightFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_screen);
        // Call object
        car = findViewById(R.id.car);
        spike1 = findViewById(R.id.spike1);
        spike2 = findViewById(R.id.spike2);
        /*To HW2: scoreText = findViewById(R.id.score);*/
        heart1 = findViewById(R.id.heart1);
        heart2 = findViewById(R.id.heart2);
        heart3 = findViewById(R.id.heart3);
        findViewById(R.id.left).setOnClickListener(this);
        findViewById(R.id.right).setOnClickListener(this);
        random = new Random();
        timer = new Timer();

        //Initial variables
        vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        playingNow = true;
        carX = car.getX() + STEP;
        spike1Y = spike1.getY();
        spike2Y = spike2.getY();
        lifes = LIFE;
        /*Tם HW2:
        timeCount = ZERO;
        score = ZERO;
        scoreText.setText(*/
        /*"Score: " + score*//*
        "OH NO OBSTACLE");
        */

        //Car Move
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (playingNow)
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePos();
                        }
                    });
            }
        }, 0, 20);

        //Spike1 Movement
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (playingNow)
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            spike1Obstacle();
                        }
                    });
            }
        }, 1, 20);

        //Spike2 Movement
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (playingNow)
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            spike2Obstacle();
                        }
                    });
            }
        }, 1, 20);
    }

    @Override
    protected void onPause() {
        playingNow = false;
        super.onPause();
    }

    @Override
    protected void onResume() {
        playingNow = true;

        super.onResume();
    }

    public void changePos() {

        if (actionRightFlag) {
            carX += STEP;
            actionRightFlag = false;
        }
        if (actionLeftFlag){
            carX -= STEP;
            actionLeftFlag = false;
        }

        // Check car position
        if(carX < ZERO){
            carX = ZERO;
        }
        if(carX > STEP){
            carX = END;
        }
        car.setX(carX);
    }

    private void setLife() {
        switch (lifes){
            case 2:
                heart2.setVisibility(View.INVISIBLE);
                break;
            case 1:
                heart3.setVisibility(View.INVISIBLE);
                break;
            case ZERO:
                heart1.setVisibility(View.INVISIBLE);
                heart2.setVisibility(View.VISIBLE);
                heart3.setVisibility(View.VISIBLE);
                heart1.setVisibility(View.VISIBLE);
                lifes = LIFE;
                break;
        }
    }


    public boolean hurtCheck(float x, float y){
        return carX == x && car.getY() + 160 <= y;
    }

    public void spike1Obstacle() {
        spike1Y += MOVE_SPIKE;
        //Check zombie position
        if (spike1Y > 1500) {
            spike1Y = ZERO;
            spike1X = divideScreen[random.nextInt(divideScreen.length)];
        }

        spike1.setX(spike1X);
        spike1.setY(spike1Y);

        if (hurtCheck(spike1X, spike1Y) && spike1X != spike2X){
            lifes--;
            vibrationPhone();
            setLife();
        }
    }

    public void spike2Obstacle() {
        spike2Y += MOVE_SPIKE;

        //Check zombie position
        if(spike2Y > 1500){
            spike2Y = ZERO;
            spike2X = divideScreen[random.nextInt(divideScreen.length)];
        }

        spike2.setX(spike2X);
        spike2.setY(spike2Y);

        if(hurtCheck(spike2X, spike2Y)) {
            lifes--;
            vibrationPhone();
            setLife();
        }
    }

    public void vibrationPhone ( ){
        if (Build.VERSION.SDK_INT >= 26) {
            vib.vibrate((VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE)));
            if (lifes == ZERO)
                vib.vibrate((VibrationEffect.createOneShot(350, VibrationEffect.DEFAULT_AMPLITUDE)));
        }else{
            vib.vibrate(150);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.left:
                actionLeftFlag = true;
                break;
            case R.id.right:
                actionRightFlag = true;
                break;
            default:
                actionLeftFlag = false;
                actionRightFlag = false;
                break;
        }
    }

}

