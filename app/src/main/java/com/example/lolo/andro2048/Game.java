package com.example.lolo.andro2048;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class Game {
    private static String SAVE_PATH = "current_game";
    private static int SIZE = 4;
    ArrayList<Integer> mGame;
    private boolean mWon;
    private boolean mOver;
    private Integer mScore;

    public Game() {
        mGame = new ArrayList<Integer>();
        startGame();
    }

    static private ArrayList<Integer> rotateMatrixRight(ArrayList<Integer> matrix) {
        ArrayList<Integer> ret = new ArrayList<Integer>(matrix);

        for (int i = 0; i < SIZE; ++i) {
            for (int j = 0; j < SIZE; ++j) {
                ret.set(i * SIZE + j, matrix.get((SIZE - j - 1) * SIZE + i));
            }
        }

        return ret;
    }

    private static void printMatrix(ArrayList<Integer> matrix) {
        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++)
                System.err.print(matrix.get(y * SIZE + x) + " ");
            System.err.println("");
        }
    }

    private int getRandomEmptyTile() {
        ArrayList<Integer> tiles = new ArrayList<Integer>();
        for (int i = 0; i < SIZE*SIZE; i++) {
            if (mGame.get(i) == 0)
                tiles.add(i);
        }

        if (tiles.size() <= 0)
            return -1;
        return tiles.get(new Random().nextInt(tiles.size()));
    }

    public void startGame() {
        mGame.clear();
        for (Integer i = 0; i < SIZE*SIZE; i++)
            mGame.add(0);

        mWon = false;
        mOver = false;
        mScore = 0;
        mGame.set(getRandomEmptyTile(), 2);
        mGame.set(getRandomEmptyTile(), (new Random().nextInt(10) == 9) ? 4 : 2);
    }

    private boolean tileMatchesAvailable() {
        boolean ret = false;

        for (int dir = 0; dir < SIZE; dir++) {
            mGame = rotateMatrixRight(mGame);
            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < SIZE; x++) {
                    int value = mGame.get(y * SIZE + x);
                    if (value > 0 && mGame.get((y+1)*SIZE+x) == value) {
                        ret = true;
                    }
                }
            }
        }
        return ret;
    }

    public void play(int dir) {
        if (mOver || mWon) return; // Don't do anything if the game's over

        int original_dir = dir;

        while (dir != 0) {
            mGame = rotateMatrixRight(mGame);
            dir = (dir + 1) % 4;
        }

        ArrayList<Boolean> merged = new ArrayList<Boolean>(16);
        for (int i = 0; i < 16; i++)
            merged.add(false);

        boolean moved = false;
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                int value = mGame.get(y*SIZE+x);
                if (value > 0) {
                    int cell = y;
                    int previous;
                    int nextValue = value;
                    do {
                        previous = cell;
                        cell = previous - 1;
                    } while (cell >= 0 && (nextValue = mGame.get(cell * SIZE + x)) == 0);

                    if (cell >= 0 && value == nextValue && !merged.get(cell * SIZE + x)) {
                        int newValue = value * 2;
                        mGame.set(y*SIZE+x, 0);
                        mGame.set(cell*SIZE+x, newValue);
                        merged.add(cell * SIZE + x, true);

                        mScore += newValue;

                        if (newValue == 2048) {
                            mWon = true;
                            System.err.println("you win!");
                        }
                        else
                            moved = true;

                    } else if (y != previous) {
                        mGame.set(y*SIZE+x, 0);
                        mGame.set(previous * SIZE + x, value);
                        moved = true;
                    }

                }
            }
        }

        while (dir != original_dir) {
            mGame = rotateMatrixRight(mGame);
            dir = (dir + 1) % 4;
        }

        if (moved) {
            int new_tile = getRandomEmptyTile();
            mGame.set(new_tile, 2);
            if (!(getRandomEmptyTile() >= 0 || tileMatchesAvailable())) {
                this.mOver = true; // Game over!
                System.err.println("over");
            }
        }
    }

    public Integer getTileNumber(int idx) {
        return mGame.get(idx);
    }

    public int getSize() {
        return SIZE;
    }

    public Integer getScore() { return mScore; }

    public void save(Context ctx) {
        FileOutputStream outputStream = null;
        String saveData = "";

        for (Integer cell : mGame) saveData += cell + ":";
        saveData = saveData.substring(0, saveData.length()-1);

        try {
            outputStream = ctx.openFileOutput(SAVE_PATH, Context.MODE_PRIVATE);
            outputStream.write(saveData.getBytes());
            System.err.println("same saved " + saveData);
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
            System.err.println("game loaded");
            fis.close();
        } catch (IOException e) {
            // We couldn't load the previous game. Start a new one.
            failure = true;
            System.err.println("io load error");
        }

        String[] cells = content.split(":");
        if (cells.length == SIZE * SIZE){
            int idx = 0;
            for (String s:cells) {
                mGame.set(idx,Integer.parseInt(s));
                idx++;
            }
        } else {
            failure = true;
            System.err.println("bad save file");
        }

        if (failure) {
            startGame();
            save(ctx);
        }

    }
}
