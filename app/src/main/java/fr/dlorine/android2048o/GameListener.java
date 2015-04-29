package fr.dlorine.android2048o;


public abstract interface GameListener {
    abstract void onScoreUpdate();
    abstract void onWin();
    abstract void onLose();
}
