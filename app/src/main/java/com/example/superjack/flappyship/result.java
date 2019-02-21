package com.example.superjack.flappyship;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class result extends AppCompatActivity {

    private Button exitBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView scoreView = (TextView) findViewById(R.id.scoreView);
        TextView highScoreView = (TextView) findViewById(R.id.highScoreView);
        exitBtn = (Button) findViewById(R.id.exitBtn);

        int score = getIntent().getIntExtra("SCORE", 0);
        scoreView.setText(score + "");
        SharedPreferences settings = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        int highScore = settings.getInt("HIGH_SCORE", 0);

        if(score > highScore){
            highScoreView.setText("High Score : " + score);

            //Save!!!!
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("HIGH_SCORE", score);
            editor.commit();
        }else {
            highScoreView.setText("High Score : " + highScore);
        }
    }

    public void tryAgain(View view){
        startActivity(new Intent(getApplicationContext(), start.class));
    }

    public void exit(View view){
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }


    //Back button disable
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
