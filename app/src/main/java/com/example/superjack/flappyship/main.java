package com.example.superjack.flappyship;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Timer;
import java.util.TimerTask;

public class main extends AppCompatActivity {

    // XML params
    private TextView scoreView;
    private TextView startView;
    private ImageView shipView;
    private ImageView asteroidView;
    private ImageView starView;
    private ImageView heartView;

    //Size
    private int frameHeight;
    private int shipSize;
    private int screenWidth;
    private int screenHeight;

    private int score = 0;

    //Position
    private int shipY;
    private int starX;
    private int starY;
    private int heartX;
    private int heartY;
    private int asteroidX;
    private int asteroidY;

    //Status Check
    private boolean action_flg = false;
    private boolean start_flg = false;

    //Classes
    private Handler handler = new Handler();
    private Timer timer = new Timer();
    private SoundPlayer sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sound = new SoundPlayer(this);

        scoreView = (TextView) findViewById(R.id.scoreView);
        startView = (TextView) findViewById(R.id.startView);
        shipView = (ImageView) findViewById(R.id.shipView);
        asteroidView = (ImageView) findViewById(R.id.asteroidView);
        starView = (ImageView) findViewById(R.id.starView);
        heartView = (ImageView) findViewById(R.id.heartView);

        /******************************************************************************************/
        //Get ekran
        WindowManager wm = getWindowManager();
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);

        screenWidth = size.x;
        screenHeight = size.y;

        /******************************************************************************************/
        //Move to out of screen (Ekranın Dışına haydeee !!!)
        asteroidView.setY(-80);
        asteroidView.setX(-80);
        starView.setY(-80);
        starView.setY(-80);
        heartView.setX(-80);
        heartView.setY(-80);


        scoreView.setText("Score : 0");
        //Testing!!!!!!!!!!!!



    }

    public void changePos(){
        //Vuruş kontrolü
        hitCheck();

        //Star NEsnesi
        starX -=12; //Ekranın dışından gelmesi için
        if(starX < 0){
            starX = screenWidth + 20;
            starY = (int) Math.floor(Math.random() * (frameHeight - starView.getHeight()));
        }
        starView.setX(starX);
        starView.setY(starY);

        //Asteroid
        asteroidX -= 16; //Ekranın dışından gelmesi için
        if(asteroidX < 0){
            asteroidX = screenWidth + 10;
            asteroidY = (int) Math.floor(Math.random() * (frameHeight - asteroidView.getHeight()));
        }
        asteroidView.setX(asteroidX);
        asteroidView.setY(asteroidY);

        //Heart
        heartX -= 20; //Ekranın dışından gelmesi için
        if(heartX < 0){
            heartX = screenWidth + 5000;
            heartY = (int) Math.floor(Math.random() * (frameHeight - heartView.getHeight()));
        }
        heartView.setX(heartX);
        heartView.setY(heartY);


        //Moving Ship
        if (action_flg == true){
            shipY -= 20; //Clicking
        }else {
            shipY += 20; //Serbest Düşüş ??
        }

        if (shipY < 0) shipY = 0;
        if(shipY > frameHeight-shipSize) shipY = frameHeight-shipSize;

        shipView.setY(shipY);
        scoreView.setText("Score : " + score);

    }

    public void hitCheck(){
        //Kutu olarak yarattığımız için merkezlerinde oynama yapacaz

        //Star
        int starCenterX = starX + starView.getWidth()/2;
        int starCenterY = starY + starView.getHeight()/2;

        //0 <= starCenterX <= shipWidth
        //shipY <= starCenterY <= shipY + shipheight @stuckoverflow!!!

        if(0 <= starCenterX && starCenterX <= shipSize && shipY <= starCenterY && starCenterY <= shipY + shipSize){
            score += 10;
            starX = -10;
            sound.playHitSound();
        }

        //Asteroid
        int asCenterX = asteroidX + asteroidView.getWidth()/2;
        int asCenterY = asteroidY + asteroidView.getHeight()/2;

        if(0 <= asCenterX && asCenterX <= shipSize && shipY <= asCenterY && asCenterY <= shipY + shipSize){
            //Timeri durdur
            timer.cancel();
            timer = null;
            sound.playOverSound();

            //Show the score
            Intent intent = new Intent(getApplicationContext(), result.class);
            intent.putExtra("SCORE", score);
            startActivity(intent);
        }

        //Heart
        int heartCenterX = heartX + heartView.getWidth()/2;
        int heartCenterY = heartY + heartView.getHeight()/2;

        if(0 <= heartCenterX && heartCenterX <= shipSize && shipY <= heartCenterY && heartCenterY <= shipY + shipSize){
            score += 30;
            heartX = -10;
            sound.playHitSound();
        }



    }

    public boolean onTouchEvent(MotionEvent me) {
        if (start_flg == false) {

            start_flg = true;

            FrameLayout frame = (FrameLayout) findViewById(R.id.frameLay);
            frameHeight = frame.getHeight(); //Get frame height from R.id.framelay

            shipY = (int) shipView.getY();
            shipSize = shipView.getHeight();

            startView.setVisibility(View.GONE);

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePos(); //changing position
                        }
                    });
                }
            }, 0, 20);

        } else {

            if (me.getAction() == MotionEvent.ACTION_DOWN) {
                action_flg = true;
            } else if (me.getAction() == MotionEvent.ACTION_UP) {
                action_flg = false;
            }
        }
        return true;
    }

    //Geri tuşunu disable yapma
    @Override
    public boolean dispatchKeyEvent(KeyEvent event){
        if (event.getAction() == KeyEvent.ACTION_DOWN){
            switch (event.getKeyCode()){
                case KeyEvent.KEYCODE_BACK:
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
