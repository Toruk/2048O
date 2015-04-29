package fr.dlorine.android2048o;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Game {
    private static String SAVE_PATH = "current_game";
    private static int SIZE = 4;
    ArrayList<Integer> mCells;
    private boolean mWon;
    private boolean mOver;
    private Integer mScore;

    static private ArrayList<Integer> rotateMatrixRight(ArrayList<Integer> matrix) {
        ArrayList<Integer> ret = new ArrayList<Integer>(matrix);

        for (int i = 0; i < SIZE; ++i) {
            for (int j = 0; j < SIZE; ++j) {
                ret.set(i * SIZE + j, matrix.get((SIZE - j - 1) * SIZE + i));
            }
        }

        return ret;
    }

    public Game() {
        mCells = new ArrayList<>();
        startGame();
    }

    private int getRandomEmptyCell() {
        ArrayList<Integer> tiles = new ArrayList<Integer>();
        for (int i = 0; i < SIZE*SIZE; i++) {
            if (mCells.get(i) == 0)
                tiles.add(i);
        }

        if (tiles.size() <= 0)
            return -1;

        return tiles.get(new Random().nextInt(tiles.size()));
    }

    public void startGame() {
        mCells.clear();
        for (Integer i = 0; i < SIZE * SIZE; i++)
            mCells.add(0);

        mWon = false;
        mOver = false;
        mScore = 0;
        mCells.set(getRandomEmptyCell(), 2);
        mCells.set(getRandomEmptyCell(), (new Random().nextInt(10) == 9) ? 4 : 2);
    }

    // finds if two adjacent tiles can be merged
    private boolean tileMatchesAvailable() {
        boolean ret = false;

        for (int dir = 0; dir < SIZE; dir++) {
            mCells = rotateMatrixRight(mCells);
            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < SIZE; x++) {
                    int value = mCells.get(y * SIZE + x);
                    if (value > 0 && mCells.get((y+1)*SIZE+x) == value) {
                        ret = true;
                    }
                }
            }
        }
        return ret;
    }

    public void play(int dir) {
        if (mOver || mWon) return;

        int original_dir = dir;

        // Transform the matrix to swipe up
        while (dir != 0) {
            mCells = rotateMatrixRight(mCells);
            dir = (dir + 1) % 4;
        }

        // merge mask
        ArrayList<Boolean> merged = new ArrayList<Boolean>(16);
        for (int i = 0; i < 16; i++)
            merged.add(false);

        boolean moved = false;
        // iterate over the map
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                int value = mCells.get(y*SIZE+x);

                if (value > 0) {
                    int cell = y;
                    int previous;
                    int nextValue = value;

                    // fetch the farthest cell
                    do {
                        previous = cell;
                        cell = previous - 1;
                    } while (cell >= 0 && (nextValue = mCells.get(cell * SIZE + x)) == 0);

                    // if we can merge with it, merge!
                    if (cell >= 0 && value == nextValue && !merged.get(cell * SIZE + x)) {
                        int newValue = value * 2;
                        mCells.set(y * SIZE + x, 0);
                        mCells.set(cell * SIZE + x, newValue);
                        merged.add(cell * SIZE + x, true);

                        mScore += newValue;

                        // Victory condition
                        if (newValue == 2048) {
                            mWon = true;
                        }
                        else
                            moved = true;

                    // no merge, but we can move it
                    } else if (y != previous) {
                        mCells.set(y * SIZE + x, 0);
                        mCells.set(previous * SIZE + x, value);
                        moved = true;
                    }

                }
            }
        }

        // restore the original game orientation
        while (dir != original_dir) {
            mCells = rotateMatrixRight(mCells);
            dir = (dir + 1) % 4;
        }

        // if the played has moved a tiles during this, generate a new one
        if (moved) {
            int new_tile = getRandomEmptyCell();
            mCells.set(new_tile, 2);
            if (!(getRandomEmptyCell() >= 0 || tileMatchesAvailable())) {
                this.mOver = true;
            }
        }
    }

    public Integer getTileNumber(int idx) {
        return mCells.get(idx);
    }

    public int getSize() {
        return SIZE * SIZE;
    }

    public boolean getWon() { return mWon; }

    public boolean getOver() { return mOver; }

    public Integer getScore() { return mScore; }

    public void save(Context ctx) {
        FileOutputStream outputStream = null;
        String saveData = mScore+":";

        for (Integer cell : mCells) saveData += cell + ":";
        saveData = saveData.substring(0, saveData.length()-1);

        try {
            outputStream = ctx.openFileOutput(SAVE_PATH, Context.MODE_PRIVATE);
            outputStream.write(saveData.getBytes());
            outputStream.close();
        } catch (Exception e) {
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
            failure = true;
        }

        ArrayList<String> cells = new ArrayList<>(Arrays.asList(content.split(":")));
        if (cells.size() == 1 + SIZE * SIZE) {
            mScore = Integer.parseInt(cells.get(0));
            for (int idx = 1; idx < cells.size(); idx++)
                mCells.set(idx-1,Integer.parseInt(cells.get(idx)));
        } else {
            failure = true;
        }

        if (failure) {
            // We couldn't load the game. Start a new one.
            startGame();
            save(ctx);
        }

    }
}
