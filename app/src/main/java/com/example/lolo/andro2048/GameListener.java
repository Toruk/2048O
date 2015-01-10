package com.example.lolo.andro2048;


public abstract interface GameListener {
    abstract void onScoreUpdate();
    abstract void onWin();
    abstract void onLose();
}
