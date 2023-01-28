package com.example.hw1application;

import android.os.Build;
import java.util.ArrayList;
import java.util.List;

public class LeaderBoard {

    private List<Score> scores;

    public LeaderBoard() {
        scores = new ArrayList<>();
    }

    public List<Score> getScores() {
        return scores;
    }

    public Score addScore(Score tempScore) {

        if(isFull()) {
            if(!isBetter(tempScore.getScore())) {
                return null;
            }
            else {
                scores.remove(9);
                scores.add(tempScore);
                sortScores();
                return tempScore;
            }
        }
        else {
            scores.add(tempScore);
            if(scores.size() > 1) {
                sortScores();
            }
        }
        return tempScore;

    }

    private boolean isBetter(int scoreOfTempScore) {
        return scoreOfTempScore > scores.get(9).getScore();
    }

    private boolean isFull() {
        return scores.size() == 10;
    }

    private void sortScores() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            scores.sort((score1, score2) -> score2.getScore() - score1.getScore());
        }
    }
}
