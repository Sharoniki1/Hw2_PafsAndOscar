package com.example.hw1application;

public class Score {

    private int score;
    private double latitude;
    private double longitude;


    public Score(){}

    public int getScore() {
        return score;
    }

    public Score setScore(int score) {
        this.score = score;
        return this;
    }

    public double getLongitude() {
        return longitude;
    }

    public Score setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    public double getLatitude() {
        return latitude;
    }

    public Score setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    @Override
    public String toString() {
        return "score=" + score +
                ", lat=" + latitude +
                ", lon=" + longitude;
    }
}
