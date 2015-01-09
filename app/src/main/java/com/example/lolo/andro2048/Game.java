package com.example.lolo.andro2048;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class Game implements SwipeHandler {
    private static String SAVE_PATH = "current_game";
    ArrayList<Integer> mGame;
    private boolean mWon;
    private boolean mOver;
    private int[][] mVectors = {{0,-1},{1,0},{0,1},{-1,0}};

    public Game() {
        mGame = new ArrayList<Integer>(16);
        startGame();
    }

    private int getRandomEmptyTile() {
        ArrayList<Integer> tiles = new ArrayList<Integer>();
        for (int i = 0; i < 16; i++) {
            if (mGame.get(i) == 0)
                tiles.add(i);
        }

        return tiles.get(new Random().nextInt(tiles.size()));
    }

    public void startGame() {
        mGame.clear();
        for (Integer i = 0; i < 16; i++)
            mGame.add(0);

        mWon = false;
        mOver = false;
        mGame.set(getRandomEmptyTile(), 2);
        mGame.set(getRandomEmptyTile(), (new Random().nextInt(10) == 9) ? 4 : 2);
    }
/*
    GameManager.prototype.findFarthestPosition(GameCell cell, vector) {
        var previous;
// Progress towards the vector direction until an obstacle is found
        do {
            previous = cell;
            cell = { x: previous.x + vector.x, y: previous.y + vector.y };
        } while (this.grid.withinBounds(cell) &&
                this.grid.cellAvailable(cell));
        return {
                farthest: previous,
                next: cell // Used to check if a merge is required
        };
    };
*/
    public void onSwipe(int dir) {
        System.out.println("swipe in direction " + dir);
        if (mOver || mWon) return; // Don't do anything if the game's over
/*
        var cell, tile;
        int[] vector = mVectors[dir];
        var traversals = this.buildTraversals(vector);
        var moved = false;
// Save the current tile positions and remove merger information
        this.prepareTiles();
// Traverse the grid in the right direction and move tiles
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                if (mGame.get(y*4+x) > 0) {

                }
            }
        }
        traversals.x.forEach(function (x) {
            traversals.y.forEach(function (y) {
                cell = { x: x, y: y };
                tile = self.grid.cellContent(cell);
                if (tile) {
                    var positions = self.findFarthestPosition(cell, vector);
                    var next = self.grid.cellContent(positions.next);
// Only one merger per row traversal?
                    if (next && next.value === tile.value && !next.mergedFrom) {
                        var merged = new Tile(positions.next, tile.value * 2);
                        merged.mergedFrom = [tile, next];
                        self.grid.insertTile(merged);
                        self.grid.removeTile(tile);
// Converge the two tiles' positions
                        tile.updatePosition(positions.next);
// Update the score
                        self.score += merged.value;
// The mighty 2048 tile
                        if (merged.value === 2048) self.won = true;
                    } else {
                        self.moveTile(tile, positions.farthest);
                    }
                    if (!self.positionsEqual(cell, tile)) {
                        moved = true; // The tile moved from its original cell!
                    }
                }
            });
        });

        while (dir != original_dir) {
            mGame = rotateMatrixRight(mGame);
            dir = (dir + 1) % 4;
        }

*/
    }

    public Integer getTileNumber(int idx) {
        return mGame.get(idx);
    }

    public int getSize() {
        return 16;
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
