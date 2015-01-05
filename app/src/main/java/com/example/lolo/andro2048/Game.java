package com.example.lolo.andro2048;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by lolo on 1/4/2015.
 */
public class Game {
    private ArrayList<Integer> mGame;

    public Game() {
        mGame = new ArrayList<Integer>();
        for (int i = 0; i < 16; i++) {
            mGame.add(i, 0);
        }
        startGame();
    }

    private int getRandomValue(ArrayList<Integer> valueSet) {
        Random r = new Random();
        return valueSet.get(r.nextInt(valueSet.size()));
    }

    private int getRandomEmptyTile() {
        ArrayList<Integer> tiles = new ArrayList<Integer>();
        for (int i = 0; i < mGame.size(); i++) {
            if (mGame.get(i) == 0)
                tiles.add(i);
        }

        int position = getRandomValue(tiles);
        return position;
    }

    private int getRandomStarter() {
        return getRandomValue(new ArrayList<Integer>(Arrays.asList(2, 4)));
    }

    private void startGame() {
        mGame.set(getRandomEmptyTile(), 2);
        mGame.set(getRandomEmptyTile(), getRandomStarter());
    }

    public void collapseHorizontally() {

    }

    public void collapseVertically() {

    }

    public Integer getTileNumber(int position) {
        return mGame.get(position);
    }

    public int getSize() {
        return 16;
    }
}
