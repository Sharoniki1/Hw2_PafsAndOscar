package com.example.hw1application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;

import com.bumptech.glide.Glide;
import com.google.android.material.textview.MaterialTextView;

public class Game_Activity extends AppCompatActivity {

    public static final String KEY_MODE = "KEY_MODE";

    private ShapeableImageView game_IMG_background;
    private ShapeableImageView[] game_IMG_allhearts;
    private ShapeableImageView[][] game_IMG_allpafs;
    private ShapeableImageView[] game_IMG_alloscars;
    private ExtendedFloatingActionButton game_BTN_left;
    private ExtendedFloatingActionButton game_BTN_right;
    private ShapeableImageView[][] game_BTN_allicecreams;
    private MaterialTextView game_LBL_score;

    private StepDetector stepDetector;

    final int DELAY=1000;
    final Handler handler = new Handler();
    final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(runnable, DELAY);
            move(game_IMG_allpafs, 2, gameManager.getAllPafs());
            move(game_BTN_allicecreams, 4, gameManager.getAllIceCreams());
            refreshUI();
        }
    };

    private void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
    }

    Game_Manager gameManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        findViews();

        Intent previousIntent = getIntent();
        int mode = previousIntent.getExtras().getInt(KEY_MODE);
        if(mode > 0) { // if in buttons mode
            initViews();
        }
        else {
            game_BTN_left.setVisibility(View.INVISIBLE);
            game_BTN_right.setVisibility(View.INVISIBLE);

            stepDetector = new StepDetector(this, callBack_steps);
        }

        Glide
                .with(Game_Activity.this)
                .load("https://cdn3.vectorstock.com/i/1000x1000/00/17/underwater-landscape-the-ocean-and-the-undersea-vector-10230017.jpg")
                .into(game_IMG_background);

        gameManager = new Game_Manager(
                game_IMG_allhearts.length,
                game_IMG_allpafs.length,
                game_IMG_alloscars.length);

        initGame();

        startTimer();
    }

    private StepDetector.CallBack_Steps callBack_steps = new StepDetector.CallBack_Steps() {
      public void oneStep(int indication) {
          clicked(indication);
      }
    };


    private void move(ShapeableImageView[][] objectsMatrix, int forLocation, GameObject[] gameObjects)
    {for(int i = 0; i<gameManager.getNumber_of_columns(); i++){
        if(gameManager.isInArray(gameObjects[i].getLocation()))
            objectsMatrix[gameObjects[i].getLocation()][i].setVisibility(View.INVISIBLE);
    }

    gameManager.move(gameObjects, forLocation);
        for(int i = 0; i<gameManager.getNumber_of_columns(); i++) {if(gameManager.isInArray(gameObjects[i].getLocation()))
            objectsMatrix[gameObjects[i].getLocation()][i].setVisibility(View.VISIBLE);
        }

    }

    private void initGame(){
        game_IMG_alloscars[gameManager.getOscarIndex().getLocation()].setVisibility(View.VISIBLE);
    }

    private void initViews() {

        game_BTN_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked(-1);
            }
        });

        game_BTN_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked(1);
            }
        });
    }

    private void clicked(int indication) {
        game_IMG_alloscars[gameManager.getOscarIndex().getLocation()].setVisibility(View.INVISIBLE);
        gameManager.moveOscars(indication);
        game_IMG_alloscars[gameManager.getOscarIndex().getLocation()].setVisibility(View.VISIBLE);

        refreshUI();
    }

    private void refreshUI() {

        if(gameManager.isCrash()){
            updateCrash();
        }

        if(gameManager.isGameOver()){
            updateGameOver();
        }

        if(gameManager.isCatch()) {
            updateCatch();
        }
    }


    private void updateCatch() {
        MySignal.getInstance().toast("Yamiiii");
        game_LBL_score.setText(gameManager.getScore() +"");
    }

    private void updateGameOver() {
        MySignal.getInstance().toast("Game Over!!!");
        stopTimer();
        //TODO delay
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showLeaderBoard();
            }
        }, DELAY+1500);
    }

    private void showLeaderBoard() {
        Intent intent = new Intent(this, LeaderBoardActivity.class);
        intent.putExtra(LeaderBoardActivity.KEY_SCORE,gameManager.getScore());
        startActivity(intent);
        finish();
    }


    private void updateCrash() {
        game_IMG_allhearts[gameManager.getLife()].setVisibility(View.INVISIBLE);
        MySignal.getInstance().vibrate();
        if(gameManager.getLife()!=0) {
            MySignal.getInstance().toast("loserrrr");
            crash();
        }

    }

    private void startTimer(){
        handler.postDelayed(runnable,DELAY);
    }

    private void stopTimer(){
        handler.removeCallbacks(runnable);
    }

    private void findViews() {

        game_IMG_background = findViewById(R.id.game_IMG_background);
        game_IMG_allhearts = new ShapeableImageView[]{
                findViewById(R.id.game_IMG_heart1),
                findViewById(R.id.game_IMG_heart2),
                findViewById(R.id.game_IMG_heart3)};

        game_IMG_allpafs = new ShapeableImageView[][]{
                {findViewById(R.id.game_IMG_obstaclepirane1),
                        findViewById(R.id.game_IMG_obstaclepirane2),
                        findViewById(R.id.game_IMG_obstaclepirane3),
                        findViewById(R.id.game_IMG_obstaclepirane1a),
                        findViewById(R.id.game_IMG_obstaclepirane2a)},
                {findViewById(R.id.game_IMG_obstaclepirane4),
                        findViewById(R.id.game_IMG_obstaclepirane5),
                        findViewById(R.id.game_IMG_obstaclepirane6),
                        findViewById(R.id.game_IMG_obstaclepirane1b),
                        findViewById(R.id.game_IMG_obstaclepirane2b)},
                {findViewById(R.id.game_IMG_obstaclepirane7),
                        findViewById(R.id.game_IMG_obstaclepirane8),
                        findViewById(R.id.game_IMG_obstaclepirane9),
                        findViewById(R.id.game_IMG_obstaclepirane1c),
                        findViewById(R.id.game_IMG_obstaclepirane2c)},
                {findViewById(R.id.game_IMG_obstaclepirane10),
                        findViewById(R.id.game_IMG_obstaclepirane11),
                        findViewById(R.id.game_IMG_obstaclepirane12),
                        findViewById(R.id.game_IMG_obstaclepirane13),
                        findViewById(R.id.game_IMG_obstaclepirane14)},
                {findViewById(R.id.game_IMG_obstaclepirane15),
                        findViewById(R.id.game_IMG_obstaclepirane16),
                        findViewById(R.id.game_IMG_obstaclepirane17),
                        findViewById(R.id.game_IMG_obstaclepirane18),
                        findViewById(R.id.game_IMG_obstaclepirane19)}
        };

        game_IMG_alloscars = new ShapeableImageView[]{
                findViewById(R.id.game_IMG_oscar1),
                findViewById(R.id.game_IMG_oscar2),
                findViewById(R.id.game_IMG_oscar3),
                findViewById(R.id.game_IMG_oscar4),
                findViewById(R.id.game_IMG_oscar5)

        };

        game_BTN_left = findViewById(R.id.game_BTN_left);
        game_BTN_right = findViewById(R.id.game_BTN_right);

        game_BTN_allicecreams = new ShapeableImageView[][] {
                {findViewById(R.id.game_IMG_icecream1),
                        findViewById(R.id.game_IMG_icecream2),
                        findViewById(R.id.game_IMG_icecream3),
                        findViewById(R.id.game_IMG_icecream1a),
                        findViewById(R.id.game_IMG_icecream2a)},
                {findViewById(R.id.game_IMG_icecream4),
                        findViewById(R.id.game_IMG_icecream5),
                        findViewById(R.id.game_IMG_icecream6),
                        findViewById(R.id.game_IMG_icecream1b),
                        findViewById(R.id.game_IMG_icecream2b)},
                {findViewById(R.id.game_IMG_icecream7),
                        findViewById(R.id.game_IMG_icecream8),
                        findViewById(R.id.game_IMG_icecream9),
                        findViewById(R.id.game_IMG_icecream1c),
                        findViewById(R.id.game_IMG_icecream2c)},
                {findViewById(R.id.game_IMG_icecream10),
                        findViewById(R.id.game_IMG_icecream11),
                        findViewById(R.id.game_IMG_icecream12),
                        findViewById(R.id.game_IMG_icecream13),
                        findViewById(R.id.game_IMG_icecream14)},
                {findViewById(R.id.game_IMG_icecream15),
                        findViewById(R.id.game_IMG_icecream16),
                        findViewById(R.id.game_IMG_icecream17),
                        findViewById(R.id.game_IMG_icecream18),
                        findViewById(R.id.game_IMG_icecream19)}
        };

        game_LBL_score = findViewById(R.id.game_LBL_score);
    }

    @Override
    protected void onPause(){
        super.onPause();
        stopTimer();
        if(stepDetector != null)
            stepDetector.stop();
    }

    @Override
    protected void onResume(){
        super.onResume();
        startTimer();
        if(stepDetector != null)
            stepDetector.start();
    }

    public void crash(){
        CrashSound crashSound = new CrashSound();
        crashSound.execute();
    }

    public class CrashSound extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            MediaPlayer player = MediaPlayer.create(Game_Activity.this, R.raw.msc_crash);
            player.setVolume(1.0f, 1.0f);
            player.start();
            return null;
        }
    }
}