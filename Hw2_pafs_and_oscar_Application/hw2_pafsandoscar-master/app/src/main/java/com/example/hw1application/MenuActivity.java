package com.example.hw1application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;

public class MenuActivity extends AppCompatActivity {

    private ShapeableImageView menu_IMG_background;
    private MaterialButton menu_BTN_buttonsmode;
    private MaterialButton menu_BTN_sensorsmode;
    private MaterialButton menu_BTN_leaderboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        findViews();
        initViews();

        Glide
                .with(MenuActivity.this)
                .load("https://pbs.twimg.com/profile_images/988919457622917121/cJVC3J7q_400x400.jpg")
                .into(menu_IMG_background);



    }

    private void initViews() {
        menu_BTN_buttonsmode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameClicked(1);
            }
        });
        menu_BTN_sensorsmode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameClicked(-1);
            }
        });
        menu_BTN_leaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leaderboardClicked();
            }
        });

    }

    private void findViews() {
        menu_IMG_background = findViewById(R.id.menu_IMG_background);
        menu_BTN_buttonsmode  = findViewById(R.id.menu_BTN_buttonsmode);
        menu_BTN_sensorsmode = findViewById(R.id.menu_BTN_sensorsmode);
        menu_BTN_leaderboard = findViewById(R.id.menu_BTN_leaderboard);
    }

    void gameClicked(int value) {
        Intent intent = new Intent(this, Game_Activity.class);
        intent.putExtra(Game_Activity.KEY_MODE, value);
        startActivity(intent);
        finish();

    }

    void leaderboardClicked() {
        Intent intent = new Intent(this, LeaderBoardActivity.class);
        startActivity(intent);
    }

}