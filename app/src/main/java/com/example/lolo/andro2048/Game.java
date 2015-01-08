package com.example.lolo.andro2048;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Game {

    private static String SAVE_PATH = "current_game";
    private ArrayList<Integer> mGame;
    private static final int SIZE = 16;

    public Game() {
        mGame = new ArrayList<Integer>(16);
        startGame();
    }

    private int getRandomEmptyTile() {
        ArrayList<Integer> tiles = new ArrayList<Integer>();
        for (int i = 0; i < SIZE; i++) {
            if (mGame.get(i) == 0)
                tiles.add(i);
        }

        return tiles.get(new Random().nextInt(tiles.size()));
    }

    public void startGame() {
        mGame.clear();
        for (Integer i = 0; i < SIZE; i++)
            mGame.add(0);
        mGame.set(getRandomEmptyTile(), 2);
        mGame.set(getRandomEmptyTile(), (new Random().nextInt(2)+1)*2);
    }

    public void collapseTop() {

    }

    public void collapseBottom() {

    }

    public void collapseRight() {

    }

    public void collapseLeft() {

    }

    public Integer getTileNumber(int idx) {
        return mGame.get(idx);
    }

    public int getSize() {
        return SIZE;
    }

    public void save(Context ctx) {
        FileOutputStream outputStream = null;
        String saveData = "";

        for (Integer cell : mGame) saveData += cell + ":";
        saveData = saveData.substring(0, saveData.length()-1);

        try {
            outputStream = ctx.openFileOutput(SAVE_PATH, Context.MODE_PRIVATE);
            outputStream.write(saveData.getBytes());
            System.err.println("saved " + saveData);
            outputStream.close();
        } catch (Exception e) {
            System.err.println("io save error");
        }
    }

    public void load(Context ctx) {
        Boolean failure = false;
        String content = "";

        try {
            FileInputStream fis = ctx.openFileInput(SAVE_PATH);
            byte[] buffer = new byte[(int) fis.getChannel().size()];
            fis.read(buffer);
            for (byte b:buffer) content += (char)b;
            fis.close();
        } catch (IOException e) {
            // We couldn't load the previous game. Start a new one.
            failure = true;
            System.err.println("io load error");
        }

        String[] cells = content.split(":");
        if (cells.length == getSize()){
            int idx = 0;
            for (String s:cells) {
                mGame.set(idx,Integer.parseInt(s));
                idx++;
            }
        } else {
            failure = true;
            System.err.println("bad save format");
        }

        if (failure) {
            startGame();
            save(ctx);
        }

    }
}
