package com.example.hw1application;

public class Game_Manager {

    private int life;
    private int number_of_rows;
    private int number_of_columns;
    private Oscar oscarIndex;
    private Paf[] allPafs;
    private IceCream[] allIceCreams;
    private int score;


    public Game_Manager(int life, int number_of_rows, int number_of_columns){
        this.life= life;
        this.number_of_rows = number_of_rows;
        this.number_of_columns = number_of_columns;
        this.oscarIndex = new Oscar().setLocation(number_of_columns/2);
        allPafs = new Paf[number_of_columns];
        allIceCreams = new IceCream[number_of_columns];
        this.score = 0;

        for(int i=0; i<number_of_columns;i++)
        {
            allPafs[i] = new Paf().setLocation(-i-3);
            allIceCreams[i] = new IceCream().setLocation(-i-5);
        }
    }

    public int getLife() {
        return life;
    }

    public Paf[] getAllPafs(){
        return allPafs;
    }

    public IceCream[] getAllIceCreams() {
        return allIceCreams;
    }

    public Oscar getOscarIndex(){
        return oscarIndex;
    }

    public int getNumber_of_columns(){
        return number_of_columns;
    }

    public boolean isInArray(int index) {
        return index>=0 && index< number_of_rows;
    }

    public void moveOscars(int indication) {
        int newLocation = oscarIndex.getLocation() + indication;
        if(newLocation >= 0 && newLocation < number_of_columns) {
            oscarIndex.setLocation(newLocation);
        }
    }

    public void move(GameObject[] fallingObjects, int forLocation)
    {
        for(int i = 0; i< number_of_columns; i++){
            if(fallingObjects[i].getLocation() < number_of_rows)
                fallingObjects[i].setLocation(fallingObjects[i].getLocation()+1);
            else
                fallingObjects[i].setLocation(-i-forLocation);
        }
    }

    public boolean isCrash() {
        if(allPafs[oscarIndex.getLocation()].getLocation() == number_of_rows){
            life--;
            return true;
        }
        return false;
    }

    public boolean isGameOver() {
        return life==0;
    }

    public boolean isCatch() {
        if(allIceCreams[oscarIndex.getLocation()].getLocation() == number_of_rows) {
            score += allIceCreams[oscarIndex.getLocation()].getBONUS();
            return true;
        }
        return false;
    }
    public int getScore() {
        return score;
    }
}
