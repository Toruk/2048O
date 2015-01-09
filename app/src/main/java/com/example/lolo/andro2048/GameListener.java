package com.example.lolo.andro2048;

/**
 * Created by lolo on 1/9/2015.
 */
public abstract interface GameListener {
    abstract void onScoreUpdate();
    abstract void onWin();
    abstract void onLose();
}
